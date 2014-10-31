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

import com.dsh105.commodus.dependency.PluginDependencyProviderBase;
import com.dsh105.echopet.api.hook.VanishProvider;
import com.dsh105.echopet.listeners.VanishListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.kitteh.vanish.VanishPlugin;

public class VanishProviderBase extends PluginDependencyProviderBase<VanishPlugin> implements VanishProvider {

    public VanishProviderBase(Plugin myPluginInstance) {
        super(myPluginInstance, "VanishNoPacket");
    }

    @Override
    public void onHook() {
        this.getHandlingPlugin().getServer().getPluginManager().registerEvents(new VanishListener(), this.getHandlingPlugin());
    }

    @Override
    public void onUnhook() {

    }

    @Override
    public boolean isVanished(Player player) {
        return this.isVanished(player.getName());
    }

    @Override
    public boolean isVanished(String player) {
        return this.isHooked() && this.getDependency().getManager().isVanished(player);
    }
}