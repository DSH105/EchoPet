package io.github.dsh105.echopet.entity.living.type.witch;

import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Witch;

public class CraftWitchPet extends CraftLivingPet implements Witch {

    public CraftWitchPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }
}