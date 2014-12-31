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

package com.dsh105.echopet.bridge.platform.sponge.entity.type;

import com.dsh105.echopet.bridge.entity.type.ZombieEntityBridge;
import com.dsh105.echopet.bridge.platform.sponge.entity.SpongeEquipableEntityBridge;
import org.spongepowered.api.entity.living.monster.Zombie;

public class SpongeZombieEntityBridge<E extends Zombie> extends SpongeEquipableEntityBridge<E> implements ZombieEntityBridge {

    @Override
    public void setVillager(boolean flag) {
        getSpongeEntity().setVillagerZombie(flag);
    }

    @Override
    public boolean isVillager() {
        return getSpongeEntity().isVillagerZombie();
    }

    @Override
    public void setAdult(boolean flag) {
        if (flag) {
            getSpongeEntity().setAdult();
        } else {
            getSpongeEntity().setBaby();
        }
    }

    @Override
    public boolean isAdult() {
        return !getSpongeEntity().isBaby();
    }
}