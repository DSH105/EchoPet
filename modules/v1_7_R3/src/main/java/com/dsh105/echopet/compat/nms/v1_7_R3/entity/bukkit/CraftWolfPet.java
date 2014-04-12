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

package com.dsh105.echopet.compat.nms.v1_7_R3.entity.bukkit;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.IWolfPet;
import com.dsh105.echopet.compat.nms.v1_7_R3.entity.EntityPet;
import org.bukkit.DyeColor;
import org.bukkit.entity.Wolf;

@EntityPetType(petType = PetType.WOLF)
public class CraftWolfPet extends CraftAgeablePet implements Wolf {

    public CraftWolfPet(EntityPet entity) {
        super(entity);
    }

    @Override
    public boolean isAngry() {
        IPet p = this.getPet();
        if (p instanceof IWolfPet) {
            return ((IWolfPet) p).isAngry();
        }
        return false;
    }

    @Override
    public void setAngry(boolean b) {
        /*Pet p = this.getPet();
        if (p instanceof WolfPet) {
            ((WolfPet) p).setAngry(b);
        }*/
    }

    @Override
    public boolean isSitting() {
        IPet p = this.getPet();
        if (p instanceof IWolfPet) {
            return ((IWolfPet) p).isAngry();
        }
        return false;
    }

    @Override
    public void setSitting(boolean b) {
        // Pets can't do this
    }

    @Override
    public DyeColor getCollarColor() {
        IPet p = this.getPet();
        if (p instanceof IWolfPet) {
            return ((IWolfPet) p).getCollarColor();
        }
        return null;
    }

    @Override
    public void setCollarColor(DyeColor dyeColor) {
        /*Pet p = this.getPet();
        if (p instanceof WolfPet) {
            ((WolfPet) p).setCollarColor(dyeColor);
        }*/
    }
}