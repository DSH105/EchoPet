package io.github.dsh105.echopet;

import io.github.dsh105.echopet.Updater.UpdateType;
import io.github.dsh105.echopet.api.EchoPetAPI;
import io.github.dsh105.echopet.commands.CommandComplete;
import io.github.dsh105.echopet.commands.CustomCommand;
import io.github.dsh105.echopet.commands.PetAdminCommand;
import io.github.dsh105.echopet.commands.PetCommand;
import io.github.dsh105.echopet.config.YAMLConfig;
import io.github.dsh105.echopet.config.YAMLConfigManager;
import io.github.dsh105.echopet.config.options.ConfigOptions;
import io.github.dsh105.echopet.data.AutoSave;
import io.github.dsh105.echopet.entity.living.data.PetData;
import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.entity.living.data.PetType;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.type.bat.EntityBatPet;
import io.github.dsh105.echopet.entity.living.type.blaze.EntityBlazePet;
import io.github.dsh105.echopet.entity.living.type.cavespider.EntityCaveSpiderPet;
import io.github.dsh105.echopet.entity.living.type.chicken.EntityChickenPet;
import io.github.dsh105.echopet.entity.living.type.cow.EntityCowPet;
import io.github.dsh105.echopet.entity.living.type.creeper.EntityCreeperPet;
import io.github.dsh105.echopet.entity.living.type.enderdragon.EntityEnderDragonPet;
import io.github.dsh105.echopet.entity.living.type.enderman.EntityEndermanPet;
import io.github.dsh105.echopet.entity.living.type.ghast.EntityGhastPet;
import io.github.dsh105.echopet.entity.living.type.giant.EntityGiantPet;
import io.github.dsh105.echopet.entity.living.type.horse.EntityHorsePet;
import io.github.dsh105.echopet.entity.living.type.irongolem.EntityIronGolemPet;
import io.github.dsh105.echopet.entity.living.type.magmacube.EntityMagmaCubePet;
import io.github.dsh105.echopet.entity.living.type.mushroomcow.EntityMushroomCowPet;
import io.github.dsh105.echopet.entity.living.type.ocelot.EntityOcelotPet;
import io.github.dsh105.echopet.entity.living.type.pig.EntityPigPet;
import io.github.dsh105.echopet.entity.living.type.pigzombie.EntityPigZombiePet;
import io.github.dsh105.echopet.entity.living.type.sheep.EntitySheepPet;
import io.github.dsh105.echopet.entity.living.type.silverfish.EntitySilverfishPet;
import io.github.dsh105.echopet.entity.living.type.skeleton.EntitySkeletonPet;
import io.github.dsh105.echopet.entity.living.type.slime.EntitySlimePet;
import io.github.dsh105.echopet.entity.living.type.snowman.EntitySnowmanPet;
import io.github.dsh105.echopet.entity.living.type.spider.EntitySpiderPet;
import io.github.dsh105.echopet.entity.living.type.squid.EntitySquidPet;
import io.github.dsh105.echopet.entity.living.type.villager.EntityVillagerPet;
import io.github.dsh105.echopet.entity.living.type.witch.EntityWitchPet;
import io.github.dsh105.echopet.entity.living.type.wither.EntityWitherPet;
import io.github.dsh105.echopet.entity.living.type.wolf.EntityWolfPet;
import io.github.dsh105.echopet.entity.living.type.zombie.EntityZombiePet;
import io.github.dsh105.echopet.listeners.*;
import io.github.dsh105.echopet.logger.ConsoleLogger;
import io.github.dsh105.echopet.logger.Logger;
import io.github.dsh105.echopet.mysql.SQLConnection;
import io.github.dsh105.echopet.mysql.SQLPetHandler;
import io.github.dsh105.echopet.mysql.SQLRefresh;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.ReflectionUtil;
import io.github.dsh105.echopet.util.SQLUtil;
import net.minecraft.server.v1_6_R3.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_6_R3.CraftServer;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

public class EchoPet extends JavaPlugin {

    public static EchoPet plugin;

    private YAMLConfigManager configManager;
    private YAMLConfig petConfig;
    private YAMLConfig mainConfig;
    private YAMLConfig langConfig;
    public ConfigOptions options;
    public AutoSave AS;
    public PetHandler PH;
    public SQLPetHandler SPH;
    public SQLConnection sqlCon;
    private SQLRefresh sqlRefresh;
    public String prefix = "" + ChatColor.DARK_RED + "[" + ChatColor.RED + "PETS" + ChatColor.DARK_RED + "] " + ChatColor.RESET;

    private EchoPetAPI api;

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
        Logger.initiate();
        ConsoleLogger.initiate();

        // Make sure that the plugin is running under the correct version to prevent errors
        if (!(Version.getNMSPackage()).equalsIgnoreCase(ReflectionUtil.getVersionString())) {
            ConsoleLogger.log(ChatColor.RED + "EchoPet " + ChatColor.GOLD
                    + this.getDescription().getVersion() + ChatColor.RED
                    + " is only compatible with:");
            ConsoleLogger.log(ChatColor.RED + "    " + Version.getMinecraftVersion() + "-" + Version.getCraftBukkitVersion() + ".");
            ConsoleLogger.log(ChatColor.RED + "Initialisation failed. Please update the plugin.");
            return;
        }

        this.api = new EchoPetAPI();

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
            sqlCon = new SQLConnection(host, port, db, user, pass);
            Connection con = sqlCon.getConnection();
            if (con != null) {
                try {
                    con.prepareStatement("CREATE TABLE IF NOT EXISTS Pets (" +
                            "OwnerName varchar(255)," +
                            "PetType varchar(255)," +
                            "PetName varchar(255)," +
                            SQLUtil.serialise(PetData.values(), false) + ", " +
                            "MountPetType varchar(255), MountPetName varchar(255), " +
                            SQLUtil.serialise(PetData.values(), true) +
                            ", PRIMARY KEY (OwnerName)" +
                            ");").executeUpdate();
                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "`Pets` Table generation failed [MySQL DataBase: " + db + "].", e, true);
                }
                this.sqlRefresh = new SQLRefresh(getMainConfig().getInt("sql.timeout") * 20 * 60);
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
            if (Bukkit.getServer() instanceof CraftServer) {
                final Field f = CraftServer.class.getDeclaredField("commandMap");
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
        petCmd.setExecutor(new PetCommand(cmdString));
        petCmd.setTabCompleter(new CommandComplete());
        this.cmdString = cmdString;

        CustomCommand petAdminCmd = new CustomCommand(adminCmdString);
        CM.register("ec", petAdminCmd);
        petAdminCmd.setExecutor(new PetAdminCommand(adminCmdString));
        this.adminCmdString = adminCmdString;

        // Register listeners
        manager.registerEvents(new MenuListener(this), this);
        manager.registerEvents(new PetEntityListener(this), this);
        manager.registerEvents(new PetOwnerListener(this), this);

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
            final Updater.UpdateType updateType = this.getMainConfig().getBoolean("autoUpdate", false) ? UpdateType.DEFAULT : UpdateType.NO_DOWNLOAD;
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
        PH.removeAllPets();
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
        }
        return false;
    }

    public void registerEntity(Class<? extends EntityLivingPet> clazz, String name, int id) {
        Field field_c = null;
        try {
            field_c = EntityTypes.class.getDeclaredField("c");
            Field field_e = EntityTypes.class.getDeclaredField("e");
            field_c.setAccessible(true);
            field_e.setAccessible(true);

            Map<Class, String> c = (Map) field_c.get(field_c);
            Map<Class, Integer> e = (Map) field_e.get(field_e);

            Iterator i = c.keySet().iterator();
            while (i.hasNext()) {
                Class cl = (Class) i.next();
                if (cl.getCanonicalName().equals(clazz.getCanonicalName())) {
                    i.remove();
                }
            }

            Iterator i2 = e.keySet().iterator();
            while (i2.hasNext()) {
                Class cl = (Class) i2.next();
                if (cl.getCanonicalName().equals(clazz.getCanonicalName())) {
                    i2.remove();
                }
            }

            c.put(clazz, name);
            e.put(clazz, id);

        } catch (Exception e) {
            Logger.log(Logger.LogLevel.SEVERE, "Registration of Pet Entity [" + name + "] has failed. This Pet will not be available.", e, true);
        }
    }

    public EchoPetAPI getAPI() {
        return this.api;
    }

    public static EchoPet getPluginInstance() {
        return plugin;
    }

    public Connection getSqlCon() {
        return this.sqlCon.getConnection();
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
