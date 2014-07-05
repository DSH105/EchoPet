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

package com.dsh105.echopet.compat.api.util;

import com.dsh105.dshutils.config.YAMLConfig;
import com.dsh105.echopet.compat.api.config.ConfigOptions;
import com.dsh105.echopet.compat.api.entity.IPet;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class PetNames {

    public static boolean allow(String input, IPet pet) {
        YAMLConfig config = ConfigOptions.instance.getConfig();
        String nameToCheck = ChatColor.stripColor(input);
        ConfigurationSection cs = config.getConfigurationSection("petNames");
        if (cs != null) {
            for (String key : cs.getKeys(false)) {
                if (key.equalsIgnoreCase(nameToCheck)) {
                    String value = config.getString("petNames." + key);
                    return pet.getOwner().hasPermission("echopet.pet.name.override") || !(value.equalsIgnoreCase("deny") || value.equalsIgnoreCase("false"));
                }
            }
        }

        if (config.getBoolean("petNamesRegexMatching")) {
            ConfigurationSection csRegex = config.getConfigurationSection("petNamesRegex");
            if (csRegex != null) {
                for (String key : csRegex.getKeys(false)) {
                    if (key.matches(nameToCheck)) {
                        String value = config.getString("petNames." + key);
                        return pet.getOwner().hasPermission("echopet.pet.name.override") || !(value.equalsIgnoreCase("deny") || value.equalsIgnoreCase("false"));
                    }
                }
            }
        }
        return true;
    }
}