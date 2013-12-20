package io.github.dsh105.echopet.entity;

import org.bukkit.entity.Entity;

public interface ICraftPet extends Entity {

    public void remove();

    public IEntityPet getHandle();
}