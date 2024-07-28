package com.cjcrafter.scheduler.bukkit

import com.cjcrafter.scheduler.EntitySchedulerImplementation
import com.cjcrafter.scheduler.TaskImplementation
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.function.Consumer

class BukkitEntityScheduler(
    private val plugin: Plugin,
    private val entity: Entity,
): EntitySchedulerImplementation {
    override fun execute(run: Runnable, retired: Runnable?, delay: Long): Boolean {
        if (!entity.isValid) {
            retired?.run()
            return false
        }

        object : BukkitRunnable() {
            override fun run() {
                if (!entity.isValid) {
                    retired?.run()
                    return
                }
                run.run()
            }
        }.runTask(plugin)
        return true
    }

    override fun run(consumer: Consumer<TaskImplementation>, retired: Runnable?): TaskImplementation? {
        if (!entity.isValid) {
            retired?.run()
            return null
        }

        val taskImplementation = BukkitTask(plugin, false)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                if (!entity.isValid) {
                    retired?.run()
                    return
                }
                consumer.accept(taskImplementation)
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTask(plugin)

        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun runDelayed(
        consumer: Consumer<TaskImplementation>,
        retired: Runnable?,
        delay: Long
    ): TaskImplementation? {
        if (!entity.isValid) {
            retired?.run()
            return null
        }

        val taskImplementation = BukkitTask(plugin, false)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                if (!entity.isValid) {
                    retired?.run()
                    return
                }
                consumer.accept(taskImplementation)
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTaskLater(plugin, delay)

        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun runAtFixedRate(
        consumer: Consumer<TaskImplementation>,
        retired: Runnable?,
        delay: Long,
        period: Long
    ): TaskImplementation? {
        if (!entity.isValid) {
            retired?.run()
            return null
        }

        val taskImplementation = BukkitTask(plugin, true)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                if (!entity.isValid) {
                    retired?.run()
                    return
                }
                consumer.accept(taskImplementation)
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTaskTimer(plugin, delay, period)

        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }
}
