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

import com.dsh105.command.CommandManager;
import com.dsh105.commodus.config.Options;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.commodus.dependency.PluginDependencyProvider;
import com.dsh105.echopetv3.api.config.ConfigType;
import com.jolbox.bonecp.BoneCP;
import org.bukkit.plugin.Plugin;

public interface EchoPetCore extends Plugin {

    public static final String DEFAULT_PREFIX = "&4[&cEchoPet&4]&r ";

    CommandManager getCommandManager();

    PetManager getPetManager();

    <T extends PluginDependencyProvider> T getProvider(Class<T> providerClass);

    public abstract BoneCP getDbPool();

    public abstract boolean isUpdateAvailable();

    public abstract <T extends Options> T getSettings(Class<T> settingsClass);

    public abstract Options getSettings(ConfigType configType);

    public abstract YAMLConfig getConfig(ConfigType configType);
}