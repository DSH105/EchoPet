package io.github.dsh105.echopet.entity.type.magmacube;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.Pet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.MagmaCube;

public class CraftMagmaCubePet extends CraftPet implements MagmaCube {

    public CraftMagmaCubePet(CraftServer server, EntityPet entity) {
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