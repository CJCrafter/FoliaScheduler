package com.cjcrafter.scheduler

import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * Represents a scheduler that can schedule tasks to be run asynchronously (separate from the server thread(s)).
 */
interface AsyncSchedulerImplementation {
    /**
     * Runs a task asynchronously immediately.
     */
    fun runNow(consumer: Consumer<TaskImplementation>): TaskImplementation

    /**
     * Schedules a task to be run asynchronously after a delay.
     *
     * @param consumer The task to run.
     * @param delay The delay before the task is run.
     * @param unit The time unit of the delay.
     * @return The task that was scheduled.
     */
    fun runDelayed(
        consumer: Consumer<TaskImplementation>,
        delay: Long,
        unit: TimeUnit
    ): TaskImplementation

    /**
     * Schedules a task to be run asynchronously after a delay, in ticks.
     *
     * @param consumer The task to run.
     * @param delay The delay in ticks before the task is run.
     * @return The task that was scheduled.
     */
    fun runDelayed(
        consumer: Consumer<TaskImplementation>,
        delay: Long
    ): TaskImplementation {
        // assumes the time unit is server ticks. Since 20 ticks = 1000ms, we can convert the delay to milliseconds
        return runDelayed(consumer, delay * 50, TimeUnit.MILLISECONDS)
    }

    /**
     * Schedules a task to be run asynchronously after a delay and then repeatedly after a period.
     *
     * @param consumer The task to run.
     * @param delay The delay before the task is run.
     * @param period The period between each run.
     * @param unit The time unit of the delay and period.
     * @return The task that was scheduled.
     */
    fun runAtFixedRate(
        consumer: Consumer<TaskImplementation>,
        delay: Long,
        period: Long,
        unit: TimeUnit
    ): TaskImplementation

    /**
     * Schedules a task to be run asynchronously after a delay and then repeatedly after a period, in ticks.
     *
     * @param consumer The task to run.
     * @param delay The delay in ticks before the task is run.
     * @param period The period in ticks between each run.
     * @return The task that was scheduled.
     */
    fun runAtFixedRate(
        consumer: Consumer<TaskImplementation>,
        delay: Long,
        period: Long
    ): TaskImplementation {
        // assumes the time unit is server ticks. Since 20 ticks = 1000ms, we can convert the delay to milliseconds
        return runAtFixedRate(consumer, delay * 50, period * 50, TimeUnit.MILLISECONDS)
    }
}
