package io.github.dsh105.echopet.entity.inanimate;

import io.github.dsh105.echopet.entity.IEntityPet;
import net.minecraft.server.v1_6_R3.Entity;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.World;

public class EntityInanimatePet extends Entity implements IEntityPet {


    public EntityInanimatePet(World world, InanimatePet pet) {
        super(world);
    }

    @Override
    protected void a() {
    }

    @Override
    protected void a(NBTTagCompound nbtTagCompound) {
    }

    @Override
    protected void b(NBTTagCompound nbtTagCompound) {
    }
}