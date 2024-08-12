plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

group = "com.cjcrafter"
version = "0.4.0"

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

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
    withSourcesJar()
    withJavadocJar()
}

tasks {
    jar {
        manifest {
            attributes(
                "Multi-Release" to "true"
            )
        }
        from(sourceSets.main.get().output)
        dependsOn(":folia:jar", ":spigot:jar")
        from(zipTree(project(":spigot").tasks.jar.get().archiveFile)) {
            exclude("META-INF/**")
        }
        from(zipTree(project(":folia").tasks.jar.get().archiveFile)) {
            into("META-INF/versions/17")
        }
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
        exclude("META-INF/versions/**")
    }

    // Update sources JAR to include subproject sources
    named<Jar>("sourcesJar") {
        from(sourceSets.main.get().allSource)
        from(project(":spigot").sourceSets.main.get().allSource)
        from(project(":folia").sourceSets.main.get().allSource) {
            into("META-INF/versions/17")
        }
    }

    // Update Javadoc JAR to include subproject Javadoc
    named<Jar>("javadocJar") {
        from(javadoc)
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(System.getenv("OSSRH_USERNAME") ?: findProperty("OSSRH_USERNAME").toString())
            password.set(System.getenv("OSSRH_PASSWORD") ?: findProperty("OSSRH_PASSWORD").toString())
        }
    }
}

signing {
    isRequired = true
    useInMemoryPgpKeys(
        System.getenv("SIGNING_KEY_ID") ?: findProperty("SIGNING_KEY_ID").toString(),
        System.getenv("SIGNING_PRIVATE_KEY") ?: findProperty("SIGNING_PRIVATE_KEY").toString(),
        System.getenv("SIGNING_PASSWORD") ?: findProperty("SIGNING_PASSWORD").toString(),
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
