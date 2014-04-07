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

package com.dsh105.echopet.compat.nms.v1_7_R2.entity.bukkit;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.IOcelotPet;
import com.dsh105.echopet.compat.nms.v1_7_R2.entity.EntityPet;
import org.bukkit.entity.Ocelot;

@EntityPetType(petType = PetType.OCELOT)
public class CraftOcelotPet extends CraftAgeablePet implements Ocelot {

    public CraftOcelotPet(EntityPet entity) {
        super(entity);
    }

    @Override
    public Type getCatType() {
        IPet p = this.getPet();
        if (p instanceof IOcelotPet) {
            return ((IOcelotPet) p).getCatType();
        }
        return null;
    }

    @Override
    public void setCatType(Type type) {
        /*Pet p = this.getPet();
        if (p instanceof OcelotPet) {
            ((OcelotPet) p).setCatType(type);
        }*/
    }

    @Override
    public boolean isSitting() {
        return false;
    }

    @Override
    public void setSitting(boolean b) {
        // Doesn't apply to Pets
    }
}