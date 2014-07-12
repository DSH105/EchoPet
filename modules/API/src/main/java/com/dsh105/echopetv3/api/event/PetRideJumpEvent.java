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
 * Called when a Pet jumps when their owner is riding
 */

public class PetRideJumpEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private Pet pet;
    private double jumpHeight;

    public PetRideJumpEvent(Pet pet, final double jumpHeight) {
        this.pet = pet;
        this.jumpHeight = jumpHeight;
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
     * Gets the height jumped by this Pet
     *
     * @return Height jumped
     */
    public double getJumpHeight() {
        return this.jumpHeight;
    }

    /**
     * Sets the height this Pet jumped
     *
     * @param jumpHeight New jump height for this event
     */
    public void setJumpHeight(double jumpHeight) {
        this.jumpHeight = jumpHeight;
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