package io.github.dsh105.echopet.entity.pet.chicken;


import io.github.dsh105.echopet.data.PetType;
import io.github.dsh105.echopet.entity.pet.IAgeablePet;
import io.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.entity.Player;

public class ChickenPet extends Pet implements IAgeablePet {

    boolean baby;

    public ChickenPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setBaby(boolean flag) {
        ((EntityChickenPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public boolean isBaby() {
        return this.baby;
    }
}