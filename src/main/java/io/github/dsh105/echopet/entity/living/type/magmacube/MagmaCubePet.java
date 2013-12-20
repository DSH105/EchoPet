package io.github.dsh105.echopet.entity.living.type.magmacube;

import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.entity.Player;


public class MagmaCubePet extends LivingPet {

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
