package io.github.dsh105.echopet.entity.living.type.mushroomcow;

import io.github.dsh105.echopet.entity.living.CraftAgeablePet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.MushroomCow;

public class CraftMushroomCowPet extends CraftAgeablePet implements MushroomCow {

    public CraftMushroomCowPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }
}