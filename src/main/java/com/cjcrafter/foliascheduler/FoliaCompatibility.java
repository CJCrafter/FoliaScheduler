package com.cjcrafter.foliascheduler;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * The main class of FoliaScheduler, provides methods for getting the server-specific schedulers.
 */
public class FoliaCompatibility {

    private final @NotNull ServerImplementation serverImplementation;

    public FoliaCompatibility(@NotNull Plugin plugin) {
        assertRelocated(plugin);
        ServerImplementation scheduler;

        // Look for the method org.bukkit.Server#isOwnedByCurrentRegion(Location)
        // If such a method exists, use the Folia scheduler. Otherwise, use the Bukkit scheduler.
        try {
            try {
                Server.class.getMethod("isOwnedByCurrentRegion", Location.class);
                scheduler = Class.forName(this.getClass().getPackage().getName() + ".folia.FoliaServer")
                        .asSubclass(ServerImplementation.class)
                        .getConstructor(Plugin.class)
                        .newInstance(plugin);
            } catch (NoSuchMethodException ex) {
                scheduler = Class.forName(this.getClass().getPackage().getName() + ".bukkit.BukkitServer")
                        .asSubclass(ServerImplementation.class)
                        .getConstructor(Plugin.class)
                        .newInstance(plugin);
            }
        } catch (Throwable ex) {
            throw new RuntimeException("Failed to initialize scheduler", ex);
        }

        this.serverImplementation = scheduler;
    }

    /**
     * Gets the server-specific scheduler implementation.
     *
     * @return The server-specific scheduler implementation.
     */
    public @NotNull ServerImplementation getServerImplementation() {
        return serverImplementation;
    }

    private static void assertRelocated(@NotNull Plugin plugin) {
        String originalPackage = String.join(".", "com", "cjcrafter", "scheduler");
        String relocatedPackage = FoliaCompatibility.class.getPackage().getName();
        if (originalPackage.equals(relocatedPackage)) {
            String authors = String.join(", ", plugin.getDescription().getAuthors());
            String possiblePackage = plugin.getClass().getPackage().getName() + ".libs.scheduler";
            plugin.getLogger().warning("The FoliaScheduler lib has not been relocated!");
            plugin.getLogger().warning("The package is still in '" + originalPackage + "'");
            plugin.getLogger().warning("Please relocate the lib to '" + possiblePackage + "'");
            plugin.getLogger().warning("Please warn authors " + authors + " about this issue.");
        }
    }
}
