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

package com.dsh105.echopet.hook;

import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.api.plugin.hook.IWorldGuardProvider;
import com.dsh105.echopet.api.plugin.hook.PluginDependencyProvider;
import com.dsh105.echopet.listeners.RegionListener;
import com.dsh105.echopetv3.api.config.ConfigType;
import com.dsh105.echopetv3.api.config.Settings;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class WorldGuardProvider extends PluginDependencyProvider<WorldGuardPlugin> implements IWorldGuardProvider {

    public WorldGuardProvider(Plugin myPluginInstance) {
        super(myPluginInstance, "WorldGuard");
    }

    @Override
    public void onHook() {
        if (EchoPet.getPlugin().getMainConfig().getBoolean("worldguard.regionEnterCheck", true)) {
            this.getHandlingPlugin().getServer().getPluginManager().registerEvents(new RegionListener(), this.getHandlingPlugin());
        }
    }

    @Override
    public void onUnhook() {

    }

    @Override
    public boolean allowPets(Location location) {
        return Settings.WORLD.getValue(location.getWorld().getName()) && allowRegion(location);
    }

    @Override
    public boolean allowRegion(Location location) {
        boolean result = true;
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

            ConfigurationSection cs = EchoPet.getPlugin().getMainConfig().getConfigurationSection("worldguard.regions");
            for (ProtectedRegion region : set) {
                if (EchoPet.getConfig(ConfigType.GENERAL).get(Settings.WORLDGUARD_REGION.getPath(region.getId())) != null) {
                    // Defaults are handled by the Settings manager
                    result = Settings.WORLDGUARD_REGION.getValue(region.getId());
                }
            }

        }
        return result;
    }
}