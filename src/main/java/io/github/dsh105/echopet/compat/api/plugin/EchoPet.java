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

package io.github.dsh105.echopet.compat.api.plugin;

import com.dsh105.dshutils.config.YAMLConfig;
import io.github.dsh105.echopet.compat.api.config.ConfigOptions;

public final class EchoPet {

    private static IEchoPetPlugin PLUGIN;

    public static void setPlugin(IEchoPetPlugin plugin) {
        if (PLUGIN != null) {
            return;
        }
        PLUGIN = plugin;
    }

    public static IEchoPetPlugin getPlugin() {
        return PLUGIN;
    }

    public static String getPrefix() {
        return PLUGIN.getPrefix();
    }

    public static IPetManager getManager() {
        return PLUGIN.getPetManager();
    }

    public static ISqlPetManager getSqlManager() {
        return PLUGIN.getSqlPetManager();
    }

    public static ConfigOptions getOptions() {
        return PLUGIN.getOptions();
    }

    public static boolean isUsingNetty() {
        return PLUGIN.isUsingNetty();
    }

    public static YAMLConfig getConfig() {
        return getConfig(ConfigType.MAIN);
    }

    public static YAMLConfig getConfig(ConfigType type) {
        switch (type) {
            case DATA:
                return PLUGIN.getPetConfig();
            case LANG:
                return PLUGIN.getLangConfig();
            default:
                return PLUGIN.getMainConfig();
        }
    }

    public enum ConfigType {
        MAIN, DATA, LANG
    }
}