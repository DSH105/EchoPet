package io.github.dsh105.echopet.entity.living.type.chicken;

import io.github.dsh105.echopet.entity.living.EntityAgeablePet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import net.minecraft.server.v1_7_R1.World;

public class EntityChickenPet extends EntityAgeablePet {

    public EntityChickenPet(World world) {
        super(world);
    }

    public EntityChickenPet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.3F, 0.7F);
        this.fireProof = true;
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
