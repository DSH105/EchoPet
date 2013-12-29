package io.github.dsh105.echopet.entity.living.type.cow;

import io.github.dsh105.echopet.entity.living.CraftAgeablePet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Cow;

public class CraftCowPet extends CraftAgeablePet implements Cow {

    public CraftCowPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }
}