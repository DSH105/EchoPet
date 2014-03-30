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

package io.github.dsh105.echopet.util;

import com.dsh105.dshutils.config.YAMLConfig;
import io.github.dsh105.echopet.config.ConfigOptions;
import io.github.dsh105.echopet.api.entity.pet.Pet;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class PetNames {

    public static boolean allow(String input, Pet pet) {
        YAMLConfig config = ConfigOptions.instance.getConfig();
        String nameToCheck = ChatColor.stripColor(input);
        ConfigurationSection cs = config.getConfigurationSection("petNames");
        if (cs != null) {
            for (String key : cs.getKeys(false)) {
                if (key.equalsIgnoreCase(nameToCheck)) {
                    String value = config.getString("petNames." + key);
                    return pet.getOwner().hasPermission("echopet.pet.name.override") ? true : !(value.equalsIgnoreCase("deny") || value.equalsIgnoreCase("false"));
                }
            }
        }
        return true;
    }
}