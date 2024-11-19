package com.cjcrafter.foliascheduler.util;

/**
 * Utility class containing methods to quickly grab information of the server's runtime.
 */
public final class ServerVersions {

    private static Boolean isPaper = null;
    private static Boolean isFolia = null;
    private static int javaVersion = -1;

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
                Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
                isFolia = true;
            } catch (ClassNotFoundException e) {
                isFolia = false;
            }
        }
        return isFolia;
    }

    /**
     * Returns the JRE version (Java version) that the server is running.
     * <p>
     * This method only returns the "major" component of the Java version. For example, if the server
     * is running Java 11.0.1, this method will return 11. If the server is running Java 1.8.0_191, this
     * method will return 8.
     *
     * @return the major JRE version
     */
    public static int getJavaVersion() {
        if (javaVersion != -1) {
            try {
                String version = System.getProperty("java.version");
                if (version.startsWith("1.")) {
                    version = version.substring(2, 3);
                } else {
                    int dot = version.indexOf(".");
                    if (dot != -1)
                        version = version.substring(0, dot);
                }
                // IF version is something like 18-ea ->
                version = version.split("-")[0];

                javaVersion = Integer.parseInt(version);
            } catch (Throwable throwable) {
                throw new RuntimeException("Failed to determine Java version", throwable);
            }
        }

        return javaVersion;
    }
}
