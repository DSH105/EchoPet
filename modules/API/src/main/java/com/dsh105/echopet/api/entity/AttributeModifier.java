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

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.google.common.base.Preconditions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AttributeModifier<P extends Pet> {

    private final Map<Boolean, List<Attribute>> attributes;
    private final Map<PetData, Integer> linkedData = new HashMap<>();
    private final List<PetData> applicableDataTypes = new ArrayList<>();
    private final Map<PetData.Type, Integer> linkedDataTypes = new HashMap<>();
    private final Map<Attribute, Attribute> settersOf = new HashMap<>();

    private PetType petType;
    private Class<P> petClass;

    protected AttributeModifier(PetType petType) {
        this.petType = petType;
        this.petClass = (Class<P>) petType.getPetClass();
        attributes = initialise();
    }

    private Map<Boolean, List<Attribute>> initialise() {
        HashMap<Boolean, List<Attribute>> petAttributeMap = new HashMap<>();
        List<Attribute> getters = new ArrayList<>();
        List<Attribute> setters = new ArrayList<>();

        List<Class<? super P>> superClasses = new ArrayList<>();
        Class<? super P> superClass = petClass;
        try {
            while (superClass != Class.forName("com.dsh105.echopet.api.entity.pet.PetBase")) {
                superClasses.add(superClass);
                superClass = superClass.getSuperclass();
            }
        } catch (ClassNotFoundException e) {
            // Uh-oh. Something bad happened
            e.printStackTrace();
        }


        for (Class<? super P> c : superClasses) {
            for (Method candidate : c.getDeclaredMethods()) {
                AttributeHandler attributeHandler = candidate.getAnnotation(AttributeHandler.class);
                if (attributeHandler != null) {
                    Attribute attribute = new Attribute(attributeHandler, candidate);
                    if (attributeHandler.getter()) {
                        getters.add(attribute);
                    } else {
                        setters.add(attribute);
                    }
                }
            }
        }

        for (Attribute getter : getters) {
            for (Attribute setterCandidate : setters) {
                if (getter.getData() == PetData.DEFAULT && setterCandidate.getData() == PetData.DEFAULT) {
                    if (!getter.getDataType().equals(setterCandidate.getDataType())) {
                        continue;
                    }
                }

                if (!getter.getDataType().equals(setterCandidate.getDataType())) {
                    continue;
                }

                settersOf.put(getter, setterCandidate);
            }

            if (getter.getData() == PetData.DEFAULT) {
                applicableDataTypes.addAll(PetData.allOfType(getter.getDataType()));
            } else {
                applicableDataTypes.add(getter.getData());
            }
        }

        petAttributeMap.put(true, Collections.unmodifiableList(getters));
        petAttributeMap.put(false, Collections.unmodifiableList(setters));

        return petAttributeMap;
    }

    public PetType getPetType() {
        return petType;
    }

    public Class<P> getPetClass() {
        return petClass;
    }

    public List<Attribute> getAttributeGetters() {
        return getAttributeHandlers(AttributeType.GETTER);
    }

    public List<Attribute> getAttributeSetters() {
        return getAttributeHandlers(AttributeType.SETTER);
    }

    public List<Attribute> getAttributeHandlers(AttributeType attributeType) {
        return Collections.unmodifiableList(this.attributes.get(attributeType.result));
    }

    public void setDataValue(P pet, PetData petData) {
        Attribute attribute = findAttribute(petData, AttributeType.SETTER);

        try {
            Object value = attribute.getDataType() == PetData.Type.BOOLEAN ? true : petData.getTypeToObjectMap().get(attribute.getDataType());
            if (value != null) {
                attribute.getMethod().invoke(pet, value);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            // Uh-oh. Something bad happened
            e.printStackTrace();
        }
    }

    public void setDataValue(P pet, PetData petData, Object value) {
        Preconditions.checkNotNull(value, "Value to set must not be null.");

        Attribute attribute = findAttribute(petData, AttributeType.SETTER);

        try {
            Class<?> paramType = attribute.getMethod().getParameterTypes()[0];
            Object valueToSet = value;

            valueResolution:
            {
                if (!paramType.getClass().isAssignableFrom(valueToSet.getClass())) {
                    if (Enum.class.isAssignableFrom(paramType)) {
                        if (GeneralUtil.isEnumType((Class<Enum>) paramType, valueToSet.toString().toUpperCase())) {
                            valueToSet = Enum.valueOf((Class<Enum>) paramType, value.toString().toUpperCase());
                        }
                    }
                }
            }
            attribute.getMethod().invoke(pet, valueToSet);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // Uh-oh. Something bad happened
            e.printStackTrace();
        }
    }

    public Object getDataValue(P pet, PetData petData) {
        Attribute attribute = findAttribute(petData, AttributeType.GETTER);

        try {
            return attribute.getMethod().invoke(pet);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getDataValue(P pet, PetData.Type petDataType) {
        Attribute attribute = findAttribute(petDataType, AttributeType.GETTER);

        try {
            return attribute.getMethod().invoke(pet);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void invertDataValue(P pet, PetData petData) {
        setDataValue(pet, petData, !getActiveDataValues(pet).contains(petData));
    }

    public List<PetData> getActiveDataValues(P pet) {
        List<PetData> activeData = new ArrayList<>();
        for (Attribute attribute : getAttributeGetters()) {
            if (attribute.getData() == PetData.DEFAULT) {
                for (PetData petData : PetData.valid()) {
                    if (!petData.getTypeToObjectMap().isEmpty()) {
                        if (attribute.getMethod().getReturnType().isAssignableFrom(petData.getTypeToObjectMap().values().toArray(new Object[0])[0].getClass())) {
                            // this may look silly, but the method actually checks against the pet data type in this situation
                            // thus, it may return a different value
                            if (getDataValue(pet, petData) == petData.getTypeToObjectMap().get(attribute.getDataType())) {
                                activeData.add(petData);
                            }
                        }
                    }
                }
            } else {
                activeData.add(attribute.getData());
            }
        }
        return Collections.unmodifiableList(activeData);
    }

    public List<PetData> getApplicableDataTypes() {
        return Collections.unmodifiableList(applicableDataTypes);
    }

    private Attribute findAttribute(PetData petData, AttributeType attributeType) {
        Attribute attribute = null;
        if (linkedData.containsKey(petData)) {
            attribute = getAttributeSetters().get(linkedData.get(petData));
        }

        if (attribute == null) {
            for (Attribute candidate : (attributeType == AttributeType.GETTER ? getAttributeGetters() : getAttributeSetters())) {
                if ((candidate.getData() == PetData.DEFAULT && petData.isType(candidate.getDataType())) || candidate.getData() == petData) {
                    attribute = candidate;
                    linkedData.put(petData, getAttributeSetters().indexOf(candidate));
                    break;
                }
            }
        }

        if (attribute == null) {
            throw new IllegalStateException("Attribute setter was not found for: PetData#" + petData.toString());
        }
        return attribute;
    }

    private Attribute findAttribute(PetData.Type petDataType, AttributeType attributeType) {
        Attribute attribute = null;
        if (linkedDataTypes.containsKey(petDataType)) {
            attribute = getAttributeSetters().get(linkedDataTypes.get(petDataType));
        }

        if (attribute == null) {
            for (Attribute candidate : (attributeType == AttributeType.GETTER ? getAttributeGetters() : getAttributeSetters())) {
                if (candidate.getDataType() == petDataType) {
                    attribute = candidate;
                    linkedDataTypes.put(petDataType, getAttributeSetters().indexOf(candidate));
                    break;
                }
            }
        }

        if (attribute == null) {
            throw new IllegalStateException("Attribute setter was not found for: PetData.Type#" + petDataType.toString());
        }
        return attribute;
    }

    public enum AttributeType {
        GETTER(true), SETTER(false);

        private boolean result;

        AttributeType(boolean result) {
            this.result = result;
        }

        public boolean isResult() {
            return result;
        }
    }
}