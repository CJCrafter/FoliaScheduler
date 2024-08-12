package com.cjcrafter.scheduler.bukkit;

import com.cjcrafter.scheduler.AsyncSchedulerImplementation;
import com.cjcrafter.scheduler.EntitySchedulerImplementation;
import com.cjcrafter.scheduler.SchedulerImplementation;
import com.cjcrafter.scheduler.ServerImplementation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class BukkitServer implements ServerImplementation {

    private final @NotNull Plugin owningPlugin;
    private final @NotNull BukkitSyncScheduler sync;
    private final @NotNull BukkitAsyncScheduler async;

    @ApiStatus.Internal
    public BukkitServer(@NotNull Plugin owningPlugin) {
        this.owningPlugin = owningPlugin;
        this.sync = new BukkitSyncScheduler(owningPlugin);
        this.async = new BukkitAsyncScheduler(owningPlugin);
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
    public @NotNull SchedulerImplementation global() {
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
    public @NotNull SchedulerImplementation region(@NotNull World world, int chunkX, int chunkZ) {
        return sync;
    }
}
