package com.cjcrafter.scheduler.bukkit

import com.cjcrafter.scheduler.SchedulerImplementation
import com.cjcrafter.scheduler.TaskImplementation
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.function.Consumer

class BukkitSyncScheduler(private val plugin: Plugin) : SchedulerImplementation {
    override fun execute(run: Runnable) {
        object : BukkitRunnable() {
            override fun run() {
                run.run()
            }
        }.runTask(plugin)
    }

    override fun run(consumer: Consumer<TaskImplementation>): TaskImplementation {
        val taskImplementation = BukkitTask(plugin, false)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                consumer.accept(taskImplementation)
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTask(plugin)

        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun runDelayed(consumer: Consumer<TaskImplementation>, delay: Long): TaskImplementation {
        val taskImplementation = BukkitTask(plugin, false)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                consumer.accept(taskImplementation)
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTaskLater(plugin, delay)

        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun runAtFixedRate(consumer: Consumer<TaskImplementation>, delay: Long, period: Long): TaskImplementation {
        val taskImplementation = BukkitTask(plugin, true)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                consumer.accept(taskImplementation)
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTaskTimer(plugin, delay, period)

        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }
}