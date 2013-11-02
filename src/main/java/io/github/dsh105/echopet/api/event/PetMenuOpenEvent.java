package io.github.dsh105.echopet.api.event;

import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PetMenuOpenEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private LivingPet pet;
    private MenuType menuType;

    public PetMenuOpenEvent(LivingPet pet, MenuType menuType) {
        this.pet = pet;
        this.menuType = menuType;
    }

    /**
     * Gets the {@link io.github.dsh105.echopet.entity.living.LivingPet} involved in this event
     *
     * @return the {@link io.github.dsh105.echopet.entity.living.LivingPet} involved
     */
    public LivingPet getPet() {
        return this.pet;
    }

    /**
     * Returns the type of Menu opened
     *
     * @return {@link MenuType} representing the menu opened
     */
    public MenuType getMenuType() {
        return this.menuType;
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

    public enum MenuType {
        MAIN,
        DATA;
    }
}