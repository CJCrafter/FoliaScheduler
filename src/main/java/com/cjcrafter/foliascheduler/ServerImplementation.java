package com.cjcrafter.foliascheduler;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface ServerImplementation {

    /** The plugin used to schedule tasks. */
    @NotNull Plugin getOwningPlugin();

    /**
     * Returns true if the current thread will be used to tick the region the location is in.
     *
     * @param location The location to check.
     * @return True if the current thread will be used to tick the region the location is in.
     */
    boolean isOwnedByCurrentRegion(@NotNull Location location);

    /**
     * Returns true if the current thread will be used to tick the region the location, and the
     * surrounding area, is in.
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
     * Returns true if the current thread will be used to tick the region the chunk is in, and the
     * surrounding area.
     *
     * @param world The world the chunk is in.
     * @param chunkX The x-coordinate of the chunk.
     * @param chunkZ The z-coordinate of the chunk.
     * @param squareRadiusChunks The radius in chunks to check, as a Chebyshev distance.
     * @return True if the current thread will be used to tick the region the chunk is in.
     */
    boolean isOwnedByCurrentRegion(
            @NotNull World world, int chunkX, int chunkZ, int squareRadiusChunks);

    /**
     * Returns true if the current thread will be used to tick the region the entity is in.
     *
     * @param entity The entity to check.
     * @return True if the current thread will be used to tick the region the entity is in.
     */
    boolean isOwnedByCurrentRegion(@NotNull Entity entity);

    /**
     * Returns the global region scheduler. On Folia, this will run tasks during server ticks, but
     * separately from any specific region. This is useful for tasks that don't need to be run in a
     * specific region. On Paper/Spigot, all tasks are run in the main thread.
     */
    @NotNull SchedulerImplementation global();

    /**
     * Returns the async scheduler. On all servers, this will run tasks asynchronously, separate
     * from the server thread(s). This is useful for I/O-bound tasks or tasks that don't need to be
     * run in the main thread.
     */
    @NotNull AsyncSchedulerImplementation async();

    /**
     * Returns an entity scheduler. On Folia, this will run tasks in the region the entity is in,
     * and will follow the entity as it moves between regions. On Paper/Spigot, this will run tasks
     * in the main thread.
     */
    @NotNull EntitySchedulerImplementation entity(@NotNull Entity entity);

    /**
     * Returns a region scheduler. On Folia, this will run tasks in the specified region. On
     * Paper/Spigot, this will run tasks in the main thread.
     */
    @NotNull SchedulerImplementation region(@NotNull World world, int chunkX, int chunkZ);

    /**
     * Returns a region scheduler. On Folia, this will run tasks in the specified region. On
     * Paper/Spigot, this will run tasks in the main thread.
     */
    default @NotNull SchedulerImplementation region(@NotNull Location location) {
        World world = location.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Location world cannot be null");
        }
        return region(world, location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    /**
     * Returns a region scheduler. On Folia, this will run tasks in the specified region. On
     * Paper/Spigot, this will run tasks in the main thread.
     */
    default @NotNull SchedulerImplementation region(@NotNull Block block) {
        return region(block.getWorld(), block.getX() >> 4, block.getZ() >> 4);
    }

    /**
     * Returns a region scheduler. On Folia, this will run tasks in the specified region. On
     * Paper/Spigot, this will run tasks in the main thread.
     */
    default @NotNull SchedulerImplementation region(@NotNull Chunk chunk) {
        return region(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }
}
