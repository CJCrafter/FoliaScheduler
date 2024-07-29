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
Plugin plugin = /* your plugin instance */;
Entity entity = /* your entity */;

// You should re-use this instance
ServerImplementation scheduler = new FoliaCompatibility(plugin).getServerImplementation();
scheduler.entity(entity).run(task -> {
    entity.setVelocity(new Vector(0, 1, 0));    
});
```

#### Execute an async task after 5 seconds
```java
Plugin plugin = /* your plugin instance */;

// You should re-use this instance
ServerImplementation scheduler = new FoliaCompatibility(plugin).getServerImplementation();
scheduler.async().runDelayed(task -> {
    plugin.getLogger().info("Yippee! I'm async!");    
});
```

#### Doing some I/O operations with a callback
```java
Plugin plugin = /* your plugin instance */;
File file = /* your file */;

// You should re-use this instance
ServerImplementation scheduler = new FoliaCompatibility(plugin).getServerImplementation();
TaskImplementation scheduledTask = scheduler.async().runNow(task -> {
    // Do some I/O operations
});

// You can also get fancy with *thenCompose* and *thenAccept*
scheduledTask.asFuture().thenAccept(task -> {
    scheduler.sync().run(task -> {
        plugin.getLogger().info("We did some I/O");
    })
});
```
