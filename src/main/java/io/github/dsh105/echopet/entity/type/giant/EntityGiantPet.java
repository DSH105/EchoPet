package io.github.dsh105.echopet.entity.type.giant;

import io.github.dsh105.echopet.entity.*;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 5.5F, height = 5.5F)
public class EntityGiantPet extends EntityPet {

    public EntityGiantPet(World world) {
        super(world);
    }

    public EntityGiantPet(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    public PetType getEntityPetType() {
        return PetType.GIANT;
    }

    protected void makeStepSound() {
        this.makeSound("mob.zombie.step", 0.15F, 1.0F);
    }

    @Override
    protected String getIdleSound() {
        return "mob.zombie.say";
    }

    @Override
    protected String getDeathSound() {
        return "mob.zombie.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.OVERSIZE;
    }
}