package io.github.dsh105.echopet.entity.living.type.bat;

import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Bat;

public class CraftBatPet extends CraftLivingPet implements Bat {

    public CraftBatPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }
}