package io.github.dsh105.echopet.entity.type.cow;

import io.github.dsh105.echopet.entity.EntityAgeablePet;
import io.github.dsh105.echopet.entity.EntitySize;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 0.9F, height = 1.3F)
public class EntityCowPet extends EntityAgeablePet {

    public EntityCowPet(World world) {
        super(world);
    }

    public EntityCowPet(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    public PetType getEntityPetType() {
        return PetType.COW;
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("mob.cow.step", 0.15F, 1.0F);
    }

    @Override
    protected String getIdleSound() {
        return "mob.cow.say";
    }

    @Override
    protected String getDeathSound() {
        return "mob.cow.hurt";
    }
}
