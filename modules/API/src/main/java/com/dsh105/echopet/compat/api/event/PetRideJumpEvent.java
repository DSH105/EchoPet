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

package com.dsh105.echopet.compat.api.event;

import com.dsh105.echopet.compat.api.entity.IPet;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a {@link com.dsh105.echopet.api.pet.Pet} jumps when their owner is riding
 */

public class PetRideJumpEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private IPet pet;
    private double jumpHeight;

    public PetRideJumpEvent(IPet pet, final double jumpHeight) {
        this.pet = pet;
        this.jumpHeight = jumpHeight;
    }

    /**
     * Gets the {@link com.dsh105.echopet.api.pet.Pet} involved in this event
     *
     * @return the {@link com.dsh105.echopet.api.pet.Pet} involved
     */
    public IPet getPet() {
        return this.pet;
    }

    /**
     * Gets the height jumped by this {@link com.dsh105.echopet.api.pet.Pet}
     *
     * @return height jumped
     */
    public double getJumpHeight() {
        return this.jumpHeight;
    }

    /**
     * Sets the height this {@link com.dsh105.echopet.api.pet.Pet} jumped
     *
     * @param jumpHeight new jump height for this event
     */
    public void setJumpHeight(double jumpHeight) {
        this.jumpHeight = jumpHeight;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
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