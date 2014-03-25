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

package io.github.dsh105.echopet.entity.type.wolf;

import io.github.dsh105.echopet.entity.*;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.entity.Wolf;

@EntityPetType(petType = PetType.WOLF)
public class CraftWolfPet extends CraftAgeablePet implements Wolf {

    public CraftWolfPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }

    @Override
    public boolean isAngry() {
        Pet p = this.getPet();
        if (p instanceof WolfPet) {
            return ((WolfPet) p).isAngry();
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
        Pet p = this.getPet();
        if (p instanceof WolfPet) {
            return ((WolfPet) p).isAngry();
        }
        return false;
    }

    @Override
    public void setSitting(boolean b) {
        // Pets can't do this
    }

    @Override
    public DyeColor getCollarColor() {
        Pet p = this.getPet();
        if (p instanceof WolfPet) {
            return ((WolfPet) p).getCollarColor();
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