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

import com.dsh105.echopet.api.pet.AgeablePetImpl;
import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.nms.type.EntityChickenPet;
import com.dsh105.echopet.compat.api.entity.pet.type.ChickenPet;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.CHICKEN, width = 0.3F, height = 0.7F)
public class ChickenPetImpl extends AgeablePetImpl implements ChickenPet {

    boolean baby;

    public ChickenPetImpl(Player owner) {
        super(owner);
    }

    @AttributeHandler(data = PetData.BABY)
    @Override
    public void setBaby(boolean flag) {
        ((EntityChickenPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    @AttributeHandler(data = PetData.BABY)
    @Override
    public boolean isBaby() {
        return this.baby;
    }
}