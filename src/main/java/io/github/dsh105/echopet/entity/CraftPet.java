package io.github.dsh105.echopet.entity;

import net.minecraft.server.v1_7_R1.EntityCreature;
import net.minecraft.server.v1_7_R1.EntityLiving;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftLivingEntity;
import org.bukkit.entity.*;

public abstract class CraftPet extends CraftCreature implements Vehicle, Tameable {

    protected EntityPet entityPet;

    public CraftPet(CraftServer server, EntityPet entity) {
        super(server, entity);
        this.entityPet = entity;
    }

    @Override
    public EntityPet getHandle() {
        return this.entityPet;
    }

    @Override
    public EntityType getType() {
        return this.entityPet.getPet().getPetType().getEntityType();
    }

    @Override
    public void remove() {
        super.remove();
    }

    public Pet getPet() {
        return this.entityPet.getPet();
    }

    @Override
    public CraftLivingEntity getTarget() {
        if (getHandle().target == null) return null;
        if (!(getHandle().target instanceof EntityLiving)) return null;
        return (CraftLivingEntity) getHandle().target.getBukkitEntity();
    }

    @Override
    public void setTarget(LivingEntity target) {
        EntityCreature entity = getHandle();
        if (target == null) {
            entity.target = null;
        } else if (target instanceof CraftLivingEntity) {
            entity.target = ((CraftLivingEntity) target).getHandle();
            entity.pathEntity = entity.world.findPath(entity, entity.target, 16.0F, true, false, false, true);
        }
    }

    @Override
    public void _INVALID_damage(int amount) {
        damage((double) amount);
    }

    @Override
    public void _INVALID_damage(int amount, Entity source) {
        damage((double) amount, source);
    }

    @Override
    public int _INVALID_getHealth() {
        return (int) getHealth();
    }

    @Override
    public int _INVALID_getLastDamage() {
        return (int) getLastDamage();
    }

    @Override
    public int _INVALID_getMaxHealth() {
        return (int) getMaxHealth();
    }

    @Override
    public void _INVALID_setHealth(int health) {
        setHealth((double) health);
    }

    @Override
    public void _INVALID_setLastDamage(int damage) {
        setLastDamage((double) damage);
    }

    @Override
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

    @Override
    public boolean isTamed() {
        return true;
    }

    @Override
    public void setTamed(boolean b) {
        // Not applicable. Pets are ALWAYS tamed
    }

    @Override
    public AnimalTamer getOwner() {
        return this.entityPet.getPlayerOwner();
    }

    @Override
    public void setOwner(AnimalTamer animalTamer) {
        // Not applicable. Pets can't change owners
    }
}