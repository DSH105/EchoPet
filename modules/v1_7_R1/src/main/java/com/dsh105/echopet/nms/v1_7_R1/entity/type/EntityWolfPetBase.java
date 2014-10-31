/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.nms.v1_7_R1.entity.type;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.echopet.api.entity.entitypet.type.EntityWolfPet;
import com.dsh105.echopet.api.entity.pet.type.WolfPet;
import com.dsh105.echopet.nms.v1_7_R1.entity.EntityAgeablePetBase;
import net.minecraft.server.v1_7_R1.MathHelper;
import net.minecraft.server.v1_7_R1.World;
import org.bukkit.DyeColor;

public class EntityWolfPetBase extends EntityAgeablePetBase<WolfPet> implements EntityWolfPet {

    public EntityWolfPetBase(World world, WolfPet pet) {
        super(world, pet);
    }

    @Override
    public void setWolfCollarColor(DyeColor color) {
        if (isTamed()) {
            this.datawatcher.watch(DATAWATCHER_COLLAR_COLOUR, color.getWoolData());
        }
    }

    @Override
    public DyeColor getWolfCollarColor() {
        return DyeColor.getByWoolData(this.datawatcher.getByte(DATAWATCHER_COLLAR_COLOUR));
    }

    @Override
    public void setTamed(boolean flag) {
        if (isAngry() && flag) {
            setAngry(false);
        }

        byte value = this.datawatcher.getByte(DATAWATCHER_ENTITY_STATUS);

        if (flag) {
            this.datawatcher.watch(DATAWATCHER_ENTITY_STATUS, (byte) (value | 4));
        } else {
            this.datawatcher.watch(DATAWATCHER_ENTITY_STATUS, (byte) (value & -5));
        }
    }

    @Override
    public boolean isTamed() {
        return (this.datawatcher.getByte(DATAWATCHER_ENTITY_STATUS) & 4) != 0;
    }

    @Override
    public void setAngry(boolean flag) {
        if (isTamed() && flag) {
            setTamed(false);
        }

        byte value = this.datawatcher.getByte(DATAWATCHER_ENTITY_STATUS);

        if (flag) {
            this.datawatcher.watch(DATAWATCHER_ENTITY_STATUS, (byte) (value | 2));
        } else {
            this.datawatcher.watch(DATAWATCHER_ENTITY_STATUS, (byte) (value & -3));
        }
    }

    @Override
    public boolean isAngry() {
        return (this.datawatcher.getByte(DATAWATCHER_ENTITY_STATUS) & 2) != 0;
    }

    @Override
    public float getTailHealth() {
        return this.datawatcher.getFloat(DATAWATCHER_HEALTH);
    }

    @Override
    public void shakeParticle(float shakeCount) {
        float f = (float) this.boundingBox.b;
        int i = (int) (MathHelper.sin((shakeCount - 0.4F) * 3.1415927F) * 7.0F);

        for (int j = 0; j < i; ++j) {
            float f1 = (GeneralUtil.random().nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
            float f2 = (GeneralUtil.random().nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;

            this.world.addParticle("splash", this.locX + (double) f1, (double) (f + 0.8F), this.locZ + (double) f2, this.motX, this.motY, this.motZ);
        }
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(DATAWATCHER_OWNER_NAME, getPet().getOwnerName());
        this.datawatcher.a(DATAWATCHER_ENTITY_STATUS, new Byte((byte) 0));
        this.datawatcher.a(DATAWATCHER_HEALTH, this.getHealth());
        this.datawatcher.a(DATAWATCHER_BEGGING, new Byte((byte) 0));
        this.datawatcher.a(DATAWATCHER_COLLAR_COLOUR, new Byte(DyeColor.RED.getWoolData()));
    }
}