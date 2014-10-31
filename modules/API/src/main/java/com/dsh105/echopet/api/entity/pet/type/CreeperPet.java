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

import com.dsh105.echopet.api.entity.AttributeHandler;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.entitypet.type.EntityCreeperPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import org.bukkit.entity.Creeper;

public interface CreeperPet extends Pet<Creeper, EntityCreeperPet> {

    void setPowered(boolean flag);

    boolean isPowered();

    @AttributeHandler(data = PetData.POWER)
    void setIgnited(boolean flag);

    @AttributeHandler(data = PetData.POWER, getter = true)
    boolean isIgnited();
}