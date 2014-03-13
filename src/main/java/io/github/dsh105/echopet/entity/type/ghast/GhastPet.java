package io.github.dsh105.echopet.entity.type.ghast;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.GHAST)
public class GhastPet extends Pet {

    public GhastPet(Player owner) {
        super(owner);
    }

    public GhastPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }
}
