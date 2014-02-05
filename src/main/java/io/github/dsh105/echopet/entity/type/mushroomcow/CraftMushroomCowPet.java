package io.github.dsh105.echopet.entity.type.mushroomcow;

import io.github.dsh105.echopet.entity.CraftAgeablePet;
import io.github.dsh105.echopet.entity.EntityPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.MushroomCow;

public class CraftMushroomCowPet extends CraftAgeablePet implements MushroomCow {

    public CraftMushroomCowPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}