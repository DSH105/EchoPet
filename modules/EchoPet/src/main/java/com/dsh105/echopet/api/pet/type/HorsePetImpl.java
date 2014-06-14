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
import com.dsh105.echopet.compat.api.entity.nms.type.EntityHorsePet;
import com.dsh105.echopet.compat.api.entity.pet.type.HorsePet;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.HORSE, width = 1.4F, height = 1.6F)
public class HorsePetImpl extends AgeablePetImpl implements HorsePet {

    HorseType horseType;
    HorseVariant variant;
    HorseMarking marking;
    HorseArmour armour;
    boolean chested = false;
    boolean saddle = false;

    public HorsePetImpl(Player owner) {
        super(owner);
    }

    @AttributeHandler(dataType = PetData.Type.HORSE_TYPE)
    @Override
    public void setHorseType(HorseType type) {
        ((EntityHorsePet) getEntityPet()).setType(type);
        this.horseType = type;
    }

    @AttributeHandler(dataType = PetData.Type.HORSE_VARIANT)
    @Override
    public void setVariant(HorseVariant variant) {
        ((EntityHorsePet) getEntityPet()).setVariant(variant, getMarking());
        this.variant = variant;
    }

    @AttributeHandler(dataType = PetData.Type.HORSE_MARKING)
    @Override
    public void setMarking(HorseMarking marking) {
        ((EntityHorsePet) getEntityPet()).setVariant(getVariant(), marking);
        this.marking = marking;
    }

    @AttributeHandler(dataType = PetData.Type.HORSE_ARMOUR)
    @Override
    public void setArmour(HorseArmour armour) {
        ((EntityHorsePet) getEntityPet()).setArmour(armour);
        this.armour = armour;
    }

    @AttributeHandler(data = PetData.SADDLE)
    @Override
    public void setSaddled(boolean flag) {
        ((EntityHorsePet) getEntityPet()).setSaddled(flag);
        this.saddle = flag;
    }

    @AttributeHandler(data = PetData.CHESTED)
    @Override
    public void setChested(boolean flag) {
        ((EntityHorsePet) getEntityPet()).setChested(flag);
        this.chested = flag;
    }

    @AttributeHandler(dataType = PetData.Type.HORSE_TYPE, getter = true)
    @Override
    public HorseType getHorseType() {
        return this.horseType;
    }

    @AttributeHandler(dataType = PetData.Type.HORSE_VARIANT, getter = true)
    @Override
    public HorseVariant getVariant() {
        return this.variant;
    }

    @AttributeHandler(dataType = PetData.Type.HORSE_MARKING, getter = true)
    @Override
    public HorseMarking getMarking() {
        return this.marking;
    }

    @AttributeHandler(dataType = PetData.Type.HORSE_ARMOUR, getter = true)
    @Override
    public HorseArmour getArmour() {
        return this.armour;
    }

    @AttributeHandler(data = PetData.SADDLE, getter = true)
    @Override
    public boolean isSaddled() {
        return this.saddle;
    }

    @AttributeHandler(data = PetData.CHESTED, getter = true)
    @Override
    public boolean isChested() {
        return this.chested;
    }
}