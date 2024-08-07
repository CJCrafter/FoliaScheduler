package com.cjcrafter.scheduler

import java.util.function.Consumer
import java.util.function.Function

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
     * Schedules a task to be run after a delay.
     *
     * @param function The task to run.
     * @return The task that was scheduled.
     */
    fun <T : Any> run(function: Function<TaskImplementation<T>, T?>): TaskImplementation<T>

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @return The task that was scheduled.
     */
    fun run(consumer: Consumer<TaskImplementation<Void>>): TaskImplementation<Void> {
        val wrapperFunction = Function<TaskImplementation<Void>, Void?> { task ->
            consumer.accept(task)
            null
        }
        return run(wrapperFunction)
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param runnable The task to run.
     * @return The task that was scheduled.
     */
    fun run(runnable: Runnable): TaskImplementation<Void> {
        val wrapperFunction = Function<TaskImplementation<Void>, Void?> { task ->
            runnable.run()
            null
        }
        return run(wrapperFunction)
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param function The task to run.
     * @param delay The delay in ticks before the task is run.
     * @return The task that was scheduled.
     */
    fun <T : Any> runDelayed(
        function: Function<TaskImplementation<T>, T?>,
        delay: Long,
    ): TaskImplementation<T>

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @param delay The delay in ticks before the task is run.
     * @return The task that was scheduled.
     */
    fun runDelayed(
        consumer: Consumer<TaskImplementation<Void>>,
        delay: Long,
    ): TaskImplementation<Void> {
        val wrapperFunction = Function<TaskImplementation<Void>, Void?> { task ->
            consumer.accept(task)
            null
        }
        return runDelayed(wrapperFunction, delay)

    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param runnable The task to run.
     * @param delay The delay in ticks before the task is run.
     * @return The task that was scheduled.
     */
    fun runDelayed(
        runnable: Runnable,
        delay: Long,
    ): TaskImplementation<Void> {
        val wrapperFunction = Function<TaskImplementation<Void>, Void?> { task ->
            runnable.run()
            null
        }
        return runDelayed(wrapperFunction, delay)
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param function The task to run.
     * @param delay The delay before the task is run.
     * @param period The time unit of the delay.
     */
    fun <T : Any> runAtFixedRate(
        function: Function<TaskImplementation<T>, T?>,
        delay: Long,
        period: Long,
    ): TaskImplementation<T>

    /**
     * Schedules a task to be run after a delay.
     *
     * @param consumer The task to run.
     * @param delay The delay before the task is run.
     * @param period The time unit of the delay.
     */
    fun runAtFixedRate(
        consumer: Consumer<TaskImplementation<Void>>,
        delay: Long,
        period: Long,
    ): TaskImplementation<Void> {
        val wrapperFunction = Function<TaskImplementation<Void>, Void?> { task ->
            consumer.accept(task)
            null
        }
        return runAtFixedRate(wrapperFunction, delay, period)
    }

    /**
     * Schedules a task to be run after a delay.
     *
     * @param runnable The task to run.
     * @param delay The delay before the task is run.
     * @param period The time unit of the delay.
     */
    fun runAtFixedRate(
        runnable: Runnable,
        delay: Long,
        period: Long,
    ): TaskImplementation<Void> {
        val wrapperFunction = Function<TaskImplementation<Void>, Void?> { task ->
            runnable.run()
            null
        }
        return runAtFixedRate(wrapperFunction, delay, period)
    }
}
