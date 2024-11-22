package com.cjcrafter.foliascheduler.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerVersionsTest {

    @ParameterizedTest
    @CsvSource({
        "1.6.0_23, 6",
        "1.7.0, 7",
        "1.7.0_80, 7",
        "1.8.0_211, 8",
        "9.0.1, 9",
        "11.0.4, 11",
        "12, 12",
        "12.0.1, 12"
    })
    public void testJavaVersion(String version, int expected) {
        // Reflect into ServerVersions and set javaVersion to -1, so we can test it
        ReflectionUtil.getField(ServerVersions.class, "javaVersion").set(null, -1);

        // Mock the java version
        String oldJavaVersion = System.getProperty("java.version");
        System.setProperty("java.version", version);

        int actual = ServerVersions.getJavaVersion();
        assertEquals(expected, actual);

        // Reset the java version
        System.setProperty("java.version", oldJavaVersion);
    }
}
