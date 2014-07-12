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
import com.dsh105.echopetv3.api.entity.entitypet.type.EntityCaveSpiderPet;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.CAVE_SPIDER, width = 0.7F, height = 0.5F)
public class CaveSpiderPetBase extends SpiderPetBase<CaveSpider, EntityCaveSpiderPet> implements CaveSpiderPet {

    public CaveSpiderPetBase(Player owner) {
        super(owner);
    }
}