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

import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.pet.Pet;

import java.util.HashMap;
import java.util.Map;

public class AttributeManager {

    private static Map<Class<? extends Pet>, PetAttributeModifier<?>> ATTRIBUTE_MAP = initialise();

    private static Map<Class<? extends Pet>, PetAttributeModifier<?>> initialise() {
        Map<Class<? extends Pet>, PetAttributeModifier<?>> attributeModifierMap = new HashMap<>();
        for (PetType petType : PetType.values()) {
            attributeModifierMap.put(petType.getPetClass(), new PetAttributeModifier(petType));
        }
        return attributeModifierMap;
    }

    public static <P extends Pet> PetAttributeModifier<P> getModifier(Class<P> petClass) {
        return (PetAttributeModifier<P>) ATTRIBUTE_MAP.get(petClass);
    }

    public static PetAttributeModifier<?> getModifier(PetType petType) {
        return getModifier(petType.getPetClass());
    }

    public static <P extends Pet> PetAttributeModifier<P> getModifier(P pet) {
        return (PetAttributeModifier<P>) getModifier(pet.getType());
    }
}