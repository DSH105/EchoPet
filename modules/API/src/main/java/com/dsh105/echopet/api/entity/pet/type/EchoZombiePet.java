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

import com.dsh105.echopet.api.entity.entitypet.type.EntityZombiePet;
import com.dsh105.echopet.api.entity.pet.EchoEquipablePet;
import com.dsh105.echopet.bridge.entity.type.ZombieEntityBridge;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class EchoZombiePet<T extends ZombieEntityBridge, S extends EntityZombiePet> extends EchoEquipablePet<T, S> implements ZombiePet<T, S> {

    public EchoZombiePet(UUID playerUID) {
        super(playerUID);
    }

    @Override
    public void setVillager(boolean flag) {
        getBridgeEntity().setVillager(flag);
    }

    @Override
    public boolean isVillager() {
        return getBridgeEntity().isVillager();
    }

    @Override
    public void setBaby(boolean flag) {
        getBridgeEntity().setAdult(!flag);
    }

    @Override
    public boolean isBaby() {
        return !getBridgeEntity().isAdult();
    }
}