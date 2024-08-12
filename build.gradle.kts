import com.github.jengelman.gradle.plugins.shadow.transformers.Transformer
import com.github.jengelman.gradle.plugins.shadow.transformers.TransformerContext
import org.apache.tools.zip.ZipOutputStream

plugins {
    kotlin("jvm") version "1.9.23"
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.cjcrafter"
version = "0.3.4"

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

class KotlinMinimizer : Transformer {
    override fun getName(): String {
        return "KotlinMinimizer"
    }

    override fun canTransformResource(element: FileTreeElement?): Boolean {
        // Only transform stuff from the kotlin package
        return element?.name?.startsWith("kotlin") ?: false
    }

    override fun transform(context: TransformerContext?) {
        // Exclude everything from the kotlin package
    }

    override fun hasTransformedResource(): Boolean {
        TODO("Not yet implemented")
    }

    override fun modifyOutputStream(os: ZipOutputStream?, preserveFileTimestamps: Boolean) {
    }

}

tasks {
    shadowJar {
        archiveClassifier.set("")

        subprojects.forEach { subproject ->
            from(subproject.sourceSets.main.get().output)
            configurations += subproject.configurations.runtimeClasspath.get()
        }

        minimize()


        // exclude everything else from the kotlin package
        relocate("kotlin.jvm.internal", "com.cjcrafter.scheduler.kotlin.jvm.internal") {
            // stdlib
            include("kotlin.jvm.internal.**")
        }
    }

    jar {
        // Include all compiled classes from subprojects
        enabled = false
    }

    build {
        dependsOn(shadowJar)
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
