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

package com.dsh105.echopet.api.event.bukkit;

import com.dsh105.echopet.api.entity.pet.Pet;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a Pet attacks another Entity
 */

public class PetAttackEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private Pet pet;
    private Entity attacked;
    private double damage;

    public PetAttackEvent(Pet pet, Entity attacked, final double damage) {
        this.pet = pet;
        this.attacked = attacked;
        this.damage = damage;
    }

    /**
     * Gets the damage dealt
     *
     * @return Damage dealt
     */
    public double getDamage() {
        return this.damage;
    }

    /**
     * Sets the damage to be applied to the attacked
     *
     * @param damage Amount of health to take off the attacked
     */
    public void setDamage(double damage) {
        this.damage = damage;
    }

    /**
     * Gets the Entity attacked
     *
     * @return The Entity attacked
     */
    public Entity getAttacked() {
        return this.attacked;
    }

    /**
     * Gets the Pet involved in this event
     *
     * @return The Pet involved
     */
    public Pet getPet() {
        return this.pet;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return this.handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}