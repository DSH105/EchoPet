package com.github.dsh105.echopet.entity.pathfinder.goals;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.api.event.PetMoveEvent;
import com.github.dsh105.echopet.api.event.PetTeleportEvent;
import net.minecraft.server.v1_6_R2.EntityPlayer;
import net.minecraft.server.v1_6_R2.Navigation;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;

import com.github.dsh105.echopet.entity.pathfinder.PetGoal;
import com.github.dsh105.echopet.entity.pet.EntityPet;


public class PetGoalFollowOwner extends PetGoal {

	private EntityPet pet; //d
	private float speed = 0.4F; //f
	private Navigation nav; //g
	private int timer = 0; //h
	private double startDistance;
	private double stopDistance;
	private double teleportDistance;
	private EntityPlayer owner;
	
	public PetGoalFollowOwner(EntityPet pet, double startDistance, double stopDistance, double teleportDistance) {
		this.pet = pet;
		this.nav = pet.getNavigation();
		this.startDistance = startDistance;
		this.stopDistance = stopDistance;
		this.teleportDistance = teleportDistance;
		this.owner = ((CraftPlayer) pet.getOwner()).getHandle();
	}
	
	@Override
	public boolean shouldStart() {
		if (!this.pet.isAlive()) {
			return false;
		}
		else if (this.owner == null) {
			return false;
		}
		else if (this.pet.e(this.owner) < this.startDistance) {
			return false;
		}
		else {
			return true;
		}
		
	}
	
	@Override
	public boolean shouldFinish() {
		PetGoalAttack attackGoal = (PetGoalAttack) this.pet.petGoalSelector.getGoal(PetGoalAttack.class);
		if (attackGoal != null && attackGoal.isActive) {
			return true;
		}
		if (this.owner == null) {
			return true;
		}
		else if (this.pet.e(this.owner) <= this.stopDistance) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void start() {
		this.timer = 0;
		//this.nav.a(false);
	}
	
	@Override
	public void finish() {
		this.nav.g();
	}
	
	@Override
	public void tick() {
		//https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/PathfinderGoalFollowOwner.java#L57
		this.pet.getControllerLook().a(owner, 10.0F, (float) this.pet.bp()); //(bl() - 1.6.1) (bs() - 1.5.2)
		if (--this.timer <= 0) {
			this.timer = 10;
			if (this.pet.getOwner().isSprinting()) {
				this.speed = 0.5F;
			}
			else if (this.pet.getOwner().isSneaking()) {
				this.speed = 0.3F;
			}
			else {
				this.speed = 0.4F;
			}
			PetMoveEvent moveEvent = new PetMoveEvent(this.pet.getPet(), this.pet.getLocation(), this.pet.getOwner().getLocation());
			EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(moveEvent);
			if (moveEvent.isCancelled()) {
				return;
			}
			Location to = moveEvent.getTo();
			if ((!this.pet.getOwner().isFlying() && this.pet.e(this.owner) > this.teleportDistance) || (this.pet.getOwner().isFlying() && this.pet.e(this.owner) > 50)) {
				this.pet.getPet().teleport(this.pet.getOwner().getLocation());
			}
			else if (!this.nav.a(to.getX(), to.getY(), to.getZ(), speed)) {
				if (owner.onGround && pet.goalTarget == null) {
					this.pet.setPositionRotation(to.getX(), to.getY(), to.getZ(), this.pet.yaw, this.pet.pitch);
					this.nav.a(to.getX(), to.getY(), to.getZ(), speed);
				}
			}
		}
	}
}