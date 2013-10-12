package io.github.dsh105.echopet.api.event;

import io.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

public class PetTeleportEvent extends PetMoveEvent {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    public PetTeleportEvent(Pet pet, Location from, Location to) {
        super(pet, from, to);
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