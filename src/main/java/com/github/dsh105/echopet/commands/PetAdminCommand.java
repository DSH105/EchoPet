package com.github.dsh105.echopet.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.data.PetHandler;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.util.Lang;
import com.github.dsh105.echopet.util.StringUtil;

public class PetAdminCommand implements CommandExecutor {
	
	private EchoPet ec;
	
	public PetAdminCommand(EchoPet ec, String commandLabel) {
		this.ec = ec;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		boolean sendError = true;
		
		
		if (args.length == 0) {
			
			if (StringUtil.hpp("echopet.pet", "", sender, true)) {
				PluginDescriptionFile pdFile = ec.getDescription();
				sender.sendMessage(ChatColor.RED + "-------- EchoPet --------");
				sender.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.YELLOW + "DSH105");
				sender.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.YELLOW + pdFile.getDescription());
				sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.YELLOW + pdFile.getVersion());
				sender.sendMessage(ChatColor.GOLD + "Website: " + ChatColor.YELLOW + pdFile.getWebsite());
				return true;
			} else sendError = false;
			
		}
		
		if (args.length == 2) {
			
			if (args[0].equalsIgnoreCase("remove")) {
				if (StringUtil.hpp("echopet.petadmin", "remove", sender, true)) {
					Player p = Bukkit.getPlayer(args[1]);
					if (p == null) {
						String path =  "autosave." + "." + p.getName();
						if (ec.getPetConfig().get(path + ".pet.type") == null) {
							sender.sendMessage(Lang.NO_PLAYER_FILE_DATA.toString().replace("%player%", args[1]));
							return true;
						}
						PetHandler.getInstance().clearFileData("autosave", p);
						sender.sendMessage(Lang.NULL_PLAYER.toString().replace("%player%", args[1]));
						return true;
					}
					else {
						Pet pet = PetHandler.getInstance().getPet(p);
						
						if (pet == null) {
							sender.sendMessage(Lang.PLAYER_NO_PET.toString().replace("%player%", p.getName()));
							return true;
						}

						ec.PH.clearFileData("autosave", pet);
						ec.SPH.clearFromDatabase(p);
						ec.PH.removePet(pet);

						sender.sendMessage(Lang.PLAYER_PET_REMOVED.toString().replace("%player%", p.getName()));
						p.sendMessage(Lang.REMOVE_PET.toString());
					}
				} else sendError = false;
			}
			
		}
		
		if (args.length >= 1 && args[0].equalsIgnoreCase("name")) {
			if (StringUtil.hpp("echopet.petadmin", "name", sender, true)) {
				
				if (args.length == 1 || (args.length == 2 && args[1].equalsIgnoreCase("mount"))) {
					sender.sendMessage(ChatColor.RED + "------------ EchoPet Help - Pet Names ------------");
					sender.sendMessage(ChatColor.GOLD + "/petadmin name <player> <name>");
					sender.sendMessage(ChatColor.YELLOW + "    - Set the name tag of a Player's pet.");
					sender.sendMessage(ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.");
					sender.sendMessage(ChatColor.GOLD + "/petadmin name <player> mount <name>");
					sender.sendMessage(ChatColor.YELLOW + "    - Set the name tag of a Player's pet's mount.");
					sender.sendMessage(ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.");
					return true;
				}
				
				if (args.length >= 3 && args[1].equalsIgnoreCase("mount")) {
					Player target = Bukkit.getPlayer(args[2]);
					if (target == null) {
						sender.sendMessage(Lang.NULL_PLAYER.toString().replace("%player%", args[2]));
						return true;
					}
					
					Pet pet = ec.PH.getPet(target);
					if (pet == null) {
						sender.sendMessage(Lang.PLAYER_NO_PET.toString().replace("%player%", target.getName()));
						return true;
					}
					
					if (pet.getMount() == null) {
						sender.sendMessage(Lang.PLAYER_NO_MOUNT.toString());
						return true;
					}
					
					String name = StringUtil.replaceStringWithColours(StringUtil.combineSplit(3, args, " "));
					if (name.length() > 32) {
						sender.sendMessage(Lang.PET_NAME_TOO_LONG.toString());
						return true;
					}
					pet.getMount().setName(name);
					sender.sendMessage(Lang.PLAYER_NAME_MOUNT.toString()
							.replace("%player%", target.getName())
							.replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " ")))
							.replace("%name%", name));
					target.sendMessage(Lang.NAME_MOUNT.toString()
							.replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " ")))
							.replace("%name%", name));
					return true;
				}
				else if (args.length >= 2) {
					Player target = Bukkit.getPlayer(args[1]);
					if (target == null) {
						sender.sendMessage(Lang.NULL_PLAYER.toString().replace("%player%", args[1]));
						return true;
					}
					
					Pet pet = ec.PH.getPet(target);
					if (pet == null) {
						sender.sendMessage(Lang.PLAYER_NO_PET.toString().replace("%player%", target.getName()));
						return true;
					}
					
					String name = StringUtil.replaceStringWithColours(StringUtil.combineSplit(2, args, " "));
					if (name.length() > 32) {
						sender.sendMessage(Lang.PET_NAME_TOO_LONG.toString());
						return true;
					}
					pet.getMount().setName(name);
					sender.sendMessage(Lang.PLAYER_NAME_PET.toString()
							.replace("%player%", target.getName())
							.replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " ")))
							.replace("%name%", name));
					target.sendMessage(Lang.NAME_PET.toString()
							.replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " ")))
							.replace("%name%", name));
					return true;
				}
				
			} else sendError = false;
		}
		
		if (sendError) {
			// Something went wrong. Maybe the player didn't use a command correctly?
			// Send them a message with the exact command to make sure
			/*if (!StringUtil.sendHelpMessage(sender, args)) {
				sender.sendMessage(Lang.COMMAND_ERROR.toString()
						.replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil .combineSplit(0, args, " "))));
			}*/
		}
		return true;
	}
}