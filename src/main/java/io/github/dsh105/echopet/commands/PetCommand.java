package io.github.dsh105.echopet.commands;

import com.dsh105.dshutils.util.StringUtil;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.conversation.NameFactory;
import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.data.UnorganisedPetData;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetData;
import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.menu.main.MenuOption;
import io.github.dsh105.echopet.menu.main.PetMenu;
import io.github.dsh105.echopet.menu.selector.SelectorLayout;
import io.github.dsh105.echopet.menu.selector.SelectorMenu;
import io.github.dsh105.echopet.util.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PetCommand implements CommandExecutor {

    private EchoPetPlugin ec;
    private String cmdLabel;

    public PetCommand(String commandLabel) {
        this.ec = EchoPetPlugin.getInstance();
        this.cmdLabel = commandLabel;
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
                Pet pet = ec.PH.getPet(p);
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
                                .replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " ")))
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
                                .replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " ")))
                                .replace("%name%", name));
                    }
                    return true;
                }
            } else return true;
        } else if (args.length == 1) {
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
                    new SelectorMenu().showTo(p);
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("selector")) {
                if (Perm.BASE_SELECTOR.hasPerm(sender, true, false)) {
                    Player p = (Player) sender;
                    p.getInventory().addItem(SelectorLayout.getSelectorItem());
                    Lang.sendTo(sender, Lang.ADD_SELECTOR.toString());
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("call")) {
                if (Perm.BASE_CALL.hasPerm(sender, true, false)) {
                    Player player = (Player) sender;
                    Pet pet = PetHandler.getInstance().getPet(player);
                    if (pet == null) {
                        Lang.sendTo(sender, Lang.NO_PET.toString());
                        return true;
                    }
                    pet.teleport(player.getLocation());
                    Lang.sendTo(sender, Lang.PET_CALL.toString());
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("toggle")) {
                if (Perm.BASE_TOGGLE.hasPerm(sender, true, false)) {
                    Player player = (Player) sender;
                    Pet p = ec.PH.getPet(player);
                    if (p == null) {
                        PetHandler.getInstance().removePets(player.getName(), true);
                        Pet pet = PetHandler.getInstance().loadPets(player, false, false, false);
                        if (pet == null) {
                            Lang.sendTo(sender, Lang.NO_HIDDEN_PET.toString());
                            return true;
                        }
                        if (WorldUtil.allowPets(player.getLocation())) {
                            Lang.sendTo(sender, Lang.SHOW_PET.toString().replace("%type%", StringUtil.capitalise(pet.getPetType().toString())));
                            return true;
                        } else {
                            Lang.sendTo(sender, Lang.PETS_DISABLED_HERE.toString().replace("%world%", player.getWorld().getName()));
                            if (pet != null) {
                                PetHandler.getInstance().removePet(pet, true);
                            }
                            return true;
                        }
                    } else {
                        ec.PH.saveFileData("autosave", p);
                        ec.SPH.saveToDatabase(p, false);
                        ec.PH.removePet(p, true);
                        Lang.sendTo(sender, Lang.HIDE_PET.toString());
                    }
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("hide")) {
                if (Perm.BASE_HIDE.hasPerm(sender, true, false)) {
                    Player player = (Player) sender;
                    Pet pet = ec.PH.getPet(player);
                    if (pet == null) {
                        Lang.sendTo(sender, Lang.NO_PET.toString());
                        return true;
                    }
                    ec.PH.saveFileData("autosave", pet);
                    ec.SPH.saveToDatabase(pet, false);
                    ec.PH.removePet(pet, true);
                    Lang.sendTo(sender, Lang.HIDE_PET.toString());
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("show")) {
                if (Perm.BASE_SHOW.hasPerm(sender, true, false)) {
                    Player player = (Player) sender;
                    PetHandler.getInstance().removePets(player.getName(), true);
                    Pet pet = PetHandler.getInstance().loadPets(player, false, false, false);
                    if (pet == null) {
                        Lang.sendTo(sender, Lang.NO_HIDDEN_PET.toString());
                        return true;
                    }
                    if (WorldUtil.allowPets(player.getLocation())) {
                        Lang.sendTo(sender, Lang.SHOW_PET.toString().replace("%type%", StringUtil.capitalise(pet.getPetType().toString())));
                        return true;
                    } else {
                        Lang.sendTo(sender, Lang.PETS_DISABLED_HERE.toString().replace("%world%", player.getWorld().getName()));
                        if (pet != null) {
                            PetHandler.getInstance().removePet(pet, true);
                        }
                        return true;
                    }
                } else return true;
            } else if (args[0].equalsIgnoreCase("menu")) {
                if (Perm.BASE_MENU.hasPerm(sender, true, false)) {
                    Player player = (Player) sender;
                    Pet p = ec.PH.getPet(player);
                    if (p == null) {
                        Lang.sendTo(sender, Lang.NO_PET.toString());
                        return true;
                    }
                    ArrayList<MenuOption> options = MenuUtil.createOptionList(p.getPetType());
                    int size = p.getPetType() == PetType.HORSE ? 18 : 9;
                    PetMenu menu = new PetMenu(p, options, size);
                    menu.open(true);
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("hat")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    Pet pi = ec.PH.getPet(p);
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
                    Pet pi = ec.PH.getPet(p);
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
                    sender.sendMessage(ChatColor.RED + "------------ EchoPet Help 1/6 ------------");
                    sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
                    for (String s : HelpPage.getHelpPage(1)) {
                        sender.sendMessage(s);
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
                    Pet pi = ec.PH.getPet((Player) sender);
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
                    Pet pi = ec.PH.getPet((Player) sender);
                    if (pi == null) {
                        Lang.sendTo(sender, Lang.NO_PET.toString());
                        return true;
                    }
                    ec.PH.clearFileData("autosave", pi);
                    ec.SPH.clearFromDatabase(pi.getNameOfOwner());
                    ec.PH.removePet(pi, true);
                    Lang.sendTo(sender, Lang.REMOVE_PET.toString());
                    return true;
                } else return true;
            } else {
                if (!(sender instanceof Player)) {
                    Lang.sendTo(sender, Lang.IN_GAME_ONLY.toString()
                            .replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil.combineSplit(0, args, " "))));
                    return true;
                }

                UnorganisedPetData UPD = PetUtil.formPetFromArgs(ec, sender, args[0], false);
                if (UPD == null) {
                    return true;
                }
                PetType petType = UPD.petType;
                ArrayList<PetData> petDataList = UPD.petDataList;

                if (petType == null || petDataList == null) {
                    return true;
                }

                if (Perm.hasTypePerm(sender, true, Perm.BASE_PETTYPE, false, petType)) {
                    Pet pi = ec.PH.createPet((Player) sender, petType, true);
                    if (pi == null) {
                        return true;
                    }
                    if (!petDataList.isEmpty()) {
                        ec.PH.setData(pi, petDataList.toArray(new PetData[petDataList.size()]), true);
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
                    ec.PH.saveFileData("autosave", pi);
                    ec.SPH.saveToDatabase(pi, false);
                    Lang.sendTo(sender, Lang.CREATE_PET.toString()
                            .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
                    return true;
                } else return true;
            }

        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("rider")) {
                if (args[1].equalsIgnoreCase("remove")) {
                    if (Perm.BASE_REMOVE.hasPerm(sender, true, false)) {
                        Pet pi = ec.PH.getPet((Player) sender);
                        if (pi == null) {
                            Lang.sendTo(sender, Lang.NO_PET.toString());
                            return true;
                        }
                        if (pi.getRider() == null) {
                            Lang.sendTo(sender, Lang.NO_RIDER.toString());
                            return true;
                        }
                        pi.removeRider();
                        ec.PH.saveFileData("autosave", pi);
                        ec.SPH.saveToDatabase(pi, false);
                        Lang.sendTo(sender, Lang.REMOVE_RIDER.toString());
                        return true;
                    } else return true;
                } else {
                    if (!(sender instanceof Player)) {
                        Lang.sendTo(sender, Lang.IN_GAME_ONLY.toString()
                                .replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil.combineSplit(0, args, " "))));
                        return true;
                    }

                    Pet pi = ec.PH.getPet((Player) sender);

                    if (pi == null) {
                        Lang.sendTo(sender, Lang.NO_PET.toString());
                        return true;
                    }

                    UnorganisedPetData UPD = PetUtil.formPetFromArgs(ec, sender, args[1], false);
                    if (UPD == null) {
                        return true;
                    }
                    PetType petType = UPD.petType;
                    ArrayList<PetData> petDataList = UPD.petDataList;

                    if (petType == null || petDataList == null) {
                        return true;
                    }

                    if (!ec.options.allowRidersFor(petType)) {
                        Lang.sendTo(sender, Lang.RIDERS_DISABLED.toString()
                                .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", " "))));
                        return true;
                    }

                    if (Perm.hasTypePerm(sender, true, Perm.BASE_PETTYPE, false, petType)) {
                        Pet rider = pi.createRider(petType, true);
                        if (rider == null) {
                            return true;
                        }
                        if (!petDataList.isEmpty()) {
                            ec.PH.setData(rider, petDataList.toArray(new PetData[petDataList.size()]), true);
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
                        ec.PH.saveFileData("autosave", pi);
                        ec.SPH.saveToDatabase(pi, false);
                        Lang.sendTo(sender, Lang.CHANGE_RIDER.toString()
                                .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
                        return true;
                    } else return true;
                }
            }

            // Help pages 1 through to 3
            else if (args[0].equalsIgnoreCase("help")) {
                if (Perm.BASE.hasPerm(sender, true, true)) {
                    if (StringUtil.isInt(args[1])) {
                        sender.sendMessage(ChatColor.RED + "------------ EchoPet Help " + args[1] + "/6 ------------");
                        sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
                        for (String s : HelpPage.getHelpPage(Integer.parseInt(args[1]))) {
                            sender.sendMessage(s);
                        }
                        return true;
                    }
                    sender.sendMessage(ChatColor.RED + "------------ EchoPet Help 1/6 ------------");
                    sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
                    for (String s : HelpPage.getHelpPage(1)) {
                        sender.sendMessage(s);
                    }
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("default")) {
                if (args[1].equalsIgnoreCase("remove")) {
                    if (Perm.BASE_DEFAULT_REMOVE.hasPerm(sender, true, false)) {
                        String path = "default." + sender.getName() + ".";
                        if (ec.getPetConfig().get(path + "pet.type") == null) {
                            Lang.sendTo(sender, Lang.NO_DEFAULT.toString());
                            return true;
                        }

                        ec.PH.clearFileData("default", (Player) sender);
                        ec.SPH.clearFromDatabase(sender.getName());
                        Lang.sendTo(sender, Lang.REMOVE_DEFAULT.toString());
                        return true;
                    } else return true;
                }
            } else {
                UnorganisedPetData UPD = PetUtil.formPetFromArgs(ec, sender, args[0], false);
                if (UPD == null) {
                    return true;
                }
                PetType petType = UPD.petType;
                ArrayList<PetData> petDataList = UPD.petDataList;

                UnorganisedPetData UMD = PetUtil.formPetFromArgs(ec, sender, args[1], false);
                if (UMD == null) {
                    return true;
                }
                PetType riderType = UMD.petType;
                ArrayList<PetData> riderDataList = UMD.petDataList;

                if (petType == null || petDataList == null || riderType == null || riderDataList == null) {
                    return true;
                }

                if (Perm.hasTypePerm(sender, true, Perm.BASE_PETTYPE, false, petType) && Perm.hasTypePerm(sender, true, Perm.BASE_PETTYPE, false, riderType)) {
                    Pet pi = ec.PH.createPet(((Player) sender), petType, riderType, true);
                    if (pi == null) {
                        return true;
                    }
                    if (!petDataList.isEmpty()) {
                        ec.PH.setData(pi, petDataList.toArray(new PetData[petDataList.size()]), true);
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
                        ec.PH.setData(pi.getRider(), riderDataList.toArray(new PetData[riderDataList.size()]), true);
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
                    ec.PH.saveFileData("autosave", pi);
                    ec.SPH.saveToDatabase(pi, false);
                    Lang.sendTo(sender, Lang.CREATE_PET_WITH_RIDER.toString()
                            .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", "")))
                            .replace("%mtype%", StringUtil.capitalise(riderType.toString().replace("_", ""))));
                    return true;
                } else return true;
            }

        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("default")) {
                if (args[1].equalsIgnoreCase("set")) {
                    if (args[2].equalsIgnoreCase("current")) {
                        if (Perm.BASE_DEFAULT_SET_CURRENT.hasPerm(sender, true, false)) {
                            Pet pi = ec.PH.getPet(((Player) sender));
                            if (pi == null) {
                                Lang.sendTo(sender, Lang.NO_PET.toString());
                                return true;
                            }

                            ec.PH.saveFileData("default", pi);
                            Lang.sendTo(sender, Lang.SET_DEFAULT_TO_CURRENT.toString());
                            return true;
                        } else return true;
                    } else {
                        UnorganisedPetData UPD = PetUtil.formPetFromArgs(ec, sender, args[2], false);
                        if (UPD == null) {
                            return true;
                        }
                        PetType petType = UPD.petType;
                        ArrayList<PetData> petDataList = UPD.petDataList;

                        if (petType == null || petDataList == null) {
                            return true;
                        }

                        if (Perm.hasTypePerm(sender, true, Perm.BASE_DEFAULT_SET_PETTYPE, false, petType)) {
                            ec.PH.saveFileData("default", (Player) sender, UPD);
                            Lang.sendTo(sender, Lang.SET_DEFAULT.toString()
                                    .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
                            return true;
                        } else return true;
                    }
                }
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("default")) {
                if (args[1].equalsIgnoreCase("set")) {
                    UnorganisedPetData UPD = PetUtil.formPetFromArgs(ec, sender, args[2], false);
                    if (UPD == null) {
                        return true;
                    }
                    PetType petType = UPD.petType;
                    ArrayList<PetData> petDataList = UPD.petDataList;

                    UnorganisedPetData UMD = PetUtil.formPetFromArgs(ec, sender, args[3], false);
                    if (UMD == null) {
                        return true;
                    }
                    PetType riderType = UMD.petType;
                    ArrayList<PetData> riderDataList = UMD.petDataList;

                    if (petType == null || petDataList == null || riderType == null || riderDataList == null) {
                        return true;
                    }

                    if (Perm.hasTypePerm(sender, true, Perm.BASE_DEFAULT_SET_PETTYPE, false, petType) && Perm.hasTypePerm(sender, true, Perm.BASE_DEFAULT_SET_PETTYPE, false, petType)) {
                        ec.PH.saveFileData("default", (Player) sender, UPD, UMD);
                        Lang.sendTo(sender, Lang.SET_DEFAULT_WITH_RIDER.toString()
                                .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", "")))
                                .replace("%mtype%", StringUtil.capitalise(riderType.toString().replace("_", ""))));
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
