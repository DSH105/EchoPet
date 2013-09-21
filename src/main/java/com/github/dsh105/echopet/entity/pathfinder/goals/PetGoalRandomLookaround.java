package com.github.dsh105.echopet.entity.pathfinder.goals;

import com.github.dsh105.echopet.entity.pathfinder.PetGoal;
import com.github.dsh105.echopet.entity.pet.EntityPet;

public class PetGoalRandomLookaround extends PetGoal {
	private EntityPet pet;
	private double xDir;
	private double zDir;
	private int ticksUntilStop = 0;
	
	public PetGoalRandomLookaround(EntityPet pet) {
		this.pet = pet;
	}
	
	@Override
	public boolean shouldStart() {
		if (this.pet.getGoalTarget() != null && this.pet.getGoalTarget().isAlive()) {
			return false;
		}
		return this.pet.aD().nextFloat() < 0.2F;
	}
	
	@Override
	public boolean shouldFinish() {
		return this.ticksUntilStop <= 0;
	}
	
	@Override
	public void start() {
		double d0 = 6.283185307179586D * this.pet.aD().nextDouble();
		this.xDir = Math.cos(d0);
		this.zDir = Math.sin(d0);
		this.ticksUntilStop = 20 + this.pet.aD().nextInt(20);
	}
	
	@Override
	public void tick() {
		--this.ticksUntilStop;
		this.pet.getControllerLook().a(this.pet.locX + this.xDir, this.pet.locY + this.pet.getHeadHeight(), this.pet.locZ + this.zDir, 10.0F, this.pet.bp());
	}
}
