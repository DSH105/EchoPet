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
import com.dsh105.echopet.api.entity.attribute.AttributeGetter;
import com.dsh105.echopet.api.entity.attribute.AttributeSetter;
import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.entitypet.type.EntityCreeperPet;
import com.dsh105.echopet.api.entity.pet.Hostility;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.bridge.entity.type.CreeperEntityBridge;

@Traits(type = PetType.CREEPER, hositility = Hostility.AGGRESSIVE, width = 0.6F, height = 1.9F, health = 20.0D, attackDamage = 6.0D)
@Size(SizeCategory.TINY)
public interface CreeperPet extends Pet<CreeperEntityBridge, EntityCreeperPet> {

    @AttributeSetter(Attributes.Attribute.POWER)
    void setPowered(boolean flag);

    @AttributeGetter(Attributes.Attribute.POWER)
    boolean isPowered();

    @AttributeSetter(Attributes.Attribute.IGNITION)
    void setIgnited(boolean flag);

    @AttributeGetter(Attributes.Attribute.IGNITION)
    boolean isIgnited();
}