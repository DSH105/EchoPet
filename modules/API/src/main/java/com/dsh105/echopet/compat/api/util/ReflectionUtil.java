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

package com.dsh105.echopet.compat.api.util;

import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.reflection.utility.CommonReflection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReflectionUtil {

    public static String COMPAT_NMS_PATH = prepareCompatNmsPath();

    public static int MC_VERSION_NUMERIC = Integer.valueOf(getServerVersion().replaceAll("[^0-9]", ""));
    public static int BUKKIT_VERSION_NUMERIC = Integer.valueOf(getBukkitVersion().replaceAll("[^0-9]", ""));

    private static String prepareCompatNmsPath() {
        String versionTag = getServerVersion();
        if (Bukkit.getVersion().contains("Spigot") && versionTag.equals("v1_7_R4") && isSpigot1dot8()) {
            // At least it works
            versionTag = "v1_8_Spigot";
        }
        return "com.dsh105.echopet.compat.nms." + versionTag;
    }

    public static boolean isSpigot1dot8() {
        try {
            // not ideal, but it's the only class needed
            Class.forName("org.spigotmc.ProtocolData");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static Object getEntityHandle(Entity entity) {
        return invokeMethod(getMethod(getCBCClass("entity.CraftEntity"), "getHandle"), entity);
    }

    public static String getServerVersion() {
        return CommonReflection.getVersionTag();
    }

    // Thanks ProtocolLib <3
    public static String getBukkitVersion() {
        Pattern versionPattern = Pattern.compile(".*\\(.*MC.\\s*([a-zA-z0-9\\-\\.]+)\\s*\\)");
        Matcher version = versionPattern.matcher(Bukkit.getServer().getVersion());

        if (version.matches() && version.group(1) != null) {
            return version.group(1);
        } else {
            return "";
        }
    }

    public static boolean isServerMCPC() {
        return Bukkit.getVersion().contains("MCPC-Plus");
    }

    public static String getNMSPackageName() {
        return "net.minecraft.server." + getServerVersion();
    }

    public static String getCBCPackageName() {
        return "org.bukkit.craftbukkit." + getServerVersion();
    }

    /**
     * Class stuff
     */

    public static Class getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            EchoPet.getPlugin().getReflectionLogger().warning("Could not find class: " + name + "!");
            e.printStackTrace();
            return null;
        }
    }

    public static Class getVersionedClass(String classPath) {
        return getClass(COMPAT_NMS_PATH + "." + classPath);
    }

    public static Class getPetNMSClass(String classIdentifier) {
        return getVersionedClass("entity.type.Entity" + classIdentifier + "Pet");
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
            EchoPet.getPlugin().getReflectionLogger().warning("No such field: " + fieldName + "!");
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T getField(Class<?> clazz, String fieldName, Object instance) {
        try {
            return (T) getField(clazz, fieldName).get(instance);
        } catch (IllegalAccessException e) {
            EchoPet.getPlugin().getReflectionLogger().warning("Failed to access field: " + fieldName + "!");
            e.printStackTrace();
            return null;
        }
    }

    public static void setField(Class<?> clazz, String fieldName, Object instance, Object value) {
        try {
            getField(clazz, fieldName).set(instance, value);
        } catch (IllegalAccessException e) {
            EchoPet.getPlugin().getReflectionLogger().warning("Could not set new field value for: " + fieldName);
            e.printStackTrace();
        }
    }

    public static <T> T getField(Field field, Object instance) {
        try {
            return (T) field.get(instance);
        } catch (IllegalAccessException e) {
            EchoPet.getPlugin().getReflectionLogger().warning("Failed to retrieve field: " + field.getName());
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
            EchoPet.getPlugin().getReflectionLogger().warning("No such method: " + methodName + "!");
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T invokeMethod(Method method, Object instance, Object... args) {
        try {
            return (T) method.invoke(instance, args);
        } catch (IllegalAccessException e) {
            EchoPet.getPlugin().getReflectionLogger().warning("Failed to access method: " + method.getName() + "!");
            return null;
        } catch (InvocationTargetException e) {
            EchoPet.getPlugin().getReflectionLogger().warning("Failed to invoke method: " + method.getName() + "!");
            e.printStackTrace();
            return null;
        }
    }
}
