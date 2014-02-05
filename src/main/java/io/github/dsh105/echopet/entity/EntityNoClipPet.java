package io.github.dsh105.echopet.entity;

import io.github.dsh105.echopet.entity.EntityPet;
import net.minecraft.server.v1_7_R1.World;


public abstract class EntityNoClipPet extends EntityPet {

    public EntityNoClipPet(World world, Pet pet) {
        super(world, pet);
    }

    protected EntityNoClipPet(World world) {
        super(world);
    }

    public void noClip(boolean b) {
        this.Y = b;
    }
}