package io.github.dsh105.echopet.util;

import io.github.dsh105.echopet.EchoPet;
import io.github.dsh105.echopet.Hook;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Project by DSH105
 */

public class WorldUtil {

	public static boolean allowPets(Location location) {
		boolean allowRegion = allowRegion(location);
		String worldName = location.getWorld().getName();
		Boolean allowWorld = EchoPet.getPluginInstance().getMainConfig().getBoolean("worlds." + worldName);
		if (allowWorld != null) {
			return allowWorld && allowRegion;
		}
		else {
			return EchoPet.getPluginInstance().getMainConfig().getBoolean("worlds.enableByDefault", true) && allowRegion;
		}
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
		boolean def = EchoPet.getPluginInstance().getMainConfig().getBoolean("worldguard.regions.allowByDefault", true);

		ConfigurationSection cs = EchoPet.getPluginInstance().getMainConfig().getConfigurationSection("worldguard.regions");
		for (ProtectedRegion region : set) {
			for (String key : cs.getKeys(false)) {
				if (region.getId().equals(key)) {
					if (!hasSet) {
						result = EchoPet.getPluginInstance().getMainConfig().getBoolean("worldguard.regions." + key, true);
						hasSet = true;
					}
					else if (result != def) {
						result = def;
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