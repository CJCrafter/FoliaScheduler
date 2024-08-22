package com.cjcrafter.foliascheduler.folia;

import com.cjcrafter.foliascheduler.TaskImplementation;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class FoliaTask<T> implements TaskImplementation<T> {

    private final ReentrantLock lock;
    private final AtomicReference<ScheduledTask> scheduledTaskRef;
    private final CompletableFuture<TaskImplementation<T>> future;
    private T callback;

    @ApiStatus.Internal
    public FoliaTask() {
        this.lock = new ReentrantLock();
        this.scheduledTaskRef = new AtomicReference<>();
        this.future = new CompletableFuture<>();
    }

    @ApiStatus.Internal
    public void setScheduledTask(@NotNull ScheduledTask task) {
        this.scheduledTaskRef.set(task);
    }

    @Override
    public @NotNull Plugin getOwningPlugin() {
        return scheduledTaskRef.get().getOwningPlugin();
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
        ScheduledTask.ExecutionState state = scheduledTaskRef.get().getExecutionState();
        return state == ScheduledTask.ExecutionState.RUNNING
                || state == ScheduledTask.ExecutionState.CANCELLED_RUNNING;
    }

    @Override
    public boolean isRepeatingTask() {
        return scheduledTaskRef.get().isRepeatingTask();
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
    public void setCallback(T callback) {
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
