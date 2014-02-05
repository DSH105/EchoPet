package io.github.dsh105.echopet.entity.type.cow;

import io.github.dsh105.echopet.entity.CraftAgeablePet;
import io.github.dsh105.echopet.entity.EntityPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Cow;

public class CraftCowPet extends CraftAgeablePet implements Cow {

    public CraftCowPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}