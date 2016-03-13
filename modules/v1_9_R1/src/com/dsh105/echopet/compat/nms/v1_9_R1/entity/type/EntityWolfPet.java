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

package com.dsh105.echopet.compat.nms.v1_9_R1.entity.type;


import org.bukkit.DyeColor;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWolfPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IWolfPet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityTameablePet;

import net.minecraft.server.v1_9_R1.*;

@EntitySize(width = 0.6F, height = 0.8F)
@EntityPetType(petType = PetType.WOLF)
public class EntityWolfPet extends EntityTameablePet implements IEntityWolfPet{

	private static final DataWatcherObject<Float> DATA_HEALTH = DataWatcher.a(EntityWolf.class, DataWatcherRegistry.c);
	private static final DataWatcherObject<Boolean> bA = DataWatcher.a(EntityWolf.class, DataWatcherRegistry.h);
	private static final DataWatcherObject<Integer> bB = DataWatcher.a(EntityWolf.class, DataWatcherRegistry.b);
    private boolean wet;
    private boolean shaking;
    private float shakeCount;

    public EntityWolfPet(World world) {
        super(world);
    }

    public EntityWolfPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public void setAngry(boolean flag) {
        if (isTamed() && flag) {
            this.getPet().getPetData().remove(PetData.TAMED);
            setTamed(false);
        }

		byte b0 = ((Byte) this.datawatcher.get(bv)).byteValue();
        if (flag) {
			this.datawatcher.set(bv, Byte.valueOf((byte) (b0 | 0x2)));
        } else {
			this.datawatcher.set(bv, Byte.valueOf((byte) (b0 & 0xFFFFFFFD)));
        }
    }

    public boolean isAngry() {
		return (((Byte) this.datawatcher.get(bv)).byteValue() & 0x2) != 0;
    }

    @Override
	public void setCollarColor(DyeColor dc){
        if (((IWolfPet) pet).isTamed()) {
			this.datawatcher.set(bB, Integer.valueOf(dc.getWoolData()));
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
				a(SoundEffects.gQ, cd(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
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
	protected SoundEffect getIdleSound(){
		return this.random.nextInt(3) == 0 ? SoundEffects.gP : (isTamed()) && (((Float) this.datawatcher.get(DATA_HEALTH)).floatValue() < 10.0F) ? SoundEffects.gS : isAngry() ? SoundEffects.gM : SoundEffects.gK;
    }

    @Override
	protected SoundEffect getDeathSound(){
		return SoundEffects.gL;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
		this.datawatcher.register(DATA_HEALTH, Float.valueOf(getHealth()));
		this.datawatcher.register(bA, Boolean.valueOf(false));
		this.datawatcher.register(bB, Integer.valueOf(EnumColor.RED.getInvColorIndex()));
    }

}
