package io.github.dsh105.echopet.entity.type.magmacube;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.MAGMACUBE)
public class MagmaCubePet extends Pet {

    int size;

    public MagmaCubePet(Player owner) {
        super(owner);
    }

    public MagmaCubePet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setSize(int i) {
        ((EntityMagmaCubePet) getEntityPet()).setSize(i);
        this.size = i;
    }

    public int getSize() {
        return this.size;
    }
}
