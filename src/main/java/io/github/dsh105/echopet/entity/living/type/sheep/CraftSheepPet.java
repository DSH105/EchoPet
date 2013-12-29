package io.github.dsh105.echopet.entity.living.type.sheep;

import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.living.CraftAgeablePet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Sheep;

public class CraftSheepPet extends CraftAgeablePet implements Sheep {

    public CraftSheepPet(CraftServer server, EntityLivingPet entity) {
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
        Pet p = this.getPet();
        if (p instanceof SheepPet) {
            ((SheepPet) p).setSheared(b);
        }
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
        Pet p = this.getPet();
        if (p instanceof SheepPet) {
            ((SheepPet) p).setColor(dyeColor);
        }
    }
}