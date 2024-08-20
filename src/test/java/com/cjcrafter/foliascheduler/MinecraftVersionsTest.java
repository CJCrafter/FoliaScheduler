package com.cjcrafter.foliascheduler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * These tests are more for verifying that new updates are added correctly.
 */
public class MinecraftVersionsTest {

    @ParameterizedTest
    @CsvSource({
        "Bukkit 1.12.2,1.12.2R1",
        "Paper (1.16.5),1.16.5R3",
        "Purpur 1.20.4,1.20.4R3"
    })
    public void testParseVersion(String versionString, String expected) {
        MinecraftVersions.Version version = MinecraftVersions.parseCurrentVersion(versionString);
        if (!String.format("%sR%d", version, version.getProtocol()).equals(expected)) {
            fail(String.format("Version was incorrect, expected %s but got %s", expected, version));
        }
    }

    @ParameterizedTest
    @CsvSource({
        "Bukkit 1.20,1.20.0R1",
        "Paper (1.20),1.20.0R1"
    })
    public void testParseVersionWithoutPatch(String versionString, String expected) {
        MinecraftVersions.Version version = MinecraftVersions.parseCurrentVersion(versionString);
        if (!String.format("%sR%d", version, version.getProtocol()).equals(expected)) {
            fail(String.format("Version was incorrect, expected %s but got %s", expected, version));
        }
    }

    @ParameterizedTest
    @CsvSource({
        "Bukkit version 12345",
        "Who knows"
    })
    public void testParseVersionWithInvalidVersion(String versionString) {
        assertThrows(IllegalArgumentException.class, () -> MinecraftVersions.parseCurrentVersion(versionString));
    }

    @Test
    public void ensureUpdateOrderIsIncreasing() {
        MinecraftVersions.Update previous = null;
        for (MinecraftVersions.Update update : MinecraftVersions.updates().values()) {
            if (previous != null && previous.compareTo(update) > 0) {
                fail(String.format("Update order was incorrect, %s came before %s", previous, update));
            }
            previous = update;
        }
    }

    @Test
    public void ensureVersionOrderIsIncreasing() {
        MinecraftVersions.Version previous = null;
        for (MinecraftVersions.Version version : MinecraftVersions.versions().values()) {
            if (previous != null && previous.compareTo(version) > 0) {
                fail(String.format("Version order was incorrect, %s came before %s", previous, version));
            }
            previous = version;
        }
    }

    @Test
    public void cannotAddVersions() {
        assertThrows(IllegalStateException.class, () -> {
            MinecraftVersions.Update update = MinecraftVersions.BUZZY_BEES;
            update.version(7, 5);
        });
    }

    @Test
    public void testAllowUnknownVersion() {
        // no way! Minecraft 2.0!
        MinecraftVersions.Version version = MinecraftVersions.parseCurrentVersion("Minecraft 2.0");

        // unknown versions will have a protocol of -1
        assertEquals(-1, version.getProtocol());
        assertEquals("2.0.0", version.toString());
    }

    @Test
    public void testUnknownVersionGreaterThan() {
        // no way! Minecraft 2.0!
        MinecraftVersions.Version version = MinecraftVersions.parseCurrentVersion("Minecraft 2.0");
        MinecraftVersions.Version updateAquatic = MinecraftVersions.UPDATE_AQUATIC.get(2);

        // We expect 2.0.0 to be newer than 1.13.2
        assertTrue(version.compareTo(updateAquatic) > 0);
    }

    @Test
    public void testUnknownVersionLessThan() {
        // no way! Minecraft 0.0!
        MinecraftVersions.Version version = MinecraftVersions.parseCurrentVersion("Minecraft 0.0");
        MinecraftVersions.Version updateAquatic = MinecraftVersions.UPDATE_AQUATIC.get(2);

        // We expect 0.0 to be older than 1.13.2
        assertTrue(version.compareTo(updateAquatic) < 0);
    }
}
