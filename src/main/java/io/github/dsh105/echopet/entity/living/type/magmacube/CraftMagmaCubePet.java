package io.github.dsh105.echopet.entity.living.type.magmacube;

import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.MagmaCube;

public class CraftMagmaCubePet extends CraftLivingPet implements MagmaCube {

    public CraftMagmaCubePet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }

    @Override
    public int getSize() {
        Pet p = this.getPet();
        if (p instanceof MagmaCubePet) {
            return ((MagmaCubePet) p).getSize();
        }
        return 0;
    }

    @Override
    public void setSize(int i) {
        /*Pet p = this.getPet();
        if (p instanceof MagmaCubePet) {
            ((MagmaCubePet) p).setSize(i);
        }*/
    }
}