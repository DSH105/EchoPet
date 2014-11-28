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

package com.dsh105.echopet.api.plugin;

import com.captainbern.minecraft.reflection.MinecraftReflection;
import com.dsh105.commodus.Affirm;
import com.dsh105.commodus.AffirmationCallable;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.commodus.dependency.PluginDependencyProvider;
import com.dsh105.commodus.logging.Log;
import com.dsh105.echopet.api.config.ConfigType;
import com.dsh105.echopet.api.registration.PetRegistry;
import com.dsh105.echopet.bridge.BridgeManager;
import com.dsh105.echopet.bridge.GenericBridge;
import com.dsh105.echopet.bridge.PlatformBridge;
import com.dsh105.echopet.util.AffirmationException;
import com.dsh105.influx.InfluxBukkitManager;

public class EchoPet {

    public static String INTERNAL_NMS_PATH = "com.dsh105.echopet.nms." + MinecraftReflection.getVersionTag();

    private static EchoPetCore CORE;
    public static Log LOG;

    public static void setCore(EchoPetCore core) {
        if (CORE != null) {
            throw new RuntimeException("Core cannot be set twice!");
        }
        CORE = core;
        LOG = new Log("EchoPet");
        Affirm.setErrorCallable(new AffirmationCallable<Object>() {
            @Override
            public Object call(Throwable cause) {
                throw new AffirmationException(cause);
            }
        });
    }

    public static EchoPetCore getCore() {
        return CORE;
    }

    public static BridgeManager getBridgeManager() {
        return CORE.getBridgeManager();
    }

    public static <T extends GenericBridge> T getBridge(Class<T> bridgeType) {
        return getBridgeManager().getGenericBridge(bridgeType);
    }

    public static PetManager getManager() {
        return CORE.getPetManager();
    }

    public static PetRegistry getPetRegistry() {
        return CORE.getPetRegistry();
    }

    public static InfluxBukkitManager getCommandManager() {
        return CORE.getCommandManager();
    }

    public static <T extends PluginDependencyProvider> T getProvider(Class<T> providerClass) {
        return CORE.getProvider(providerClass);
    }

    public static YAMLConfig getConfig() {
        return getConfig(ConfigType.GENERAL);
    }

    public static YAMLConfig getConfig(ConfigType configType) {
        return CORE.getConfig(configType);
    }
}