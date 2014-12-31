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
import com.dsh105.echopet.api.entity.Traits;
import com.dsh105.echopet.api.entity.attribute.*;
import com.dsh105.echopet.api.entity.entitypet.type.EntityWolfPet;
import com.dsh105.echopet.api.entity.pet.AgeablePet;
import com.dsh105.echopet.api.entity.pet.Hostility;
import com.dsh105.echopet.bridge.entity.type.WolfEntityBridge;

@Traits(type = PetType.WOLF, hositility = Hostility.NEUTRAL, width = 0.6F, height = 0.8F, health = 20.0D, attackDamage = 6.0D)
public interface WolfPet extends AgeablePet<WolfEntityBridge, EntityWolfPet> {

    @GroupAttributeSetter(AttributeType.COLOR)
    void setCollarColor(Attributes.Color color);

    @GroupAttributeGetter(AttributeType.COLOR)
    Attributes.Color getCollarColor();

    @AttributeSetter(Attributes.Attribute.TAME)
    void setTamed(boolean flag);

    @AttributeGetter(Attributes.Attribute.TAME)
    boolean isTamed();

    @AttributeSetter(Attributes.Attribute.ANGRY)
    void setAngry(boolean flag);

    @AttributeGetter(Attributes.Attribute.ANGRY)
    boolean isAngry();
}