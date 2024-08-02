package com.cjcrafter.scheduler.bukkit

import com.cjcrafter.scheduler.EntitySchedulerImplementation
import com.cjcrafter.scheduler.TaskImplementation
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.jetbrains.annotations.ApiStatus
import java.util.function.Consumer
import java.util.function.Function

@ApiStatus.Internal
internal class BukkitEntityScheduler(
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

    override fun <T : Any> run(
        function: Function<TaskImplementation<T>, T>,
        retired: Runnable?,
    ): TaskImplementation<T>? {
        if (!entity.isValid) {
            retired?.run()
            return null
        }

        val taskImplementation = BukkitTask<T>(plugin, false)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                if (!entity.isValid) {
                    retired?.run()
                    return
                }
                val callback = function.apply(taskImplementation)
                taskImplementation.callback = callback
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTask(plugin)

        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun <T : Any> runDelayed(
        function: Function<TaskImplementation<T>, T>,
        retired: Runnable?,
        delay: Long,
    ): TaskImplementation<T>? {
        if (!entity.isValid) {
            retired?.run()
            return null
        }

        val taskImplementation = BukkitTask<T>(plugin, false)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                if (!entity.isValid) {
                    retired?.run()
                    return
                }
                val callback = function.apply(taskImplementation)
                taskImplementation.callback = callback
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTaskLater(plugin, delay)

        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun <T : Any> runAtFixedRate(
        function: Function<TaskImplementation<T>, T>,
        retired: Runnable?,
        delay: Long,
        period: Long
    ): TaskImplementation<T>? {
        if (!entity.isValid) {
            retired?.run()
            return null
        }

        val taskImplementation = BukkitTask<T>(plugin, true)
        val scheduledTask = object : BukkitRunnable() {
            override fun run() {
                if (!entity.isValid) {
                    retired?.run()
                    return
                }
                val callback = function.apply(taskImplementation)
                taskImplementation.callback = callback
                taskImplementation.asFuture().complete(taskImplementation)
            }
        }.runTaskTimer(plugin, delay, period)

        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }
}
