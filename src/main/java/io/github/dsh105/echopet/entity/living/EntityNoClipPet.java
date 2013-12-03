package io.github.dsh105.echopet.entity.living;

import net.minecraft.server.v1_7_R1.World;


public abstract class EntityNoClipPet extends EntityLivingPet {

    public EntityNoClipPet(World world, LivingPet pet) {
        super(world, pet);
    }

    protected EntityNoClipPet(World world) {
        super(world);
    }

    public void noClip(boolean b) {
        this.Y = b;
    }
}