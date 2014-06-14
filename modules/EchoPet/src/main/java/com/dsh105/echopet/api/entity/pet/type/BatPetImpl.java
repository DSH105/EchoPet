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

import com.dsh105.echopet.api.entity.pet.PetImpl;
import com.dsh105.echopet.api.entity.*;
import com.dsh105.echopet.api.entity.nms.type.EntityBatPet;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.BAT, width = 0.5F, height = 0.9F)
public class BatPetImpl extends PetImpl implements BatPet {

    boolean hanging;

    public BatPetImpl(Player owner) {
        super(owner);
    }

    @Override
    public boolean isHanging() {
        return this.hanging;
    }

    @Override
    public void setHanging(boolean flag) {
        ((EntityBatPet) getEntityPet()).setHanging(flag);
        this.hanging = flag;
    }
}
