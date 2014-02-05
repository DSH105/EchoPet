package io.github.dsh105.echopet.entity.type.slime;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.Pet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Slime;

public class CraftSlimePet extends CraftPet implements Slime {

    public CraftSlimePet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }

    @Override
    public int getSize() {
        Pet p = this.getPet();
        if (p instanceof SlimePet) {
            return ((SlimePet) p).getSize();
        }
        return 0;
    }

    @Override
    public void setSize(int i) {
        /*Pet p = this.getPet();
        if (p instanceof SlimePet) {
            ((SlimePet) p).setSize(i);
        }*/
    }
}