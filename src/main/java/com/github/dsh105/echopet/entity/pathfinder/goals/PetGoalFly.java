package com.github.dsh105.echopet.entity.pathfinder.goals;

import com.github.dsh105.echopet.entity.pathfinder.PetGoal;
import com.github.dsh105.echopet.entity.pet.EntityPet;

public class PetGoalFly extends PetGoal {

	private EntityPet pet;
	
	public PetGoalFly(EntityPet pet) {
		this.pet = pet;
	}
	
	@Override
	public boolean a() {
		if (!this.pet.isAlive()) {
			return false;
		}
		else if (this.pet.getOwner() == null) {
			return false;
		}
		else if (!this.pet.getOwner().isFlying()) {
			return false;
		}
		else {
			return true;
		}
	}
	
	@Override
	public boolean b() {
		if (this.pet.getOwner() == null) {
			return true;
		}
		else if (!this.pet.getOwner().isFlying()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void e() {
		this.pet.teleport(this.pet.getOwner().getLocation());
	}
}