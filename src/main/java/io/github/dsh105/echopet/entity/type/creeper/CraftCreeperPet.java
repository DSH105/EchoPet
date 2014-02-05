package io.github.dsh105.echopet.entity.type.creeper;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.Pet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Creeper;

public class CraftCreeperPet extends CraftPet implements Creeper {

    public CraftCreeperPet(CraftServer server, EntityPet entity) {
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
        /*Pet p = this.getPet();
        if (p instanceof CreeperPet) {
            ((CreeperPet) p).setPowered(b);
        }*/
    }
}