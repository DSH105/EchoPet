package io.github.dsh105.echopet.entity.type.blaze;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Blaze;

@EntityPetType(petType = PetType.BLAZE)
public class CraftBlazePet extends CraftPet implements Blaze {

    public CraftBlazePet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}