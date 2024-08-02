package com.cjcrafter.scheduler.folia

import com.cjcrafter.scheduler.SchedulerImplementation
import com.cjcrafter.scheduler.TaskImplementation
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.World
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.ApiStatus
import java.util.function.Consumer
import java.util.function.Function

@ApiStatus.Internal
internal class FoliaRegionScheduler(
    private val plugin: Plugin,
    private val world: World,
    private val chunkX: Int,
    private val chunkZ: Int,
) : SchedulerImplementation {

    private val regionScheduler = plugin.server.regionScheduler

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

    override fun execute(run: Runnable) {
        regionScheduler.execute(plugin, world, chunkX, chunkZ, run)
    }

    override fun <T : Any> run(function: Function<TaskImplementation<T>, T>): TaskImplementation<T> {
        val taskImplementation = FoliaTask<T>()
        val foliaConsumer = buildFoliaConsumer(taskImplementation, function)
        val scheduledTask = regionScheduler.run(plugin, world, chunkX, chunkZ, foliaConsumer)
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun <T : Any> runDelayed(
        function: Function<TaskImplementation<T>, T>,
        delay: Long,
    ): TaskImplementation<T> {
        val taskImplementation = FoliaTask<T>()
        val foliaConsumer = buildFoliaConsumer(taskImplementation, function)
        val scheduledTask = regionScheduler.runDelayed(plugin, world, chunkX, chunkZ, foliaConsumer, delay)
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }

    override fun <T : Any> runAtFixedRate(
        function: Function<TaskImplementation<T>, T>,
        delay: Long,
        period: Long,
    ): TaskImplementation<T> {
        val taskImplementation = FoliaTask<T>()
        val foliaConsumer = buildFoliaConsumer(taskImplementation, function)
        val scheduledTask = regionScheduler.runAtFixedRate(plugin, world, chunkX, chunkZ, foliaConsumer, delay, period)
        taskImplementation.scheduledTask = scheduledTask
        return taskImplementation
    }
}
