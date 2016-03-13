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

package com.dsh105.echopet.compat.api.util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VersionIncompatibleCommand implements CommandExecutor {

    private String cmdLabel;
    private String pluginPrefix;
    private String msg;
    private String perm;
    private String permMsg;

    public VersionIncompatibleCommand(String cmdLabel, String pluginPrefix, String msg, String perm, String permMsg) {
        this.cmdLabel = cmdLabel;
        this.pluginPrefix = pluginPrefix;
        this.msg = msg;
        this.perm = perm;
        this.permMsg = permMsg;
    }


    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission(this.perm)) {
            commandSender.sendMessage(this.pluginPrefix + " " + this.msg);
        } else {
            commandSender.sendMessage(this.pluginPrefix + " " + this.permMsg);
        }
        return true;
    }
}