package com.cjcrafter.foliascheduler;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to check the current Minecraft version.
 * <p>
 * Minecraft updates all follow the `major.minor.patch` format. This utility
 * separates this into two classes: {@link Update} and {@link Version}. An {@link Update} is a
 * "named" update from Minecraft, such as "1.13" ({@link #UPDATE_AQUATIC}). If you
 * need to get more specific, you can use a {@link Version} object, which is a patch
 * for an {@link Update}, such as "1.13.2".
 * <p>
 * You can get a specific {@link Version} object calling {@link Update#get(int)} with the patch
 * number.
 */
public class MinecraftVersions {
    private static final Map<String, Update> allUpdates = new LinkedHashMap<>();
    private static final Map<String, Version> allVersions = new LinkedHashMap<>();

    /**
     * Returns an immutable map of all updates.
     * <p>
     * The key is the result of {@link Update#toString()}, and the value is the
     * {@link Update} object. Only updates down to 1.12 are included.
     *
     * @return An immutable map of all updates.
     */
    public static @NotNull Map<String, Update> updates() {
        return Collections.unmodifiableMap(allUpdates);
    }

    /**
     * Returns an immutable map of all versions.
     * <p>
     * The key is the result of {@link Version#toString()}, and the value is the
     * {@link Version} object. Only versions down to "1.12.0" are included.
     *
     * @return An immutable map of all versions.
     */
    public static @NotNull Map<String, Version> versions() {
        return Collections.unmodifiableMap(allVersions);
    }

    @NotNull
    private static Version parseCurrentVersion() {
        return parseCurrentVersion(Bukkit.getVersion());
    }

    /**
     * Used internally to parse a version from a string.
     *
     * @param versionString The version string.
     * @return The parsed version.
     */
    @ApiStatus.Internal
    public static @NotNull Version parseCurrentVersion(@NotNull String versionString) {
        Pattern pattern = Pattern.compile("\\d+\\.\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(versionString);

        if (matcher.find()) {
            String currentVersion = matcher.group();
            if (!currentVersion.contains(".")) {
                currentVersion += ".0";
            }
            Version version = allVersions.get(currentVersion);
            if (version != null) {
                return version;
            }
        }

        throw new IllegalStateException("Invalid version: " + versionString);
    }

    /**
     * The current version of the server.
     */
    public static final @NotNull Version CURRENT = parseCurrentVersion();

    /**
     * 1.12, the colorful blocks update (concrete)
     */
    public static final @NotNull Update WORLD_OF_COLOR = new Update(1, 12, update -> {
        update.version(0, 1); // 1.12
        update.version(1, 1); // 1.12.1
        update.version(2, 1); // 1.12.2
    });

    /**
     * 1.13, ocean update (the flattening, waterloggable blocks, sprint swimming, brigadier commands)
     */
    public static final @NotNull Update UPDATE_AQUATIC = new Update(1, 13, update -> {
        update.version(0, 1); // 1.13
        update.version(1, 2); // 1.13.1
        update.version(2, 2); // 1.13.2
    });

    /**
     * 1.14, villagers update (sneaking below slabs, new village generation)
     */
    public static final @NotNull Update VILLAGE_AND_PILLAGE = new Update(1, 14, update -> {
        update.version(0, 1); // 1.14
        update.version(1, 1); // 1.14.1
        update.version(2, 1); // 1.14.2
        update.version(3, 1); // 1.14.3
        update.version(4, 1); // 1.14.4
    });

    /**
     * 1.15, bees update (bug fixes, bees)
     */
    public static final @NotNull Update BUZZY_BEES = new Update(1, 15, update -> {
        update.version(0, 1); // 1.15
        update.version(1, 1); // 1.15.1
        update.version(2, 1); // 1.15.2
    });

    /**
     * 1.16, nether update (crimson, fungus, nether generation, biome fogs)
     */
    public static final @NotNull Update NETHER_UPDATE = new Update(1, 16, update -> {
        update.version(0, 1); // 1.16
        update.version(1, 1); // 1.16.1
        update.version(2, 2); // 1.16.2
        update.version(3, 2); // 1.16.3
        update.version(4, 3); // 1.16.4
        update.version(5, 3); // 1.16.5
    });

    /**
     * 1.17, caves and cliffs part 1 (tuff, new mobs, new blocks)
     */
    public static final @NotNull Update CAVES_AND_CLIFFS_1 = new Update(1, 17, update -> {
        update.version(0, 1); // 1.17
        update.version(1, 1); // 1.17.1
    });

    /**
     * 1.18, caves and cliffs part 2 (new generations)
     */
    public static final @NotNull Update CAVES_AND_CLIFFS_2 = new Update(1, 18, update -> {
        update.version(0, 1); // 1.18
        update.version(1, 1); // 1.18.1
        update.version(2, 2); // 1.18.2
    });

    /**
     * 1.19, the deep dark update (sculk, warden, mud, mangrove, etc.)
     */
    public static final @NotNull Update WILD_UPDATE = new Update(1, 19, update -> {
        update.version(0, 1); // 1.19
        update.version(1, 1); // 1.19.1
        update.version(2, 2); // 1.19.2
        update.version(3, 3); // 1.19.3
        update.version(4, 3); // 1.19.4
    });

    /**
     * 1.20, the archaeology update (cherry grove, sniffers, etc.)
     */
    public static final @NotNull Update TRAILS_AND_TAILS = new Update(1, 20, update -> {
        update.version(0, 1); // 1.20
        update.version(1, 1); // 1.20.1
        update.version(2, 2); // 1.20.2
        update.version(3, 3); // 1.20.3
        update.version(4, 3); // 1.20.4
        update.version(5, 4); // 1.20.5
        update.version(6, 4); // 1.20.6
    });

    /**
     * 1.21, the dungeons update (mace, potions, paintings, etc.)
     */
    public static final @NotNull Update TRICKY_TRIALS = new Update(1, 21, update -> {
        update.version(0, 1); // 1.21
    });

    /**
     * Represents a "big" Minecraft update, e.g., 1.13 -> 1.14
     */
    public static class Update implements Comparable<Update> {
        private final int major;
        private final int minor;
        private final List<Version> versions = new ArrayList<>();
        private boolean lock = false;

        /**
         * Creates a new Update instance.
         *
         * @param major The major version. Always 1.
         * @param minor The minor version. Always 12 or higher.
         * @param init A Consumer to initialize the versions for this update.
         */
        public Update(int major, int minor, @NotNull Consumer<Update> init) {
            this.major = major;
            this.minor = minor;
            init.accept(this);
            lock = true;
            allUpdates.put(toString(), this);
            for (Version version : versions) {
                allVersions.put(version.toString(), version);
            }
        }

        /**
         * Returns the major version, always 1.
         *
         * @return The major version.
         */
        public int getMajor() {
            return major;
        }

        /**
         * Returns the minor version.
         *
         * @return The minor version.
         */
        public int getMinor() {
            return minor;
        }

        /**
         * Adds a new version to this update.
         *
         * @param patch The patch number.
         * @param protocol The protocol version.
         * @return The created Version object.
         * @throws IllegalStateException if versions are added after initialization.
         */
        @NotNull Version version(int patch, int protocol) {
            if (lock) {
                throw new IllegalStateException("Cannot add versions after initialization");
            }
            Version version = new Version(this, patch, protocol);
            versions.add(version);
            return version;
        }

        /**
         * Returns true if the server update equals this update.
         *
         * @return true if the server update equals this update.
         */
        public boolean isCurrent() {
            return CURRENT.getUpdate().equals(this);
        }

        /**
         * Returns true if the server update is newer than this update.
         *
         * @return true if the server update is newer than this update.
         */
        public boolean isOlder() {
            return CURRENT.getUpdate().compareTo(this) > 0;
        }

        /**
         * Returns true if the server update is at least this update.
         *
         * @return true if the server update is at least this update.
         */
        public boolean isAtLeast() {
            return CURRENT.getUpdate().compareTo(this) >= 0;
        }

        /**
         * Returns true if the server update is older than this update.
         *
         * @return true if the server update is older than this update.
         */
        public boolean isBelow() {
            return CURRENT.getUpdate().compareTo(this) < 0;
        }

        /**
         * Returns true if the server update is at most this update.
         *
         * @return true if the server update is at most this update.
         */
        public boolean isAtMost() {
            return CURRENT.getUpdate().compareTo(this) <= 0;
        }

        /**
         * Gets a specific Version object for this Update.
         *
         * @param patch The patch number.
         * @return The Version object for the specified patch.
         */
        @NotNull
        public Version get(int patch) {
            try {
                return versions.get(patch);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Invalid patch number: " + patch);
            }
        }

        @Override
        public int compareTo(@NotNull Update other) {
            int majorCompare = Integer.compare(major, other.major);
            return majorCompare != 0 ? majorCompare : Integer.compare(minor, other.minor);
        }

        @Override
        public @NotNull String toString() {
            return major + "." + minor;
        }
    }

    /**
     * Represents a patch for an {@link Update}. e.g., 1.16.4 -> 1.16.5
     */
    public static class Version implements Comparable<Version> {

        private final @NotNull Update update;
        private final int patch;
        private final int protocol;

        /**
         * Creates a new Version instance.
         *
         * @param update The parent update.
         * @param patch The patch number.
         * @param protocol The current protocol version.
         */
        public Version(@NotNull Update update, int patch, int protocol) {
            this.update = update;
            this.patch = patch;
            this.protocol = protocol;
        }

        /**
         * Returns true if the server version equals this version.
         *
         * @return true if the server version equals this version.
         */
        public boolean isCurrent() {
            return CURRENT.equals(this);
        }

        /**
         * Returns true if the server version is newer than this version.
         *
         * @return true if the server version is newer than this version.
         */
        public boolean isOlder() {
            return CURRENT.compareTo(this) > 0;
        }

        /**
         * Returns true if the server version is at least this version.
         *
         * @return true if the server version is at least this version.
         */
        public boolean isAtLeast() {
            return CURRENT.compareTo(this) >= 0;
        }

        /**
         * Returns true if the server version is older than this version.
         *
         * @return true if the server version is older than this version.
         */
        public boolean isBelow() {
            return CURRENT.compareTo(this) < 0;
        }

        /**
         * Returns true if the server version is at most this version.
         *
         * @return true if the server version is at most this version.
         */
        public boolean isAtMost() {
            return CURRENT.compareTo(this) <= 0;
        }

        /**
         * Returns the version in the protocol format, e.g., `v1_16_R3`.
         *
         * @return The version in protocol format.
         */
        public @NotNull String toProtocolString() {
            return "v" + update.major + "_" + update.minor + "_R" + protocol;
        }

        @Override
        public int compareTo(@NotNull Version other) {
            int updateCompare = update.compareTo(other.update);
            return updateCompare != 0 ? updateCompare : Integer.compare(patch, other.patch);
        }

        @Override
        public @NotNull String toString() {
            return update.major + "." + update.minor + "." + patch;
        }

        /**
         * Returns the parent update.
         *
         * @return The parent update.
         */
        public @NotNull Update getUpdate() {
            return update;
        }

        /**
         * Returns the patch number, the last number in the version.
         *
         * @return The patch number.
         */
        public int getPatch() {
            return patch;
        }

        /**
         * Returns the protocol version, like R1, R2, etc.
         *
         * @return The protocol version.
         */
        public int getProtocol() {
            return protocol;
        }
    }
}