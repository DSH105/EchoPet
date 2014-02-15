package io.github.dsh105.echopet.commands;

import com.dsh105.dshutils.util.StringUtil;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.data.UnorganisedPetData;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetData;
import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.menu.main.MenuOption;
import io.github.dsh105.echopet.menu.main.PetMenu;
import io.github.dsh105.echopet.menu.selector.SelectorLayout;
import io.github.dsh105.echopet.menu.selector.SelectorMenu;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.MenuUtil;
import io.github.dsh105.echopet.util.Perm;
import io.github.dsh105.echopet.util.PetUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PetAdminCommand implements CommandExecutor {

    private EchoPetPlugin ec;
    public String cmdLabel;

    public PetAdminCommand(String commandLabel) {
        this.ec = EchoPetPlugin.getInstance();
        this.cmdLabel = commandLabel;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (Perm.ADMIN.hasPerm(sender, true, true)) {
                Lang.sendTo(sender, Lang.HELP.toString().replace("%cmd%", "pet help"));
                return true;
            } else return true;

        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (Perm.ADMIN_RELOAD.hasPerm(sender, true, true)) {
                    EchoPetPlugin.getInstance().getMainConfig().reloadConfig();
                    Lang.sendTo(sender, Lang.ADMIN_RELOAD_CONFIG.toString());
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("help")) {
                if (Perm.ADMIN.hasPerm(sender, true, true)) {
                    sender.sendMessage(ChatColor.RED + "------------ EchoPet Admin Help 1/6 ------------");
                    sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
                    for (String s : AdminHelpPage.getHelpPage(1)) {
                        sender.sendMessage(s);
                    }
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("list")) {
                if (Perm.ADMIN.hasPerm(sender, true, true)) {
                    sender.sendMessage(ChatColor.RED + "------------ EchoPet Pet List ------------");
                    for (String s : PetUtil.getPetList(sender, true)) {
                        sender.sendMessage(s);
                    }
                    return true;
                } else return true;
            }

        } else if (args.length >= 1 && args[0].equalsIgnoreCase("name")) {
            if (Perm.ADMIN_NAME.hasPerm(sender, true, true)) {
                if (args.length == 1 || (args.length == 2 && args[1].equalsIgnoreCase("rider"))) {
                    sender.sendMessage(ChatColor.RED + "------------ EchoPet Help - Pet Names ------------");
                    sender.sendMessage(ChatColor.GOLD + "/petadmin name <player> <name>");
                    sender.sendMessage(ChatColor.YELLOW + "    - Set the name tag of a Player's pet.");
                    sender.sendMessage(ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.");
                    sender.sendMessage(ChatColor.GOLD + "/petadmin name <player> rider <name>");
                    sender.sendMessage(ChatColor.YELLOW + "    - Set the name tag of a Player's pet's rider.");
                    sender.sendMessage(ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.");
                    return true;
                } else if (args.length >= 4 && args[1].equalsIgnoreCase("rider")) {
                    Player target = Bukkit.getPlayer(args[2]);
                    if (target == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[2]));
                        return true;
                    }

                    Pet pet = ec.PH.getPet(target);
                    if (pet == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                        return true;
                    }

                    if (pet.getRider() == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NO_RIDER.toString().replace("%player%", target.getName()));
                        return true;
                    }

                    String name = ChatColor.translateAlternateColorCodes('&', StringUtil.combineSplit(3, args, " "));
                    if (name.length() > 32) {
                        Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
                        return true;
                    }
                    pet.getRider().setPetName(name);
                    Lang.sendTo(sender, Lang.ADMIN_NAME_RIDER.toString()
                            .replace("%player%", target.getName())
                            .replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " ")))
                            .replace("%name%", name));
                    Lang.sendTo(target, Lang.NAME_RIDER.toString()
                            .replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " ")))
                            .replace("%name%", name));
                    return true;
                } else if (args.length >= 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }

                    Pet pet = ec.PH.getPet(target);
                    if (pet == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                        return true;
                    }

                    String name = ChatColor.translateAlternateColorCodes('&', StringUtil.combineSplit(2, args, " "));
                    if (name.length() > 32) {
                        Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
                        return true;
                    }
                    pet.setPetName(name);
                    Lang.sendTo(sender, Lang.ADMIN_NAME_PET.toString()
                            .replace("%player%", target.getName())
                            .replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " ")))
                            .replace("%name%", name));
                    Lang.sendTo(target, Lang.NAME_PET.toString()
                            .replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " ")))
                            .replace("%name%", name));
                    return true;
                }

            } else return true;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("menu")) {
                if (Perm.ADMIN_MENU.hasPerm(sender, true, true)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null || !target.isOnline()) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }
                    Pet pet = PetHandler.getInstance().getPet(target);

                    if (pet == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                        return true;
                    }
                    ArrayList<MenuOption> options = MenuUtil.createOptionList(pet.getPetType());
                    int size = pet.getPetType() == PetType.HORSE ? 18 : 9;
                    PetMenu menu = new PetMenu(pet, options, size);
                    menu.open(true);
                    Lang.sendTo(sender, Lang.ADMIN_OPEN_MENU.toString()
                            .replace("%player%", target.getName())
                            .replace("%type%", StringUtil.capitalise(pet.getPetType().toString())));
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("call")) {
                if (Perm.ADMIN_CALL.hasPerm(sender, true, true)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null || !target.isOnline()) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }
                    Pet pet = PetHandler.getInstance().getPet(target);

                    if (pet == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                        return true;
                    }
                    pet.teleport(target.getLocation());
                    Lang.sendTo(target, Lang.PET_CALL.toString());
                    Lang.sendTo(sender, Lang.ADMIN_PET_CALL.toString().replace("%player%", target.getName()));
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("show")) {
                if (Perm.ADMIN_SHOW.hasPerm(sender, true, true)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null || !target.isOnline()) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }
                    Pet pet = PetHandler.getInstance().getPet(target);

                    if (pet == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NO_HIDDEN_PET.toString());
                        return true;
                    }
                    Lang.sendTo(sender, Lang.ADMIN_SHOW_PET.toString()
                            .replace("%player%", target.getName())
                            .replace("%type%", StringUtil.capitalise(pet.getPetType().toString())));
                    Lang.sendTo(target, Lang.SHOW_PET.toString().replace("%type%", StringUtil.capitalise(pet.getPetType().toString())));
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("hide")) {
                if (Perm.ADMIN_HIDE.hasPerm(sender, true, true)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null || !target.isOnline()) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }
                    Pet pet = PetHandler.getInstance().getPet(target);

                    if (pet == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                        return true;
                    }
                    ec.PH.saveFileData("autosave", pet);
                    ec.SPH.saveToDatabase(pet, false);
                    ec.PH.removePet(pet, true);
                    Lang.sendTo(target, Lang.HIDE_PET.toString());
                    Lang.sendTo(sender, Lang.ADMIN_HIDE_PET.toString().replace("%player%", target.getName()));
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("select")) {
                if (Perm.ADMIN_SELECT.hasPerm(sender, true, true)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null || !target.isOnline()) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }
                    if (target.getOpenInventory() != null && target.getOpenInventory().getTitle().equals("Pets")) {
                        target.closeInventory();
                        Lang.sendTo(sender, Lang.ADMIN_CLOSE_SELECTOR.toString().replace("%player%", target.getName()));
                        return true;
                    }
                    new SelectorMenu().showTo(target);
                    Lang.sendTo(sender, Lang.ADMIN_OPEN_SELECTOR.toString().replace("%player%", target.getName()));
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("selector")) {
                if (Perm.ADMIN_SELECTOR.hasPerm(sender, true, true)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null || !target.isOnline()) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }
                    target.getInventory().addItem(SelectorLayout.getSelectorItem());
                    Lang.sendTo(target, Lang.ADD_SELECTOR.toString());
                    Lang.sendTo(sender, Lang.ADMIN_ADD_SELECTOR.toString().replace("%player%", target.getName()));
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("info")) {
                if (Perm.ADMIN_INFO.hasPerm(sender, true, true)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null || !target.isOnline()) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }
                    Pet pet = PetHandler.getInstance().getPet(target);

                    if (pet == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                        return true;
                    }
                    sender.sendMessage(ChatColor.RED + "------------ EchoPet Pet Info ------------");
                    for (String s : PetUtil.generatePetInfo(pet)) {
                        sender.sendMessage(s);
                    }
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("help")) {
                if (Perm.ADMIN.hasPerm(sender, true, true)) {
                    if (StringUtil.isInt(args[1])) {
                        sender.sendMessage(ChatColor.RED + "------------ EchoPet Admin Help " + args[1] + "/6 ------------");
                        sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
                        for (String s : AdminHelpPage.getHelpPage(Integer.parseInt(args[1]))) {
                            sender.sendMessage(s);
                        }
                        return true;
                    }
                    sender.sendMessage(ChatColor.RED + "------------ EchoPet Admin Help 1/6 ------------");
                    sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
                    for (String s : AdminHelpPage.getHelpPage(1)) {
                        sender.sendMessage(s);
                    }
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (Perm.ADMIN_REMOVE.hasPerm(sender, true, true)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null || !target.isOnline()) {
                        String path = "autosave." + args[1];
                        if (ec.getPetConfig().get(path + ".pet.type") == null) {
                            Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER_DATA.toString().replace("%player%", args[1]));
                            return true;
                        } else {
                            PetHandler.getInstance().clearFileData("autosave", args[1]);
                            EchoPetPlugin.getInstance().SPH.clearFromDatabase(args[1]);
                            Lang.sendTo(sender, Lang.ADMIN_PET_REMOVED.toString().replace("%player%", args[1]));
                            return true;
                        }
                    } else {
                        Pet pet = PetHandler.getInstance().getPet(target);

                        if (pet == null) {
                            Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                            return true;
                        }

                        ec.PH.clearFileData("autosave", pet);
                        ec.SPH.clearFromDatabase(target.getName());
                        ec.PH.removePet(pet, true);

                        Lang.sendTo(sender, Lang.ADMIN_PET_REMOVED.toString().replace("%player%", target.getName()));
                        Lang.sendTo(target, Lang.REMOVE_PET.toString());
                        return true;
                    }
                } else return true;
            } else if (args[0].equalsIgnoreCase("hat")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null || !target.isOnline()) {
                    Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                    return true;
                }
                Pet pet = PetHandler.getInstance().getPet(target);

                if (pet == null) {
                    Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                    return true;
                }
                if (Perm.hasTypePerm(sender, true, Perm.ADMIN_HAT, true, pet.getPetType())) {
                    pet.setAsHat(!pet.isHat());
                    if (pet.isHat()) {
                        Lang.sendTo(target, Lang.HAT_PET_ON.toString());
                        Lang.sendTo(sender, Lang.ADMIN_HAT_PET_ON.toString().replace("%player%", target.getName()));
                    } else {
                        Lang.sendTo(target, Lang.HAT_PET_OFF.toString());
                        Lang.sendTo(sender, Lang.ADMIN_HAT_PET_OFF.toString().replace("%player%", target.getName()));
                    }
                    return true;
                } else return true;
            } else if (args[0].equalsIgnoreCase("ride")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null || !target.isOnline()) {
                    Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                    return true;
                }
                Pet pet = PetHandler.getInstance().getPet(target);

                if (pet == null) {
                    Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                    return true;
                }
                if (Perm.hasTypePerm(sender, true, Perm.ADMIN_RIDE, true, pet.getPetType())) {
                    pet.ownerRidePet(!pet.isOwnerRiding());
                    if (pet.isOwnerRiding()) {
                        Lang.sendTo(target, Lang.RIDE_PET_ON.toString());
                        Lang.sendTo(sender, Lang.ADMIN_RIDE_PET_ON.toString().replace("%player%", target.getName()));
                    } else {
                        Lang.sendTo(target, Lang.RIDE_PET_OFF.toString());
                        Lang.sendTo(sender, Lang.ADMIN_RIDE_PET_OFF.toString().replace("%player%", target.getName()));
                    }
                    return true;
                } else return true;
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null || !target.isOnline()) {
                    Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                    return true;
                }
                UnorganisedPetData UPD = PetUtil.formPetFromArgs(ec, sender, args[1], true);
                if (UPD == null) {
                    return true;
                }
                PetType petType = UPD.petType;
                ArrayList<PetData> petDataList = UPD.petDataList;

                if (petType == null || petDataList == null) {
                    return true;
                }

                if (Perm.hasTypePerm(sender, true, Perm.ADMIN_PETTYPE, true, petType)) {
                    Pet pet = ec.PH.createPet(target, petType, true);
                    if (pet == null) {
                        return true;
                    }
                    if (!petDataList.isEmpty()) {
                        ec.PH.setData(pet, petDataList.toArray(new PetData[petDataList.size()]), true);
                    }
                    if (UPD.petName != null && !UPD.petName.equalsIgnoreCase("")) {
                        if (Perm.ADMIN_NAME.hasPerm(sender, true, true)) {
                            if (UPD.petName.length() > 32) {
                                Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
                            } else {
                                pet.setPetName(UPD.petName);
                            }
                        }
                    }
                    ec.PH.saveFileData("autosave", pet);
                    ec.SPH.saveToDatabase(pet, false);
                    Lang.sendTo(target, Lang.CREATE_PET.toString()
                            .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
                    Lang.sendTo(sender, Lang.ADMIN_CREATE_PET.toString()
                            .replace("%player%", target.getName())
                            .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
                    return true;
                } else return true;
            }

        } else if (args.length >= 3 && args[0].equalsIgnoreCase("default")) {
            if (args.length == 3 && args[2].equalsIgnoreCase("remove")) {
                if (Perm.ADMIN_DEFAULT_REMOVE.hasPerm(sender, true, true)) {
                    String name = args[1];
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null && target.isOnline()) {
                        name = target.getName();
                    }
                    String path = "default." + name + ".";
                    if (ec.getPetConfig().get(path + "pet.type") == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NO_DEFAULT.toString().replace("%player%", name));
                        return true;
                    }

                    ec.PH.clearFileData("default", name);
                    ec.SPH.clearFromDatabase(name);
                    Lang.sendTo(sender, Lang.ADMIN_REMOVE_DEFAULT.toString().replace("%player%", name));
                    return true;
                } else return true;
            } else if (args.length == 4 && args[2].equalsIgnoreCase("set")) { //args[3] == pet info
                if (args[3].equalsIgnoreCase("current")) {
                    if (Perm.ADMIN_DEFAULT_SET_CURRENT.hasPerm(sender, true, true)) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) {
                            Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                            return true;
                        }
                        Pet pet = PetHandler.getInstance().getPet(target);

                        if (pet == null) {
                            Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                            return true;
                        }

                        ec.PH.saveFileData("default", pet);
                        Lang.sendTo(sender, Lang.ADMIN_SET_DEFAULT_TO_CURRENT.toString().replace("%player%", args[2]));
                        return true;
                    } else return true;
                } else {
                    String name = args[1];
                    if (Bukkit.getPlayer(args[1]) != null) {
                        name = Bukkit.getPlayer(args[1]).getName();
                    }

                    UnorganisedPetData UPD = PetUtil.formPetFromArgs(ec, sender, args[3], false);
                    if (UPD == null) {
                        return true;
                    }
                    PetType petType = UPD.petType;
                    ArrayList<PetData> petDataList = UPD.petDataList;

                    if (petType == null || petDataList == null) {
                        return true;
                    }

                    if (Perm.hasTypePerm(sender, true, Perm.ADMIN_DEFAULT_SET_PETTYPE, true, petType)) {
                        PetHandler.getInstance().saveFileData("default", name, UPD);
                        Lang.sendTo(sender, Lang.ADMIN_SET_DEFAULT.toString()
                                .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", "")))
                                .replace("%player%", name));
                        return true;
                    } else return true;
                }
            } else if (args.length == 5 && args[2].equalsIgnoreCase("set")) {
                String name = args[1];
                if (Bukkit.getPlayer(args[1]) != null) {
                    name = Bukkit.getPlayer(args[1]).getName();
                }

                UnorganisedPetData UPD = PetUtil.formPetFromArgs(ec, sender, args[3], false);
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

                if (Perm.hasTypePerm(sender, true, Perm.ADMIN_DEFAULT_SET_PETTYPE, true, petType) && Perm.hasTypePerm(sender, true, Perm.ADMIN_DEFAULT_SET_PETTYPE, true, riderType)) {
                    PetHandler.getInstance().saveFileData("default", name, UPD, UMD);
                    Lang.sendTo(sender, Lang.ADMIN_SET_DEFAULT_WITH_RIDER.toString()
                            .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", "")))
                            .replace("%mtype%", StringUtil.capitalise(riderType.toString().replace("_", "")))
                            .replace("%player%", name));
                    return true;
                } else return true;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("rider")) {
                if (args[1].equalsIgnoreCase("remove")) {
                    if (Perm.ADMIN_REMOVE.hasPerm(sender, true, true)) {
                        Player target = Bukkit.getPlayer(args[2]);
                        if (target == null) {
                            String path = "autosave." + "." + args[2];
                            if (ec.getPetConfig().get(path + ".rider.type") == null) {
                                Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER_DATA.toString().replace("%player%", args[2]));
                                return true;
                            } else {
                                for (String key : ec.getPetConfig().getConfigurationSection(path + ".rider").getKeys(false)) {
                                    ec.getPetConfig().set(path + ".rider" + key, null);
                                }

                                EchoPetPlugin.getInstance().SPH.clearRiderFromDatabase(args[2]);
                                Lang.sendTo(sender, Lang.ADMIN_REMOVE_RIDER.toString().replace("%player%", args[2]));
                                return true;
                            }
                        } else {
                            Pet pet = PetHandler.getInstance().getPet(target);

                            if (pet == null) {
                                Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                                return true;
                            }

                            if (pet.getRider() == null) {
                                Lang.sendTo(sender, Lang.ADMIN_NO_RIDER.toString().replace("%player%", target.getName()));
                                return true;
                            }

                            ec.PH.clearFileData("autosave", pet);
                            ec.SPH.clearFromDatabase(target.getName());
                            ec.PH.removePet(pet, true);

                            Lang.sendTo(sender, Lang.ADMIN_REMOVE_RIDER.toString().replace("%player%", target.getName()));
                            Lang.sendTo(target, Lang.REMOVE_RIDER.toString());
                            return true;
                        }
                    } else return true;
                } else {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }
                    Pet pet = PetHandler.getInstance().getPet(target);

                    if (pet == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                        return true;
                    }

                    UnorganisedPetData UPD = PetUtil.formPetFromArgs(ec, sender, args[2], true);
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

                    if (Perm.hasTypePerm(sender, true, Perm.ADMIN_PETTYPE, true, petType)) {
                        Pet rider = pet.createRider(petType, true);
                        if (rider == null) {
                            return true;
                        }
                        if (!petDataList.isEmpty()) {
                            ec.PH.setData(rider, petDataList.toArray(new PetData[petDataList.size()]), true);
                        }
                        if (UPD.petName != null && !UPD.petName.equalsIgnoreCase("")) {
                            if (Perm.ADMIN_NAME.hasPerm(sender, true, true)) {
                                if (UPD.petName.length() > 32) {
                                    Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
                                } else {
                                    rider.setPetName(UPD.petName);
                                }
                            }
                        }
                        ec.PH.saveFileData("autosave", pet);
                        ec.SPH.saveToDatabase(pet, false);
                        Lang.sendTo(target, Lang.CHANGE_RIDER.toString()
                                .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
                        Lang.sendTo(sender, Lang.ADMIN_CHANGE_RIDER.toString()
                                .replace("%player%", target.getName())
                                .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
                        return true;
                    } else return true;
                }
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[0]));
                    return true;
                }

                UnorganisedPetData UPD = PetUtil.formPetFromArgs(ec, sender, args[1], false);
                if (UPD == null) {
                    return true;
                }
                PetType petType = UPD.petType;
                ArrayList<PetData> petDataList = UPD.petDataList;

                UnorganisedPetData UMD = PetUtil.formPetFromArgs(ec, sender, args[2], false);
                if (UMD == null) {
                    return true;
                }
                PetType riderType = UMD.petType;
                ArrayList<PetData> riderDataList = UMD.petDataList;

                if (petType == null || petDataList == null || riderType == null || riderDataList == null) {
                    return true;
                }

                if (Perm.hasTypePerm(sender, true, Perm.ADMIN_PETTYPE, true, petType) && Perm.hasTypePerm(sender, true, Perm.ADMIN_PETTYPE, true, riderType)) {
                    Pet pi = ec.PH.createPet(target, petType, riderType, true);
                    if (pi == null) {
                        return true;
                    }
                    if (!petDataList.isEmpty()) {
                        ec.PH.setData(pi, petDataList.toArray(new PetData[petDataList.size()]), true);
                    }
                    if (UPD.petName != null && !UPD.petName.equalsIgnoreCase("")) {
                        if (Perm.ADMIN_NAME.hasPerm(sender, true, true)) {
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
                        if (Perm.ADMIN_NAME.hasPerm(sender, true, true)) {
                            if (UPD.petName.length() > 32) {
                                Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
                            } else {
                                pi.getRider().setPetName(UPD.petName);
                            }
                        }
                    }
                    ec.PH.saveFileData("autosave", pi);
                    ec.SPH.saveToDatabase(pi, false);
                    Lang.sendTo(target, Lang.CREATE_PET_WITH_RIDER.toString()
                            .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", "")))
                            .replace("%mtype%", StringUtil.capitalise(riderType.toString().replace("_", ""))));
                    Lang.sendTo(sender, Lang.ADMIN_CREATE_PET_WITH_RIDER.toString()
                            .replace("%player%", target.getName())
                            .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", "")))
                            .replace("%mtype%", StringUtil.capitalise(riderType.toString().replace("_", ""))));
                    return true;
                } else return true;
            }
        }

        // Something went wrong. Maybe the player didn't use a command correctly?
        // Send them a message with the exact command to make sure
        if (!AdminHelpPage.sendRelevantHelpMessage(sender, args)) {
            Lang.sendTo(sender, Lang.ADMIN_COMMAND_ERROR.toString()
                    .replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil.combineSplit(0, args, " "))));
        }
        return true;
    }
}