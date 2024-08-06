package com.cjcrafter.scheduler

import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.function.Function

/**
 * Represents a scheduler that can schedule tasks to be run asynchronously (separate from the server thread(s)).
 */
interface AsyncSchedulerImplementation {

    /**
     * Runs a task asynchronously immediately.
     */
    fun <T : Any> runNow(function: Function<TaskImplementation<T>, T>): TaskImplementation<T>

    /**
     * Runs a task asynchronously immediately.
     */
    fun <T : Any> runNow(consumer: Consumer<TaskImplementation<Void>>): TaskImplementation<Void> {
        val wrapperFunction = Function<TaskImplementation<Void>, Void> { task ->
            consumer.accept(task)
            null
        }
        return this.runNow(wrapperFunction)
    }

    /**
     * Runs a task asynchronously immediately.
     */
    fun runNow(runnable: Runnable): TaskImplementation<Void> {
        val wrapperFunction = Function<TaskImplementation<Void>, Void> { task ->
            runnable.run()
            null
        }
        return this.runNow(wrapperFunction)
    }

    fun <T : Any> runDelayed(
        function: Function<TaskImplementation<T>, T>,
        delay: Long,
        unit: TimeUnit,
    ): TaskImplementation<T>

    /**
     * Schedules a task to be run asynchronously after a delay.
     *
     * @param consumer The task to run.
     * @param delay The delay before the task is run.
     * @param unit The time unit of the delay.
     * @return The task that was scheduled.
     */
    fun runDelayed(
        consumer: Consumer<TaskImplementation<Void>>,
        delay: Long,
        unit: TimeUnit,
    ): TaskImplementation<Void> {
        val wrapperFunction = Function<TaskImplementation<Void>, Void> { task ->
            consumer.accept(task)
            null
        }
        return this.runDelayed(wrapperFunction, delay, unit)
    }

    /**
     * Schedules a task to be run asynchronously after a delay.
     *
     * @param runnable The task to run.
     * @param delay The delay before the task is run.
     * @param unit The time unit of the delay.
     * @return The task that was scheduled.
     */
    fun runDelayed(
        runnable: Runnable,
        delay: Long,
        unit: TimeUnit,
    ): TaskImplementation<Void> {
        val wrapperFunction = Function<TaskImplementation<Void>, Void> { task ->
            runnable.run()
            null
        }
        return this.runDelayed(wrapperFunction, delay, unit)
    }

    /**
     * Schedules a task to be run asynchronously after a delay, in ticks.
     *
     * @param function The task to run.
     * @param ticks The delay in ticks before the task is run.
     * @return The task that was scheduled.
     */
    fun <T : Any> runDelayed(
        function: Function<TaskImplementation<T>, T>,
        ticks: Long,
    ): TaskImplementation<T> {
        // assumes the time unit is server ticks. Since 20 ticks = 1000ms, we can convert the delay to milliseconds
        return runDelayed(function, ticks * 50, TimeUnit.MILLISECONDS)
    }

    /**
     * Schedules a task to be run asynchronously after a delay, in ticks.
     *
     * @param consumer The task to run.
     * @param ticks The delay in ticks before the task is run.
     * @return The task that was scheduled.
     */
    fun runDelayed(
        consumer: Consumer<TaskImplementation<Void>>,
        ticks: Long,
    ): TaskImplementation<Void> {
        // assumes the time unit is server ticks. Since 20 ticks = 1000ms, we can convert the delay to milliseconds
        return runDelayed(consumer, ticks * 50, TimeUnit.MILLISECONDS)
    }

    /**
     * Schedules a task to be run asynchronously after a delay, in ticks.
     *
     * @param runnable The task to run.
     * @param ticks The delay in ticks before the task is run.
     * @return The task that was scheduled.
     */
    fun runDelayed(
        runnable: Runnable,
        ticks: Long,
    ): TaskImplementation<Void> {
        // assumes the time unit is server ticks. Since 20 ticks = 1000ms, we can convert the delay to milliseconds
        return runDelayed(runnable, ticks * 50, TimeUnit.MILLISECONDS)
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
    fun <T : Any> runAtFixedRate(
        function: Function<TaskImplementation<T>, T>,
        delay: Long,
        period: Long,
        unit: TimeUnit,
    ): TaskImplementation<T>

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
        consumer: Consumer<TaskImplementation<Void>>,
        delay: Long,
        period: Long,
        unit: TimeUnit,
    ): TaskImplementation<Void> {
        val wrapperFunction = Function<TaskImplementation<Void>, Void> { task ->
            consumer.accept(task)
            null
        }
        return this.runAtFixedRate(wrapperFunction, delay, period, unit)
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
    fun runAtFixedRate(
        runnable: Runnable,
        delay: Long,
        period: Long,
        unit: TimeUnit,
    ): TaskImplementation<Void> {
        val wrapperFunction = Function<TaskImplementation<Void>, Void> { task ->
            runnable.run()
            null
        }
        return this.runAtFixedRate(wrapperFunction, delay, period, unit)
    }

    /**
     * Schedules a task to be run asynchronously after a delay and then repeatedly after a period, in ticks.
     *
     * @param function The task to run.
     * @param delay The delay in ticks before the task is run.
     * @param ticks The period in ticks between each run.
     * @return The task that was scheduled.
     */
    fun <T : Any> runAtFixedRate(
        function: Function<TaskImplementation<T>, T>,
        delay: Long,
        ticks: Long,
    ): TaskImplementation<T> {
        // assumes the time unit is server ticks. Since 20 ticks = 1000ms, we can convert the delay to milliseconds
        return runAtFixedRate(function, delay * 50, ticks * 50, TimeUnit.MILLISECONDS)
    }

    /**
     * Schedules a task to be run asynchronously after a delay and then repeatedly after a period, in ticks.
     *
     * @param consumer The task to run.
     * @param delay The delay in ticks before the task is run.
     * @param ticks The period in ticks between each run.
     * @return The task that was scheduled.
     */
    fun runAtFixedRate(
        consumer: Consumer<TaskImplementation<Void>>,
        delay: Long,
        ticks: Long,
    ): TaskImplementation<Void> {
        // assumes the time unit is server ticks. Since 20 ticks = 1000ms, we can convert the delay to milliseconds
        return runAtFixedRate(consumer, delay * 50, ticks * 50, TimeUnit.MILLISECONDS)
    }

    /**
     * Schedules a task to be run asynchronously after a delay and then repeatedly after a period, in ticks.
     *
     * @param runnable The task to run.
     * @param delay The delay in ticks before the task is run.
     * @param ticks The period in ticks between each run.
     * @return The task that was scheduled.
     */
    fun runAtFixedRate(
        runnable: Runnable,
        delay: Long,
        ticks: Long,
    ): TaskImplementation<Void> {
        // assumes the time unit is server ticks. Since 20 ticks = 1000ms, we can convert the delay to milliseconds
        return runAtFixedRate(runnable, delay * 50, ticks * 50, TimeUnit.MILLISECONDS)
    }
}
