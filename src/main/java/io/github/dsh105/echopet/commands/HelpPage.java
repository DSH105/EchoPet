package io.github.dsh105.echopet.commands;

import io.github.dsh105.echopet.EchoPetPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public enum HelpPage {
    NONE(0, ChatColor.RED + "No help page found."),

    GENERAL(1, ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " <type>:[data],[data];[name]",
            ChatColor.YELLOW + "    - Changes your current pet.",
            ChatColor.YELLOW + "    - Each data value is separated by a comma.",
            ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.type.<type>",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " <type>:[data],[data];[name <rider>:[data],[data];[name]",
            ChatColor.YELLOW + "    - Spawns a pet by your side with the specified rider.",
            ChatColor.YELLOW + "    - Each data value is separated by a comma.",
            ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.type.<type> and echopet.pet.type.<rider>",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " name <name>",
            ChatColor.YELLOW + "    - Set the name tag of your pet.",
            ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.name",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " remove",
            ChatColor.YELLOW + "    - Remove your current pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.remove"),

    RIDER(2, ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " rider <type>:[data],[data];[name]",
            ChatColor.YELLOW + "    - Changes the rider type of your current pet.",
            ChatColor.YELLOW + "    - Each data value is separated by a comma.",
            ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.type.<type>",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " name rider <name>",
            ChatColor.YELLOW + "    - Set the name tag of your pet's rider.",
            ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.name",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " riderremove",
            ChatColor.YELLOW + "    - Remove your pet's current rider.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.remove"),

    DEFAULT(3, ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " list",
            ChatColor.YELLOW + "    - Lists available pet types.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.list",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " info",
            ChatColor.YELLOW + "    - Provides info on your current pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.info",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " default set <type>:[data],[data] [rider]:[data],[data]",
            ChatColor.YELLOW + "    - Set the default pet for when you log in.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.default.set.type.<type> and echopet.pet.default.set.type.<rider>",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " default set current",
            ChatColor.YELLOW + "    - Set the default pet to your current pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.default.set.current",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " default remove",
            ChatColor.YELLOW + "    - Remove your default pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.default.remove"),

    SKILLS(4, ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " ride",
            ChatColor.YELLOW + "    - Ride your pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.ride",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " hat",
            ChatColor.YELLOW + "    - Have your pet ride on your head.",
            ChatColor.YELLOW + "    - Appears higher to the owner to prevent sight obstruction.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.hat",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " call",
            ChatColor.YELLOW + "    - Call your Pet to your side.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.call"),

    OTHER(5, ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " menu",
            ChatColor.YELLOW + "    - Open the Data Menu GUI for your Pet",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.menu",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " show",
            ChatColor.YELLOW + "    - Show your hidden Pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.show",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " hide",
            ChatColor.YELLOW + "    - Hide your currently active Pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.hide"),

    SELECTOR(6, ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " select",
            ChatColor.YELLOW + "    - Open the Pet Selector GUI.",
            ChatColor.DARK_RED + "    - Permission: echopet.pet.select",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " selector",
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