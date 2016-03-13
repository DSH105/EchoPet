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

import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.echopet.compat.api.config.ConfigOptions;
import com.dsh105.echopet.compat.api.plugin.hook.IVanishProvider;
import com.dsh105.echopet.compat.api.plugin.hook.IWorldGuardProvider;
import com.dsh105.echopet.compat.api.registration.PetRegistry;
import com.dsh105.echopet.compat.api.util.ISpawnUtil;
import com.jolbox.bonecp.BoneCP;
import org.bukkit.plugin.Plugin;

public interface IEchoPetPlugin extends Plugin {

    public ISpawnUtil getSpawnUtil();

    public String getPrefix();

    public String getCommandString();

    public String getAdminCommandString();

    PetRegistry getPetRegistry();

    public IPetManager getPetManager();

    public ISqlPetManager getSqlPetManager();

    public BoneCP getDbPool();

    public IVanishProvider getVanishProvider();

    public IWorldGuardProvider getWorldGuardProvider();

    public YAMLConfig getPetConfig();

    public YAMLConfig getMainConfig();

    public YAMLConfig getLangConfig();

    public ConfigOptions getOptions();

    public boolean isUsingNetty();

    public boolean isUpdateAvailable();

    public String getUpdateName();

    public long getUpdateSize();
}