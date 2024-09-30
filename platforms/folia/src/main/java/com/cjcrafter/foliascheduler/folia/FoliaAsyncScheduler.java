package com.cjcrafter.foliascheduler.folia;

import com.cjcrafter.foliascheduler.AsyncSchedulerImplementation;
import com.cjcrafter.foliascheduler.TaskImplementation;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

@ApiStatus.Internal
public class FoliaAsyncScheduler implements AsyncSchedulerImplementation {

    private final @NotNull Plugin plugin;
    private final @NotNull AsyncScheduler asyncScheduler;

    @ApiStatus.Internal
    public FoliaAsyncScheduler(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.asyncScheduler = plugin.getServer().getAsyncScheduler();
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
    public <T> @NotNull TaskImplementation<T> runNow(@NotNull Function<TaskImplementation<T>, T> function) {
        FoliaTask<T> taskImplementation = new FoliaTask<>();
        Consumer<ScheduledTask> foliaConsumer = buildFoliaConsumer(taskImplementation, function);
        ScheduledTask scheduledTask = asyncScheduler.runNow(plugin, foliaConsumer);
        taskImplementation.setScheduledTask(scheduledTask);
        return taskImplementation;
    }

    @Override
    public <T> @NotNull TaskImplementation<T> runDelayed(@NotNull Function<TaskImplementation<T>, T> function, long delay, @NotNull TimeUnit unit) {
        FoliaTask<T> taskImplementation = new FoliaTask<>();
        Consumer<ScheduledTask> foliaConsumer = buildFoliaConsumer(taskImplementation, function);
        ScheduledTask scheduledTask = asyncScheduler.runDelayed(plugin, foliaConsumer, delay, unit);
        taskImplementation.setScheduledTask(scheduledTask);
        return taskImplementation;
    }

    @Override
    public <T> @NotNull TaskImplementation<T> runAtFixedRate(@NotNull Function<TaskImplementation<T>, T> function, long delay, long period, @NotNull TimeUnit unit) {
        FoliaTask<T> taskImplementation = new FoliaTask<>();
        Consumer<ScheduledTask> foliaConsumer = buildFoliaConsumer(taskImplementation, function);
        ScheduledTask scheduledTask = asyncScheduler.runAtFixedRate(plugin, foliaConsumer, delay, period, unit);
        taskImplementation.setScheduledTask(scheduledTask);
        return taskImplementation;
    }

    @Override
    public void cancelTasks() {
        asyncScheduler.cancelTasks(plugin);
    }
}
