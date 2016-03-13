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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityEndermanPet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityPet;
import com.google.common.base.Optional;

import net.minecraft.server.v1_9_R1.*;

@EntitySize(width = 0.6F, height = 2.9F)
@EntityPetType(petType = PetType.ENDERMAN)
public class EntityEndermanPet extends EntityPet implements IEntityEndermanPet {

	private static final DataWatcherObject<Optional<IBlockData>> bv = DataWatcher.a(EntityEnderman.class, DataWatcherRegistry.g);
	private static final DataWatcherObject<Boolean> bw = DataWatcher.a(EntityEnderman.class, DataWatcherRegistry.h);

    public EntityEndermanPet(World world) {
        super(world);
    }

    public EntityEndermanPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public void setScreaming(boolean flag) {
		this.datawatcher.set(bw, Boolean.valueOf(flag));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
		this.datawatcher.register(bv, Optional.absent());
		this.datawatcher.register(bw, Boolean.valueOf(false));
    }

    @Override
	protected SoundEffect getIdleSound(){
		return isScreaming() ? SoundEffects.aX : SoundEffects.aU;
    }

    public boolean isScreaming() {
		return ((Boolean) this.datawatcher.get(bw)).booleanValue();
    }

	public void setCarried(IBlockData iblockdata){
		this.datawatcher.set(bv, Optional.fromNullable(iblockdata));
    }

	public IBlockData getCarried(){
		return (IBlockData) ((Optional<IBlockData>) this.datawatcher.get(bv)).orNull();
    }

    @Override
	protected SoundEffect getDeathSound(){
		return SoundEffects.aV;
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }
}
