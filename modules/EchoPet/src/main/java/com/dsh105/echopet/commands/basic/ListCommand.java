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

package com.dsh105.echopet.commands.basic;

import com.dsh105.echopet.api.config.Lang;
import com.dsh105.echopet.api.entity.AttributeAccessor;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.util.Perm;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.annotation.Authorize;
import com.dsh105.influx.annotation.Command;
import com.dsh105.influx.dispatch.BukkitCommandEvent;
import com.dsh105.powermessage.core.PowerMessage;
import org.bukkit.ChatColor;

import java.util.List;

public class ListCommand implements CommandListener {

    @Command(
            syntax = "list",
            desc = "View available pet and data types"
    )
    @Authorize(Perm.LIST)
    public boolean list(BukkitCommandEvent event) {
        for (PetType type : PetType.values()) {
            boolean access = event.sender().hasPermission(Perm.TYPE.replace("<type>", type.storageName()));
            PowerMessage message = new PowerMessage("• " + (access ? ChatColor.GREEN : ChatColor.RED) + type.humanName());
            ChatColor format = access ? ChatColor.DARK_GREEN : ChatColor.DARK_RED;
            ChatColor highlight = access ? ChatColor.GREEN : ChatColor.RED;

            StringBuilder dataBuilder = new StringBuilder();
            List<PetData> registeredData = AttributeAccessor.getRegisteredData(type);
            if (!registeredData.isEmpty()) {
                dataBuilder.append(format).append("Valid data types: ");
                for (PetData data : registeredData) {
                    if (dataBuilder.length() >= 35) {
                        dataBuilder.append("\n");
                    }
                    dataBuilder.append(highlight).append(data.humanName()).append(format).append(", ");
                }
                message.tooltip(dataBuilder.substring(0, dataBuilder.length() - 2));
            }
            message.send(event.sender());
        }
        event.respond(Lang.HOVER_TIP.getValue());
        return true;
    }
}