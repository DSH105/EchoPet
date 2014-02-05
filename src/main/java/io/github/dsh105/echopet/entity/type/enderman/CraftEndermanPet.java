package io.github.dsh105.echopet.entity.type.enderman;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.craftbukkit.v1_7_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Enderman;
import org.bukkit.material.MaterialData;

public class CraftEndermanPet extends CraftPet implements Enderman {

    public CraftEndermanPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }

    @Override
    public MaterialData getCarriedMaterial() {
        EntityPet e = getHandle();
        if (e instanceof EntityEndermanPet) {
            EntityEndermanPet endermanPet = (EntityEndermanPet) e;
            return CraftMagicNumbers.getMaterial(endermanPet.getCarried()).getNewData((byte) endermanPet.getCarriedData());
        }
        return null;
    }

    @Override
    public void setCarriedMaterial(MaterialData data) {
        /*EntityPet e = getHandle();
        if (e instanceof EntityEndermanPet) {
            EntityEndermanPet endermanPet = (EntityEndermanPet) e;
            endermanPet.setCarried(CraftMagicNumbers.getBlock(data.getItemTypeId()));
            endermanPet.setCarriedData(data.getData());
        }*/
    }
}