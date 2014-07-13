package com.dsh105.echopetv3.nms.v1_7_R4.entity.type;

import com.dsh105.echopetv3.api.entity.entitypet.type.EntityWitchPet;
import com.dsh105.echopetv3.api.entity.pet.type.WitchPet;
import com.dsh105.echopetv3.nms.v1_7_R4.entity.EntityPetBase;
import net.minecraft.server.v1_7_R4.World;

public class EntityWitchPetBase extends EntityPetBase<WitchPet> implements EntityWitchPet {

    public EntityWitchPetBase(World world, WitchPet pet) {
        super(world, pet);
    }
}