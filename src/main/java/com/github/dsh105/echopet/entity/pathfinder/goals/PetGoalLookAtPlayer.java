package com.github.dsh105.echopet.entity.pathfinder.goals;

import net.minecraft.server.v1_6_R2.Entity;
import net.minecraft.server.v1_6_R2.EntityHuman;

import com.github.dsh105.echopet.entity.pathfinder.PetGoal;
import com.github.dsh105.echopet.entity.pet.EntityPet;

@SuppressWarnings("rawtypes")
public class PetGoalLookAtPlayer extends PetGoal {
	
	private EntityPet pet;
	protected Entity player;
	private float range;
	private int ticksLeft;
	private float chance;
	private Class clazz;
	
	public PetGoalLookAtPlayer(EntityPet pet, Class c, float f) {
		this.pet = pet;
		this.range = f;
		this.chance = 0.2F;
		this.clazz = c;
	}
	
	public PetGoalLookAtPlayer(EntityPet pet, Class c, float f, float f1) {
		this.pet = pet;
		this.range = f;
		this.chance = f1;
		this.clazz = c;
	}
	
	@Override
	public boolean shouldStart() {
		//https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/PathfinderGoalLookAtPlayer.java#L29
		//a.aB() - 1.6.1
		if (this.pet.aC().nextFloat() >= this.chance) {
			return false;
		}
		else if (this.pet.passenger != null) {
			return false;
		}
		else {
			if (this.clazz == EntityHuman.class) {
				this.player = this.pet.world.findNearbyPlayer(this.pet, (double) this.range);
			}
			else {
				this.player = this.pet.world.a(this.clazz, this.player.boundingBox.grow((double) this.range, 3.0D, (double) this.range), this.player);
			}
			return this.player != null;
		}
	}
	
	@Override
	public boolean shouldFinish() {
		return !this.player.isAlive() ? false : (this.pet.e(this.player) > (double) (this.range * this.range) ? false : this.ticksLeft > 0);
	}
	
	public void c() {
		this.ticksLeft = 40 + this.pet.aC().nextInt(40);
	}

	public void d() {
		this.player = null;
	}

	public void e() {
		//https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/PathfinderGoalLookAtPlayer.java#L59
		this.pet.getControllerLook().a(this.player.locX, this.player.locY + (double) this.player.getHeadHeight(), this.player.locZ, 10.0F, (float) this.pet.bp()); //(bl() - 1.6.1) (bs() - 1.5.2)
		--this.ticksLeft;
	}
}
