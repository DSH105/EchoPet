package io.github.dsh105.echopet.entity.living.type.snowman;

import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Snowman;

public class CraftSnowmanPet extends CraftLivingPet implements Snowman {

    public CraftSnowmanPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }
}