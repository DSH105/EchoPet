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

import com.dsh105.commodus.config.Options;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.commodus.dependency.PluginDependencyProvider;
import com.dsh105.echopet.api.config.ConfigType;
import com.dsh105.echopet.api.registration.PetRegistry;
import com.dsh105.influx.InfluxBukkitManager;
import com.jolbox.bonecp.BoneCP;
import org.bukkit.plugin.Plugin;

public interface EchoPetCore extends Plugin {

    public static final String DEFAULT_PREFIX = "&4[&cEchoPet&4]&r ";

    InfluxBukkitManager getCommandManager();

    PetManager getPetManager();

    PetRegistry getPetRegistry();

    <T extends PluginDependencyProvider> T getProvider(Class<T> providerClass);

    BoneCP getDbPool();

    boolean isUpdateAvailable();

    <T extends Options> T getSettings(Class<T> settingsClass);

    Options getSettings(ConfigType configType);

    YAMLConfig getConfig(ConfigType configType);
}