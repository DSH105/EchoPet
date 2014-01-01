package io.github.dsh105.echopet.util;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.Hook;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;


public class WorldUtil {

    public static boolean allowPets(Location location) {
        boolean allowWorld = EchoPetPlugin.getInstance().getMainConfig().getBoolean("worlds." + location.getWorld().getName(), EchoPetPlugin.getInstance().getMainConfig().getBoolean("worlds.enableByDefault", true));
        return allowWorld && allowRegion(location);
    }

    public static boolean allowRegion(Location location) {
        WorldGuardPlugin wg = Hook.getWorldGuard();
        if (wg == null) {
            return true;
        }
        RegionManager regionManager = wg.getRegionManager(location.getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions(location);

        boolean result = true;
        boolean hasSet = false;
        boolean def = EchoPetPlugin.getInstance().getMainConfig().getBoolean("worldguard.regions.allowByDefault", true);

        ConfigurationSection cs = EchoPetPlugin.getInstance().getMainConfig().getConfigurationSection("worldguard.regions");
        for (ProtectedRegion region : set) {
            for (String key : cs.getKeys(false)) {
                if (!key.equalsIgnoreCase("allowByDefault") && !key.equalsIgnoreCase("regionEnterCheck")) {
                    if (region.getId().equals(key)) {
                        if (!hasSet) {
                            result = EchoPetPlugin.getInstance().getMainConfig().getBoolean("worldguard.regions." + key, true);
                            hasSet = true;
                        }
                    }
                }
            }
        }

        if (!hasSet) {
            result = def;
        }

        return result;
    }
}