package com.cjcrafter.foliascheduler;

import java.util.concurrent.CompletableFuture;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TaskImplementation<T> {

    /**
     * Returns the plugin that owns this task.
     *
     * @return The plugin that owns this task.
     */
    @NotNull Plugin getOwningPlugin();

    /** Cancels this task. */
    void cancel();

    /**
     * Returns whether the task has been cancelled.
     *
     * @return Whether the task has been cancelled.
     */
    boolean isCancelled();

    /**
     * Returns whether the task is currently executing.
     *
     * @return Whether the task is currently executing.
     */
    boolean isRunning();

    /**
     * Returns whether the task is a repeating task.
     *
     * @return Whether the task is a repeating task.
     */
    boolean isRepeatingTask();

    /**
     * Returns the callback from this task's execution.
     *
     * <p>If the task has not been completed yet, this method will always return null. To check if
     * the task has been completed, use {@link #asFuture()}.
     *
     * @return The callback value from this task's execution.
     */
    @Nullable T getCallback();

    /**
     * Returns a CompletableFuture that will be completed when the task has finished executing. For
     * repeating tasks, the future will be completed on the first loop execution.
     *
     * <p>The future may not be completed if the task is cancelled before it is run, or if an
     * exception occurs during execution of the task.
     *
     * @return A CompletableFuture that will be completed when the task has finished executing.
     */
    @NotNull CompletableFuture<TaskImplementation<T>> asFuture();
}
