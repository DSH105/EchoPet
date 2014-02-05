package io.github.dsh105.echopet.entity.type.blaze;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Blaze;

public class CraftBlazePet extends CraftPet implements Blaze {

    public CraftBlazePet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}