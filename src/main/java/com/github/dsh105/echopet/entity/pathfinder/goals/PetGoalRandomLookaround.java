package com.github.dsh105.echopet.entity.pathfinder.goals;

import com.github.dsh105.echopet.entity.pathfinder.PetGoal;
import com.github.dsh105.echopet.entity.pet.EntityPet;

public class PetGoalRandomLookaround extends PetGoal {
	private EntityPet a; // Pet entity
	private double b; // X direction
	private double c; // Z direction
	private int d = 0; // Ticks until stop looking
	
	public PetGoalRandomLookaround(EntityPet pet) {
		this.a = pet;
	}
	
	@Override
	public boolean a() {
		if (this.a.getGoalTarget() != null && this.a.getGoalTarget().isAlive()) {
			return false;
		}
		//https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/PathfinderGoalRandomLookaround.java#L16
		//a.aB() - 1.6.1
		return this.a.aC().nextFloat() < 0.2F;
	}
	
	@Override
	public boolean b() {
		return this.d <= 0;
	}
	
	@Override
	public void c() {
		double d0 = 6.283185307179586D * this.a.aC().nextDouble();
		this.b = Math.cos(d0);
		this.c = Math.sin(d0);
		//https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/PathfinderGoalRandomLookaround.java#L28
		//a.aB() - 1.6.1
		this.d = 20 + this.a.aC().nextInt(20);
	}
	
	@Override
	public void e() {
		--this.d;
		//https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/PathfinderGoalRandomLookaround.java#L33
		this.a.getControllerLook().a(this.a.locX + this.b, this.a.locY + this.a.getHeadHeight(), this.a.locZ + this.c, 10.0F, this.a.bp()); //(bl() - 1.6.1) (bs() - 1.5.2)
	}
}
