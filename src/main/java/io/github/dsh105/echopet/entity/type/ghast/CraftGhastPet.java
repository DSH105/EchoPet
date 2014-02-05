package io.github.dsh105.echopet.entity.type.ghast;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Ghast;

public class CraftGhastPet extends CraftPet implements Ghast {

    public CraftGhastPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}