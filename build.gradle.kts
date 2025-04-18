plugins {
    `java-library`
    `maven-publish`
    signing
    id("com.gradleup.shadow") version "8.3.5"
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    kotlin("jvm")
}

group = "com.cjcrafter"
version = "0.6.3"

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

    // Remapping classes in paper 1.20.5+
    implementation("xyz.jpenilla:reflection-remapper:0.1.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    implementation(kotlin("stdlib-jdk8"))
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
        archiveFileName.set("$githubRepo-$version.jar")
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
            // Use the 'shadow' component for publishing
            from(components["shadow"])

            // Include sources and javadoc jars
            artifact(tasks.named<Jar>("sourcesJar").get())
            artifact(tasks.named<Jar>("javadocJar").get())

            pom {
                name.set(githubRepo)
                description.set("Task scheduler for Spigot and Folia plugins")
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
