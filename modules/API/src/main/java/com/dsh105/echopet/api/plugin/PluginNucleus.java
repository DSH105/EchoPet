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

import com.dsh105.commodus.PluginDependency;
import com.dsh105.commodus.configuration.Config;
import com.dsh105.commodus.configuration.OptionSet;
import com.dsh105.echopet.api.commands.influx.EchoPetCommandManager;
import com.dsh105.echopet.api.configuration.ConfigType;
import com.dsh105.echopet.api.event.EventManager;
import com.dsh105.echopet.api.registration.PetRegistry;
import com.dsh105.echopet.bridge.BridgeManager;
import com.dsh105.influx.InfluxManager;
import com.jolbox.bonecp.BoneCP;

public interface PluginNucleus extends EchoPetCoreBridge {

    BridgeManager getBridgeManager();

    EchoPetCommandManager getCommandManager();
    
    EventManager getEventManager();

    PetManager getPetManager();

    PetRegistry getPetRegistry();

    <T extends PluginDependency<?, ?>> T getDependency(Class<T> dependencyType);

    BoneCP getDbPool();

    <T extends OptionSet> T getOptions(Class<T> optionsType);

    OptionSet getOptions(ConfigType configType);

    Config getConfig(ConfigType configType);

    String getPluginVersion();
}