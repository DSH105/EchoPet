package io.github.dsh105.echopet.entity.living.type.horse;

import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.living.IAgeablePet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.entity.Player;


public class HorsePet extends LivingPet implements IAgeablePet {

    HorseType horseType;
    HorseVariant variant;
    HorseMarking marking;
    HorseArmour armour;
    boolean baby = false;
    boolean chested = false;
    boolean saddle = false;

    public HorsePet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setHorseType(HorseType type) {
        ((EntityHorsePet) getEntityPet()).setType(type);
        this.horseType = type;
    }

    public void setVariant(HorseVariant variant, HorseMarking marking) {
        ((EntityHorsePet) getEntityPet()).setVariant(variant, marking);
        this.variant = variant;
        this.marking = marking;
    }

    public void setArmour(HorseArmour armour) {
        ((EntityHorsePet) getEntityPet()).setArmour(armour);
        this.armour = armour;
    }

    @Override
    public void setBaby(boolean flag) {
        ((EntityHorsePet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public void setSaddled(boolean flag) {
        ((EntityHorsePet) getEntityPet()).setSaddled(flag);
        this.saddle = flag;
    }

    public void setChested(boolean flag) {
        ((EntityHorsePet) getEntityPet()).setChested(flag);
        this.chested = flag;
    }

    public HorseType getHorseType() {
        return this.horseType;
    }

    public HorseVariant getVariant() {
        return this.variant;
    }

    public HorseMarking getMarking() {
        return this.marking;
    }

    public HorseArmour getArmour() {
        return this.armour;
    }

    @Override
    public boolean isBaby() {
        return this.baby;
    }

    public boolean isSaddled() {
        return this.saddle;
    }

    public boolean isChested() {
        return this.chested;
    }
}