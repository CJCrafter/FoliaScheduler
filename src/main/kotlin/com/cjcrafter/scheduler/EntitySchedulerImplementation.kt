package com.cjcrafter.scheduler

import java.util.function.Consumer

/**
 * Represents a scheduler that can schedule tasks to be run on an entity.
 *
 * The entity scheduler can return null if the entity you are scheduling on is no longer valid (e.g. has been removed
 * from the world). In these cases, you can specify the `retired` runnable to execute code conditionally if the entity
 * is retired before a task is run.
 */
interface EntitySchedulerImplementation {

    /**
     * Schedules a task to be run immediately.
     *
     * @param run The task to run.
     * @param retired The task to run if the entity is retired.
     * @param delay The delay before the callback is run.
     */
    fun execute(run: Runnable, retired: Runnable?, delay: Long): Boolean

    /**
     * Schedules a task to be run on the next tick.
     *
     * @param run The task to run.
     */
    fun execute(run: Runnable): Boolean {
        return execute(run, null, 1)
    }

    /**
     * Schedules a task to be run on the next tick.
     *
     * @param consumer The task to run.
     * @param retired The task to run if the entity is retired.
     * @return The task that was scheduled.
     */
    fun run(consumer: Consumer<TaskImplementation>, retired: Runnable?): TaskImplementation?

    /**
     * Schedules a task to be run on the next tick.
     *
     * @param consumer The task to run.
     * @return The task that was scheduled.
     */
    fun run(consumer: Consumer<TaskImplementation>): TaskImplementation? {
        return run(consumer, null)
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @param retired The task to run if the entity is retired.
     * @param delay The delay before the task is run.
     * @return The task that was scheduled.
     */
    fun runDelayed(consumer: Consumer<TaskImplementation>, retired: Runnable?, delay: Long): TaskImplementation?

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @param delay The delay before the task is run.
     * @return The task that was scheduled.
     */
    fun runDelayed(consumer: Consumer<TaskImplementation>, delay: Long): TaskImplementation? {
        return runDelayed(consumer, null, delay)
    }

    /**
     * Schedules a task to be run after a delay and then repeatedly after a period.
     *
     * @param consumer The task to run.
     * @param retired The task to run if the entity is retired.
     * @param delay The delay before the task is run.
     * @param period The period between each run.
     * @return The task that was scheduled.
     */
    fun runAtFixedRate(consumer: Consumer<TaskImplementation>, retired: Runnable?, delay: Long, period: Long): TaskImplementation?

    /**
     * Schedules a task to be run after a delay and then repeatedly after a period.
     *
     * @param consumer The task to run.
     * @param delay The delay before the task is run.
     * @param period The period between each run.
     * @return The task that was scheduled.
     */
    fun runAtFixedRate(consumer: Consumer<TaskImplementation>, delay: Long, period: Long): TaskImplementation? {
        return runAtFixedRate(consumer, null, delay, period)
    }
}

