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

/*
 * This file is part of HoloAPI.
 *
 * HoloAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HoloAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HoloAPI.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.dsh105.echopet.hook;

import io.github.dsh105.echopet.compat.api.plugin.hook.IVanishProvider;
import io.github.dsh105.echopet.compat.api.plugin.hook.PluginDependencyProvider;
import io.github.dsh105.echopet.listeners.VanishListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.kitteh.vanish.VanishPlugin;

public class VanishProvider extends PluginDependencyProvider<VanishPlugin> implements IVanishProvider {

    public VanishProvider(Plugin myPluginInstance) {
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