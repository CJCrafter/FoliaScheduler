package com.cjcrafter.foliascheduler;

/**
 * Utility class to determine the fork type of the server.
 */
public final class ServerVersions {

    private static Boolean isPaper = null;
    private static Boolean isFolia = null;

    private ServerVersions() {
        // Prevent instantiation
    }

    /**
     * Returns <code>true</code> if the server is running a version of Paper, or any fork of Paper.
     *
     * @return whether the server is running a version of Paper
     */
    public static boolean isPaper() {
        if (isPaper == null) {
            try {
                Class.forName("com.destroystokyo.paper.PaperConfig");
                isPaper = true;
            } catch (ClassNotFoundException e) {
                isPaper = false;
            }
        }
        return isPaper;
    }

    /**
     * Returns <code>true</code> if the server is running a version of Folia, or any fork of Folia.
     * <p>
     * Note that when {@link #isFolia} returns true, {@link #isPaper} will also return true, since
     * Folia is a fork of Paper.
     *
     * @return Whether the server is running a version of Folia
     */
    public static boolean isFolia() {
        if (isFolia == null) {
            try {
                Class.forName("io.papermc.paper.threadedregion.RegionizedServer");
                isFolia = true;
            } catch (ClassNotFoundException e) {
                isFolia = false;
            }
        }
        return isFolia;
    }
}
