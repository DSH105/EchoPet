package io.github.dsh105.echopet.entity.type.chicken;

import io.github.dsh105.echopet.entity.CraftAgeablePet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.entity.Chicken;

@EntityPetType(petType = PetType.CHICKEN)
public class CraftChickenPet extends CraftAgeablePet implements Chicken {

    public CraftChickenPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}