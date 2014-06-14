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

import com.dsh105.echopet.api.pet.AgeablePetImpl;
import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.nms.type.EntityWolfPet;
import com.dsh105.echopet.compat.api.entity.pet.type.WolfPet;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.WOLF, width = 0.6F, height = 0.8F)
public class WolfPetImpl extends AgeablePetImpl implements WolfPet {

    DyeColor collar = DyeColor.RED;
    boolean tamed = false;
    boolean angry = false;

    public WolfPetImpl(Player owner) {
        super(owner);
    }

    @AttributeHandler(dataType = PetData.Type.COLOUR)
    @Override
    public void setCollarColor(DyeColor dc) {
        ((EntityWolfPet) getEntityPet()).setCollarColor(dc);
        this.collar = dc;
    }

    @AttributeHandler(dataType = PetData.Type.COLOUR, getter = true)
    @Override
    public DyeColor getCollarColor() {
        return this.collar;
    }

    @AttributeHandler(data = PetData.TAMED)
    @Override
    public void setTamed(boolean flag) {
        ((EntityWolfPet) getEntityPet()).setTamed(flag);
        this.tamed = flag;
    }

    @AttributeHandler(data = PetData.TAMED, getter = true)
    @Override
    public boolean isTamed() {
        return this.tamed;
    }

    @AttributeHandler(data = PetData.ANGRY)
    @Override
    public void setAngry(boolean flag) {
        ((EntityWolfPet) getEntityPet()).setAngry(flag);
        this.angry = flag;
    }

    @AttributeHandler(data = PetData.ANGRY, getter = true)
    @Override
    public boolean isAngry() {
        return this.angry;
    }
}