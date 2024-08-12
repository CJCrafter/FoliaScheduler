package com.cjcrafter.scheduler.folia;

import com.cjcrafter.scheduler.EntitySchedulerImplementation;
import com.cjcrafter.scheduler.TaskImplementation;
import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

@ApiStatus.Internal
public class FoliaEntityScheduler implements EntitySchedulerImplementation {

    private final @NotNull Plugin plugin;
    private final @NotNull EntityScheduler entityScheduler;

    @ApiStatus.Internal
    public FoliaEntityScheduler(@NotNull Plugin plugin, @NotNull Entity entity) {
        this.plugin = plugin;
        this.entityScheduler = entity.getScheduler();
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
    public boolean execute(@NotNull Runnable run, @Nullable Runnable retired, long delay) {
        return entityScheduler.execute(plugin, run, retired, delay);
    }

    @Override
    public @Nullable <T> TaskImplementation<T> run(@NotNull Function<TaskImplementation<T>, T> function, @Nullable Runnable retired) {
        FoliaTask<T> taskImplementation = new FoliaTask<>();
        Consumer<ScheduledTask> foliaConsumer = buildFoliaConsumer(taskImplementation, function);
        ScheduledTask scheduledTask = entityScheduler.run(plugin, foliaConsumer, retired);

        // Happens when entity is not valid, check Entity#isValid()
        if (scheduledTask == null)
            return null;

        taskImplementation.setScheduledTask(scheduledTask);
        return taskImplementation;
    }

    @Override
    public @Nullable <T> TaskImplementation<T> runDelayed(@NotNull Function<TaskImplementation<T>, T> function, @Nullable Runnable retired, long delay) {
        FoliaTask<T> taskImplementation = new FoliaTask<>();
        Consumer<ScheduledTask> foliaConsumer = buildFoliaConsumer(taskImplementation, function);
        ScheduledTask scheduledTask = entityScheduler.runDelayed(plugin, foliaConsumer, retired, delay);

        // Happens when entity is not valid, check Entity#isValid()
        if (scheduledTask == null)
            return null;

        taskImplementation.setScheduledTask(scheduledTask);
        return taskImplementation;
    }

    @Override
    public @Nullable <T> TaskImplementation<T> runAtFixedRate(@NotNull Function<TaskImplementation<T>, T> function, @Nullable Runnable retired, long delay, long period) {
        FoliaTask<T> taskImplementation = new FoliaTask<>();
        Consumer<ScheduledTask> foliaConsumer = buildFoliaConsumer(taskImplementation, function);
        ScheduledTask scheduledTask = entityScheduler.runAtFixedRate(plugin, foliaConsumer, retired, delay, period);

        // Happens when entity is not valid, check Entity#isValid()
        if (scheduledTask == null)
            return null;

        taskImplementation.setScheduledTask(scheduledTask);
        return taskImplementation;
    }
}
