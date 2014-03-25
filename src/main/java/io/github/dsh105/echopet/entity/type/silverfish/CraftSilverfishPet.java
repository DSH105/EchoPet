package io.github.dsh105.echopet.entity.type.silverfish;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.entity.Silverfish;

@EntityPetType(petType = PetType.SILVERFISH)
public class CraftSilverfishPet extends CraftPet implements Silverfish {

    public CraftSilverfishPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }
}