package io.github.dsh105.echopet.entity.type.wither;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Wither;

@EntityPetType(petType = PetType.WITHER)
public class CraftWitherPet extends CraftPet implements Wither {

    public CraftWitherPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}