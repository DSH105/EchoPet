package io.github.dsh105.echopet.entity.pathfinder.goals;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.pathfinder.PetGoal;
import io.github.dsh105.echopet.entity.pathfinder.PetGoalType;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityHuman;

public class PetGoalLookAtPlayer extends PetGoal {

    private EntityPet pet;
    protected Entity player;
    private float range;
    private int ticksLeft;
    private float chance;
    private Class clazz;

    public PetGoalLookAtPlayer(EntityPet pet, Class c, float f) {
        this.pet = pet;
        this.range = f;
        this.chance = 0.1F;
        this.clazz = c;
    }

    public PetGoalLookAtPlayer(EntityPet pet, Class c, float f, float f1) {
        this.pet = pet;
        this.range = f;
        this.chance = f1;
        this.clazz = c;
    }

    @Override
    public PetGoalType getType() {
        return PetGoalType.TWO;
    }

    @Override
    public String getDefaultKey() {
        return "LookAtPlayer";
    }

    @Override
    public boolean shouldStart() {
        if (this.pet.random().nextFloat() >= this.chance) {
            return false;
        } else if (this.pet.passenger != null) {
            return false;
        } else {
            if (this.clazz == EntityHuman.class) {
                this.player = this.pet.world.findNearbyPlayer(this.pet, (double) this.range);
            } else {
                this.player = this.pet.world.a(this.clazz, this.pet.boundingBox.grow((double) this.range, 3.0D, (double) this.range), this.pet);
            }
            return this.player != null;
        }
    }

    @Override
    public boolean shouldContinue() {
        return !this.player.isAlive() ? false : (this.pet.e(this.player) > (double) (this.range * this.range) ? false : this.ticksLeft > 0);
    }

    @Override
    public void start() {
        this.ticksLeft = 40 + this.pet.random().nextInt(40);
    }

    @Override
    public void finish() {
        this.player = null;
    }

    @Override
    public void tick() {
        this.pet.getControllerLook().a(this.player.locX, this.player.locY + (double) this.player.getHeadHeight(), this.player.locZ, 10.0F, (float) this.pet.bv());
        --this.ticksLeft;
    }
}
