package io.github.dsh105.echopet.entity.living.type.pig;


import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.living.IAgeablePet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.entity.Player;

public class PigPet extends LivingPet implements IAgeablePet {

    boolean baby;
    boolean saddle;

    public PigPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    @Override
    public void setBaby(boolean flag) {
        ((EntityPigPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    @Override
    public boolean isBaby() {
        return this.baby;
    }

    public void setSaddle(boolean flag) {
        ((EntityPigPet) getEntityPet()).setSaddle(flag);
        this.saddle = flag;
    }

    public boolean hasSaddle() {
        return this.saddle;
    }
}