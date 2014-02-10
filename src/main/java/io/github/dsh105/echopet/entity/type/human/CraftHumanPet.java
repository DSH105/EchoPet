package io.github.dsh105.echopet.entity.type.human;

import io.github.dsh105.echopet.entity.*;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;

@EntityPetType(petType = PetType.HUMAN)
public class CraftHumanPet extends CraftPet {

    public CraftHumanPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}