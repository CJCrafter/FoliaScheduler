package com.cjcrafter.foliascheduler.util;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to check the current Minecraft version.
 * <p>
 * Historically Minecraft used the {@code 1.major.minor} naming scheme (e.g.
 * {@code 1.21.11}). Starting with the {@code Tiny Takeover} drop on
 * March 24, 2026, Mojang switched to a {@code year.drop.hotfix} scheme
 * (e.g. {@code 26.1.2}). This class parses both formats: the first numeric
 * component is treated as the "major" version, the second as the "minor"
 * version, and the third (optional) as the "patch" / hotfix version.
 * <p>
 * This utility separates versions into two classes: {@link Update} and
 * {@link Version}. An {@link Update} is a "named" Minecraft update such
 * as {@code 1.13} ({@link #UPDATE_AQUATIC}) or {@code 26.1}
 * ({@link #TINY_TAKEOVER}). For more granularity use a {@link Version}
 * object, which is a specific patch of an {@link Update} such as
 * {@code 1.13.2} or {@code 26.1.2}.
 * <p>
 * You can get a specific {@link Version} object by calling
 * {@link Update#get(int)} with the patch number.
 * <p>
 * Note that this utility will need to be updated to access newer versions
 * of Minecraft. If this utility is used on a newer version of Minecraft,
 * it will be parsed into an "unknown" version, which will still function
 * as expected ({@link Version#isAtLeast()} will still work, for example).
 * However, {@link #updates()} and {@link #versions()} will not contain the
 * newer versions.
 */
public final class MinecraftVersions {

    private static final @NotNull Map<String, Update> allUpdates = new LinkedHashMap<>();
    private static final @NotNull Map<String, Version> allVersions = new LinkedHashMap<>();
    private static @Nullable Version CURRENT = null;

    private MinecraftVersions() {
        // Prevent instantiation
    }

    /**
     * Used internally to populate <code>allUpdates</code> and <code>allVersions</code>.
     *
     * @param update The update to register.
     * @return The registered update.
     */
    private static @NotNull Update registerUpdate(@NotNull Update update) {
        allUpdates.put(update.toString(), update);
        for (Version version : update.versions) {
            allVersions.put(version.toString(), version);
        }
        return update;
    }

    /**
     * Returns an immutable map of all updates.
     * <p>
     * The key is the result of {@link Update#toString()}, and the value is the
     * {@link Update} object.
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
     * {@link Version} object.
     *
     * @return An immutable map of all versions.
     */
    public static @NotNull Map<String, Version> versions() {
        return Collections.unmodifiableMap(allVersions);
    }

    /**
     * Returns the current Minecraft version.
     *
     * @return The current Minecraft version.
     */
    public static @NotNull Version getCurrent() {
        if (CURRENT == null) {
            CURRENT = parseCurrentVersion(Bukkit.getVersion());
        }
        return CURRENT;
    }

    /**
     * Used internally to parse a version from a string.
     * <p>
     * This handles both the legacy {@code 1.X.Y} numbering and the newer
     * {@code YY.D.H} (year.drop.hotfix) numbering introduced with
     * Minecraft 26.1 in March 2026.
     *
     * @param versionString The version string.
     * @return The parsed version.
     */
    static @NotNull Version parseCurrentVersion(@NotNull String versionString) {
        Pattern pattern = Pattern.compile("(\\d+)\\.(\\d+)(?:\\.(\\d+))?(?!\\.\\d)");
        Matcher matcher = pattern.matcher(versionString);

        if (!matcher.find())
            throw new IllegalArgumentException("Could not find any version in: " + versionString);

        int major = Integer.parseInt(matcher.group(1));
        int minor = Integer.parseInt(matcher.group(2));
        int patch = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : 0;

        // Check if the version is for a Minecraft version that we know about
        Version version = allVersions.get(major + "." + minor + "." + patch);
        if (version != null)
            return version;

        // If the version is not known, create a new "unknown" version
        return new Version(major, minor, patch, -1);
    }

    /**
     * 1.12, the colorful blocks update (concrete)
     */
    public static final @NotNull Update WORLD_OF_COLOR = registerUpdate(new Update(1, 12, update -> {
        update.version(0, 1); // 1.12
        update.version(1, 1); // 1.12.1
        update.version(2, 1); // 1.12.2
    }));

    /**
     * 1.13, ocean update (the flattening, waterloggable blocks, sprint swimming, brigadier commands)
     */
    public static final @NotNull Update UPDATE_AQUATIC = registerUpdate(new Update(1, 13, update -> {
        update.version(0, 1); // 1.13
        update.version(1, 2); // 1.13.1
        update.version(2, 2); // 1.13.2
    }));

    /**
     * 1.14, villagers update (sneaking below slabs, new village generation)
     */
    public static final @NotNull Update VILLAGE_AND_PILLAGE = registerUpdate(new Update(1, 14, update -> {
        update.version(0, 1); // 1.14
        update.version(1, 1); // 1.14.1
        update.version(2, 1); // 1.14.2
        update.version(3, 1); // 1.14.3
        update.version(4, 1); // 1.14.4
    }));

    /**
     * 1.15, bees update (bug fixes, bees)
     */
    public static final @NotNull Update BUZZY_BEES = registerUpdate(new Update(1, 15, update -> {
        update.version(0, 1); // 1.15
        update.version(1, 1); // 1.15.1
        update.version(2, 1); // 1.15.2
    }));

    /**
     * 1.16, nether update (crimson, fungus, nether generation, biome fogs)
     */
    public static final @NotNull Update NETHER_UPDATE = registerUpdate(new Update(1, 16, update -> {
        update.version(0, 1); // 1.16
        update.version(1, 1); // 1.16.1
        update.version(2, 2); // 1.16.2
        update.version(3, 2); // 1.16.3
        update.version(4, 3); // 1.16.4
        update.version(5, 3); // 1.16.5
    }));

    /**
     * 1.17, caves and cliffs part 1 (tuff, new mobs, new blocks)
     */
    public static final @NotNull Update CAVES_AND_CLIFFS_1 = registerUpdate(new Update(1, 17, update -> {
        update.version(0, 1); // 1.17
        update.version(1, 1); // 1.17.1
    }));

    /**
     * 1.18, caves and cliffs part 2 (new generations)
     */
    public static final @NotNull Update CAVES_AND_CLIFFS_2 = registerUpdate(new Update(1, 18, update -> {
        update.version(0, 1); // 1.18
        update.version(1, 1); // 1.18.1
        update.version(2, 2); // 1.18.2
    }));

    /**
     * 1.19, the deep dark update (sculk, warden, mud, mangrove, etc.)
     */
    public static final @NotNull Update WILD_UPDATE = registerUpdate(new Update(1, 19, update -> {
        update.version(0, 1); // 1.19
        update.version(1, 1); // 1.19.1
        update.version(2, 2); // 1.19.2
        update.version(3, 3); // 1.19.3
        update.version(4, 3); // 1.19.4
    }));

    /**
     * 1.20, the archaeology update (cherry grove, sniffers, etc.)
     */
    public static final @NotNull Update TRAILS_AND_TAILS = registerUpdate(new Update(1, 20, update -> {
        update.version(0, 1); // 1.20
        update.version(1, 1); // 1.20.1
        update.version(2, 2); // 1.20.2
        update.version(3, 3); // 1.20.3 (Bats and Pots)
        update.version(4, 3); // 1.20.4
        update.version(5, 4); // 1.20.5 (Armored Paws)
        update.version(6, 4); // 1.20.6
    }));

    /**
     * 1.21, the dungeons update (mace, new potions, new paintings, etc.).
     */
    public static final @NotNull Update TRICKY_TRIALS = registerUpdate(new Update(1, 21, update -> {
        update.version(0, 1); // 1.21
        update.version(1, 1); // 1.21.1
        update.version(2, 2); // 1.21.2 (Bundles of Bravery)
        update.version(3, 2); // 1.21.3
        update.version(4, 3); // 1.21.4 (The Garden Awakens)
        update.version(5, 4); // 1.21.5 (Spring to Life)
        update.version(6, 5); // 1.21.6 (Chase the Skies)
        update.version(7, 5); // 1.21.7
        update.version(8, 5); // 1.21.8
        update.version(9, 6); // 1.21.9 (The Copper Age)
        update.version(10, 6); // 1.21.10
        update.version(11, 7); // 1.21.11 (Mounts of Mayhem)
    }));

    /**
     * 26.1, the first drop of 2026 (released March 24, 2026).
     *
     * <p>Note that starting in version <code>26</code>, the protocol holds no
     * meaning and is selected arbitrarily as the drop number for a given year.
     */
    public static final @NotNull Update TINY_TAKEOVER = registerUpdate(new Update(26, 1, update -> {
        update.version(0, 1); // 26.1
        update.version(1, 1); // 26.1.1
        update.version(2, 1); // 26.1.2
    }));

    /**
     * Represents a "big" Minecraft update, e.g., 1.13 -> 1.14 or 26.1 -> 26.2.
     */
    public static class Update implements Comparable<Update> {
        private final int major;
        private final int minor;
        private final List<Version> versions = new ArrayList<>();
        private boolean lock = false;

        public Update(int major, int minor, @NotNull List<Version> versions) {
            this.major = major;
            this.minor = minor;
            this.versions.addAll(versions);
            lock = true;
        }

        /**
         * Creates a new Update instance.
         *
         * @param major The major version. {@code 1} for the legacy {@code 1.X.Y}
         *              scheme; the two-digit year (e.g. {@code 26}) for releases
         *              from {@code 26.1} ({@link #TINY_TAKEOVER}) onward.
         * @param minor The minor version (the "drop" number under the new scheme).
         * @param init A Consumer to initialize the versions for this update.
         */
        public Update(int major, int minor, @NotNull Consumer<Update> init) {
            this.major = major;
            this.minor = minor;
            init.accept(this);
            lock = true;
        }

        /**
         * Returns the major version.
         * <p>
         * For legacy versions ({@code 1.12} through {@code 1.21}) this is
         * always {@code 1}. For releases from {@code 26.1}
         * ({@link #TINY_TAKEOVER}) onward, this is the two-digit year
         * (e.g. {@code 26}).
         *
         * @return The major version.
         */
        public int getMajor() {
            return major;
        }

        /**
         * Returns the minor version (the "drop" number under the new
         * year-based scheme).
         *
         * @return The minor version.
         */
        public int getMinor() {
            return minor;
        }

        /**
         * Used internally to add a new version to this update.
         *
         * @param patch The patch number.
         * @param protocol The protocol version.
         * @throws IllegalStateException if versions are added after initialization.
         */
        void version(int patch, int protocol) {
            if (lock) {
                throw new IllegalStateException("Cannot add versions after initialization");
            }
            Version version = new Version(this, patch, protocol);
            versions.add(version);
        }

        /**
         * Returns true if the server update equals this update.
         *
         * @return true if the server update equals this update.
         */
        public boolean isCurrent() {
            return getCurrent().getUpdate().equals(this);
        }

        /**
         * Returns true if the server update is newer than this update.
         *
         * @return true if the server update is newer than this update.
         */
        public boolean isAbove() {
            return getCurrent().getUpdate().compareTo(this) > 0;
        }

        /**
         * Returns true if the server update is at least this update.
         *
         * @return true if the server update is at least this update.
         */
        public boolean isAtLeast() {
            return getCurrent().getUpdate().compareTo(this) >= 0;
        }

        /**
         * Returns true if the server update is older than this update.
         *
         * @return true if the server update is older than this update.
         */
        public boolean isBelow() {
            return getCurrent().getUpdate().compareTo(this) < 0;
        }

        /**
         * Returns true if the server update is at most this update.
         *
         * @return true if the server update is at most this update.
         */
        public boolean isAtMost() {
            return getCurrent().getUpdate().compareTo(this) <= 0;
        }

        /**
         * Gets a specific Version object for this Update.
         *
         * @param patch The patch number.
         * @return The Version object for the specified patch.
         * @throws IllegalArgumentException if no such version exists
         */
        public @NotNull Version get(int patch) {
            try {
                return versions.get(patch);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Unknown version: " + major + "." + minor + "." + patch);
            }
        }

        @Override
        public int compareTo(@NotNull Update other) {
            int majorCompare = Integer.compare(major, other.major);
            return majorCompare != 0 ? majorCompare : Integer.compare(minor, other.minor);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Update update = (Update) o;
            return major == update.major && minor == update.minor && Objects.equals(versions, update.versions);
        }

        @Override
        public int hashCode() {
            return Objects.hash(major, minor, versions);
        }

        @Override
        public @NotNull String toString() {
            return major + "." + minor;
        }
    }

    /**
     * Holds a specific version of an update, formatted in
     * {@code major.minor.patch}.
     * <p>
     * Examples: {@code 1.13.2} (legacy scheme) and {@code 26.1.2}
     * (year-based scheme introduced in March 2026).
     */
    public static class Version implements Comparable<Version> {

        private final @NotNull Update update;
        private final int major;
        private final int minor;
        private final int patch;
        private final int protocol;

        /**
         * Creates a new Version instance.
         *
         * @param major The major number.
         * @param minor The minor number.
         * @param patch The patch number.
         * @param protocol The current protocol version.
         */
        public Version(int major, int minor, int patch, int protocol) {
            this.major = major;
            this.minor = minor;
            this.patch = patch;
            this.protocol = protocol;

            // Create a "fake" update to hold this version, so comparisons can
            // be made between updates. This is not added to the allUpdates map.
            this.update = new Update(major, minor, Collections.singletonList(this));
        }

        /**
         * Creates a new Version instance.
         *
         * @param update The parent update.
         * @param patch The patch number.
         * @param protocol The current protocol version.
         */
        public Version(@NotNull Update update, int patch, int protocol) {
            this.update = update;
            this.major = update.major;
            this.minor = update.minor;
            this.patch = patch;
            this.protocol = protocol;
        }

        /**
         * Returns the parent update, or null if this version was not created from an update.
         *
         * @return The parent update.
         */
        public @NotNull Update getUpdate() {
            return update;
        }

        /**
         * Returns the major version.
         * <p>
         * For legacy versions this is {@code 1}; for releases from
         * {@code 26.1} onward this is the two-digit year (e.g. {@code 26}).
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
         * Returns the patch number, the last number in the version.
         *
         * @return The patch number.
         */
        public int getPatch() {
            return patch;
        }

        /**
         * Returns the protocol revision, like R1, R2, etc.
         * <p>
         * For legacy versions ({@code 1.12} through {@code 1.21}) this
         * is the CraftBukkit NMS package revision (e.g. {@code R7} for
         * {@code 1.21.11} -> {@code v1_21_R7}).
         * <p>
         * For releases from {@code 26.1} ({@link #TINY_TAKEOVER}) onward,
         * Mojang ships the server jar unobfuscated and the historical
         * NMS R-revision no longer corresponds to a real package on
         * disk. To keep the {@link #toProtocolString()} format
         * meaningful, this utility uses the convention that R equals
         * the drop number (i.e. the minor version) for those versions,
         * so {@code 26.1.x} -> {@code R1}.
         * <p>
         * If the version is "unknown" (not included in this utility),
         * this returns {@code -1}. This usually means a new version of
         * Minecraft was released after this utility was last updated.
         *
         * @return The protocol revision.
         */
        public int getProtocol() {
            return protocol;
        }

        /**
         * Returns true if the server version equals this version.
         *
         * @return true if the server version equals this version.
         */
        public boolean isCurrent() {
            return getCurrent().equals(this);
        }

        /**
         * Returns true if the server version is newer than this version.
         *
         * @return true if the server version is newer than this version.
         */
        public boolean isAbove() {
            return getCurrent().compareTo(this) > 0;
        }

        /**
         * Returns true if the server version is at least this version.
         *
         * @return true if the server version is at least this version.
         */
        public boolean isAtLeast() {
            return getCurrent().compareTo(this) >= 0;
        }

        /**
         * Returns true if the server version is older than this version.
         *
         * @return true if the server version is older than this version.
         */
        public boolean isBelow() {
            return getCurrent().compareTo(this) < 0;
        }

        /**
         * Returns true if the server version is at most this version.
         *
         * @return true if the server version is at most this version.
         */
        public boolean isAtMost() {
            return getCurrent().compareTo(this) <= 0;
        }

        /**
         * Returns the version in the CraftBukkit NMS package format,
         * e.g. {@code v1_16_R3} or {@code v26_1_R1}.
         * <p>
         * For legacy versions ({@code 1.x.y}), this matches the real
         * CraftBukkit package on disk.
         * <p>
         * For releases from {@code 26.1} onward, this is a synthetic
         * identifier using the drop number as the R-revision (see
         * {@link #getProtocol()}). Modern Paper builds do not actually
         * relocate the CraftBukkit package, so this string should not
         * be assumed to correspond to a package on disk for 1.20.5+
         * Paper or for any 26.1+ build.
         *
         * @return The version in protocol format.
         */
        public @NotNull String toProtocolString() {
            return "v" + major + "_" + minor + "_R" + protocol;
        }

        @Override
        public int compareTo(@NotNull Version other) {
            int majorCompare = Integer.compare(major, other.major);
            if (majorCompare != 0) return majorCompare;
            int minorCompare = Integer.compare(minor, other.minor);
            if (minorCompare != 0) return minorCompare;
            return Integer.compare(patch, other.patch);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Version version = (Version) o;
            return major == version.major && minor == version.minor && patch == version.patch;
        }

        @Override
        public int hashCode() {
            return Objects.hash(major, minor, patch);
        }

        @Override
        public @NotNull String toString() {
            return major + "." + minor + "." + patch;
        }
    }
}