package com.cjcrafter.foliascheduler.folia;

import com.cjcrafter.foliascheduler.RegionSchedulerImplementation;
import com.cjcrafter.foliascheduler.TaskImplementation;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public class FoliaRegionScheduler implements RegionSchedulerImplementation {

    private final @NotNull Plugin plugin;
    private final @NotNull RegionScheduler regionScheduler;
    private final @NotNull World world;
    private final int chunkX;
    private final int chunkZ;

    public FoliaRegionScheduler(@NotNull Plugin plugin, @NotNull World world, int chunkX, int chunkZ) {
        this.plugin = plugin;
        this.regionScheduler = plugin.getServer().getRegionScheduler();
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    private <T> @NotNull Consumer<ScheduledTask> buildFoliaConsumer(
        @NotNull FoliaTask<T> taskImplementation,
        @NotNull Function<TaskImplementation<T>, T> callbackFunction
    ) {
        return scheduledTask -> {
            taskImplementation.setScheduledTask(scheduledTask);
            taskImplementation.setCallback(callbackFunction.apply(taskImplementation));
            taskImplementation.asFuture().complete(taskImplementation);
        };
    }

    public void execute(@NotNull Runnable run) {
        regionScheduler.execute(plugin, world, chunkX, chunkZ, run);
    }

    @Override
    public @NotNull <T> TaskImplementation<T> run(@NotNull Function<TaskImplementation<T>, T> function) {
        FoliaTask<T> taskImplementation = new FoliaTask<>();
        Consumer<ScheduledTask> foliaConsumer = buildFoliaConsumer(taskImplementation, function);
        ScheduledTask scheduledTask = regionScheduler.run(plugin, world, chunkX, chunkZ, foliaConsumer);
        taskImplementation.setScheduledTask(scheduledTask);
        return taskImplementation;
    }

    @Override
    public @NotNull <T> TaskImplementation<T> runDelayed(@NotNull Function<TaskImplementation<T>, T> function, long delay) {
        FoliaTask<T> taskImplementation = new FoliaTask<>();
        Consumer<ScheduledTask> foliaConsumer = buildFoliaConsumer(taskImplementation, function);
        ScheduledTask scheduledTask = regionScheduler.runDelayed(plugin, world, chunkX, chunkZ, foliaConsumer, delay);
        taskImplementation.setScheduledTask(scheduledTask);
        return taskImplementation;
    }

    @Override
    public @NotNull <T> TaskImplementation<T> runAtFixedRate(@NotNull Function<TaskImplementation<T>, T> function, long delay, long period) {
        FoliaTask<T> taskImplementation = new FoliaTask<>();
        Consumer<ScheduledTask> foliaConsumer = buildFoliaConsumer(taskImplementation, function);
        ScheduledTask scheduledTask = regionScheduler.runAtFixedRate(plugin, world, chunkX, chunkZ, foliaConsumer, delay, period);
        taskImplementation.setScheduledTask(scheduledTask);
        return taskImplementation;
    }
}
