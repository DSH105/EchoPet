package io.github.dsh105.echopet.entity.living.type.cavespider;

import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.CaveSpider;

public class CraftCaveSpiderPet extends CraftLivingPet implements CaveSpider {

    public CraftCaveSpiderPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }
}