package com.github.dsh105.echopet.entity.pathfinder.goals;

import net.minecraft.server.v1_6_R2.Entity;
import net.minecraft.server.v1_6_R2.EntityHuman;

import com.github.dsh105.echopet.entity.pathfinder.PetGoal;
import com.github.dsh105.echopet.entity.pet.EntityPet;

@SuppressWarnings("rawtypes")
public class PetGoalLookAtPlayer extends PetGoal {
	
	private EntityPet a; //pet
	protected Entity b; //player
	private float c; //range
	private int d; //ticks left
	private float e; //chance
	private Class f;
	
	public PetGoalLookAtPlayer(EntityPet pet, Class c, float f) {
		this.a = pet;
		this.c = f;
		this.e = 0.2F;
		this.f = c;
	}
	
	public PetGoalLookAtPlayer(EntityPet pet, Class c, float f, float f1) {
		this.a = pet;
		this.c = f;
		this.e = f1;
		this.f = c;
	}
	
	@Override
	public boolean a() {
		//https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/PathfinderGoalLookAtPlayer.java#L29
		//a.aB() - 1.6.1
		if (this.a.aC().nextFloat() >= this.e) {
			return false;
		}
		else if (this.a.passenger != null) {
			return false;
		}
		else {
			if (this.f == EntityHuman.class) {
				this.b = this.a.world.findNearbyPlayer(this.a, (double) this.c);
			}
			else {
				this.b = this.a.world.a(this.f, this.b.boundingBox.grow((double) this.c, 3.0D, (double) this.c), this.b);
			}
			return this.b != null;
		}
	}
	
	@Override
	public boolean b() {
		return !this.b.isAlive() ? false : (this.a.e(this.b) > (double) (this.c * this.c) ? false : this.d > 0);
	}
	
	public void c() {
		this.d = 40 + this.a.aC().nextInt(40);
	}

	public void d() {
		this.b = null;
	}

	public void e() {
		//https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/PathfinderGoalLookAtPlayer.java#L59
		this.a.getControllerLook().a(this.b.locX, this.b.locY + (double) this.b.getHeadHeight(), this.b.locZ, 10.0F, (float) this.a.bp()); //(bl() - 1.6.1) (bs() - 1.5.2)
		--this.d;
	}
}
