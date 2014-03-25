package io.github.dsh105.echopet.entity.pathfinder.goals;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.pathfinder.PetGoal;
import io.github.dsh105.echopet.entity.pathfinder.PetGoalType;
import net.minecraft.server.v1_7_R2.EntityLiving;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;

public class PetGoalMeleeAttack extends PetGoal {

    EntityLiving target;
    EntityPet pet;
    int ticksBetweenAttack;
    int ticksUntilAttack;
    double lockRange;
    int navUpdate;

    public boolean isActive;

    public PetGoalMeleeAttack(EntityPet pet, double lockRange, int ticksBetweenAttack) {
        this.pet = pet;
        this.ticksBetweenAttack = this.ticksUntilAttack = ticksBetweenAttack;
        this.lockRange = lockRange * lockRange;
    }

    @Override
    public PetGoalType getType() {
        return PetGoalType.THREE;
    }

    @Override
    public String getDefaultKey() {
        return "Attack";
    }

    @Override
    public boolean shouldStart() {
        EntityLiving entityLiving = this.pet.getGoalTarget();
        if (entityLiving == null) {
            return false;
        } else if (!entityLiving.isAlive()) {
            return false;
        } else if (this.pet.e(entityLiving) >= this.lockRange) {
            return false;
        }
        this.target = entityLiving;

        return this.pet.getEntitySenses().canSee(entityLiving);
    }

    @Override
    public boolean shouldContinue() {
        EntityLiving entityliving = this.pet.getGoalTarget();

        if (entityliving == null) {
            return false;
        } else if (!entityliving.isAlive()) {
            return false;
        } else if (this.pet.e(((CraftPlayer) this.pet.getPlayerOwner()).getHandle()) >= this.pet.getSizeCategory().getTeleport(this.pet.getPet().getPetType())) {
            return false;
        }
        return true;
    }

    @Override
    public void start() {
        this.isActive = true;
        this.pet.getNavigation().a(this.target);
        this.navUpdate = 0;
    }

    @Override
    public void finish() {
        this.isActive = false;
        this.pet.getNavigation().h();
    }

    @Override
    public void tick() {
        this.pet.getControllerLook().a(this.target, 30.0F, 30.0F);
        if (this.pet.getEntitySenses().canSee(this.target) && --this.navUpdate <= 0) {
            this.navUpdate = 4 + this.pet.random().nextInt(7);
            this.pet.getNavigation().a(this.target);
        }

        this.ticksUntilAttack = Math.max(this.ticksUntilAttack - 1, 0);
        double attackRange = (double) (this.pet.width * 2.0F * this.pet.width * 2.0F + this.target.width);

        if (this.pet.e(this.target.locX, this.target.boundingBox.b, this.target.locZ) <= attackRange) {
            if (this.ticksUntilAttack <= 0) {
                this.ticksUntilAttack = this.ticksBetweenAttack;

                // Arm animation
                if (this.pet.bd() != null) {
                    this.pet.aZ();
                }
                //this.pet.m(this.target)
                this.pet.attack(this.target);
            }
        }
    }
}