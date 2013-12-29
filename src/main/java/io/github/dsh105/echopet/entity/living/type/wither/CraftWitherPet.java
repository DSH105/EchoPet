package io.github.dsh105.echopet.entity.living.type.wither;

import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Wither;

public class CraftWitherPet extends CraftLivingPet implements Wither {

    public CraftWitherPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }
}