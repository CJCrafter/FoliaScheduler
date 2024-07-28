package com.cjcrafter.scheduler.bukkit

import com.cjcrafter.scheduler.AsyncSchedulerImplementation
import com.cjcrafter.scheduler.TaskImplementation
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class BukkitAsyncScheduler(private val plugin: Plugin) : AsyncSchedulerImplementation {
    override fun runNow(consumer: Consumer<TaskImplementation>): TaskImplementation {
        val taskImplementation = BukkitTask(plugin, false)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                consumer.accept(taskImplementation)
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTaskAsynchronously(plugin)

        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun runDelayed(consumer: Consumer<TaskImplementation>, delay: Long, unit: TimeUnit): TaskImplementation {
        val taskImplementation = BukkitTask(plugin, false)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                consumer.accept(taskImplementation)
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTaskLaterAsynchronously(plugin, unit.toMillis(delay) / 50)

        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun runAtFixedRate(
        consumer: Consumer<TaskImplementation>,
        delay: Long,
        period: Long,
        unit: TimeUnit
    ): TaskImplementation {
        val taskImplementation = BukkitTask(plugin, true)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                consumer.accept(taskImplementation)
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTaskTimerAsynchronously(plugin, unit.toMillis(delay) / 50, unit.toMillis(period) / 50)

        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }


}