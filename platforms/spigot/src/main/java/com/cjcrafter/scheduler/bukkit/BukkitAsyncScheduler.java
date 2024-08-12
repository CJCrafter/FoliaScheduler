package com.cjcrafter.scheduler.bukkit;

import com.cjcrafter.scheduler.AsyncSchedulerImplementation;
import com.cjcrafter.scheduler.TaskImplementation;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@ApiStatus.Internal
public class BukkitAsyncScheduler implements AsyncSchedulerImplementation {

    private final @NotNull Plugin plugin;

    public BukkitAsyncScheduler(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    private <T> @NotNull BukkitRunnable buildBukkitRunnable(
        @NotNull Function<TaskImplementation<T>, T> function,
        @NotNull BukkitTask<T> taskImplementation
    ) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                taskImplementation.setCallback(function.apply(taskImplementation));
                taskImplementation.asFuture().complete(taskImplementation);
            }
        };
    }

    @Override
    public @NotNull <T> TaskImplementation<T> runNow(@NotNull Function<TaskImplementation<T>, T> function) {
        BukkitTask<T> taskImplementation = new BukkitTask<>(plugin, false);
        BukkitRunnable runnable = buildBukkitRunnable(function, taskImplementation);
        taskImplementation.setScheduledTask(runnable.runTaskAsynchronously(plugin));
        return taskImplementation;
    }

    @Override
    public @NotNull <T> TaskImplementation<T> runDelayed(@NotNull Function<TaskImplementation<T>, T> function, long delay, @NotNull TimeUnit unit) {
        BukkitTask<T> taskImplementation = new BukkitTask<>(plugin, false);
        BukkitRunnable runnable = buildBukkitRunnable(function, taskImplementation);
        taskImplementation.setScheduledTask(runnable.runTaskLaterAsynchronously(plugin, unit.toSeconds(delay) * 20));
        return taskImplementation;
    }

    @Override
    public @NotNull <T> TaskImplementation<T> runAtFixedRate(@NotNull Function<TaskImplementation<T>, T> function, long delay, long period, @NotNull TimeUnit unit) {
        BukkitTask<T> taskImplementation = new BukkitTask<>(plugin, true);
        BukkitRunnable runnable = buildBukkitRunnable(function, taskImplementation);
        taskImplementation.setScheduledTask(runnable.runTaskTimerAsynchronously(plugin, unit.toSeconds(delay) * 20, unit.toSeconds(period) * 20));
        return taskImplementation;
    }
}
