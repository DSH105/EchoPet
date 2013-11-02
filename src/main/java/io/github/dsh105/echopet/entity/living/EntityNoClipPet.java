package io.github.dsh105.echopet.entity.living;

import net.minecraft.server.v1_6_R3.World;

/**
 * Project by DSH105
 */

public abstract class EntityNoClipPet extends EntityLivingPet {

    public EntityNoClipPet(World world, LivingPet pet) {
        super(world, pet);
    }

    protected EntityNoClipPet(World world) {
        super(world);
    }

    public void noClip(boolean b) {
        this.Z = b;
    }
}