package io.github.dsh105.echopet.entity.living.type.chicken;

import io.github.dsh105.echopet.entity.living.CraftAgeablePet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Chicken;

public class CraftChickenPet extends CraftAgeablePet implements Chicken {

    public CraftChickenPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }
}