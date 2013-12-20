package io.github.dsh105.echopet.entity.living.type.chicken;


import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.living.IAgeablePet;
import org.bukkit.entity.Player;

public class ChickenPet extends LivingPet implements IAgeablePet {

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