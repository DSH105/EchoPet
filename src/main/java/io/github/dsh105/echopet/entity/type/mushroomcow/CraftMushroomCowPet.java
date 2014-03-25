package io.github.dsh105.echopet.entity.type.mushroomcow;

import io.github.dsh105.echopet.entity.CraftAgeablePet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.entity.MushroomCow;

@EntityPetType(petType = PetType.MUSHROOMCOW)
public class CraftMushroomCowPet extends CraftAgeablePet implements MushroomCow {

    public CraftMushroomCowPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}