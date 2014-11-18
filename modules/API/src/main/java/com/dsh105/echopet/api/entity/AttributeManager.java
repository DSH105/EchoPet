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

package com.dsh105.echopet.api.entity;

import com.dsh105.echopet.api.entity.pet.Pet;

import java.util.HashMap;
import java.util.Map;

public class AttributeManager {

    private static Map<Class<? extends Pet>, AttributeModifier<?>> attributeMap = initialise();

    static {
        initialise();
    }

    private static Map<Class<? extends Pet>, AttributeModifier<?>> initialise() {
        Map<Class<? extends Pet>, AttributeModifier<?>> attributeMap = new HashMap<>();
        for (PetType petType : PetType.values()) {
            attributeMap.put(petType.getPetClass(), new AttributeModifier<>(petType));
        }
        return attributeMap;
    }

    public static <P extends Pet> AttributeModifier<P> getModifier(P pet) {
        return (AttributeModifier<P>) getModifier(pet.getType());
    }

    public static AttributeModifier<?> getModifier(PetType petType) {
        return attributeMap.get(petType.getPetClass());
    }
}