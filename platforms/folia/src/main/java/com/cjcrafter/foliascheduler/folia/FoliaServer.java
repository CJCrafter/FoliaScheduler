package com.cjcrafter.foliascheduler.folia;

import com.cjcrafter.foliascheduler.AsyncSchedulerImplementation;
import com.cjcrafter.foliascheduler.EntitySchedulerImplementation;
import com.cjcrafter.foliascheduler.GlobalSchedulerImplementation;
import com.cjcrafter.foliascheduler.RegionSchedulerImplementation;
import com.cjcrafter.foliascheduler.ServerImplementation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class FoliaServer implements ServerImplementation {

    private final @NotNull Plugin owningPlugin;
    private final @NotNull FoliaGlobalScheduler globalScheduler;
    private final @NotNull FoliaAsyncScheduler asyncScheduler;

    @ApiStatus.Internal
    public FoliaServer(@NotNull Plugin owningPlugin) {
        this.owningPlugin = owningPlugin;
        this.globalScheduler = new FoliaGlobalScheduler(owningPlugin);
        this.asyncScheduler = new FoliaAsyncScheduler(owningPlugin);
    }

    @Override
    public @NotNull Plugin getOwningPlugin() {
        return owningPlugin;
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Location location) {
        return Bukkit.isOwnedByCurrentRegion(location);
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Location location, int squareRadiusChunks) {
        return Bukkit.isOwnedByCurrentRegion(location, squareRadiusChunks);
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Block block) {
        return Bukkit.isOwnedByCurrentRegion(block);
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull World world, int chunkX, int chunkZ) {
        return Bukkit.isOwnedByCurrentRegion(world, chunkX, chunkZ);
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull World world, int chunkX, int chunkZ, int squareRadiusChunks) {
        return Bukkit.isOwnedByCurrentRegion(world, chunkX, chunkZ, squareRadiusChunks);
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Entity entity) {
        return Bukkit.isOwnedByCurrentRegion(entity);
    }

    @Override
    public @NotNull GlobalSchedulerImplementation global() {
        return globalScheduler;
    }

    @Override
    public @NotNull AsyncSchedulerImplementation async() {
        return asyncScheduler;
    }

    @Override
    public @NotNull EntitySchedulerImplementation entity(@NotNull Entity entity) {
        return new FoliaEntityScheduler(owningPlugin, entity);
    }

    @Override
    public @NotNull RegionSchedulerImplementation region(@NotNull World world, int chunkX, int chunkZ) {
        return new FoliaRegionScheduler(owningPlugin, world, chunkX, chunkZ);
    }

    @Override
    public void cancelTasks() {
        globalScheduler.cancelTasks();
        asyncScheduler.cancelTasks();
    }
}
