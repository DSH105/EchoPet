package io.github.dsh105.echopet.api.event;

import io.github.dsh105.echopet.entity.Pet;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a {@link io.github.dsh105.echopet.entity.Pet} moves
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
     * Gets the {@link org.bukkit.Location} this {@link io.github.dsh105.echopet.entity.Pet} moved to
     *
     * @return {@link org.bukkit.Location} moved to
     */
    public Location getTo() {
        return this.to;
    }

    /**
     * Gets the {@link org.bukkit.Location} this {@link io.github.dsh105.echopet.entity.Pet} moved from
     *
     * @return {@link org.bukkit.Location} moved from
     */
    public Location getFrom() {
        return this.from;
    }

    /**
     * Sets the {@link org.bukkit.Location} that this {@link io.github.dsh105.echopet.entity.Pet} will move to
     *
     * @param to new {@link org.bukkit.Location} this {@link io.github.dsh105.echopet.entity.Pet} will move to
     */
    public void setTo(Location to) {
        this.to = to;
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
