package com.github.dsh105.echopet.util;

import com.github.dsh105.echopet.EchoPet;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public enum AdminHelpPage {

	NONE(0,			ChatColor.RED + "No help page found."),

	GENERAL(1,		ChatColor.GOLD + "/" + EchoPet.getPluginInstance().adminCmdString + " <player> <type>:[data],[data];[name]",
			ChatColor.YELLOW + "    - Changes the current pet of a Player.",
			ChatColor.YELLOW + "    - Each data value is separated by a comma.",
			ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
			ChatColor.DARK_RED + "    - Permission: echopet.petadmin.type.<type>",

			ChatColor.GOLD + "/" + EchoPet.getPluginInstance().adminCmdString + " <player> <type>:[data],[data];[name <mount>:[data],[data];[name]",
			ChatColor.YELLOW + "    - Gives a Pet to a Player with the specified mount.",
			ChatColor.YELLOW + "    - Each data value is separated by a comma.",
			ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
			ChatColor.DARK_RED + "    - Permission: echopet.petadmin.type.<type> and echopet.petadmin.type.<mount>",

			ChatColor.GOLD + "/" + EchoPet.getPluginInstance().adminCmdString + " name <player> <name>",
			ChatColor.YELLOW + "    - Set the name tag of a Player's pet.",
			ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.",
			ChatColor.DARK_RED + "    - Permission: echopet.petadmin.name",

			ChatColor.GOLD + "/" + EchoPet.getPluginInstance().adminCmdString + " <player> remove",
			ChatColor.YELLOW + "    - Remove a Player's current pet.",
			ChatColor.DARK_RED + "    - Permission: echopet.petadmin.remove"),

	MOUNT(2, 		ChatColor.GOLD + "/" + EchoPet.getPluginInstance().adminCmdString + " mount <player> <type>:[data],[data];[name]",
			ChatColor.YELLOW + "    - Changes the mount type of a Player's current pet.",
			ChatColor.YELLOW + "    - Each data value is separated by a comma.",
			ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
			ChatColor.DARK_RED + "    - Permission: echopet.petadmin.type.<type>",

			ChatColor.GOLD + "/" + EchoPet.getPluginInstance().adminCmdString + " name <player> mount <name>",
			ChatColor.YELLOW + "    - Set the name tag of a Player's pet's mount.",
			ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.",
			ChatColor.DARK_RED + "    - Permission: echopet.petadmin.name",

			ChatColor.GOLD + "/" + EchoPet.getPluginInstance().adminCmdString + " mount <player> remove",
			ChatColor.YELLOW + "    - Remove a Player's pet's current mount.",
			ChatColor.DARK_RED + "    - Permission: echopet.petadmin.remove"),

	DEFAULT(3,		ChatColor.GOLD + "/" + EchoPet.getPluginInstance().adminCmdString + " list",
			ChatColor.YELLOW + "    - Lists available pet types.",
			ChatColor.DARK_RED + "    - Permission: echopet.petadmin.list",

			ChatColor.GOLD + "/" + EchoPet.getPluginInstance().adminCmdString + " info <player>",
			ChatColor.YELLOW + "    - Provides info on a Player's current pet.",
			ChatColor.DARK_RED + "    - Permission: echopet.petadmin.info",

			ChatColor.GOLD + "/" + EchoPet.getPluginInstance().adminCmdString + " default <player> set <type>:[data],[data] [mount]:[data],[data]",
			ChatColor.YELLOW + "    - Set the default pet for when a Player logs in.",
			ChatColor.DARK_RED + "    - Permission: echopet.petadmin.default.set.type.<type> and echopet.petadmin.default.set.type.<mount>",

			ChatColor.GOLD + "/" + EchoPet.getPluginInstance().adminCmdString + " default <player> set current",
			ChatColor.YELLOW + "    - Set the default pet of a Player to their current pet.",
			ChatColor.DARK_RED + "    - Permission: echopet.petadmin.default.set.current",

			ChatColor.GOLD + "/" + EchoPet.getPluginInstance().adminCmdString + " default <player> remove",
			ChatColor.YELLOW + "    - Remove a Player's default pet.",
			ChatColor.DARK_RED + "    - Permission: echopet.petadmin.default.remove"),

	SKILLS(4,		ChatColor.GOLD + "/" + EchoPet.getPluginInstance().adminCmdString + " ride <player>",
			ChatColor.YELLOW + "    - Force a Player to ride their pet.",
			ChatColor.DARK_RED + "    - Permission: echopet.petadmin.ride",

			ChatColor.GOLD + "/" + EchoPet.getPluginInstance().adminCmdString + " hat <player>",
			ChatColor.YELLOW + "    - Force a Player's pet to ride on their head.",
			ChatColor.YELLOW + "    - Appears higher to the owner to prevent sight obstruction.",
			ChatColor.DARK_RED + "    - Permission: echopet.petadmin.hat");

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
		}
		else if (args[0].equalsIgnoreCase("name")) {
			sender.sendMessage(ChatColor.RED + "------------ EchoPet Admin Help ------------");
			for (String s : AdminHelpPage.GENERAL.getLines()) {
				sender.sendMessage(s);
			}
			return true;
		}
		else if (args[0].equalsIgnoreCase("mount")) {
			sender.sendMessage(ChatColor.RED + "------------ EchoPet Admin Help ------------");
			for (String s : AdminHelpPage.MOUNT.getLines()) {
				sender.sendMessage(s);
			}
			return true;
		}
		else if (args[0].equalsIgnoreCase("remove")) {
			sender.sendMessage(ChatColor.RED + "------------ EchoPet Admin Help ------------");
			for (String s : AdminHelpPage.GENERAL.getLines()) {
				sender.sendMessage(s);
			}
			return true;
		}
		return false;
	}
}