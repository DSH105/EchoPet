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

import com.dsh105.echopet.api.pet.Pet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySlimePet;
import com.dsh105.echopet.compat.api.entity.type.pet.ISlimePet;
import org.bukkit.entity.Player;

import java.util.UUID;

@EntityPetType(petType = PetType.SLIME)
public class SlimePet extends Pet implements ISlimePet {

    int size;

    public SlimePet(Player owner) {
        super(owner);
    }

    @Override
    public void setSize(int i) {
        ((IEntitySlimePet) getEntityPet()).setSize(i);
        this.size = i;
    }

    @Override
    public int getSize() {
        return this.size;
    }
}
