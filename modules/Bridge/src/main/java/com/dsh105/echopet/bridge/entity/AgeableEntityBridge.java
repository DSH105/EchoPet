package com.dsh105.echopet.bridge.entity;

public interface AgeableEntityBridge extends LivingEntityBridge {

    void setAgeLock(boolean flag);

    boolean isAdult();

    void setAdult();
}