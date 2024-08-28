package com.cjcrafter.foliascheduler.bukkit;

import com.cjcrafter.foliascheduler.TaskImplementation;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

@ApiStatus.Internal
public class BukkitTask<T> implements TaskImplementation<T> {

    private final @NotNull Plugin owningPlugin;
    private final boolean isRepeatingTask;
    private final @NotNull ReentrantLock lock;
    private final @NotNull AtomicReference<org.bukkit.scheduler.BukkitTask> scheduledTaskRef;
    private final @NotNull CompletableFuture<TaskImplementation<T>> future;
    private @Nullable T callback;

    @ApiStatus.Internal
    public BukkitTask(@NotNull Plugin owningPlugin, boolean isRepeatingTask) {
        this.owningPlugin = owningPlugin;
        this.isRepeatingTask = isRepeatingTask;
        this.lock = new ReentrantLock();
        this.scheduledTaskRef = new AtomicReference<>();
        this.future = new CompletableFuture<>();
    }

    @ApiStatus.Internal
    public void setScheduledTask(@NotNull org.bukkit.scheduler.BukkitTask task) {
        this.scheduledTaskRef.set(task);
    }

    @Override
    public @NotNull Plugin getOwningPlugin() {
        return owningPlugin;
    }

    @Override
    public void cancel() {
        scheduledTaskRef.get().cancel();
    }

    @Override
    public boolean isCancelled() {
        return scheduledTaskRef.get().isCancelled();
    }

    @Override
    public boolean isRunning() {
        return owningPlugin.getServer().getScheduler().isCurrentlyRunning(scheduledTaskRef.get().getTaskId());
    }

    @Override
    public boolean isRepeatingTask() {
        return isRepeatingTask;
    }

    @Override
    public @Nullable T getCallback() {
        lock.lock();
        try {
            return callback;
        } finally {
            lock.unlock();
        }
    }

    @ApiStatus.Internal
    public void setCallback(@Nullable T callback) {
        lock.lock();
        try {
            this.callback = callback;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public @NotNull CompletableFuture<TaskImplementation<T>> asFuture() {
        return future;
    }
}
