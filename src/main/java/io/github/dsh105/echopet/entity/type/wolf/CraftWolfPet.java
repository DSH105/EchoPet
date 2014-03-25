package io.github.dsh105.echopet.entity.type.wolf;

import io.github.dsh105.echopet.entity.*;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.entity.Wolf;

@EntityPetType(petType = PetType.WOLF)
public class CraftWolfPet extends CraftAgeablePet implements Wolf {

    public CraftWolfPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }

    @Override
    public boolean isAngry() {
        Pet p = this.getPet();
        if (p instanceof WolfPet) {
            return ((WolfPet) p).isAngry();
        }
        return false;
    }

    @Override
    public void setAngry(boolean b) {
        /*Pet p = this.getPet();
        if (p instanceof WolfPet) {
            ((WolfPet) p).setAngry(b);
        }*/
    }

    @Override
    public boolean isSitting() {
        Pet p = this.getPet();
        if (p instanceof WolfPet) {
            return ((WolfPet) p).isAngry();
        }
        return false;
    }

    @Override
    public void setSitting(boolean b) {
        // Pets can't do this
    }

    @Override
    public DyeColor getCollarColor() {
        Pet p = this.getPet();
        if (p instanceof WolfPet) {
            return ((WolfPet) p).getCollarColor();
        }
        return null;
    }

    @Override
    public void setCollarColor(DyeColor dyeColor) {
        /*Pet p = this.getPet();
        if (p instanceof WolfPet) {
            ((WolfPet) p).setCollarColor(dyeColor);
        }*/
    }
}