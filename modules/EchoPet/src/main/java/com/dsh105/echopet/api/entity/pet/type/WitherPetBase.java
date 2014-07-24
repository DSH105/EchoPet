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

import com.dsh105.echopet.api.entity.*;
import com.dsh105.echopet.api.entity.entitypet.type.EntityWitherPet;
import com.dsh105.echopet.api.entity.pet.PetBase;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;

@PetInfo(type = PetType.WITHER, width = 0.9F, height = 4.0F)
public class WitherPetBase extends PetBase<Wither, EntityWitherPet> implements WitherPet {

    public WitherPetBase(Player owner) {
        super(owner);
    }

    @AttributeHandler(data = PetData.SHIELD)
    @Override
    public void setShielded(boolean flag) {
        getEntity().setShielded(flag);
    }

    @AttributeHandler(data = PetData.SHIELD, getter = true)
    @Override
    public boolean isShielded() {
        return getEntity().isShielded();
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.LARGE;
    }

    @Override
    public String getIdleSound() {
        return "mob.wither.idle";
    }

    @Override
    public String getDeathSound() {
        return "mob.wither.death";
    }
}