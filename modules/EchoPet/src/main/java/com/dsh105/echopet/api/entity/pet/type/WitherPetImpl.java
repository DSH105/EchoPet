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
import com.dsh105.echopet.api.entity.PetInfo;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.nms.type.EntityWitherPet;
import com.dsh105.echopet.api.entity.pet.PetImpl;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.WITHER, width = 0.9F, height = 4.0F)
public class WitherPetImpl extends PetImpl implements WitherPet {

    boolean shield = false;

    public WitherPetImpl(Player owner) {
        super(owner);
    }

    @AttributeHandler(data = PetData.SHIELD)
    @Override
    public void setShielded(boolean flag) {
        ((EntityWitherPet) getEntityPet()).setShielded(flag);
        this.shield = flag;
    }

    @AttributeHandler(data = PetData.SHIELD, getter = true)
    @Override
    public boolean isShielded() {
        return this.shield;
    }

}
