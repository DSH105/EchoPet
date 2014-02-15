package io.github.dsh105.echopet.entity.type.giant;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.GIANT)
public class GiantPet extends Pet {

    public GiantPet(Player owner) {
        super(owner);
    }

    public GiantPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }
}