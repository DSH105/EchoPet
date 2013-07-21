package com.github.dsh105.echopet.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.data.PetData;
import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.data.UnorganisedPetData;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.menu.MenuOption;
import com.github.dsh105.echopet.menu.PetMenu;
import com.github.dsh105.echopet.util.HelpPage;
import com.github.dsh105.echopet.util.Lang;
import com.github.dsh105.echopet.util.MenuUtil;
import com.github.dsh105.echopet.util.PetUtil;
import com.github.dsh105.echopet.util.StringUtil;

public class PetCommand implements CommandExecutor {

	private EchoPet ec;
	private String cmdLabel;
	
	public PetCommand(EchoPet ec, String commandLabel) {
		this.ec = ec;
		this.cmdLabel = commandLabel;
	}
	
	//public static HashMap<String, Menu> openMenus = new HashMap<String, Menu>();
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
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
		
		// Setting the pet and mount names
		// Supports colour coding
		if (args.length >= 1 && args[0].equalsIgnoreCase("name")) {
			if (StringUtil.hpp("echopet.pet", "name", sender, false)) {
				Player p = (Player) sender;
				
				if (args.length == 1 || (args.length == 2 && args[1].equalsIgnoreCase("mount"))) {
					sender.sendMessage(ChatColor.RED + "------------ EchoPet Help - Pet Names ------------");
					sender.sendMessage(ChatColor.GOLD + "/" + this.cmdLabel + " name <name>");
					sender.sendMessage(ChatColor.YELLOW + "    - Set the name tag of your pet.");
					sender.sendMessage(ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.");
					sender.sendMessage(ChatColor.GOLD + "/" + this.cmdLabel + " name mount <name>");
					sender.sendMessage(ChatColor.YELLOW + "    - Set the name tag of your pet's mount.");
					sender.sendMessage(ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.");
					return true;
				}
				
				// Sets the name of the pet's mount if it exists
				if (args.length >= 3 && args[1].equalsIgnoreCase("mount")) {
					Pet pi = ec.PH.getPet(p);
					if (pi == null) {
						sender.sendMessage(Lang.NO_PET.toString());
						return true;
					}
					
					if (pi.getMount() == null) {
						sender.sendMessage(Lang.NO_MOUNT.toString());
						return true;
					}
					
					String name = StringUtil.replaceStringWithColours(StringUtil.combineSplit(2, args, " "));
					if (name.length() > 32) {
						sender.sendMessage(Lang.PET_NAME_TOO_LONG.toString());
						return true;
					}
					pi.getMount().setName(name);
					sender.sendMessage(Lang.NAME_MOUNT.toString()
							.replace("%type%", StringUtil.capitalise(pi.getPetType().toString().replace("_", " ")))
							.replace("%name%", name));
					return true;
				}
				
				// Sets the name of a player's pet
				else if (args.length >= 2) {
					Pet pi = ec.PH.getPet(p);
					if (pi == null) {
						sender.sendMessage(Lang.NO_PET.toString());
						return true;
					}
					
					String name = StringUtil.replaceStringWithColours(StringUtil.combineSplit(1, args, " "));
					if (name.length() > 32) {
						sender.sendMessage(Lang.PET_NAME_TOO_LONG.toString());
						return true;
					}
					pi.setName(name);
					sender.sendMessage(Lang.NAME_PET.toString()
							.replace("%type%", StringUtil.capitalise(pi.getPetType().toString().replace("_", " ")))
							.replace("%name%", name));
					return true;
				}
			} else sendError = false;
		}
		
		
		if (args.length == 1) {
			
			if (args[0].equalsIgnoreCase("menu")) {
				if (StringUtil.hpp("echopet.pet", "menu", sender, false)) {
					Player player = (Player) sender;
					Pet p = ec.PH.getPet(player);
					if (p == null) {
						sender.sendMessage(Lang.NO_PET.toString());
						return true;
					}
					ArrayList<MenuOption> options = MenuUtil.createOptionList(p.getPetType());
					PetMenu menu = new PetMenu(p, options);
					menu.open(true);
					return true;
				} else sendError = false;
			}
			
			if (args[0].equalsIgnoreCase("hat")) {
				if (StringUtil.hpp("echopet.pet", "hat", sender, false)) {
					Player p = (Player) sender;
					Pet pi = ec.PH.getPet(p);
					if (pi == null) {
						sender.sendMessage(Lang.NO_PET.toString());
						return true;
					}
					pi.setAsHat(!pi.isPetHat());
					if (pi.isPetHat()) {
						sender.sendMessage(Lang.HAT_PET_ON.toString());
					}
					else {
						sender.sendMessage(Lang.HAT_PET_OFF.toString());
					}
					return true;
				} else sendError = false;
			}
			
			if (args[0].equalsIgnoreCase("ride")) {
				if (StringUtil.hpp("echopet.pet", "ride", sender, false)) {
					Player p = (Player) sender;
					Pet pi = ec.PH.getPet(p);
					if (pi == null) {
						sender.sendMessage(Lang.NO_PET.toString());
						return true;
					}
					pi.ownerRidePet(!pi.isOwnerRiding());
					if (pi.isOwnerRiding()) {
						sender.sendMessage(Lang.RIDE_PET_ON.toString());
					}
					else {
						sender.sendMessage(Lang.RIDE_PET_OFF.toString());
					}
					return true;
				} else sendError = false;
			}
			
			// Help page 1
			if (args[0].equalsIgnoreCase("help")) {
				if (StringUtil.hpp("echopet.pet", "", sender, true)) {
					sender.sendMessage(ChatColor.RED + "------------ EchoPet Help 1/3 ------------");
					sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
					for (String s : HelpPage.getHelpPage(1)) {
						sender.sendMessage(s);
					}
					return true;
				} else sendError = false;
			}
			
			// List of all pet types
			if (args[0].equalsIgnoreCase("list")) {
				if (StringUtil.hpp("echopet.pet", "", sender, true)) {
					sender.sendMessage(ChatColor.RED + "------------ EchoPet Pet List ------------");
					for (String s : StringUtil.getPetList(sender)) {
						sender.sendMessage(s);
					}
					return true;
				} else sendError = false;
			}
			
			// Get all the info for a specific pet and send it to the player
			if (args[0].equalsIgnoreCase("info")) {
				if (StringUtil.hpp("echopet.pet", "info", sender, false)) {
					Pet pi = ec.PH.getPet((Player) sender);
					if (pi == null) {
						sender.sendMessage(Lang.NO_PET.toString());
						return true;
					}
					sender.sendMessage(ChatColor.RED + "------------ EchoPet Pet Info ------------");
					for (String s : PetUtil.generatePetInfo(pi)) {
						sender.sendMessage(s);
					}
					return true;
				} else sendError = false;
			}
			
			if (args[0].equalsIgnoreCase("remove")) {
				if (StringUtil.hpp("echopet.pet", "remove", sender, false)) {
					Pet pi = ec.PH.getPet((Player) sender);
					if (pi == null) {
						sender.sendMessage(Lang.NO_PET.toString());
						return true;
					}
					ec.PH.clearFileData("autosave", pi);
					ec.SPH.clearFromDatabase(pi.getOwner());
					ec.PH.removePet(pi);
					sender.sendMessage(Lang.REMOVE_PET.toString());
					return true;
				} else sendError = false;
			}
			
			else if (sendError) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(Lang.IN_GAME_ONLY.toString()
							.replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil .combineSplit(0, args, " "))));
					return true;
				}
				
				UnorganisedPetData UPD = PetUtil.formPetFromArgs(ec, sender, args[0]);
				if (UPD == null) {
					return true;
				}
				PetType petType = UPD.petType;
				ArrayList<PetData> petDataList = UPD.petDataList;
				
				if (petType == null || petDataList == null) {
					return true;
				}
				
				if (sender.hasPermission("echopet.pet.type.*") || StringUtil.hpp("echopet.pet", "type." + PetUtil.getPetPerm(petType), sender, false)) {
					Pet pi = ec.PH.createPet((Player) sender, petType);
					if (!petDataList.isEmpty()) {
						ec.PH.setData(pi, petDataList.toArray(new PetData[petDataList.size()]), true);
					}
					if (UPD.petName != null && !UPD.petName.equalsIgnoreCase("")) {
						pi.setName(UPD.petName);
					}
					ec.PH.saveFileData("autosave", pi);
					ec.SPH.saveToDatabase(pi, false);
					sender.sendMessage(Lang.CREATE_PET.toString()
							.replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
					return true;
				} else sendError = false;
			}
			
		}
		
		if (args.length == 2) {
			
			// Mount settings
			if (args[0].equalsIgnoreCase("mount")) {
				if (args[1].equalsIgnoreCase("remove")) {
					if (StringUtil.hpp("echopet.pet", "remove", sender, false)) {
						Pet pi = ec.PH.getPet((Player) sender);
						if (pi == null) {
							sender.sendMessage(Lang.NO_PET.toString());
							return true;
						}
						if (pi.getMount() == null) {
							sender.sendMessage(Lang.NO_MOUNT.toString());
							return true;
						}
						pi.removeMount();
						ec.PH.saveFileData("autosave", pi);
						ec.SPH.saveToDatabase(pi, false);
						sender.sendMessage(Lang.REMOVE_MOUNT.toString());
						return true;
					} else sendError = false;
				}
				else if (sendError) {
					if (!(sender instanceof Player)) {
						sender.sendMessage(Lang.IN_GAME_ONLY.toString()
								.replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil .combineSplit(0, args, " "))));
						return true;
					}
					
					Pet pi = ec.PH.getPet((Player) sender);
					
					if (pi == null) {
						sender.sendMessage(Lang.NO_PET.toString());
						return true;
					}
					
					UnorganisedPetData UPD = PetUtil.formPetFromArgs(ec, sender, args[1]);
					if (UPD == null) {
						return true;
					}
					PetType petType = UPD.petType;
					ArrayList<PetData> petDataList = UPD.petDataList;
					
					if (petType == null || petDataList == null) {
						return true;
					}
					
					if (!ec.DO.allowMounts(petType)) {
						sender.sendMessage(Lang.MOUNTS_DISABLED.toString()
								.replace("%type%", StringUtil.capitalise(petType.toString().replace("_", " "))));
						return true;
					}
					
					if (sender.hasPermission("echopet.pet.type.*") || StringUtil.hpp("echopet.pet", "type." + PetUtil.getPetPerm(petType), sender, false)) {
						pi.createMount(petType);
						if (!petDataList.isEmpty()) {
							ec.PH.setData(pi, petDataList.toArray(new PetData[petDataList.size()]), true);
						}
						if (UPD.petName != null && !UPD.petName.equalsIgnoreCase("")) {
							pi.setName(UPD.petName);
						}
						ec.PH.saveFileData("autosave", pi);
						ec.SPH.saveToDatabase(pi, false);
						sender.sendMessage(Lang.CHANGE_MOUNT.toString()
								.replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
						return true;
					} else sendError = false;
				}
			}
			
			// Help pages 1 through to 3
			if (args[0].equalsIgnoreCase("help")) {
				if (StringUtil.hpp("echopet.pet", "", sender, true)) {
					if (StringUtil.isInt(args[1])) {
						sender.sendMessage(ChatColor.RED + "------------ EchoPet Help " + args[1] + "/3 ------------");
						sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
						for (String s : HelpPage.getHelpPage(Integer.parseInt(args[1]))) {
							sender.sendMessage(s);
						}
						return true;
					}
					sender.sendMessage(ChatColor.RED + "------------ EchoPet Help 1/4 ------------");
					sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
					for (String s : HelpPage.getHelpPage(1)) {
						sender.sendMessage(s);
					}
					return true;
				} else sendError = false;
			}
			
			if (args[0].equalsIgnoreCase("default")) {
				
				if (args[1].equalsIgnoreCase("remove")) {
					if (StringUtil.hpp("echopet.pet", "default.remove", sender, false) || sender.hasPermission("echopet.pet.default.*")) {
						String path = "default." + sender.getName() + ".";
						if (ec.getPetConfig().get(path + "pet.type") == null) {
							sender.sendMessage(Lang.NO_DEFAULT.toString());
							return true;
						}
						
						ec.PH.clearFileData("default", (Player) sender);
						ec.SPH.clearFromDatabase((Player) sender);
						sender.sendMessage(Lang.REMOVE_DEFAULT.toString());
						return true;
					} else sendError = false;
				}
			}
			
			else if (sendError) {
				UnorganisedPetData UPD = PetUtil.formPetFromArgs(ec, sender, args[0]);
				if (UPD == null) {
					return true;
				}
				PetType petType = UPD.petType;
				ArrayList<PetData> petDataList = UPD.petDataList;
				
				UnorganisedPetData UMD = PetUtil.formPetFromArgs(ec, sender, args[1]);
				if (UMD == null) {
					return true;
				}
				PetType mountType = UMD.petType;
				ArrayList<PetData> mountDataList = UMD.petDataList;
				
				if (petType == null || petDataList == null || mountType == null || mountDataList == null) {
					return true;
				}
				
				if (sender.hasPermission("echopet.pet.type.*") || (StringUtil.hpp("echopet.pet", "type." + PetUtil.getPetPerm(petType), sender, false)
						&& StringUtil.hpp("echopet.pet", "type." + PetUtil.getPetPerm(mountType), sender, false))) {
					Pet pi = ec.PH.createPet(((Player) sender), petType, mountType);
					if (!petDataList.isEmpty()) {
						ec.PH.setData(pi, petDataList.toArray(new PetData[petDataList.size()]), true);
					}
					if (UPD.petName != null && !UPD.petName.equalsIgnoreCase("")) {
						pi.setName(UPD.petName);
					}
					if (!mountDataList.isEmpty()) {
						ec.PH.setData(pi.getMount(), mountDataList.toArray(new PetData[mountDataList.size()]), true);
					}
					if (UMD.petName != null && !UMD.petName.equalsIgnoreCase("")) {
						pi.getMount().setName(UPD.petName);
					}
					ec.PH.saveFileData("autosave", pi);
					ec.SPH.saveToDatabase(pi, false);
					sender.sendMessage(Lang.CREATE_PET_WITH_MOUNT.toString()
							.replace("%type%", StringUtil.capitalise(petType.toString().replace("_", "")))
							.replace("%mtype%", StringUtil.capitalise(mountType.toString().replace("_", ""))));
					return true;
				} else sendError = false;
			}
			
		}
		
		if (args.length == 3) {
			
			if (args[0].equalsIgnoreCase("default")) {
				
				if (args[1].equalsIgnoreCase("set")) {
					if (args[2].equalsIgnoreCase("current")) {
						if (StringUtil.hpp("echopet.pet", "default.set.current", sender, false) || sender.hasPermission("echopet.pet.default.*")) {
							
							Pet pi = ec.PH.getPet(((Player) sender));
							if (pi == null) {
								sender.sendMessage(Lang.NO_PET.toString());
								return true;
							}
							
							ec.PH.saveFileData("default", pi);
							sender.sendMessage(Lang.SET_DEFAULT_TO_CURRENT.toString());
							return true;
						} else sendError = false;
					}
					
					else if (sendError) {
						UnorganisedPetData UPD = PetUtil.formPetFromArgs(ec, sender, args[2]);
						if (UPD == null) {
							return true;
						}
						PetType petType = UPD.petType;
						ArrayList<PetData> petDataList = UPD.petDataList;
						
						if (petType == null || petDataList == null) {
							return true;
						}
						
						if (sender.hasPermission("echopet.pet.default.set.type.*") || StringUtil.hpp("echopet.pet", "default.set.type" + PetUtil.getPetPerm(petType), sender, false)
								|| sender.hasPermission("echopet.pet.default.*")) {
							ec.PH.saveFileData("default", (Player) sender, UPD);
							sender.sendMessage(Lang.SET_DEFAULT.toString()
									.replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
							return true;
						} else sendError = false;
					}
				}
			}
		}
		
		if (args.length == 4) {
			
			if (args[0].equalsIgnoreCase("default")) {
				
				if (args[1].equalsIgnoreCase("set")) {
					UnorganisedPetData UPD = PetUtil.formPetFromArgs(ec, sender, args[2]);
					if (UPD == null) {
						return true;
					}
					PetType petType = UPD.petType;
					ArrayList<PetData> petDataList = UPD.petDataList;
					
					UnorganisedPetData UMD = PetUtil.formPetFromArgs(ec, sender, args[3]);
					if (UMD == null) {
						return true;
					}
					PetType mountType = UMD.petType;
					ArrayList<PetData> mountDataList = UMD.petDataList;
					
					if (petType == null || petDataList == null || mountType == null || mountDataList == null) {
						return true;
					}
					
					if ((StringUtil.hpp("echopet.pet", "default.set.type" + PetUtil.getPetPerm(petType), sender, false)
							&& StringUtil.hpp("echopet.pet", "default.set.type" + PetUtil.getPetPerm(petType), sender, false))
									|| sender.hasPermission("echopet.pet.default.*")
									|| sender.hasPermission("echopet.pet.default.set.type.*")) {
						ec.PH.saveFileData("default", (Player) sender, UPD, UMD);
						sender.sendMessage(Lang.SET_DEFAULT_WITH_MOUNT.toString()
								.replace("%type%", StringUtil.capitalise(petType.toString().replace("_", "")))
								.replace("%mtype%", StringUtil.capitalise(mountType.toString().replace("_", ""))));
						return true;
					} else sendError = false;
				}
			}
		}
		
		if (sendError) {
			// Something went wrong. Maybe the player didn't use a command correctly?
			// Send them a message with the exact command to make sure
			if (!HelpPage.sendRelevantHelpMessage(sender, args)) {
				sender.sendMessage(Lang.COMMAND_ERROR.toString()
						.replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil .combineSplit(0, args, " "))));
			}
		}
		return true;
	}
}
