package io.github.dsh105.echopet.api.event;

import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a {@link io.github.dsh105.echopet.entity.Pet} jumps when their owner is riding
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
     * Gets the {@link io.github.dsh105.echopet.entity.Pet} involved in this event
     *
     * @return the {@link io.github.dsh105.echopet.entity.Pet} involved
     */
    public Pet getPet() {
        return this.pet;
    }

    /**
     * Gets the height jumped by this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return height jumped
     */
    public double getJumpHeight() {
        return this.jumpHeight;
    }

    /**
     * Sets the height this {@link io.github.dsh105.echopet.entity.Pet} jumped
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