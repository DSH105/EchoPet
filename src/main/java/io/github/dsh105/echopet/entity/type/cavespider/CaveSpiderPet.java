package io.github.dsh105.echopet.entity.type.cavespider;


import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.CAVESPIDER)
public class CaveSpiderPet extends Pet {

    public CaveSpiderPet(Player owner) {
        super(owner);
    }

    public CaveSpiderPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }
}