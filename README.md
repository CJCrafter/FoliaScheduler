<div align="center">

# FoliaScheduler

</div>

FoliaScheduler is a fully-featured task scheduler for plugins seeking Spigot/Paper/Folia support. With clean syntax,
you can now schedule tasks on entities, regions, asynchronously, and more!

## Features
* Folia 1.20+
* Spigot 1.12.2+ (and *likely* any older version)
* Paper 1.12.2+ (and *likely* any older version)
* Task chaining (`CompletableFuture` support)
* Easy task cancellation (Some libs don't have this... for some crazy reason)

## How it works
We simply try to use Folia's scheduler (included in paper server jars) if it's available. If not, we fallback to
Spigot's scheduler (using `BukkitRunnable`, which allows basically any server version to use). We expect java 8+.

## Maven
```xml
<dependency>
    <groupId>com.cjcrafter</groupId>
    <artifactId>foliascheduler</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Gradle
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.cjcrafter:foliascheduler:0.1.0")
}
```

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
