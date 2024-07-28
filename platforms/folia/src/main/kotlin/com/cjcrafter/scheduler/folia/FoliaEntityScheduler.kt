package com.cjcrafter.scheduler.folia

import com.cjcrafter.scheduler.EntitySchedulerImplementation
import com.cjcrafter.scheduler.TaskImplementation
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.ApiStatus
import java.util.function.Consumer

@ApiStatus.Internal
class FoliaEntityScheduler(private val plugin: Plugin, entity: Entity) : EntitySchedulerImplementation {

    private val entityScheduler = entity.scheduler

    override fun execute(run: Runnable, retired: Runnable?, delay: Long): Boolean {
        return entityScheduler.execute(plugin, run, retired, delay)
    }

    override fun run(consumer: Consumer<TaskImplementation>, retired: Runnable?): TaskImplementation? {
        val taskImplementation = FoliaTask()
        val foliaConsumer: Consumer<ScheduledTask> = Consumer { scheduledTask ->
            taskImplementation.scheduledTask = scheduledTask  // updating is probably not necessary
            consumer.accept(taskImplementation)
            taskImplementation.asFuture().complete(taskImplementation)
        }

        val scheduledTask = entityScheduler.run(plugin, foliaConsumer, retired) ?: return null
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun runDelayed(
        consumer: Consumer<TaskImplementation>,
        retired: Runnable?,
        delay: Long
    ): TaskImplementation? {
        val taskImplementation = FoliaTask()
        val foliaConsumer: Consumer<ScheduledTask> = Consumer { task ->
            taskImplementation.scheduledTask = task  // updating is probably not necessary
            consumer.accept(taskImplementation)
            taskImplementation.asFuture().complete(taskImplementation)
        }

        val scheduledTask = entityScheduler.runDelayed(plugin, foliaConsumer, retired, delay) ?: return null
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun runAtFixedRate(
        consumer: Consumer<TaskImplementation>,
        retired: Runnable?,
        delay: Long,
        period: Long
    ): TaskImplementation? {
        val taskImplementation = FoliaTask()
        val foliaConsumer: Consumer<ScheduledTask> = Consumer { task ->
            taskImplementation.scheduledTask = task  // updating is probably not necessary
            consumer.accept(taskImplementation)
            taskImplementation.asFuture().complete(taskImplementation)
        }

        val scheduledTask = entityScheduler.runAtFixedRate(plugin, foliaConsumer, retired, delay, period) ?: return null
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }
}
