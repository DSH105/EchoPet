package io.github.dsh105.echopet.entity;

import org.bukkit.entity.Player;

public class PacketPet extends Pet {

    public PacketPet(Player owner) {
        super(owner);
    }

    public PacketPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    @Override
    public EntityPacketPet getEntityPet() {
        return (EntityPacketPet) super.getEntityPet();
    }
}