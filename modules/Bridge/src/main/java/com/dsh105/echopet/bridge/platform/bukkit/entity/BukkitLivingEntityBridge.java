package com.dsh105.echopet.bridge.platform.bukkit.entity;

import com.dsh105.commodus.Affirm;
import com.dsh105.commodus.container.PositionContainer;
import com.dsh105.commodus.container.Vector3dContainer;
import com.dsh105.echopet.bridge.entity.LivingEntityBridge;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class BukkitLivingEntityBridge<E extends LivingEntity> implements LivingEntityBridge {

    protected E getBukkitEntity() {
        // TODO
        return null;
    }

    @Override
    public E getBridgedEntity() {
        return getBukkitEntity();
    }

    @Override
    public boolean isAlive() {
        return !getBukkitEntity().isDead();
    }

    @Override
    public void setHealth(float health) {
        getBukkitEntity().setHealth(health);
    }

    @Override
    public float getHealth() {
        return (float) getBukkitEntity().getHealth();
    }

    @Override
    public float getMaxHealth() {
        return (float) getBukkitEntity().getMaxHealth();
    }

    @Override
    public void setMaxHealth(double maxHealth) {
        getBukkitEntity().setMaxHealth(maxHealth);
    }

    @Override
    public void setName(String name) {
        getBukkitEntity().setCustomName(name);
    }

    @Override
    public void setNameVisibility(boolean flag) {
        getBukkitEntity().setCustomNameVisible(flag);
    }

    @Override
    public void removeEntity() {
        getBukkitEntity().remove();
    }

    @Override
    public void setPassenger(LivingEntityBridge livingEntityBridge) {
        setPassenger(livingEntityBridge.getBridgedEntity());
    }

    @Override
    public void setPassenger(Object passenger) {
        Affirm.checkInstanceOf(Entity.class, passenger);
        getBukkitEntity().setPassenger((Entity) passenger);
    }

    @Override
    public PositionContainer getPassengerLocation() {
        return PositionContainer.from(getBukkitEntity().getLocation());
    }

    @Override
    public void eject() {
        getBukkitEntity().eject();
    }

    @Override
    public boolean move(PositionContainer to) {
        return getBukkitEntity().teleport(to.toBukkit());
    }

    @Override
    public void setVelocity(Vector3dContainer vector3d) {
        getBukkitEntity().setVelocity(vector3d.toBukkit());
    }

    @Override
    public PositionContainer getLocation() {
        return PositionContainer.from(getBukkitEntity().getLocation());
    }
}