package io.github.dsh105.echopet.api.event;

import io.github.dsh105.echopet.entity.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a {@link Player} interacts with a {@link io.github.dsh105.echopet.entity.Pet}
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
     * Gets the {@link io.github.dsh105.echopet.entity.Pet} involved in this event
     *
     * @return the {@link io.github.dsh105.echopet.entity.Pet} involved
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
     * @return the {@link io.github.dsh105.echopet.api.event.PetInteractEvent.Action} of the event
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
        return this.player == this.pet.getOwner();
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
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
        RIGHT_CLICK;
    }
}
