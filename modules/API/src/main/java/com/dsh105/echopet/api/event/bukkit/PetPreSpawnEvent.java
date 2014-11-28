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
 * Called when a Pet spawns
 */

public class PetPreSpawnEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private Pet pet;
    private Location spawnLocation;

    public PetPreSpawnEvent(Pet pet, Location spawnLocation) {
        this.pet = pet;
        this.spawnLocation = spawnLocation;
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
     * Gets the spawn Location of the Pet
     * <p/>
     * This Location is most likely going to be the owner's Location
     *
     * @return the Location this LivingPet spawned
     */
    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    /**
     * Sets the spawn Location of this Pet
     *
     * @param spawnLocation new Location to spawn the Pet
     */
    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
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
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
