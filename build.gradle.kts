import org.jreleaser.model.Active

plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.5"
    id("org.jreleaser") version "1.18.0"
    signing
}

group = "com.cjcrafter"
version = "0.7.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/public/")
}

dependencies {
    // 1.12.2 is the oldest version we plan on officially supporting
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:24.1.0")

    // Remapping classes in paper 1.20.5+
    implementation("xyz.jpenilla:reflection-remapper:0.1.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
    withSourcesJar()
    withJavadocJar()
}

tasks {
    shadowJar {
        archiveFileName.set("FoliaScheduler-$version.jar")
        archiveClassifier.set("")

        dependsOn(":folia:jar", ":spigot:jar")
        from(zipTree(project(":spigot").tasks.jar.get().archiveFile)) {
            exclude("META-INF/**")
        }
        from(zipTree(project(":folia").tasks.jar.get().archiveFile)) {
            exclude("META-INF/**")
        }

        relocate("xyz.jpenilla.reflectionremapper", "com.cjcrafter.foliascheduler.reflectionremapper")
        relocate("net.fabricmc.mappingio", "com.cjcrafter.foliascheduler.mappingio")
    }

    javadoc {
        options {
            this as StandardJavadocDocletOptions
            // suppress warnings for missing Javadoc comments
            addStringOption("Xdoclint:none", "-quiet")
            addStringOption("encoding", "UTF-8")
        }
        source(sourceSets.main.get().allJava)
        classpath = sourceSets.main.get().compileClasspath
    }

    named<Jar>("sourcesJar") {
        dependsOn(":folia:jar", ":spigot:jar")

        from(sourceSets.main.get().allSource)
        //from(project(":spigot").sourceSets.main.get().allSource)
        //from(project(":folia").sourceSets.main.get().allSource)
    }

    test {
        useJUnitPlatform()
    }
}


publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            // Use the 'shadow' component for publishing
            from(components["shadow"])
            artifact(tasks.named<Jar>("sourcesJar").get())
            artifact(tasks.named<Jar>("javadocJar").get())

            groupId = "com.cjcrafter"
            artifactId = "foliascheduler"
            version = project.version.toString()

            pom {
                name.set("FoliaScheduler")
                description.set("Task scheduler for Spigot and Folia plugins")
                url.set("https://github.com/CJCrafter/FoliaScheduler")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("CJCrafter")
                        name.set("Collin Barber")
                        email.set("collinjbarber@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/CJCrafter/FoliaScheduler.git")
                    developerConnection.set("scm:git:ssh://github.com/CJCrafter/FoliaScheduler.git")
                    url.set("https://github.com/CJCrafter/FoliaScheduler")
                }
            }
        }
    }

    // Deploy this repository locally for staging, then let the root project actually
    // upload the maven repo using jReleaser
    repositories {
        maven {
            name = "stagingDeploy"
            url = layout.buildDirectory.dir("staging-deploy").map { it.asFile.toURI() }.get()
        }
    }
}

jreleaser {
    project {
        name.set("FoliaScheduler")
        group = "com.cjcrafter"
        version = project.version.get()
        description = "Task scheduler for Spigot and Folia plugins"
        authors.add("Collin Barber <collinjbarber@gmail.com>")
        license = "MIT" // SPDX identifier

        java {
            groupId = "com.cjcrafter"
            artifactId = "foliascheduler"
            version = project.version.get()
        }
    }

    signing {
        active.set(Active.ALWAYS)
        armored.set(true)
    }

    deploy {
        maven {
            mavenCentral {
                create("releaseDeploy") {
                    active.set(Active.RELEASE)
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    // run `./gradlew publish` before deployment
                    stagingRepository("build/staging-deploy")
                    // Credentials (JRELEASER_MAVENCENTRAL_USERNAME, JRELEASER_MAVENCENTRAL_PASSWORD or JRELEASER_MAVENCENTRAL_TOKEN)
                    // will be picked up from ~/.jreleaser/config.toml
                }
            }

            nexus2 {
                create("sonatypeSnapshots") {
                    active.set(Active.SNAPSHOT)
                    url.set("https://central.sonatype.com/repository/maven-snapshots/")
                    snapshotUrl.set("https://central.sonatype.com/repository/maven-snapshots/")
                    applyMavenCentralRules = true
                    snapshotSupported = true
                    closeRepository = true
                    releaseRepository = true
                    stagingRepository("build/staging-deploy")
                }
            }
        }
    }

    project {
        snapshot {
            fullChangelog.set(true)
        }
    }

    release {
        github {
            repoOwner.set("CJCrafter")
            name.set("FoliaScheduler")
            host.set("github.com")

            val isSnapshot = project.version.get().endsWith("-SNAPSHOT")
            releaseName.set(if (isSnapshot) "SNAPSHOT" else "v${project.version.get()}")
            tagName.set("v{{projectVersion}}")
            draft.set(false)
            skipTag.set(isSnapshot)
            overwrite.set(false)
            update { enabled.set(isSnapshot) }

            prerelease {
                enabled.set(isSnapshot)
                pattern.set(".*-SNAPSHOT")
            }

            commitAuthor {
                name.set("Collin Barber")
                email.set("collinjbarber@gmail.com")
            }

            changelog {
                formatted.set(Active.ALWAYS)
                preset.set("conventional-commits")
                format.set("- {{commitShortHash}} {{commitTitle}}")
                contributors {
                    enabled.set(true)
                    format.set("{{contributorUsernameAsLink}}")
                }
                hide {
                    contributors.set(listOf("[bot]"))
                }
            }
        }
    }
}