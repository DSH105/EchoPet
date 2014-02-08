package io.github.dsh105.echopet.entity.type.human;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPacketPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;

@EntityPetType(petType = PetType.HUMAN)
public class CraftHumanPet extends CraftPet {

    public CraftHumanPet(CraftServer server, EntityPacketPet entity) {
        super(server, entity);
    }
}