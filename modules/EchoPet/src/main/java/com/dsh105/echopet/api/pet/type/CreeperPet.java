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
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityCreeperPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ICreeperPet;
import org.bukkit.entity.Player;

import java.util.UUID;

@EntityPetType(petType = PetType.CREEPER)
public class CreeperPet extends Pet implements ICreeperPet {

    boolean powered;
    boolean ignited;

    public CreeperPet(Player owner) {
        super(owner);
    }

    @Override
    public void setPowered(boolean flag) {
        ((IEntityCreeperPet) getEntityPet()).setPowered(flag);
        this.powered = flag;
    }

    @Override
    public boolean isPowered() {
        return this.powered;
    }

    @Override
    public void setIgnited(boolean flag) {
        ((IEntityCreeperPet) getEntityPet()).setIgnited(flag);
        this.ignited = flag;
    }

    @Override
    public boolean isIgnited() {
        return this.ignited;
    }
}
