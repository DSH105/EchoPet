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

import com.dsh105.dshutils.util.EnumUtil;
import com.dsh105.dshutils.util.StringUtil;
import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.pet.*;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.PetStorage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PetUtil {

    public static String getPetPerm(PetType petType) {
        return petType.toString().toLowerCase().replace("_", "");
    }

    public static PetStorage formPetFromArgs(CommandSender sender, String s, boolean petAdmin) {
        String admin = petAdmin ? "admin" : "";
        String petString = s;
        String dataString = "";
        PetData singlePetData = null;
        String name = "";

        if (s.contains(";")) {
            String[] split = s.split(";");
            if (split.length <= 1) {
                Lang.sendTo(sender, Lang.STRING_ERROR.toString().replace("%string%", s));
                return null;
            }
            if (split[0].contains(":")) {
                String[] splitt = split[0].split(":");
                if (splitt.length <= 1) {
                    Lang.sendTo(sender, Lang.STRING_ERROR.toString().replace("%string%", split[0]));
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
                            Lang.sendTo(sender, Lang.INVALID_PET_DATA_TYPE.toString()
                                    .replace("%data%", StringUtil.capitalise(dataString)));
                            return null;
                        }
                    } else {
                        Lang.sendTo(sender, Lang.INVALID_PET_DATA_TYPE.toString()
                                .replace("%data%", StringUtil.capitalise(dataString)));
                        return null;
                    }
                }
            } else if (split[1].contains(":")) {
                String[] splitt = split[1].split(":");
                if (splitt.length <= 1) {
                    Lang.sendTo(sender, Lang.STRING_ERROR.toString().replace("%string%", split[1]));
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
                            Lang.sendTo(sender, Lang.INVALID_PET_DATA_TYPE.toString()
                                    .replace("%data%", StringUtil.capitalise(dataString)));
                            return null;
                        }
                    } else {
                        Lang.sendTo(sender, Lang.INVALID_PET_DATA_TYPE.toString()
                                .replace("%data%", StringUtil.capitalise(dataString)));
                        return null;
                    }
                }
            } else {
                petString = split[0].toLowerCase();
                name = split[1];
            }
        } else if (s.contains(":")) {
            String[] split = s.split(":");
            if (split.length <= 1) {
                Lang.sendTo(sender, Lang.STRING_ERROR.toString().replace("%string%", s));
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
                        Lang.sendTo(sender, Lang.INVALID_PET_DATA_TYPE.toString()
                                .replace("%data%", StringUtil.capitalise(dataString)));
                        return null;
                    }
                } else {
                    Lang.sendTo(sender, Lang.INVALID_PET_DATA_TYPE.toString()
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
            Lang.sendTo(sender, Lang.INVALID_PET_TYPE.toString()
                    .replace("%type%", StringUtil.capitalise(petString)));
            return null;
        }

        if (dataString.contains(",")) {
            for (String dataTypeString : dataString.split(",")) {
                if (isPetDataType(dataTypeString)) {
                    PetData dataTemp = PetData.valueOf(dataTypeString.toUpperCase());
                    if (dataTemp != null) {
                        petDataList.add(dataTemp);
                    } else {
                        Lang.sendTo(sender, Lang.INVALID_PET_DATA_TYPE.toString()
                                .replace("%data%", StringUtil.capitalise(dataTypeString)));
                    }
                } else {
                    Lang.sendTo(sender, Lang.INVALID_PET_DATA_TYPE.toString()
                            .replace("%data%", StringUtil.capitalise(dataTypeString)));
                    return null;
                }
            }
        } else {
            if (singlePetData != null) {
                petDataList.add(singlePetData);
            }
        }

        if (!petDataList.isEmpty()) {
            for (PetData dataTemp : petDataList) {
                if (dataTemp != null) {
                    if (!petType.isDataAllowed(dataTemp)) {
                        Lang.sendTo(sender, Lang.INVALID_PET_DATA_TYPE_FOR_PET.toString()
                                .replace("%data%", StringUtil.capitalise(dataTemp.toString().replace("_", "")))
                                .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", " "))));
                        return null;
                    }
                    if (!EchoPet.getOptions().allowData(petType, dataTemp)) {
                        Lang.sendTo(sender, Lang.DATA_TYPE_DISABLED.toString()
                                .replace("%data%", StringUtil.capitalise(dataTemp.toString().replace("_", ""))));
                        return null;
                    }
                }
            }
        }

        if (!EchoPet.getOptions().allowPetType(petType)) {
            Lang.sendTo(sender, Lang.PET_TYPE_DISABLED.toString()
                    .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
            return null;
        }

        for (PetData dataTemp : petDataList) {
            if (!Perm.hasDataPerm(sender, true, petType, dataTemp, false)) {
                return null;
            }
        }

        return new PetStorage(petDataList, petType, name);
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

    public static ArrayList<String> generatePetInfo(IPet pt) {
        ArrayList<String> info = new ArrayList<String>();
        info.add(ChatColor.GOLD + " - Pet Type: " + ChatColor.YELLOW + StringUtil.capitalise(pt.getPetType().toString()));
        info.add(ChatColor.GOLD + " - Name: " + ChatColor.YELLOW + pt.getPetName());
        if (pt instanceof IAgeablePet) {
            info.add(ChatColor.GOLD + " - Baby: " + ChatColor.YELLOW + ((IAgeablePet) pt).isBaby());
        }
        if (pt.getPetType() == PetType.ZOMBIE) {
            info.add(ChatColor.GOLD + " - Baby: " + ChatColor.YELLOW + ((IZombiePet) pt).isBaby());
        }
        if (pt.getPetType() == PetType.PIGZOMBIE) {
            info.add(ChatColor.GOLD + " - Baby: " + ChatColor.YELLOW + ((IPigZombiePet) pt).isBaby());
        }
        info.addAll(generatePetDataInfo(pt));

        if (pt.getRider() != null) {
            info.add(ChatColor.RED + "Rider:");
            info.addAll(generatePetInfo(pt.getRider()));
        }

        return info;
    }

    public static ArrayList<String> generatePetDataInfo(IPet pt) {
        ArrayList<String> info = new ArrayList<String>();
        if (pt.getPetType() == PetType.BLAZE) {
            info.add(ChatColor.GOLD + " - On Fire: " + ChatColor.YELLOW + ((IBlazePet) pt).isOnFire());
        }

        if (pt.getPetType() == PetType.CREEPER) {
            info.add(ChatColor.GOLD + " - Powered: " + ChatColor.YELLOW + ((ICreeperPet) pt).isPowered());
        }

        if (pt.getPetType() == PetType.ENDERMAN) {
            info.add(ChatColor.GOLD + " - Screaming: " + ChatColor.YELLOW + ((IEndermanPet) pt).isScreaming());
        }

        if (pt.getPetType() == PetType.SHEEP) {
            String color = "";
            color = ((ISheepPet) pt).getColor() == null ? "Default" : StringUtil.capitalise(((ISheepPet) pt).getColor().toString().replace("_", " "));
            info.add(ChatColor.GOLD + " - Wool Colour: " + ChatColor.YELLOW + color);
        }

        if (pt.getPetType() == PetType.VILLAGER) {
            String prof = "";
            prof = ((IVillagerPet) pt).getProfession() == null ? "Farmer" : StringUtil.capitalise(((IVillagerPet) pt).getProfession().toString().replace("_", " "));
            info.add(ChatColor.GOLD + " - Profession: " + ChatColor.YELLOW + prof);
        }

        if (pt.getPetType() == PetType.OCELOT) {
            String oType = "";
            oType = ((IOcelotPet) pt).getCatType() == null ? "Default" : StringUtil.capitalise(((IOcelotPet) pt).getCatType().toString().replace("_", " "));
            info.add(ChatColor.GOLD + " - Ocelot Type: " + ChatColor.YELLOW + oType);
        }

        if (pt.getPetType() == PetType.PIG) {
            info.add(ChatColor.GOLD + " - Saddled: " + ChatColor.YELLOW + ((IPigPet) pt).hasSaddle());
        }

        if (pt.getPetType() == PetType.SLIME) {
            String size = "";
            size = StringUtil.capitalise(((ISlimePet) pt).getSize() + "");
            String s = " (Small)";
            if (size.equals("2")) s = " (Medium)";
            if (size.equals("4")) s = " (Large)";
            info.add(ChatColor.GOLD + " - Slime Size: " + ChatColor.YELLOW + size + s);
        }

        if (pt.getPetType() == PetType.MAGMACUBE) {
            String size = "";
            size = StringUtil.capitalise(((IMagmaCubePet) pt).getSize() + "");
            String s = " (Small)";
            if (size.equals("2")) s = " (Medium)";
            if (size.equals("4")) s = " (Large)";
            info.add(ChatColor.GOLD + " - Slime Size: " + ChatColor.YELLOW + size + s);
        }

        if (pt.getPetType() == PetType.WOLF) {
            info.add(ChatColor.GOLD + " - Tamed (Wolf): " + ChatColor.YELLOW + ((IWolfPet) pt).isTamed());
            info.add(ChatColor.GOLD + " - Angry (Wolf): " + ChatColor.YELLOW + ((IWolfPet) pt).isAngry());
            String color = "";
            color = ((IWolfPet) pt).getCollarColor() == null ? "Red" : StringUtil.capitalise(((IWolfPet) pt).getCollarColor().toString().replace("_", " "));
            info.add(ChatColor.GOLD + " - Collar Colour: " + ChatColor.YELLOW + color);
        }

        if (pt.getPetType() == PetType.SKELETON) {
            info.add(ChatColor.GOLD + " - Wither: " + ChatColor.YELLOW + ((ISkeletonPet) pt).isWither());
        }

        if (pt.getPetType() == PetType.WITHER) {
            info.add(ChatColor.GOLD + " - Shielded: " + ChatColor.YELLOW + ((IWitherPet) pt).isShielded());
        }

        if (pt.getPetType() == PetType.ZOMBIE) {
            info.add(ChatColor.GOLD + " - Villager: " + ChatColor.YELLOW + ((IZombiePet) pt).isVillager());
        }
        if (pt.getPetType() == PetType.PIGZOMBIE) {
            info.add(ChatColor.GOLD + " - Villager: " + ChatColor.YELLOW + ((IPigZombiePet) pt).isVillager());
        }
        if (pt.getPetType() == PetType.HORSE) {
            HorseType ht = ((IHorsePet) pt).getHorseType();
            info.add(ChatColor.GOLD + " - Saddled: " + ChatColor.YELLOW + ((IHorsePet) pt).isSaddled());
            info.add(ChatColor.GOLD + " - Type: " + ChatColor.YELLOW + StringUtil.capitalise(ht.toString().replace("_", " ")));
            if (ht == HorseType.NORMAL) {
                info.add(ChatColor.GOLD + " - Variant: " + ChatColor.YELLOW + StringUtil.capitalise(((IHorsePet) pt).getVariant().toString().replace("_", " ")));
                info.add(ChatColor.GOLD + " - Marking: " + ChatColor.YELLOW + StringUtil.capitalise(((IHorsePet) pt).getMarking().toString().replace("_", " ")));
            }
            info.add(ChatColor.GOLD + " - Chested: " + ChatColor.YELLOW + ((IHorsePet) pt).isChested());
        }

        return info;
    }

    public static ArrayList<String> getPetList(CommandSender sender, boolean petAdmin) {
        ArrayList<String> list = new ArrayList<String>();
        String admin = petAdmin ? "admin" : "";
        for (PetType pt : PetType.values()) {
            ChatColor color1 = ChatColor.GREEN;
            ChatColor color2 = ChatColor.DARK_GREEN;
            String separator = ", ";
            if (sender instanceof Player) {

                if (!sender.hasPermission("echopet.pet" + admin + ".type." + pt.toString().toLowerCase().replace("_", ""))) {
                    color1 = ChatColor.RED;
                    color2 = ChatColor.DARK_RED;
                }

                StringBuilder builder = new StringBuilder();

                builder.append(color1).append("- ").append(StringUtil.capitalise(pt.toString().toLowerCase().replace("_", " ")));

                if (!pt.getAllowedDataTypes().isEmpty()) {
                    builder.append(color2).append("    ");
                    for (PetData data : pt.getAllowedDataTypes()) {
                        builder.append(color2).append(StringUtil.capitalise(data.toString().toLowerCase().replace("_", "")));
                        builder.append(separator);
                    }
                    builder.deleteCharAt(builder.length() - separator.length());
                }

                list.add(builder.toString());
            } else {
                StringBuilder builder = new StringBuilder();

                builder.append(color1).append("- ").append(StringUtil.capitalise(pt.toString().toLowerCase().replace("_", " ")));

                if (!pt.getAllowedDataTypes().isEmpty()) {
                    builder.append(color2).append(" (");
                    for (PetData data : pt.getAllowedDataTypes()) {
                        builder.append(separator);
                        builder.append(color2).append(StringUtil.capitalise(data.toString().toLowerCase().replace("_", "")));
                    }
                    builder.deleteCharAt(builder.length() - separator.length());
                    builder.append(color2).append(")");
                }

                list.add(builder.toString().replace(" )", ")"));
            }
        }
        return list;
    }

    public static String dataToString(ArrayList<PetData> data) {
        if (data.isEmpty()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (PetData pd : data) {
            builder.append(pd.getConfigOptionString());
            builder.append(", ");
        }
        builder.deleteCharAt(builder.length() - 2);
        return builder.toString();
    }

    public static String dataToString(ArrayList<PetData> data, ArrayList<PetData> riderData) {
        if (data.isEmpty()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (PetData pd : data) {
            builder.append(pd.getConfigOptionString());
            builder.append(", ");
        }
        for (PetData pd : riderData) {
            builder.append(pd.getConfigOptionString());
            builder.append("(Rider), ");
        }
        builder.deleteCharAt(builder.length() - 2);
        return builder.toString();
    }
}