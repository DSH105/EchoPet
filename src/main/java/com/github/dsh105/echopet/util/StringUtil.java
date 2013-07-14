package com.github.dsh105.echopet.util;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.data.PetData;
import com.github.dsh105.echopet.data.PetType;

public class StringUtil {
	
	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {return false;}
	}
	
	// Permission handler and blocking of Console
	public static boolean hp(String perm, CommandSender sender, boolean allowConsole) {
		if (sender instanceof Player) {
			if (sender.hasPermission("echopet.*")) {
				return true;
			}
			boolean b = true;
			if ((Boolean) EchoPet.getPluginInstance().DO.getConfigOption("requireDefaultPerm", false)) {
				if (!sender.hasPermission("echopet.pet.echopet")) {
					b = false;
				}
			}
			if (!(sender.hasPermission(perm) && b)) {
				sender.sendMessage(EchoPet.getPluginInstance().prefix + ChatColor.YELLOW + "" + ChatColor.ITALIC
						+ "Action denied. " + ChatColor.GOLD + perm
						+ ChatColor.YELLOW + " permission needed.");
			}
			return sender.hasPermission(perm) && b;
		} else {
			return allowConsole ? true : false;
		}
	}
	
	public static boolean hpp(String start, String perm, CommandSender sender, boolean allowConsole) {
		String fullPerm;
		if (perm.equalsIgnoreCase("")) {
			fullPerm = start;
		}
		else {
			fullPerm = start + "." + perm;
		}
		if (sender instanceof Player) {
			if (sender.hasPermission("echopet.*") || sender.hasPermission(start + ".*")) {
				return true;
			}
			boolean b = true;
			if ((Boolean) EchoPet.getPluginInstance().DO.getConfigOption("requireDefaultPerm", false)) {
				if (!sender.hasPermission("echopet.pet.echopet")) {
					b = false;
				}
			}
			if (!(sender.hasPermission(fullPerm) && b)) {
				sender.sendMessage(EchoPet.getPluginInstance().prefix + ChatColor.YELLOW + "" + ChatColor.ITALIC
						+ "Action denied. " + ChatColor.GOLD + fullPerm
						+ ChatColor.YELLOW + " permission needed.");
			}
			return sender.hasPermission(fullPerm) && b;
		} else {
			return allowConsole ? true : false;
		}
	}
	
	public static boolean hpp(String start, String perm, Player sender) {
		String fullPerm;
		if (perm.equalsIgnoreCase("")) {
			fullPerm = start;
		}
		else {
			fullPerm = start + "." + perm;
		}
		if (sender.hasPermission("echopet.*") || sender.hasPermission(start + ".*")) {
			return true;
		}
		boolean b = true;
		if ((Boolean) EchoPet.getPluginInstance().DO.getConfigOption("requireDefaultPerm", false)) {
			if (!sender.hasPermission("echopet.pet.echopet")) {
				b = false;
			}
		}
		if (!(sender.hasPermission(fullPerm) && b)) {
			sender.sendMessage(EchoPet.getPluginInstance().prefix + ChatColor.YELLOW + "" + ChatColor.ITALIC
					+ "Action denied. " + ChatColor.GOLD + fullPerm
					+ ChatColor.YELLOW + " permission needed.");
		}
		return sender.hasPermission(fullPerm) && b;
	}
	
	public static String capitalise(String s) {
		String finalString = "";
		if (s.contains(" ")) {
			StringBuilder builder = new StringBuilder();
			String[] sp = s.split(" ");
			for (String string : sp) {
				string = string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
				builder.append(string);
				builder.append(" ");
			}
			builder.deleteCharAt(builder.length() - 1);
			finalString = builder.toString();
		}
		else {
			finalString = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
		}
		return finalString;
	}
	
	public static String combineSplit(int startIndex, String[] string, String separator) {
		if (string == null) {
			return "";
		}
		else {
			StringBuilder builder = new StringBuilder();
			for (int i = startIndex; i < string.length; i++) {
				builder.append(string[i]);
				builder.append(separator);
			}
			builder.deleteCharAt(builder.length() - separator.length());
			return builder.toString();
		}
	}
	
	public static String replaceStringWithColours(String string) {
		string = string.replace("&0", ChatColor.BLACK.toString());
		string = string.replace("&1", ChatColor.DARK_BLUE.toString());
		string = string.replace("&2", ChatColor.DARK_GREEN.toString());
		string = string.replace("&3", ChatColor.DARK_AQUA.toString());
		string = string.replace("&4", ChatColor.DARK_RED.toString());
		string = string.replace("&5", ChatColor.DARK_PURPLE.toString());
		string = string.replace("&6", ChatColor.GOLD.toString());
		string = string.replace("&7", ChatColor.GRAY.toString());
		string = string.replace("&8", ChatColor.DARK_GRAY.toString());
		string = string.replace("&9", ChatColor.BLUE.toString());
		string = string.replace("&a", ChatColor.GREEN.toString());
		string = string.replace("&b", ChatColor.AQUA.toString());
		string = string.replace("&c", ChatColor.RED.toString());
		string = string.replace("&d", ChatColor.LIGHT_PURPLE.toString());
		string = string.replace("&e", ChatColor.YELLOW.toString());
		string = string.replace("&f", ChatColor.WHITE.toString());
		
		string = string.replace("&k", ChatColor.MAGIC.toString());
		string = string.replace("&l", ChatColor.BOLD.toString());
		string = string.replace("&m", ChatColor.STRIKETHROUGH.toString());
		string = string.replace("&n", ChatColor.UNDERLINE.toString());
		string = string.replace("&o", ChatColor.ITALIC.toString());
		string = string.replace("&r", ChatColor.RESET.toString());
		return string;
	}
	
	public static String replaceColoursWithString(String s) {
		s = s.replace(ChatColor.BLACK + "", "&0");
		s = s.replace(ChatColor.DARK_BLUE + "", "&1");
		s = s.replace(ChatColor.DARK_GREEN + "", "&2");
		s = s.replace(ChatColor.DARK_AQUA + "", "&3");
		s = s.replace(ChatColor.DARK_RED + "", "&4");
		s = s.replace(ChatColor.DARK_PURPLE + "", "&5");
		s = s.replace(ChatColor.GOLD + "", "&6");
		s = s.replace(ChatColor.GRAY + "", "&7");
		s = s.replace(ChatColor.DARK_GRAY + "", "&8");
		s = s.replace(ChatColor.BLUE + "", "&9");
		s = s.replace(ChatColor.GREEN + "", "&a");
		s = s.replace(ChatColor.AQUA + "", "&b");
		s = s.replace(ChatColor.RED + "", "&c");
		s = s.replace(ChatColor.LIGHT_PURPLE + "", "&d");
		s = s.replace(ChatColor.YELLOW + "", "&e");
		s = s.replace(ChatColor.WHITE + "", "&f");
		
		s = s.replace(ChatColor.MAGIC + "", "&k");
		s = s.replace(ChatColor.BOLD + "", "&l");
		s = s.replace(ChatColor.STRIKETHROUGH + "", "&m");
		s = s.replace(ChatColor.UNDERLINE + "", "&n");
		s = s.replace(ChatColor.ITALIC + "", "&o");
		s = s.replace(ChatColor.RESET + "", "&r");
		return s;
	}
	
	/*public static ArrayList<String> getPetListPage(EchoPet ec, CommandSender sender, int i) {
		
		if (ec.getDefaultOptions().getShowAllPetsInList()) {
			return getAllPetList(sender, i);
		}
		
		ArrayList<PetType> list = new ArrayList<PetType>();
		
		for (PetType type : PetType.values()) {
			if (sender instanceof Player) {
				if (sender.hasPermission("echopet." + type.toString().toLowerCase().replace("_", ""))) {
					list.add(type);
				}
			}
			else {
				list.add(type);
			}
		}
		
		int count = 0;
		ArrayList<String> finalListPage1 = new ArrayList<String>();
		ArrayList<String> finalListPage2 = new ArrayList<String>();
		ArrayList<String> finalListPage3 = new ArrayList<String>();
		ArrayList<String> finalListPage4 = new ArrayList<String>();
		ArrayList<String> finalListPage5 = new ArrayList<String>();
		ArrayList<String> finalListPage6 = new ArrayList<String>();
		for (PetType pt : list) {
			if (count <= 6) {
				finalListPage1.add(ChatColor.GOLD + "- " + capitalise(pt.toString().toLowerCase().replace("_", " ")));
				for (PetData data : pt.getAllowedDataTypes()) {
					finalListPage1.add(ChatColor.YELLOW + "    - " + capitalise(data.toString().toLowerCase().replace("_", "")));
				}
			}
			else if (count <= 12) {
				finalListPage2.add(ChatColor.GOLD + "- " + capitalise(pt.toString().toLowerCase().replace("_", " ")));
				for (PetData data : pt.getAllowedDataTypes()) {
					finalListPage2.add(ChatColor.YELLOW + "    - " + capitalise(data.toString().toLowerCase().replace("_", "")));
				}
			}
			else if (count <= 18) {
				finalListPage3.add(ChatColor.GOLD + "- " + capitalise(pt.toString().toLowerCase().replace("_", " ")));
				for (PetData data : pt.getAllowedDataTypes()) {
					finalListPage3.add(ChatColor.YELLOW + "    - " + capitalise(data.toString().toLowerCase().replace("_", "")));
				}
			}
			else if (count <= 24) {
				finalListPage4.add(ChatColor.GOLD + "- " + capitalise(pt.toString().toLowerCase().replace("_", " ")));
				for (PetData data : pt.getAllowedDataTypes()) {
					finalListPage4.add(ChatColor.YELLOW + "    - " + capitalise(data.toString().toLowerCase().replace("_", "")));
				}
			}
			else if (count <= 30) {
				finalListPage5.add(ChatColor.GOLD + "- " + capitalise(pt.toString().toLowerCase().replace("_", " ")));
				for (PetData data : pt.getAllowedDataTypes()) {
					finalListPage5.add(ChatColor.YELLOW + "    - " + capitalise(data.toString().toLowerCase().replace("_", "")));
				}
			}
			else if (count <= 36) {
				finalListPage6.add(ChatColor.GOLD + "- " + capitalise(pt.toString().toLowerCase().replace("_", " ")));
				for (PetData data : pt.getAllowedDataTypes()) {
					finalListPage6.add(ChatColor.YELLOW + "    - " + capitalise(data.toString().toLowerCase().replace("_", "")));
				}
			}
		}
		
		ArrayList<String> noPets = new ArrayList<String>(Arrays.asList("No allowed pets found."));
		
		switch (i) {
		case 1:
			return finalListPage1.isEmpty() ? noPets : finalListPage1;
			
		case 2:
			return finalListPage2.isEmpty() ? noPets : finalListPage2;
			
		case 3:
			return finalListPage3.isEmpty() ? noPets : finalListPage3;
			
		case 4:
			return finalListPage4.isEmpty() ? noPets : finalListPage4;
			
		case 5:
			return finalListPage5.isEmpty() ? noPets : finalListPage5;
			
		case 6:
			return finalListPage6.isEmpty() ? noPets : finalListPage6;
			
		default:
			return noPets;
		}
		
	}*/
	
	public static ArrayList<String> getPetList(CommandSender sender) {
		ArrayList<String> list = new ArrayList<String>();
		for (PetType pt : PetType.values()) {
			ChatColor color1 = ChatColor.GREEN;
			ChatColor color2 = ChatColor.DARK_GREEN;
			String separator = ", ";
			if (sender instanceof Player) {
				
				if (!sender.hasPermission("echopet." + pt.toString().toLowerCase().replace("_", ""))) {
					color1 = ChatColor.RED;
					color2 = ChatColor.DARK_RED;
				}
				
				StringBuilder builder = new StringBuilder();
				
				builder.append(color1 + "- " + capitalise(pt.toString().toLowerCase().replace("_", " ")));
				
				if (pt.getAllowedDataTypes().size() != 0) {
					builder.append(color2 + "    ");
					for (PetData data : pt.getAllowedDataTypes()) {
						builder.append(color2 + capitalise(data.toString().toLowerCase().replace("_", "")));
						builder.append(separator);
					}
					builder.deleteCharAt(builder.length() - separator.length());
				}
				
				list.add(builder.toString());
			}
			else {
				StringBuilder builder = new StringBuilder();
				
				builder.append(color1 + "- " + capitalise(pt.toString().toLowerCase().replace("_", " ")));
				
				if (pt.getAllowedDataTypes().size() != 0) {
					builder.append(color2 + " (");
					for (PetData data : pt.getAllowedDataTypes()) {
						builder.append(separator);
						builder.append(color2 + capitalise(data.toString().toLowerCase().replace("_", "")));
					}
					builder.deleteCharAt(builder.length() - separator.length());
					builder.append(color2 + ")");
				}
				
				list.add(builder.toString().replace(" )", ")"));
			}
		}
		return list;
	}

	public static String[] getHelpPage(int i, String cmdLabel) {
		if (i == 1) {
			String[] s = {ChatColor.GOLD + "/" + cmdLabel + " <type>:[data],[data];[name]",
					ChatColor.YELLOW + "    - Changes your current pet.",
					ChatColor.YELLOW + "    - Each data value is separated by a comma.",
					ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.type.<type>",
					
					ChatColor.GOLD + "/" + cmdLabel + " <type>:[data],[data];[name <mount>:[data],[data];[name]",
					ChatColor.YELLOW + "    - Spawns a pet by your side with the specified mount.",
					ChatColor.YELLOW + "    - Each data value is separated by a comma.",
					ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.type.<type> and echopet.pet.type.<mount>",
					
					ChatColor.GOLD + "/" + cmdLabel + " name <name>",
					ChatColor.YELLOW + "    - Set the name tag of your pet.",
					ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.name",
					
					ChatColor.GOLD + "/" + cmdLabel + " remove",
					ChatColor.YELLOW + "    - Remove your current pet.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.remove"};
			return s;
		}
		
		if (i == 2) {
			String[] s = {ChatColor.GOLD + "/" + cmdLabel + " mount <type>:[data],[data];[name]",
					ChatColor.YELLOW + "    - Changes the mount type of your current pet.",
					ChatColor.YELLOW + "    - Each data value is separated by a comma.",
					ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.type.<type>",
					
					ChatColor.GOLD + "/" + cmdLabel + " name mount <name>",
					ChatColor.YELLOW + "    - Set the name tag of your pet's mount.",
					ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.name",
					
					ChatColor.GOLD + "/" + cmdLabel + " mount remove",
					ChatColor.YELLOW + "    - Remove your pet's current mount.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.remove"};
			return s;
		}
		
		if (i == 3) {
			String[] s = {ChatColor.GOLD + "/" + cmdLabel + " list",
					ChatColor.YELLOW + "    - Lists available pet types.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.list",
					
					ChatColor.GOLD + "/" + cmdLabel + " info",
					ChatColor.YELLOW + "    - Provides info on your current pet.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.info",
					
					ChatColor.GOLD + "/" + cmdLabel + " default set <type>:[data],[data] [mount]:[data],[data]",
					ChatColor.YELLOW + "    - Set the default pet for when you log in.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.default.set.type.<type> and echopet.pet.default.set.type.<mount>",
					
					ChatColor.GOLD + "/" + cmdLabel + " default set current",
					ChatColor.YELLOW + "    - Set the default pet to your current pet.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.default.set.current",
					
					ChatColor.GOLD + "/" + cmdLabel + " default remove",
					ChatColor.YELLOW + "    - Remove your default pet.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.default.remove",};
			return s;
		}
		
		if (i == 4) {
			String[] s = {ChatColor.GOLD + "/" + cmdLabel + " ride",
					ChatColor.YELLOW + "    - Ride your pet.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.ride",
					
					ChatColor.GOLD + "/" + cmdLabel + " hat",
					ChatColor.YELLOW + "    - Have your pet ride on your head.",
					ChatColor.YELLOW + "    - Appears higher to the owner to prevent sight obstruction.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.hat",};
			return s;
		}
		
		String[] s = {ChatColor.RED + "No help page found."};
		return s;
	}

	public static String dataToString(ArrayList<PetData> data) {
		if (data.isEmpty()) {
			return " ";
		}
		StringBuilder builder = new StringBuilder();
		for (PetData pd : data) {
			builder.append(pd.getConfigOptionString());
			builder.append(", ");
		}
		builder.deleteCharAt(builder.length() - 2);
		return builder.toString();
	}
	
	public static String dataToString(ArrayList<PetData> data, ArrayList<PetData> mountData) {
		if (data.isEmpty()) {
			return " ";
		}
		StringBuilder builder = new StringBuilder();
		for (PetData pd : data) {
			builder.append(pd.getConfigOptionString());
			builder.append(", ");
		}
		for (PetData pd : mountData) {
			builder.append(pd.getConfigOptionString());
			builder.append("(Mount), ");
		}
		builder.deleteCharAt(builder.length() - 2);
		return builder.toString();
	}
	
	public static boolean sendHelpMessage(CommandSender sender, String[] args, String cmdLabel) {
		if (args[0].equalsIgnoreCase("default")) {
			sender.sendMessage(ChatColor.RED + "------------ EchoPet Help Intelligence ------------");
			sender.sendMessage(ChatColor.GOLD + "/" + cmdLabel + " default set <type>:[data],[data];<name> [mount]:[data],[data];<name>");
			sender.sendMessage(ChatColor.YELLOW + "    - Set the default pet for when you log in.");
			sender.sendMessage(ChatColor.GOLD + "/" + cmdLabel + " default remove");
			sender.sendMessage(ChatColor.YELLOW + "    - Remove your default pet.");
			return true;
		}
		else if (args[0].equalsIgnoreCase("name")) {
			sender.sendMessage(ChatColor.RED + "------------ EchoPet Help Intelligence ------------");
			sender.sendMessage(ChatColor.GOLD + "/" + cmdLabel + " name <name>");
			sender.sendMessage(ChatColor.YELLOW + "    - Set the name tag of your pet.");
			sender.sendMessage(ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.");
			sender.sendMessage(ChatColor.GOLD + "/" + cmdLabel + " name mount <name>");
			sender.sendMessage(ChatColor.YELLOW + "    - Set the name tag of your pet's mount.");
			sender.sendMessage(ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.");
			return true;
		}
		else if (args[0].equalsIgnoreCase("mount")) {
			sender.sendMessage(ChatColor.RED + "------------ EchoPet Help Intelligence ------------");
			sender.sendMessage(ChatColor.GOLD + "/" + cmdLabel + " mount <type>:[data],[data]");
			sender.sendMessage(ChatColor.YELLOW + "    - Changes the mount type of your current pet.");
			sender.sendMessage(ChatColor.YELLOW + "    - Each data value is separated by a comma.");
			sender.sendMessage(ChatColor.GOLD + "/" + cmdLabel + " name mount <name>");
			sender.sendMessage(ChatColor.YELLOW + "    - Set the name tag of your pet's mount.");
			sender.sendMessage(ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.");
			sender.sendMessage(ChatColor.GOLD + "/" + cmdLabel + " mount remove");
			sender.sendMessage(ChatColor.YELLOW + "    - Remove your pet's current mount.");
			return true;
		}
		else if (args[0].equalsIgnoreCase("remove")) {
			sender.sendMessage(ChatColor.RED + "------------ EchoPet Help Intelligence ------------");
			sender.sendMessage(ChatColor.GOLD + "/" + cmdLabel + " remove");
			sender.sendMessage(ChatColor.YELLOW + "    - Remove your current pet.");
			sender.sendMessage(ChatColor.GOLD + "/" + cmdLabel + " mount remove");
			sender.sendMessage(ChatColor.YELLOW + "    - Remove your pet's current mount.");
			return true;
		}
		return false;
	}
}
