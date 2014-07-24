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

package com.dsh105.echopet.nms.v1_7_R3.entity;

import com.dsh105.echopet.api.entity.entitypet.EntityEquipablePet;
import com.dsh105.echopet.api.entity.pet.EquipablePet;
import net.minecraft.server.v1_7_R3.Item;
import net.minecraft.server.v1_7_R3.ItemStack;
import net.minecraft.server.v1_7_R3.World;
import org.bukkit.Material;

public abstract class EntityEquipablePetBase<T extends EquipablePet> extends EntityPetBase<T> implements EntityEquipablePet<T> {

    public EntityEquipablePetBase(World world, T pet) {
        super(world, pet);
    }

    @Override
    public org.bukkit.inventory.ItemStack getWeapon() {
        return new org.bukkit.inventory.ItemStack(Material.getMaterial(getEquipment(0).getName()));
    }

    @Override
    public void setWeapon(org.bukkit.inventory.ItemStack stack) {
        setEquipment(0, new ItemStack((Item) Item.REGISTRY.a(stack.getType().name().toLowerCase())));
    }
}