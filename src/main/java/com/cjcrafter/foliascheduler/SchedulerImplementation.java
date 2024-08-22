package com.cjcrafter.foliascheduler;

import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

/** Represents a scheduler that can schedule tasks to be run. */
public interface SchedulerImplementation {

    /**
     * Schedules a task to be run.
     *
     * @param run The task to run.
     */
    void execute(@NotNull Runnable run);

    /**
     * Schedules a task to be run after a delay.
     *
     * @param function The task to run.
     * @return The task that was scheduled.
     */
    <T> @NotNull TaskImplementation<T> run(@NotNull Function<TaskImplementation<T>, T> function);

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @return The task that was scheduled.
     */
    default @NotNull TaskImplementation<Void> run(
            @NotNull Consumer<TaskImplementation<Void>> consumer) {
        Function<TaskImplementation<Void>, Void> wrapperFunction =
                task -> {
                    consumer.accept(task);
                    return null;
                };
        return run(wrapperFunction);
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param runnable The task to run.
     * @return The task that was scheduled.
     */
    default @NotNull TaskImplementation<Void> run(@NotNull Runnable runnable) {
        Function<TaskImplementation<Void>, Void> wrapperFunction =
                task -> {
                    runnable.run();
                    return null;
                };
        return run(wrapperFunction);
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param function The task to run.
     * @param delay The delay in ticks before the task is run.
     * @return The task that was scheduled.
     */
    @NotNull <T> TaskImplementation<T> runDelayed(
            @NotNull Function<TaskImplementation<T>, T> function, long delay);

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @param delay The delay in ticks before the task is run.
     * @return The task that was scheduled.
     */
    default @NotNull TaskImplementation<Void> runDelayed(
            @NotNull Consumer<TaskImplementation<Void>> consumer, long delay) {
        Function<TaskImplementation<Void>, Void> wrapperFunction =
                task -> {
                    consumer.accept(task);
                    return null;
                };
        return runDelayed(wrapperFunction, delay);
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param runnable The task to run.
     * @param delay The delay in ticks before the task is run.
     * @return The task that was scheduled.
     */
    default @NotNull TaskImplementation<Void> runDelayed(@NotNull Runnable runnable, long delay) {
        Function<TaskImplementation<Void>, Void> wrapperFunction =
                task -> {
                    runnable.run();
                    return null;
                };
        return runDelayed(wrapperFunction, delay);
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param function The task to run.
     * @param delay The delay before the task is run.
     * @param period The time unit of the delay.
     */
    <T> @NotNull TaskImplementation<T> runAtFixedRate(
            @NotNull Function<TaskImplementation<T>, T> function, long delay, long period);

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @param delay The delay before the task is run.
     * @param period The time unit of the delay.
     */
    default @NotNull TaskImplementation<Void> runAtFixedRate(
            @NotNull Consumer<TaskImplementation<Void>> consumer, long delay, long period) {
        Function<TaskImplementation<Void>, Void> wrapperFunction =
                task -> {
                    consumer.accept(task);
                    return null;
                };
        return runAtFixedRate(wrapperFunction, delay, period);
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param runnable The task to run.
     * @param delay The delay before the task is run.
     * @param period The time unit of the delay.
     */
    @NotNull default TaskImplementation<Void> runAtFixedRate(
            @NotNull Runnable runnable, long delay, long period) {
        Function<TaskImplementation<Void>, Void> wrapperFunction =
                task -> {
                    runnable.run();
                    return null;
                };
        return runAtFixedRate(wrapperFunction, delay, period);
    }
}
