package io.github.dsh105.echopet.entity.living.type.silverfish;

import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Silverfish;

public class CraftSilverfishPet extends CraftLivingPet implements Silverfish {

    public CraftSilverfishPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }
}