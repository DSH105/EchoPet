package io.github.dsh105.echopet.entity.type.ghast;

import io.github.dsh105.echopet.entity.*;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 4.0F, height = 4.0F)
public class EntityGhastPet extends EntityPet {

    public EntityGhastPet(World world) {
        super(world);
    }

    public EntityGhastPet(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    public PetType getEntityPetType() {
        return PetType.GHAST;
    }

    @Override
    protected String getIdleSound() {
        return "mob.ghast.moan";
    }

    @Override
    protected String getDeathSound() {
        return "mob.ghast.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.OVERSIZE;
    }
}