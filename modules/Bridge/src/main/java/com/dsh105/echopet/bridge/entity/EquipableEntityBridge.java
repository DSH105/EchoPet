package com.dsh105.echopet.bridge.entity;

import com.dsh105.commodus.container.ItemStackContainer;

public interface EquipableEntityBridge extends LivingEntityBridge {

    void setWeapon(ItemStackContainer itemStack);

    ItemStackContainer getWeapon();
}