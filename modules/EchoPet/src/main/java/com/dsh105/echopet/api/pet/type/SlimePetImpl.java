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
import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.nms.type.EntitySlimePet;
import com.dsh105.echopet.compat.api.entity.pet.type.SlimePet;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.SLIME, width = 0.6F, height = 0.6F)
public class SlimePetImpl extends PetImpl implements SlimePet {

    int size;

    public SlimePetImpl(Player owner) {
        super(owner);
    }

    @AttributeHandler(dataType = PetData.Type.SLIME_SIZE)
    @Override
    public void setSize(int i) {
        ((EntitySlimePet) getEntityPet()).setSize(i);
        this.size = i;
    }

    @AttributeHandler(dataType = PetData.Type.SLIME_SIZE, getter = true)
    @Override
    public int getSize() {
        return this.size;
    }
}
