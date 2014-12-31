package com.dsh105.echopet.bridge.platform.sponge;

import com.dsh105.commodus.sponge.SpongeUtil;
import com.dsh105.echopet.bridge.SchedulerBridge;
import com.dsh105.echopet.bridge.platform.TaskId;
import org.spongepowered.api.service.scheduler.Scheduler;

import java.util.concurrent.TimeUnit;

public class SpongeSchedulerBridge implements SchedulerBridge {

    private Object spongePlugin = SpongeUtil.getPluginContainer().getInstance();
    private Scheduler scheduler = SpongeUtil.getGame().getScheduler();

    @Override
    public TaskId run(boolean asynchronous, Runnable task) {
        return runLater(asynchronous, 0, task);
    }

    @Override
    public TaskId runLater(boolean asynchronous, long delay, Runnable task) {
        // TODO: async or not? o.O
        if (asynchronous) {
            return null;
        } else {
            return TaskId.from(scheduler.runTaskAfter(spongePlugin, task, delay).get());
        }
    }

    @Override
    public TaskId runTimer(boolean asynchronous, long period, Runnable task) {
        return runTimer(asynchronous, 0, period, task);
    }

    @Override
    public TaskId runTimer(boolean asynchronous, long delay, long period, Runnable task) {
        if (asynchronous) {
            // TODO
            return null;
        } else {
            return TaskId.from(scheduler.runRepeatingTaskAfter(spongePlugin, task, period, delay).get());
        }
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
}