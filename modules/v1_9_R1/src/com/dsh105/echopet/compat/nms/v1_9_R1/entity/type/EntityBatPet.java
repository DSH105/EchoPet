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

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityBatPet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityPet;

import net.minecraft.server.v1_9_R1.*;

@EntitySize(width = 0.5F, height = 0.9F)
@EntityPetType(petType = PetType.BAT)
public class EntityBatPet extends EntityPet implements IEntityBatPet {

	private static final DataWatcherObject<Byte> a = DataWatcher.a(EntityBat.class, DataWatcherRegistry.a);

    public EntityBatPet(World world) {
        super(world);
    }

    public EntityBatPet(World world, IPet pet) {
        super(world, pet);
    }

    public void setHanging(boolean flag) {
		int i = ((Byte) this.datawatcher.get(a)).byteValue();
        if (flag) {
			this.datawatcher.set(a, Byte.valueOf((byte) (i | 0x1)));
        } else {
			this.datawatcher.set(a, Byte.valueOf((byte) (i & 0xFFFFFFFE)));
        }
    }


    protected void initDatawatcher() {
        super.initDatawatcher();
		this.datawatcher.register(a, Byte.valueOf((byte) 0));
    }

	protected SoundEffect getIdleSound(){
		if((!isStartled()) && (this.random.nextInt(4) != 0)){ return null; }
		return SoundEffects.w;
	}


    public void onLive() {
        super.onLive();
        if (this.isStartled()) {
            this.motX = this.motY = this.motZ = 0.0D;
            this.locY = (double) MathHelper.floor(this.locY) + 1.0D - (double) this.length;
        } else {
            this.motY *= 0.6000000238418579D;
        }
    }

    public boolean isStartled() {
		return (((Byte) this.datawatcher.get(a)).byteValue() & 0x1) != 0;
    }


	protected SoundEffect getDeathSound(){
		return SoundEffects.x;
    }


    public SizeCategory getSizeCategory() {
        return SizeCategory.TINY;
    }
}
