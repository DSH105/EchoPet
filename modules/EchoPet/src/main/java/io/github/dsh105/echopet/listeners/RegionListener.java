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

package io.github.dsh105.echopet.listeners;

import io.github.dsh105.echopet.compat.api.entity.IPet;
import io.github.dsh105.echopet.compat.api.plugin.EchoPet;
import io.github.dsh105.echopet.compat.api.util.Lang;
import io.github.dsh105.echopet.compat.api.util.WorldUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class RegionListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        IPet pet = EchoPet.getManager().getPet(p);
        if (pet != null) {
            if (!WorldUtil.allowRegion(event.getTo())) {
                EchoPet.getManager().removePet(pet, true);
                Lang.sendTo(p, Lang.ENTER_PET_DISABLED_REGION.toString());
            }
        }
    }
}