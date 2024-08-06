package com.cjcrafter.scheduler.bukkit

import com.cjcrafter.scheduler.TaskImplementation
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class BukkitTask<T : Any>(
    override val owningPlugin: Plugin,
    private val isRepeatingTask: Boolean,
) : TaskImplementation<T> {

    private val lock = ReentrantLock()
    private val scheduledTaskRef = AtomicReference<BukkitTask>()
    private val future: CompletableFuture<TaskImplementation<T>> = CompletableFuture()
    private var callback: T? = null

    @ApiStatus.Internal
    fun setScheduledTask(scheduledTask: BukkitTask) {
        scheduledTaskRef.set(scheduledTask)
    }

    override fun cancel() {
        scheduledTaskRef.get().cancel()
    }

    override fun isCancelled(): Boolean {
        return scheduledTaskRef.get().isCancelled
    }

    override fun isRunning(): Boolean {
        return owningPlugin.server.scheduler.isCurrentlyRunning(scheduledTaskRef.get().taskId)
    }

    override fun isRepeatingTask(): Boolean {
        return isRepeatingTask
    }

    override fun getCallback(): T? {
        return lock.withLock { callback }
    }

    fun setCallback(callback: T?) {
        lock.withLock { this.callback = callback }
    }

    override fun asFuture(): CompletableFuture<TaskImplementation<T>> {
        return future
    }
}