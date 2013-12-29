package io.github.dsh105.echopet.entity.living.type.irongolem;

import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.IronGolem;

public class CraftIronGolemPet extends CraftLivingPet implements IronGolem {

    public CraftIronGolemPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }

    @Override
    public boolean isPlayerCreated() {
        return false;
    }

    @Override
    public void setPlayerCreated(boolean b) {
        // Doesn't apply to Pets
    }
}