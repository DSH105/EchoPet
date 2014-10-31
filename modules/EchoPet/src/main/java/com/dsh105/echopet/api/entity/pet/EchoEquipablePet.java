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

package com.dsh105.echopet.api.entity.pet;

import com.dsh105.echopet.api.entity.entitypet.EntityEquipablePet;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public abstract class EchoEquipablePet<T extends LivingEntity, S extends EntityEquipablePet> extends PetBase<T, S> implements EquipablePet<T, S> {

    protected EchoEquipablePet(Player owner) {
        super(owner);
    }

    @Override
    public EntityEquipment getEquipment() {
        return getBukkitEntity().getEquipment();
    }

    @Override
    public ItemStack getWeapon() {
        return getEquipment().getItemInHand();
    }

    @Override
    public void setWeapon(ItemStack stack) {
        getEquipment().setItemInHand(stack);
    }
}