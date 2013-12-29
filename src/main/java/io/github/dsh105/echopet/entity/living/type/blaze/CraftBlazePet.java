package io.github.dsh105.echopet.entity.living.type.blaze;

import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Blaze;

public class CraftBlazePet extends CraftLivingPet implements Blaze {

    public CraftBlazePet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }
}