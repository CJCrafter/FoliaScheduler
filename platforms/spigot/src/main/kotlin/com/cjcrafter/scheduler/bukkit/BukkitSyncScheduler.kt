package com.cjcrafter.scheduler.bukkit

import com.cjcrafter.scheduler.SchedulerImplementation
import com.cjcrafter.scheduler.TaskImplementation
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.jetbrains.annotations.ApiStatus
import java.util.function.Consumer
import java.util.function.Function

@ApiStatus.Internal
internal class BukkitSyncScheduler(private val plugin: Plugin) : SchedulerImplementation {
    override fun execute(run: Runnable) {
        object : BukkitRunnable() {
            override fun run() {
                run.run()
            }
        }.runTask(plugin)
    }

    override fun <T : Any> run(function: Function<TaskImplementation<T>, T>): TaskImplementation<T> {
        val taskImplementation = BukkitTask<T>(plugin, false)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                val callback = function.apply(taskImplementation)
                taskImplementation.setCallback(callback)
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTask(plugin)

        taskImplementation.setScheduledTask(scheduledTask)
        return taskImplementation
    }

    override fun <T : Any> runDelayed(
        function: Function<TaskImplementation<T>, T>,
        delay: Long,
    ): TaskImplementation<T> {
        val taskImplementation = BukkitTask<T>(plugin, false)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                val callback = function.apply(taskImplementation)
                taskImplementation.setCallback(callback)
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTaskLater(plugin, delay)

        taskImplementation.setScheduledTask(scheduledTask)
        return taskImplementation
    }

    override fun <T : Any> runAtFixedRate(
        function: Function<TaskImplementation<T>, T>,
        delay: Long,
        period: Long,
    ): TaskImplementation<T> {
        val taskImplementation = BukkitTask<T>(plugin, true)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                val callback = function.apply(taskImplementation)
                taskImplementation.setCallback(callback)
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTaskTimer(plugin, delay, period)

        taskImplementation.setScheduledTask(scheduledTask)
        return taskImplementation
    }
}
