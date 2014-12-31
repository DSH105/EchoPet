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

import com.dsh105.commodus.bukkit.AbstractBukkitPluginDependency;
import com.dsh105.echopet.api.event.listeners.platform.BukkitListener;
import com.dsh105.echopet.api.hook.VanishNoPacketDependency;
import org.bukkit.plugin.Plugin;
import org.kitteh.vanish.VanishPlugin;

public class BukkitVanishDependency extends AbstractBukkitPluginDependency<VanishPlugin> implements VanishNoPacketDependency<Plugin> {

    public BukkitVanishDependency(Plugin myPluginInstance) {
        super(myPluginInstance, "VanishNoPacket");
    }

    @Override
    public void onHook() {
        this.getHandlingPlugin().getServer().getPluginManager().registerEvents(new BukkitListener.VanishListener(), this.getHandlingPlugin());
    }

    @Override
    public void onUnhook() {

    }

    @Override
    public boolean isVanished(String playerName) {
        return this.isHooked() && this.getDependency().getManager().isVanished(playerName);
    }
}