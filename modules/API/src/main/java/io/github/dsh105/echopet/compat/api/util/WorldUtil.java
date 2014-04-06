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

package io.github.dsh105.echopet.compat.api.util;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.github.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class WorldUtil {

    public static boolean allowPets(Location location) {
        boolean allowWorld = EchoPet.getPlugin().getMainConfig().getBoolean("worlds." + location.getWorld().getName(), EchoPet.getPlugin().getMainConfig().getBoolean("worlds.enableByDefault", true));
        return allowWorld && allowRegion(location);
    }

    public static boolean allowRegion(Location location) {
        if (EchoPet.getPlugin().getWorldGuardProvider().isHooked()) {
            WorldGuardPlugin wg = EchoPet.getPlugin().getWorldGuardProvider().getDependency();
            if (wg == null) {
                return true;
            }
            RegionManager regionManager = wg.getRegionManager(location.getWorld());

            if (regionManager == null) {
                return true;
            }

            ApplicableRegionSet set = regionManager.getApplicableRegions(location);

            if (set.size() <= 0) {
                return true;
            }

            boolean result = true;
            boolean hasSet = false;
            boolean def = EchoPet.getPlugin().getMainConfig().getBoolean("worldguard.regions.allowByDefault", true);

            ConfigurationSection cs = EchoPet.getPlugin().getMainConfig().getConfigurationSection("worldguard.regions");
            for (ProtectedRegion region : set) {
                for (String key : cs.getKeys(false)) {
                    if (!key.equalsIgnoreCase("allowByDefault") && !key.equalsIgnoreCase("regionEnterCheck")) {
                        if (region.getId().equals(key)) {
                            if (!hasSet) {
                                result = EchoPet.getPlugin().getMainConfig().getBoolean("worldguard.regions." + key, true);
                                hasSet = true;
                            }
                        }
                    }
                }
            }

            return hasSet ? result : def;
        }
        return true;
    }
}