package io.github.dsh105.echopet.entity.living.type.ghast;

import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Ghast;

public class CraftGhastPet extends CraftLivingPet implements Ghast {

    public CraftGhastPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }
}