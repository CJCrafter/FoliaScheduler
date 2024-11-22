pluginManagement {
    plugins {
        kotlin("jvm") version "2.0.21"
    }
}
include(":folia")
include(":spigot")

project(":folia").projectDir = file("platforms/folia")
project(":spigot").projectDir = file("platforms/spigot")
