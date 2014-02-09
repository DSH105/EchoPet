package io.github.dsh105.echopet.entity;

public class PacketPet extends Pet {

    public PacketPet(String owner) {
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