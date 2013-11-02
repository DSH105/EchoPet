package io.github.dsh105.echopet.entity.living.pathfinder.goals;

import io.github.dsh105.echopet.entity.living.pathfinder.PetGoal;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;

public class PetGoalFloat extends PetGoal {

    private EntityLivingPet pet;

    public PetGoalFloat(EntityLivingPet pet) {
        this.pet = pet;
        pet.getNavigation().e(true);
    }

    @Override
    public boolean shouldStart() {
        return pet.world.getMaterial((int) pet.locX, (int) pet.locY, (int) pet.locZ).isLiquid();
    }

    @Override
    public void tick() {
        if (pet.aD().nextFloat() < 0.8F) {
            this.pet.getControllerJump().a();
        }
    }
}
