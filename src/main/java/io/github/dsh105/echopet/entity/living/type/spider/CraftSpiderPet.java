package io.github.dsh105.echopet.entity.living.type.spider;

import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Spider;

public class CraftSpiderPet extends CraftLivingPet implements Spider {

    public CraftSpiderPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }
}