package io.github.dsh105.echopet.entity.type.enderdragon;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.ENDERDRAGON)
public class EnderDragonPet extends Pet {

    public EnderDragonPet(Player owner) {
        super(owner);
    }

    public EnderDragonPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }
}