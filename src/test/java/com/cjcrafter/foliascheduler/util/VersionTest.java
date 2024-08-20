package com.cjcrafter.foliascheduler.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VersionTest {

    @Test
    public void testLessThan() {
        // 1.12.2 < 1.13.2
        int compare = MinecraftVersions.WORLD_OF_COLOR.get(2).compareTo(MinecraftVersions.UPDATE_AQUATIC.get(2));
        assertTrue(compare < 0);

        // 1.14.1 < 1.14.2
        int compare2 = MinecraftVersions.VILLAGE_AND_PILLAGE.get(1).compareTo(MinecraftVersions.VILLAGE_AND_PILLAGE.get(2));
        assertTrue(compare2 < 0);
    }

    @Test
    public void testGreaterThan() {
        // 1.13.2 > 1.12.2
        int compare = MinecraftVersions.UPDATE_AQUATIC.get(2).compareTo(MinecraftVersions.WORLD_OF_COLOR.get(2));
        assertTrue(compare > 0);

        // 1.14.2 > 1.14.1
        int compare2 = MinecraftVersions.VILLAGE_AND_PILLAGE.get(2).compareTo(MinecraftVersions.VILLAGE_AND_PILLAGE.get(1));
        assertTrue(compare2 > 0);
    }

    @Test
    public void testEquals() {
        // 1.13.2 == 1.13.2
        int compare = MinecraftVersions.UPDATE_AQUATIC.get(2).compareTo(MinecraftVersions.UPDATE_AQUATIC.get(2));
        assertEquals(0, compare);
        assertEquals(MinecraftVersions.UPDATE_AQUATIC.get(2), MinecraftVersions.UPDATE_AQUATIC.get(2));
    }
}
