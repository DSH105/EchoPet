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

import org.bukkit.entity.Player;

import com.dsh105.echopet.api.pet.Pet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySnowmanPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ISnowmanPet;

@EntityPetType(petType = PetType.SNOWMAN)
public class SnowmanPet extends Pet implements ISnowmanPet {

	boolean sheared;

    public SnowmanPet(Player owner) {
        super(owner);
    }

	public void setSheared(boolean flag){
		((IEntitySnowmanPet) getEntityPet()).setSheared(flag);
		this.sheared = flag;
	}

	public boolean isSheared(){
		return this.sheared;
	}
}
