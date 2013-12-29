package io.github.dsh105.echopet.entity.inanimate;

import io.github.dsh105.echopet.entity.CraftPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;

public abstract class CraftInanimatePet extends CraftPet {

    public CraftInanimatePet(CraftServer server, EntityInanimatePet entity) {
        super(server, entity);
        this.entityPet = entity;
    }

    @Override
    public EntityInanimatePet getHandle() {
        if (this.entityPet instanceof EntityInanimatePet) {
            return (EntityInanimatePet) this.entityPet;
        }
        return null;
    }

    @Override
    public InanimatePet getPet() {
        if (this.entityPet.getPet() instanceof InanimatePet) {
            return (InanimatePet) entityPet.getPet();
        }
        return null;
    }
}