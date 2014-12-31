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

package com.dsh105.echopet;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.ServerBrand;
import com.dsh105.commodus.bukkit.BukkitConfigManager;
import com.dsh105.commodus.bukkit.BukkitMetrics;
import com.dsh105.commodus.bukkit.DBOUpdater;
import com.dsh105.commodus.configuration.ConfigManager;
import com.dsh105.echopet.api.configuration.Lang;
import com.dsh105.echopet.api.configuration.Settings;
import com.dsh105.echopet.api.event.listeners.platform.BukkitListener;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.api.plugin.EchoPetNucleus;
import com.dsh105.echopet.api.plugin.PluginCore;
import com.dsh105.echopet.api.plugin.PluginNucleus;
import com.dsh105.echopet.api.plugin.hook.BukkitVanishDependency;
import com.dsh105.echopet.api.plugin.hook.BukkitWorldGuardDependency;
import com.dsh105.influx.response.BukkitResponder;
import com.dsh105.interact.Interact;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class EchoPetPlugin extends JavaPlugin implements PluginCore {

    // Update Checker stuff
    public boolean updateAvailable = false;
    public String updateName = "";
    public boolean updateChecked = false;
    public File file;
    private EchoPetNucleus nucleus;

    @Override
    public void onLoad() {
        nucleus = new EchoPetNucleus(this);
        nucleus.preEnable();
        Interact.prepare(this);
    }

    @Override
    public void onDisable() {
        nucleus.disable();
    }

    @Override
    public void onEnable() {
        nucleus.enable();
    }

    @Override
    public ServerBrand getServerBrand() {
        return ServerBrand.BUKKIT;
    }

    @Override
    public PluginNucleus getNucleus() {
        return nucleus;
    }

    @Override
    public ConfigManager<?> prepareConfigManager() {
        return new BukkitConfigManager(this);
    }

    @Override
    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new BukkitListener(), this);
    }

    @Override
    public void loadHooks() {
        nucleus.addDependency(new BukkitVanishDependency(this));
        nucleus.addDependency(new BukkitWorldGuardDependency(this));
    }

    @Override
    public void prepareMetrics() {
        try {
            BukkitMetrics metrics = new BukkitMetrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :(
        }
    }

    @Override
    public void checkForUpdates() {
        if (Settings.CHECK_FOR_UPDATES.getValue()) {
            final File file = getFile();
            final DBOUpdater.UpdateType updateType = Settings.AUTO_UPDATE.getValue() ? DBOUpdater.UpdateType.DEFAULT : DBOUpdater.UpdateType.NO_DOWNLOAD;
            getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
                @Override
                public void run() {
                    DBOUpdater updater = new DBOUpdater((Plugin) EchoPet.getCore(), 53655, file, updateType, false);
                    updateAvailable = updater.getResult() == DBOUpdater.UpdateResult.UPDATE_AVAILABLE;
                    if (updateAvailable) {
                        updateName = updater.getLatestName();
                        for (String part : Lang.UPDATE_AVAILABLE.getValue().split("\n")) {
                            EchoPet.log().console(part);
                        }
                        if (!updateChecked) {
                            updateChecked = true;
                        }
                    }
                }
            });
        }
    }

    @Override
    public void applyPrefixSettings() {
        nucleus.getCommandManager().getResponder().setResponsePrefix(Lang.PREFIX.getValue() + ChatColor.RESET);
        ((BukkitResponder) nucleus.getCommandManager().getResponder()).setMessageFormats(GeneralUtil.toEnumType(ChatColor.class, Settings.BASE_CHAT_COLOUR.getValue()), GeneralUtil.toEnumType(ChatColor.class, Settings.HIGHLIGHT_CHAT_COLOUR.getValue()));
    }

    @Override
    public void performUpdate() {
        if (Settings.CHECK_FOR_UPDATES.getValue()) {
            new DBOUpdater(this, 53655, getFile(), DBOUpdater.UpdateType.NO_VERSION_CHECK, true);
        }
    }

    @Override
    public void cancelTasks() {
        getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    @Override
    public boolean isUpdateChecked() {
        return updateChecked;
    }
}
