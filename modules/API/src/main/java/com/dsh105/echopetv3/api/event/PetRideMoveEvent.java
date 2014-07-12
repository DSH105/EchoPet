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

package com.dsh105.echopetv3.api.event;

import com.dsh105.echopetv3.api.entity.pet.Pet;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a Pet moves when their owner is riding
 */

public class PetRideMoveEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private Pet pet;
    private float forwardSpeed;
    private float sidewardSpeed;

    public PetRideMoveEvent(Pet pet, float forwardSpeed, float sidewardSpeed) {
        this.pet = pet;
        this.forwardSpeed = forwardSpeed;
        this.sidewardSpeed = sidewardSpeed;
    }

    /**
     * Gets the Pet involved in this event
     *
     * @return Pet involved
     */
    public Pet getPet() {
        return this.pet;
    }

    /**
     * Gets the forward motion of the Pet
     *
     * @return Forward motion
     */
    public float getForwardMotionSpeed() {
        return this.forwardSpeed;
    }

    /**
     * Gets the sideward motion of the Pet
     *
     * @return Sideward motion
     */
    public float getSidewardMotionSpeed() {
        return this.sidewardSpeed;
    }

    /**
     * Sets the forward motion to be applied to the Pet
     *
     * @param forwardMotionSpeed New forward motion
     */
    public void setForwardMotionSpeed(float forwardMotionSpeed) {
        this.forwardSpeed = forwardMotionSpeed;
    }

    /**
     * Sets the sideward motion to be applied to the Pet
     *
     * @param sidewardMotionSpeed New sideward motion
     */
    public void setSidewardMotionSpeed(float sidewardMotionSpeed) {
        this.sidewardSpeed = sidewardMotionSpeed;
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