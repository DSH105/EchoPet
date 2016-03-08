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

import org.bukkit.scheduler.BukkitRunnable;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySkeletonPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ISkeletonPet;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityPet;

import net.minecraft.server.v1_9_R1.*;

@EntitySize(width = 0.6F, height = 1.9F)
@EntityPetType(petType = PetType.SKELETON)
public class EntitySkeletonPet extends EntityPet implements IEntitySkeletonPet {

	private static final DataWatcherObject<Integer> a = DataWatcher.a(EntitySkeleton.class, DataWatcherRegistry.b);
	private static final DataWatcherObject<Boolean> b = DataWatcher.a(EntitySkeleton.class, DataWatcherRegistry.h);

    public EntitySkeletonPet(World world) {
        super(world);
    }

    public EntitySkeletonPet(World world, final IPet pet) {
        super(world, pet);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (((ISkeletonPet) pet).isWither()) {
					setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                } else {
					setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Items.BOW));
                }
            }
        }.runTaskLater(EchoPet.getPlugin(), 5L);
    }

    @Override
    public void setWither(boolean flag) {
		this.datawatcher.set(b, Boolean.valueOf(flag));
        if (flag) {
			setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        } else {
			setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Items.BOW));
        }
    }

    public int getSkeletonType() {
		return ((Integer) this.datawatcher.get(a)).intValue();
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
		this.datawatcher.register(a, Integer.valueOf(0));
		this.datawatcher.register(b, Boolean.valueOf(false));
    }

    @Override
    protected SoundEffect getIdleSound() {
        return SoundEffects.fh;
    }

    @Override
	protected void a(BlockPosition blockposition, Block block){
		a(SoundEffects.fo, 0.15F, 1.0F);
    }

    @Override
	protected SoundEffect getDeathSound(){
        return SoundEffects.fi;
    }

    @Override
    public SizeCategory getSizeCategory() {
        if (this.getSkeletonType() == 1) {
            return SizeCategory.LARGE;
        } else {
            return SizeCategory.REGULAR;
        }
    }
}
