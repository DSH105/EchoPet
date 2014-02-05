package io.github.dsh105.echopet.entity;

public class PacketPet extends Pet {

    public PacketPet(String owner) {
        super(owner);
    }

    @Override
    public EntityPacketPet getEntityPet() {
        return (EntityPacketPet) super.getEntityPet();
    }
}