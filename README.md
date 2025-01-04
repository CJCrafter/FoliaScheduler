<div align="center">

# FoliaScheduler

</div>

FoliaScheduler is a fully-featured task scheduler for plugins seeking Spigot/Paper/Folia support. With clean syntax,
you can now schedule tasks on entities, regions, asynchronously, and more!

## Features
* Java 1.8+ (but you will need a JDK of 17+ to shade the Folia compatibility classes!)
* Folia 1.20+
* Spigot/Paper 1.12.2+ (and *likely* any older version)
* Task chaining w/ callback values (`CompletableFuture<T>` support)
* Easy task cancellation through both `Consumer<TaskImplementation<T>>` and return values.
* [`ServerVersions`](https://github.com/CJCrafter/FoliaScheduler/blob/master/src/main/java/com/cjcrafter/foliascheduler/util/ServerVersions.java) and [`MinecraftVersions`](https://github.com/CJCrafter/FoliaScheduler/blob/master/src/main/java/com/cjcrafter/foliascheduler/util/MinecraftVersions.java) utility classes for checking server type and version.
* [`ReflectionUtil`](https://github.com/CJCrafter/FoliaScheduler/blob/master/src/main/java/com/cjcrafter/foliascheduler/util/ReflectionUtil.java) with automatic remapping for Paper remapping compatibility.

## How it works
We use Folia's scheduler (included in paper server jars) if it is available. If not, we fallback to
Spigot's scheduler, `BukkitRunnable`. This adds support for practically any server. 

## Maven
```xml
<dependency>
    <groupId>com.cjcrafter</groupId>
    <artifactId>foliascheduler</artifactId>
    <version>0.6.3</version>
</dependency>
```

<details>
<summary><b>[Full Maven + Shade example]</b></summary>

```xml
<dependencies>
    <dependency>
        <groupId>com.cjcrafter</groupId>
        <artifactId>foliascheduler</artifactId>
        <version>0.6.3</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.6.0</version>  <!-- always check for latest -->
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <relocations>
                            <relocation>
                                <pattern>com.cjcrafter.foliascheduler</pattern>
                                <shadedPattern>com.example.foliascheduler</shadedPattern>
                            </relocation>
                        </relocations>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
</details>

## Gradle
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.cjcrafter:foliascheduler:0.6.3")
}
```

<details>
<summary><b>[Full Gradle + Shade example]</b></summary>

```kotlin
plugins {
    java  // or kotlin("jvm") version "..."
    //id("com.github.johnrengelman.shadow") version "8.1.1"  // for below Java 21... always check for latest
    id("io.github.gooler.shadow") version "8.1.7"  // for Java 21+... always check for latest
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"  // always check for latest
}

repositories {
    mavenCentral()
}

dependencies {
    // TODO add your version of Spigot/Paper here
    implementation("com.cjcrafter:foliascheduler:0.6.3")
}

// See https://github.com/Minecrell/plugin-yml
bukkit {
    main = "com.example.MyPlugin"
    foliaSupported = true
}

tasks.shadowJar {
    archiveFileName.set("MyPlugin-${project.version}.jar")
    relocate("com.cjcrafter.foliascheduler", "com.example.foliascheduler")
}
```
</details>

## How do I convert my whole plugin to Folia?
Your plugin is *SO CLOSE* to working on Folia, except you need to change usage of 
[`BukkitRunnable`](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/scheduler/BukkitRunnable.html) and 
[`Bukkit#getScheduler`](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Bukkit.html#getScheduler()),
and add `folia-supported: true` to your plugin.yml. 

Not sure where to start? Take a look at these projects that used this library to add Folia:
* [WeaponMechanics](https://github.com/WeaponMechanics/MechanicsMain/pull/433/)
* [VivecraftSpigot](https://github.com/CJCrafter/VivecraftSpigot/pull/5)

#### Thread safety tips
* Before modifying any block/entity, you can check if your thread is correct by using:
  * `ServerImplementation#isOwnedByCurrentRegion(Entity)`
  * `ServerImplementation#isOwnedByCurrentRegion(Block)`
* Typically, you should not use `ServerImplementation#async()` unless you are doing I/O operations or heavy calculations.
  * When you use async, you should probably use a sync callback (See examples below)
* It is safe to send packets to players on any thread. 

## How do I check the server's version?
We provide 2 utility classes:
* `ServerVersions` - Check `isFolia()` and `isPaper()`
* `MinecraftVersions` - Get current `major.minor.patch` version

```java
    if (!MinecraftVersions.UPDATE_AQUATIC.isAtLeast()) {
        getLogger().warning("Uh oh! You're not on 1.13+! This plugin may not work correctly!");
        // disable plugin
    }
```

## Scheduler Examples

#### Modify an entity on the next tick
```java
    Plugin plugin = null;
    Entity entity = null;

    // You should re-use this instance
    ServerImplementation scheduler = new FoliaCompatibility(plugin).getServerImplementation();
    scheduler.entity(entity).run(() -> {
        entity.setVelocity(new Vector(0, 1, 0));
    }); 
```

#### Execute an async task after 5 seconds
```java
    Plugin plugin = null;

    // You should re-use this instance
    ServerImplementation scheduler = new FoliaCompatibility(plugin).getServerImplementation();
    scheduler.async().runDelayed(task -> {
        plugin.getLogger().info("This is the scheduled task! I'm async! " + task);
    }, 5 * 20L);
```

#### Doing some I/O operations with a callback
```java
    Plugin plugin = null;
    File file = null;

    // You should re-use this instance
    ServerImplementation scheduler = new FoliaCompatibility(plugin).getServerImplementation();
    TaskImplementation<String> scheduledTask = scheduler.async().runNow(task -> {
        String contents = "Read the file";
        return contents;
    });

    // Use "thenCompose" to chain many tasks together
    CompletableFuture<TaskImplementation<Void>> composed = scheduledTask.asFuture().thenCompose(task -> {
        return scheduler.global().run(nextTask -> {
            System.out.println("We're back on the global thread: " + scheduledTask.getCallback());
        }).asFuture();
    });
        
    composed.thenAccept(task -> {
        System.out.println("We finished everything now!");
    });
```

## Using Reflection
Paper and Spigot often "*disagree*" on what to name a class, field, or method. This is a
major problem if you plan on using reflection to access these classes. Like this example:
```java
    Class.forName("net.minecraft.world.entity.monster.EntityCreeper");  // Works on Spigot servers
    Class.forName("net.minecraft.world.entity.monster.Creeper");  // Works on Paper servers
```

To solve this, we provide a utility class called [`ReflectionUtil`](https://github.com/CJCrafter/FoliaScheduler/blob/master/src/main/java/com/cjcrafter/foliascheduler/util/ReflectionUtil.java)
that will automatically remap classes, fields, and methods for you. The example
above can be solved by using the code below:

```java
    // Always provide a Spigot-mapped class, and we'll remap as needed
    Class<?> entityCreeper = ReflectionUtil.getMinecraftClass("net.minecraft.world.entity.monster.EntityCreeper");
```
