package com.dsh105.echopet.bridge.entity;

public interface AgeableEntityBridge extends LivingEntityBridge {

    boolean isAdult();

    void setAdult(boolean flag);
}