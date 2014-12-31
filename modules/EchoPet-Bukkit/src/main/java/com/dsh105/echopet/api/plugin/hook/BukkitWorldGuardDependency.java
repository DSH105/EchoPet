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
import com.dsh105.commodus.bukkit.AbstractBukkitPluginDependency;
import com.dsh105.commodus.container.PositionContainer;
import com.dsh105.echopet.api.configuration.ConfigType;
import com.dsh105.echopet.api.configuration.Settings;
import com.dsh105.echopet.api.event.listeners.platform.BukkitListener;
import com.dsh105.echopet.api.hook.WorldGuardDependency;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class BukkitWorldGuardDependency extends AbstractBukkitPluginDependency<WorldGuardPlugin> implements WorldGuardDependency<Plugin> {

    public BukkitWorldGuardDependency(Plugin myPluginInstance) {
        super(myPluginInstance, "WorldGuard");
    }

    @Override
    public void onHook() {
        if (new Version(getDependency().getDescription().getVersion()).isCompatible("6.0")) {
            if (Settings.WORLDGUARD_REGION_ENTER.getValue()) {
                this.getHandlingPlugin().getServer().getPluginManager().registerEvents(new BukkitListener.RegionListener(), this.getHandlingPlugin());
            }
        } else {
            throw new IllegalStateException("Only WorldGuard 6.0 and after are supported");
        }
    }

    @Override
    public void onUnhook() {

    }

    @Override
    public boolean allowPets(PositionContainer position) {
        Location location = position.toBukkit();
        Boolean allowWorld = Settings.WORLD.getValue(location.getWorld().getName());
        return (allowWorld == null ? Settings.WORLDS_DEFAULT.getValue() : allowWorld) && allowRegion(position);
    }

    @Override
    public boolean allowRegion(PositionContainer position) {
        Location location = position.toBukkit();
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