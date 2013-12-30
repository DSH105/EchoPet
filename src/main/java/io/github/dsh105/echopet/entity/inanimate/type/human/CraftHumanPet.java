package io.github.dsh105.echopet.entity.inanimate.type.human;

import io.github.dsh105.echopet.entity.inanimate.CraftInanimatePet;
import io.github.dsh105.echopet.entity.inanimate.EntityInanimatePet;
import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;

public class CraftHumanPet extends CraftInanimatePet {

    public CraftHumanPet(CraftServer server, EntityInanimatePet entity) {
        super(server, entity);
    }
}