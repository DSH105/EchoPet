/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.dsh105.echopet.nms.v1_7_R1.entity;

import io.github.dsh105.echopet.api.entity.EntityPetType;
import io.github.dsh105.echopet.api.entity.PetType;
import io.github.dsh105.echopet.api.entity.nms.ICraftPet;
import io.github.dsh105.echopet.api.entity.pet.Pet;
import net.minecraft.server.v1_7_R1.EntityCreature;
import net.minecraft.server.v1_7_R1.EntityLiving;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftLivingEntity;
import org.bukkit.entity.*;

public abstract class CraftPet extends CraftCreature implements Vehicle, Tameable, ICraftPet {

    protected EntityPet entityPet;

    public CraftPet(EntityPet entity) {
        super(entity.world.getServer(), entity);
        this.entityPet = entity;
    }

    public PetType getPetType() {
        EntityPetType entityPetType = this.getClass().getAnnotation(EntityPetType.class);
        if (entityPetType != null) {
            return entityPetType.petType();
        }
        return null;
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