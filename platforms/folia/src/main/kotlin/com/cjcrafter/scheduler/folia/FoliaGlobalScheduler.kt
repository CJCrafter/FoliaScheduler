package com.cjcrafter.scheduler.folia

import com.cjcrafter.scheduler.SchedulerImplementation
import com.cjcrafter.scheduler.TaskImplementation
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.ApiStatus
import java.util.function.Consumer
import java.util.function.Function

@ApiStatus.Internal
internal class FoliaGlobalScheduler(private val plugin: Plugin) : SchedulerImplementation {

    private val globalRegionScheduler = plugin.server.globalRegionScheduler

    private fun <T : Any> buildFoliaConsumer(
        taskImplementation: FoliaTask<T>,
        function: Function<TaskImplementation<T>, T>,
    ): Consumer<ScheduledTask> {
        return Consumer { scheduledTask ->
            taskImplementation.setScheduledTask(scheduledTask)  // updating is probably not necessary
            val callback = function.apply(taskImplementation)
            taskImplementation.setCallback(callback)
            taskImplementation.asFuture().complete(taskImplementation)
        }
    }

    override fun execute(run: Runnable) {
        globalRegionScheduler.execute(plugin, run)
    }

    override fun <T : Any> run(function: Function<TaskImplementation<T>, T>): TaskImplementation<T> {
        val taskImplementation = FoliaTask<T>()
        val foliaConsumer = buildFoliaConsumer(taskImplementation, function)
        val scheduledTask = globalRegionScheduler.run(plugin, foliaConsumer)
        taskImplementation.setScheduledTask(scheduledTask)
        return taskImplementation
    }

    override fun <T : Any> runDelayed(
        function: Function<TaskImplementation<T>, T>,
        delay: Long,
    ): TaskImplementation<T> {
        val taskImplementation = FoliaTask<T>()
        val foliaConsumer = buildFoliaConsumer(taskImplementation, function)
        val scheduledTask = globalRegionScheduler.runDelayed(plugin, foliaConsumer, delay)
        taskImplementation.setScheduledTask(scheduledTask)
        return taskImplementation
    }

    override fun <T : Any> runAtFixedRate(
        function: Function<TaskImplementation<T>, T>,
        delay: Long,
        period: Long,
    ): TaskImplementation<T> {
        val taskImplementation = FoliaTask<T>()
        val foliaConsumer = buildFoliaConsumer(taskImplementation, function)
        val scheduledTask = globalRegionScheduler.runAtFixedRate(plugin, foliaConsumer, delay, period)
        taskImplementation.setScheduledTask(scheduledTask)
        return taskImplementation
    }
}
