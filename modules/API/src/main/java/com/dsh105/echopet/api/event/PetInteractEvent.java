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

package com.dsh105.echopet.api.event;

import com.dsh105.commodus.IdentUtil;
import com.dsh105.echopet.api.entity.pet.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a Player interacts with a Pet
 */

public class PetInteractEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private Pet pet;
    private Player player;
    private Action action;

    public PetInteractEvent(Pet pet, Player player, Action action, boolean cancelledByDefault) {
        this.pet = pet;
        this.action = action;
        this.player = player;
        this.cancelled = cancelledByDefault;
    }

    /**
     * Gets the Pet involved in this event
     *
     * @return The Pet involved
     */
    public Pet getPet() {
        return this.pet;
    }

    /**
     * Gets the player that interacted with the Pet
     *
     * @return The player that interacted with the Pet
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets the action executed by the Player
     *
     * @return The action executed
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * Returns whether the Player that interacted was the Pet's owner
     *
     * @return True if it is the owner
     */
    public boolean isPlayerOwner() {
        return IdentUtil.isIdentical(this.player, this.pet.getOwner());
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

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

    public enum Action {
        /**
         * Represents a left click
         */
        LEFT_CLICK,

        /**
         * Represents a right click
         */
        RIGHT_CLICK
    }
}
