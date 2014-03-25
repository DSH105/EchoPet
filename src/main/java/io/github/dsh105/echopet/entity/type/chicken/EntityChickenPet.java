package io.github.dsh105.echopet.entity.type.chicken;

import io.github.dsh105.echopet.entity.EntityAgeablePet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.EntitySize;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import net.minecraft.server.v1_7_R2.World;

@EntitySize(width = 0.3F, height = 0.7F)
@EntityPetType(petType = PetType.CHICKEN)
public class EntityChickenPet extends EntityAgeablePet {

    public EntityChickenPet(World world) {
        super(world);
    }

    public EntityChickenPet(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.watch(12, new Integer(0));
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("mob.chicken.step", 0.15F, 1.0F);
    }

    @Override
    protected String getIdleSound() {
        return "mob.chicken.say";
    }

    @Override
    protected String getDeathSound() {
        return "mob.chicken.hurt";
    }
}
