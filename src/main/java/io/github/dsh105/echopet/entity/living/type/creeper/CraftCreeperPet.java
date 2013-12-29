package io.github.dsh105.echopet.entity.living.type.creeper;

import io.github.dsh105.dshutils.logger.ConsoleLogger;
import io.github.dsh105.dshutils.logger.Logger;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Creeper;

public class CraftCreeperPet extends CraftLivingPet implements Creeper {

    public CraftCreeperPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }

    @Override
    public boolean isPowered() {
        Pet p = this.getPet();
        if (p instanceof CreeperPet) {
            return ((CreeperPet) p).isPowered();
        }
        return false;
    }

    @Override
    public void setPowered(boolean b) {
        Pet p = this.getPet();
        if (p instanceof CreeperPet) {
            ((CreeperPet) p).setPowered(b);
        }
    }
}