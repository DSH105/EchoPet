package com.dsh105.echopet.bridge.platform.bukkit;

import com.dsh105.echopet.bridge.BridgeManager;
import com.dsh105.echopet.bridge.SchedulerBridge;
import com.dsh105.echopet.bridge.platform.TaskId;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class BukkitSchedulerBridge implements SchedulerBridge {

    @Override
    public TaskId run(boolean asynchronous, Runnable task) {
        return runLater(asynchronous, 0, task);
    }

    @Override
    public TaskId runLater(boolean asynchronous, long delay, Runnable task) {
        BukkitRunnable bukkitRunnable = create(task);
        if (asynchronous) {
            bukkitRunnable.runTaskLaterAsynchronously((Plugin) BridgeManager.getBridgeManager().getPluginCore(), delay);
        } else {
            bukkitRunnable.runTaskLater((Plugin) BridgeManager.getBridgeManager().getPluginCore(), delay);
        }
        return TaskId.from(bukkitRunnable);
    }

    @Override
    public TaskId runTimer(boolean asynchronous, long period, Runnable task) {
        return runTimer(asynchronous, 0, period, task);
    }

    @Override
    public TaskId runTimer(boolean asynchronous, long delay, long period, Runnable task) {
        BukkitRunnable bukkitRunnable = create(task);
        if (asynchronous) {
            bukkitRunnable.runTaskTimerAsynchronously((Plugin) BridgeManager.getBridgeManager().getPluginCore(), delay, period);
        } else {
            bukkitRunnable.runTaskTimer((Plugin) BridgeManager.getBridgeManager().getPluginCore(), delay, period);
        }
        return TaskId.from(bukkitRunnable);
    }

    @Override
    public TaskId runLater(boolean asynchronous, TimeUnit timeUnit, long delay, Runnable task) {
        return runLater(asynchronous, timeUnit.toSeconds(delay) * 20, task);
    }

    @Override
    public TaskId runTimer(boolean asynchronous, TimeUnit timeUnit, long period, Runnable task) {
        return runTimer(asynchronous, timeUnit.toSeconds(period) * 20, task);
    }

    @Override
    public TaskId runTimer(boolean asynchronous, TimeUnit timeUnit, long delay, long period, Runnable task) {
        return runTimer(asynchronous, timeUnit.toSeconds(delay) * 20, timeUnit.toSeconds(period) * 20, task);
    }

    private BukkitRunnable create(final Runnable task) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        };
    }
}