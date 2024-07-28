package com.cjcrafter.scheduler.folia

import com.cjcrafter.scheduler.AsyncSchedulerImplementation
import com.cjcrafter.scheduler.EntitySchedulerImplementation
import com.cjcrafter.scheduler.ServerImplementation
import com.cjcrafter.scheduler.SchedulerImplementation
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin

/**
 * The Folia provider implementation for multithreaded scheduling.
 */
class FoliaServer(override val owningPlugin: Plugin) : ServerImplementation {
    override fun isOwnedByCurrentRegion(location: Location): Boolean {
        return owningPlugin.server.isOwnedByCurrentRegion(location)
    }

    override fun isOwnedByCurrentRegion(location: Location, squareRadiusChunks: Int): Boolean {
        return owningPlugin.server.isOwnedByCurrentRegion(location, squareRadiusChunks)
    }

    override fun isOwnedByCurrentRegion(block: Block): Boolean {
        return owningPlugin.server.isOwnedByCurrentRegion(block)
    }

    override fun isOwnedByCurrentRegion(world: World, chunkX: Int, chunkZ: Int): Boolean {
        return owningPlugin.server.isOwnedByCurrentRegion(world, chunkX, chunkZ)
    }

    override fun isOwnedByCurrentRegion(world: World, chunkX: Int, chunkZ: Int, squareRadiusChunks: Int): Boolean {
        return owningPlugin.server.isOwnedByCurrentRegion(world, chunkX, chunkZ, squareRadiusChunks)
    }

    override fun isOwnedByCurrentRegion(entity: Entity): Boolean {
        return owningPlugin.server.isOwnedByCurrentRegion(entity)
    }

    override fun global(): SchedulerImplementation {
        return FoliaGlobalScheduler(owningPlugin)
    }

    override fun async(): AsyncSchedulerImplementation {
        return FoliaAsyncScheduler(owningPlugin)
    }

    override fun entity(entity: Entity): EntitySchedulerImplementation {
        return FoliaEntityScheduler(owningPlugin, entity)
    }

    override fun region(world: World, chunkX: Int, chunkZ: Int): SchedulerImplementation {
        return FoliaRegionScheduler(owningPlugin, world, chunkX, chunkZ)
    }
}
