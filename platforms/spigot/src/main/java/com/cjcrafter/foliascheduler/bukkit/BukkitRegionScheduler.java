package com.cjcrafter.foliascheduler.bukkit;

import com.cjcrafter.foliascheduler.RegionSchedulerImplementation;
import com.cjcrafter.foliascheduler.TaskImplementation;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class BukkitRegionScheduler implements RegionSchedulerImplementation {

    private final @NotNull Plugin plugin;

    public BukkitRegionScheduler(@NotNull Plugin plugin) {
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
    public void execute(@NotNull Runnable run) {
        new BukkitRunnable() {
            @Override
            public void run() {
                run.run();
            }
        }.runTask(plugin);
    }

    @Override
    public @NotNull <T> TaskImplementation<T> run(@NotNull Function<TaskImplementation<T>, T> function) {
        BukkitTask<T> taskImplementation = new BukkitTask<>(plugin, false);
        BukkitRunnable runnable = buildBukkitRunnable(function, taskImplementation);
        taskImplementation.setScheduledTask(runnable.runTask(plugin));
        return taskImplementation;
    }

    @Override
    public @NotNull <T> TaskImplementation<T> runDelayed(@NotNull Function<TaskImplementation<T>, T> function, long delay) {
        BukkitTask<T> taskImplementation = new BukkitTask<>(plugin, false);
        BukkitRunnable runnable = buildBukkitRunnable(function, taskImplementation);
        taskImplementation.setScheduledTask(runnable.runTaskLater(plugin, delay));
        return taskImplementation;
    }

    @Override
    public @NotNull <T> TaskImplementation<T> runAtFixedRate(@NotNull Function<TaskImplementation<T>, T> function, long delay, long period) {
        BukkitTask<T> taskImplementation = new BukkitTask<>(plugin, true);
        BukkitRunnable runnable = buildBukkitRunnable(function, taskImplementation);
        taskImplementation.setScheduledTask(runnable.runTaskTimer(plugin, delay, period));
        return taskImplementation;
    }
}
