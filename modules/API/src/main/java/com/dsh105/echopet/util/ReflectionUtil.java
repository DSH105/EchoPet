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

package com.dsh105.echopet.util;

import com.dsh105.commodus.logging.Level;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.reflection.utility.CommonReflection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

    public static Object getEntityHandle(Entity entity) {
        return invokeMethod(getMethod(getCBCClass("entity.CraftEntity"), "getHandle"), entity);
    }

    public static boolean isServerMCPC() {
        return Bukkit.getVersion().contains("MCPC-Plus");
    }

    /**
     * Class stuff
     */

    public static Class getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            EchoPet.LOG.console(Level.WARNING, "Could not find class: " + name + "!");
            e.printStackTrace();
            return null;
        }
    }

    public static Class getNMSClass(String className) {
        return CommonReflection.getMinecraftClass(className);
    }

    public static Class getCBCClass(String className) {
        return CommonReflection.getCraftBukkitClass(className);
    }

    /**
     * Field stuff
     */

    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);

            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            return field;
        } catch (NoSuchFieldException e) {
            EchoPet.LOG.console(Level.WARNING, "No such field: " + fieldName + "!");
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T getField(Class<?> clazz, String fieldName, Object instance) {
        try {
            return (T) getField(clazz, fieldName).get(instance);
        } catch (IllegalAccessException e) {
            EchoPet.LOG.console(Level.WARNING, "Failed to access field: " + fieldName + "!");
            e.printStackTrace();
            return null;
        }
    }

    public static void setField(Class<?> clazz, String fieldName, Object instance, Object value) {
        try {
            getField(clazz, fieldName).set(instance, value);
        } catch (IllegalAccessException e) {
            EchoPet.LOG.console(Level.WARNING, "Could not set new field value for: " + fieldName);
            e.printStackTrace();
        }
    }

    public static <T> T getField(Field field, Object instance) {
        try {
            return (T) field.get(instance);
        } catch (IllegalAccessException e) {
            EchoPet.LOG.console(Level.WARNING, "Failed to retrieve field: " + field.getName());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method stuff
     */

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        try {
            return clazz.getDeclaredMethod(methodName, params);
        } catch (NoSuchMethodException e) {
            EchoPet.LOG.console(Level.WARNING, "No such method: " + methodName + "!");
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T invokeMethod(Method method, Object instance, Object... args) {
        try {
            return (T) method.invoke(instance, args);
        } catch (IllegalAccessException e) {
            EchoPet.LOG.console(Level.WARNING, "Failed to access method: " + method.getName() + "!");
            return null;
        } catch (InvocationTargetException e) {
            EchoPet.LOG.console(Level.WARNING, "Failed to invoke method: " + method.getName() + "!");
            e.printStackTrace();
            return null;
        }
    }
}
