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
        assertThrows(IllegalStateException.class, () -> MinecraftVersions.parseCurrentVersion(versionString));
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
    public void lessThanUpdate() {
        // 1.12 < 1.13
        int compare = MinecraftVersions.WORLD_OF_COLOR.compareTo(MinecraftVersions.UPDATE_AQUATIC);
        assertTrue(compare < 0);

        // 1.13 < 1.20
        int compare2 = MinecraftVersions.UPDATE_AQUATIC.compareTo(MinecraftVersions.TRAILS_AND_TAILS);
        assertTrue(compare2 < 0);
    }

    @Test
    public void greaterThanUpdate() {
        // 1.20 > 1.13
        int compare = MinecraftVersions.TRAILS_AND_TAILS.compareTo(MinecraftVersions.UPDATE_AQUATIC);
        assertTrue(compare > 0);

        // 1.13 > 1.12
        int compare2 = MinecraftVersions.UPDATE_AQUATIC.compareTo(MinecraftVersions.WORLD_OF_COLOR);
        assertTrue(compare2 > 0);
    }

    @Test
    public void lessThanVersion() {
        // 1.12.2 < 1.13.2
        int compare = MinecraftVersions.WORLD_OF_COLOR.get(2).compareTo(MinecraftVersions.UPDATE_AQUATIC.get(2));
        assertTrue(compare < 0);

        // 1.14.1 < 1.14.2
        int compare2 = MinecraftVersions.VILLAGE_AND_PILLAGE.get(1).compareTo(MinecraftVersions.VILLAGE_AND_PILLAGE.get(2));
        assertTrue(compare2 < 0);
    }

    @Test
    public void greaterThanVersion() {
        // 1.13.2 > 1.12.2
        int compare = MinecraftVersions.UPDATE_AQUATIC.get(2).compareTo(MinecraftVersions.WORLD_OF_COLOR.get(2));
        assertTrue(compare > 0);

        // 1.14.2 > 1.14.1
        int compare2 = MinecraftVersions.VILLAGE_AND_PILLAGE.get(2).compareTo(MinecraftVersions.VILLAGE_AND_PILLAGE.get(1));
        assertTrue(compare2 > 0);
    }
}
