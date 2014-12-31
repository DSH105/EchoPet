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

import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.Size;
import com.dsh105.echopet.api.entity.SizeCategory;
import com.dsh105.echopet.api.entity.Traits;
import com.dsh105.echopet.api.entity.attribute.*;
import com.dsh105.echopet.api.entity.entitypet.type.EntityHorsePet;
import com.dsh105.echopet.api.entity.pet.AgeablePet;
import com.dsh105.echopet.api.entity.pet.Hostility;
import com.dsh105.echopet.bridge.entity.type.HorseEntityBridge;

@Traits(type = PetType.HORSE, hositility = Hostility.PASSIVE, width = 1.4F, height = 1.6F, health = 30.0D, attackDamage = 4.0D)
@Size(SizeCategory.REGULAR)
public interface HorsePet extends AgeablePet<HorseEntityBridge, EntityHorsePet> {

    @GroupAttributeSetter(AttributeType.HORSE_VARIANT)
    void setVariant(Attributes.HorseVariant variant);

    @GroupAttributeGetter(AttributeType.HORSE_VARIANT)
    Attributes.HorseVariant getVariant();

    @GroupAttributeSetter(AttributeType.HORSE_COLOUR)
    void setColor(Attributes.HorseColor color);

    @GroupAttributeGetter(AttributeType.HORSE_COLOUR)
    Attributes.HorseColor getColor();

    @GroupAttributeSetter(AttributeType.HORSE_STYLE)
    void setStyle(Attributes.HorseStyle style);

    @GroupAttributeGetter(AttributeType.HORSE_STYLE)
    Attributes.HorseStyle getStyle();

    @GroupAttributeSetter(AttributeType.HORSE_ARMOUR)
    void setArmour(Attributes.HorseArmour armour);

    @GroupAttributeGetter(AttributeType.HORSE_ARMOUR)
    Attributes.HorseArmour getArmour();

    @AttributeSetter(Attributes.Attribute.SADDLE)
    void setSaddled(boolean flag);

    @AttributeGetter(Attributes.Attribute.SADDLE)
    boolean isSaddled();

    @AttributeSetter(Attributes.Attribute.CHESTED)
    void setChested(boolean flag);

    @AttributeGetter(Attributes.Attribute.CHESTED)
    boolean isChested();
}