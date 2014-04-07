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

import com.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public enum HelpPage {
    NONE(0, ChatColor.RED + "No help page found."),

    GENERAL(1, ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " <type>:[data],[data];[name]",
            ChatColor.YELLOW + "    - Changes your current pet.",
            ChatColor.YELLOW + "    - Each data value is separated by a comma.",
            ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.type.<type>",

            ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " <type>:[data],[data];[name <rider>:[data],[data];[name]",
            ChatColor.YELLOW + "    - Spawns a pet by your side with the specified rider.",
            ChatColor.YELLOW + "    - Each data value is separated by a comma.",
            ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.type.<type> and echopet.pet.type.<rider>",

            ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " name <name>",
            ChatColor.YELLOW + "    - Set the name tag of your pet.",
            ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.name",

            ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " remove",
            ChatColor.YELLOW + "    - Remove your current pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.remove"),

    RIDER(2, ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " rider <type>:[data],[data];[name]",
            ChatColor.YELLOW + "    - Changes the rider type of your current pet.",
            ChatColor.YELLOW + "    - Each data value is separated by a comma.",
            ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.type.<type>",

            ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " name rider <name>",
            ChatColor.YELLOW + "    - Set the name tag of your pet's rider.",
            ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.name",

            ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " riderremove",
            ChatColor.YELLOW + "    - Remove your pet's current rider.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.remove"),

    DEFAULT(3, ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " list",
            ChatColor.YELLOW + "    - Lists available pet types.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.list",

            ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " info",
            ChatColor.YELLOW + "    - Provides info on your current pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.info",

            ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " default set <type>:[data],[data] [rider]:[data],[data]",
            ChatColor.YELLOW + "    - Set the default pet for when you log in.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.default.set.type.<type> and echopet.pet.default.set.type.<rider>",

            ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " default set current",
            ChatColor.YELLOW + "    - Set the default pet to your current pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.default.set.current",

            ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " default remove",
            ChatColor.YELLOW + "    - Remove your default pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.default.remove"),

    SKILLS(4, ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " ride",
            ChatColor.YELLOW + "    - Ride your pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.ride",

            ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " hat",
            ChatColor.YELLOW + "    - Have your pet ride on your head.",
            ChatColor.YELLOW + "    - Appears higher to the owner to prevent sight obstruction.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.hat",

            ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " call",
            ChatColor.YELLOW + "    - Call your Pet to your side.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.call"),

    OTHER(5, ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " menu",
            ChatColor.YELLOW + "    - Open the Data Menu GUI for your Pet",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.menu",

            ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " show",
            ChatColor.YELLOW + "    - Show your hidden Pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.show",

            ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " hide",
            ChatColor.YELLOW + "    - Hide your currently active Pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.hide"),

    SELECTOR(6, ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " select",
            ChatColor.YELLOW + "    - Open the Pet Selector GUI.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.select",

            ChatColor.GOLD + "/" + EchoPet.getPlugin().getCommandString() + " selector",
            ChatColor.YELLOW + "    - Give yourself the Pet Selector.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.selector");

    private int id;
    private String[] lines;

    HelpPage(int id, String... lines) {
        this.id = id;
        this.lines = lines;
    }

    public String[] getLines() {
        return this.lines;
    }

    public int getId() {
        return this.id;
    }

    public static String[] getHelpPage(int i) {
        for (HelpPage hp : HelpPage.values()) {
            if (hp.getId() == i) {
                return hp.getLines();
            }
        }
        return HelpPage.NONE.getLines();
    }

    public static boolean sendRelevantHelpMessage(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("default")) {
            sender.sendMessage(ChatColor.RED + "------------ EchoPet Help ------------");
            for (String s : HelpPage.DEFAULT.getLines()) {
                sender.sendMessage(s);
            }
            return true;
        } else if (args[0].equalsIgnoreCase("name")) {
            sender.sendMessage(ChatColor.RED + "------------ EchoPet Help ------------");
            for (String s : HelpPage.GENERAL.getLines()) {
                sender.sendMessage(s);
            }
            return true;
        } else if (args[0].equalsIgnoreCase("rider")) {
            sender.sendMessage(ChatColor.RED + "------------ EchoPet Help ------------");
            for (String s : HelpPage.RIDER.getLines()) {
                sender.sendMessage(s);
            }
            return true;
        } else if (args[0].equalsIgnoreCase("remove")) {
            sender.sendMessage(ChatColor.RED + "------------ EchoPet Help ------------");
            for (String s : HelpPage.GENERAL.getLines()) {
                sender.sendMessage(s);
            }
            return true;
        }
        return false;
    }
}