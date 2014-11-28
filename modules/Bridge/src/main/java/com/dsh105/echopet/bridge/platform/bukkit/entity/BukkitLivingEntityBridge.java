package com.dsh105.echopet.bridge.platform.bukkit.entity;

import com.dsh105.echopet.bridge.PlatformBridge;
import com.dsh105.echopet.bridge.entity.LivingEntityBridge;
import org.bukkit.entity.LivingEntity;

public class BukkitLivingEntityBridge<E extends LivingEntity> implements LivingEntityBridge {

    protected E getBukkitEntity() {
        // TODO
        return null;
    }

    @Override
    public boolean isAlive() {
        return !getBukkitEntity().isDead();
    }
}