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
import com.dsh105.echopet.api.plugin.hook.IVanishProvider;
import com.dsh105.echopet.api.plugin.hook.IWorldGuardProvider;
import com.dsh105.echopetv3.api.config.ConfigType;
import com.jolbox.bonecp.BoneCP;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class IEchoPetPlugin extends JavaPlugin {

    public abstract YAMLConfig getConfig(ConfigType type);

    public abstract IPetManager getPetManager();

    public abstract <T extends Options> T getSettings(Class<T> settingsClass);

    public abstract Options getSettings(ConfigType configType);

    public abstract ISqlPetManager getSqlPetManager();

    public abstract BoneCP getDbPool();

    public abstract IVanishProvider getVanishProvider();

    public abstract IWorldGuardProvider getWorldGuardProvider();

    public abstract YAMLConfig getDataConfig();

    public abstract YAMLConfig getMainConfig();

    public abstract YAMLConfig getLangConfig();

    public abstract boolean isUpdateAvailable();

    public abstract String getUpdateName();

    public abstract long getUpdateSize();
}