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

import com.dsh105.commodus.container.ItemStackContainer;
import com.dsh105.echopet.api.entity.entitypet.EntityEquipablePet;
import com.dsh105.echopet.bridge.entity.EquipableEntityBridge;

import java.util.UUID;

public abstract class EchoEquipablePet<T extends EquipableEntityBridge, S extends EntityEquipablePet> extends AbstractPetBase<T, S> implements EquipablePet<T, S> {

    protected EchoEquipablePet(UUID playerUID) {
        super(playerUID);
        getEntity().applyDefaultItems();
    }

    @Override
    public void setWeapon(ItemStackContainer itemStack) {
        getBridgeEntity().setWeapon(itemStack);
    }

    @Override
    public ItemStackContainer getWeapon() {
        return getBridgeEntity().getWeapon();
    }
}