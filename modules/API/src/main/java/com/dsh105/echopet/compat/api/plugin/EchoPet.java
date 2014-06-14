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

package com.dsh105.echopet.compat.api.plugin;

import com.dsh105.commodus.config.Options;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.commodus.logging.Log;
import com.dsh105.echopet.compat.api.config.ConfigType;

public final class EchoPet {

    private static IEchoPetPlugin PLUGIN;
    public static Log LOG;

    public static void setPlugin(IEchoPetPlugin plugin) {
        if (PLUGIN != null) {
            return;
        }
        PLUGIN = plugin;
        LOG = new Log("EchoPet");
    }

    public static IEchoPetPlugin getPlugin() {
        return PLUGIN;
    }

    public static IPetManager getManager() {
        return PLUGIN.getPetManager();
    }

    public static ISqlPetManager getSqlManager() {
        return PLUGIN.getSqlPetManager();
    }

    public static <T extends Options> T getSettings(Class<T> settingsClass) {
        return PLUGIN.getSettings(settingsClass);
    }

    public static Options getSettings(ConfigType configType){
        return PLUGIN.getSettings(configType);
    }

    public static YAMLConfig getConfig() {
        return getConfig(ConfigType.MAIN);
    }

    public static YAMLConfig getConfig(ConfigType type) {
        return PLUGIN.getConfig(type);
    }

}