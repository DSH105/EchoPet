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

import com.captainbern.minecraft.reflection.MinecraftReflection;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.compat.api.config.ConfigType;
import com.dsh105.echopet.compat.api.config.PetSettings;
import com.dsh105.echopet.compat.api.entity.pet.Pet;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.inventory.DataMenu;
import com.dsh105.echopet.compat.api.inventory.PetSelector;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.PetStorage;
import com.dsh105.echopet.compat.api.util.*;
import com.dsh105.echopet.compat.api.util.protocol.wrapper.WrapperPacketEntityMetadata;
import com.dsh105.echopet.compat.api.util.protocol.wrapper.WrapperPacketNamedEntitySpawn;
import com.dsh105.echopet.compat.api.util.protocol.wrapper.WrapperPacketPlayOutChat;
import com.dsh105.echopet.compat.api.util.protocol.wrapper.WrapperPacketWorldParticles;
import com.dsh105.echopet.conversation.NameFactory;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class PetCommand implements CommandExecutor {

    private FancyPaginator getHelp(CommandSender sender) {
        ArrayList<FancyMessage> helpMessages = new ArrayList<FancyMessage>();
        for (HelpEntry he : HelpEntry.values()) {
            helpMessages.add(he.getFancyMessage(sender));
        }
        return new FancyPaginator(helpMessages, 5);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (args.length == 0) {
            if (Perm.BASE.hasPerm(sender, true, true)) {
                Lang.sendTo(sender, Lang.HELP.toString().replace("%cmd%", "pet help"));
                return true;
            } else return true;

        }

        // Setting the pet and rider names
        // Supports colour coding
        else if (args.length >= 1 && args[0].equalsIgnoreCase("name")) {
            if (Perm.BASE_NAME.hasPerm(sender, true, false)) {
                Player p = (Player) sender;
                Pet pet = EchoPet.getManager().getPet(p);
                if (pet == null) {
                    Lang.sendTo(sender, Lang.NO_PET.toString());
                    return true;
                }

                if (args.length >= 2 && args[1].equals("rider")) {
                    if (pet.getRider() == null) {
                        Lang.sendTo(sender, Lang.NO_RIDER.toString());
                        return true;
                    }
                    if (args.length == 2) {
                        NameFactory.askForName(p, pet.getRider(), false);
                    } else {
                        String name = ChatColor.translateAlternateColorCodes('&', StringUtil.combineSplit(2, args, " "));
                        if (name.length() > 32) {
                            Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
                            return true;
                        }
                        pet.getRider().setPetName(name);
                        Lang.sendTo(sender, Lang.NAME_RIDER.toString()
                                .replace("%type%", pet.getPetType().humanName())
                                .replace("%name%", name));
                    }
                    return true;
                } else {
                    if (args.length == 1) {
                        NameFactory.askForName(p, pet, false);
                    } else {
                        String name = ChatColor.translateAlternateColorCodes('&', StringUtil.combineSplit(1, args, " "));
                        if (name.length() > 32) {
                            Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
                            return true;
                        }
                        pet.setPetName(name);
                        Lang.sendTo(sender, Lang.NAME_PET.toString()
                                .replace("%type%", pet.getPetType().humanName())
                                .replace("%name%", name));
                    }
                    return true;
                }
            } else return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("debug")) {
                System.out.println("DEBUG Particles:");
                for (Field f : new WrapperPacketWorldParticles().getPacketClass().getDeclaredFields()) {
                    System.out.println(f.getName() + " -> " + f.getType());
                }
                System.out.println("DEBUG Chat:");
                for (Field f : new WrapperPacketPlayOutChat().getPacketClass().getDeclaredFields()) {
                    System.out.println(f.getName() + " -> " + f.getType());
                }
                System.out.println("DEBUG Meta:");
                for (Field f : new WrapperPacketEntityMetadata().getPacketClass().getDeclaredFields()) {
                    System.out.println(f.getName() + " -> " + f.getType());
                }
                System.out.println("DEBUG Named Spawn:");
                for (Field f : new WrapperPacketNamedEntitySpawn().getPacketClass().getDeclaredFields()) {
                    System.out.println(f.getName() + " -> " + f.getType());
                }
                System.out.println("DEBUG Chat Serializer:");
                for (Method m : ReflectionUtil.getNMSClass("ChatSerializer").getDeclaredMethods()) {
                    /*if (ReflectionUtil.getNMSClass("IChatBaseComponent").isAssignableFrom(m.getReturnType())) {
                        System.out.println("set component: " + m.getName());
                    } else*/
                    if (String.class.isAssignableFrom(m.getReturnType())) {
                        System.out.println("get component: " + m.getName());
                    }
                }
                System.out.println("DEBUG DataWatcher:");
                for (Method m : ReflectionUtil.getNMSClass("DataWatcher").getDeclaredMethods()) {
                    System.out.println(m.getName());
                    for (Class c : m.getParameterTypes()) {
                        System.out.println("      -> " + c.getCanonicalName());
                    }
                }
                System.out.println("DEBUG EntityTypes:");
                for (Field f : ReflectionUtil.getNMSClass("EntityTypes").getDeclaredFields()) {
                    System.out.println(f.getName() + " -> " + f.getType());
                }
                System.out.println("DEBUG Packet:");
                for (Method m : ReflectionUtil.getNMSClass("PlayerConnection").getDeclaredMethods()) {
                    System.out.println(m.getName());
                    for (Class c : m.getParameterTypes()) {
                        System.out.println("      -> " + c.getCanonicalName());
                    }
                }
                System.out.println("DEBUG Player:");
                for (Field f : ReflectionUtil.getNMSClass("EntityPlayer").getDeclaredFields()) {
                    System.out.println(f.getName() + " -> " + f.getType());
                }
                System.out.println("DEBUG ItemStack:");
                for (Method m : ReflectionUtil.getNMSClass("ItemStack").getDeclaredMethods()) {
                    System.out.println(m.getName());
                    for (Class c : m.getParameterTypes()) {
                        System.out.println("      -> " + c.getCanonicalName());
                    }
                }
                System.out.println("DEBUG Achievement:");
                for (Field f : ReflectionUtil.getNMSClass("Achievement").getDeclaredFields()) {
                    System.out.println(f.getName() + " -> " + f.getType());
                }
            }
            if (args[0].equalsIgnoreCase("select")) {
                if (sender instanceof Player) {
                    // We can exempt the player from having the appropriate permission here
                    Player p = (Player) sender;
                    if (p.getOpenInventory() != null && p.getOpenInventory().getTitle().equals("Pets")) {
                        p.closeInventory();
                        return true;
                    }
                }
                if (Perm.BASE_SELECT.hasPerm(sender, true, false)) {
                    Player p = (Player) sender;
                    PetSelector.prepare().show(p);
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("selector")) {
                if (Perm.BASE_SELECTOR.hasPerm(sender, true, false)) {
                    Player p = (Player) sender;
                    p.getInventory().addItem(PetSelector.prepare().getClickItem());
                    Lang.sendTo(sender, Lang.ADD_SELECTOR.toString());
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("call")) {
                if (Perm.BASE_CALL.hasPerm(sender, true, false)) {
                    Player player = (Player) sender;
                    Pet pet = EchoPet.getManager().getPet(player);
                    if (pet == null) {
                        Lang.sendTo(sender, Lang.NO_PET.toString());
                        return true;
                    }
                    pet.teleportToOwner();
                    Lang.sendTo(sender, Lang.PET_CALL.toString());
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("toggle")) {
                if (Perm.BASE_TOGGLE.hasPerm(sender, true, false)) {
                    Player player = (Player) sender;
                    Pet p = EchoPet.getManager().getPet(player);
                    if (p == null) {
                        EchoPet.getManager().removePet(p, true);
                        Pet pet = EchoPet.getManager().loadPets(player, false, false, false);
                        if (pet == null) {
                            Lang.sendTo(sender, Lang.NO_HIDDEN_PET.toString());
                            return true;
                        }
                        if (EchoPet.getPlugin().getWorldGuardProvider().allowPets(player.getLocation())) {
                            Lang.sendTo(sender, Lang.SHOW_PET.toString().replace("%type%", pet.getPetType().humanName()));
                            return true;
                        } else {
                            Lang.sendTo(sender, Lang.PETS_DISABLED_HERE.toString().replace("%world%", player.getWorld().getName()));
                            if (pet != null) {
                                EchoPet.getManager().removePet(pet, true);
                            }
                            return true;
                        }
                    } else {
                        EchoPet.getManager().saveFileData("autosave", p);
                        EchoPet.getSqlManager().saveToDatabase(p, false);
                        EchoPet.getManager().removePet(p, true);
                        Lang.sendTo(sender, Lang.HIDE_PET.toString());
                    }
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("hide")) {
                if (Perm.BASE_HIDE.hasPerm(sender, true, false)) {
                    Player player = (Player) sender;
                    Pet pet = EchoPet.getManager().getPet(player);
                    if (pet == null) {
                        Lang.sendTo(sender, Lang.NO_PET.toString());
                        return true;
                    }
                    EchoPet.getManager().saveFileData("autosave", pet);
                    EchoPet.getSqlManager().saveToDatabase(pet, false);
                    EchoPet.getManager().removePet(pet, true);
                    Lang.sendTo(sender, Lang.HIDE_PET.toString());
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("show")) {
                if (Perm.BASE_SHOW.hasPerm(sender, true, false)) {
                    Player player = (Player) sender;
                    EchoPet.getManager().removePets(player, true);
                    Pet pet = EchoPet.getManager().loadPets(player, false, false, false);
                    if (pet == null) {
                        Lang.sendTo(sender, Lang.NO_HIDDEN_PET.toString());
                        return true;
                    }
                    if (EchoPet.getPlugin().getWorldGuardProvider().allowPets(player.getLocation())) {
                        Lang.sendTo(sender, Lang.SHOW_PET.toString().replace("%type%", pet.getPetType().humanName()));
                        return true;
                    } else {
                        Lang.sendTo(sender, Lang.PETS_DISABLED_HERE.toString().replace("%world%", player.getWorld().getName()));
                        if (pet != null) {
                            EchoPet.getManager().removePet(pet, true);
                        }
                        return true;
                    }
                } else return true;
            } else if (args[0].equalsIgnoreCase("menu")) {
                if (Perm.BASE_MENU.hasPerm(sender, true, false)) {
                    Player player = (Player) sender;
                    Pet p = EchoPet.getManager().getPet(player);
                    if (p == null) {
                        Lang.sendTo(sender, Lang.NO_PET.toString());
                        return true;
                    }
                    DataMenu.prepare(pet).show(player);
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("hat")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    Pet pi = EchoPet.getManager().getPet(p);
                    if (pi == null) {
                        Lang.sendTo(sender, Lang.NO_PET.toString());
                        return true;
                    }
                    if (Perm.hasTypePerm(sender, true, Perm.BASE_HAT, false, pi.getPetType())) {
                        pi.setAsHat(!pi.isHat());
                        if (pi.isHat()) {
                            Lang.sendTo(sender, Lang.HAT_PET_ON.toString());
                        } else {
                            Lang.sendTo(sender, Lang.HAT_PET_OFF.toString());
                        }
                        return true;
                    } else return true;
                }
            } else if (args[0].equalsIgnoreCase("ride")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    Pet pi = EchoPet.getManager().getPet(p);
                    if (pi == null) {
                        Lang.sendTo(sender, Lang.NO_PET.toString());
                        return true;
                    }
                    if (Perm.hasTypePerm(sender, true, Perm.BASE_RIDE, false, pi.getPetType())) {
                        pi.ownerRidePet(!pi.isOwnerRiding());
                        if (pi.isOwnerRiding()) {
                            Lang.sendTo(sender, Lang.RIDE_PET_ON.toString());
                        } else {
                            Lang.sendTo(sender, Lang.RIDE_PET_OFF.toString());
                        }
                        return true;
                    } else return true;
                }
            }

            // Help page 1
            else if (args[0].equalsIgnoreCase("help")) {
                if (Perm.BASE.hasPerm(sender, true, true)) {
                    if (sender instanceof Player && MinecraftReflection.isUsingNetty()) {
                        FancyPaginator paginator = this.getHelp(sender);
                        sender.sendMessage(ChatColor.RED + "------------ EchoPet Help 1/" + paginator.getIndex() + " ------------");
                        sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
                        for (FancyMessage fancy : paginator.getPage(1)) {
                            fancy.send((Player) sender);
                        }
                        sender.sendMessage(Lang.TIP_HOVER_PREVIEW.toString());
                    } else {
                        sender.sendMessage(ChatColor.RED + "------------ EchoPet Help 1/6 ------------");
                        sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
                        for (String s : HelpPage.getHelpPage(1)) {
                            sender.sendMessage(s);
                        }
                    }
                    return true;
                } else return true;
            }

            // List of all pet types
            else if (args[0].equalsIgnoreCase("list")) {
                if (Perm.LIST.hasPerm(sender, true, true)) {
                    sender.sendMessage(ChatColor.RED + "------------ EchoPet Pet List ------------");
                    for (String s : PetUtil.getPetList(sender, false)) {
                        sender.sendMessage(s);
                    }
                    return true;
                } else return true;
            }

            // Get all the info for a specific pet and send it to the player
            else if (args[0].equalsIgnoreCase("info")) {
                if (Perm.BASE_INFO.hasPerm(sender, true, false)) {
                    Pet pi = EchoPet.getManager().getPet((Player) sender);
                    if (pi == null) {
                        Lang.sendTo(sender, Lang.NO_PET.toString());
                        return true;
                    }
                    sender.sendMessage(ChatColor.RED + "------------ EchoPet Pet Info ------------");
                    for (String s : PetUtil.generatePetInfo(pi)) {
                        sender.sendMessage(s);
                    }
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (Perm.BASE_REMOVE.hasPerm(sender, true, false)) {
                    Pet pi = EchoPet.getManager().getPet((Player) sender);
                    if (pi == null) {
                        Lang.sendTo(sender, Lang.NO_PET.toString());
                        return true;
                    }
                    EchoPet.getManager().clearFileData("autosave", pi);
                    EchoPet.getSqlManager().clearFromDatabase(pi.getOwner());
                    EchoPet.getManager().removePet(pi, true);
                    Lang.sendTo(sender, Lang.REMOVE_PET.toString());
                    return true;
                } else return true;
            } else {
                if (!(sender instanceof Player)) {
                    Lang.sendTo(sender, Lang.IN_GAME_ONLY.toString()
                            .replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil.combineSplit(0, args, " "))));
                    return true;
                }

                PetStorage UPD = PetUtil.formPetFromArgs(sender, args[0], false);
                if (UPD == null) {
                    return true;
                }
                PetType petType = UPD.petType;
                ArrayList<PetData> petDataList = UPD.petDataList;

                if (petType == null || petDataList == null) {
                    return true;
                }

                if (Perm.hasTypePerm(sender, true, Perm.BASE_PETTYPE, false, petType)) {
                    Pet pi = EchoPet.getManager().createPet((Player) sender, petType, true);
                    if (pi == null) {
                        return true;
                    }
                    if (!petDataList.isEmpty()) {
                        pi.setDataValue(petDataList.toArray(new PetData[petDataList.size()]));
                    }
                    if (UPD.petName != null && !UPD.petName.equalsIgnoreCase("")) {
                        if (Perm.BASE_NAME.hasPerm(sender, true, false)) {
                            if (UPD.petName.length() > 32) {
                                Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
                            } else {
                                pi.setPetName(UPD.petName);
                            }
                        }
                    }
                    EchoPet.getManager().saveFileData("autosave", pi);
                    EchoPet.getSqlManager().saveToDatabase(pi, false);
                    Lang.sendTo(sender, Lang.CREATE_PET.toString()
                            .replace("%type%", petType.humanName()));
                    return true;
                } else return true;
            }

        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("rider")) {
                if (args[1].equalsIgnoreCase("remove")) {
                    if (Perm.BASE_REMOVE.hasPerm(sender, true, false)) {
                        Pet pi = EchoPet.getManager().getPet((Player) sender);
                        if (pi == null) {
                            Lang.sendTo(sender, Lang.NO_PET.toString());
                            return true;
                        }
                        if (pi.getRider() == null) {
                            Lang.sendTo(sender, Lang.NO_RIDER.toString());
                            return true;
                        }
                        pi.removeRider();
                        EchoPet.getManager().saveFileData("autosave", pi);
                        EchoPet.getSqlManager().saveToDatabase(pi, false);
                        Lang.sendTo(sender, Lang.REMOVE_RIDER.toString());
                        return true;
                    } else return true;
                } else {
                    if (!(sender instanceof Player)) {
                        Lang.sendTo(sender, Lang.IN_GAME_ONLY.toString()
                                .replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil.combineSplit(0, args, " "))));
                        return true;
                    }

                    Pet pi = EchoPet.getManager().getPet((Player) sender);

                    if (pi == null) {
                        Lang.sendTo(sender, Lang.NO_PET.toString());
                        return true;
                    }

                    PetStorage UPD = PetUtil.formPetFromArgs(sender, args[1], false);
                    if (UPD == null) {
                        return true;
                    }
                    PetType petType = UPD.petType;
                    ArrayList<PetData> petDataList = UPD.petDataList;

                    if (petType == null || petDataList == null) {
                        return true;
                    }

                    if (!PetSettings.ALLOW_RIDERS.getValue(petType.storageName())) {
                        Lang.sendTo(sender, Lang.RIDERS_DISABLED.toString()
                                .replace("%type%", petType.humanName()));
                        return true;
                    }

                    if (Perm.hasTypePerm(sender, true, Perm.BASE_PETTYPE, false, petType)) {
                        Pet rider = pi.createRider(petType, true);
                        if (rider == null) {
                            return true;
                        }
                        if (!petDataList.isEmpty()) {
                            rider.setDataValue(petDataList.toArray(new PetData[petDataList.size()]));
                        }
                        if (UPD.petName != null && !UPD.petName.equalsIgnoreCase("")) {
                            if (Perm.BASE_NAME.hasPerm(sender, true, false)) {
                                if (UPD.petName.length() > 32) {
                                    Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
                                } else {
                                    rider.setPetName(UPD.petName);
                                }
                            }
                        }
                        EchoPet.getManager().saveFileData("autosave", pi);
                        EchoPet.getSqlManager().saveToDatabase(pi, false);
                        Lang.sendTo(sender, Lang.CHANGE_RIDER.toString()
                                .replace("%type%", petType.humanName()));
                        return true;
                    } else return true;
                }
            }

            // Help pages 1 through to 3
            else if (args[0].equalsIgnoreCase("help")) {
                if (Perm.BASE.hasPerm(sender, true, true)) {
                    if (StringUtil.isInt(args[1])) {
                        if (sender instanceof Player && MinecraftReflection.isUsingNetty()) {
                            FancyPaginator paginator = this.getHelp(sender);
                            sender.sendMessage(ChatColor.RED + "------------ EchoPet Help " + args[1] + "/" + paginator.getIndex() + " ------------");
                            sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
                            if (Integer.parseInt(args[1]) > paginator.getIndex()) {
                                Lang.sendTo(sender, Lang.HELP_INDEX_TOO_BIG.toString().replace("%index%", args[1]));
                                return true;
                            }
                            for (FancyMessage fancy : paginator.getPage(Integer.parseInt(args[1]))) {
                                fancy.send((Player) sender);
                            }
                            sender.sendMessage(Lang.TIP_HOVER_PREVIEW.toString());
                        } else {
                            sender.sendMessage(ChatColor.RED + "------------ EchoPet Help " + args[1] + "/6 ------------");
                            sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
                            for (String s : HelpPage.getHelpPage(Integer.parseInt(args[1]))) {
                                sender.sendMessage(s);
                            }
                        }
                        return true;
                    }
                    if (sender instanceof Player && MinecraftReflection.isUsingNetty()) {
                        FancyPaginator paginator = this.getHelp(sender);
                        sender.sendMessage(ChatColor.RED + "------------ EchoPet Help 1/" + paginator.getIndex() + " ------------");
                        sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
                        for (FancyMessage fancy : paginator.getPage(1)) {
                            fancy.send((Player) sender);
                        }
                        sender.sendMessage(Lang.TIP_HOVER_PREVIEW.toString());
                    } else {
                        sender.sendMessage(ChatColor.RED + "------------ EchoPet Help 1/6 ------------");
                        sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
                        for (String s : HelpPage.getHelpPage(1)) {
                            sender.sendMessage(s);
                        }
                    }
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("default")) {
                if (args[1].equalsIgnoreCase("remove")) {
                    if (Perm.BASE_DEFAULT_REMOVE.hasPerm(sender, true, false)) {
                        String path = "default." + sender.getName() + ".";
                        if (EchoPet.getConfig(ConfigType.DATA).get(path + "pet.type") == null) {
                            Lang.sendTo(sender, Lang.NO_DEFAULT.toString());
                            return true;
                        }

                        EchoPet.getManager().clearFileData("default", (Player) sender);
                        EchoPet.getSqlManager().clearFromDatabase((Player) sender);
                        Lang.sendTo(sender, Lang.REMOVE_DEFAULT.toString());
                        return true;
                    } else return true;
                }
            } else {
                PetStorage UPD = PetUtil.formPetFromArgs(sender, args[0], false);
                if (UPD == null) {
                    return true;
                }
                PetType petType = UPD.petType;
                ArrayList<PetData> petDataList = UPD.petDataList;

                PetStorage UMD = PetUtil.formPetFromArgs(sender, args[1], false);
                if (UMD == null) {
                    return true;
                }
                PetType riderType = UMD.petType;
                ArrayList<PetData> riderDataList = UMD.petDataList;

                if (petType == null || petDataList == null || riderType == null || riderDataList == null) {
                    return true;
                }

                if (Perm.hasTypePerm(sender, true, Perm.BASE_PETTYPE, false, petType) && Perm.hasTypePerm(sender, true, Perm.BASE_PETTYPE, false, riderType)) {
                    Pet pi = EchoPet.getManager().createPet(((Player) sender), petType, riderType);
                    if (pi == null) {
                        return true;
                    }
                    if (!petDataList.isEmpty()) {
                        pi.setDataValue(petDataList.toArray(new PetData[petDataList.size()]));
                    }
                    if (UPD.petName != null && !UPD.petName.equalsIgnoreCase("")) {
                        if (Perm.BASE_NAME.hasPerm(sender, true, false)) {
                            if (UPD.petName.length() > 32) {
                                Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
                            } else {
                                pi.setPetName(UPD.petName);
                            }
                        }
                    }
                    if (!riderDataList.isEmpty()) {
                        pi.getRider().setDataValue(riderDataList.toArray(new PetData[riderDataList.size()]));
                    }
                    if (UMD.petName != null && !UMD.petName.equalsIgnoreCase("")) {
                        if (Perm.BASE_NAME.hasPerm(sender, true, false)) {
                            if (UPD.petName.length() > 32) {
                                Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
                            } else {
                                pi.getRider().setPetName(UPD.petName);
                            }
                        }
                    }
                    EchoPet.getManager().saveFileData("autosave", pi);
                    EchoPet.getSqlManager().saveToDatabase(pi, false);
                    Lang.sendTo(sender, Lang.CREATE_PET_WITH_RIDER.toString()
                            .replace("%type%", petType.humanName())
                            .replace("%mtype%", riderType.humanName()));
                    return true;
                } else return true;
            }

        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("default")) {
                if (args[1].equalsIgnoreCase("set")) {
                    if (args[2].equalsIgnoreCase("current")) {
                        if (Perm.BASE_DEFAULT_SET_CURRENT.hasPerm(sender, true, false)) {
                            Pet pi = EchoPet.getManager().getPet(((Player) sender));
                            if (pi == null) {
                                Lang.sendTo(sender, Lang.NO_PET.toString());
                                return true;
                            }

                            EchoPet.getManager().saveFileData("default", pi);
                            Lang.sendTo(sender, Lang.SET_DEFAULT_TO_CURRENT.toString());
                            return true;
                        } else return true;
                    } else {
                        PetStorage UPD = PetUtil.formPetFromArgs(sender, args[2], false);
                        if (UPD == null) {
                            return true;
                        }
                        PetType petType = UPD.petType;
                        ArrayList<PetData> petDataList = UPD.petDataList;

                        if (petType == null || petDataList == null) {
                            return true;
                        }

                        if (Perm.hasTypePerm(sender, true, Perm.BASE_DEFAULT_SET_PETTYPE, false, petType)) {
                            EchoPet.getManager().saveFileData("default", (Player) sender, UPD);
                            Lang.sendTo(sender, Lang.SET_DEFAULT.toString()
                                    .replace("%type%", petType.humanName()));
                            return true;
                        } else return true;
                    }
                }
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("default")) {
                if (args[1].equalsIgnoreCase("set")) {
                    PetStorage UPD = PetUtil.formPetFromArgs(sender, args[2], false);
                    if (UPD == null) {
                        return true;
                    }
                    PetType petType = UPD.petType;
                    ArrayList<PetData> petDataList = UPD.petDataList;

                    PetStorage UMD = PetUtil.formPetFromArgs(sender, args[3], false);
                    if (UMD == null) {
                        return true;
                    }
                    PetType riderType = UMD.petType;
                    ArrayList<PetData> riderDataList = UMD.petDataList;

                    if (petType == null || petDataList == null || riderType == null || riderDataList == null) {
                        return true;
                    }

                    if (Perm.hasTypePerm(sender, true, Perm.BASE_DEFAULT_SET_PETTYPE, false, petType) && Perm.hasTypePerm(sender, true, Perm.BASE_DEFAULT_SET_PETTYPE, false, petType)) {
                        EchoPet.getManager().saveFileData("default", (Player) sender, UPD, UMD);
                        Lang.sendTo(sender, Lang.SET_DEFAULT_WITH_RIDER.toString()
                                .replace("%type%", petType.humanName())
                                .replace("%mtype%", riderType.humanName()));
                        return true;
                    } else return true;
                }
            }
        }
        // Something went wrong. Maybe the player didn't use a command correctly?
        // Send them a message with the exact command to make sure
        if (!HelpPage.sendRelevantHelpMessage(sender, args)) {
            Lang.sendTo(sender, Lang.COMMAND_ERROR.toString()
                    .replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil.combineSplit(0, args, " "))));
        }
        return true;
    }
}
