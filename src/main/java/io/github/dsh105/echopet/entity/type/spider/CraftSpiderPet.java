package io.github.dsh105.echopet.entity.type.spider;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Spider;

@EntityPetType(petType = PetType.SPIDER)
public class CraftSpiderPet extends CraftPet implements Spider {

    public CraftSpiderPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}