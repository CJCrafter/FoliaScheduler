package com.cjcrafter.scheduler.bukkit

import com.cjcrafter.scheduler.TaskImplementation
import org.bukkit.plugin.Plugin
import java.util.concurrent.CompletableFuture

class BukkitTask<T : Any>(
    override val owningPlugin: Plugin,
    private val isRepeatingTask: Boolean,
) : TaskImplementation<T> {

    lateinit var scheduledTask: org.bukkit.scheduler.BukkitTask
    private val future: CompletableFuture<TaskImplementation<T>> = CompletableFuture()
    internal var callback: T? = null

    override fun cancel() {
        scheduledTask.cancel()
    }

    override fun isCancelled(): Boolean {
        return scheduledTask.isCancelled
    }

    override fun isRunning(): Boolean {
        return owningPlugin.server.scheduler.isCurrentlyRunning(scheduledTask.taskId)
    }

    override fun isRepeatingTask(): Boolean {
        return isRepeatingTask
    }

    override fun getCallback(): T? {
        return callback
    }

    override fun asFuture(): CompletableFuture<TaskImplementation<T>> {
        return future
    }
}