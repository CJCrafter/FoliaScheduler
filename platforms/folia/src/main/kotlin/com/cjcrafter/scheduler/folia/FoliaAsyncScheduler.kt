package com.cjcrafter.scheduler.folia

import com.cjcrafter.scheduler.AsyncSchedulerImplementation
import com.cjcrafter.scheduler.TaskImplementation
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.function.Function

@ApiStatus.Internal
internal class FoliaAsyncScheduler(private val plugin: Plugin) : AsyncSchedulerImplementation {

    private val asyncScheduler = plugin.server.asyncScheduler

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

    override fun <T : Any> runNow(function: Function<TaskImplementation<T>, T>): TaskImplementation<T> {
        val taskImplementation = FoliaTask<T>()
        val foliaConsumer = buildFoliaConsumer(taskImplementation, function)
        val scheduledTask = asyncScheduler.runNow(plugin, foliaConsumer)
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun <T : Any> runDelayed(
        function: Function<TaskImplementation<T>, T>,
        delay: Long,
        unit: TimeUnit,
    ): TaskImplementation<T> {
        val taskImplementation = FoliaTask<T>()
        val foliaConsumer = buildFoliaConsumer(taskImplementation, function)
        val scheduledTask = asyncScheduler.runDelayed(plugin, foliaConsumer, delay, unit)
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun <T : Any> runAtFixedRate(
        function: Function<TaskImplementation<T>, T>,
        delay: Long,
        period: Long,
        unit: TimeUnit,
    ): TaskImplementation<T> {
        val taskImplementation = FoliaTask<T>()
        val foliaConsumer = buildFoliaConsumer(taskImplementation, function)
        val scheduledTask = asyncScheduler.runAtFixedRate(plugin, foliaConsumer, delay, period, unit)
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }
}
