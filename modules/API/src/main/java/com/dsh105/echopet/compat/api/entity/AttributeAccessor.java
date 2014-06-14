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

package com.dsh105.echopet.compat.api.entity;

import com.dsh105.echopet.compat.api.entity.pet.Pet;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AttributeAccessor {

    public static <T extends Pet> ArrayList<AttributeHandler> attributeGetters(Class<T> petClass) {
        return attributeHandlers(petClass, true);
    }

    public static <T extends Pet> ArrayList<AttributeHandler> attributeSetters(Class<T> petClass) {
        return attributeHandlers(petClass, false);
    }

    public static <T extends Pet> ArrayList<AttributeHandler> attributeHandlers(Class<T> petClass, boolean getter) {
        ArrayList<AttributeHandler> handlers = new ArrayList<AttributeHandler>();
        for (AttributeHandler attributeHandler : attributeHandlers(petClass)) {
            if (getter == attributeHandler.getter()) {
                handlers.add(attributeHandler);
            }
        }
        return handlers;
    }

    public static <T extends Pet> ArrayList<AttributeHandler> attributeHandlers(Class<T> petClass) {
        return new ArrayList<AttributeHandler>(attributeMethodHandlers(petClass).keySet());
    }

    public static <T extends Pet> HashMap<AttributeHandler, Method> attributeMethodGetters(Class<T> petClass) {
        return attributeMethodHandlers(petClass, true);
    }

    public static <T extends Pet> HashMap<AttributeHandler, Method> attributeMethodSetters(Class<T> petClass) {
        return attributeMethodHandlers(petClass, false);
    }

    public static <T extends Pet> HashMap<AttributeHandler, Method> attributeMethodHandlers(Class<T> petClass, boolean getter) {
        HashMap<AttributeHandler, Method> handlers = new HashMap<AttributeHandler, Method>();
        for (Map.Entry<AttributeHandler, Method> entry : attributeMethodHandlers(petClass).entrySet()) {
            if (getter == entry.getKey().getter()) {
                handlers.put(entry.getKey(), entry.getValue());
            }
        }
        return handlers;
    }


    public static <T extends Pet> HashMap<AttributeHandler, Method> attributeMethodHandlers(Class<T> petClass) {
        List<Class<? super T>> superClasses = new ArrayList<Class<? super T>>();
        Class<? super T> superClass = petClass;
        while (superClass != ReflectionUtil.getClass("com.dsh105.echopet.api.pet.PetImpl")) {
            superClasses.add(superClass);
            superClass = superClass.getSuperclass();
        }

        HashMap<AttributeHandler, Method> methodHandlers = new HashMap<AttributeHandler, Method>();

        for (Class<? super T> c : superClasses) {
            for (Method method : c.getDeclaredMethods()) {
                AttributeHandler attributeHandler = method.getAnnotation(AttributeHandler.class);
                if (attributeHandler != null) {
                    methodHandlers.put(attributeHandler, method);
                }
            }
        }
        return methodHandlers;
    }

    public static void setDataValue(Pet pet, PetData petData) {
        for (Map.Entry<AttributeHandler, Method> entry : attributeMethodSetters(pet.getClass()).entrySet()) {
            AttributeHandler handler = entry.getKey();
            if (handler.data() == null || Arrays.asList(handler.data()).contains(petData)) {
                try {
                    Object value = handler.dataType() == PetData.Type.BOOLEAN ? true : petData.getTypeToObjectMap().get(handler.dataType());
                    if (value != null) {
                        entry.getValue().invoke(pet, value);
                        break;
                    }
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            }
        }
    }

    public static void setDataValue(Pet pet, PetData petData, Object value) {
        for (Map.Entry<AttributeHandler, Method> entry : attributeMethodSetters(pet.getClass()).entrySet()) {
            if ((entry.getKey().data() == null && petData.isType(entry.getKey().dataType())) || Arrays.asList(entry.getKey().data()).contains(petData)) {
                try {
                    if (value != null && entry.getValue().getReturnType().getClass().isAssignableFrom(value.getClass())) {
                        entry.getValue().invoke(pet, value);
                        break;
                    }
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            }
        }
    }

    public static void invertDataValue(Pet pet, PetData petData) {
        setDataValue(pet, petData, !pet.getActiveData().contains(petData));
    }

    public static <T extends Pet> ArrayList<PetData> getRegisteredData(Class<T> petClass) {
        ArrayList<PetData> dataList = new ArrayList<PetData>();
        for (AttributeHandler handler : attributeHandlers(petClass)) {
            if (handler.data() == null) {
                dataList.addAll(PetData.allOfType(handler.dataType()));
            } else {
                for (PetData data : handler.data()) {
                    dataList.add(data);
                }
            }
        }
        return dataList;
    }

    public static ArrayList<PetData> getActiveData(Pet pet) {
        ArrayList<PetData> activeData = new ArrayList<PetData>();
        for (Map.Entry<AttributeHandler, Method> entry : attributeMethodGetters(pet.getClass()).entrySet()) {
            try {
                AttributeHandler handler = entry.getKey();
                Method attributeMethod = entry.getValue();
                if (attributeMethod.getReturnType() == Boolean.class) {
                    if ((Boolean) entry.getValue().invoke(pet)) {
                        for (PetData data : handler.data()) {
                            if (data.isType(handler.dataType())) {
                                activeData.add(data);
                            }
                        }
                    }
                } else if (handler.data() == null) {
                    activeData.addAll(PetData.allOfType(handler.dataType()));
                }
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return activeData;
    }

    public static ArrayList<AttributeHandler> attributeGetters(PetType petType) {
        return attributeHandlers(petType, true);
    }

    public static ArrayList<AttributeHandler> attributeSetters(PetType petType) {
        return attributeHandlers(petType, false);
    }

    public static ArrayList<AttributeHandler> attributeHandlers(PetType petType, boolean getter) {
        return attributeHandlers(petType.getPetClass(), getter);
    }

    public static ArrayList<AttributeHandler> attributeHandlers(PetType petType) {
        return attributeHandlers(petType.getPetClass());
    }

    public static HashMap<AttributeHandler, Method> attributeMethodGetters(PetType petType) {
        return attributeMethodHandlers(petType, true);
    }

    public static HashMap<AttributeHandler, Method> attributeMethodSetters(PetType petType) {
        return attributeMethodHandlers(petType, false);
    }

    public static HashMap<AttributeHandler, Method> attributeMethodHandlers(PetType petType, boolean getter) {
        return attributeMethodHandlers(petType.getPetClass(), getter);
    }


    public static HashMap<AttributeHandler, Method> attributeMethodHandlers(PetType petType) {
        return attributeMethodHandlers(petType.getPetClass());
    }

    public static ArrayList<PetData> getRegisteredData(PetType petType) {
        return getRegisteredData(petType.getPetClass());
    }

    public static ArrayList<PetType> getApplicableTypes(PetData petData) {
        ArrayList<PetType> types = new ArrayList<PetType>();
        for (PetType petType : PetType.values()) {
            for (AttributeHandler handler : attributeGetters(petType)) {
                if (Arrays.asList(handler.data()).contains(petData)) {
                    types.add(petType);
                }
            }
        }
        return types;
    }
}