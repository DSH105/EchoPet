package io.github.dsh105.echopet.entity.living.type.enderdragon;

import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.EnderDragon;

import java.util.Set;

public class CraftEnderDragonPet extends CraftLivingPet implements EnderDragon {

    public CraftEnderDragonPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }

    @Override
    public Set<ComplexEntityPart> getParts() {
        return null;
    }
}