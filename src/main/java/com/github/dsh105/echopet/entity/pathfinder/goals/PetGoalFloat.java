package com.github.dsh105.echopet.entity.pathfinder.goals;

import com.github.dsh105.echopet.entity.pathfinder.PetGoal;
import com.github.dsh105.echopet.entity.pet.EntityPet;

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
		//https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/PathfinderGoalFloat.java#L18
		//a.aB() - 1.6.1
		if (pet.aC().nextFloat() < 0.8F) {
			this.pet.getControllerJump().a();
		}
	}
}
