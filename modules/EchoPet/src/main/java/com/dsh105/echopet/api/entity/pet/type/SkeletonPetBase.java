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

package com.dsh105.echopet.api.entity.pet.type;

import com.dsh105.echopet.api.entity.*;
import com.dsh105.echopet.api.entity.entitypet.type.EntitySkeletonPet;
import com.dsh105.echopet.api.entity.pet.EquipablePetBase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;

@PetInfo(type = PetType.SKELETON, width = 0.6F, height = 1.9F)
public class SkeletonPetBase extends EquipablePetBase<Skeleton, EntitySkeletonPet> implements SkeletonPet {

    public SkeletonPetBase(Player owner) {
        super(owner);
    }

    @AttributeHandler(data = PetData.WITHER)
    @Override
    public void setWither(boolean flag) {
        if (flag != isWither()) {
            setWeapon(new ItemStack(flag ? Material.STONE_SWORD : Material.BOW));
        }
        getEntity().setWither(flag);
    }

    @AttributeHandler(data = PetData.WITHER, getter = true)
    @Override
    public boolean isWither() {
        return getEntity().isWither();
    }

    @Override
    public SizeCategory getSizeCategory() {
        return isWither() ? SizeCategory.LARGE : SizeCategory.REGULAR;
    }

    @Override
    public String getIdleSound() {
        return "mob.skeleton.say";
    }

    @Override
    public String getDeathSound() {
        return "mob.skeleton.death";
    }

    @Override
    public void makeStepSound() {
        getEntity().makeSound("mob.skeleton.step", 0.15F, 1.0F);
    }
}