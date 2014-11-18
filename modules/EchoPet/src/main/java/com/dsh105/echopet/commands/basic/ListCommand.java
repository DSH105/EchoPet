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

import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.api.config.Lang;
import com.dsh105.echopet.api.entity.AttributeManager;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.util.Perm;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.annotation.Authorize;
import com.dsh105.influx.annotation.Bind;
import com.dsh105.influx.annotation.Command;
import com.dsh105.influx.annotation.Default;
import com.dsh105.influx.dispatch.BukkitCommandEvent;
import com.dsh105.powermessage.core.PowerMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListCommand implements CommandListener {

    @Command(
            syntax = "list [inline]",
            desc = "View available pet and data types",
            help = "If [inline] is specified as \"true\", data types will be shown in the following format (as well as in hover information): \"<type> (<data>, <data>)\""
    )
    @Authorize(Perm.LIST)
    public boolean list(BukkitCommandEvent event, @Bind("inline") @Default("") boolean inline) {
        if (!(event.sender() instanceof Player) && event.var("inline").isEmpty()) {
            inline = true;
        }

        PowerMessage message = new PowerMessage()
                .then(EchoPet.getCommandManager().getResponder().getResponsePrefix())
                .then(EchoPet.getCommandManager().getResponder().format("{c1}Valid pet types: "));

        for (PetType type : PetType.values()) {
            boolean access = event.sender().hasPermission(Perm.TYPE.replace("<type>", type.storageName()));
            ChatColor format = access ? ChatColor.DARK_GREEN : ChatColor.DARK_RED;
            ChatColor highlight = access ? ChatColor.GREEN : ChatColor.RED;
            message.then(highlight + type.humanName());

            List<PetData> registeredData = AttributeManager.getModifier(type).getApplicableDataTypes();
            List<String> registeredStringData = new ArrayList<>();

            StringBuilder dataBuilder = new StringBuilder();
            dataBuilder.append(format).append("Valid data types: ");
            int length = 0;
            for (PetData data : registeredData) {
                boolean dataAccess = event.sender().hasPermission(Perm.DATA.replace("<type>", type.storageName()).replace("<data>", data.storageName()));
                if (dataAccess) {
                    registeredStringData.add(data.humanName());
                    if (length >= 35) {
                        dataBuilder.append("\n");
                        length = 0;
                    }
                    dataBuilder.append(highlight).append(data.humanName()).append(format).append(", ");
                    length += data.humanName().length();
                }
            }

            if (registeredStringData.size() <= 0) {
                message.tooltip(format + "No valid data types.");
            } else {
                String data = dataBuilder.substring(0, dataBuilder.length() - 2);
                message.tooltip(data);
                if (inline) {
                    message.then(" (" + StringUtil.combine(", ", registeredStringData) + ")").colour(format);
                }
            }
            message.then(format + ", " + highlight);
        }
        message.send(event.sender());
        event.respond(Lang.HOVER_TIP.getValue());
        return true;
    }
}