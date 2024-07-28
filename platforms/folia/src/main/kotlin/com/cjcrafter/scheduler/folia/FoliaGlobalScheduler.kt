package com.cjcrafter.scheduler.folia

import com.cjcrafter.scheduler.SchedulerImplementation
import com.cjcrafter.scheduler.TaskImplementation
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.ApiStatus
import java.util.function.Consumer

@ApiStatus.Internal
internal class FoliaGlobalScheduler(private val plugin: Plugin) : SchedulerImplementation {

    private val globalRegionScheduler = plugin.server.globalRegionScheduler

    override fun execute(run: Runnable) {
        globalRegionScheduler.execute(plugin, run)
    }

    override fun run(consumer: Consumer<TaskImplementation>): TaskImplementation {
        val taskImplementation = FoliaTask()
        val foliaConsumer: Consumer<ScheduledTask> = Consumer { scheduledTask ->
            taskImplementation.scheduledTask = scheduledTask  // updating is probably not necessary
            consumer.accept(taskImplementation)
            taskImplementation.asFuture().complete(taskImplementation)
        }

        val scheduledTask = globalRegionScheduler.run(plugin, foliaConsumer)
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun runDelayed(consumer: Consumer<TaskImplementation>, delay: Long): TaskImplementation {
        val taskImplementation = FoliaTask()
        val foliaConsumer: Consumer<ScheduledTask> = Consumer { scheduledTask ->
            taskImplementation.scheduledTask = scheduledTask  // updating is probably not necessary
            consumer.accept(taskImplementation)
            taskImplementation.asFuture().complete(taskImplementation)
        }

        val scheduledTask = globalRegionScheduler.runDelayed(plugin, foliaConsumer, delay)
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun runAtFixedRate(consumer: Consumer<TaskImplementation>, delay: Long, period: Long): TaskImplementation {
        val taskImplementation = FoliaTask()
        val foliaConsumer: Consumer<ScheduledTask> = Consumer { scheduledTask ->
            taskImplementation.scheduledTask = scheduledTask  // updating is probably not necessary
            consumer.accept(taskImplementation)
            taskImplementation.asFuture().complete(taskImplementation)
        }

        val scheduledTask = globalRegionScheduler.runAtFixedRate(plugin, foliaConsumer, delay, period)
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }
}
