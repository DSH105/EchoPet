package io.github.dsh105.echopet.entity.type.snowman;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.entity.Snowman;

@EntityPetType(petType = PetType.SNOWMAN)
public class CraftSnowmanPet extends CraftPet implements Snowman {

    public CraftSnowmanPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}