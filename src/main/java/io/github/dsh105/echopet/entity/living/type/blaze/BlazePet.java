package io.github.dsh105.echopet.entity.living.type.blaze;

import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.entity.Player;


public class BlazePet extends LivingPet {

    boolean onFire;

    public BlazePet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setOnFire(boolean flag) {
        ((EntityBlazePet) getEntityPet()).setOnFire(flag);
        this.onFire = flag;
    }

    public boolean isOnFire() {
        return this.onFire;
    }
}
