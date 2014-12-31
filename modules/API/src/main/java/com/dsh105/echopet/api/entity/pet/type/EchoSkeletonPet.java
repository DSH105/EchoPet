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

import com.dsh105.echopet.api.entity.SizeCategory;
import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.entitypet.type.EntitySkeletonPet;
import com.dsh105.echopet.api.entity.pet.EchoEquipablePet;
import com.dsh105.echopet.bridge.entity.type.SkeletonEntityBridge;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class EchoSkeletonPet extends EchoEquipablePet<SkeletonEntityBridge, EntitySkeletonPet> implements SkeletonPet {

    public EchoSkeletonPet(UUID playerUID) {
        super(playerUID);
    }

    @Override
    public void setWither(boolean flag) {
        setSkeletonType(flag ? Attributes.SkeletonType.WITHER : Attributes.SkeletonType.NORMAL);
    }

    @Override
    public boolean isWither() {
        return getSkeletonType() == Attributes.SkeletonType.WITHER;
    }

    @Override
    public void setSkeletonType(Attributes.SkeletonType type) {
        if (type != getSkeletonType()) {
            getEntity().applyDefaultItems();
        }
        getEntity().setSkeletonEntityType(type);
    }

    @Override
    public Attributes.SkeletonType getSkeletonType() {
        return getEntity().getSkeletonEntityType();
    }

    @Override
    public SizeCategory getSizeCategory() {
        return isWither() ? SizeCategory.LARGE : super.getSizeCategory();
    }

    @Override
    public void rangedAttack(Object livingEntity, float speed) {
        getEntity().rangedAttack(livingEntity, speed);
    }
}