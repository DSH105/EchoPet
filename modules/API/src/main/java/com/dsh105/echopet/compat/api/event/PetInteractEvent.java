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

import com.dsh105.commodus.PlayerIdent;
import com.dsh105.echopet.compat.api.entity.pet.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a {@link Player} interacts with a {@link com.dsh105.echopet.api.pet.Pet}
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
     * Gets the {@link com.dsh105.echopet.api.pet.Pet} involved in this event
     *
     * @return the {@link com.dsh105.echopet.api.pet.Pet} involved
     */
    public Pet getPet() {
        return this.pet;
    }

    /**
     * Gets the player that interacted with the LivingPet
     *
     * @return
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets the action executed by the {@link org.bukkit.entity.Player}
     *
     * @return the {@link com.dsh105.echopet.compat.api.event.PetInteractEvent.Action} of the event
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * Returns whether the {@link org.bukkit.entity.Player} that interacted was the Pet's owner
     *
     * @return true if it is the owner
     */
    public boolean isPlayerOwner() {
        return PlayerIdent.getIdentificationFor(this.player).equals(PlayerIdent.getIdentificationFor(this.pet.getOwner()));
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

    public enum Action {
        /**
         * Left clicking a LivingPet
         */
        LEFT_CLICK,

        /**
         * Right clicking a LivingPet
         */
        RIGHT_CLICK
    }
}
