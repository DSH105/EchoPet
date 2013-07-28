package com.github.dsh105.echopet.entity.pathfinder.goals;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.entity.pathfinder.PetGoal;
import com.github.dsh105.echopet.entity.pet.EntityPet;
import net.minecraft.server.v1_6_R2.EntityLiving;
import net.minecraft.server.v1_6_R2.World;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;

public class PetGoalAttack extends PetGoal {

	EntityLiving target;
	//double d; //range
	EntityPet pet; //pet
	int ticksBetweenAttack;
	int ticksUntilAttack; //ticks until next hit
	double lockRange; //lock onto range
	int navUpdate; //navUpdate

	public boolean isActive;

	public PetGoalAttack(EntityPet pet, double lockRange, int ticksBetweenAttack) {
		this.pet = pet;
		this.ticksBetweenAttack = this.ticksUntilAttack = ticksBetweenAttack;
		this.lockRange = lockRange * lockRange;
	}

	@Override
	public boolean shouldStart() {
		EntityLiving entityLiving = this.pet.getGoalTarget();
		if (entityLiving == null) {
			return false;
		}
		else if (!entityLiving.isAlive()) {
			return false;
		}
		else if (this.pet.e(entityLiving) >= this.lockRange) {
			return false;
		}
		this.target = entityLiving;

		//https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/PathfinderGoalMeleeAttack.java#L61
		//canSee()
		return this.pet.getEntitySenses().canSee(entityLiving);
	}

	@Override
	public boolean shouldFinish() {
		EntityLiving entityliving = this.pet.getGoalTarget();

		if (entityliving == null) {
			return true;
		}
		else if (!entityliving.isAlive()) {
			return true;
		}
		else if (this.pet.e(((CraftPlayer) this.pet.getOwner()).getHandle()) >= this.pet.getSizeCategory().getTeleport()) {
			return true;
		}
		return false;
	}

	@Override
	public void start() {
		this.isActive = true;
		this.pet.getNavigation().a(this.target);
		this.navUpdate = 0;
	}

	@Override
	public void finish() {
		this.isActive = false;
		this.pet.getNavigation().h();
	}

	@Override
	public void tick() {
		this.pet.getControllerLook().a(this.target, 30.0F, 30.0F);
		if (this.pet.getEntitySenses().canSee(this.target) && --this.navUpdate <= 0) {
			this.navUpdate = 4 + this.pet.aC().nextInt(7);
			this.pet.getNavigation().a(this.target);
		}

		this.ticksUntilAttack = Math.max(this.ticksUntilAttack - 1, 0);
		double attackRange = (double) (this.pet.width * 2.0F * this.pet.width * 2.0F + this.target.width);

		if (this.pet.e(this.target.locX, this.target.boundingBox.b, this.target.locZ) <= attackRange) {
			if (this.ticksUntilAttack <= 0) {
				this.ticksUntilAttack = this.ticksBetweenAttack;

				// Arm animation
				if (this.pet.aY() != null) {
					this.pet.aU();
				}
				this.pet.attack(this.target);
			}
		}
	}
}