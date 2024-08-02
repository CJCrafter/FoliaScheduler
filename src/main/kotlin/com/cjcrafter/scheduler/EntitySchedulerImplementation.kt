package com.cjcrafter.scheduler

import java.util.function.Consumer
import java.util.function.Function

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
     * @param function The task to run.
     * @param retired The task to run if the entity is retired.
     * @return The task that was scheduled.
     */
    fun <T : Any> run(function: Function<TaskImplementation<T>, T>, retired: Runnable?): TaskImplementation<T>?

    /**
     * Schedules a task to be run on the next tick.
     *
     * @param consumer The task to run.
     * @return The task that was scheduled.
     */
    fun run(consumer: Consumer<TaskImplementation<Void>>, retired: Runnable?): TaskImplementation<Void>? {
        val wrapperFunction = Function<TaskImplementation<Void>, Void> { task ->
            consumer.accept(task)
            null
        }
        return run(wrapperFunction, retired)
    }

    /**
     * Schedules a task to be run on the next tick.
     *
     * @param runnable The task to run.
     * @return The task that was scheduled.
     */
    fun run(runnable: Runnable, retired: Runnable?): TaskImplementation<Void>? {
        val wrapperFunction = Function<TaskImplementation<Void>, Void> { task ->
            runnable.run()
            null
        }
        return run(wrapperFunction, retired)
    }

    /**
     * Schedules a task to be run on the next tick.
     *
     * @param function The task to run.
     * @return The task that was scheduled.
     */
    fun <T : Any> run(function: Function<TaskImplementation<T>, T>): TaskImplementation<T>? {
        return run(function, null)
    }

    /**
     * Schedules a task to be run on the next tick.
     *
     * @param consumer The task to run.
     * @return The task that was scheduled.
     */
    fun run(consumer: Consumer<TaskImplementation<Void>>): TaskImplementation<Void>? {
        return run(consumer, null)
    }

    /**
     * Schedules a task to be run on the next tick.
     *
     * @param runnable The task to run.
     * @return The task that was scheduled.
     */
    fun run(runnable: Runnable): TaskImplementation<Void>? {
        return run(runnable, null)
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param function The task to run.
     * @param retired The task to run if the entity is retired.
     * @param delay The delay before the task is run.
     * @return The task that was scheduled.
     */
    fun <T : Any> runDelayed(
        function: Function<TaskImplementation<T>, T>,
        retired: Runnable?,
        delay: Long,
    ): TaskImplementation<T>?

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @param retired The task to run if the entity is retired.
     * @param delay The delay before the task is run.
     * @return The task that was scheduled.
     */
    fun runDelayed(
        consumer: Consumer<TaskImplementation<Void>>,
        retired: Runnable?,
        delay: Long,
    ): TaskImplementation<Void>? {
        val wrapperFunction = Function<TaskImplementation<Void>, Void> { task ->
            consumer.accept(task)
            null
        }
        return runDelayed(wrapperFunction, retired, delay)
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param runnable The task to run.
     * @param retired The task to run if the entity is retired.
     * @param delay The delay before the task is run.
     * @return The task that was scheduled.
     */
    fun runDelayed(
        runnable: Runnable,
        retired: Runnable?,
        delay: Long,
    ): TaskImplementation<Void>? {
        val wrapperFunction = Function<TaskImplementation<Void>, Void> { task ->
            runnable.run()
            null
        }
        return runDelayed(wrapperFunction, retired, delay)
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param function The task to run.
     * @param delay The delay before the task is run.
     * @return The task that was scheduled.
     */
    fun <T : Any> runDelayed(
        function: Function<TaskImplementation<T>, T>,
        delay: Long,
    ): TaskImplementation<T>? {
        return runDelayed(function, null, delay)
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @param delay The delay before the task is run.
     * @return The task that was scheduled.
     */
    fun runDelayed(
        consumer: Consumer<TaskImplementation<Void>>,
        delay: Long,
    ): TaskImplementation<Void>? {
        return runDelayed(consumer, null, delay)
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param runnable The task to run.
     * @param delay The delay before the task is run.
     * @return The task that was scheduled.
     */
    fun runDelayed(
        runnable: Runnable,
        delay: Long,
    ): TaskImplementation<Void>? {
        return runDelayed(runnable, null, delay)
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
    fun <T : Any> runAtFixedRate(
        function: Function<TaskImplementation<T>, T>,
        retired: Runnable?,
        delay: Long,
        period: Long,
    ): TaskImplementation<T>?

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @param retired The task to run if the entity is retired.
     * @param delay The delay before the task is run.
     * @param period The period between each run.
     * @return The task that was scheduled.
     */
    fun runAtFixedRate(
        consumer: Consumer<TaskImplementation<Void>>,
        retired: Runnable?,
        delay: Long,
        period: Long,
    ): TaskImplementation<Void>? {
        val wrapperFunction = Function<TaskImplementation<Void>, Void> { task ->
            consumer.accept(task)
            null
        }
        return runAtFixedRate(wrapperFunction, retired, delay, period)
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
    fun runAtFixedRate(
        runnable: Runnable,
        retired: Runnable?,
        delay: Long,
        period: Long,
    ): TaskImplementation<Void>? {
        val wrapperFunction = Function<TaskImplementation<Void>, Void> { task ->
            runnable.run()
            null
        }
        return runAtFixedRate(wrapperFunction, retired, delay, period)
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param function The task to run.
     * @param delay The delay before the task is run.
     * @param period The period between each run.
     * @return The task that was scheduled.
     */
    fun <T : Any> runAtFixedRate(
        function: Function<TaskImplementation<T>, T>,
        delay: Long,
        period: Long,
    ): TaskImplementation<T>? {
        return runAtFixedRate(function, null, delay, period)
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @param delay The delay before the task is run.
     * @param period The period between each run.
     * @return The task that was scheduled.
     */
    fun runAtFixedRate(
        consumer: Consumer<TaskImplementation<Void>>,
        delay: Long,
        period: Long,
    ): TaskImplementation<Void>? {
        return runAtFixedRate(consumer, null, delay, period)
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param runnable The task to run.
     * @param delay The delay before the task is run.
     * @param period The period between each run.
     * @return The task that was scheduled.
     */
    fun runAtFixedRate(
        runnable: Runnable,
        delay: Long,
        period: Long,
    ): TaskImplementation<Void>? {
        return runAtFixedRate(runnable, null, delay, period)
    }
}

