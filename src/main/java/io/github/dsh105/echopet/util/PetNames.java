package io.github.dsh105.echopet.util;

import com.dsh105.dshutils.config.YAMLConfig;
import io.github.dsh105.echopet.config.ConfigOptions;
import io.github.dsh105.echopet.entity.Pet;
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