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

package io.github.dsh105.echopet.nms.v1_7_R2.entity;

import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.entity.Ageable;

/**
 * Bukkit API stuff. Methods here shouldn't be accessed and controlled outside the EchoPet API
 */

public abstract class CraftAgeablePet extends CraftPet implements Ageable {

    public CraftAgeablePet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }

    @Override
    public EntityAgeablePet getHandle() {
        if (this.entityPet instanceof EntityAgeablePet) {
            return (EntityAgeablePet) this.entityPet;
        }
        return null;
    }

    @Override
    public int getAge() {
        return this.getHandle().getAge();
    }

    @Override
    public void setAge(int i) {
        // Nuh-uh. Not allowed
        // Pet age should not be controlled like this
    }

    @Override
    public void setAgeLock(boolean b) {
        // Nuh-uh. Not allowed
        //this.getHandle().setAgeLocked(b);
    }

    @Override
    public boolean getAgeLock() {
        return this.getHandle().isAgeLocked();
    }

    @Override
    public void setBaby() {
        // Nuh-uh. Not allowed
        //this.getHandle().setBaby(true);
    }

    @Override
    public void setAdult() {
        // Nuh-uh. Not allowed
        //this.getHandle().setBaby(false);
    }

    @Override
    public boolean isAdult() {
        return !this.getHandle().isBaby();
    }

    @Override
    public boolean canBreed() {
        return false;
    }

    @Override
    public void setBreed(boolean b) {
        // Not applicable to Pets
    }
}