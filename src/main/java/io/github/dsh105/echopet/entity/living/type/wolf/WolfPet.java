package io.github.dsh105.echopet.entity.living.type.wolf;

import io.github.dsh105.echopet.entity.living.data.PetType;
import io.github.dsh105.echopet.entity.living.IAgeablePet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;


public class WolfPet extends LivingPet implements IAgeablePet {

    DyeColor collar = DyeColor.RED;
    boolean baby = false;
    boolean tamed = false;
    boolean angry = false;

    public WolfPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setCollarColor(DyeColor dc) {
        ((EntityWolfPet) getEntityPet()).setCollarColor(dc);
        this.collar = dc;
    }

    public DyeColor getCollarColor() {
        return this.collar;
    }

    public void setBaby(boolean flag) {
        ((EntityWolfPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public boolean isBaby() {
        return this.baby;
    }

    public void setTamed(boolean flag) {
        ((EntityWolfPet) getEntityPet()).setTamed(flag);
        this.tamed = flag;
    }

    public boolean isTamed() {
        return this.tamed;
    }

    public void setAngry(boolean flag) {
        ((EntityWolfPet) getEntityPet()).setAngry(flag);
        this.angry = flag;
    }

    public boolean isAngry() {
        return this.angry;
    }
}