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

package io.github.dsh105.echopet.api.entity.pet.type;

import io.github.dsh105.echopet.api.entity.*;
import io.github.dsh105.echopet.api.entity.nms.IEntityPet;
import io.github.dsh105.echopet.api.entity.nms.type.IEntityHorsePet;
import io.github.dsh105.echopet.api.entity.pet.Pet;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.HORSE)
public class HorsePet extends Pet implements IAgeablePet {

    HorseType horseType;
    HorseVariant variant;
    HorseMarking marking;
    HorseArmour armour;
    boolean baby = false;
    boolean chested = false;
    boolean saddle = false;

    public HorsePet(Player owner) {
        super(owner);
    }

    public HorsePet(String owner, IEntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setHorseType(HorseType type) {
        ((IEntityHorsePet) getEntityPet()).setType(type);
        this.horseType = type;
    }

    public void setVariant(HorseVariant variant, HorseMarking marking) {
        ((IEntityHorsePet) getEntityPet()).setVariant(variant, marking);
        this.variant = variant;
        this.marking = marking;
    }

    public void setArmour(HorseArmour armour) {
        ((IEntityHorsePet) getEntityPet()).setArmour(armour);
        this.armour = armour;
    }

    @Override
    public void setBaby(boolean flag) {
        ((IEntityHorsePet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public void setSaddled(boolean flag) {
        ((IEntityHorsePet) getEntityPet()).setSaddled(flag);
        this.saddle = flag;
    }

    public void setChested(boolean flag) {
        ((IEntityHorsePet) getEntityPet()).setChested(flag);
        this.chested = flag;
    }

    public HorseType getHorseType() {
        return this.horseType;
    }

    public HorseVariant getVariant() {
        return this.variant;
    }

    public HorseMarking getMarking() {
        return this.marking;
    }

    public HorseArmour getArmour() {
        return this.armour;
    }

    @Override
    public boolean isBaby() {
        return this.baby;
    }

    public boolean isSaddled() {
        return this.saddle;
    }

    public boolean isChested() {
        return this.chested;
    }
}