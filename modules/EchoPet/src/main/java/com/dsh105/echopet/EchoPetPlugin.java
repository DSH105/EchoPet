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

import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.commodus.config.YAMLConfigManager;
import com.dsh105.commodus.data.Metrics;
import com.dsh105.echopet.api.PetManager;
import com.dsh105.echopet.api.SqlPetManager;
import com.dsh105.echopet.commands.CommandComplete;
import com.dsh105.echopet.commands.PetAdminCommand;
import com.dsh105.echopet.commands.PetCommand;
import com.dsh105.echopet.commands.util.CommandManager;
import com.dsh105.echopet.commands.util.DynamicPluginCommand;
import com.dsh105.echopet.compat.api.config.ConfigOptions;
import com.dsh105.echopet.compat.api.plugin.*;
import com.dsh105.echopet.compat.api.plugin.data.Updater;
import com.dsh105.echopet.compat.api.plugin.uuid.UUIDMigration;
import com.dsh105.echopet.compat.api.reflection.SafeConstructor;
import com.dsh105.echopet.compat.api.reflection.utility.CommonReflection;
import com.dsh105.echopet.compat.api.registration.PetRegistry;
import com.dsh105.echopet.compat.api.util.*;
import com.dsh105.echopet.hook.VanishProvider;
import com.dsh105.echopet.hook.WorldGuardProvider;
import com.dsh105.echopet.listeners.ChunkListener;
import com.dsh105.echopet.listeners.MenuListener;
import com.dsh105.echopet.listeners.PetEntityListener;
import com.dsh105.echopet.listeners.PetOwnerListener;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class EchoPetPlugin extends JavaPlugin implements IEchoPetPlugin {

    private static boolean isUsingNetty;

    private static ISpawnUtil SPAWN_UTIL;
    private static PetManager MANAGER;
    private static SqlPetManager SQL_MANAGER;
    private static ConfigOptions OPTIONS;

    public static final ModuleLogger LOGGER = new ModuleLogger("EchoPet");
    public static final ModuleLogger LOGGER_REFLECTION = LOGGER.getModule("Reflection");

    private PetRegistry petRegistry;

    private CommandManager COMMAND_MANAGER;
    private YAMLConfigManager configManager;
    private YAMLConfig petConfig;
    private YAMLConfig mainConfig;
    private YAMLConfig langConfig;
    private BoneCP dbPool;

    private VanishProvider vanishProvider;
    private WorldGuardProvider worldGuardProvider;

    public String prefix = "" + ChatColor.DARK_RED + "[" + ChatColor.RED + "EchoPet" + ChatColor.DARK_RED + "] " + ChatColor.RESET;

    public String cmdString = "pet";
    public String adminCmdString = "petadmin";

    // Update data
    public boolean update = false;
    public String name = "";
    public long size = 0;
    public boolean updateChecked = false;

    @Override
    public void onEnable() {
        EchoPet.setPlugin(this);
        isUsingNetty = CommonReflection.isUsingNetty();

        this.configManager = new YAMLConfigManager(this);
        COMMAND_MANAGER = new CommandManager(this);
        // Make sure that the plugin is running under the correct version to prevent errors

        try {
            Class.forName(ReflectionUtil.COMPAT_NMS_PATH + ".SpawnUtil");
        } catch (ClassNotFoundException e) {
            EchoPet.LOG.log(ChatColor.RED + "EchoPet " + ChatColor.GOLD
                                    + this.getDescription().getVersion() + ChatColor.RED
                                    + " is not compatible with this version of CraftBukkit");
            EchoPet.LOG.log(ChatColor.RED + "Initialisation failed. Please update the plugin.");

            DynamicPluginCommand cmd = new DynamicPluginCommand(this.cmdString, new String[0], "", "",
                                                                new VersionIncompatibleCommand(this.cmdString, prefix, ChatColor.YELLOW +
                                                                        "EchoPet " + ChatColor.GOLD + this.getDescription().getVersion() + ChatColor.YELLOW + " is not compatible with this version of CraftBukkit. Please update the plugin.",
                                                                                               "echopet.pet", ChatColor.YELLOW + "You are not allowed to do that."),
                                                                null, this);
            COMMAND_MANAGER.register(cmd);
            return;
        }

        this.petRegistry = new PetRegistry();

        SPAWN_UTIL = new SafeConstructor<ISpawnUtil>(ReflectionUtil.getVersionedClass("SpawnUtil")).newInstance();

        this.loadConfiguration();

        PluginManager manager = getServer().getPluginManager();

        MANAGER = new PetManager();
        SQL_MANAGER = new SqlPetManager();

        if (OPTIONS.useSql()) {
            this.prepareSqlDatabase();
        }

        // Register custom commands
        // Command string based off the string defined in config.yml
        // By default, set to 'pet'
        // PetAdmin command draws from the original, with 'admin' on the end
        this.cmdString = OPTIONS.getCommandString();
        this.adminCmdString = OPTIONS.getCommandString() + "admin";
        DynamicPluginCommand petCmd = new DynamicPluginCommand(this.cmdString, new String[0], "Create and manage your own custom pets.", "Use /" + this.cmdString + " help to see the command list.", new PetCommand(this.cmdString), null, this);
        petCmd.setTabCompleter(new CommandComplete());
        COMMAND_MANAGER.register(petCmd);
        COMMAND_MANAGER.register(new DynamicPluginCommand(this.adminCmdString, new String[0], "Create and manage the pets of other players.", "Use /" + this.adminCmdString + " help to see the command list.", new PetAdminCommand(this.adminCmdString), null, this));

        // Register listeners
        manager.registerEvents(new MenuListener(), this);
        manager.registerEvents(new PetEntityListener(), this);
        manager.registerEvents(new PetOwnerListener(), this);
        //manager.registerEvents(new ChunkListener(), this);

        this.vanishProvider = new VanishProvider(this);
        this.worldGuardProvider = new WorldGuardProvider(this);

        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :(
        }

        this.checkUpdates();
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
        String[] header = {"EchoPet By DSH105", "---------------------",
                "Configuration for EchoPet 2",
                "See the EchoPet Wiki before editing this file"};
        try {
            mainConfig = this.configManager.getNewConfig("config.yml", header);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.WARNING, "Configuration File [config.yml] generation failed.", e, true);
        }

        OPTIONS = new ConfigOptions(mainConfig);

        mainConfig.reloadConfig();

        try {
            petConfig = this.configManager.getNewConfig("pets.yml");
            petConfig.reloadConfig();
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.WARNING, "Configuration File [pets.yml] generation failed.", e, true);
        }

        // Make sure to convert those UUIDs!
        if (ReflectionUtil.MC_VERSION_NUMERIC >= 172 && UUIDMigration.supportsUuid() && mainConfig.getBoolean("convertDataFileToUniqueId", true) && petConfig.getConfigurationSection("autosave") != null) {
            EchoPet.LOG.info("Converting data files to UUID system...");
            UUIDMigration.migrateConfig(petConfig);
            mainConfig.set("convertDataFileToUniqueId", false);
            mainConfig.saveConfig();
        }

        String[] langHeader = {"EchoPet By DSH105", "---------------------",
                "Language Configuration File"};
        try {
            langConfig = this.configManager.getNewConfig("language.yml", langHeader);
            try {
                for (Lang l : Lang.values()) {
                    String[] desc = l.getDescription();
                    langConfig.set(l.getPath(), langConfig.getString(l.getPath(), l.toString_()), desc);
                }
                langConfig.saveConfig();
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.WARNING, "Configuration File [language.yml] generation failed.", e, true);
            }

        } catch (Exception e) {
            Logger.log(Logger.LogLevel.WARNING, "Configuration File [language.yml] generation failed.", e, true);
        }
        langConfig.reloadConfig();

        if (Lang.PREFIX.toString_().equals("&4[&cEchoPet&4]&r")) {
            langConfig.set(Lang.PREFIX.getPath(), "&4[&cEchoPet&4]&r ", Lang.PREFIX.getDescription());
        }
        this.prefix = Lang.PREFIX.toString();
    }

    private void prepareSqlDatabase() {
        String host = mainConfig.getString("sql.host", "localhost");
        int port = mainConfig.getInt("sql.port", 3306);
        String db = mainConfig.getString("sql.database", "EchoPet");
        String user = mainConfig.getString("sql.username", "none");
        String pass = mainConfig.getString("sql.password", "none");
        BoneCPConfig bcc = new BoneCPConfig();
        bcc.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + db);
        bcc.setUsername(user);
        bcc.setPassword(pass);
        bcc.setPartitionCount(2);
        bcc.setMinConnectionsPerPartition(3);
        bcc.setMaxConnectionsPerPartition(7);
        bcc.setConnectionTestStatement("SELECT 1");
        try {
            dbPool = new BoneCP(bcc);
        } catch (SQLException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to connect to MySQL! [MySQL DataBase: " + db + "].", e, true);
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
                Logger.log(Logger.LogLevel.SEVERE, "Table generation failed [MySQL DataBase: " + db + "].", e, true);
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

        // Make sure to convert those UUIDs!

    }

    protected void checkUpdates() {
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
                        EchoPet.LOG.log(ChatColor.GOLD + "An update is available: " + name);
                        EchoPet.LOG.log(ChatColor.GOLD + "Type /ecupdate to update.");
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
                    @SuppressWarnings("unused")
                    Updater updater = new Updater(this, 53655, this.getFile(), Updater.UpdateType.NO_VERSION_CHECK, true);
                    return true;
                } else {
                    sender.sendMessage(this.prefix + ChatColor.GOLD + " An update is not available.");
                    return true;
                }
            } else {
                Lang.sendTo(sender, Lang.NO_PERMISSION.toString().replace("%perm%", "echopet.update"));
                return true;
            }
        } else if (commandLabel.equalsIgnoreCase("echopet")) {
            if (sender.hasPermission("echopet.petadmin")) {
                PluginDescriptionFile pdFile = this.getDescription();
                sender.sendMessage(ChatColor.RED + "-------- EchoPet --------");
                sender.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.YELLOW + "DSH105");
                sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.YELLOW + pdFile.getVersion());
                sender.sendMessage(ChatColor.GOLD + "Website: " + ChatColor.YELLOW + pdFile.getWebsite());
                sender.sendMessage(ChatColor.GOLD + "Commands are registered at runtime to provide you with more dynamic control over the command labels.");
                sender.sendMessage(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Command Registration:");
                sender.sendMessage(ChatColor.GOLD + "Main: " + this.OPTIONS.getCommandString());
                sender.sendMessage(ChatColor.GOLD + "Admin: " + this.OPTIONS.getCommandString() + "admin");
            } else {
                Lang.sendTo(sender, Lang.NO_PERMISSION.toString().replace("%perm%", "echopet.petadmin"));
                return true;
            }
        }
        return false;
    }

    @Override
    public YAMLConfig getPetConfig() {
        return this.petConfig;
    }

    @Override
    public YAMLConfig getMainConfig() {
        return mainConfig;
    }

    @Override
    public YAMLConfig getLangConfig() {
        return langConfig;
    }

    @Override
    public ISpawnUtil getSpawnUtil() {
        return SPAWN_UTIL;
    }

    @Override
    public VanishProvider getVanishProvider() {
        return vanishProvider;
    }

    @Override
    public WorldGuardProvider getWorldGuardProvider() {
        return worldGuardProvider;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    public static PetManager getManager() {
        return MANAGER;
    }

    @Override
    public PetRegistry getPetRegistry() {
        return this.petRegistry;
    }

    @Override
    public IPetManager getPetManager() {
        return MANAGER;
    }

    @Override
    public ConfigOptions getOptions() {
        return OPTIONS;
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
    public String getCommandString() {
        return cmdString;
    }

    @Override
    public String getAdminCommandString() {
        return adminCmdString;
    }

    @Override
    public boolean isUsingNetty() {
        return isUsingNetty;
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
