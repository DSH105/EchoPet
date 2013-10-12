package io.github.dsh105.echopet.entity.pet.creeper;

import io.github.dsh105.echopet.data.PetType;
import io.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.entity.Player;


public class CreeperPet extends Pet {

    boolean powered;

    public CreeperPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setPowered(boolean flag) {
        ((EntityCreeperPet) getEntityPet()).setPowered(flag);
        this.powered = flag;
    }

    public boolean isPowered() {
        return this.powered;
    }
}
