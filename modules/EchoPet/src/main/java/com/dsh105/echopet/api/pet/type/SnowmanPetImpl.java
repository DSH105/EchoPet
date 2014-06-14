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

package com.dsh105.echopet.api.pet.type;

import com.dsh105.echopet.api.pet.PetImpl;
import com.dsh105.echopet.compat.api.entity.PetInfo;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.pet.type.SnowmanPet;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.SNOWMAN, width = 0.4F, height = 1.8F)
public class SnowmanPetImpl extends PetImpl implements SnowmanPet {

    public SnowmanPetImpl(Player owner) {
        super(owner);
    }
}
