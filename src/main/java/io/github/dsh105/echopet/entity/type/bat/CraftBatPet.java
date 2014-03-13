package io.github.dsh105.echopet.entity.type.bat;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Bat;

@EntityPetType(petType = PetType.BAT)
public class CraftBatPet extends CraftPet implements Bat {

    public CraftBatPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}