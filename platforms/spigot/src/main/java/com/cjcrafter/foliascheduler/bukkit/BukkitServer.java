package com.cjcrafter.foliascheduler.bukkit;

import com.cjcrafter.foliascheduler.AsyncSchedulerImplementation;
import com.cjcrafter.foliascheduler.EntitySchedulerImplementation;
import com.cjcrafter.foliascheduler.GlobalSchedulerImplementation;
import com.cjcrafter.foliascheduler.RegionSchedulerImplementation;
import com.cjcrafter.foliascheduler.ServerImplementation;
import com.cjcrafter.foliascheduler.util.MethodInvoker;
import com.cjcrafter.foliascheduler.util.ReflectionUtil;
import com.cjcrafter.foliascheduler.util.WrappedReflectiveOperationException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

@ApiStatus.Internal
public class BukkitServer implements ServerImplementation {

    private final @NotNull Plugin owningPlugin;
    private final @NotNull BukkitSyncScheduler sync;
    private final @NotNull BukkitRegionScheduler region;
    private final @NotNull BukkitAsyncScheduler async;

    // On Paper servers, the teleportAsync method is supported back to 1.13
    private @Nullable MethodInvoker teleportAsyncMethod;

    @ApiStatus.Internal
    public BukkitServer(@NotNull Plugin owningPlugin) {
        this.owningPlugin = owningPlugin;
        this.sync = new BukkitSyncScheduler(owningPlugin);
        this.region = new BukkitRegionScheduler(owningPlugin);
        this.async = new BukkitAsyncScheduler(owningPlugin);

        try {
            teleportAsyncMethod = ReflectionUtil.getMethod(Entity.class, "teleportAsync", Location.class, PlayerTeleportEvent.TeleportCause.class);
        } catch (WrappedReflectiveOperationException ignore) {
            // This happens on older paper servers, or plain Spigot servers
        }
    }

    @Override
    public @NotNull Plugin getOwningPlugin() {
        return owningPlugin;
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Location location) {
        return owningPlugin.getServer().isPrimaryThread();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Location location, int squareRadiusChunks) {
        return owningPlugin.getServer().isPrimaryThread();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Block block) {
        return owningPlugin.getServer().isPrimaryThread();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull World world, int chunkX, int chunkZ) {
        return owningPlugin.getServer().isPrimaryThread();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull World world, int chunkX, int chunkZ, int squareRadiusChunks) {
        return owningPlugin.getServer().isPrimaryThread();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Entity entity) {
        return owningPlugin.getServer().isPrimaryThread();
    }

    @Override
    public @NotNull GlobalSchedulerImplementation global() {
        return sync;
    }

    @Override
    public @NotNull AsyncSchedulerImplementation async() {
        return async;
    }

    @Override
    public @NotNull EntitySchedulerImplementation entity(@NotNull Entity entity) {
        return new BukkitEntityScheduler(owningPlugin, entity);
    }

    @Override
    public @NotNull RegionSchedulerImplementation region(@NotNull World world, int chunkX, int chunkZ) {
        return region;
    }

    @Override
    public void cancelTasks() {
        owningPlugin.getServer().getScheduler().cancelTasks(owningPlugin);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> teleportAsync(@NotNull Entity entity, @NotNull Location location, PlayerTeleportEvent.@NotNull TeleportCause cause) {
        // Check if the teleportAsync method is supported (Paper 1.13+)
        if (teleportAsyncMethod != null) {
            Object result = teleportAsyncMethod.invoke(entity, location, cause);
            return (CompletableFuture<Boolean>) Objects.requireNonNull(result);
        }

        // Fallback to the synchronous teleport method, executed 1 tick later on the main thread
        // to allow this method to be called asynchronously.
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        entity(entity).run(task -> {
            try {
                entity.teleport(location);
                future.complete(true);
            } catch (Throwable ex) {
                owningPlugin.getLogger().log(Level.SEVERE, "Failed to teleport entity", ex);
                future.complete(false);
            }
        });

        return future;
    }
}
