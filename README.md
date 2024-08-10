<div align="center">

# FoliaScheduler

</div>

FoliaScheduler is a fully-featured task scheduler for plugins seeking Spigot/Paper/Folia support. With clean syntax,
you can now schedule tasks on entities, regions, asynchronously, and more!

## Features
* Java 1.8+ (but you will need a JDK of 17+ to shade the Folia compatibility classes!)
* Folia 1.20+
* Spigot 1.12.2+ (and *likely* any older version)
* Paper 1.12.2+ (and *likely* any older version)
* Task chaining (`CompletableFuture` support)
* Easy task cancellation (Some libs don't have this... for some crazy reason)

## How it works
We simply try to use Folia's scheduler (included in paper server jars) if it's available. If not, we fallback to
Spigot's scheduler (using `BukkitRunnable`, which allows basically any server version to use).

## Maven
```xml
<dependency>
    <groupId>com.cjcrafter</groupId>
    <artifactId>foliascheduler</artifactId>
    <version>0.3.4</version>
</dependency>
```

## Gradle
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.cjcrafter:foliascheduler:0.3.4")
}
```

## How do I convert my whole plugin to Folia?
Pretty much any plugin is already "compatible" with Folia. Usually, the only change you need to worry about
is the scheduler. Any usage of 
[`BukkitRunnable`](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/scheduler/BukkitRunnable.html) and 
[`Bukkit#getScheduler`](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Bukkit.html#getScheduler()) will
need to be replaced.

You'll also need to add `folia-supported: true` to your plugin.yml. 

Here are some PRs that show how to convert a plugin to Folia:
* [WeaponMechanics](https://github.com/WeaponMechanics/MechanicsMain/pull/433/)
* (Do you have a PR that shows how to convert a plugin to Folia using this lib? Let me know, or just make a PR to add it here!)

#### Thread safety tips
* Before modifying any block/entity, you can check if your thread is correct by using:
  * `ServerImplementation#isOwnedByCurrentRegion(Entity)`
  * `ServerImplementation#isOwnedByCurrentRegion(Block)`
* Typically, you should not use `ServerImplementation#async()` unless you are doing I/O operations or heavy calculations.
  * When you use async, you should probably use a callback... See examples below...
* It's generally safe to send packets to players on any thread, since it doesn't modify the state of anything. 


## Examples
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
