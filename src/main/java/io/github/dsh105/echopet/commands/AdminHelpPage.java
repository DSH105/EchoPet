package io.github.dsh105.echopet.commands;

import io.github.dsh105.echopet.EchoPetPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public enum AdminHelpPage {

    NONE(0, ChatColor.RED + "No help page found."),

    GENERAL(1, ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " <player> <type>:[data],[data];[name]",
            ChatColor.YELLOW + "    - Changes the current pet of a Player.",
            ChatColor.YELLOW + "    - Each data value is separated by a comma.",
            ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.type.<type>",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " <player> <type>:[data],[data];[name] <rider>:[data],[data];[name]",
            ChatColor.YELLOW + "    - Gives a Pet to a Player with the specified rider.",
            ChatColor.YELLOW + "    - Each data value is separated by a comma.",
            ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.type.<type> and echopet.petadmin.type.<rider>",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " name <player> <name>",
            ChatColor.YELLOW + "    - Set the name tag of a Player's pet.",
            ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.name",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " remove <player>",
            ChatColor.YELLOW + "    - Remove a Player's current pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.remove"),

    RIDER(2, ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " rider <player> <type>:[data],[data];[name]",
            ChatColor.YELLOW + "    - Changes the rider type of a Player's current pet.",
            ChatColor.YELLOW + "    - Each data value is separated by a comma.",
            ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.type.<type>",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " name <player> rider <name>",
            ChatColor.YELLOW + "    - Set the name tag of a Player's pet's rider.",
            ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.name",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " rider <player> remove",
            ChatColor.YELLOW + "    - Remove a Player's pet's current rider.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.remove"),

    DEFAULT(3, ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " list",
            ChatColor.YELLOW + "    - Lists available pet types.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.list",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " info <player>",
            ChatColor.YELLOW + "    - Provides info on a Player's current pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.info",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " default <player> set <type>:[data],[data] [rider]:[data],[data]",
            ChatColor.YELLOW + "    - Set the default pet for when a Player logs in.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.default.set.type.<type> and echopet.petadmin.default.set.type.<rider>",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " default <player> set current",
            ChatColor.YELLOW + "    - Set the default pet of a Player to their current pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.default.set.current",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " default <player> remove",
            ChatColor.YELLOW + "    - Remove a Player's default pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.default.remove"),

    SKILLS(4, ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " ride <player>",
            ChatColor.YELLOW + "    - Force a Player to ride their pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.ride",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " hat <player>",
            ChatColor.YELLOW + "    - Force a Player's pet to ride on their head.",
            ChatColor.YELLOW + "    - Appears higher to the owner to prevent sight obstruction.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.hat",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().cmdString + " call <player>",
            ChatColor.YELLOW + "    - Call a Player's Pet to their side.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.call"),

    OTHER(5, ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " menu <player>",
            ChatColor.YELLOW + "    - Open the Data Menu GUI for a Player's Pet",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.menu",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " show <player>",
            ChatColor.YELLOW + "    - Show a Player's hidden Pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.show",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " hide <player>",
            ChatColor.YELLOW + "    - Hide a Player's currently active Pet.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.hide"),

    SELECTOR(6, ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " select <player>",
            ChatColor.YELLOW + "    - Open the Pet Selector GUI for another Player.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.select",

            ChatColor.GOLD + "/" + EchoPetPlugin.getInstance().adminCmdString + " selector <player>",
            ChatColor.YELLOW + "    - Give another Player the Pet Selector.",
            ChatColor.DARK_RED + "    - Permission: echopet.petadmin.selector");

    private int id;
    private String[] lines;

    AdminHelpPage(int id, String... lines) {
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
        for (AdminHelpPage hp : AdminHelpPage.values()) {
            if (hp.getId() == i) {
                return hp.getLines();
            }
        }
        return HelpPage.NONE.getLines();
    }

    public static boolean sendRelevantHelpMessage(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("default")) {
            sender.sendMessage(ChatColor.RED + "------------ EchoPet Admin Help ------------");
            for (String s : AdminHelpPage.DEFAULT.getLines()) {
                sender.sendMessage(s);
            }
            return true;
        } else if (args[0].equalsIgnoreCase("name")) {
            sender.sendMessage(ChatColor.RED + "------------ EchoPet Admin Help ------------");
            for (String s : AdminHelpPage.GENERAL.getLines()) {
                sender.sendMessage(s);
            }
            return true;
        } else if (args[0].equalsIgnoreCase("rider")) {
            sender.sendMessage(ChatColor.RED + "------------ EchoPet Admin Help ------------");
            for (String s : AdminHelpPage.RIDER.getLines()) {
                sender.sendMessage(s);
            }
            return true;
        } else if (args[0].equalsIgnoreCase("remove")) {
            sender.sendMessage(ChatColor.RED + "------------ EchoPet Admin Help ------------");
            for (String s : AdminHelpPage.GENERAL.getLines()) {
                sender.sendMessage(s);
            }
            return true;
        }
        return false;
    }
}