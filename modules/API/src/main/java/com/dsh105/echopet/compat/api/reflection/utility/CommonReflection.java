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

/*
 * This file is part of HoloAPI.
 *
 * HoloAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HoloAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HoloAPI.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.api.reflection.utility;

import com.dsh105.commodus.logging.Level;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.reflection.*;
import com.google.common.base.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Allows us to access several classes and yet be compatible with MCPC+, Bukkit and Spigot
 */
public class CommonReflection {

    private static ClassHandler DEFAULT_HANDLER;
    private static ClassPackageMapper NMS_HANDLER;
    private static ClassPackageMapper CRAFTBUKKIT_HANDLER;

    /**
     * The Minecraft package
     */
    private static String MINECARFT_PACKAGE;

    private static String MINECARFT_PACKAGE_PREFIX = "net.minecraft.server";

    private static String FORGE_ENTITY_PACKAGE = "net.minecraft.entity";

    /**
     * The Craftbukkit package
     */
    private static String CRAFTBUKKIT_PACKAGE;

    /**
     * The Version tag
     */
    private static String VERSION_TAG = "";

    /**
     * Pattern
     */
    private static final Pattern PACKAGE_VERSION_MATCHER = Pattern.compile(".*\\.(v\\d+_\\d+_\\w*\\d+)");

    private CommonReflection() {
        super();
    }

    /**
     * Returns the version-tag
     *
     * @return
     */
    public static String getVersionTag() {
        if (VERSION_TAG == null)
            initializePackageNames();
        return VERSION_TAG;
    }

    /**
     * Returns the Minecraft package
     *
     * @return
     */
    public static String getMinecraftPackage() {
        if (MINECARFT_PACKAGE == null)
            initializePackageNames();
        return MINECARFT_PACKAGE;
    }

    /**
     * Returns the Craftbukkit package
     *
     * @return
     */
    public static String getCraftBukkitPackage() {
        if (CRAFTBUKKIT_PACKAGE == null)
            initializePackageNames();
        return CRAFTBUKKIT_PACKAGE;
    }

    /**
     * Initializes the package names.
     *
     * @return
     */
    protected static void initializePackageNames() {

        Server bukkitServer = Bukkit.getServer();

        if (bukkitServer != null) {

            // Handle CraftBukkit package
            Class<?> craftServerClass = bukkitServer.getClass();
            CRAFTBUKKIT_PACKAGE = trimPackageName(craftServerClass.getCanonicalName());

            Matcher matcher = PACKAGE_VERSION_MATCHER.matcher(CRAFTBUKKIT_PACKAGE);

            if (matcher.matches()) {
                VERSION_TAG = matcher.group(1);
            }

            // Handle NMS-Package
            Class<?> craftEntityClass = getCraftEntityClass();
            MethodAccessor<Object> getHandle = ClassTemplate.create(craftEntityClass).getMethod("getHandle");
            MINECARFT_PACKAGE = trimPackageName(getHandle.getReturnType().getCanonicalName());

            if (!MINECARFT_PACKAGE.startsWith(MINECARFT_PACKAGE_PREFIX)) {

                // We're dealing with a Forge server.
                // Credits to ProtocolLib for this method
                if (MINECARFT_PACKAGE.equals(FORGE_ENTITY_PACKAGE)) {

                    // Hack for MCPC+ for 1.7.x
                    try {
                        if (VERSION_TAG == null || VERSION_TAG == "") {
                            if (getClass("org.bukkit.plugin.java.PluginClassLoader") != null) {
                                ClassTemplate pluginClassLoader = ClassTemplate.create(getClass("org.bukkit.plugin.java.PluginClassLoader"));
                                MethodAccessor<String> getNativeVersion = pluginClassLoader.getMethod("getNativeVersion");
                                if (getNativeVersion != null) {
                                    VERSION_TAG = getNativeVersion.invoke(null);
                                }
                            }
                        }
                    } catch (Exception e) {
                        if (VERSION_TAG == null)
                            EchoPet.LOG.console(Level.WARNING, "Version tag is null and it appears the server is modded but does not contain the expected method(s)! HoloAPI may not work correctly!");
                    }

                    MINECARFT_PACKAGE = combine(MINECARFT_PACKAGE_PREFIX, VERSION_TAG);
                } else {
                    MINECARFT_PACKAGE_PREFIX = MINECARFT_PACKAGE;
                }
            }

        } else {
            throw new IllegalStateException("Failed to find Bukkit!");
        }
    }

    /**
     * "Fixes" the given package name. (used to parse the version-tag)
     *
     * @param packageName
     * @return
     */
    private static String trimPackageName(String packageName) {
        int index = packageName.lastIndexOf('.');

        if (index > 0) {
            return packageName.substring(0, index);
        } else {
            return "<unknown>";
        }
    }

    private static String combine(String packageName, String className) {
        if (Strings.isNullOrEmpty(packageName))
            return className;
        if (Strings.isNullOrEmpty(className))
            return packageName;
        return packageName + "." + className;
    }

    /**
     * Returns the Default ClassHandler.
     * <p/>
     * The ClassHandler is a utility class which allows us to easily access MCPC+ classes.
     * (Because they are remapped)
     *
     * @return
     */
    private static ClassHandler getDefaultClassHandler() {
        if (DEFAULT_HANDLER == null) {
            try {
                return DEFAULT_HANDLER = new RemappedClassHandler().initialize();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (UnsupportedOperationException e) {
                DEFAULT_HANDLER = ClassHandler.fromClassLoader();
            }
        }
        return DEFAULT_HANDLER;
    }

    /**
     * Returns a class with the given name
     *
     * @param className
     * @return
     */
    public static Class<?> getClass(String className) {
        try {
            return CommonReflection.class.getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to find class: " + className);
        }
    }

    /**
     * Returns an NMS class with the given name
     *
     * @param className
     * @return
     */
    public static Class<?> getMinecraftClass(String className) {
        if (NMS_HANDLER == null)
            NMS_HANDLER = new ClassPackageMapper(getMinecraftPackage(), getDefaultClassHandler());

        return NMS_HANDLER.getClass(className);
    }

    /**
     * Returns a CraftBukkit class with the given name
     *
     * @param className
     * @return
     */
    public static Class<?> getCraftBukkitClass(String className) {
        if (CRAFTBUKKIT_HANDLER == null)
            CRAFTBUKKIT_HANDLER = new ClassPackageMapper(getCraftBukkitPackage(), getDefaultClassHandler());

        return CRAFTBUKKIT_HANDLER.getClass(className);
    }

    public static boolean isUsingNetty() {
        try {
            Class<?> enumProtocol = getMinecraftClass("EnumProtocol");

            if (enumProtocol != null) {   // Better be safe then sorry...
                return true;
            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }

    // Usefull classes here

    /**
     * Returns the CraftServer class
     *
     * @return
     */
    public static Class<?> getCraftServerClass() {
        return getCraftBukkitClass("CraftServer");
    }

    /**
     * Returns the CraftEntity class
     *
     * @return
     */
    public static Class<?> getCraftEntityClass() {
        return getCraftBukkitClass("entity.CraftEntity");
    }

    /**
     * Returns the CraftPlayer class
     *
     * @return
     */
    public static Class<?> getCraftPlayerClass() {
        return getCraftBukkitClass("entity.CraftPlayer");
    }

    public static Class<?> getMinecraftEntityClass() {
        try {
            return getMinecraftClass("Entity");
        } catch (RuntimeException e) {
            Class<?> craftEntity = getCraftEntityClass();
            MethodAccessor<Object> getHandle = new SafeMethod<Object>(craftEntity, "getHandle"); // Each bukkit-version has this method for sure.

            Class<?> nmsEntity = getHandle.getReturnType();

            return nmsEntity;
        }
    }

    public static Class<?> getEntityPlayerClass() {
        try {
            return getMinecraftClass("EntityPlayer");
        } catch (Exception e) {
            Class<?> craftPlayer = getCraftPlayerClass();
            MethodAccessor<Object> getHandle = new SafeMethod<Object>(craftPlayer, "getHandle");

            Class<?> entityPlayer = getHandle.getReturnType();

            return entityPlayer;
        }
    }

    public static Class<?> getDataWatcherClasss() {
        try {
            return getMinecraftClass("DataWatcher");
        } catch (RuntimeException e) {
            Class<?> entityClass = getMinecraftEntityClass();

            FieldAccessor<Object> dataWatcherField = new SafeField<Object>(entityClass, "dataWatcher");
            return dataWatcherField.getField().getType();
        }
    }

    public static Class<?> getPlayerConnectionClass() {
        try {
            return getMinecraftClass("PlayerConnection");
        } catch (Exception e) {
            Class<?> entityPlayer = getEntityPlayerClass();
            FieldAccessor<Object> playerConnectionField = new SafeField<Object>(entityPlayer, "playerConnection");

            Class<?> playerConnection = playerConnectionField.getField().getType();

            return playerConnection;
        }
    }

    public static Class<?> getNetworkManagerClass() {
        try {
            return getMinecraftClass("NetworkManager");
        } catch (Exception e) {
            Class<?> playerConnection = getPlayerConnectionClass();
            FieldAccessor<Object> networkManagerField = new SafeField<Object>(playerConnection, "networkManager");

            Class<?> networkManager = networkManagerField.getField().getType();

            return networkManager;
        }
    }
}
