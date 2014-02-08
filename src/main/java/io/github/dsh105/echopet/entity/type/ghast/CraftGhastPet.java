package io.github.dsh105.echopet.entity.type.ghast;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Ghast;

@EntityPetType(petType = PetType.GHAST)
public class CraftGhastPet extends CraftPet implements Ghast {

    public CraftGhastPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}