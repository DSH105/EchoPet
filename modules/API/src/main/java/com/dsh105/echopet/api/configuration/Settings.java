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

package com.dsh105.echopet.api.configuration;

import com.dsh105.commodus.configuration.Config;
import com.dsh105.commodus.configuration.Option;
import com.dsh105.commodus.configuration.OptionSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Settings extends OptionSet {

    // TODO: comments?
    public static final Option<String> COMMAND = optionString("command", "pet");
    public static final Option<String> BASE_CHAT_COLOUR = optionString("baseChatColour", "yellow");
    public static final Option<String> HIGHLIGHT_CHAT_COLOUR = optionString("highlightChatColour", "gold");
    public static final Option<Boolean> AUTO_UPDATE = option("autoUpdate", false, "If set to true, EchoPet will automatically download and install new updates from BukkitDev.");
    public static final Option<Boolean> CHECK_FOR_UPDATES = option("checkForUpdates", true, "Notifies when new updates are available");
    public static final Option<Boolean> SQL_ENABLE = option("sql.use", false);
    public static final Option<String> SQL_HOST = optionString("sql.host", "localhost");
    public static final Option<Integer> SQL_PORT = option("sql.port", 3306);
    public static final Option<String> SQL_DATABASE = optionString("sql.database", "EchoPet");
    public static final Option<String> SQL_USERNAME = optionString("sql.username", "non");
    public static final Option<String> SQL_PASSWORD = optionString("sql.password", "none");
    public static final Option<Boolean> STRIP_DIACRITICS = option("stripDiacriticsFromNames", true);
    public static final Option<Boolean> FIX_HUMAN_SKINS = option("enableHumanSkinFixing", true);
    public static final Option<Boolean> LOAD_SAVED_PETS = option("loadSavedPets", true, "Loads saved pets for players");
    public static final Option<Boolean> LOAD_SAVED_PETS_ON_WORLD_CHANGE = option("loadSavedPetsWorldChange", true, "Loads saved pets for players changing worlds.");
    public static final Option<Boolean> SEND_LOAD_MESSAGE = option("sendLoadMessage", true, "Sends a message to pet owners when pets are loaded.");
    public static final Option<Boolean> SEND_FORCE_MESSAGE = option("sendForceMessage", true, "Sends a message to pet owners when data values are forced");
    public static final Option<Boolean> CONVERT_DATA_FILE_TO_UUID = option("convertDataFileToUniqueId", true, "Converts the data file to use UUIDs instead of player names");
    public static final Option<Boolean> PET_NAME = option("petNames.%s", true);
    public static final Option<Boolean> PET_NAME_REGEX_MATCHING = option("petNamesRegexMatching", true);
    public static final Option<ArrayList<Map<String, String>>> PET_NAME_REGEX = option("petNamesRegex", new ArrayList<Map<String, String>>());
    public static final Option<Boolean> WORLDS_DEFAULT = option("worlds.enableByDefault", true);
    public static final Option<Boolean> WORLD = option("worlds.%s", WORLDS_DEFAULT.getValue());
    public static final Option<Boolean> WORLDGUARD_REGION_DEFAULT = option("worldguard.regions.allowByDefault", true);
    public static final Option<Boolean> WORLDGUARD_REGION = option("worldguard.regions.%s", WORLDGUARD_REGION_DEFAULT.getValue());
    public static final Option<Boolean> WORLDGUARD_REGION_ENTER = option("worldguard.regionEnterCheck", true);

    public Settings(Config config) {
        super(config);
    }

    @Override
    public void setDefaults() {
        try {
            for (Option option : getOptions()) {
                if (option.equals(PET_NAME)) {
                    setDefault(option, "My Pet");
                } else if (option.equals(WORLD)) {
                    setDefault(option, "world");
                } else if (option.equals(WORLDGUARD_REGION)) {
                    setDefault(option, "echopet");
                } else if (option.equals(PET_NAME_REGEX)) {
                    PET_NAME_REGEX.setValue(new ArrayList<Map<String, String>>() {
                        {
                            add(new HashMap<String, String>() {
                                {
                                    put(".*administrator.*", "false");
                                }
                            });
                        }
                    });
                } else {
                    setDefault(option);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set configuration defaults", e);
        }
    }
}