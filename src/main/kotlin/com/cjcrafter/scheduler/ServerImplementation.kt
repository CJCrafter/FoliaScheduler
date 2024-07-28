package com.cjcrafter.scheduler

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin

interface ServerImplementation {

    /**
     * The plugin used to schedule tasks.
     */
    val owningPlugin: Plugin

    /**
     * Returns true if the current thread will be used to tick the region the location is in.
     *
     * @param location The location to check.
     * @return True if the current thread will be used to tick the region the location is in.
     */
    fun isOwnedByCurrentRegion(location: Location): Boolean

    /**
     * Returns true if the current thread will be used to tick the region the location, and the surrounding area, is in.
     *
     * @param location The location to check.
     * @param squareRadiusChunks The radius in chunks to check, as a Chebyshev distance.
     * @return True if the current thread will be used to tick the region the location is in.
     */
    fun isOwnedByCurrentRegion(location: Location, squareRadiusChunks: Int): Boolean

    /**
     * Returns true if the current thread will be used to tick the region the block is in.
     *
     * @param block The block to check.
     * @return True if the current thread will be used to tick the region the block is in.
     */
    fun isOwnedByCurrentRegion(block: Block): Boolean

    /**
     * Returns true if the current thread will be used to tick the region the chunk is in.
     *
     * @param world The world the chunk is in.
     * @param chunkX The x-coordinate of the chunk.
     * @param chunkZ The z-coordinate of the chunk.
     * @return True if the current thread will be used to tick the region the chunk is in.
     */
    fun isOwnedByCurrentRegion(world: World, chunkX: Int, chunkZ: Int): Boolean

    /**
     * Returns true if the current thread will be used to tick the region the chunk is in, and the surrounding area.
     *
     * @param world The world the chunk is in.
     * @param chunkX The x-coordinate of the chunk.
     * @param chunkZ The z-coordinate of the chunk.
     * @param squareRadiusChunks The radius in chunks to check, as a Chebyshev distance.
     * @return True if the current thread will be used to tick the region the chunk is in.
     */
    fun isOwnedByCurrentRegion(world: World, chunkX: Int, chunkZ: Int, squareRadiusChunks: Int): Boolean

    /**
     * Returns true if the current thread will be used to tick the region the entity is in.
     *
     * @param entity The entity to check.
     * @return True if the current thread will be used to tick the region the entity is in.
     */
    fun isOwnedByCurrentRegion(entity: Entity): Boolean

    /**
     * Returns the global region scheduler. On Folia, this will run tasks during server ticks, but separately from any
     * specific region. This is useful for tasks that don't need to be run in a specific region. On Paper/Spigot, all
     * tasks are run in the main thread.
     */
    fun global(): SchedulerImplementation

    /**
     * Returns the async scheduler. On all servers, this will run tasks asynchronously, separate from the server
     * thread(s). This is useful for I/O-bound tasks or tasks that don't need to be run in the main thread.
     */
    fun async(): AsyncSchedulerImplementation

    /**
     * Returns an entity scheduler. On Folia, this will run tasks in the region the entity is in, and will follow the
     * entity as it moves between regions. On Paper/Spigot, this will run tasks in the main thread.
     */
    fun entity(entity: Entity): EntitySchedulerImplementation

    /**
     * Returns a region scheduler. On Folia, this will run tasks in the specified region. On Paper/Spigot, this will run
     * tasks in the main thread.
     */
    fun region(world: World, chunkX: Int, chunkZ: Int): SchedulerImplementation

    /**
     * Returns a region scheduler. On Folia, this will run tasks in the specified region. On Paper/Spigot, this will run
     * tasks in the main thread.
     */
    fun region(location: Location): SchedulerImplementation {
        if (location.world == null) {
            throw IllegalArgumentException("Location world cannot be null")
        }
        return region(location.world, location.blockX shr 4, location.blockZ shr 4)
    }

    /**
     * Returns a region scheduler. On Folia, this will run tasks in the specified region. On Paper/Spigot, this will run
     * tasks in the main thread.
     */
    fun region(block: Block): SchedulerImplementation {
        return region(block.world, block.x shr 4, block.z shr 4)
    }

    /**
     * Returns a region scheduler. On Folia, this will run tasks in the specified region. On Paper/Spigot, this will run
     * tasks in the main thread.
     */
    fun region(chunk: Chunk): SchedulerImplementation {
        return region(chunk.world, chunk.x, chunk.z)
    }
}