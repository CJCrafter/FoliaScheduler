package com.cjcrafter.foliascheduler;

import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a scheduler that can schedule tasks to be run on an entity.
 *
 * <p>The entity scheduler can return null if the entity you are scheduling on is no longer valid
 * (e.g. has been removed from the world). In these cases, you can specify the `retired` runnable to
 * execute code conditionally if the entity is retired before a task is run.
 */
public interface EntitySchedulerImplementation {

    /**
     * Schedules a task to be run immediately.
     *
     * @param run The task to run.
     * @param retired The task to run if the entity is retired.
     * @param delay The delay before the callback is run.
     */
    boolean execute(@NotNull Runnable run, @Nullable Runnable retired, long delay);

    /**
     * Schedules a task to be run on the next tick.
     *
     * @param run The task to run.
     */
    default boolean execute(@NotNull Runnable run) {
        return execute(run, null, 1);
    }

    /**
     * Schedules a task to be run on the next tick.
     *
     * @param function The task to run.
     * @param retired The task to run if the entity is retired.
     * @return The task that was scheduled.
     */
    @Nullable <T> TaskImplementation<T> run(
            @NotNull Function<TaskImplementation<T>, T> function, @Nullable Runnable retired);

    /**
     * Schedules a task to be run on the next tick.
     *
     * @param consumer The task to run.
     * @return The task that was scheduled.
     */
    @Nullable default TaskImplementation<Void> run(
            @NotNull Consumer<TaskImplementation<Void>> consumer, @Nullable Runnable retired) {
        Function<TaskImplementation<Void>, Void> wrapperFunction =
                task -> {
                    consumer.accept(task);
                    return null;
                };
        return run(wrapperFunction, retired);
    }

    /**
     * Schedules a task to be run on the next tick.
     *
     * @param runnable The task to run.
     * @return The task that was scheduled.
     */
    @Nullable default TaskImplementation<Void> run(@NotNull Runnable runnable, @Nullable Runnable retired) {
        Function<TaskImplementation<Void>, Void> wrapperFunction =
                task -> {
                    runnable.run();
                    return null;
                };
        return run(wrapperFunction, retired);
    }

    /**
     * Schedules a task to be run on the next tick.
     *
     * @param function The task to run.
     * @return The task that was scheduled.
     */
    @Nullable default <T> TaskImplementation<T> run(@NotNull Function<TaskImplementation<T>, T> function) {
        return run(function, null);
    }

    /**
     * Schedules a task to be run on the next tick.
     *
     * @param consumer The task to run.
     * @return The task that was scheduled.
     */
    @Nullable default TaskImplementation<Void> run(@NotNull Consumer<TaskImplementation<Void>> consumer) {
        return run(consumer, null);
    }

    /**
     * Schedules a task to be run on the next tick.
     *
     * @param runnable The task to run.
     * @return The task that was scheduled.
     */
    @Nullable default TaskImplementation<Void> run(@NotNull Runnable runnable) {
        return run(runnable, null);
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param function The task to run.
     * @param retired The task to run if the entity is retired.
     * @param delay The delay before the task is run.
     * @return The task that was scheduled.
     */
    @Nullable <T> TaskImplementation<T> runDelayed(
            @NotNull Function<TaskImplementation<T>, T> function,
            @Nullable Runnable retired,
            long delay);

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @param retired The task to run if the entity is retired.
     * @param delay The delay before the task is run.
     * @return The task that was scheduled.
     */
    @Nullable default TaskImplementation<Void> runDelayed(
            @NotNull Consumer<TaskImplementation<Void>> consumer,
            @Nullable Runnable retired,
            long delay) {
        Function<TaskImplementation<Void>, Void> wrapperFunction =
                task -> {
                    consumer.accept(task);
                    return null;
                };
        return runDelayed(wrapperFunction, retired, delay);
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param runnable The task to run.
     * @param retired The task to run if the entity is retired.
     * @param delay The delay before the task is run.
     * @return The task that was scheduled.
     */
    @Nullable default TaskImplementation<Void> runDelayed(
            @NotNull Runnable runnable, @Nullable Runnable retired, long delay) {
        Function<TaskImplementation<Void>, Void> wrapperFunction =
                task -> {
                    runnable.run();
                    return null;
                };
        return runDelayed(wrapperFunction, retired, delay);
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param function The task to run.
     * @param delay The delay before the task is run.
     * @return The task that was scheduled.
     */
    @Nullable default <T> TaskImplementation<T> runDelayed(
            @NotNull Function<TaskImplementation<T>, T> function, long delay) {
        return runDelayed(function, null, delay);
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @param delay The delay before the task is run.
     * @return The task that was scheduled.
     */
    @Nullable default TaskImplementation<Void> runDelayed(
            @NotNull Consumer<TaskImplementation<Void>> consumer, long delay) {
        return runDelayed(consumer, null, delay);
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param runnable The task to run.
     * @param delay The delay before the task is run.
     * @return The task that was scheduled.
     */
    @Nullable default TaskImplementation<Void> runDelayed(@NotNull Runnable runnable, long delay) {
        return runDelayed(runnable, null, delay);
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param function The task to run.
     * @param retired The task to run if the entity is retired.
     * @param delay The delay before the task is run.
     * @param period The period between each run.
     * @return The task that was scheduled.
     */
    @Nullable <T> TaskImplementation<T> runAtFixedRate(
            @NotNull Function<TaskImplementation<T>, T> function,
            @Nullable Runnable retired,
            long delay,
            long period);

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @param retired The task to run if the entity is retired.
     * @param delay The delay before the task is run.
     * @param period The period between each run.
     * @return The task that was scheduled.
     */
    @Nullable default TaskImplementation<Void> runAtFixedRate(
            @NotNull Consumer<TaskImplementation<Void>> consumer,
            @Nullable Runnable retired,
            long delay,
            long period) {
        Function<TaskImplementation<Void>, Void> wrapperFunction =
                task -> {
                    consumer.accept(task);
                    return null;
                };
        return runAtFixedRate(wrapperFunction, retired, delay, period);
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param runnable The task to run.
     * @param retired The task to run if the entity is retired.
     * @param delay The delay before the task is run.
     * @param period The period between each run.
     * @return The task that was scheduled.
     */
    @Nullable default TaskImplementation<Void> runAtFixedRate(
            @NotNull Runnable runnable, @Nullable Runnable retired, long delay, long period) {
        Function<TaskImplementation<Void>, Void> wrapperFunction =
                task -> {
                    runnable.run();
                    return null;
                };
        return runAtFixedRate(wrapperFunction, retired, delay, period);
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param function The task to run.
     * @param delay The delay before the task is run.
     * @param period The period between each run.
     * @return The task that was scheduled.
     */
    @Nullable default <T> TaskImplementation<T> runAtFixedRate(
            @NotNull Function<TaskImplementation<T>, T> function, long delay, long period) {
        return runAtFixedRate(function, null, delay, period);
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @param delay The delay before the task is run.
     * @param period The period between each run.
     * @return The task that was scheduled.
     */
    @Nullable default TaskImplementation<Void> runAtFixedRate(
            @NotNull Consumer<TaskImplementation<Void>> consumer, long delay, long period) {
        return runAtFixedRate(consumer, null, delay, period);
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param runnable The task to run.
     * @param delay The delay before the task is run.
     * @param period The period between each run.
     * @return The task that was scheduled.
     */
    @Nullable default TaskImplementation<Void> runAtFixedRate(
            @NotNull Runnable runnable, long delay, long period) {
        return runAtFixedRate(runnable, null, delay, period);
    }
}
