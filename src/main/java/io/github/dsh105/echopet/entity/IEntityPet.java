package io.github.dsh105.echopet.entity;

import net.minecraft.server.v1_7_R1.IMonster;

public interface IEntityPet extends IMonster {

    public Pet getPet();

    public void remove(boolean makeSound);

    public void setInvisible(boolean flag);

    public boolean vnp();

    public void setVnp(boolean flag);
}