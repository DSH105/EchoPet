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

package com.dsh105.echopet.api.config;

import com.dsh105.commodus.config.Options;
import com.dsh105.commodus.config.YAMLConfig;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Settings extends Options {

    public Settings(YAMLConfig config) {
        super(config);
    }

    @Override
    public void setDefaults() {
        for (Setting setting : Setting.getOptions(Settings.class, Setting.class)) {
            if (setting.equals(PET_NAME)) {
                set(setting, "My Pet");
            } else if (setting.equals(WORLD)) {
                set(setting, Bukkit.getWorlds().get(0).getName());
            } else if (setting.equals(WORLDGUARD_REGION)) {
                set(setting, "echopet");
            } else if (setting.equals(PET_NAME_REGEX)) {
                PET_NAME_REGEX.setValue(this, new ArrayList<Map<String, String>>() {
                    {
                        add(new HashMap<String, String>() {
                            {
                                put(".*administrator.*", "false");
                            }
                        });
                    }
                });
            } else {
                set(setting);
            }
        }
    }

    public static final Setting<String> COMMAND = new Setting<>("command", "pet");
    public static final Setting<String> BASE_CHAT_COLOUR = new Setting<>("baseChatColour", "e");
    public static final Setting<String> HIGHLIGHT_CHAT_COLOUR = new Setting<>("highlightChatColour", "6");
    public static final Setting<Boolean> AUTO_UPDATE = new Setting<>("autoUpdate", false, "If set to true, EchoPet will automatically download and install new updates from BukkitDev.");
    public static final Setting<Boolean> CHECK_FOR_UPDATES = new Setting<>("checkForUpdates", true, "Notifies when new updates are available");
    public static final Setting<Boolean> SQL_OVERRIDE = new Setting<>("sql.overrideFile", true);
    public static final Setting<Boolean> SQL_ENABLE = new Setting<>("sql.use", false);
    public static final Setting<String> SQL_HOST = new Setting<>("sql.host", "localhost");
    public static final Setting<Integer> SQL_PORT = new Setting<>("sql.port", 3306);
    public static final Setting<String> SQL_DATABASE = new Setting<>("sql.database", "EchoPet");
    public static final Setting<String> SQL_USERNAME = new Setting<>("sql.username", "non");
    public static final Setting<String> SQL_PASSWORD = new Setting<>("sql.password", "none");
    public static final Setting<Boolean> STRIP_DIACRITICS = new Setting<>("stripDiacriticsFromNames", true);
    public static final Setting<Boolean> FIX_HUMAN_SKINS = new Setting<>("enableHumanSkinFixing", true);
    public static final Setting<Boolean> LOAD_SAVED_PETS = new Setting<>("loadSavedPets", true, "Loads saved pets for players");
    public static final Setting<Boolean> LOAD_SAVED_PETS_ON_WORLD_CHANGE = new Setting<>("loadSavedPetsWorldChange", true, "Loads saved pets for players changing worlds.");
    public static final Setting<Boolean> SEND_LOAD_MESSAGE = new Setting<>("sendLoadMessage", true, "Sends a message to pet owners when pets are loaded.");
    public static final Setting<Boolean> SEND_FORCE_MESSAGE = new Setting<>("sendForceMessage", true, "Sends a message to pet owners when data values are forced");
    public static final Setting<Boolean> CONVERT_DATA_FILE_TO_UUID = new Setting<Boolean>("convertDataFileToUniqueId", true, "Converts the data file to use UUIDs instead of player names");

    public static final Setting<Boolean> PET_NAME = new Setting<>("petNames.%s", true);
    public static final Setting<Boolean> PET_NAME_REGEX_MATCHING = new Setting<>("petNamesRegexMatching", true);
    public static final Setting<ArrayList<Map<String, String>>> PET_NAME_REGEX = new Setting<>("petNamesRegex", new ArrayList<Map<String, String>>());
    public static final Setting<Boolean> WORLDS_DEFAULT = new Setting<>("worlds.enableByDefault", true);
    public static final Setting<Boolean> WORLD = new Setting<>("worlds.%s", null);
    public static final Setting<Boolean> WORLDGUARD_REGION_DEFAULT = new Setting<>("worldguard.regions.allowByDefault", true);
    public static final Setting<Boolean> WORLDGUARD_REGION = new Setting<>("worldguard.regions.%s", null);
    public static final Setting<Boolean> WORLDGUARD_REGION_ENTER = new Setting<>("worldguard.regionEnterCheck", true);

}