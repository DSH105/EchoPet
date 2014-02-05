package io.github.dsh105.echopet.entity.type.cavespider;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.CaveSpider;

public class CraftCaveSpiderPet extends CraftPet implements CaveSpider {

    public CraftCaveSpiderPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}