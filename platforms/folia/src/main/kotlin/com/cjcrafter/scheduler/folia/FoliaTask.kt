package com.cjcrafter.scheduler.folia

import com.cjcrafter.scheduler.TaskImplementation
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.plugin.Plugin
import java.util.*
import java.util.concurrent.CompletableFuture

class FoliaTask<T : Any> : TaskImplementation<T> {

    lateinit var scheduledTask: ScheduledTask
    private val future: CompletableFuture<TaskImplementation<T>> = CompletableFuture()
    internal var callback: T? = null

    override val owningPlugin: Plugin
        get() = scheduledTask.owningPlugin

    override fun cancel() {
        scheduledTask.cancel()
    }

    override fun isCancelled(): Boolean {
        return scheduledTask.isCancelled
    }

    override fun isRunning(): Boolean {
        return scheduledTask.executionState == ScheduledTask.ExecutionState.RUNNING
                || scheduledTask.executionState == ScheduledTask.ExecutionState.CANCELLED_RUNNING
    }

    override fun isRepeatingTask(): Boolean {
        return scheduledTask.isRepeatingTask
    }

    override fun getCallback(): T? {
        return callback
    }

    override fun asFuture(): CompletableFuture<TaskImplementation<T>> {
        return future
    }
}
