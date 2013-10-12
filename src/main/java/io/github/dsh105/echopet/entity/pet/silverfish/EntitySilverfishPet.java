package io.github.dsh105.echopet.entity.pet.silverfish;

import io.github.dsh105.echopet.entity.pet.EntityPet;
import io.github.dsh105.echopet.entity.pet.Pet;
import io.github.dsh105.echopet.entity.pet.SizeCategory;
import net.minecraft.server.v1_6_R3.World;

public class EntitySilverfishPet extends EntityPet {

    public EntitySilverfishPet(World world) {
        super(world);
    }

    public EntitySilverfishPet(World world, Pet pet) {
        super(world, pet);
        this.a(0.3F, 0.7F);
        this.fireProof = true;
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("mob.silverfish.step", 0.15F, 1.0F);
    }

    @Override
    protected String getIdleSound() {
        return "mob.silverfish.say";
    }

    @Override
    protected String getDeathSound() {
        return "mob.silverfish.kill";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.TINY;
    }
}