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
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a Pet moves
 */

public class PetMoveEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private Pet pet;
    private Location from;
    private Location to;

    public PetMoveEvent(Pet pet, Location from, Location to) {
        this.pet = pet;
        this.from = from;
        this.to = to;
    }

    /**
     * Gets the Location this Pet moved to
     *
     * @return Location moved to
     */
    public Location getTo() {
        return this.to;
    }

    /**
     * Gets the Location this Pet moved from
     *
     * @return Location moved from
     */
    public Location getFrom() {
        return this.from;
    }

    /**
     * Sets the Location that this Pet will move to
     *
     * @param to New Location this Pet will move to
     */
    public void setTo(Location to) {
        this.to = to;
    }

    /**
     * Gets the Pet involved in this event
     *
     * @return Pet involved
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
