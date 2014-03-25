package io.github.dsh105.echopet.entity.type.human;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;

@EntityPetType(petType = PetType.HUMAN)
public class CraftHumanPet extends CraftPet {

    public CraftHumanPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}
