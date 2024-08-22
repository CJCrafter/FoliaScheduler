package com.cjcrafter.foliascheduler.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UpdateTest {

    @Test
    public void testLessThan() {
        // 1.12 < 1.13
        int compare = MinecraftVersions.WORLD_OF_COLOR.compareTo(MinecraftVersions.UPDATE_AQUATIC);
        assertTrue(compare < 0);

        // 1.13 < 1.20
        int compare2 =
                MinecraftVersions.UPDATE_AQUATIC.compareTo(MinecraftVersions.TRAILS_AND_TAILS);
        assertTrue(compare2 < 0);
    }

    @Test
    public void testGreaterThan() {
        // 1.20 > 1.13
        int compare =
                MinecraftVersions.TRAILS_AND_TAILS.compareTo(MinecraftVersions.UPDATE_AQUATIC);
        assertTrue(compare > 0);

        // 1.13 > 1.12
        int compare2 = MinecraftVersions.UPDATE_AQUATIC.compareTo(MinecraftVersions.WORLD_OF_COLOR);
        assertTrue(compare2 > 0);
    }

    @Test
    public void testEquals() {
        // 1.13 == 1.13
        MinecraftVersions.Update other = MinecraftVersions.parseCurrentVersion("1.13").getUpdate();
        int compare = MinecraftVersions.UPDATE_AQUATIC.compareTo(other);
        assertEquals(0, compare);
        assertEquals(MinecraftVersions.UPDATE_AQUATIC, other);
    }
}
