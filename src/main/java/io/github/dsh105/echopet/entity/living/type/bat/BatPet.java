package io.github.dsh105.echopet.entity.living.type.bat;

import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.living.data.PetType;
import org.bukkit.entity.Player;


public class BatPet extends LivingPet {

    boolean hanging;

    public BatPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setHanging(boolean flag) {
        ((EntityBatPet) getEntityPet()).setHanging(flag);
        this.hanging = flag;
    }

    public boolean isHanging() {
        return this.hanging;
    }
}
