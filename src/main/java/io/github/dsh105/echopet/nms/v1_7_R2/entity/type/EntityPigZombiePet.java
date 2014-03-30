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

package io.github.dsh105.echopet.nms.v1_7_R2.entity.type;

import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.api.entity.EntityPetType;
import io.github.dsh105.echopet.api.entity.EntitySize;
import io.github.dsh105.echopet.api.entity.PetType;
import io.github.dsh105.echopet.api.entity.SizeCategory;
import io.github.dsh105.echopet.api.entity.nms.type.IEntityPigZombiePet;
import io.github.dsh105.echopet.api.entity.pet.Pet;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.*;
import net.minecraft.server.v1_7_R2.ItemStack;
import net.minecraft.server.v1_7_R2.Items;
import net.minecraft.server.v1_7_R2.World;
import org.bukkit.scheduler.BukkitRunnable;

@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.PIGZOMBIE)
public class EntityPigZombiePet extends EntityPet implements IEntityPigZombiePet {

    public EntityPigZombiePet(World world) {
        super(world);
    }

    public EntityPigZombiePet(World world, Pet pet) {
        super(world, pet);
        new BukkitRunnable() {
            @Override
            public void run() {
                setEquipment(0, new ItemStack(Items.GOLD_SWORD));
            }
        }.runTaskLater(EchoPetPlugin.getInstance(), 5L);
    }

    public void setBaby(boolean flag) {
        this.datawatcher.watch(12, (byte) (flag ? 1 : 0));
    }

    public void setVillager(boolean flag) {
        this.datawatcher.watch(13, (byte) (flag ? 1 : 0));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(12, new Byte((byte) 0));
        this.datawatcher.a(13, new Byte((byte) 0));
    }

    @Override
    protected String getIdleSound() {
        return "mob.zombiepig.zpig";
    }

    @Override
    protected String getDeathSound() {
        return "mob.zombiepig.zpigdeath";
    }

    @Override
    public boolean isBaby() {
        return this.datawatcher.getByte(12) < 0;
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