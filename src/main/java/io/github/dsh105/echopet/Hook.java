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

package io.github.dsh105.echopet;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.plugin.Plugin;
import org.kitteh.vanish.VanishPlugin;


public class Hook {

    public static VanishPlugin getVNP() {
        Plugin plugin = EchoPetPlugin.getInstance().getServer().getPluginManager().getPlugin("VanishNoPacket");
        if (plugin == null || !(plugin instanceof VanishPlugin)) {
            return null;
        }
        return (VanishPlugin) plugin;
    }

    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = EchoPetPlugin.getInstance().getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }
}