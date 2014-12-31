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

import com.dsh105.commodus.ServerBrand;
import com.dsh105.commodus.configuration.ConfigManager;
import com.dsh105.commodus.sponge.SpongeConfigManager;
import com.dsh105.commodus.sponge.SpongeUtil;
import com.dsh105.echopet.api.configuration.Lang;
import com.dsh105.echopet.api.configuration.Settings;
import com.dsh105.echopet.api.event.listeners.platform.SpongeListener;
import com.dsh105.echopet.api.plugin.EchoPetNucleus;
import com.dsh105.echopet.api.plugin.PluginCore;
import com.dsh105.echopet.api.plugin.PluginNucleus;
import com.dsh105.echopet.api.plugin.PluginVersion;
import com.dsh105.influx.response.SpongeResponder;
import com.dsh105.interact.Interact;
import com.google.inject.Inject;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.state.InitializationEvent;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.config.ConfigDir;
import org.spongepowered.api.service.scheduler.Task;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.event.Subscribe;

import java.io.File;

@Plugin(id = PluginVersion.PLUGIN_ID, name = PluginVersion.PLUGIN_NAME, version = PluginVersion.PLUGIN_VERSION)
public class EchoPetPlugin implements PluginCore {

    // TODO: Update Checker stuff?
    private EchoPetNucleus nucleus;

    @Inject
    private Game game;

    @Inject
    private PluginContainer container;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDirectory;

    @Subscribe
    public void onPreInitialisation(PreInitializationEvent event) {
        SpongeUtil.prepare(this, game);
        nucleus = new EchoPetNucleus(this);
        nucleus.preEnable();
        Interact.prepare(this, game);
    }

    @Subscribe
    public void onInitialisation(InitializationEvent event) {
        nucleus.enable();
    }

    @Subscribe
    public void onServerStopping(ServerStoppingEvent event) {
        nucleus.disable();
    }

    @Override
    public ServerBrand getServerBrand() {
        return ServerBrand.SPONGE;
    }

    @Override
    public PluginNucleus getNucleus() {
        return nucleus;
    }

    @Override
    public ConfigManager<?> prepareConfigManager() {
        return new SpongeConfigManager(configDirectory);
    }

    @Override
    public void registerListeners() {
        game.getEventManager().register(this, new SpongeListener());
    }

    @Override
    public void loadHooks() {
        // TODO: wait for ports
    }

    @Override
    public void prepareMetrics() {
        // TODO
        /*try {
            SpongeMetrics metrics = new SpongeMetrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :(
        }*/
    }

    @Override
    public void checkForUpdates() {
        if (Settings.CHECK_FOR_UPDATES.getValue()) {
            // TODO
        }
    }

    @Override
    public void applyPrefixSettings() {
        nucleus.getCommandManager().getResponder().setResponsePrefix(Lang.PREFIX.getValue() + TextStyles.RESET);
        ((SpongeResponder) nucleus.getCommandManager().getResponder()).setMessageFormats(TextColors.valueOf(Settings.BASE_CHAT_COLOUR.getValue().toUpperCase()).get(), TextColors.valueOf(Settings.HIGHLIGHT_CHAT_COLOUR.getValue().toUpperCase()).get());
    }

    @Override
    public void performUpdate() {
        if (Settings.CHECK_FOR_UPDATES.getValue()) {
            // TODO
        }
    }

    @Override
    public void cancelTasks() {
        for (Task task : game.getScheduler().getScheduledTasks(this)) {
            task.cancel();
        }
    }

    @Override
    public boolean isUpdateAvailable() {
        // TODO
        return false;
    }

    @Override
    public boolean isUpdateChecked() {
        // TODO
        return false;
    }
}
