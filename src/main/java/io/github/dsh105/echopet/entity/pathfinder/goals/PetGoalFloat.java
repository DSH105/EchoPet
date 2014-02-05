package io.github.dsh105.echopet.entity.pathfinder.goals;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.pathfinder.PetGoal;

public class PetGoalFloat extends PetGoal {

    private EntityPet pet;

    public PetGoalFloat(EntityPet pet) {
        this.pet = pet;
        pet.getNavigation().e(true);
    }

    @Override
    public boolean shouldStart() {
        // Returns if the pet is in water. A handy change :D
        return this.pet.M();
    }

    @Override
    public void tick() {
        if (pet.random().nextFloat() < 0.8F) {
            this.pet.getControllerJump().a();
        }
    }
}
