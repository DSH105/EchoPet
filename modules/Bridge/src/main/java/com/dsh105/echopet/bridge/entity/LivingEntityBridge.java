package com.dsh105.echopet.bridge.entity;

import com.dsh105.commodus.container.PositionContainer;
import com.dsh105.commodus.container.Vector3dContainer;
import com.dsh105.echopet.bridge.PlatformBridge;

public interface LivingEntityBridge extends PlatformBridge {

    Object getBridgedEntity();

    boolean isAlive();

    void setHealth(float health);

    float getHealth();

    float getMaxHealth();

    void setMaxHealth(double maxHealth);

    void setName(String name);

    void setNameVisibility(boolean flag);

    void removeEntity();

    void setPassenger(LivingEntityBridge livingEntityBridge);

    void setPassenger(Object passenger);

    PositionContainer getPassengerLocation();

    void eject();

    boolean move(PositionContainer to);

    void setVelocity(Vector3dContainer vector3d);

    PositionContainer getLocation();
}