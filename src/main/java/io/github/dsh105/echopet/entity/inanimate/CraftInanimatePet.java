package io.github.dsh105.echopet.entity.inanimate;

import io.github.dsh105.echopet.entity.ICraftPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import net.minecraft.server.v1_7_R1.EntityCreature;
import net.minecraft.server.v1_7_R1.EntityLiving;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class CraftInanimatePet extends CraftEntity implements ICraftPet {

    private EntityInanimatePet entityPet;

    public CraftInanimatePet(CraftServer server, EntityInanimatePet entity) {
        super(server, entity);
        this.entityPet = entity;
    }

    @Override
    public EntityType getType() {
        return this.entityPet.getPet().getPetType().getEntityType();
    }

    public void remove() {
        super.remove();
    }

    public InanimatePet getPet() {
        return entityPet.getPet();
    }

    @Override
    public EntityInanimatePet getHandle() {
        return this.entityPet;
    }
}