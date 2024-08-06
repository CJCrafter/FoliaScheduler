package com.cjcrafter.scheduler.bukkit

import com.cjcrafter.scheduler.AsyncSchedulerImplementation
import com.cjcrafter.scheduler.TaskImplementation
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.function.Function

@ApiStatus.Internal
internal class BukkitAsyncScheduler(private val plugin: Plugin) : AsyncSchedulerImplementation {
    override fun <T : Any> runNow(function: Function<TaskImplementation<T>, T>): TaskImplementation<T> {
        val taskImplementation = BukkitTask<T>(plugin, false)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                val callback = function.apply(taskImplementation)
                taskImplementation.setCallback(callback)
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTaskAsynchronously(plugin)

        taskImplementation.setScheduledTask(scheduledTask)
        return taskImplementation
    }

    override fun <T : Any> runDelayed(
        function: Function<TaskImplementation<T>, T>,
        delay: Long,
        unit: TimeUnit,
    ): TaskImplementation<T> {
        val taskImplementation = BukkitTask<T>(plugin, false)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                val callback = function.apply(taskImplementation)
                taskImplementation.setCallback(callback)
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTaskLaterAsynchronously(plugin, unit.toMillis(delay) / 50)

        taskImplementation.setScheduledTask(scheduledTask)
        return taskImplementation
    }

    override fun <T : Any> runAtFixedRate(
        function: Function<TaskImplementation<T>, T>,
        delay: Long,
        period: Long,
        unit: TimeUnit
    ): TaskImplementation<T> {
        val taskImplementation = BukkitTask<T>(plugin, true)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                val callback = function.apply(taskImplementation)
                taskImplementation.setCallback(callback)
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTaskTimerAsynchronously(plugin, unit.toMillis(delay) / 50, unit.toMillis(period) / 50)

        taskImplementation.setScheduledTask(scheduledTask)
        return taskImplementation
    }
}
