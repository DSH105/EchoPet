package io.github.dsh105.echopet.entity.inanimate;

import io.github.dsh105.echopet.entity.IEntityPet;
import io.github.dsh105.echopet.entity.Pet;
import net.minecraft.server.v1_7_R1.Entity;
import net.minecraft.server.v1_7_R1.World;

public abstract class EntityInanimatePet extends Entity implements IEntityPet {


    public EntityInanimatePet(World world, InanimatePet pet) {
        super(world);
    }

    @Override
    public InanimatePet getPet() {
        return null;
    }
}