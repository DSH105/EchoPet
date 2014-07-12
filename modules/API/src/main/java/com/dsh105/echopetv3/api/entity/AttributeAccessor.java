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

package com.dsh105.echopetv3.api.entity;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.echopetv3.api.entity.pet.Pet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

// TODO: Some caching here for performance
public class AttributeAccessor {

    public static <T extends Pet> ArrayList<AttributeHandler> attributeGetters(Class<T> petClass) {
        return attributeHandlers(petClass, true);
    }

    public static <T extends Pet> ArrayList<AttributeHandler> attributeSetters(Class<T> petClass) {
        return attributeHandlers(petClass, false);
    }

    public static <T extends Pet> ArrayList<AttributeHandler> attributeHandlers(Class<T> petClass, boolean getter) {
        ArrayList<AttributeHandler> handlers = new ArrayList<>();
        for (AttributeHandler attributeHandler : attributeHandlers(petClass)) {
            if (getter == attributeHandler.getter()) {
                handlers.add(attributeHandler);
            }
        }
        return handlers;
    }

    public static <T extends Pet> ArrayList<AttributeHandler> attributeHandlers(Class<T> petClass) {
        return new ArrayList<>(attributeMethodHandlers(petClass).keySet());
    }

    public static <T extends Pet> HashMap<AttributeHandler, Method> attributeMethodGetters(Class<T> petClass) {
        return attributeMethodHandlers(petClass, true);
    }

    public static <T extends Pet> HashMap<AttributeHandler, Method> attributeMethodSetters(Class<T> petClass) {
        return attributeMethodHandlers(petClass, false);
    }

    public static <T extends Pet> HashMap<AttributeHandler, Method> attributeMethodHandlers(Class<T> petClass, boolean getter) {
        HashMap<AttributeHandler, Method> handlers = new HashMap<>();
        for (Map.Entry<AttributeHandler, Method> entry : attributeMethodHandlers(petClass).entrySet()) {
            if (getter == entry.getKey().getter()) {
                handlers.put(entry.getKey(), entry.getValue());
            }
        }
        return handlers;
    }


    public static <T extends Pet> HashMap<AttributeHandler, Method> attributeMethodHandlers(Class<T> petClass) {
        List<Class<? super T>> superClasses = new ArrayList<>();
        Class<? super T> superClass = petClass;
        try {
            while (superClass != Class.forName("com.dsh105.echopetv3.api.entity.pet.PetBase")) {
                superClasses.add(superClass);
                superClass = superClass.getSuperclass();
            }
        } catch (ClassNotFoundException e) {
            // Uh-oh. Something bad happened
            e.printStackTrace();
        }

        HashMap<AttributeHandler, Method> methodHandlers = new HashMap<>();

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
            if ((handler.data() == PetData.DEFAULT && petData.isType(handler.dataType())) || handler.data() == petData) {
                try {
                    Object value = handler.dataType() == PetData.Type.BOOLEAN ? true : petData.getTypeToObjectMap().get(handler.dataType());
                    if (value != null) {
                        entry.getValue().invoke(pet, value);
                        break;
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    // Uh-oh. Something bad happened
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setDataValue(Pet pet, PetData petData, Object value) {
        for (Map.Entry<AttributeHandler, Method> entry : attributeMethodSetters(pet.getClass()).entrySet()) {
            if ((entry.getKey().data() == PetData.DEFAULT && petData.isType(entry.getKey().dataType())) || entry.getKey().data() == petData) {
                try {
                    if (value != null) {
                        Class<?> returnType = entry.getValue().getReturnType();
                        Object valueToSet = value;
                        if (!returnType.getClass().isAssignableFrom(valueToSet.getClass())) {
                            if (Enum.class.isAssignableFrom(returnType)) {
                                if (GeneralUtil.isEnumType((Class<Enum>) returnType, valueToSet.toString().toUpperCase())) {
                                    valueToSet = Enum.valueOf((Class<Enum>) returnType, value.toString().toUpperCase());
                                }
                            }
                        }
                        entry.getValue().invoke(pet, valueToSet);
                        break;
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    // Uh-oh. Something bad happened
                    e.printStackTrace();
                }
            }
        }
    }

    public static void invertDataValue(Pet pet, PetData petData) {
        setDataValue(pet, petData, !getActiveDataValues(pet).contains(petData));
    }

    public static <T extends Pet> ArrayList<PetData> getRegisteredData(Class<T> petClass) {
        ArrayList<PetData> dataList = new ArrayList<>();
        for (AttributeHandler handler : attributeHandlers(petClass)) {
            if (handler.data() == PetData.DEFAULT) {
                dataList.addAll(PetData.allOfType(handler.dataType()));
            } else {
                dataList.add(handler.data());
            }
        }
        return dataList;
    }

    public static List<PetData> getActiveDataValues(Pet pet) {
        List<PetData> activeData = new ArrayList<>();
        for (Map.Entry<AttributeHandler, Method> entry : attributeMethodGetters(pet.getClass()).entrySet()) {
            AttributeHandler handler = entry.getKey();
            //Object result = entry.getValue().invoke(pet);

            if (handler.data() == PetData.DEFAULT) {
                Collections.addAll(activeData, PetData.allOfType(handler.dataType()).toArray(new PetData[0]));
            } else {
                activeData.add(handler.data());
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
                if ((handler.data() == PetData.DEFAULT && petData.isType(handler.dataType())) || handler.data() == petData) {
                    types.add(petType);
                }
            }
        }
        return types;
    }
}