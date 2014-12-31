package com.dsh105.echopet.bridge.platform.sponge.entity;

import com.dsh105.commodus.Affirm;
import com.dsh105.commodus.container.PositionContainer;
import com.dsh105.commodus.container.Vector3dContainer;
import com.dsh105.echopet.bridge.entity.LivingEntityBridge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;

public class SpongeLivingEntityBridge<E extends Living> implements LivingEntityBridge {

    protected E getSpongeEntity() {
        // TODO
        return null;
    }

    @Override
    public E getBridgedEntity() {
        return getSpongeEntity();
    }

    @Override
    public boolean isAlive() {
        // TODO
        return false;
        //return !getSpongeEntity().isDead();
    }

    @Override
    public void setHealth(float health) {
        getSpongeEntity().setHealth(health);
    }

    @Override
    public float getHealth() {
        return (float) getSpongeEntity().getHealth();
    }

    @Override
    public float getMaxHealth() {
        return (float) getSpongeEntity().getMaxHealth();
    }

    @Override
    public void setMaxHealth(double maxHealth) {
        getSpongeEntity().setMaxHealth(maxHealth);
    }

    @Override
    public void setName(String name) {
        getSpongeEntity().setCustomName(name);
    }

    @Override
    public void setNameVisibility(boolean flag) {
        getSpongeEntity().setCustomNameVisible(flag);
    }

    @Override
    public void removeEntity() {
        getSpongeEntity().remove();
    }

    @Override
    public void setPassenger(LivingEntityBridge livingEntityBridge) {
        setPassenger(livingEntityBridge.getBridgedEntity());
    }

    @Override
    public void setPassenger(Object passenger) {
        Affirm.checkInstanceOf(Entity.class, passenger);
        getSpongeEntity().setPassenger((Entity) passenger);
    }

    @Override
    public PositionContainer getPassengerLocation() {
        Entity passenger = getSpongeEntity().getPassenger().get();
        return PositionContainer.from(passenger.getLocation(), passenger.getRotation());
    }

    @Override
    public void eject() {
        getSpongeEntity().setVehicle(null);
    }

    @Override
    public boolean move(PositionContainer to) {
        return getSpongeEntity().setLocation(to.toSponge().getLocation());
    }

    @Override
    public void setVelocity(Vector3dContainer vector3d) {
        // TODO
    }

    @Override
    public PositionContainer getLocation() {
        return PositionContainer.from(getSpongeEntity().getLocation(), getSpongeEntity().getRotation());
    }
}