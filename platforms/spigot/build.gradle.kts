plugins {
    kotlin("jvm") version "1.9.23"
    `java-library`
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/public/")
}

dependencies {
    // 1.12.2 is the oldest version we plan on officially supporting
    compileOnly(project(":"))
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:24.1.0")
}

kotlin {
    jvmToolchain(8)
}
