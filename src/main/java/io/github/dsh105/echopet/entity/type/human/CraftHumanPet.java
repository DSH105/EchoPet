package io.github.dsh105.echopet.entity.type.human;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPacketPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;

public class CraftHumanPet extends CraftPet {

    public CraftHumanPet(CraftServer server, EntityPacketPet entity) {
        super(server, entity);
    }
}