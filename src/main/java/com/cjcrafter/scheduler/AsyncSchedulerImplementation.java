package com.cjcrafter.scheduler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a scheduler that can schedule tasks to be run asynchronously (separate from the server thread(s)).
 */
public interface AsyncSchedulerImplementation {

    /**
     * Runs a task asynchronously immediately.
     */
    <T> @NotNull TaskImplementation<T> runNow(@NotNull Function<TaskImplementation<T>, T> function);

    /**
     * Runs a task asynchronously immediately.
     */
    default @NotNull TaskImplementation<Void> runNow(@NotNull Consumer<TaskImplementation<Void>> consumer) {
        Function<TaskImplementation<Void>, Void> wrapperFunction = task -> {
            consumer.accept(task);
            return null;
        };
        return this.runNow(wrapperFunction);
    }

    /**
     * Runs a task asynchronously immediately.
     */
    default @NotNull TaskImplementation<Void> runNow(@NotNull Runnable runnable) {
        Function<TaskImplementation<Void>, Void> wrapperFunction = task -> {
            runnable.run();
            return null;
        };
        return this.runNow(wrapperFunction);
    }

    /**
     * Schedules a task to be run asynchronously after a delay.
     *
     * @param function The task to run.
     * @param delay The delay before the task is run.
     * @param unit The time unit of the delay.
     * @return The task that was scheduled.
     */
    <T> @NotNull TaskImplementation<T> runDelayed(
        @NotNull Function<TaskImplementation<T>, T> function,
        long delay,
        @NotNull TimeUnit unit
    );

    /**
     * Schedules a task to be run asynchronously after a delay.
     *
     * @param consumer The task to run.
     * @param delay The delay before the task is run.
     * @param unit The time unit of the delay.
     * @return The task that was scheduled.
     */
    default @NotNull TaskImplementation<Void> runDelayed(
        @NotNull Consumer<TaskImplementation<Void>> consumer,
        long delay,
        @NotNull TimeUnit unit
    ) {
        Function<TaskImplementation<Void>, Void> wrapperFunction = task -> {
            consumer.accept(task);
            return null;
        };
        return this.runDelayed(wrapperFunction, delay, unit);
    }

    /**
     * Schedules a task to be run asynchronously after a delay.
     *
     * @param runnable The task to run.
     * @param delay The delay before the task is run.
     * @param unit The time unit of the delay.
     * @return The task that was scheduled.
     */
    default @NotNull TaskImplementation<Void> runDelayed(
        @NotNull Runnable runnable,
        long delay,
        @NotNull TimeUnit unit
    ) {
        Function<TaskImplementation<Void>, Void> wrapperFunction = task -> {
            runnable.run();
            return null;
        };
        return this.runDelayed(wrapperFunction, delay, unit);
    }

    /**
     * Schedules a task to be run asynchronously after a delay, in ticks.
     *
     * @param function The task to run.
     * @param ticks The delay in ticks before the task is run.
     * @return The task that was scheduled.
     */
    default <T> @NotNull TaskImplementation<T> runDelayed(
        @NotNull Function<TaskImplementation<T>, @Nullable T> function,
        long ticks
    ) {
        // assumes the time unit is server ticks. Since 20 ticks = 1000ms, we can convert the delay to milliseconds
        return runDelayed(function, ticks * 50, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedules a task to be run asynchronously after a delay, in ticks.
     *
     * @param consumer The task to run.
     * @param ticks The delay in ticks before the task is run.
     * @return The task that was scheduled.
     */
    default @NotNull TaskImplementation<Void> runDelayed(
        @NotNull Consumer<TaskImplementation<Void>> consumer,
        long ticks
    ) {
        // assumes the time unit is server ticks. Since 20 ticks = 1000ms, we can convert the delay to milliseconds
        return runDelayed(consumer, ticks * 50, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedules a task to be run asynchronously after a delay, in ticks.
     *
     * @param runnable The task to run.
     * @param ticks The delay in ticks before the task is run.
     * @return The task that was scheduled.
     */
    default @NotNull TaskImplementation<Void> runDelayed(
        @NotNull Runnable runnable,
        long ticks
    ) {
        // assumes the time unit is server ticks. Since 20 ticks = 1000ms, we can convert the delay to milliseconds
        return runDelayed(runnable, ticks * 50, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedules a task to be run asynchronously after a delay and then repeatedly after a period.
     *
     * @param function The task to run.
     * @param delay The delay before the task is run.
     * @param period The period between each run.
     * @param unit The time unit of the delay and period.
     * @return The task that was scheduled.
     */
    <T> @NotNull TaskImplementation<T> runAtFixedRate(
        @NotNull Function<TaskImplementation<T>, T> function,
        long delay,
        long period,
        @NotNull TimeUnit unit
    );

    /**
     * Schedules a task to be run asynchronously after a delay and then repeatedly after a period.
     *
     * @param consumer The task to run.
     * @param delay The delay before the task is run.
     * @param period The period between each run.
     * @param unit The time unit of the delay and period.
     * @return The task that was scheduled.
     */
    default @NotNull TaskImplementation<Void> runAtFixedRate(
        @NotNull Consumer<TaskImplementation<Void>> consumer,
        long delay,
        long period,
        @NotNull TimeUnit unit
    ) {
        Function<TaskImplementation<Void>, Void> wrapperFunction = task -> {
            consumer.accept(task);
            return null;
        };
        return this.runAtFixedRate(wrapperFunction, delay, period, unit);
    }

    /**
     * Schedules a task to be run asynchronously after a delay and then repeatedly after a period.
     *
     * @param runnable The task to run.
     * @param delay The delay before the task is run.
     * @param period The period between each run.
     * @param unit The time unit of the delay and period.
     * @return The task that was scheduled.
     */
    default @NotNull TaskImplementation<Void> runAtFixedRate(
        @NotNull Runnable runnable,
        long delay,
        long period,
        @NotNull TimeUnit unit
    ) {
        Function<TaskImplementation<Void>, Void> wrapperFunction = task -> {
            runnable.run();
            return null;
        };
        return this.runAtFixedRate(wrapperFunction, delay, period, unit);
    }

    /**
     * Schedules a task to be run asynchronously after a delay and then repeatedly after a period, in ticks.
     *
     * @param function The task to run.
     * @param delay The delay in ticks before the task is run.
     * @param ticks The period in ticks between each run.
     * @return The task that was scheduled.
     */
    default <T> @NotNull TaskImplementation<T> runAtFixedRate(
        @NotNull Function<TaskImplementation<T>, @Nullable T> function,
        long delay,
        long ticks
    ) {
        // assumes the time unit is server ticks. Since 20 ticks = 1000ms, we can convert the delay to milliseconds
        return runAtFixedRate(function, delay * 50, ticks * 50, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedules a task to be run asynchronously after a delay and then repeatedly after a period, in ticks.
     *
     * @param consumer The task to run.
     * @param delay The delay in ticks before the task is run.
     * @param ticks The period in ticks between each run.
     * @return The task that was scheduled.
     */
    default @NotNull TaskImplementation<Void> runAtFixedRate(
        @NotNull Consumer<TaskImplementation<Void>> consumer,
        long delay,
        long ticks
    ) {
        // assumes the time unit is server ticks. Since 20 ticks = 1000ms, we can convert the delay to milliseconds
        return runAtFixedRate(consumer, delay * 50, ticks * 50, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedules a task to be run asynchronously after a delay and then repeatedly after a period, in ticks.
     *
     * @param runnable The task to run.
     * @param delay The delay in ticks before the task is run.
     * @param ticks The period in ticks between each run.
     * @return The task that was scheduled.
     */
    default @NotNull TaskImplementation<Void> runAtFixedRate(
        @NotNull Runnable runnable,
        long delay,
        long ticks
    ) {
        // assumes the time unit is server ticks. Since 20 ticks = 1000ms, we can convert the delay to milliseconds
        return runAtFixedRate(runnable, delay * 50, ticks * 50, TimeUnit.MILLISECONDS);
    }
}