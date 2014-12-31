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
import com.dsh105.commodus.PluginDependency;
import com.dsh105.commodus.ServerUtil;
import com.dsh105.commodus.bukkit.BukkitLog;
import com.dsh105.commodus.configuration.Config;
import com.dsh105.commodus.configuration.OptionSet;
import com.dsh105.commodus.logging.Log;
import com.dsh105.commodus.sponge.SpongeLog;
import com.dsh105.echopet.api.commands.influx.EchoPetCommandManager;
import com.dsh105.echopet.api.configuration.ConfigType;
import com.dsh105.echopet.api.event.EventManager;
import com.dsh105.echopet.api.registration.PetRegistry;
import com.dsh105.echopet.bridge.BridgeManager;
import com.dsh105.echopet.bridge.GenericBridge;
import com.dsh105.echopet.util.AffirmationException;
import com.dsh105.influx.InfluxManager;

public class EchoPet {

    public static String INTERNAL_NMS_PATH = "com.dsh105.echopet.nms." + MinecraftReflection.getVersionTag();

    private static PluginCore CORE;
    private static Log LOG;

    public static void setCore(PluginCore core) {
        if (CORE != null) {
            throw new RuntimeException("Core cannot be set twice!");
        }
        CORE = core;
        switch (ServerUtil.getServerBrand().getCapsule()) {
            case BUKKIT:
                LOG = new BukkitLog("EchoPet");
            case SPONGE:
                LOG = new SpongeLog("EchoPet");
        }
        Affirm.setErrorCallable(new AffirmationCallable() {
            @Override
            public void call(Throwable cause) {
                throw new AffirmationException(cause);
            }
        });
    }

    public static PluginCore getCore() {
        return CORE;
    }

    public static Log log() {
        return LOG;
    }

    public static PluginNucleus getNucleus() {
        return CORE.getNucleus();
    }

    public static BridgeManager getBridgeManager() {
        return getNucleus().getBridgeManager();
    }

    public static EventManager getEventManager() {
        return getNucleus().getEventManager();
    }

    public static <T extends GenericBridge> T getBridge(Class<T> bridgeType) {
        return getBridgeManager().getGenericBridge(bridgeType);
    }

    public static EchoPetCommandManager getCommandManager() {
        return getNucleus().getCommandManager();
    }

    public static PetManager getManager() {
        return getNucleus().getPetManager();
    }

    public static PetRegistry getPetRegistry() {
        return getNucleus().getPetRegistry();
    }

    public static <T extends PluginDependency<?, ?>> T getDependency(Class<T> providerType) {
        return getNucleus().getDependency(providerType);
    }

    public static <T extends OptionSet> T getOptions(Class<T> optionsType) {
        return getNucleus().getOptions(optionsType);
    }

    public static OptionSet getOptions(ConfigType configType) {
        return getNucleus().getOptions(configType);
    }

    public static Config getConfig() {
        return getConfig(ConfigType.GENERAL);
    }

    public static Config getConfig(ConfigType configType) {
        return getNucleus().getConfig(configType);
    }
}