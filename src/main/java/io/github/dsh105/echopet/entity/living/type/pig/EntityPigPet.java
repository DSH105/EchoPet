package io.github.dsh105.echopet.entity.living.type.pig;

import io.github.dsh105.echopet.entity.living.EntityAgeablePet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import net.minecraft.server.v1_7_R1.World;

public class EntityPigPet extends EntityAgeablePet {

    public EntityPigPet(World world) {
        super(world);
    }

    public EntityPigPet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.9F, 0.9F);
        this.fireProof = true;
    }

    public void setBaby(boolean flag) {
        if (flag) {
            this.datawatcher.watch(12, Integer.valueOf(Integer.MIN_VALUE));
        } else {
            this.datawatcher.watch(12, new Integer(0));
        }
        ((PigPet) pet).baby = flag;
    }

    public boolean hasSaddle() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    public void setSaddle(boolean flag) {
        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) 1));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) 0));
        }
        ((PigPet) pet).saddle = flag;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("mob.pig.step", 0.15F, 1.0F);
    }

    @Override
    protected String getIdleSound() {
        return "mob.pig.say";
    }

    @Override
    protected String getDeathSound() {
        return "mob.pig.death";
    }
}