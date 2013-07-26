package com.github.dsh105.echopet.entity.pathfinder.goals;

import com.github.dsh105.echopet.entity.pathfinder.PetGoal;
import com.github.dsh105.echopet.entity.pet.EntityPet;

public class PetGoalRandomLookaround extends PetGoal {
	private EntityPet pet; // Pet entity
	private double xDir; // X direction
	private double zDir; // Z direction
	private int ticksUntilStop = 0; // Ticks until stop looking
	
	public PetGoalRandomLookaround(EntityPet pet) {
		this.pet = pet;
	}
	
	@Override
	public boolean shouldStart() {
		if (this.pet.getGoalTarget() != null && this.pet.getGoalTarget().isAlive()) {
			return false;
		}
		//https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/PathfinderGoalRandomLookaround.java#L16
		//a.aB() - 1.6.1
		return this.pet.aC().nextFloat() < 0.2F;
	}
	
	@Override
	public boolean shouldFinish() {
		return this.ticksUntilStop <= 0;
	}
	
	@Override
	public void start() {
		double d0 = 6.283185307179586D * this.pet.aC().nextDouble();
		this.xDir = Math.cos(d0);
		this.zDir = Math.sin(d0);
		//https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/PathfinderGoalRandomLookaround.java#L28
		//a.aB() - 1.6.1
		this.ticksUntilStop = 20 + this.pet.aC().nextInt(20);
	}
	
	@Override
	public void tick() {
		--this.ticksUntilStop;
		//https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/PathfinderGoalRandomLookaround.java#L33
		this.pet.getControllerLook().a(this.pet.locX + this.xDir, this.pet.locY + this.pet.getHeadHeight(), this.pet.locZ + this.zDir, 10.0F, this.pet.bp()); //(bl() - 1.6.1) (bs() - 1.5.2)
	}
}
