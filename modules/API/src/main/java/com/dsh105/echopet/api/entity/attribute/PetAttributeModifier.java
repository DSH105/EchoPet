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

import java.lang.reflect.Method;
import java.util.*;

public class PetAttributeModifier<P extends Pet> {

    private Map<Attributes.Attribute, TypeAttributeModifier<P>> modifiers = new HashMap<>();
    private Map<AttributeType, GroupAttributeModifier<P>> groupModifiers = new HashMap<>();
    private List<EntityAttribute> validAttributes = new ArrayList<>();
    private Class<P> typeClass;

    protected PetAttributeModifier(PetType petType) {
        this.typeClass = (Class<P>) petType.getPetClass();
        this.prepare();
    }

    private void prepare() {
        // true -> setter, false -> getter
        Map<Attributes.Attribute, Map<Boolean, Method>> modifiers = new HashMap<>();
        Map<AttributeType, Map<Boolean, Method>> groupModifiers = new HashMap<>();

        for (Attributes.Attribute attribute : Attributes.Attribute.values()) {
            modifiers.put(attribute, new HashMap<Boolean, Method>());
        }

        for (AttributeType attributeType : AttributeType.values()) {
            groupModifiers.put(attributeType, new HashMap<Boolean, Method>());
        }

        for (Class<? super P> annotatedClass : getAnnotatedClasses()) {
            for (Method candidate : annotatedClass.getDeclaredMethods()) {
                if (candidate.getAnnotation(AttributeSetter.class) != null) {
                    modifiers.get(candidate.getAnnotation(AttributeSetter.class).value()).put(true, candidate);
                } else if (candidate.getAnnotation(AttributeGetter.class) != null) {
                    modifiers.get(candidate.getAnnotation(AttributeSetter.class).value()).put(false, candidate);
                } else if (candidate.getAnnotation(GroupAttributeSetter.class) != null) {
                    groupModifiers.get(candidate.getAnnotation(GroupAttributeSetter.class).value()).put(true, candidate);
                } else if (candidate.getAnnotation(AttributeSetter.class) != null) {
                    groupModifiers.get(candidate.getAnnotation(GroupAttributeGetter.class).value()).put(false, candidate);
                }
            }
        }

        for (Attributes.Attribute attribute : modifiers.keySet()) {
            Map<Boolean, Method> accessorMap = modifiers.get(attribute);
            this.modifiers.put(attribute, new TypeAttributeModifier<P>(accessorMap.get(true), accessorMap.get(false), attribute));
            this.validAttributes.add(attribute);
        }
        for (AttributeType attributeType : groupModifiers.keySet()) {
            Map<Boolean, Method> accessorMap = groupModifiers.get(attributeType);
            this.groupModifiers.put(attributeType, new GroupAttributeModifier<P>(accessorMap.get(true), accessorMap.get(false), attributeType));

            for (AttributeValue attributeValue : attributeType.getValidValues()) {
                validAttributes.add(attributeValue.getValue());
            }
        }
    }

    private List<Class<? super P>> getAnnotatedClasses() {
        List<Class<? super P>> candidates = new ArrayList<>();
        for (Class<?> petInterface : typeClass.getInterfaces()) {
            candidates.add((Class<P>) petInterface);
            Class<? super P> superClass = (Class<P>) petInterface;
            while (superClass != Pet.class) {
                candidates.add(superClass);
                superClass = superClass.getSuperclass();
            }
        }
        return candidates;
    }

    public TypeAttributeModifier<P> getModifier(Attributes.Attribute attribute) {
        return this.modifiers.get(attribute);
    }

    public GroupAttributeModifier<P> getModifier(AttributeType attributeType) {
        return this.groupModifiers.get(attributeType);
    }

    public boolean isActive(P pet, EntityAttribute entityAttribute) {
        if (entityAttribute instanceof Attributes.Attribute) {
            return getModifier((Attributes.Attribute) entityAttribute).getAttribute(pet);
        }
        return getModifier(AttributeType.getByEnumBridge(entityAttribute.getClass())).getAttribute(pet).equals(entityAttribute);
    }

    public List<EntityAttribute> getValidAttributes() {
        return Collections.unmodifiableList(validAttributes);
    }
}