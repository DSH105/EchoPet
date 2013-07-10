package com.github.dsh105.echopet.entity.pathfinder.goals;

import com.github.dsh105.echopet.entity.pathfinder.PetGoal;
import com.github.dsh105.echopet.entity.pet.EntityPet;

public class PetGoalFloat extends PetGoal {
	
	private EntityPet a;
	
	public PetGoalFloat(EntityPet pet) {
		this.a = pet;
		a.getNavigation().e(true);
	}
	
	@Override
	public boolean a() {
		return a.world.getMaterial((int) a.locX, (int) a.locY, (int) a.locZ).isLiquid();
	}
	
	@Override
	public void e() {
		//https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/PathfinderGoalFloat.java#L18
		//a.aB() - 1.6.1
		if (a.aC().nextFloat() < 0.8F) {
			this.a.getControllerJump().a();
		}
	}
}
