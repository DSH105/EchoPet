package io.github.dsh105.echopet.entity.type.sheep;

import io.github.dsh105.echopet.entity.*;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Sheep;

@EntityPetType(petType = PetType.SHEEP)
public class CraftSheepPet extends CraftAgeablePet implements Sheep {

    public CraftSheepPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }

    @Override
    public boolean isSheared() {
        Pet p = this.getPet();
        if (p instanceof SheepPet) {
            return ((SheepPet) p).isSheared();
        }
        return false;
    }

    @Override
    public void setSheared(boolean b) {
        /*Pet p = this.getPet();
        if (p instanceof SheepPet) {
            ((SheepPet) p).setSheared(b);
        }*/
    }

    @Override
    public DyeColor getColor() {
        Pet p = this.getPet();
        if (p instanceof SheepPet) {
            return ((SheepPet) p).getColor();
        }
        return null;
    }

    @Override
    public void setColor(DyeColor dyeColor) {
        /*Pet p = this.getPet();
        if (p instanceof SheepPet) {
            ((SheepPet) p).setColor(dyeColor);
        }*/
    }
}