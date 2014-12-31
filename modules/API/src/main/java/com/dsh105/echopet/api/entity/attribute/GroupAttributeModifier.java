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

package com.dsh105.echopet.api.entity.attribute;

import com.dsh105.commodus.Affirm;
import com.dsh105.commodus.reflection.Reflection;
import com.dsh105.echopet.api.entity.pet.Pet;

import java.lang.reflect.Method;

public class GroupAttributeModifier<P extends Pet> extends AttributeModifier<P> {

    protected AttributeType type;

    public GroupAttributeModifier(Method setter, Method getter, AttributeType type) {
        super(setter, getter);
        this.type = type;
    }

    public AttributeType getType() {
        return type;
    }

    public EntityAttribute getAttribute(P pet) {
        return (EntityAttribute) Reflection.invoke(getGetter(), pet);
    }

    public void setAttribute(P pet, EntityAttribute value) {
        Method setter = getSetter();
        Affirm.checkInstanceOf(setter.getParameterTypes()[0], value);
        Reflection.invoke(setter, pet, value);
    }
}