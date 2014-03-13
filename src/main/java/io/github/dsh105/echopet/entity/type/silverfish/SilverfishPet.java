package io.github.dsh105.echopet.entity.type.silverfish;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.SILVERFISH)
public class SilverfishPet extends Pet {

    public SilverfishPet(Player owner) {
        super(owner);
    }

    public SilverfishPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }
}
