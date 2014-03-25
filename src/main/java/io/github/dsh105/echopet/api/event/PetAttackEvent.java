package io.github.dsh105.echopet.api.event;

import io.github.dsh105.echopet.entity.Pet;
import net.minecraft.server.v1_7_R2.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a {@link io.github.dsh105.echopet.entity.Pet} attacks another {@link org.bukkit.entity.Entity}
 */

public class PetAttackEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private Pet pet;
    private Entity attacked;
    public DamageSource damageSource;
    private double damage;

    public PetAttackEvent(Pet pet, Entity attacked, DamageSource damageSource, final double damage) {
        this.pet = pet;
        this.attacked = attacked;
        this.damage = damage;
        this.damageSource = damageSource;
    }

    /**
     * Gets the damage dealt by the {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return damage dealt
     */
    public double getDamage() {
        return this.damage;
    }

    /**
     * Sets the damage to be applied to the attacked
     *
     * @param damage amount of health to take off the attacked
     */
    public void setDamage(double damage) {
        this.damage = damage;
    }

    /**
     * Gets the {@link org.bukkit.entity.Entity} attacked
     *
     * @return the {@link org.bukkit.entity.Entity} attacked
     */
    public Entity getAttacked() {
        return this.attacked;
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