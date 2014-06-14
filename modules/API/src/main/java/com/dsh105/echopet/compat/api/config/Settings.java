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

package com.dsh105.echopet.compat.api.config;

import com.dsh105.commodus.config.Options;
import com.dsh105.commodus.config.YAMLConfig;
import org.bukkit.Bukkit;

public class Settings extends Options {

    public Settings(YAMLConfig config) {
        super(config);
    }

    @Override
    public void setDefaults() {
        for (Setting setting : Setting.getOptions(Settings.class, Setting.class)) {
            if (setting.equals(PET_NAME)) {
                set(setting.getPath("My Pet"), get(setting), setting.getComments());
            } else if (setting.equals(WORLD)) {
                set(setting.getPath(Bukkit.getWorlds().get(0).getName()), get(setting), setting.getComments());
            } else if (setting.equals(WORLDGUARD_REGION)) {
                set(setting.getPath("echopet"), get(setting), setting.getComments());
            } else {
                set(setting.getPath(), setting.getDefaultValue(), setting.getComments());
            }
        }
    }

    public static final Setting<String> COMMAND = new Setting<String>("command", "pet");
    public static final Setting<Boolean> AUTO_UPDATE = new Setting<Boolean>("autoUpdate", false, "If set to true, EchoPet will automatically download and install new updates from BukkitDev.");
    public static final Setting<Boolean> CHECK_FOR_UPDATES = new Setting<Boolean>("checkForUpdates", true, "Notifies when new updates are available");
    public static final Setting<Boolean> SQL_OVERRIDE = new Setting<Boolean>("sql.overrideFile", true);
    public static final Setting<Boolean> SQL_ENABLE = new Setting<Boolean>("sql.use", false);
    public static final Setting<String> SQL_HOST = new Setting<String>("sql.host", "localhost");
    public static final Setting<Integer> SQL_PORT = new Setting<Integer>("sql.port", 3306);
    public static final Setting<String> SQL_DATABASE = new Setting<String>("sql.database", "EchoPet");
    public static final Setting<String> SQL_USERNAME = new Setting<String>("sql.username", "non");
    public static final Setting<String> SQL_PASSWORD = new Setting<String>("sql.password", "none");
    public static final Setting<Boolean> STRIP_DIACRITICS = new Setting<Boolean>("stripDiacriticsFromNames", true);
    public static final Setting<Boolean> FIX_HUMAN_SKINS = new Setting<Boolean>("enableHumanSkinFixing", true);
    public static final Setting<Boolean> LOAD_SAVED_PETS = new Setting<Boolean>("loadSavedPets", true, "Loads saved pets for players");
    public static final Setting<Boolean> LOAD_SAVED_PETS_ON_WORLD_CHANGE = new Setting<Boolean>("loadSavedPetsWorldChange", true, "Loads saved pets for players changing worlds.");
    public static final Setting<Boolean> SEND_LOAD_MESSAGE = new Setting<Boolean>("sendLoadMessage", true, "Sends a message to pet owners when pets are loaded.");
    public static final Setting<Boolean> SEND_FORCE_MESSAGE = new Setting<Boolean>("sendForceMessage", true, "Sends a message to pet owners when data values are forced");
    public static final Setting<Boolean> CONVERT_DATA_FILE_TO_UUID = new Setting<Boolean>("convertDataFileToUniqueId", true, "Converts the data file to use UUIDs instead of player names");

    public static final Setting<Boolean> PET_NAME = new Setting<Boolean>("petNames.%s", true);
    public static final Setting<Boolean> WORLDS_DEFAULT = new Setting<Boolean>("worlds.enableByDefault", true);
    public static final Setting<Boolean> WORLD = new Setting<Boolean>("worlds.%s", WORLDS_DEFAULT.getValue());
    public static final Setting<Boolean> WORLDGUARD_REGION_DEFAULT = new Setting<Boolean>("worldguard.regions.allowByDefault", true);
    public static final Setting<Boolean> WORLDGUARD_REGION = new Setting<Boolean>("worldguard.regions.%s", WORLDGUARD_REGION_DEFAULT.getValue());
    public static final Setting<Boolean> WORLDGUARD_REGION_ENTER = new Setting<Boolean>("worldguard.regionEnterCheck", true);

}