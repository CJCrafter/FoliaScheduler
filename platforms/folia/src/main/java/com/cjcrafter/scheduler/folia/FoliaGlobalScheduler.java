package com.cjcrafter.scheduler.folia;

import com.cjcrafter.scheduler.SchedulerImplementation;
import com.cjcrafter.scheduler.TaskImplementation;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

@ApiStatus.Internal
public class FoliaGlobalScheduler implements SchedulerImplementation {

    private final @NotNull Plugin plugin;
    private final @NotNull GlobalRegionScheduler globalRegionScheduler;

    @ApiStatus.Internal
    public FoliaGlobalScheduler(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.globalRegionScheduler = plugin.getServer().getGlobalRegionScheduler();
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

    @Override
    public void execute(@NotNull Runnable run) {
        globalRegionScheduler.execute(plugin, run);
    }

    @Override
    public @NotNull <T> TaskImplementation<T> run(@NotNull Function<TaskImplementation<T>, T> function) {
        FoliaTask<T> taskImplementation = new FoliaTask<>();
        Consumer<ScheduledTask> foliaConsumer = buildFoliaConsumer(taskImplementation, function);
        ScheduledTask scheduledTask = globalRegionScheduler.run(plugin, foliaConsumer);
        taskImplementation.setScheduledTask(scheduledTask);
        return taskImplementation;
    }

    @Override
    public @NotNull <T> TaskImplementation<T> runDelayed(@NotNull Function<TaskImplementation<T>, T> function, long delay) {
        FoliaTask<T> taskImplementation = new FoliaTask<>();
        Consumer<ScheduledTask> foliaConsumer = buildFoliaConsumer(taskImplementation, function);
        ScheduledTask scheduledTask = globalRegionScheduler.runDelayed(plugin, foliaConsumer, delay);
        taskImplementation.setScheduledTask(scheduledTask);
        return taskImplementation;
    }

    @Override
    public @NotNull <T> TaskImplementation<T> runAtFixedRate(@NotNull Function<TaskImplementation<T>, T> function, long delay, long period) {
        FoliaTask<T> taskImplementation = new FoliaTask<>();
        Consumer<ScheduledTask> foliaConsumer = buildFoliaConsumer(taskImplementation, function);
        ScheduledTask scheduledTask = globalRegionScheduler.runAtFixedRate(plugin, foliaConsumer, delay, period);
        taskImplementation.setScheduledTask(scheduledTask);
        return taskImplementation;
    }
}
