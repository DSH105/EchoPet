package io.github.dsh105.echopet.entity.living.pathfinder.goals;

import io.github.dsh105.echopet.entity.living.pathfinder.PetGoal;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import net.minecraft.server.v1_6_R3.Entity;
import net.minecraft.server.v1_6_R3.EntityHuman;

public class PetGoalLookAtPlayer extends PetGoal {

    private EntityLivingPet pet;
    protected Entity player;
    private float range;
    private int ticksLeft;
    private float chance;
    private Class clazz;

    public PetGoalLookAtPlayer(EntityLivingPet pet, Class c, float f) {
        this.pet = pet;
        this.range = f;
        this.chance = 0.2F;
        this.clazz = c;
    }

    public PetGoalLookAtPlayer(EntityLivingPet pet, Class c, float f, float f1) {
        this.pet = pet;
        this.range = f;
        this.chance = f1;
        this.clazz = c;
    }

    @Override
    public boolean shouldStart() {
        if (this.pet.aD().nextFloat() >= this.chance) {
            return false;
        } else if (this.pet.passenger != null) {
            return false;
        } else {
            if (this.clazz == EntityHuman.class) {
                this.player = this.pet.world.findNearbyPlayer(this.pet, (double) this.range);
            } else {
                this.player = this.pet.world.a(this.clazz, this.player.boundingBox.grow((double) this.range, 3.0D, (double) this.range), this.player);
            }
            return this.player != null;
        }
    }

    @Override
    public boolean shouldFinish() {
        return !this.player.isAlive() ? false : (this.pet.e(this.player) > (double) (this.range * this.range) ? false : this.ticksLeft > 0);
    }

    public void c() {
        this.ticksLeft = 40 + this.pet.aD().nextInt(40);
    }

    public void d() {
        this.player = null;
    }

    public void e() {
        this.pet.getControllerLook().a(this.player.locX, this.player.locY + (double) this.player.getHeadHeight(), this.player.locZ, 10.0F, (float) this.pet.bp());
        --this.ticksLeft;
    }
}
