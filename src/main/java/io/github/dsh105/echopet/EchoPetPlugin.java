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
import io.github.dsh105.echopet.commands.CommandComplete;
import io.github.dsh105.echopet.commands.PetAdminCommand;
import io.github.dsh105.echopet.commands.PetCommand;
import io.github.dsh105.echopet.commands.util.CommandManager;
import io.github.dsh105.echopet.commands.util.DynamicPluginCommand;
import io.github.dsh105.echopet.config.ConfigOptions;
import io.github.dsh105.echopet.data.AutoSave;
import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.PetData;
import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.listeners.*;
import io.github.dsh105.echopet.mysql.SQLPetHandler;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.SQLUtil;
import net.minecraft.server.v1_7_R2.EntityTypes;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;

public class EchoPetPlugin extends DSHPlugin {

    private CommandManager COMMAND_MANAGER;

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

    //public CommandMap CM;

    @Override
    public void onEnable() {
        super.onEnable();
        Logger.initiate(this, "EchoPet", "[EchoPet]");

        COMMAND_MANAGER = new CommandManager(this);
        // Make sure that the plugin is running under the correct version to prevent errors
        if (!VersionUtil.compareVersions()) {
            ConsoleLogger.log(ChatColor.RED + "EchoPet " + ChatColor.GOLD
                    + this.getDescription().getVersion() + ChatColor.RED
                    + " is only compatible with:");
            ConsoleLogger.log(ChatColor.RED + "    " + VersionUtil.getMinecraftVersion() + "-" + VersionUtil.getCraftBukkitVersion() + ".");
            ConsoleLogger.log(ChatColor.RED + "Initialisation failed. Please update the plugin.");

            DynamicPluginCommand cmd = new DynamicPluginCommand(this.cmdString, new String[0], "", "",
                    new VersionIncompatibleCommand(this.cmdString, prefix, ChatColor.YELLOW +
                    "EchoPet " + ChatColor.GOLD + VersionUtil.getPluginVersion() + ChatColor.YELLOW + " is only compatible with "
                    + ChatColor.GOLD + VersionUtil.getMinecraftVersion() + "-" + VersionUtil.getCraftBukkitVersion()
                    + ChatColor.YELLOW + ". Please update the plugin.",
                    "echopet.pet", ChatColor.YELLOW + "You are not allowed to do that."),
                    null, this);
            COMMAND_MANAGER.register(cmd);
            return;
        }

        PluginManager manager = getServer().getPluginManager();

        String[] header = {"EchoPet By DSH105", "---------------------",
                "Configuration for EchoPet 2",
                "See the EchoPet Wiki before editing this file"};
        try {
            mainConfig = this.getConfigManager().getNewConfig("config.yml", header);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.WARNING, "Configuration File [config.yml] generation failed.", e, true);
        }

        options = new ConfigOptions(mainConfig);

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
        this.cmdString = options.getCommandString();
        this.adminCmdString = options.getCommandString() + "admin";
        DynamicPluginCommand petCmd = new DynamicPluginCommand(this.cmdString, new String[0], "Create and manage your own custom pets.", "Use /" + this.cmdString + " help to see the command list.", new PetCommand(this.cmdString), null, this);
        petCmd.setTabCompleter(new CommandComplete());
        COMMAND_MANAGER.register(petCmd);
        COMMAND_MANAGER.register(new DynamicPluginCommand(this.adminCmdString, new String[0], "Create and manage the pets of other players.", "Use /" + this.adminCmdString + " help to see the command list.", new PetAdminCommand(this.adminCmdString), null, this));

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
    public void onDisable() {
        if (PH != null) {
            PH.removeAllPets();
        }
        if (dbPool != null) {
            dbPool.shutdown();
        }

        // Unregister the commands
        this.COMMAND_MANAGER.unregister();

        // Don't nullify instance until after we're done
        super.onDisable();
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
        } catch (NoSuchFieldException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Registration of Pet Entity [" + name + "] has failed. This Pet will not be available.", e, true);
        } catch (SecurityException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Registration of Pet Entity [" + name + "] has failed. This Pet will not be available.", e, true);
        } catch (IllegalArgumentException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Registration of Pet Entity [" + name + "] has failed. This Pet will not be available.", e, true);
        } catch (IllegalAccessException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Registration of Pet Entity [" + name + "] has failed. This Pet will not be available.", e, true);
        }
    }

    public static EchoPetPlugin getInstance() {
        return (EchoPetPlugin) getPluginInstance();
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
