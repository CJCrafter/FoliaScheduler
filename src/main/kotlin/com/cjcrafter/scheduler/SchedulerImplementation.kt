package com.cjcrafter.scheduler

import java.util.function.Consumer

/**
 * Represents a scheduler that can schedule tasks to be run.
 */
interface SchedulerImplementation {
    /**
     * Schedules a task to be run.
     *
     * @param run The task to run.
     */
    fun execute(run: Runnable)

    /**
     * Schedules a task to be run on the next tick.
     *
     * @param consumer The task to run.
     * @return The task that was scheduled.
     */
    fun run(consumer: Consumer<TaskImplementation>): TaskImplementation

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @param delay The delay in ticks before the task is run.
     * @return The task that was scheduled.
     */
    fun runDelayed(consumer: Consumer<TaskImplementation>, delay: Long): TaskImplementation

    /**
     * Schedules a task to be run after a delay and then repeatedly after a period.
     *
     * @param consumer The task to run.
     * @param delay The delay in ticks before the task is run.
     * @param period The period in ticks between each run.
     * @return The task that was scheduled.
     */
    fun runAtFixedRate(consumer: Consumer<TaskImplementation>, delay: Long, period: Long): TaskImplementation
}
