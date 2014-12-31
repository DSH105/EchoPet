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
import com.dsh105.echopet.api.entity.entitypet.type.EntitySheepPet;
import com.dsh105.echopet.api.entity.pet.AgeablePet;
import com.dsh105.echopet.api.entity.pet.Hostility;
import com.dsh105.echopet.bridge.entity.type.SheepEntityBridge;

@Traits(type = PetType.SHEEP, hositility = Hostility.PASSIVE, width = 0.9F, height = 1.3F, health = 8.0D, attackDamage = 3.0D)
public interface SheepPet extends AgeablePet<SheepEntityBridge, EntitySheepPet> {

    @AttributeSetter(Attributes.Attribute.SHEARED)
    void setSheared(boolean flag);

    @AttributeGetter(Attributes.Attribute.SHEARED)
    boolean isSheared();

    @GroupAttributeSetter(AttributeType.COLOR)
    void setColor(Attributes.Color color);

    @GroupAttributeGetter(AttributeType.COLOR)
    Attributes.Color getColor();
}