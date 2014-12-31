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

import com.dsh105.commodus.ManifestUtil;
import com.dsh105.commodus.PluginDependency;
import com.dsh105.commodus.configuration.Config;
import com.dsh105.commodus.configuration.ConfigManager;
import com.dsh105.commodus.configuration.OptionSet;
import com.dsh105.commodus.logging.Level;
import com.dsh105.echopet.api.commands.admin.PetAdminCommand;
import com.dsh105.echopet.api.commands.influx.EchoPetCommandManager;
import com.dsh105.echopet.api.commands.user.PetCommand;
import com.dsh105.echopet.api.configuration.*;
import com.dsh105.echopet.api.event.EventManager;
import com.dsh105.echopet.api.event.listeners.ChunkListener;
import com.dsh105.echopet.api.event.listeners.DependencyListener;
import com.dsh105.echopet.api.event.listeners.PetListener;
import com.dsh105.echopet.api.event.listeners.PlayerListener;
import com.dsh105.echopet.api.registration.PetRegistry;
import com.dsh105.echopet.bridge.BridgeManager;
import com.dsh105.echopet.api.commands.IncompatiblePluginCommand;
import com.dsh105.echopet.util.Perm;
import com.dsh105.echopet.util.TableMigrationUtil;
import com.dsh105.echopet.util.UUIDMigration;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.Controller;
import com.dsh105.influx.InfluxManager;
import com.dsh105.influx.annotation.Authorize;
import com.dsh105.influx.annotation.Command;
import com.dsh105.influx.annotation.Nest;
import com.dsh105.influx.annotation.Nested;
import com.dsh105.influx.dispatch.CommandContext;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Nest(nests = {"echopet", "ep"})
@Authorize(Perm.ECHOPET)
public class EchoPetNucleus implements PluginNucleus, CommandListener {

    public static final String DEFAULT_PREFIX = "&4[&cEchoPet&4]&r ";
    protected ConfigManager<?> configManager;
    private PluginCore pluginCore;
    private String pluginVersion = "unknown";
    private BridgeManager bridgeManager;
    private EchoPetCommandManager commandManager;
    private PetManager manager;
    private PetRegistry petRegistry;
    private EventManager eventManager;
    private BoneCP dbPool;
    private HashMap<ConfigType, Config> configFiles = new HashMap<>();
    private HashMap<ConfigType, OptionSet> settings = new HashMap<>();
    private List<PluginDependency<?, ?>> dependencies = new ArrayList<>();

    public EchoPetNucleus(PluginCore pluginCore) {
        this.pluginCore = pluginCore;
    }

    public void preEnable() {
        EchoPet.setCore(pluginCore);
        try {
            pluginVersion = ManifestUtil.getAttribute("Plugin-Version");
        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException("Failed to retrieve plugin version.", e);
        }
        bridgeManager = new BridgeManager(this, pluginCore.getServerBrand());
        commandManager = new EchoPetCommandManager();
    }

    public void enable() {
        try {
            Class.forName(EchoPet.INTERNAL_NMS_PATH + ".entity.EchoEntityPetBase");
        } catch (ClassNotFoundException e) {
            // Make sure the server owner is aware
            EchoPet.log().console(Level.WARNING, "+----------------------+");
            EchoPet.log().console(Level.WARNING, "EchoPet is not compatible with this server version");
            EchoPet.log().console(Level.WARNING, "Please upgrade/downgrade to the appropriate version");
            EchoPet.log().console(Level.WARNING, "+----------------------+");

            // If it isn't already obvious enough...
            commandManager.nestCommandsIn(this, new IncompatiblePluginCommand(), false);
            return;
        }

        loadConfiguration();

        if (Settings.SQL_ENABLE.getValue()) {
            prepareSqlDatabase();
        }

        manager = getDbPool() == null ? new PetManagerImpl() : new SQLPetManagerImpl();

        commandManager.register(this);
        
        // Register commands (done inside these two classes)
        new PetCommand();
        new PetAdminCommand();

        petRegistry = new PetRegistry();

        // Register listeners
        eventManager = new EventManager();
        eventManager.register(new ChunkListener());
        eventManager.register(new DependencyListener());
        eventManager.register(new PetListener());
        eventManager.register(new PlayerListener());
        pluginCore.registerListeners(); // TODO: perhaps handle this dynamically

        pluginCore.loadHooks();
        pluginCore.prepareMetrics();
        pluginCore.checkForUpdates();
    }

    public void disable() {
        if (manager != null) {
            manager.removeAllPets();
        }

        pluginCore.cancelTasks();
        petRegistry.shutdown();

        if (dbPool != null) {
            dbPool.shutdown();
        }
    }

    private void loadConfiguration() {
        configManager = pluginCore.prepareConfigManager();

        for (ConfigType configType : ConfigType.values()) {
            Config config = configManager.prepareConfig(configType.getPath(), configType.getHeader());
            configFiles.put(configType, config);
        }

        settings.put(ConfigType.GENERAL, new Settings(configFiles.get(ConfigType.GENERAL)));
        settings.put(ConfigType.PETS, new PetSettings(configFiles.get(ConfigType.PETS)));
        settings.put(ConfigType.DATA, new Data(configFiles.get(ConfigType.DATA)));
        settings.put(ConfigType.MESSAGES, new Lang(configFiles.get(ConfigType.MESSAGES)));
        settings.put(ConfigType.MENU, new MenuSettings(configFiles.get(ConfigType.MENU)));

        for (Config config : configFiles.values()) {
            config.reload();
        }

        // Handle any UUID conversion
        // previously if (IdentUtil.supportsUuid() && ...) - EchoPet 3.x only supports 1.8+
        if (Settings.CONVERT_DATA_FILE_TO_UUID.getValue()) {
            EchoPet.log().console("Ensuring data files are converted to UUID system...");
            UUIDMigration.migrateConfig(getConfig(ConfigType.DATA));
            Settings.CONVERT_DATA_FILE_TO_UUID.setValue(false);
        }

        pluginCore.applyPrefixSettings();
    }

    private void prepareSqlDatabase() {
        BoneCPConfig bcc = new BoneCPConfig();
        bcc.setJdbcUrl("jdbc:mysql://" + Settings.SQL_HOST.getValue() + ":" + Settings.SQL_PORT.getValue() + "/" + Settings.SQL_DATABASE.getValue());
        bcc.setUsername(Settings.SQL_USERNAME.getValue());
        bcc.setPassword(Settings.SQL_PASSWORD.getValue());
        bcc.setPartitionCount(2);
        bcc.setMinConnectionsPerPartition(3);
        bcc.setMaxConnectionsPerPartition(7);
        bcc.setConnectionTestStatement("SELECT 1");
        try {
            dbPool = new BoneCP(bcc);
        } catch (SQLException e) {
            EchoPet.log().console(Level.WARNING, "Failed to connect to MySQL DataBase.");
            e.printStackTrace();
        }
        if (dbPool != null) {
            TableMigrationUtil.migrateTables();
            TableMigrationUtil.createNewestTable();
        }
    }

    public void addDependency(PluginDependency<?, ?> dependencyProvider) {
        if (dependencyProvider != null) {
            dependencies.add(dependencyProvider);
        }
    }

    @Override
    public BridgeManager getBridgeManager() {
        return bridgeManager;
    }

    @Override
    public EchoPetCommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public PetManager getPetManager() {
        return manager;
    }

    @Override
    public PetRegistry getPetRegistry() {
        return petRegistry;
    }

    @Override
    public <T extends PluginDependency<?, ?>> T getDependency(Class<T> dependencyType) {
        for (PluginDependency<?, ?> provider : dependencies) {
            if (dependencyType.isAssignableFrom(provider.getClass())) {
                return (T) provider;
            }
        }
        return null;
    }

    @Override
    public BoneCP getDbPool() {
        return dbPool;
    }

    @Override
    public <T extends OptionSet> T getOptions(Class<T> optionsType) {
        for (OptionSet options : settings.values()) {
            if (optionsType.isAssignableFrom(options.getClass())) {
                return (T) options;
            }
        }
        return null;
    }

    @Override
    public OptionSet getOptions(ConfigType configType) {
        for (Map.Entry<ConfigType, OptionSet> entry : settings.entrySet()) {
            if (entry.getKey() == configType) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Config getConfig(ConfigType configType) {
        return configFiles.get(configType);
    }

    @Override
    public String getPluginVersion() {
        return pluginVersion;
    }

    @Command(
            syntax = "",
            desc = "EchoPet: custom entities at your control"
    )
    public boolean onCommand(CommandContext context) {
        context.respond(Lang.PLUGIN_INFORMATION.getValue("version", pluginVersion));
        return true;
    }

    @Command(
            syntax = "update",
            desc = "Update the EchoPet plugin"
    )
    @Authorize(Perm.UPDATE)
    @Nested
    public boolean onUpdateCommand(CommandContext context) {
        if (pluginCore.isUpdateChecked()) {
            pluginCore.performUpdate();
        } else {
            context.respond(Lang.UPDATE_NOT_AVAILABLE.getValue());
        }
        return true;
    }
}