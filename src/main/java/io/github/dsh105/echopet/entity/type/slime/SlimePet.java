package io.github.dsh105.echopet.entity.type.slime;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.SLIME)
public class SlimePet extends Pet {

    int size;

    public SlimePet(Player owner) {
        super(owner);
    }

    public SlimePet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setSize(int i) {
        ((EntitySlimePet) getEntityPet()).setSize(i);
        this.size = i;
    }

    public int getSize() {
        return this.size;
    }
}
