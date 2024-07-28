package com.cjcrafter.scheduler.folia

import com.cjcrafter.scheduler.SchedulerImplementation
import com.cjcrafter.scheduler.TaskImplementation
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.World
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.ApiStatus
import java.util.function.Consumer

@ApiStatus.Internal
internal class FoliaRegionScheduler(
    private val plugin: Plugin,
    private val world: World,
    private val chunkX: Int,
    private val chunkZ: Int,
) : SchedulerImplementation {

    private val regionScheduler = plugin.server.regionScheduler

    override fun execute(run: Runnable) {
        regionScheduler.execute(plugin, world, chunkX, chunkZ, run)
    }

    override fun run(consumer: Consumer<TaskImplementation>): TaskImplementation {
        val taskImplementation = FoliaTask()
        val foliaConsumer: Consumer<ScheduledTask> = Consumer { scheduledTask ->
            taskImplementation.scheduledTask = scheduledTask  // updating is probably not necessary
            consumer.accept(taskImplementation)
            taskImplementation.asFuture().complete(taskImplementation)
        }

        val scheduledTask = regionScheduler.run(plugin, world, chunkX, chunkZ, foliaConsumer)
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

        val scheduledTask = regionScheduler.runDelayed(plugin, world, chunkX, chunkZ, foliaConsumer, delay)
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

        val scheduledTask = regionScheduler.runAtFixedRate(plugin, world, chunkX, chunkZ, foliaConsumer, delay, period)
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }
}
