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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityZombiePet;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityPet;

import net.minecraft.server.v1_9_R1.*;

@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.ZOMBIE)
public class EntityZombiePet extends EntityPet implements IEntityZombiePet {

	private static final DataWatcherObject<Boolean> bv = DataWatcher.a(EntityZombie.class, DataWatcherRegistry.h);
	private static final DataWatcherObject<Integer> bw = DataWatcher.a(EntityZombie.class, DataWatcherRegistry.b);
	private static final DataWatcherObject<Boolean> bx = DataWatcher.a(EntityZombie.class, DataWatcherRegistry.h);
	private static final DataWatcherObject<Boolean> by = DataWatcher.a(EntityZombie.class, DataWatcherRegistry.h);
    public EntityZombiePet(World world) {
        super(world);
    }

    public EntityZombiePet(World world, IPet pet) {
        super(world, pet);
        new BukkitRunnable() {
            @Override
            public void run() {
				setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
            }
        }.runTaskLater(EchoPet.getPlugin(), 5L);
    }

    @Override
    public void setBaby(boolean flag) {
		getDataWatcher().set(bv, Boolean.valueOf(flag));
    }

    @Override
	public void setVillagerType(int i){
		getDataWatcher().set(bw, Integer.valueOf(i + 1));
    }

	public boolean isVillager(){
		return false;
	}

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
		getDataWatcher().register(bv, Boolean.valueOf(false));
		getDataWatcher().register(bw, Integer.valueOf(0));
		getDataWatcher().register(bx, Boolean.valueOf(false));
		getDataWatcher().register(by, Boolean.valueOf(false));
    }

    @Override
	protected SoundEffect getIdleSound(){
		return isVillager() ? SoundEffects.hv : SoundEffects.hg;
    }

	protected void a(BlockPosition blockposition, Block block){
		a(isVillager() ? SoundEffects.hA : SoundEffects.hu, 0.15F, 1.0F);
    }

    @Override
	protected SoundEffect getDeathSound(){
		return isVillager() ? SoundEffects.hy : SoundEffects.hk;
    }

    @Override
    public boolean isBaby() {
		return ((Boolean) getDataWatcher().get(bv)).booleanValue();
    }

    @Override
    public SizeCategory getSizeCategory() {
        if (this.isBaby()) {
            return SizeCategory.TINY;
        } else {
            return SizeCategory.REGULAR;
        }
    }
}
