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

package com.dsh105.echopetv3;

import com.captainbern.minecraft.reflection.MinecraftReflection;
import com.captainbern.reflection.ClassTemplate;
import com.captainbern.reflection.Reflection;
import com.captainbern.reflection.SafeField;
import com.dsh105.command.*;
import com.dsh105.commodus.IdentUtil;
import com.dsh105.commodus.config.Options;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.commodus.config.YAMLConfigManager;
import com.dsh105.commodus.data.Metrics;
import com.dsh105.commodus.data.Updater;
import com.dsh105.commodus.dependency.PluginDependencyProvider;
import com.dsh105.commodus.logging.Level;
import com.dsh105.echopetv3.api.config.*;
import com.dsh105.echopetv3.api.entity.PetType;
import com.dsh105.echopetv3.api.entity.entitypet.EntityPet;
import com.dsh105.echopetv3.api.plugin.*;
import com.dsh105.echopetv3.api.plugin.hook.VanishProviderBase;
import com.dsh105.echopetv3.api.plugin.hook.WorldGuardProviderBase;
import com.dsh105.echopetv3.commands.IncompatiblePluginCommand;
import com.dsh105.echopetv3.commands.admin.PetAdminCommand;
import com.dsh105.echopetv3.commands.basic.PetCommand;
import com.dsh105.echopetv3.listeners.ChunkListener;
import com.dsh105.echopetv3.listeners.PetListener;
import com.dsh105.echopetv3.listeners.PetOwnerListener;
import com.dsh105.echopetv3.util.Perm;
import com.dsh105.echopetv3.util.TableMigrationUtil;
import com.dsh105.echopetv3.util.UUIDMigration;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static com.captainbern.reflection.matcher.Matchers.withType;

@Command(
        command = "echopet",
        description = "EchoPet - custom entities at your control",
        permission = Perm.ECHOPET,
        aliases = "ec"
)
public class EchoPetPlugin extends JavaPlugin implements EchoPetCore, CommandListener {

    private CommandManager commandManager;
    private PetManager manager;
    private BoneCP dbPool;

    protected YAMLConfigManager configManager;
    private HashMap<ConfigType, YAMLConfig> configFiles = new HashMap<>();
    private HashMap<ConfigType, Options> settings = new HashMap<>();
    private ArrayList<PluginDependencyProvider> providers = new ArrayList<>();

    // Update Checker stuff
    public boolean updateAvailable = false;
    public String updateName = "";
    public boolean updateChecked = false;
    public File file;

    @Override
    public void onEnable() {
        EchoPet.setCore(this);

        commandManager = new CommandManager(this, DEFAULT_PREFIX);

        try {
            Class.forName(EchoPet.INTERNAL_NMS_PATH + ".entity.EntityPetBase");
        } catch (ClassNotFoundException e) {
            // Make sure the server owner is aware
            EchoPet.LOG.console(Level.WARNING, "+----------------------+");
            EchoPet.LOG.console(Level.WARNING, "EchoPet is not compatible with this server version");
            EchoPet.LOG.console(Level.WARNING, "Please upgrade/downgrade to the appropriate version");
            EchoPet.LOG.console(Level.WARNING, "+----------------------+");

            // If it isn't already obvious enough...
            commandManager.register(new IncompatiblePluginCommand());
        }

        loadConfiguration();

        if (Settings.SQL_ENABLE.getValue()) {
            prepareSqlDatabase();
        }

        manager = dbPool != null ? new SimpleSQLPetManager() : new SimplePetManager();

        registerCommands();

        // Register custom entities
        for (PetType petType : PetType.values()) {
            registerEntity(petType.getEntityClass(), petType.humanName() + " Pet", petType.getRegistrationId());
        }

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

        commandManager.setResponsePrefix(Lang.PREFIX.getValue());
        commandManager.setFormatColour(ChatColor.getByChar(Settings.BASE_CHAT_COLOUR.getValue()));
        commandManager.setHighlightColour(ChatColor.getByChar(Settings.HIGHLIGHT_CHAT_COLOUR.getValue()));
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

    private void registerCommands() {
        // All sub commands are registered inside the classes themselves
        new PetCommand();
        new PetAdminCommand();
        commandManager.register(this);
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

    private void registerEntity(Class<? extends EntityPet> clazz, String name, int id) {
        // Initiate all our fields
        ClassTemplate entityTypesTemplate = new Reflection().reflect(MinecraftReflection.getMinecraftClass("EntityTypes"));
        List<SafeField<Map>> fields = entityTypesTemplate.getSafeFields(withType(Map.class));
        Map<String, Class> entityNameToClassMapping = (Map<String, Class>) fields.get(0).getAccessor().getStatic();
        Map<Class, String> classToEntityNameMapping = (Map<Class, String>) fields.get(1).getAccessor().getStatic();
        Map<Class, Integer> classToIdMapping = (Map<Class, Integer>) fields.get(3).getAccessor().getStatic();
        Map<String, Integer> entityNameToIdMapping = (Map<String, Integer>) fields.get(4).getAccessor().getStatic();

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
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public PetManager getPetManager() {
        return manager;
    }

    @Override
    public <T extends PluginDependencyProvider> T getProvider(Class<T> providerClass) {
        for (PluginDependencyProvider provider : providers) {
            if (providerClass.equals(provider.getClass())) {
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
            if (options.getClass().equals(settingsClass)) {
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

    @ParentCommand
    public boolean onCommand(CommandEvent event) {
        event.respond(Lang.PLUGIN_INFORMATION.getValue("version", EchoPet.getCore().getDescription().getVersion()));
        return true;
    }

    public class Update {
        @Command(
                command = "update",
                description = "Update the EchoPet plugin",
                permission = Perm.UPDATE
        )
        public boolean onUpdateCommand(CommandEvent event) {
            if (updateChecked) {
                new Updater(EchoPet.getCore(), 53655, getFile(), Updater.UpdateType.NO_VERSION_CHECK, true);
            } else {
                event.respond(Lang.UPDATE_NOT_AVAILABLE.getValue());
            }
            return true;
        }
    }
}
