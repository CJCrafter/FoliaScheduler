package com.cjcrafter.scheduler

import com.cjcrafter.scheduler.bukkit.BukkitServer
import com.cjcrafter.scheduler.folia.FoliaServer
import org.bukkit.Server
import org.bukkit.plugin.Plugin

/**
 * The main class of FoliaScheduler, provides methods for getting the server-specific schedulers.
 */
class SchedulerCompatibility(val plugin: Plugin) {

    /**
     * The server-specific scheduler implementation
     */
    val scheduler: ServerImplementation

    init {
        assertRelocated()

        // Look for the method org.bukkit.Server#isOwnedByCurrentRegion(Location)
        // If such a method exists, use the Folia scheduler. Otherwise, use the Bukkit scheduler.
        var scheduler: ServerImplementation
        try {
            Server::class.java.getMethod("isOwnedByCurrentRegion")
            scheduler = FoliaServer(plugin)
        } catch (e: NoSuchMethodException) {
            scheduler = BukkitServer(plugin)
        }

        this.scheduler = scheduler
    }

    private fun assertRelocated() {
        val originalPackage = "com.cjcrafter.scheduler"
        val relocatedPackage = this::class.java.`package`.name
        if (originalPackage == relocatedPackage) {
            val authors = plugin.description.authors.joinToString(", ")
            plugin.logger.warning("The FoliaScheduler lib has not been relocated!")
            plugin.logger.warning("This may cause conflicts with other plugins that use the same library.")
            plugin.logger.warning("Please relocate the library to your unique package")
            plugin.logger.warning("Please warn authors $authors about this issue.")
        }
    }
}