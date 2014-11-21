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

import com.dsh105.commodus.IdentUtil;
import com.dsh105.commodus.config.Options;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.commodus.config.YAMLConfigManager;
import com.dsh105.commodus.data.Metrics;
import com.dsh105.commodus.data.Updater;
import com.dsh105.commodus.dependency.PluginDependencyProvider;
import com.dsh105.commodus.logging.Level;
import com.dsh105.echopet.api.config.*;
import com.dsh105.echopet.api.plugin.*;
import com.dsh105.echopet.api.plugin.hook.VanishProviderBase;
import com.dsh105.echopet.api.plugin.hook.WorldGuardProviderBase;
import com.dsh105.echopet.api.registration.PetRegistry;
import com.dsh105.echopet.commands.IncompatiblePluginCommand;
import com.dsh105.echopet.commands.admin.PetAdminCommand;
import com.dsh105.echopet.commands.basic.PetCommand;
import com.dsh105.echopet.listeners.ChunkListener;
import com.dsh105.echopet.listeners.PetListener;
import com.dsh105.echopet.listeners.PetOwnerListener;
import com.dsh105.echopet.util.Perm;
import com.dsh105.echopet.util.TableMigrationUtil;
import com.dsh105.echopet.util.UUIDMigration;
import com.dsh105.influx.BukkitCommandManager;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.Controller;
import com.dsh105.influx.InfluxBukkitManager;
import com.dsh105.influx.annotation.Authorize;
import com.dsh105.influx.annotation.Command;
import com.dsh105.influx.annotation.Nest;
import com.dsh105.influx.annotation.Nested;
import com.dsh105.influx.dispatch.BukkitCommandEvent;
import com.dsh105.influx.response.BukkitResponder;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Nest(nests = {"echopet", "ep"})
@Authorize(Perm.ECHOPET)
public class EchoPetPlugin extends JavaPlugin implements EchoPetCore, CommandListener {

    private InfluxBukkitManager commandManager;
    private PetManager manager;
    private BoneCP dbPool;

    protected YAMLConfigManager configManager;
    private HashMap<ConfigType, YAMLConfig> configFiles = new HashMap<>();
    private HashMap<ConfigType, Options> settings = new HashMap<>();
    private List<PluginDependencyProvider> providers = new ArrayList<>();

    private PetRegistry petRegistry;

    // Update Checker stuff
    public boolean updateAvailable = false;
    public String updateName = "";
    public boolean updateChecked = false;
    public File file;

    @Override
    public void onLoad() {
        EchoPet.setCore(this);
        commandManager = new BukkitCommandManager(this);
    }

    @Override
    public void onEnable() {
        try {
            Class.forName(EchoPet.INTERNAL_NMS_PATH + ".entity.EchoEntityPetBase");
        } catch (ClassNotFoundException e) {
            // Make sure the server owner is aware
            EchoPet.LOG.console(Level.WARNING, "+----------------------+");
            EchoPet.LOG.console(Level.WARNING, "EchoPet is not compatible with this server version");
            EchoPet.LOG.console(Level.WARNING, "Please upgrade/downgrade to the appropriate version");
            EchoPet.LOG.console(Level.WARNING, "+----------------------+");

            // If it isn't already obvious enough...
            commandManager.nestCommandsIn(this, new IncompatiblePluginCommand(), false);
            return;
        }

        loadConfiguration();

        if (Settings.SQL_ENABLE.getValue()) {
            prepareSqlDatabase();
        }

        manager = dbPool != null ? new SimpleSQLPetManager() : new SimplePetManager();

        registerCommands();

        for (Controller controller : commandManager) {
            System.out.println(controller.getCommand().getStringSyntax());
        }

        petRegistry = new PetRegistry();

        getServer().getPluginManager().registerEvents(new PetListener(), this);
        getServer().getPluginManager().registerEvents(new PetOwnerListener(), this);
        getServer().getPluginManager().registerEvents(new ChunkListener(), this);

        loadHooks();

        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :(
        }

        this.checkForUpdates();
    }

    @Override
    public void onDisable() {
        if (manager != null) {
            manager.removeAllPets();
        }

        getServer().getScheduler().cancelTasks(this);

        if (dbPool != null) {
            dbPool.shutdown();
        }
    }

    private void loadConfiguration() {
        configManager = new YAMLConfigManager(this);

        for (ConfigType configType : ConfigType.values()) {
            YAMLConfig config = configManager.getNewConfig(configType.getPath(), configType.getHeader());
            configFiles.put(configType, config);
        }

        settings.put(ConfigType.GENERAL, new Settings(configFiles.get(ConfigType.GENERAL)));
        settings.put(ConfigType.PETS, new PetSettings(configFiles.get(ConfigType.PETS)));
        settings.put(ConfigType.DATA, new Data(configFiles.get(ConfigType.DATA)));
        settings.put(ConfigType.MESSAGES, new Lang(configFiles.get(ConfigType.MESSAGES)));
        settings.put(ConfigType.MENU, new MenuSettings(configFiles.get(ConfigType.MENU)));

        for (YAMLConfig config : configFiles.values()) {
            config.reloadConfig();
        }

        // Handle any UUID conversion
        if (IdentUtil.supportsUuid() && Settings.CONVERT_DATA_FILE_TO_UUID.getValue()) {
            EchoPet.LOG.console("Ensuring data files are converted to UUID system...");
            UUIDMigration.migrateConfig(getConfig(ConfigType.DATA));
            Settings.CONVERT_DATA_FILE_TO_UUID.setValue(false);
        }

        commandManager.getResponder().setResponsePrefix(Lang.PREFIX.getValue() + ChatColor.RESET);
        ((BukkitResponder) commandManager.getResponder()).setMessageFormats(ChatColor.getByChar(Settings.BASE_CHAT_COLOUR.getValue()), ChatColor.getByChar(Settings.HIGHLIGHT_CHAT_COLOUR.getValue()));
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
            EchoPet.LOG.console(Level.WARNING, "Failed to connect to MySQL DataBase.");
            e.printStackTrace();
        }
        if (dbPool != null) {
            TableMigrationUtil.migrateTables();
            TableMigrationUtil.createNewestTable();
        }
    }

    private void registerCommands() {
        // All sub commands are registered inside the classes themselves
        commandManager.register(this);
        new PetCommand();
        new PetAdminCommand();
    }

    private void loadHooks() {
        providers.add(new VanishProviderBase(this));
        providers.add(new WorldGuardProviderBase(this));
    }

    private void checkForUpdates() {
        if (Settings.CHECK_FOR_UPDATES.getValue()) {
            final File file = getFile();
            final Updater.UpdateType updateType = Settings.AUTO_UPDATE.getValue() ? Updater.UpdateType.DEFAULT : Updater.UpdateType.NO_DOWNLOAD;
            getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
                @Override
                public void run() {
                    Updater updater = new Updater(EchoPet.getCore(), 53655, file, updateType, false);
                    updateAvailable = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
                    if (updateAvailable) {
                        updateName = updater.getLatestName();
                        for (String part : Lang.UPDATE_AVAILABLE.getValue().split("\n")) {
                            EchoPet.LOG.console(part);
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
    public InfluxBukkitManager getCommandManager() {
        return commandManager;
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
    public <T extends PluginDependencyProvider> T getProvider(Class<T> providerClass) {
        for (PluginDependencyProvider provider : providers) {
            if (providerClass.isAssignableFrom(provider.getClass())) {
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
    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    @Override
    public <T extends Options> T getSettings(Class<T> settingsClass) {
        for (Options options : settings.values()) {
            if (settingsClass.isAssignableFrom(options.getClass())) {
                return (T) options;
            }
        }
        return null;
    }

    @Override
    public Options getSettings(ConfigType configType) {
        for (Map.Entry<ConfigType, Options> entry : settings.entrySet()) {
            if (entry.getKey() == configType) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public YAMLConfig getConfig(ConfigType configType) {
        return configFiles.get(configType);
    }

    @Command(
            syntax = "",
            desc = "EchoPet: custom entities at your control"
    )
    public boolean onCommand(BukkitCommandEvent event) {
        event.respond(Lang.PLUGIN_INFORMATION.getValue("version", EchoPet.getCore().getDescription().getVersion()));
        return true;
    }

    @Command(
            syntax = "update",
            desc = "Update the EchoPet plugin"
    )
    @Authorize(Perm.UPDATE)
    @Nested
    public boolean onUpdateCommand(BukkitCommandEvent event) {
        if (updateChecked) {
            new Updater(EchoPet.getCore(), 53655, getFile(), Updater.UpdateType.NO_VERSION_CHECK, true);
        } else {
            event.respond(Lang.UPDATE_NOT_AVAILABLE.getValue());
        }
        return true;
    }
}
