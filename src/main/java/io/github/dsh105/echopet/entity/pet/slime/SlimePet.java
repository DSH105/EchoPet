package io.github.dsh105.echopet.entity.pet.slime;

import io.github.dsh105.echopet.data.PetType;
import io.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.entity.Player;


public class SlimePet extends Pet {

    int size;

    public SlimePet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setSize(int i) {
        ((EntitySlimePet) getEntityPet()).setSize(i);
        this.size = i;
    }

    public int getSize() {
        return this.size;
    }
}
