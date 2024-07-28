plugins {
    kotlin("jvm") version "1.9.23"
    `java-library`
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // 1.20.4 is strategically chosen to be the latest version of Paper that is compiled against java 17
    compileOnly(project(":"))
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(17)
}
