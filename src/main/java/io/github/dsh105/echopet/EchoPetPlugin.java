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

package io.github.dsh105.echopet;

import com.dsh105.dshutils.DSHPlugin;
import com.dsh105.dshutils.Metrics;
import com.dsh105.dshutils.Updater;
import com.dsh105.dshutils.command.VersionIncompatibleCommand;
import com.dsh105.dshutils.config.YAMLConfig;
import com.dsh105.dshutils.logger.ConsoleLogger;
import com.dsh105.dshutils.logger.Logger;
import com.dsh105.dshutils.util.VersionUtil;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import io.github.dsh105.echopet.api.PetManager;
import io.github.dsh105.echopet.api.SqlPetManager;
import io.github.dsh105.echopet.commands.CommandComplete;
import io.github.dsh105.echopet.commands.PetAdminCommand;
import io.github.dsh105.echopet.commands.PetCommand;
import io.github.dsh105.echopet.commands.util.CommandManager;
import io.github.dsh105.echopet.commands.util.DynamicPluginCommand;
import io.github.dsh105.echopet.compat.api.config.ConfigOptions;
import io.github.dsh105.echopet.compat.api.entity.IEntityPet;
import io.github.dsh105.echopet.compat.api.entity.PetData;
import io.github.dsh105.echopet.compat.api.entity.PetType;
import io.github.dsh105.echopet.compat.api.plugin.*;
import io.github.dsh105.echopet.compat.api.util.Lang;
import io.github.dsh105.echopet.compat.api.util.ReflectionUtil;
import io.github.dsh105.echopet.compat.api.util.SQLUtil;
import io.github.dsh105.echopet.compat.api.util.reflection.SafeConstructor;
import io.github.dsh105.echopet.compat.api.util.reflection.SafeField;
import io.github.dsh105.echopet.compat.nms.ISpawnUtil;
import io.github.dsh105.echopet.hook.VanishProvider;
import io.github.dsh105.echopet.hook.WorldGuardProvider;
import io.github.dsh105.echopet.listeners.ChunkListener;
import io.github.dsh105.echopet.listeners.MenuListener;
import io.github.dsh105.echopet.listeners.PetEntityListener;
import io.github.dsh105.echopet.listeners.PetOwnerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;

public class EchoPetPlugin extends DSHPlugin implements IEchoPetPlugin {

    private static boolean isUsingNetty;

    private static ISpawnUtil SPAWN_UTIL;
    private static PetManager MANAGER;
    private static SqlPetManager SQL_MANAGER;
    private static ConfigOptions OPTIONS;

    public static final ModuleLogger LOGGER = new ModuleLogger();
    public static final ModuleLogger LOGGER_REFLECTION = LOGGER.getModule();

    private CommandManager COMMAND_MANAGER;
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
        super.onEnable();
        EchoPet.setPlugin(this);
        Logger.initiate(this, "EchoPet", "[EchoPet]");

        // meh
        if (Bukkit.getVersion().contains("1.7")) {
            isUsingNetty = true;
        } else if (Bukkit.getVersion().contains("1.6")) {
            isUsingNetty = false;
        }

        COMMAND_MANAGER = new CommandManager(this);
        // Make sure that the plugin is running under the correct version to prevent errors

        try {
            Class.forName(ReflectionUtil.COMPAT_NMS_PATH + ".SpawnUtil");
        } catch (ClassNotFoundException e) {
            ConsoleLogger.log(ChatColor.RED + "EchoPet " + ChatColor.GOLD
                    + this.getDescription().getVersion() + ChatColor.RED
                    + " is not compatible with this version of CraftBukkit");
            ConsoleLogger.log(ChatColor.RED + "Initialisation failed. Please update the plugin.");

            DynamicPluginCommand cmd = new DynamicPluginCommand(this.cmdString, new String[0], "", "",
                    new VersionIncompatibleCommand(this.cmdString, prefix, ChatColor.YELLOW +
                            "EchoPet " + ChatColor.GOLD + VersionUtil.getPluginVersion() + ChatColor.YELLOW + " is not compatible with this version of CraftBukkit. Please update the plugin.",
                            "echopet.pet", ChatColor.YELLOW + "You are not allowed to do that."),
                    null, this);
            COMMAND_MANAGER.register(cmd);
            return;
        }

        SPAWN_UTIL = new SafeConstructor<ISpawnUtil>(ReflectionUtil.getVersionedClass("SpawnUtil")).newInstance();

        this.loadConfiguration();

        PluginManager manager = getServer().getPluginManager();

        MANAGER = new PetManager();
        SQL_MANAGER = new SqlPetManager();

        if (OPTIONS.useSql()) {
            this.prepareSqlDatabase();
        }

        // Register custom entities
        for (PetType pt : PetType.values()) {
            this.registerEntity(pt.getEntityClass(), pt.getDefaultName().replace(" ", ""), pt.getRegistrationId());
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
        manager.registerEvents(new ChunkListener(), this);

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

        // Don't nullify instance until after we're done
        super.onDisable();
    }

    private void loadConfiguration() {
        String[] header = {"EchoPet By DSH105", "---------------------",
                "Configuration for EchoPet 2",
                "See the EchoPet Wiki before editing this file"};
        try {
            mainConfig = this.getConfigManager().getNewConfig("config.yml", header);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.WARNING, "Configuration File [config.yml] generation failed.", e, true);
        }

        OPTIONS = new ConfigOptions(mainConfig);

        mainConfig.reloadConfig();

        try {
            petConfig = this.getConfigManager().getNewConfig("pets.yml");
            petConfig.reloadConfig();
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.WARNING, "Configuration File [pets.yml] generation failed.", e, true);
        }

        String[] langHeader = {"EchoPet By DSH105", "---------------------",
                "Language Configuration File"};
        try {
            langConfig = this.getConfigManager().getNewConfig("language.yml", langHeader);
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
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS Pets (" +
                        "OwnerName varchar(255)," +
                        "PetType varchar(255)," +
                        "PetName varchar(255)," +
                        SQLUtil.serialise(PetData.values(), false) + ", " +
                        "RiderPetType varchar(255), RiderPetName varchar(255), " +
                        SQLUtil.serialise(PetData.values(), true) +
                        ", PRIMARY KEY (OwnerName)" +
                        ");");
            } catch (SQLException e) {
                Logger.log(Logger.LogLevel.SEVERE, "`Pets` Table generation failed [MySQL DataBase: " + db + "].", e, true);
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

    protected void checkUpdates() {
        if (this.getMainConfig().getBoolean("checkForUpdates", true)) {
            final File file = this.getFile();
            final Updater.UpdateType updateType = this.getMainConfig().getBoolean("autoUpdate", false) ? Updater.UpdateType.DEFAULT : Updater.UpdateType.NO_DOWNLOAD;
            getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
                @Override
                public void run() {
                    Updater updater = new Updater(getInstance(), 53655, file, updateType, false);
                    update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
                    if (update) {
                        name = updater.getLatestName();
                        ConsoleLogger.log(ChatColor.GOLD + "An update is available: " + name);
                        ConsoleLogger.log(ChatColor.GOLD + "Type /ecupdate to update.");
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

    public void registerEntity(Class<? extends IEntityPet> clazz, String name, int id) {
        Map<String, Class> c = new SafeField<Map<String, Class>>(ReflectionUtil.getNMSClass("EntityTypes"), "c").get(null);
        Map<Class, String> d = new SafeField<Map<Class, String>>(ReflectionUtil.getNMSClass("EntityTypes"), "d").get(null);
        Map<Class, Integer> f = new SafeField<Map<Class, Integer>>(ReflectionUtil.getNMSClass("EntityTypes"), "f").get(null);
        Map<String, Integer> g = new SafeField<Map<String, Integer>>(ReflectionUtil.getNMSClass("EntityTypes"), "g").get(null);

        Iterator<String> i = c.keySet().iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals(name)) {
                i.remove();
            }
        }

        Iterator<Class> i2 = d.keySet().iterator();
        while (i2.hasNext()) {
            Class cl = i2.next();
            if (cl.getCanonicalName().equals(clazz.getCanonicalName())) {
                i2.remove();
            }
        }

        Iterator<Class> i3 = f.keySet().iterator();
        while (i2.hasNext()) {
            Class cl = i3.next();
            if (cl.getCanonicalName().equals(clazz.getCanonicalName())) {
                i3.remove();
            }
        }

        Iterator<String> i4 = g.keySet().iterator();
        while (i4.hasNext()) {
            String s = i4.next();
            if (s.equals(name)) {
                i4.remove();
            }
        }

        c.put(name, clazz);
        d.put(clazz, name);
        f.put(clazz, id);
        g.put(name, id);
    }

    public static EchoPetPlugin getInstance() {
        return (EchoPetPlugin) getPluginInstance();
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
    public ModuleLogger getModuleLogger() {
        return LOGGER;
    }

    @Override
    public ModuleLogger getReflectionLogger() {
        return LOGGER_REFLECTION;
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
