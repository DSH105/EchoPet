package com.dsh105.echopet.bridge.entity;

public interface EquipableEntityBridge extends LivingEntityBridge {

    void setWeapon(Object itemStack);

    int getWeaponItemId();
}