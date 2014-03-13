package io.github.dsh105.echopet.entity.type.irongolem;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.IRONGOLEM)
public class IronGolemPet extends Pet {

    public IronGolemPet(Player owner) {
        super(owner);
    }

    public IronGolemPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }
}
