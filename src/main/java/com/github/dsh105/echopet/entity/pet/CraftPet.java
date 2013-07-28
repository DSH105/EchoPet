package com.github.dsh105.echopet.entity.pet;

import net.minecraft.server.v1_6_R2.EntityCreature;
import net.minecraft.server.v1_6_R2.EntityLiving;
import org.bukkit.craftbukkit.v1_6_R2.CraftServer;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class CraftPet extends CraftCreature {

	private EntityPet entityPet;

	public CraftPet(CraftServer server, EntityPet entity) {
		super(server, entity);
		this.entityPet = entity;
	}

	public void remove() {
		super.remove();
	}

	public Pet getPet() {
		return entityPet.getPet();
	}

	@Override
	public EntityPet getHandle() {
		return entityPet;
	}

	@Override
	public EntityType getType() {
		return getPet().getPetType().getEntityType();
	}

	@Override
	public void damage(double amount) {
		super.damage(amount);
	}

	@Override
	public void damage(double amount, Entity entity) {
		super.damage(amount);
	}

	@Override
	public void setHealth(double health) {
		super.setHealth(health);
	}

	@Override
	public void setMaxHealth(double amount) {
		super.setMaxHealth(amount);
	}

	public void _INVALID_damage(int amount) {
		damage((double) amount);
	}
	
	public void _INVALID_damage(int amount, Entity source) {
		damage((double) amount, source);
	}
	
	public int _INVALID_getHealth() {
		return (int) getHealth();
	}
	
	public void _INVALID_setHealth(int health) {
		setHealth((double) health);
	}

	public int _INVALID_getMaxHealth() {
		return (int) getMaxHealth();
	}
	
	public void _INVALID_setMaxHealth(int health) {
		setMaxHealth((double) health);
	}

	public int _INVALID_getLastDamage() {
		return (int) getLastDamage();
	}

	public void _INVALID_setLastDamage(int damage) {
		setLastDamage((double) damage);
	}

	public CraftLivingEntity getTarget() {
		if (getHandle().target == null) return null;
		if (!(getHandle().target instanceof EntityLiving)) return null;
		return (CraftLivingEntity) getHandle().target.getBukkitEntity();
	}

	public void setTarget(LivingEntity target) {
		EntityCreature entity = getHandle();
		if (target == null) {
			entity.target = null;
		}
		else if (target instanceof CraftLivingEntity) {
			entity.target = ((CraftLivingEntity) target).getHandle();
			entity.pathEntity = entity.world.findPath(entity, entity.target, 16.0F, true, false, false, true);
		}
	}
}
