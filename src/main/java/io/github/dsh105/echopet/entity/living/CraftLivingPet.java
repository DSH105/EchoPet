package io.github.dsh105.echopet.entity.living;

import io.github.dsh105.echopet.entity.CraftPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Tameable;

public abstract class CraftLivingPet extends CraftPet implements Tameable {

    public CraftLivingPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }

    @Override
    public EntityLivingPet getHandle() {
        if (this.entityPet instanceof EntityLivingPet) {
            return (EntityLivingPet) this.entityPet;
        }
        return null;
    }

    @Override
    public LivingPet getPet() {
        if (this.entityPet.getPet() instanceof LivingPet) {
            return (LivingPet) entityPet.getPet();
        }
        return null;
    }

    // Bukkit API stuff ("Tameable" implementation)

    @Override
    public boolean isTamed() {
        return true;
    }

    @Override
    public void setTamed(boolean b) {
        // Not applicable. Pets are ALWAYS tamed
    }

    @Override
    public AnimalTamer getOwner() {
        return this.getHandle().getPlayerOwner();
    }

    @Override
    public void setOwner(AnimalTamer animalTamer) {
        // Not applicable. Pets can't change owners
    }
}
