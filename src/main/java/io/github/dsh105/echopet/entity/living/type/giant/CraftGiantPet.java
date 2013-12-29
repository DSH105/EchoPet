package io.github.dsh105.echopet.entity.living.type.giant;

import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Giant;

public class CraftGiantPet extends CraftLivingPet implements Giant {

    public CraftGiantPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }
}