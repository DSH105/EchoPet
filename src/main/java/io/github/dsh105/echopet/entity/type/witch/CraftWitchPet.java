package io.github.dsh105.echopet.entity.type.witch;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Witch;

@EntityPetType(petType = PetType.WITCH)
public class CraftWitchPet extends CraftPet implements Witch {

    public CraftWitchPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}