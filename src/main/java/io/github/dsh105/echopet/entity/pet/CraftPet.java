package io.github.dsh105.echopet.entity.pet;

import net.minecraft.server.v1_6_R3.EntityCreature;
import net.minecraft.server.v1_6_R3.EntityLiving;
import org.bukkit.craftbukkit.v1_6_R3.CraftServer;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
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

	public void _INVALID_damage(int amount) {
		damage((double) amount);
	}

	public void _INVALID_damage(int amount, Entity source) {
		damage((double) amount, source);
	}

	public int _INVALID_getHealth() {
		return (int) getHealth();
	}

	public int _INVALID_getLastDamage() {
		return (int) getLastDamage();
	}

	public int _INVALID_getMaxHealth() {
		return (int) getMaxHealth();
	}

	public void _INVALID_setHealth(int health) {
		setHealth((double) health);
	}

	public void _INVALID_setLastDamage(int damage) {
		setLastDamage((double) damage);
	}

	public void _INVALID_setMaxHealth(int health) {
	}

	@Override
	public void setHealth(double health) {
		if (health < 0) {
			health = 0;
		}
		if (health > getMaxHealth()) {
			health = getMaxHealth();
		}
		super.setHealth(health);
	}

}
