package io.github.dsh105.echopet.entity.type.witch;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Witch;

public class CraftWitchPet extends CraftPet implements Witch {

    public CraftWitchPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}