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
import com.dsh105.echopet.api.entity.nms.type.EntityCreeperPet;
import com.dsh105.echopet.api.entity.pet.PetImpl;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.CREEPER, width = 0.6F, height = 1.9F)
public class CreeperPetImpl extends PetImpl implements CreeperPet {

    boolean powered;
    boolean ignited;

    public CreeperPetImpl(Player owner) {
        super(owner);
    }

    @AttributeHandler(data = PetData.POWER)
    @Override
    public void setPowered(boolean flag) {
        ((EntityCreeperPet) getEntityPet()).setPowered(flag);
        this.powered = flag;
    }

    @AttributeHandler(data = PetData.POWER, getter = true)
    @Override
    public boolean isPowered() {
        return this.powered;
    }

    @Override
    public void setIgnited(boolean flag) {
        ((EntityCreeperPet) getEntityPet()).setIgnited(flag);
        this.ignited = flag;
    }

    @Override
    public boolean isIgnited() {
        return this.ignited;
    }
}
