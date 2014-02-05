package io.github.dsh105.echopet.entity.type.spider;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Spider;

public class CraftSpiderPet extends CraftPet implements Spider {

    public CraftSpiderPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}