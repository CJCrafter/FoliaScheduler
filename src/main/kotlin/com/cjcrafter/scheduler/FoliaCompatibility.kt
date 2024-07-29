package com.cjcrafter.scheduler

import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.plugin.Plugin

/**
 * The main class of FoliaScheduler, provides methods for getting the server-specific schedulers.
 */
class FoliaCompatibility(val plugin: Plugin) {

    /**
     * The server-specific scheduler implementation
     */
    val serverImplementation: ServerImplementation

    init {
        assertRelocated()

        // Look for the method org.bukkit.Server#isOwnedByCurrentRegion(Location)
        // If such a method exists, use the Folia scheduler. Otherwise, use the Bukkit scheduler.
        var scheduler: ServerImplementation
        try {
            try {
                Server::class.java.getMethod("isOwnedByCurrentRegion", Location::class.java)
                scheduler = Class.forName(javaClass.`package`.name + ".folia.FoliaServer")
                    .getDeclaredConstructor(Plugin::class.java)
                    .newInstance(plugin) as ServerImplementation
            } catch (e: NoSuchMethodException) {
                scheduler = Class.forName(javaClass.`package`.name + ".bukkit.BukkitServer")
                    .getDeclaredConstructor(Plugin::class.java)
                    .newInstance(plugin) as ServerImplementation
            }
        } catch (ex: Throwable) {
            throw InternalError("Failed to initialize scheduler", ex)
        }

        this.serverImplementation = scheduler
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