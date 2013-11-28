package io.github.dsh105.echopet.entity.living.type.giant;

import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.living.data.PetType;
import org.bukkit.entity.Player;


public class GiantPet extends LivingPet {

    public GiantPet(Player owner, PetType petType) {
        super(owner, petType);
    }
}