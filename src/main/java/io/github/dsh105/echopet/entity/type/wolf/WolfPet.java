package io.github.dsh105.echopet.entity.type.wolf;

import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.IAgeablePet;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.DyeColor;

@EntityPetType(petType = PetType.WOLF)
public class WolfPet extends Pet implements IAgeablePet {

    DyeColor collar = DyeColor.RED;
    boolean baby = false;
    boolean tamed = false;
    boolean angry = false;

    public WolfPet(String owner) {
        super(owner);
    }

    public void setCollarColor(DyeColor dc) {
        ((EntityWolfPet) getEntityPet()).setCollarColor(dc);
        this.collar = dc;
    }

    public DyeColor getCollarColor() {
        return this.collar;
    }

    @Override
    public void setBaby(boolean flag) {
        ((EntityWolfPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    @Override
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