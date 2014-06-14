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

import com.dsh105.echopet.api.entity.pet.AgeablePetImpl;
import com.dsh105.echopet.api.entity.*;
import com.dsh105.echopet.api.entity.nms.type.EntitySheepPet;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.SHEEP, width = 0.9F, height = 1.3F)
public class SheepPetImpl extends AgeablePetImpl implements SheepPet {

    boolean sheared;
    byte color;

    public SheepPetImpl(Player owner) {
        super(owner);
    }

    @AttributeHandler(data = PetData.SHEARED)
    @Override
    public void setSheared(boolean flag) {
        ((EntitySheepPet) getEntityPet()).setSheared(flag);
        this.sheared = flag;
    }

    @AttributeHandler(data = PetData.SHEARED, getter = true)
    @Override
    public boolean isSheared() {
        return this.sheared;
    }

    @AttributeHandler(dataType = PetData.Type.COLOUR)
    @Override
    public DyeColor getColor() {
        return org.bukkit.DyeColor.getByWoolData(color);
    }

    @AttributeHandler(dataType = PetData.Type.COLOUR)
    @Override
    public void setColor(DyeColor c) {
        ((EntitySheepPet) getEntityPet()).setColor(c.getWoolData());
        this.color = c.getWoolData();
    }

    @Override
    public byte getColorByte() {
        return color;
    }

    @Override
    public void setColor(byte b) {
        ((EntitySheepPet) getEntityPet()).setColor(b);
        this.color = b;
    }

}
