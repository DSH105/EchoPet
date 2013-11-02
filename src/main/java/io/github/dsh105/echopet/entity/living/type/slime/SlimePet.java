package io.github.dsh105.echopet.entity.living.type.slime;

import io.github.dsh105.echopet.entity.living.data.PetType;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.entity.Player;


public class SlimePet extends LivingPet {

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
