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

package com.dsh105.echopetv3.api.plugin;

import com.captainbern.minecraft.reflection.MinecraftReflection;
import com.dsh105.command.CommandManager;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.commodus.dependency.PluginDependencyProvider;
import com.dsh105.commodus.logging.Log;
import com.dsh105.echopetv3.api.config.ConfigType;

public class EchoPet {

    public static String INTERNAL_NMS_PATH = "com.dsh105.echopetv3.nms." + MinecraftReflection.getVersionTag();

    private static EchoPetCore CORE;
    public static Log LOG;

    public static void setPlugin(EchoPetCore core) {
        if (CORE != null) {
            throw new RuntimeException("Core cannot be set twice!");
        }
        CORE = core;
        LOG = new Log("EchoPet");
    }

    public static EchoPetCore getCore() {
        return CORE;
    }

    public static PetManager getManager() {
        return CORE.getPetManager();
    }

    public static CommandManager getCommandManager() {
        return CORE.getCommandManager();
    }

    public static <T extends PluginDependencyProvider> T getProvider(Class<T> providerClass) {
        return CORE.getProvider(providerClass);
    }

    public static YAMLConfig getConfig() {
        return getConfig(ConfigType.MAIN);
    }

    public static YAMLConfig getConfig(ConfigType configType) {
        return CORE.getConfig(configType);
    }
}