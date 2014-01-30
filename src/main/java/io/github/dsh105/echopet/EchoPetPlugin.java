package io.github.dsh105.echopet;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.dsh105.dshutils.Metrics;
import com.dsh105.dshutils.Updater;
import com.dsh105.dshutils.Version;
import com.dsh105.dshutils.command.CustomCommand;
import com.dsh105.dshutils.command.VersionIncompatibleCommand;
import com.dsh105.dshutils.config.YAMLConfig;
import com.dsh105.dshutils.config.YAMLConfigManager;
import com.dsh105.dshutils.logger.ConsoleLogger;
import com.dsh105.dshutils.logger.Logger;
import com.dsh105.dshutils.util.ReflectionUtil;
import io.github.dsh105.echopet.commands.CommandComplete;
import io.github.dsh105.echopet.commands.PetAdminCommand;
import io.github.dsh105.echopet.commands.PetCommand;
import io.github.dsh105.echopet.config.ConfigOptions;
import io.github.dsh105.echopet.data.AutoSave;
import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.living.PetData;
import io.github.dsh105.echopet.listeners.*;
import io.github.dsh105.echopet.mysql.SQLPetHandler;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.SQLUtil;
import net.minecraft.server.v1_7_R1.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class EchoPetPlugin extends JavaPlugin {

    private static EchoPetPlugin plugin;
    private static Random random = new Random();

    private YAMLConfigManager configManager;
    private YAMLConfig petConfig;
    private YAMLConfig mainConfig;
    private YAMLConfig langConfig;
    public ConfigOptions options;
    public AutoSave AS;
    public PetHandler PH;
    public SQLPetHandler SPH;
    public BoneCP dbPool;
    public String prefix = "" + ChatColor.DARK_RED + "[" + ChatColor.RED + "EchoPet" + ChatColor.DARK_RED + "] " + ChatColor.RESET;

    public String cmdString = "pet";
    public String adminCmdString = "petadmin";

    // Update data
    public boolean update = false;
    public String name = "";
    public long size = 0;
    public boolean updateChecked = false;

    public CommandMap CM;

    @Override
    public void onEnable() {
        plugin = this;
        Logger.initiate(this, "EchoPet", "[EchoPet]");
        ConsoleLogger.initiate(this);
        CustomCommand.initiate(this);

        // Make sure that the plugin is running under the correct version to prevent errors
        if (!(Version.getNMSPackage()).equalsIgnoreCase(ReflectionUtil.getVersionString(this))) {
            ConsoleLogger.log(ChatColor.RED + "EchoPet " + ChatColor.GOLD
                    + this.getDescription().getVersion() + ChatColor.RED
                    + " is only compatible with:");
            ConsoleLogger.log(ChatColor.RED + "    " + Version.getMinecraftVersion() + "-" + Version.getCraftBukkitVersion() + ".");
            ConsoleLogger.log(ChatColor.RED + "Initialisation failed. Please update the plugin.");

            try {
                Class craftServer = Class.forName("org.bukkit.craftbukkit." + ReflectionUtil.getVersionString(this) + ".CraftServer");
                if (craftServer.isInstance(Bukkit.getServer())) {
                    final Field f = craftServer.getDeclaredField("commandMap");
                    f.setAccessible(true);
                    CM = (CommandMap) f.get(Bukkit.getServer());
                }
                CustomCommand petCmd = new CustomCommand(cmdString);
                CM.register("ec", petCmd);
                petCmd.setExecutor(new VersionIncompatibleCommand(petCmd.getLabel(), prefix, ChatColor.YELLOW +
                        "EchoPet " + ChatColor.GOLD + Version.getPluginVersion() + ChatColor.YELLOW + " is only compatible with "
                        + ChatColor.GOLD + Version.getMinecraftVersion() + "-" + Version.getCraftBukkitVersion()
                        + ChatColor.YELLOW + ". Please update the plugin.",
                        "echopet.pet", ChatColor.YELLOW + "You are not allowed to do that."));
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.WARNING, "Registration of /pet command failed.", e, true);
            }

            return;
        }

        PluginManager manager = getServer().getPluginManager();

        configManager = new YAMLConfigManager(this);
        String[] header = {"EchoPet By DSH105", "---------------------",
                "Configuration for EchoPet 2",
                "See the EchoPet Wiki before editing this file"};
        try {
            mainConfig = configManager.getNewConfig("config.yml", header);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.WARNING, "Configuration File [config.yml] generation failed.", e, true);
        }

        options = new ConfigOptions(mainConfig);

        mainConfig.reloadConfig();

        try {
            petConfig = configManager.getNewConfig("pets.yml");
            petConfig.reloadConfig();
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.WARNING, "Configuration File [pets.yml] generation failed.", e, true);
        }

        String[] langHeader = {"EchoPet By DSH105", "---------------------",
                "Language Configuration File"};
        try {
            langConfig = configManager.getNewConfig("language.yml", langHeader);
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

        this.prefix = Lang.PREFIX.toString();

        PH = new PetHandler(this);
        SPH = new SQLPetHandler();

        if (options.useSql()) {
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
                            "MountPetType varchar(255), MountPetName varchar(255), " +
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

        // Register custom entities
        for (PetType pt : PetType.values()) {
            this.registerEntity(pt.getEntityClass(), pt.getDefaultName().replace(" ", ""), pt.getRegistrationId());
        }

        // Check whether to start AutoSave
        if (getMainConfig().getBoolean("autoSave")) {
            AS = new AutoSave(getMainConfig().getInt("autoSaveTimer"));
        }

        // Register custom commands
        // Command string based off the string defined in config.yml
        // By default, set to 'pet'
        // PetAdmin command draws from the original, with 'admin' on the end
        try {
            Class craftServer = Class.forName("org.bukkit.craftbukkit." + ReflectionUtil.getVersionString(this) + ".CraftServer");
            if (craftServer.isInstance(Bukkit.getServer())) {
                final Field f = craftServer.getDeclaredField("commandMap");
                f.setAccessible(true);
                CM = (CommandMap) f.get(Bukkit.getServer());
            }
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.WARNING, "Registration of /pet command failed.", e, true);
        }

        String cmdString = options.getCommandString();
        if (CM.getCommand(cmdString) != null) {
            ConsoleLogger.log(Logger.LogLevel.WARNING, "A command under the name " + ChatColor.RED + "/" + cmdString + ChatColor.YELLOW + " already exists. Pet Command temporarily registered under " + ChatColor.RED + "/ec:" + cmdString);
        }
        String adminCmdString = options.getCommandString() + "admin";
        if (CM.getCommand(adminCmdString) != null) {
            ConsoleLogger.log(Logger.LogLevel.WARNING, "A command under the name " + ChatColor.RED + "/" + adminCmdString + ChatColor.YELLOW + " already exists. Pet Admin Command temporarily registered under " + ChatColor.RED + "/ec:" + adminCmdString);
        }
        CustomCommand petCmd = new CustomCommand(cmdString);
        CM.register("ec", petCmd);
        petCmd.setExecutor(new PetCommand(petCmd.getLabel()));
        petCmd.setTabCompleter(new CommandComplete());
        this.cmdString = cmdString;

        CustomCommand petAdminCmd = new CustomCommand(adminCmdString);
        CM.register("ec", petAdminCmd);
        petAdminCmd.setExecutor(new PetAdminCommand(petAdminCmd.getLabel()));
        this.adminCmdString = adminCmdString;

        // Register listeners
        manager.registerEvents(new MenuListener(), this);
        manager.registerEvents(new PetEntityListener(), this);
        manager.registerEvents(new PetOwnerListener(), this);
        manager.registerEvents(new ChunkListener(), this);

        if (Hook.getVNP() != null) {
            manager.registerEvents(new VanishListener(), this);
        }

        if (Hook.getWorldGuard() != null && this.getMainConfig().getBoolean("worldguard.regionEnterCheck", true)) {
            manager.registerEvents(new RegionListener(), this);
        }


        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :(
        }

        this.checkUpdates();
    }

    protected void checkUpdates() {
        if (this.getMainConfig().getBoolean("checkForUpdates", true)) {
            final File file = this.getFile();
            final Updater.UpdateType updateType = this.getMainConfig().getBoolean("autoUpdate", false) ? Updater.UpdateType.DEFAULT : Updater.UpdateType.NO_DOWNLOAD;
            getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
                @Override
                public void run() {
                    Updater updater = new Updater(plugin, 53655, file, updateType, false);
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
    public void onDisable() {
        if (PH != null) {
            PH.removeAllPets();
        }
        if (dbPool != null) {
            dbPool.shutdown();
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
                sender.sendMessage(ChatColor.GOLD + "Main: " + this.options.getCommandString());
                sender.sendMessage(ChatColor.GOLD + "Admin: " + this.options.getCommandString() + "admin");
            } else {
                Lang.sendTo(sender, Lang.NO_PERMISSION.toString().replace("%perm%", "echopet.petadmin"));
                return true;
            }
        }
        return false;
    }

    public void registerEntity(Class<? extends EntityPet> clazz, String name, int id) {
        try {
            Field field_c = EntityTypes.class.getDeclaredField("c");
            Field field_d = EntityTypes.class.getDeclaredField("d");
            Field field_f = EntityTypes.class.getDeclaredField("f");
            Field field_g = EntityTypes.class.getDeclaredField("g");
            field_c.setAccessible(true);
            field_d.setAccessible(true);
            field_f.setAccessible(true);
            field_g.setAccessible(true);

            Map<String, Class> c = (Map) field_c.get(field_c);
            Map<Class, String> d = (Map) field_d.get(field_d);
            Map<Class, Integer> f = (Map) field_f.get(field_f);
            Map<String, Integer> g = (Map) field_g.get(field_g);

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
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.SEVERE, "Registration of Pet Entity [" + name + "] has failed. This Pet will not be available.", e, true);
        }
    }

    public static EchoPetPlugin getInstance() {
        return plugin;
    }

    public static Random random() {
        return random;
    }

    public YAMLConfig getPetConfig() {
        return this.petConfig;
    }

    public YAMLConfig getMainConfig() {
        return mainConfig;
    }

    public YAMLConfig getLangConfig() {
        return langConfig;
    }
}
