package io.github.dsh105.echopet.entity.type.slime;

import io.github.dsh105.echopet.entity.*;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Slime;

@EntityPetType(petType = PetType.SLIME)
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