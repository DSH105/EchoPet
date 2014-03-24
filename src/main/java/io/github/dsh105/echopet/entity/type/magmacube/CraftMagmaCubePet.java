package io.github.dsh105.echopet.entity.type.magmacube;

import io.github.dsh105.echopet.entity.*;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.entity.MagmaCube;

@EntityPetType(petType = PetType.MAGMACUBE)
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