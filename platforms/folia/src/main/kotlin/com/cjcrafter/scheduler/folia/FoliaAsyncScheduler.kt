package com.cjcrafter.scheduler.folia

import com.cjcrafter.scheduler.AsyncSchedulerImplementation
import com.cjcrafter.scheduler.TaskImplementation
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

@ApiStatus.Internal
internal class FoliaAsyncScheduler(private val plugin: Plugin) : AsyncSchedulerImplementation {

    private val asyncScheduler = plugin.server.asyncScheduler

    override fun runNow(consumer: Consumer<TaskImplementation>): TaskImplementation {
        val taskImplementation = FoliaTask()
        val foliaConsumer: Consumer<ScheduledTask> = Consumer { scheduledTask ->
            taskImplementation.scheduledTask = scheduledTask  // updating is probably not necessary
            consumer.accept(taskImplementation)
            taskImplementation.asFuture().complete(taskImplementation)
        }

        val scheduledTask = asyncScheduler.runNow(plugin, foliaConsumer)
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun runDelayed(consumer: Consumer<TaskImplementation>, delay: Long, unit: TimeUnit): TaskImplementation {
        val taskImplementation = FoliaTask()
        val foliaConsumer: Consumer<ScheduledTask> = Consumer { scheduledTask ->
            taskImplementation.scheduledTask = scheduledTask  // updating is probably not necessary
            consumer.accept(taskImplementation)
            taskImplementation.asFuture().complete(taskImplementation)
        }

        val scheduledTask = asyncScheduler.runDelayed(plugin, foliaConsumer, delay, unit)
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun runAtFixedRate(
        consumer: Consumer<TaskImplementation>,
        delay: Long,
        period: Long,
        unit: TimeUnit
    ): TaskImplementation {
        val taskImplementation = FoliaTask()
        val foliaConsumer: Consumer<ScheduledTask> = Consumer { scheduledTask ->
            taskImplementation.scheduledTask = scheduledTask  // updating is probably not necessary
            consumer.accept(taskImplementation)
            taskImplementation.asFuture().complete(taskImplementation)
        }

        val scheduledTask = asyncScheduler.runAtFixedRate(plugin, foliaConsumer, delay, period, unit)
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }
}
