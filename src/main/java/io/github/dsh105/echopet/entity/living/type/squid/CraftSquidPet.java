package io.github.dsh105.echopet.entity.living.type.squid;

import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Squid;

public class CraftSquidPet extends CraftLivingPet implements Squid {

    public CraftSquidPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }
}