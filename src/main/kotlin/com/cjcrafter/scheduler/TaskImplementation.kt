package com.cjcrafter.scheduler

import org.bukkit.plugin.Plugin
import java.util.concurrent.CompletableFuture

interface TaskImplementation {

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
     * Returns a CompletableFuture that will be completed when the task has finished executing. For repeating tasks,
     * the future will be completed 1 time for each execution.
     */
    fun asFuture(): CompletableFuture<TaskImplementation>
}
