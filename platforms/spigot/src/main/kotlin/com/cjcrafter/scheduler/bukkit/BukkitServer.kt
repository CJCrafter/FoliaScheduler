package com.cjcrafter.scheduler.bukkit

import com.cjcrafter.scheduler.AsyncSchedulerImplementation
import com.cjcrafter.scheduler.EntitySchedulerImplementation
import com.cjcrafter.scheduler.ServerImplementation
import com.cjcrafter.scheduler.SchedulerImplementation
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin

class BukkitServer(override val owningPlugin: Plugin) : ServerImplementation {

    private val sync: BukkitSyncScheduler = BukkitSyncScheduler(owningPlugin)
    private val async: BukkitAsyncScheduler = BukkitAsyncScheduler(owningPlugin)

    override fun isOwnedByCurrentRegion(location: Location): Boolean {
        return owningPlugin.server.isPrimaryThread
    }

    override fun isOwnedByCurrentRegion(location: Location, squareRadiusChunks: Int): Boolean {
        return owningPlugin.server.isPrimaryThread
    }

    override fun isOwnedByCurrentRegion(block: Block): Boolean {
        return owningPlugin.server.isPrimaryThread
    }

    override fun isOwnedByCurrentRegion(world: World, chunkX: Int, chunkZ: Int): Boolean {
        return owningPlugin.server.isPrimaryThread
    }

    override fun isOwnedByCurrentRegion(world: World, chunkX: Int, chunkZ: Int, squareRadiusChunks: Int): Boolean {
        return owningPlugin.server.isPrimaryThread
    }

    override fun isOwnedByCurrentRegion(entity: Entity): Boolean {
        return owningPlugin.server.isPrimaryThread
    }

    override fun global(): SchedulerImplementation {
        return sync
    }

    override fun async(): AsyncSchedulerImplementation {
        return async
    }

    override fun entity(entity: Entity): EntitySchedulerImplementation {
        return BukkitEntityScheduler(owningPlugin, entity)
    }

    override fun region(world: World, chunkX: Int, chunkZ: Int): SchedulerImplementation {
        return sync
    }
}