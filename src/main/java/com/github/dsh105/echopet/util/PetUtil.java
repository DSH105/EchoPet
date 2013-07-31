package com.github.dsh105.echopet.util;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.data.PetData;
import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.data.UnorganisedPetData;
import com.github.dsh105.echopet.entity.pet.IAgeablePet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.blaze.BlazePet;
import com.github.dsh105.echopet.entity.pet.creeper.CreeperPet;
import com.github.dsh105.echopet.entity.pet.enderman.EndermanPet;
import com.github.dsh105.echopet.entity.pet.horse.HorsePet;
import com.github.dsh105.echopet.entity.pet.horse.HorseType;
import com.github.dsh105.echopet.entity.pet.magmacube.MagmaCubePet;
import com.github.dsh105.echopet.entity.pet.ocelot.OcelotPet;
import com.github.dsh105.echopet.entity.pet.pigzombie.PigZombiePet;
import com.github.dsh105.echopet.entity.pet.sheep.SheepPet;
import com.github.dsh105.echopet.entity.pet.skeleton.SkeletonPet;
import com.github.dsh105.echopet.entity.pet.slime.SlimePet;
import com.github.dsh105.echopet.entity.pet.villager.VillagerPet;
import com.github.dsh105.echopet.entity.pet.wither.WitherPet;
import com.github.dsh105.echopet.entity.pet.wolf.WolfPet;
import com.github.dsh105.echopet.entity.pet.zombie.ZombiePet;

public class PetUtil {
	
	public static String getPetPerm(PetType petType) {
		return petType.toString().toLowerCase().replace("_", "");
	}
	
	public static UnorganisedPetData formPetFromArgs(EchoPet ec, CommandSender sender, String s, boolean petAdmin) {
		String admin = petAdmin ? "admin" : "";
		String petString = s;
		String dataString = "";
		PetData singlePetData = null;
		String name = "";
		
		if (s.contains(";")) {
			String[] split = s.split(";");
			if (split[0].contains(":")) {
				String[] splitt = split[0].split(":");
				if (splitt.length <= 1) {
					sender.sendMessage(Lang.STRING_ERROR.toString().replace("%string%", split[0]));
					return null;
				}
				petString = splitt[0].toLowerCase();
				dataString = splitt[1];
				name = split[1];
				if (!dataString.contains(",")) {
					if (isPetDataType(dataString)) {
						singlePetData = PetData.valueOf(dataString.toUpperCase());
						if (singlePetData == null) {
							singlePetData = PetData.valueOf(dataString.toUpperCase() + "_");
						}
						if (singlePetData == null) {
							sender.sendMessage(Lang.INVALID_PET_DATA_TYPE.toString()
									.replace("%data%", StringUtil.capitalise(dataString)));
							return null;
						}
					}
					else {
						sender.sendMessage(Lang.INVALID_PET_DATA_TYPE.toString()
								.replace("%data%", StringUtil.capitalise(dataString)));
						return null;
					}
				}
			}
			else if (split[1].contains(":")) {
				String[] splitt = split[1].split(":");
				if (splitt.length <= 1) {
					sender.sendMessage(Lang.STRING_ERROR.toString().replace("%string%", split[1]));
					return null;
				}
				petString = split[0].toLowerCase();
				name = splitt[0];
				dataString = splitt[1];
				if (!dataString.contains(",")) {
					if (isPetDataType(dataString)) {
						singlePetData = PetData.valueOf(dataString.toUpperCase());
						if (singlePetData == null) {
							singlePetData = PetData.valueOf(dataString.toUpperCase() + "_");
						}
						if (singlePetData == null) {
							sender.sendMessage(Lang.INVALID_PET_DATA_TYPE.toString()
									.replace("%data%", StringUtil.capitalise(dataString)));
							return null;
						}
					}
					else {
						sender.sendMessage(Lang.INVALID_PET_DATA_TYPE.toString()
								.replace("%data%", StringUtil.capitalise(dataString)));
						return null;
					}
				}
			}
			else {
				petString = split[0].toLowerCase();
				name = split[1];
			}
		}
		else if (s.contains(":")) {
			String[] split = s.split(":");
			if (split.length <= 1) {
				sender.sendMessage(Lang.STRING_ERROR.toString().replace("%string%", s));
				return null;
			}
			petString = split[0].toLowerCase();
			dataString = split[1];
			if (!dataString.contains(",")) {
				if (isPetDataType(dataString)) {
					singlePetData = PetData.valueOf(dataString.toUpperCase());
					if (singlePetData == null) {
						singlePetData = PetData.valueOf(dataString.toUpperCase() + "_");
					}
					if (singlePetData == null) {
						sender.sendMessage(Lang.INVALID_PET_DATA_TYPE.toString()
								.replace("%data%", StringUtil.capitalise(dataString)));
						return null;
					}
				}
				else {
					sender.sendMessage(Lang.INVALID_PET_DATA_TYPE.toString()
							.replace("%data%", StringUtil.capitalise(dataString)));
					return null;
				}
			}
		}
		
		ArrayList<PetData> petDataList = new ArrayList<PetData>();
		PetType petType = null;
		if (EnumUtil.isEnumType(PetType.class, petString)) {
			petType = PetType.valueOf(petString.toUpperCase());
		}
		if (petType == null) {
			sender.sendMessage(Lang.INVALID_PET_TYPE.toString()
					.replace("%type%", StringUtil.capitalise(petString)));
			return null;
		}
		
		if (dataString.contains(",")) {
			for (String dataTypeString : dataString.split(",")) {
				if (isPetDataType(dataTypeString)) {
					PetData dataTemp = PetData.valueOf(dataTypeString.toUpperCase()); 
					if (dataTemp != null) {
						petDataList.add(dataTemp);
					}
					else {
						sender.sendMessage(Lang.INVALID_PET_DATA_TYPE.toString()
								.replace("%data%", StringUtil.capitalise(dataTypeString)));
					}
				}
				else {
					sender.sendMessage(Lang.INVALID_PET_DATA_TYPE.toString()
							.replace("%data%", StringUtil.capitalise(dataTypeString)));
					return null;
				}
			}
		}
		else {
			if (singlePetData != null) {
				petDataList.add(singlePetData);
			}
		}
		
		if (!petDataList.isEmpty()) {
			for (PetData dataTemp : petDataList) {
				if (dataTemp != null) {
					if (!petType.isDataAllowed(dataTemp)) {
						sender.sendMessage(Lang.INVALID_PET_DATA_TYPE_FOR_PET.toString()
								.replace("%data%", StringUtil.capitalise(dataTemp.toString().replace("_", "")))
								.replace("%type%", StringUtil.capitalise(petType.toString().replace("_", " "))));
						return null;
					}
					if (!ec.DO.allowData(petType, dataTemp)) {
						sender.sendMessage(Lang.DATA_TYPE_DISABLED.toString()
								.replace("%data%", StringUtil.capitalise(dataTemp.toString().replace("_", ""))));
						return null;
					}
				}
			}
		}
		
		if (!ec.DO.allowPetType(petType)) {
			sender.sendMessage(Lang.PET_TYPE_DISABLED.toString()
					.replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
			return null;
		}
		
		boolean b = true;
		for(PetData dataTemp : petDataList) {
			if (!StringUtil.hpp("echopet.pet" + admin + ".data", dataTemp.getConfigOptionString().toLowerCase(), sender, false)) {
				b = false;
			}
		}
		
		if (!b) {
			return null;
		}
		
		UnorganisedPetData UPD = new UnorganisedPetData(petDataList, petType, name);
		return UPD;
	}
	
	/*private static boolean isPetType(String s) {
		try {
			PetType.valueOf(s.toUpperCase());
			return true;
		} catch (Exception e) {
			return false;
		}
	}*/
	
	private static boolean isPetDataType(String s) {
		try {
			PetData data = PetData.valueOf(s.toUpperCase());
			if (data == null) {
				PetData.valueOf(s.toUpperCase() + "_");
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static ArrayList<String> generatePetInfo(Pet pt) {
		ArrayList<String> info = new ArrayList<String>();
		info.add(ChatColor.GOLD + " - Pet Type: " + ChatColor.YELLOW + StringUtil.capitalise(pt.getPetType().toString()));
		info.add(ChatColor.GOLD + " - Name: " + ChatColor.YELLOW + pt.getPetName());
		if (pt instanceof IAgeablePet) {
			info.add(ChatColor.GOLD + " - Baby: " + ChatColor.YELLOW + ((IAgeablePet) pt).isBaby());
		}
		if (pt.getPetType() == PetType.ZOMBIE) {
			info.add(ChatColor.GOLD + " - Baby: " + ChatColor.YELLOW + ((ZombiePet) pt).isBaby());
		}
		if (pt.getPetType() == PetType.PIGZOMBIE) {
			info.add(ChatColor.GOLD + " - Baby: " + ChatColor.YELLOW + ((PigZombiePet) pt).isBaby());
		}
		info.addAll(generatePetDataInfo(pt));
		
		if (pt.getMount() != null) {
			info.add(ChatColor.RED + "Mount:");
			info.addAll(generatePetInfo(pt.getMount()));
		}
		
		return info;
	}
	
	public static ArrayList<String> generatePetDataInfo(Pet pt) {
		ArrayList<String> info = new ArrayList<String>();
		if (pt.getPetType() == PetType.BLAZE) {
			info.add(ChatColor.GOLD + " - On Fire: " + ChatColor.YELLOW + ((BlazePet) pt).isOnFire());
		}
		
		if (pt.getPetType() == PetType.CREEPER) {
			info.add(ChatColor.GOLD + " - Powered: " + ChatColor.YELLOW + ((CreeperPet) pt).isPowered());
		}
		
		if (pt.getPetType() == PetType.ENDERMAN) {
			info.add(ChatColor.GOLD + " - Screaming: " + ChatColor.YELLOW + ((EndermanPet) pt).isScreaming());
		}
		
		if (pt.getPetType() == PetType.SHEEP) {
			String color = "";
			color = ((SheepPet) pt).getColor() == null ? "Default" : StringUtil.capitalise(((SheepPet) pt).getColor().toString().replace("_", " "));
			info.add(ChatColor.GOLD + " - Wool Colour: " + ChatColor.YELLOW + color);
		}
		
		if (pt.getPetType() == PetType.VILLAGER){
			String prof = "";
			prof = ((VillagerPet) pt).getProfession() == null ? "Farmer" : StringUtil.capitalise(((VillagerPet) pt).getProfession().toString().replace("_", " "));
			info.add(ChatColor.GOLD + " - Profession: " + ChatColor.YELLOW + prof);
		}
		
		if (pt.getPetType() == PetType.OCELOT){
			String oType = "";
			oType = ((OcelotPet) pt).getCatType() == null ? "Default" : StringUtil.capitalise(((OcelotPet) pt).getCatType().toString().replace("_", " "));
			info.add(ChatColor.GOLD + " - Ocelot Type: " + ChatColor.YELLOW + oType);
		}
		
		if (pt.getPetType() == PetType.SLIME) {
			String size = "";
			size = StringUtil.capitalise(((SlimePet) pt).getSize() + "");
			String s = " (Small)";
			if (size.equals("2")) s = " (Medium)";
			if (size.equals("4")) s = " (Large)";
			info.add(ChatColor.GOLD + " - Slime Size: " + ChatColor.YELLOW + size + s);
		}
		
		if (pt.getPetType() == PetType.MAGMACUBE) {
			String size = "";
			size = StringUtil.capitalise(((MagmaCubePet) pt).getSize() + "");
			String s = " (Small)";
			if (size.equals("2")) s = " (Medium)";
			if (size.equals("4")) s = " (Large)";
			info.add(ChatColor.GOLD + " - Slime Size: " + ChatColor.YELLOW + size + s);
		}
		
		if (pt.getPetType() == PetType.WOLF) {
			info.add(ChatColor.GOLD + " - Tamed (Wolf): " + ChatColor.YELLOW + ((WolfPet) pt).isTamed());
			info.add(ChatColor.GOLD + " - Angry (Wolf): " + ChatColor.YELLOW + ((WolfPet) pt).isAngry());
			String color = "";
			color = ((WolfPet) pt).getCollarColor() == null ? "Red" : StringUtil.capitalise(((WolfPet) pt).getCollarColor().toString().replace("_", " "));
			info.add(ChatColor.GOLD + " - Collar Colour: " + ChatColor.YELLOW + color);
		}
		
		if (pt.getPetType() == PetType.SKELETON) {
			info.add(ChatColor.GOLD + " - Wither: " + ChatColor.YELLOW + ((SkeletonPet) pt).isWither());
		}
		
		if (pt.getPetType() == PetType.WITHER) {
			info.add(ChatColor.GOLD + " - Shielded: " + ChatColor.YELLOW + ((WitherPet) pt).isShielded());
		}
		
		if (pt.getPetType() == PetType.ZOMBIE) {
			info.add(ChatColor.GOLD + " - Villager: " + ChatColor.YELLOW + ((ZombiePet) pt).isVillager());
		}
		if (pt.getPetType() == PetType.PIGZOMBIE) {
			info.add(ChatColor.GOLD + " - Villager: " + ChatColor.YELLOW + ((PigZombiePet) pt).isVillager());
		}
		if (pt.getPetType() == PetType.HORSE) {
			HorseType ht = ((HorsePet) pt).getHorseType();
			info.add(ChatColor.GOLD + " - Type: " + ChatColor.YELLOW + StringUtil.capitalise(ht.toString().replace("_", " ")));
			if (ht == HorseType.NORMAL) {
				info.add(ChatColor.GOLD + " - Variant: " + ChatColor.YELLOW + StringUtil.capitalise(((HorsePet) pt).getVariant().toString().replace("_", " ")));
				info.add(ChatColor.GOLD + " - Marking: " + ChatColor.YELLOW + StringUtil.capitalise(((HorsePet) pt).getMarking().toString().replace("_", " ")));
			}
			info.add(ChatColor.GOLD + " - Chested: " + ChatColor.YELLOW + ((HorsePet) pt).isChested());
		}
		
		return info;
	}
}