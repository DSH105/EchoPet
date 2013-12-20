package io.github.dsh105.echopet.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;

public interface ICraftPet extends Entity, Vehicle {

    public void remove();

    public IEntityPet getHandle();
}