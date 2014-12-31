package com.dsh105.echopet.bridge;

import com.dsh105.echopet.bridge.platform.TaskId;

import java.util.concurrent.TimeUnit;

public interface SchedulerBridge extends GenericBridge {

    TaskId run(boolean asynchronous, Runnable task);

    TaskId runLater(boolean asynchronous, long delay, Runnable task);

    TaskId runTimer(boolean asynchronous, long period, Runnable task);

    TaskId runTimer(boolean asynchronous, long delay, long period, Runnable task);

    TaskId runLater(boolean asynchronous, TimeUnit timeUnit, long delay, Runnable task);

    TaskId runTimer(boolean asynchronous, TimeUnit timeUnit, long period, Runnable task);

    TaskId runTimer(boolean asynchronous, TimeUnit timeUnit, long delay, long period, Runnable task);
}