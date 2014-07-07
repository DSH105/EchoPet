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

import com.captainbern.minecraft.reflection.MinecraftReflection;
import com.dsh105.commodus.IdentUtil;
import com.dsh105.commodus.config.Options;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.commodus.config.YAMLConfigManager;
import com.dsh105.commodus.data.Metrics;
import com.dsh105.commodus.data.Updater;
import com.dsh105.commodus.logging.Level;
import com.dsh105.echopet.api.PetManager;
import com.dsh105.echopet.api.SqlPetManager;
import com.dsh105.echopet.commands.CommandComplete;
import com.dsh105.echopet.commands.PetAdminCommand;
import com.dsh105.echopet.commands.PetCommand;
import com.dsh105.echopet.commands.util.CommandManager;
import com.dsh105.echopet.commands.util.DynamicPluginCommand;
import com.dsh105.echopet.api.config.*;
import com.dsh105.echopet.api.entity.nms.EntityPet;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.plugin.*;
import com.dsh105.echopet.api.plugin.UUIDMigration;
import com.dsh105.echopet.reflection.ReflectionConstants;
import com.dsh105.echopet.reflection.SafeField;
import com.dsh105.echopet.util.TableMigrationUtil;
import com.dsh105.echopet.hook.VanishProvider;
import com.dsh105.echopet.hook.WorldGuardProvider;
import com.dsh105.echopet.listeners.ChunkListener;
import com.dsh105.echopet.listeners.PetEntityListener;
import com.dsh105.echopet.listeners.PetOwnerListener;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EchoPetPlugin extends IEchoPetPlugin {

    private static PetManager MANAGER;
    private static SqlPetManager SQL_MANAGER;

    private CommandManager COMMAND_MANAGER;
    private YAMLConfigManager CONFIG_MANAGER;
    private HashMap<ConfigType, YAMLConfig> CONFIG_FILES = new HashMap<ConfigType, YAMLConfig>();
    private HashMap<ConfigType, Options> SETTINGS = new HashMap<ConfigType, Options>();
    private BoneCP dbPool;

    private VanishProvider vanishProvider;
    private WorldGuardProvider worldGuardProvider;

    // Update data
    public boolean update = false;
    public String name = "";
    public long size = 0;
    public boolean updateChecked = false;

    @Override
    public void onEnable() {
        EchoPet.setPlugin(this);
        COMMAND_MANAGER = new CommandManager(this);

        // Simple check for a class that should be here if EchoPet is compatible...
        try {
            Class.forName(EchoPet.INTERNAL_NMS_PATH + ".entity.EntityPetImpl");
        } catch (ClassNotFoundException e) {
            // Make sure the server owner is aware
            EchoPet.LOG.console(Level.WARNING, "+----------------------+");
            EchoPet.LOG.console(Level.WARNING, "EchoPet is not compatible with this server version");
            EchoPet.LOG.console(Level.WARNING, "Please upgrade/downgrade to the appropriate version");
            EchoPet.LOG.console(Level.WARNING, "+----------------------+");

            COMMAND_MANAGER.register(new DynamicPluginCommand(Settings.COMMAND.getValue(), new String[0], "Create and manage your own custom pets", "Use /" + Settings.COMMAND.getValue() + " help to see the command list.", new CommandExecutor() {
                @Override
                public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
                    sender.sendMessage(ChatColor.YELLOW + "EchoPet is not compatible with this server version. Please upgrade/downgrade to the appropriate version.");
                    return true;
                }
            }, null, this));
            return;
        }


        // Load all configs
        this.loadConfiguration();

        // Prepare our pet managers
        MANAGER = new PetManager();
        SQL_MANAGER = new SqlPetManager();
        if (Settings.SQL_ENABLE.getValue()) {
            this.prepareSqlDatabase();
        }

        // Register custom entities
        for (PetType pt : PetType.values()) {
            this.registerEntity(pt.getEntityClass(), pt.humanName() + " Pet", pt.getRegistrationId());
        }

        // Register custom commands
        DynamicPluginCommand petCmd = new DynamicPluginCommand(Settings.COMMAND.getValue(), new String[0], "Create and manage your own custom pets.", "Use /" + Settings.COMMAND.getValue() + " help to see the command list.", new PetCommand(), null, this);
        petCmd.setTabCompleter(new CommandComplete());
        COMMAND_MANAGER.register(petCmd);
        COMMAND_MANAGER.register(new DynamicPluginCommand(Settings.COMMAND.getValue() + "admin", new String[0], "Create and manage the pets of other players.", "Use /" + Settings.COMMAND.getValue() + "admin help to see the command list.", new PetAdminCommand(), null, this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new PetEntityListener(), this);
        getServer().getPluginManager().registerEvents(new PetOwnerListener(), this);
        getServer().getPluginManager().registerEvents(new ChunkListener(), this);

        this.vanishProvider = new VanishProvider(this);
        this.worldGuardProvider = new WorldGuardProvider(this);

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
        if (MANAGER != null) {
            MANAGER.removeAllPets();
        }
        if (dbPool != null) {
            dbPool.shutdown();
        }

        // Unregister the commands
        this.COMMAND_MANAGER.unregister();
    }

    private void loadConfiguration() {
        CONFIG_MANAGER = new YAMLConfigManager(this);
        YAMLConfig config,
                petsConfig,
                dataConfig,
                langConfig,
                menuConfig;

        config = CONFIG_MANAGER.getNewConfig("config.yml", new String[] {"EchoPet By DSH105", "---------------------",
                "Configuration for EchoPet 2.x",
                "See the EchoPet Wiki before editing this file",
                "https://github.com/DSH105/EchoPet/wiki/"});
        petsConfig = CONFIG_MANAGER.getNewConfig("pets-config.yml");
        langConfig = CONFIG_MANAGER.getNewConfig("language.yml", new String[] {"EchoPet By DSH105", "---------------------", "Language Configuration File"});
        menuConfig = CONFIG_MANAGER.getNewConfig("menu-config.yml");
        dataConfig = CONFIG_MANAGER.getNewConfig("pets.yml");

        CONFIG_FILES.put(ConfigType.MAIN, config);
        CONFIG_FILES.put(ConfigType.PETS_CONFIG, petsConfig);
        CONFIG_FILES.put(ConfigType.LANG_CONFIG, langConfig);
        CONFIG_FILES.put(ConfigType.MENU_CONFIG, menuConfig);
        CONFIG_FILES.put(ConfigType.DATA, dataConfig);

        for (YAMLConfig yamlConfig : CONFIG_FILES.values()) {
            yamlConfig.reloadConfig();
        }

        SETTINGS.put(ConfigType.MAIN, new Settings(config));
        SETTINGS.put(ConfigType.PETS_CONFIG, new PetSettings(petsConfig));
        SETTINGS.put(ConfigType.MENU_CONFIG, new MenuSettings(menuConfig));
        SETTINGS.put(ConfigType.LANG_CONFIG, new Lang(langConfig));

        // Handle any UUID conversion
        if (IdentUtil.supportsUuid() && Settings.CONVERT_DATA_FILE_TO_UUID.getValue() && dataConfig.getConfigurationSection("autosave") != null) {
            EchoPet.LOG.console("Converting data files to UUID system...");
            UUIDMigration.migrateConfig(dataConfig);
            Settings.CONVERT_DATA_FILE_TO_UUID.setValue(false);
        }
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
            Connection connection = null;
            Statement statement = null;
            try {
                connection = dbPool.getConnection();
                statement = connection.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS EchoPet_version3 (" +
                        "OwnerName varchar(36)," +
                        "PetType varchar(255)," +
                        "PetName varchar(255)," +
                        "PetData BIGINT," +
                        "RiderPetType varchar(255)," +
                        "RiderPetName varchar(255), " +
                        "RiderPetData BIGINT," +
                        "PRIMARY KEY (OwnerName)" +
                        ");");

                // Convert previous database versions
                TableMigrationUtil.migrateTables();
            } catch (SQLException e) {
                EchoPet.LOG.console(Level.WARNING, "Failed to generate MySQL table.");
                e.printStackTrace();
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException ignored) {
                }
            }
        }

    }

    private void checkForUpdates() {
        if (this.getMainConfig().getBoolean("checkForUpdates", true)) {
            final File file = this.getFile();
            final Updater.UpdateType updateType = this.getMainConfig().getBoolean("autoUpdate", false) ? Updater.UpdateType.DEFAULT : Updater.UpdateType.NO_DOWNLOAD;
            getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
                @Override
                public void run() {
                    Updater updater = new Updater(EchoPet.getPlugin(), 53655, file, updateType, false);
                    update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
                    if (update) {
                        name = updater.getLatestName();
                        EchoPet.LOG.console("An update is available: " + name);
                        EchoPet.LOG.console("Type /ecupdate to update");
                        if (!updateChecked) {
                            updateChecked = true;
                        }
                    }
                }
            });
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("ecupdate")) {
            if (sender.hasPermission("echopet.update")) {
                if (updateChecked) {
                    new Updater(this, 53655, this.getFile(), Updater.UpdateType.NO_VERSION_CHECK, true);
                    return true;
                } else {
                    Lang.UPDATE_NOT_AVAILABLE.send(sender);
                    return true;
                }
            } else {
                Lang.NO_PERMISSION.send(sender, "%perm%", Permission.UPDATE.getValue());
                return true;
            }
        } else if (commandLabel.equalsIgnoreCase("echopet")) {
            if (sender.hasPermission("echopet.petadmin")) {
                PluginDescriptionFile pdFile = this.getDescription();
                sender.sendMessage(ChatColor.YELLOW + "Currently running " + ChatColor.GOLD + "EchoPet " + getDescription().getVersion() + ChatColor.YELLOW + " by DSH105");
                sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Commands: " + Settings.COMMAND.getValue() + ", " + Settings.COMMAND.getValue() + "admin");
            } else {
                Lang.NO_PERMISSION.send(sender, "%perm%", Permission.ADMIN.getValue());
                return true;
            }
        }
        return false;
    }

    private void registerEntity(Class<? extends EntityPet> clazz, String name, int id) {
        // Initiate all our fields
        Map<String, Class> entityNameToClassMapping = new SafeField<Map<String, Class>>(MinecraftReflection.getMinecraftClass("EntityTypes"), ReflectionConstants.ENTITYTYPES_FIELD_NAMETOCLASSMAP.getName()).get(null);
        Map<Class, String> classToEntityNameMapping = new SafeField<Map<Class, String>>(MinecraftReflection.getMinecraftClass("EntityTypes"), ReflectionConstants.ENTITYTYPES_FIELD_CLASSTONAMEMAP.getName()).get(null);
        Map<Class, Integer> classToIdMapping = new SafeField<Map<Class, Integer>>(MinecraftReflection.getMinecraftClass("EntityTypes"), ReflectionConstants.ENTITYTYPES_FIELD_CLASSTOIDMAP.getName()).get(null);
        Map<String, Integer> entityNameToIdMapping = new SafeField<Map<String, Integer>>(MinecraftReflection.getMinecraftClass("EntityTypes"), ReflectionConstants.ENTITYTYPES_FIELD_NAMETOIDMAP.getName()).get(null);

        // First make sure we don't register something twice
        Iterator mapIter = entityNameToClassMapping.keySet().iterator();
        while (mapIter.hasNext()) {
            String entityName = (String) mapIter.next();
            if (entityName.equals(name)) {
                mapIter.remove();
            }
        }

        mapIter = entityNameToIdMapping.keySet().iterator();
        while (mapIter.hasNext()) {
            String entityName = (String) mapIter.next();
            if (entityName.equals(name)) {
                mapIter.remove();
            }
        }

        mapIter = classToEntityNameMapping.keySet().iterator();
        while (mapIter.hasNext()) {
            Class entityClass = (Class) mapIter.next();
            if (entityClass.getCanonicalName().equals(clazz.getCanonicalName())) {
                mapIter.remove();
            }
        }

        mapIter = classToIdMapping.keySet().iterator();
        while (mapIter.hasNext()) {
            Class entityClass = (Class) mapIter.next();
            if (entityClass.getCanonicalName().equals(clazz.getCanonicalName())) {
                mapIter.remove();
            }
        }

        // Finally, register the entities
        entityNameToClassMapping.put(name, clazz);
        classToEntityNameMapping.put(clazz, name);
        classToIdMapping.put(clazz, id);
        entityNameToIdMapping.put(name, id);
    }

    @Override
    public YAMLConfig getDataConfig() {
        return getConfig(ConfigType.DATA);
    }

    @Override
    public YAMLConfig getMainConfig() {
        return getConfig(ConfigType.MAIN);
    }

    @Override
    public YAMLConfig getLangConfig() {
        return getConfig(ConfigType.LANG_CONFIG);
    }

    @Override
    public YAMLConfig getConfig(ConfigType type) {
        return CONFIG_FILES.get(type);
    }

    @Override
    public VanishProvider getVanishProvider() {
        return vanishProvider;
    }

    @Override
    public WorldGuardProvider getWorldGuardProvider() {
        return worldGuardProvider;
    }

    public static PetManager getManager() {
        return MANAGER;
    }

    @Override
    public IPetManager getPetManager() {
        return MANAGER;
    }

    @Override
    public <T extends Options> T getSettings(Class<T> settingsClass) {
        for (Options options : SETTINGS.values()) {
            if (options.getClass().equals(settingsClass)) {
                return (T) options;
            }
        }
        return null;
    }

    @Override
    public Options getSettings(ConfigType configType) {
        for (Map.Entry<ConfigType, Options> entry : SETTINGS.entrySet()) {
            if (entry.getKey() == configType) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public ISqlPetManager getSqlPetManager() {
        return SQL_MANAGER;
    }

    @Override
    public BoneCP getDbPool() {
        return dbPool;
    }

    @Override
    public boolean isUpdateAvailable() {
        return update;
    }

    @Override
    public String getUpdateName() {
        return name;
    }

    @Override
    public long getUpdateSize() {
        return size;
    }
}
