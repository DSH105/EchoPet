package io.github.dsh105.echopet.entity.pathfinder.goals;

import io.github.dsh105.echopet.entity.pathfinder.PetGoal;
import io.github.dsh105.echopet.entity.pet.EntityPet;

public class PetGoalFloat extends PetGoal {
	
	private EntityPet pet;
	
	public PetGoalFloat(EntityPet pet) {
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
