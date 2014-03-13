package io.github.dsh105.echopet.entity.pathfinder.goals;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.pathfinder.PetGoal;
import io.github.dsh105.echopet.entity.pathfinder.PetGoalType;

public class PetGoalFloat extends PetGoal {

    private EntityPet pet;

    public PetGoalFloat(EntityPet pet) {
        this.pet = pet;
        pet.getNavigation().e(true);
    }

    @Override
    public PetGoalType getType() {
        return PetGoalType.FOUR;
    }

    @Override
    public String getDefaultKey() {
        return "Float";
    }

    @Override
    public boolean shouldStart() {
        return this.pet.M() || this.pet.P();
    }

    @Override
    public void tick() {
        if (this.pet.aI().nextFloat() < 0.8F) {
            this.pet.getControllerJump().a();
        }
    }
}
