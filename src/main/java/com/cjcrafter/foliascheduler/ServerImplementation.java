package com.cjcrafter.foliascheduler;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface ServerImplementation {

    /**
     * The plugin used to schedule tasks.
     */
    @NotNull Plugin getOwningPlugin();

    /**
     * Returns true if the current thread will be used to tick the region the location is in.
     *
     * @param location The location to check.
     * @return True if the current thread will be used to tick the region the location is in.
     */
    boolean isOwnedByCurrentRegion(@NotNull Location location);

    /**
     * Returns true if the current thread will be used to tick the region the location, and the surrounding area, is in.
     *
     * @param location The location to check.
     * @param squareRadiusChunks The radius in chunks to check, as a Chebyshev distance.
     * @return True if the current thread will be used to tick the region the location is in.
     */
    boolean isOwnedByCurrentRegion(@NotNull Location location, int squareRadiusChunks);

    /**
     * Returns true if the current thread will be used to tick the region the block is in.
     *
     * @param block The block to check.
     * @return True if the current thread will be used to tick the region the block is in.
     */
    boolean isOwnedByCurrentRegion(@NotNull Block block);

    /**
     * Returns true if the current thread will be used to tick the region the chunk is in.
     *
     * @param world The world the chunk is in.
     * @param chunkX The x-coordinate of the chunk.
     * @param chunkZ The z-coordinate of the chunk.
     * @return True if the current thread will be used to tick the region the chunk is in.
     */
    boolean isOwnedByCurrentRegion(@NotNull World world, int chunkX, int chunkZ);

    /**
     * Returns true if the current thread will be used to tick the region the chunk is in, and the surrounding area.
     *
     * @param world The world the chunk is in.
     * @param chunkX The x-coordinate of the chunk.
     * @param chunkZ The z-coordinate of the chunk.
     * @param squareRadiusChunks The radius in chunks to check, as a Chebyshev distance.
     * @return True if the current thread will be used to tick the region the chunk is in.
     */
    boolean isOwnedByCurrentRegion(@NotNull World world, int chunkX, int chunkZ, int squareRadiusChunks);

    /**
     * Returns true if the current thread will be used to tick the region the entity is in.
     *
     * @param entity The entity to check.
     * @return True if the current thread will be used to tick the region the entity is in.
     */
    boolean isOwnedByCurrentRegion(@NotNull Entity entity);

    /**
     * Returns the global region scheduler. On Folia, this will run tasks during server ticks, but separately from any
     * specific region. This is useful for tasks that don't need to be run in a specific region. On Paper/Spigot, all
     * tasks are run in the main thread.
     */
    @NotNull
    GlobalSchedulerImplementation global();

    /**
     * Returns the async scheduler. On all servers, this will run tasks asynchronously, separate from the server
     * thread(s). This is useful for I/O-bound tasks or tasks that don't need to be run in the main thread.
     */
    @NotNull AsyncSchedulerImplementation async();

    /**
     * Returns an entity scheduler. On Folia, this will run tasks in the region the entity is in, and will follow the
     * entity as it moves between regions. On Paper/Spigot, this will run tasks in the main thread.
     */
    @NotNull EntitySchedulerImplementation entity(@NotNull Entity entity);

    /**
     * Returns a region scheduler. On Folia, this will run tasks in the specified region. On Paper/Spigot, this will run
     * tasks in the main thread.
     */
    @NotNull
    RegionSchedulerImplementation region(@NotNull World world, int chunkX, int chunkZ);

    /**
     * Returns a region scheduler. On Folia, this will run tasks in the specified region. On Paper/Spigot, this will run
     * tasks in the main thread.
     */
    default @NotNull RegionSchedulerImplementation region(@NotNull Location location) {
        World world = location.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Location world cannot be null");
        }
        return region(world, location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    /**
     * Returns a region scheduler. On Folia, this will run tasks in the specified region. On Paper/Spigot, this will run
     * tasks in the main thread.
     */
    default @NotNull RegionSchedulerImplementation region(@NotNull Block block) {
        return region(block.getWorld(), block.getX() >> 4, block.getZ() >> 4);
    }

    /**
     * Returns a region scheduler. On Folia, this will run tasks in the specified region. On Paper/Spigot, this will run
     * tasks in the main thread.
     */
    default @NotNull RegionSchedulerImplementation region(@NotNull Chunk chunk) {
        return region(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }

    /**
     * Cancels all scheduled tasks that were scheduled using your {@link Plugin}
     * instance.
     *
     * <p>Note that Folia implementations will only cancel tasks scheduled using
     * the {@link GlobalSchedulerImplementation} and
     * {@link AsyncSchedulerImplementation}, ignoring any tasks scheduled using
     * the {@link EntitySchedulerImplementation} and
     * {@link RegionSchedulerImplementation}.
     *
     * @see AsyncSchedulerImplementation#cancelTasks()
     * @see GlobalSchedulerImplementation#cancelTasks()
     * @see <a href="https://github.com/CJCrafter/FoliaScheduler/issues/28">Issue #28</a>
     */
    void cancelTasks();

    /**
     * Teleports an entity to a location. If chunks need to be loaded, those chunks will be loaded
     * asynchronously. Shortcut for {@link #teleportAsync(Entity, Location, PlayerTeleportEvent.TeleportCause)}.
     *
     * @param entity The entity to teleport
     * @param location The location to teleport the entity to
     * @return A future that completes when the entity is teleported, or the teleport fails
     */
    default @NotNull CompletableFuture<Boolean> teleportAsync(@NotNull Entity entity, @NotNull Location location) {
        return teleportAsync(entity, location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    /**
     * Teleports an entity to a location. If chunks need to be loaded, those chunks will be loaded
     * asynchronously.
     *
     * <p>The returned {@link CompletableFuture future} will complete when the entity is teleported,
     * or the teleport fails. If the teleport fails, the future will complete with a value of
     * {@code false}.
     *
     * <p>Note that this method is only asynchronous on Paper servers, versions 1.13 and later. On
     * other servers, this method will be delegated to a synchronous task that executed
     * {@link Entity#teleport(Location)} in the main thread (which may lead to lag spikes).
     *
     * @param entity The entity to teleport
     * @param location The location to teleport the entity to
     * @param cause The cause of the teleport
     * @return A future that completes when the entity is teleported, or the teleport fails
     */
    @NotNull CompletableFuture<Boolean> teleportAsync(@NotNull Entity entity, @NotNull Location location, @NotNull PlayerTeleportEvent.TeleportCause cause);
}
