package io.github.dsh105.echopet.entity.pet.magmacube;

import io.github.dsh105.echopet.data.PetType;
import io.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.entity.Player;


public class MagmaCubePet extends Pet {

    int size;

    public MagmaCubePet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setSize(int i) {
        ((EntityMagmaCubePet) getEntityPet()).setSize(i);
        this.size = i;
    }

    public int getSize() {
        return this.size;
    }
}
