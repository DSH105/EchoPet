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

package com.dsh105.echopet.api.commands.user;

import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.api.commands.influx.EchoPetCommandEvent;
import com.dsh105.echopet.api.configuration.Lang;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.attribute.AttributeManager;
import com.dsh105.echopet.api.entity.attribute.EntityAttribute;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.bridge.container.PlayerCommandSourceContainer;
import com.dsh105.echopet.util.Perm;
import com.dsh105.echopet.util.StringForm;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.annotation.Authorize;
import com.dsh105.influx.annotation.Bind;
import com.dsh105.influx.annotation.Command;
import com.dsh105.influx.annotation.Default;
import com.dsh105.powermessage.core.PowerMessage;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ListCommand implements CommandListener {

    @Command(
            syntax = "list [inline]",
            desc = "View available pet and data types",
            help = "If [inline] is specified as \"true\", data types will be shown in the following format (as well as in hover information): \"<type> (<data>, <data>)\""
    )
    @Authorize(Perm.LIST)
    public boolean list(EchoPetCommandEvent event, @Bind("inline") @Default("") boolean inline) {
        if (!(event.sender() instanceof PlayerCommandSourceContainer) && event.var("inline").isEmpty()) {
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

            List<EntityAttribute> registeredData = AttributeManager.getModifier(type).getValidAttributes();
            List<String> registeredStringData = new ArrayList<>();

            StringBuilder dataBuilder = new StringBuilder();
            dataBuilder.append(format).append("Valid data types: ");
            int length = 0;
            for (EntityAttribute attribute : registeredData) {
                StringForm form = StringForm.create(attribute);
                String name = form.getCaptalisedName();
                boolean dataAccess = event.sender().hasPermission(Perm.DATA.replace("<type>", type.storageName()).replace("<data>", form.getConfigName()));
                if (dataAccess) {
                    registeredStringData.add(name);
                    if (length >= 35) {
                        dataBuilder.append("\n");
                        length = 0;
                    }
                    dataBuilder.append(highlight).append(name).append(format).append(", ");
                    length += name.length();
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