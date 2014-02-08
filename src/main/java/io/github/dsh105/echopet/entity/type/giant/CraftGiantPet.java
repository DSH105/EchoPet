package io.github.dsh105.echopet.entity.type.giant;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Giant;

@EntityPetType(petType = PetType.GIANT)
public class CraftGiantPet extends CraftPet implements Giant {

    public CraftGiantPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}