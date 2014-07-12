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

package com.dsh105.echopetv3.api.entity.pet.type;

import com.dsh105.echopetv3.api.entity.PetInfo;
import com.dsh105.echopetv3.api.entity.PetType;
import com.dsh105.echopetv3.api.entity.entitypet.type.EntityPigZombiePet;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.PIG_ZOMBIE, width = 0.6F, height = 1.8F)
public class PigZombiePetBase extends ZombiePetBase<PigZombie, EntityPigZombiePet> implements PigZombiePet {

    public PigZombiePetBase(Player owner) {
        super(owner);
    }
}