package com.cjcrafter.scheduler.folia

import com.cjcrafter.scheduler.TaskImplementation
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.ApiStatus
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class FoliaTask<T : Any> : TaskImplementation<T> {

    private val lock = ReentrantLock()
    private val scheduledTaskRef = AtomicReference<ScheduledTask>()
    private val future: CompletableFuture<TaskImplementation<T>> = CompletableFuture()
    private var callback: T? = null

    override val owningPlugin: Plugin
        get() = scheduledTaskRef.get().owningPlugin

    @ApiStatus.Internal
    fun setScheduledTask(scheduledTask: ScheduledTask) {
        scheduledTaskRef.set(scheduledTask)
    }

    override fun cancel() {
        scheduledTaskRef.get().cancel()
    }

    override fun isCancelled(): Boolean {
        return scheduledTaskRef.get().isCancelled
    }

    override fun isRunning(): Boolean {
        return scheduledTaskRef.get().executionState == ScheduledTask.ExecutionState.RUNNING
                || scheduledTaskRef.get().executionState == ScheduledTask.ExecutionState.CANCELLED_RUNNING
    }

    override fun isRepeatingTask(): Boolean {
        return scheduledTaskRef.get().isRepeatingTask
    }

    override fun getCallback(): T? {
        return lock.withLock { callback }
    }

    @ApiStatus.Internal
    fun setCallback(callback: T?) {
        lock.withLock { this.callback = callback }
    }

    override fun asFuture(): CompletableFuture<TaskImplementation<T>> {
        return future
    }
}
