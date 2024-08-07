plugins {
    kotlin("jvm") version "1.9.23"
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

group = "com.cjcrafter"
version = "0.3.1"

val githubOwner = "CJCrafter"
val githubRepo = "FoliaScheduler"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/public/")
}

dependencies {
    // 1.12.2 is the oldest version we plan on officially supporting
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:24.1.0")
}

kotlin {
    jvmToolchain(8)
}


java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    jar {
        // Include all compiled classes from subprojects
        from(subprojects.map { it.sourceSets["main"].output })
    }

    javadoc {
        if (JavaVersion.current().isJava9Compatible) {
            (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
        }
    }

    // Update sources JAR to include subproject sources
    named<Jar>("sourcesJar") {
        from(subprojects.map { it.sourceSets["main"].allSource })
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

signing {
    isRequired = true
    useInMemoryPgpKeys(
        findProperty("SIGNING_KEY_ID").toString(),
        findProperty("SIGNING_PRIVATE_KEY").toString(),
        findProperty("SIGNING_PASSWORD").toString()
    )
    sign(publishing.publications)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set(githubRepo)
                description.set("Spigot Library to check current server version")
                url.set("https://github.com/$githubOwner/$githubRepo")

                groupId = group.toString()
                artifactId = githubRepo.lowercase()
                print(artifactId)

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
                    connection.set("scm:git:https://github.com/$githubOwner/$githubRepo.git")
                    developerConnection.set("scm:git:ssh://github.com/$githubOwner/$githubRepo.git")
                    url.set("https://github.com/$githubOwner/$githubRepo")
                }
            }
        }
    }
}
