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
import com.dsh105.echopet.compat.api.entity.nms.type.EntityBlazePet;
import com.dsh105.echopet.compat.api.entity.pet.type.BlazePet;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.BLAZE, width = 0.6F, height = 1.7F)
public class BlazePetImpl extends PetImpl implements BlazePet {

    boolean onFire;

    public BlazePetImpl(Player owner) {
        super(owner);
    }

    @AttributeHandler(data = PetData.FIRE)
    @Override
    public void setOnFire(boolean flag) {
        ((EntityBlazePet) getEntityPet()).setOnFire(flag);
        this.onFire = flag;
    }

    @AttributeHandler(data = PetData.FIRE, getter = true)
    @Override
    public boolean isOnFire() {
        return this.onFire;
    }
}
