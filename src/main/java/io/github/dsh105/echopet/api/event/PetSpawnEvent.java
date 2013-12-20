package io.github.dsh105.echopet.api.event;


import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PetSpawnEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private Pet pet;
    private Location spawnLocation;

    public PetSpawnEvent(Pet pet, Location spawnLocation) {
        this.pet = pet;
        this.spawnLocation = spawnLocation;
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
     * Gets the spawn {@link org.bukkit.Location} of the {@link io.github.dsh105.echopet.entity.Pet}
     * <p/>
     * This {@link org.bukkit.Location} is most likely going to be the owner's {@link org.bukkit.Location}
     *
     * @return the Location this LivingPet spawned
     */
    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    /**
     * Sets the spawn Location of this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @param spawnLocation new {@link org.bukkit.Location} to spawn the {@link io.github.dsh105.echopet.entity.Pet}
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

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
