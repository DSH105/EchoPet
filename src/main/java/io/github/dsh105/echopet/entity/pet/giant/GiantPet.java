package io.github.dsh105.echopet.entity.pet.giant;

import io.github.dsh105.echopet.data.PetType;
import io.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.entity.Player;

/**
 * Project by DSH105
 */

public class GiantPet extends Pet {

    public GiantPet(Player owner, PetType petType) {
        super(owner, petType);
    }
}