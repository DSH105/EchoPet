package io.github.dsh105.echopet.entity.living.type.sheep;

import io.github.dsh105.echopet.entity.living.data.PetType;
import io.github.dsh105.echopet.entity.living.IAgeablePet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;


public class SheepPet extends LivingPet implements IAgeablePet {

    boolean baby;
    boolean sheared;
    byte color;

    public SheepPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setBaby(boolean flag) {
        ((EntitySheepPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public boolean isBaby() {
        return this.baby;
    }

    public void setSheared(boolean flag) {
        ((EntitySheepPet) getEntityPet()).setSheared(flag);
        this.sheared = flag;
    }

    public boolean isSheared() {
        return this.sheared;
    }

    public DyeColor getColor() {
        return org.bukkit.DyeColor.getByWoolData(color);
    }

    public byte getColorByte() {
        return color;
    }

    public void setColor(DyeColor c) {
        ((EntitySheepPet) getEntityPet()).setColor(c.getWoolData());
    }

    public void setColor(byte b) {
        ((EntitySheepPet) getEntityPet()).setColor(b);
    }

}
