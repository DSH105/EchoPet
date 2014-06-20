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

package com.dsh105.echopet.commands;

import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.plugin.EchoPet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;


public class CommandComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<String>();
        String cmdString = EchoPet.getPlugin().getCommandString();
        if (cmd.getName().equalsIgnoreCase(cmdString)) {
            String[] completions;
            if (args.length >= 2) {
                completions = getCompletions(args.length, args[args.length - 2]);
            } else {
                completions = getCompletions(args.length);
            }
            for (String s : completions) {
                if (s.startsWith(args[args.length - 1])) {
                    list.add(s);
                }
            }
        }
        return list;
    }

    private String[] getCompletions(int i) {
        switch (i) {
            case 0:
                return new String[]{EchoPet.getPlugin().getCommandString(), EchoPet.getPlugin().getCommandString() + "admin"};
            case 1:
                return new String[]{"bat", "blaze", "cavespider", "chicken", "cow", "creeper", "enderdragon",
                        "enderman", "ghast", "horse", "human", "irongolem", "magmacube", "mushroomcow", "ocelot", "pig",
                        "pigzombie", "sheep", "silverfish", "skeleton", "slime", "snowman", "spider", "squid",
                        "villager", "witch", "wither", "wolf", "zombie", "name", "rider", "list", "info", "default",
                        "ride", "hat", "call", "show", "hide", "menu", "select", "remove"};
        }
        return new String[0];
    }

    private String[] getCompletions(int i, String argBefore) {
        switch (i) {
            case 0:
                return getCompletions(i);
            case 1:
                return getCompletions(i);
            case 2:
                ArrayList<String> list = new ArrayList<String>();
                for (PetType pt : PetType.values()) {
                    if (argBefore.equalsIgnoreCase(pt.storageName().toLowerCase())) {
                        list.add(pt.storageName().toLowerCase());
                    }
                }
                if (argBefore.equalsIgnoreCase("name")) {
                    list.add("Pet");
                    list.add("rider");
                } else if (argBefore.equalsIgnoreCase("rider")) {
                    list.add("remove");
                    for (PetType pt : PetType.values()) {
                        list.add(pt.storageName().toLowerCase());
                    }
                } else if (argBefore.equalsIgnoreCase("default")) {
                    list.add("set");
                    list.add("remove");
                }
                return list.toArray(new String[list.size()]);
        }
        return new String[0];
    }
}