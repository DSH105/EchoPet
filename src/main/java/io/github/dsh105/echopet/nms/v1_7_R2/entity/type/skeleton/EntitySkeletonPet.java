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

package io.github.dsh105.echopet.nms.v1_7_R2.entity.type.skeleton;

import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.*;
import net.minecraft.server.v1_7_R2.ItemStack;
import net.minecraft.server.v1_7_R2.Items;
import net.minecraft.server.v1_7_R2.World;
import org.bukkit.scheduler.BukkitRunnable;

@EntitySize(width = 0.6F, height = 1.9F)
@EntityPetType(petType = PetType.SKELETON)
public class EntitySkeletonPet extends EntityPet {

    public EntitySkeletonPet(World world) {
        super(world);
    }

    public EntitySkeletonPet(World world, Pet pet) {
        super(world, pet);
        final SkeletonPet sp = (SkeletonPet) pet;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (sp.wither) {
                    setEquipment(0, new ItemStack(Items.STONE_SWORD));
                } else {
                    setEquipment(0, new ItemStack(Items.BOW));
                }
            }
        }.runTaskLater(EchoPetPlugin.getInstance(), 5L);
    }

    public void setWither(boolean flag) {
        this.datawatcher.watch(13, (byte) (flag ? 1 : 0));
        if (flag) {
            setEquipment(0, new ItemStack(Items.STONE_SWORD));
        } else {
            setEquipment(0, new ItemStack(Items.BOW));
        }
    }

    public int getSkeletonType() {
        return this.datawatcher.getByte(13);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(13, new Byte((byte) 0));
    }

    @Override
    protected String getIdleSound() {
        return "mob.skeleton.say";
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("mob.skeleton.step", 0.15F, 1.0F);
    }

    @Override
    protected String getDeathSound() {
        return "mob.skeleton.death";
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