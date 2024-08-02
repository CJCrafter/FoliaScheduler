package com.cjcrafter.scheduler.folia

import com.cjcrafter.scheduler.EntitySchedulerImplementation
import com.cjcrafter.scheduler.TaskImplementation
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.ApiStatus
import java.util.function.Consumer
import java.util.function.Function

@ApiStatus.Internal
internal class FoliaEntityScheduler(private val plugin: Plugin, entity: Entity) : EntitySchedulerImplementation {

    private val entityScheduler = entity.scheduler

    private fun <T : Any> buildFoliaConsumer(
        taskImplementation: FoliaTask<T>,
        function: Function<TaskImplementation<T>, T>,
    ): Consumer<ScheduledTask> {
        return Consumer { scheduledTask ->
            taskImplementation.scheduledTask = scheduledTask  // updating is probably not necessary
            val callback = function.apply(taskImplementation)
            taskImplementation.callback = callback
            taskImplementation.asFuture().complete(taskImplementation)
        }
    }

    override fun execute(run: Runnable, retired: Runnable?, delay: Long): Boolean {
        return entityScheduler.execute(plugin, run, retired, delay)
    }

    override fun <T : Any> run(
        function: Function<TaskImplementation<T>, T>,
        retired: Runnable?,
    ): TaskImplementation<T>? {
        val taskImplementation = FoliaTask<T>()
        val foliaConsumer = buildFoliaConsumer(taskImplementation, function)
        val scheduledTask = entityScheduler.run(plugin, foliaConsumer, retired) ?: return null
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun <T : Any> runDelayed(
        function: Function<TaskImplementation<T>, T>,
        retired: Runnable?,
        delay: Long,
    ): TaskImplementation<T>? {
        val taskImplementation = FoliaTask<T>()
        val foliaConsumer = buildFoliaConsumer(taskImplementation, function)
        val scheduledTask = entityScheduler.runDelayed(plugin, foliaConsumer, retired, delay) ?: return null
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun <T : Any> runAtFixedRate(
        function: Function<TaskImplementation<T>, T>,
        retired: Runnable?,
        delay: Long,
        period: Long
    ): TaskImplementation<T>? {
        val taskImplementation = FoliaTask<T>()
        val foliaConsumer = buildFoliaConsumer(taskImplementation, function)
        val scheduledTask = entityScheduler.runAtFixedRate(plugin, foliaConsumer, retired, delay, period) ?: return null
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }
}
