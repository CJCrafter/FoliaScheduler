package com.cjcrafter.scheduler

import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.ApiStatus
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.function.Function

interface TaskImplementation<T : Any> {

    /**
     * The plugin that ran this task.
     */
    val owningPlugin: Plugin

    /**
     * Cancels the task.
     */
    fun cancel()

    /**
     * Returns whether the task has been cancelled.
     */
    fun isCancelled(): Boolean

    /**
     * Returns whether the task is currently executing.
     */
    fun isRunning(): Boolean

    /**
     * Returns whether the task is a repeating task.
     */
    fun isRepeatingTask(): Boolean

    /**
     * Returns the callback from this task's execution.
     */
    fun getCallback(): T?

    /**
     * Returns a CompletableFuture that will be completed when the task has finished executing.
     * For repeating tasks, the future will be completed on the first loop execution.
     *
     * The future may not be completed if the task is cancelled before it is run, or if an
     * exception occurs during execution of the task.
     */
    fun asFuture(): CompletableFuture<TaskImplementation<T>>
}
