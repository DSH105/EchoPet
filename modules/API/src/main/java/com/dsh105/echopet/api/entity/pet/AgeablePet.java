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

package com.dsh105.echopet.api.entity.pet;

import com.dsh105.echopet.api.entity.attribute.AttributeGetter;
import com.dsh105.echopet.api.entity.attribute.AttributeSetter;
import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.entitypet.EntityAgeablePet;
import com.dsh105.echopet.bridge.entity.AgeableEntityBridge;

public interface AgeablePet<T extends AgeableEntityBridge, S extends EntityAgeablePet> extends Pet<T, S> {

    @AttributeSetter(Attributes.Attribute.BABY)
    void setBaby(boolean flag);

    @AttributeGetter(Attributes.Attribute.BABY)
    boolean isBaby();
}