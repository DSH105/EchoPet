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

import com.dsh105.echopet.api.entity.AttributeHandler;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.nms.EntityAgeablePet;
import org.bukkit.entity.Player;

public abstract class AgeablePetImpl extends PetImpl implements AgeablePet {

    boolean baby;

    protected AgeablePetImpl(Player owner) {
        super(owner);
    }

    @AttributeHandler(data = PetData.BABY)
    @Override
    public void setBaby(boolean flag) {
        ((EntityAgeablePet) getEntityPet()).setBaby(true);
    }

    @AttributeHandler(data = PetData.BABY, getter = true)
    @Override
    public boolean isBaby() {
        return baby;
    }
}