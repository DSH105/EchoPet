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

package com.dsh105.echopet.api.commands.influx;

import com.dsh105.echopet.bridge.container.CommandSourceContainer;
import com.dsh105.influx.response.BukkitResponder;
import com.dsh105.influx.response.Responder;
import com.dsh105.influx.response.ResponseLevel;
import org.bukkit.command.CommandSender;

public class EchoPetBukkitResponder extends Responder<CommandSourceContainer> {

    private BukkitResponder responder;
    
    public EchoPetBukkitResponder(String responsePrefix) {
        super(responsePrefix);
        responder = new BukkitResponder(responsePrefix);
    }

    @Override
    public String getFormat(int index, ResponseLevel level) {
        return responder.getFormat(index, level);
    }

    @Override
    public void handleResponse(CommandSourceContainer sender, String message, ResponseLevel level) {
        responder.handleResponse((CommandSender) sender.get(), message, level);
    }
}