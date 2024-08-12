package com.cjcrafter.foliascheduler.bukkit;

import com.cjcrafter.foliascheduler.EntitySchedulerImplementation;
import com.cjcrafter.foliascheduler.TaskImplementation;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@ApiStatus.Internal
public class BukkitEntityScheduler implements EntitySchedulerImplementation {

    private final @NotNull Plugin plugin;
    private final @NotNull Entity entity;

    @ApiStatus.Internal
    public BukkitEntityScheduler(@NotNull Plugin plugin, @NotNull Entity entity) {
        this.plugin = plugin;
        this.entity = entity;
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

    private boolean isRetired(@Nullable Runnable retired) {
        if (!entity.isValid()) {
            if (retired != null)
                retired.run();
            return true;
        }
        return false;
    }

    @Override
    public boolean execute(@NotNull Runnable run, @Nullable Runnable retired, long delay) {
        if (isRetired(retired))
            return false;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (isRetired(retired))
                    return;
                run.run();
            }
        }.runTaskLater(plugin, delay);
        return true;
    }

    @Override
    public @Nullable <T> TaskImplementation<T> run(@NotNull Function<TaskImplementation<T>, T> function, @Nullable Runnable retired) {
        if (isRetired(retired))
            return null;

        BukkitTask<T> taskImplementation = new BukkitTask<>(plugin, false);
        BukkitRunnable runnable = buildBukkitRunnable(function, taskImplementation);
        taskImplementation.setScheduledTask(runnable.runTask(plugin));
        return taskImplementation;
    }

    @Override
    public @Nullable <T> TaskImplementation<T> runDelayed(@NotNull Function<TaskImplementation<T>, T> function, @Nullable Runnable retired, long delay) {
        if (isRetired(retired))
            return null;

        BukkitTask<T> taskImplementation = new BukkitTask<>(plugin, false);
        BukkitRunnable runnable = buildBukkitRunnable(function, taskImplementation);
        taskImplementation.setScheduledTask(runnable.runTaskLater(plugin, delay));
        return taskImplementation;
    }

    @Override
    public @Nullable <T> TaskImplementation<T> runAtFixedRate(@NotNull Function<TaskImplementation<T>, T> function, @Nullable Runnable retired, long delay, long period) {
        if (isRetired(retired))
            return null;

        BukkitTask<T> taskImplementation = new BukkitTask<>(plugin, true);
        BukkitRunnable runnable = buildBukkitRunnable(function, taskImplementation);
        taskImplementation.setScheduledTask(runnable.runTaskTimer(plugin, delay, period));
        return taskImplementation;
    }
}
