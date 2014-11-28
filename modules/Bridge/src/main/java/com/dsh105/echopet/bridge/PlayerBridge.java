package com.dsh105.echopet.bridge;

import com.dsh105.echopet.bridge.PlatformBridge;

public interface PlayerBridge extends PlatformBridge {

    boolean isFlying();

    double getLocX();

    double getLocY();

    double getLocZ();

    float getPitch();

    float getYaw();

    boolean isOnGround();
}