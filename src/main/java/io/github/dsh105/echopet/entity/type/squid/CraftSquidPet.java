package io.github.dsh105.echopet.entity.type.squid;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Squid;

public class CraftSquidPet extends CraftPet implements Squid {

    public CraftSquidPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}