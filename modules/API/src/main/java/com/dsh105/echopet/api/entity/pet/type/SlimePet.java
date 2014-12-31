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
import com.dsh105.echopet.api.entity.attribute.AttributeType;
import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.attribute.GroupAttributeGetter;
import com.dsh105.echopet.api.entity.attribute.GroupAttributeSetter;
import com.dsh105.echopet.api.entity.entitypet.type.EntitySlimePet;
import com.dsh105.echopet.api.entity.pet.Hostility;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.bridge.entity.type.SlimeEntityBridge;

@Traits(type = PetType.SLIME, hositility = Hostility.AGGRESSIVE, width = 0.6F, height = 0.6F, health = 20.0D, attackDamage = 4.0D)
@Size(SizeCategory.REGULAR)
public interface SlimePet<T extends SlimeEntityBridge, S extends EntitySlimePet> extends Pet<T, S> {

    @GroupAttributeSetter(AttributeType.SLIME_SIZE)
    void setSize(Attributes.SlimeSize size);

    @GroupAttributeGetter(AttributeType.SLIME_SIZE)
    Attributes.SlimeSize getSize();
}