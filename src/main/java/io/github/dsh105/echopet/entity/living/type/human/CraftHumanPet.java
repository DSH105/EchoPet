package io.github.dsh105.echopet.entity.living.type.human;

import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;

public class CraftHumanPet extends CraftLivingPet {

    public CraftHumanPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }
}