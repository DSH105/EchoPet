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

package com.dsh105.echopetv3.api.entity.pet;

import com.dsh105.echopetv3.api.entity.AttributeHandler;
import com.dsh105.echopetv3.api.entity.PetData;
import com.dsh105.echopetv3.api.entity.SizeCategory;
import com.dsh105.echopetv3.api.entity.entitypet.EntityAgeablePet;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Player;

public abstract class AgeablePetBase<T extends Ageable, S extends EntityAgeablePet> extends PetBase<T, S> implements AgeablePet<T, S> {

    protected AgeablePetBase(Player owner) {
        super(owner);
        getBukkitEntity().setAgeLock(true);
    }

    @AttributeHandler(data = PetData.BABY)
    @Override
    public void setBaby(boolean flag) {
        if (flag) {
            getBukkitEntity().setBaby();
        } else {
            getBukkitEntity().setAdult();
        }
    }

    @AttributeHandler(data = PetData.BABY, getter = true)
    @Override
    public boolean isBaby() {
        return !getBukkitEntity().isAdult();
    }

    @Override
    public SizeCategory getSizeCategory() {
        return isBaby() ? SizeCategory.TINY : SizeCategory.REGULAR;
    }
}