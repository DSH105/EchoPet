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

package com.dsh105.echopet.api.plugin.hook;

import com.dsh105.commodus.Version;
import com.dsh105.commodus.dependency.PluginDependencyProviderBase;
import com.dsh105.echopet.api.config.ConfigType;
import com.dsh105.echopet.api.config.Settings;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.listeners.RegionListener;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class WorldGuardProviderBase extends PluginDependencyProviderBase<WorldGuardPlugin> {

    public WorldGuardProviderBase(Plugin myPluginInstance) {
        super(myPluginInstance, "WorldGuard");
    }

    @Override
    public void onHook() {
        if (new Version(getDependency().getDescription().getVersion()).isCompatible("6.0")) {
            if (Settings.WORLDGUARD_REGION_ENTER.getValue()) {
                this.getHandlingPlugin().getServer().getPluginManager().registerEvents(new RegionListener(), this.getHandlingPlugin());
            }
        } else {
            throw new IllegalStateException("Only WorldGuard 6.0 and after are supported");
        }
    }

    @Override
    public void onUnhook() {

    }

    public boolean allowPets(Location location) {
        Boolean allowWorld = Settings.WORLD.getValue(location.getWorld().getName());
        return (allowWorld == null ? Settings.WORLDS_DEFAULT.getValue() : allowWorld) && allowRegion(location);
    }

    public boolean allowRegion(Location location) {
        boolean result = true;
        if (isHooked()) {
            WorldGuardPlugin wg = getDependency();
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

            for (ProtectedRegion region : set) {
                if (EchoPet.getConfig(ConfigType.GENERAL).get(Settings.WORLDGUARD_REGION.getPath(region.getId())) != null) {
                    Boolean allow = Settings.WORLDGUARD_REGION.getValue(region.getId());
                    result = allow == null ? Settings.WORLDGUARD_REGION_DEFAULT.getValue() : allow;
                }
            }

        }
        return result;
    }
}