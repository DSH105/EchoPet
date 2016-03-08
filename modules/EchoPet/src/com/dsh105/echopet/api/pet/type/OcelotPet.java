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


import com.dsh105.echopet.api.pet.Pet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityOcelotPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IOcelotPet;
import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.OCELOT)
public class OcelotPet extends Pet implements IOcelotPet {

    boolean baby;
    Type type = Type.WILD_OCELOT;

    public OcelotPet(Player owner) {
        super(owner);
    }

    @Override
    public void setBaby(boolean flag) {
        ((IEntityOcelotPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    @Override
    public boolean isBaby() {
        return this.baby;
    }

    @Override
    public Type getCatType() {
        return type;
    }

    @Override
    public void setCatType(Type t) {
        setCatType(t.getId());
        this.type = t;
    }

    @Override
    public void setCatType(int i) {
        ((IEntityOcelotPet) getEntityPet()).setCatType(i);
        this.type = Type.getType(i);
    }
}