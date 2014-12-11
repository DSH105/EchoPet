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

package com.dsh105.echopet.compat.nms.v1_8_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWolfPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IWolfPet;
import com.dsh105.echopet.compat.nms.v1_8_R1.entity.EntityAgeablePet;
import net.minecraft.server.v1_8_R1.EnumColor;
import net.minecraft.server.v1_8_R1.EnumParticle;
import net.minecraft.server.v1_8_R1.MathHelper;
import net.minecraft.server.v1_8_R1.World;
import org.bukkit.DyeColor;

@EntitySize(width = 0.6F, height = 0.8F)
@EntityPetType(petType = PetType.WOLF)
public class EntityWolfPet extends EntityAgeablePet implements IEntityWolfPet {

    private boolean wet;
    private boolean shaking;
    private float shakeCount;

    public EntityWolfPet(World world) {
        super(world);
    }

    public EntityWolfPet(World world, IPet pet) {
        super(world, pet);
    }

    public boolean isTamed() {
        return (this.datawatcher.getByte(16) & 4) != 0;
    }

    @Override
    public void setTamed(boolean flag) {
        if (isAngry() && flag) {
            this.getPet().getPetData().remove(PetData.ANGRY);
        }

        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 4)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -5)));
        }
    }

    @Override
    public void setAngry(boolean flag) {
        if (isTamed() && flag) {
            this.getPet().getPetData().remove(PetData.TAMED);
        }

        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 2)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -3)));
        }
    }

    public boolean isAngry() {
        return (this.datawatcher.getByte(16) & 2) != 0;
    }

    @Override
    public void setCollarColor(DyeColor dc) {
        if (((IWolfPet) pet).isTamed()) {
            byte colour = dc.getWoolData();
            this.datawatcher.watch(20, colour);
        }
    }

    @Override
    public void onLive() {
        super.onLive();
        if (this.inWater) {
            this.wet = true;
            this.shaking = false;
            this.shakeCount = 0.0F;
        } else if ((this.wet || this.shaking) && this.shaking) {
            if (this.shakeCount == 0.0F) {
                this.makeSound("mob.wolf.shake", this.bA(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }

            this.shakeCount += 0.05F;
            if (this.shakeCount - 0.05F >= 2.0F) {
                this.wet = false;
                this.shaking = false;
                this.shakeCount = 0.0F;
            }

            if (this.shakeCount > 0.4F) {
                float f = (float) this.getBoundingBox().b;
                int i = (int) (MathHelper.sin((this.shakeCount - 0.4F) * 3.1415927F) * 7.0F);

                for (int j = 0; j < i; ++j) {
                    float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;

                    this.world.addParticle(EnumParticle.WATER_SPLASH, this.locX + (double) f1, (double) (f + 0.8F), this.locZ + (double) f2, this.motX, this.motY, this.motZ);
                }
            }
        }
    }

    @Override
    protected String getIdleSound() {
        return this.isAngry() ? "mob.wolf.growl" : (this.random.nextInt(3) == 0 ? (this.isTamed() && this.datawatcher.getFloat(18) < 10 ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
    }

    @Override
    protected String getDeathSound() {
        return "mob.wolf.death";
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(17, "");
        this.datawatcher.a(16, new Byte((byte) 0));
        this.datawatcher.a(18, new Float(this.getHealth()));
        this.datawatcher.a(19, new Byte((byte) 0));
        this.datawatcher.a(20, new Byte((byte) EnumColor.RED.getColorIndex()));
    }
}
