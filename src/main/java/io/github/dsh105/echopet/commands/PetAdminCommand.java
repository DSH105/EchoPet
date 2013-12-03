package io.github.dsh105.echopet.commands;

import io.github.dsh105.echopet.EchoPet;
import io.github.dsh105.echopet.entity.living.data.PetData;
import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.entity.living.data.PetType;
import io.github.dsh105.echopet.data.UnorganisedPetData;
import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.menu.main.MenuOption;
import io.github.dsh105.echopet.menu.main.PetMenu;
import io.github.dsh105.echopet.menu.selector.PetSelector;
import io.github.dsh105.echopet.menu.selector.SelectorItem;
import io.github.dsh105.echopet.util.permissions.Perm;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.MenuUtil;
import io.github.dsh105.echopet.util.PetUtil;
import io.github.dsh105.echopet.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.ArrayList;

public class PetAdminCommand implements CommandExecutor {

    private EchoPet ec;
    public String cmdLabel;

    public PetAdminCommand(String commandLabel) {
        this.ec = EchoPet.getPluginInstance();
        this.cmdLabel = commandLabel;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (Perm.ADMIN.hasPerm(sender, true, true)) {
                PluginDescriptionFile pdFile = ec.getDescription();
                sender.sendMessage(ChatColor.RED + "-------- EchoPet --------");
                sender.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.YELLOW + "DSH105");
                sender.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.YELLOW + pdFile.getDescription());
                sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.YELLOW + pdFile.getVersion());
                sender.sendMessage(ChatColor.GOLD + "Website: " + ChatColor.YELLOW + pdFile.getWebsite());
                return true;
            } else return true;

        }

        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (Perm.ADMIN_RELOAD.hasPerm(sender, true, true)) {
                    EchoPet.getPluginInstance().getMainConfig().reloadConfig();
                    Lang.sendTo(sender, Lang.ADMIN_RELOAD_CONFIG.toString());
                    return true;
                } else return true;
            }

            else if (args[0].equalsIgnoreCase("help")) {
                if (Perm.ADMIN.hasPerm(sender, true, true)) {
                    sender.sendMessage(ChatColor.RED + "------------ EchoPet Admin Help 1/6 ------------");
                    sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
                    for (String s : AdminHelpPage.getHelpPage(1)) {
                        sender.sendMessage(s);
                    }
                    return true;
                } else return true;
            }

            else if (args[0].equalsIgnoreCase("list")) {
                if (Perm.ADMIN.hasPerm(sender, true, true)) {
                    sender.sendMessage(ChatColor.RED + "------------ EchoPet Pet List ------------");
                    for (String s : StringUtil.getPetList(sender, true)) {
                        sender.sendMessage(s);
                    }
                    return true;
                } else return true;
            }

        }

        else if (args.length == 2) {

            if (args[0].equalsIgnoreCase("menu")) {
                if (Perm.ADMIN_MENU.hasPerm(sender, true, true)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }
                    LivingPet pet = PetHandler.getInstance().getPet(target);

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
            }

            else if (args[0].equalsIgnoreCase("call")) {
                if (Perm.ADMIN_CALL.hasPerm(sender, true, true)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }
                    LivingPet pet = PetHandler.getInstance().getPet(target);

                    if (pet == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                        return true;
                    }
                    pet.teleport(target.getLocation());
                    Lang.sendTo(target, Lang.PET_CALL.toString());
                    Lang.sendTo(sender, Lang.ADMIN_PET_CALL.toString().replace("%player%", target.getName()));
                    return true;
                } else return true;
            }

            else if (args[0].equalsIgnoreCase("show")) {
                if (Perm.ADMIN_SHOW.hasPerm(sender, true, true)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }
                    LivingPet pet = PetHandler.getInstance().getPet(target);

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
            }

            else if (args[0].equalsIgnoreCase("hide")) {
                if (Perm.ADMIN_HIDE.hasPerm(sender, true, true)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }
                    LivingPet pet = PetHandler.getInstance().getPet(target);

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
            }

            else if (args[0].equalsIgnoreCase("select")) {
                if (Perm.ADMIN_SELECT.hasPerm(sender, true, true)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }
                    PetSelector petSelector = new PetSelector(45, target);
                    petSelector.open(false);
                    Lang.sendTo(sender, Lang.ADMIN_OPEN_SELECTOR.toString().replace("%player%", target.getName()));
                    return true;
                } else return true;
            }

            else if (args[0].equalsIgnoreCase("selector")) {
                if (Perm.ADMIN_SELECTOR.hasPerm(sender, true, true)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }
                    target.getInventory().addItem(SelectorItem.SELECTOR.getItem());
                    Lang.sendTo(target, Lang.ADD_SELECTOR.toString());
                    Lang.sendTo(sender, Lang.ADMIN_ADD_SELECTOR.toString().replace("%player%", target.getName()));
                    return true;
                } else return true;
            }

            else if (args[0].equalsIgnoreCase("info")) {
                if (Perm.ADMIN_INFO.hasPerm(sender, true, true)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }
                    LivingPet pet = PetHandler.getInstance().getPet(target);

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
            }

            else if (args[0].equalsIgnoreCase("help")) {
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
            }
            else if (args[0].equalsIgnoreCase("remove")) {
                if (Perm.ADMIN_REMOVE.hasPerm(sender, true, true)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        String path = "autosave." + args[1];
                        if (ec.getPetConfig().get(path + ".pet.type") == null) {
                            Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                            return true;
                        } else {
                            PetHandler.getInstance().clearFileData("autosave", args[1]);
                            EchoPet.getPluginInstance().SPH.clearFromDatabase(args[1]);
                            Lang.sendTo(sender, Lang.ADMIN_PET_REMOVED.toString().replace("%player%", target.getName()));
                            return true;
                        }
                    } else {
                        LivingPet pet = PetHandler.getInstance().getPet(target);

                        if (pet == null) {
                            Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                            return true;
                        }

                        ec.PH.clearFileData("autosave", pet);
                        ec.SPH.clearFromDatabase(target);
                        ec.PH.removePet(pet, true);

                        Lang.sendTo(sender, Lang.ADMIN_PET_REMOVED.toString().replace("%player%", target.getName()));
                        Lang.sendTo(target, Lang.REMOVE_PET.toString());
                        return true;
                    }
                } else return true;
            }

            else if (args[0].equalsIgnoreCase("hat")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                    return true;
                }
                LivingPet pet = PetHandler.getInstance().getPet(target);

                if (pet == null) {
                    Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                    return true;
                }
                if (Perm.hasTypePerm(sender, true, Perm.ADMIN_HAT, true, pet.getPetType())) {
                    pet.setAsHat(!pet.isPetHat());
                    if (pet.isPetHat()) {
                        Lang.sendTo(target, Lang.HAT_PET_ON.toString());
                        Lang.sendTo(sender, Lang.ADMIN_HAT_PET_ON.toString().replace("%player%", target.getName()));
                    } else {
                        Lang.sendTo(target, Lang.HAT_PET_OFF.toString());
                        Lang.sendTo(sender, Lang.ADMIN_HAT_PET_OFF.toString().replace("%player%", target.getName()));
                    }
                    return true;
                } else return true;
            }

            else if (args[0].equalsIgnoreCase("ride")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                    return true;
                }
                LivingPet pet = PetHandler.getInstance().getPet(target);

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
            }

            else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
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
                    LivingPet pet = ec.PH.createPet(target, petType, true);
                    if (pet == null) {
                        return true;
                    }
                    if (!petDataList.isEmpty()) {
                        ec.PH.setData(pet, petDataList.toArray(new PetData[petDataList.size()]), true);
                    }
                    if (UPD.petName != null && !UPD.petName.equalsIgnoreCase("")) {
                        pet.setName(UPD.petName);
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

        }

        else if (args.length >= 3 && args[0].equalsIgnoreCase("default")) {
            if (args.length == 3 && args[2].equalsIgnoreCase("remove")) {
                if (Perm.ADMIN_DEFAULT_REMOVE.hasPerm(sender, true, true)) {
                    String name = args[1];
                    if (Bukkit.getPlayer(args[1]) != null) {
                        name = Bukkit.getPlayer(args[1]).getName();
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
            }

            else if (args.length == 4 && args[2].equalsIgnoreCase("set")) { //args[3] == pet info
                if (args[3].equalsIgnoreCase("current")) {
                    if (Perm.ADMIN_DEFAULT_SET_CURRENT.hasPerm(sender, true, true)) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) {
                            Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                            return true;
                        }
                        LivingPet pet = PetHandler.getInstance().getPet(target);

                        if (pet == null) {
                            Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                            return true;
                        }

                        ec.PH.saveFileData("default", pet);
                        Lang.sendTo(sender, Lang.ADMIN_SET_DEFAULT_TO_CURRENT.toString().replace("%player%", args[2]));
                        return true;
                    } else return true;
                }

                else {
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
            }

            else if (args.length == 5 && args[2].equalsIgnoreCase("set")) {
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
                PetType mountType = UMD.petType;
                ArrayList<PetData> mountDataList = UMD.petDataList;

                if (petType == null || petDataList == null || mountType == null || mountDataList == null) {
                    return true;
                }

                if (Perm.hasTypePerm(sender, true, Perm.ADMIN_DEFAULT_SET_PETTYPE, true, petType) && Perm.hasTypePerm(sender, true, Perm.ADMIN_DEFAULT_SET_PETTYPE, true, mountType)) {
                    PetHandler.getInstance().saveFileData("default", name, UPD, UMD);
                    Lang.sendTo(sender, Lang.ADMIN_SET_DEFAULT_WITH_MOUNT.toString()
                            .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", "")))
                            .replace("%mtype%", StringUtil.capitalise(mountType.toString().replace("_", "")))
                            .replace("%player%", name));
                    return true;
                } else return true;
            }
        }

        else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("mount")) {
                if (args[1].equalsIgnoreCase("remove")) {
                    if (Perm.ADMIN_REMOVE.hasPerm(sender, true, true)) {
                        Player target = Bukkit.getPlayer(args[2]);
                        if (target == null) {
                            String path = "autosave." + "." + args[2];
                            if (ec.getPetConfig().get(path + ".mount.type") == null) {
                                Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[2]));
                                return true;
                            } else {
                                for (String key : ec.getPetConfig().getConfigurationSection(path + ".mount").getKeys(false)) {
                                    ec.getPetConfig().set(path + ".mount" + key, null);
                                }

                                EchoPet.getPluginInstance().SPH.clearMountFromDatabase(args[2]);
                                Lang.sendTo(sender, Lang.ADMIN_REMOVE_MOUNT.toString().replace("%player%", target.getName()));
                                return true;
                            }
                        } else {
                            LivingPet pet = PetHandler.getInstance().getPet(target);

                            if (pet == null) {
                                Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                                return true;
                            }

                            if (pet.getMount() == null) {
                                Lang.sendTo(sender, Lang.ADMIN_NO_MOUNT.toString().replace("%player%", target.getName()));
                                return true;
                            }

                            ec.PH.clearFileData("autosave", pet);
                            ec.SPH.clearFromDatabase(target);
                            ec.PH.removePet(pet, true);

                            Lang.sendTo(sender, Lang.ADMIN_REMOVE_MOUNT.toString().replace("%player%", target.getName()));
                            Lang.sendTo(target, Lang.REMOVE_MOUNT.toString());
                            return true;
                        }
                    } else return true;
                }

                else {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }
                    LivingPet pet = PetHandler.getInstance().getPet(target);

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

                    if (!ec.options.allowMounts(petType)) {
                        Lang.sendTo(sender, Lang.MOUNTS_DISABLED.toString()
                                .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", " "))));
                        return true;
                    }

                    if (Perm.hasTypePerm(sender, true, Perm.ADMIN_PETTYPE, true, petType)) {
                        LivingPet mount = pet.createMount(petType, true);
                        if (mount == null) {
                            return true;
                        }
                        if (!petDataList.isEmpty()) {
                            ec.PH.setData(mount, petDataList.toArray(new PetData[petDataList.size()]), true);
                        }
                        if (UPD.petName != null && !UPD.petName.equalsIgnoreCase("")) {
                            mount.setName(UPD.petName);
                        }
                        ec.PH.saveFileData("autosave", pet);
                        ec.SPH.saveToDatabase(pet, false);
                        Lang.sendTo(target, Lang.CHANGE_MOUNT.toString()
                                .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
                        Lang.sendTo(sender, Lang.ADMIN_CHANGE_MOUNT.toString()
                                .replace("%player%", target.getName())
                                .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
                        return true;
                    } else return true;
                }
            }

            else {
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
                PetType mountType = UMD.petType;
                ArrayList<PetData> mountDataList = UMD.petDataList;

                if (petType == null || petDataList == null || mountType == null || mountDataList == null) {
                    return true;
                }

                if (Perm.hasTypePerm(sender, true, Perm.ADMIN_PETTYPE, true, petType) && Perm.hasTypePerm(sender, true, Perm.ADMIN_PETTYPE, true, mountType)) {
                    LivingPet pi = ec.PH.createPet(target, petType, mountType, true);
                    if (pi == null) {
                        return true;
                    }
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
                    Lang.sendTo(target, Lang.CREATE_PET_WITH_MOUNT.toString()
                            .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", "")))
                            .replace("%mtype%", StringUtil.capitalise(mountType.toString().replace("_", ""))));
                    Lang.sendTo(sender, Lang.ADMIN_CREATE_PET_WITH_MOUNT.toString()
                            .replace("%player%", target.getName())
                            .replace("%type%", StringUtil.capitalise(petType.toString().replace("_", "")))
                            .replace("%mtype%", StringUtil.capitalise(mountType.toString().replace("_", ""))));
                    return true;
                } else return true;
            }
        }

        else if (args.length >= 1 && args[0].equalsIgnoreCase("name")) {
            if (Perm.ADMIN_NAME.hasPerm(sender, true, true)) {
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

                else if (args.length >= 3 && args[1].equalsIgnoreCase("mount")) {
                    Player target = Bukkit.getPlayer(args[2]);
                    if (target == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[2]));
                        return true;
                    }

                    LivingPet pet = ec.PH.getPet(target);
                    if (pet == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                        return true;
                    }

                    if (pet.getMount() == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NO_MOUNT.toString().replace("%player%", target.getName()));
                        return true;
                    }

                    String name = StringUtil.replaceStringWithColours(StringUtil.combineSplit(3, args, " "));
                    if (name.length() > 32) {
                        Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
                        return true;
                    }
                    pet.getMount().setName(name);
                    Lang.sendTo(sender, Lang.ADMIN_NAME_MOUNT.toString()
                            .replace("%player%", target.getName())
                            .replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " ")))
                            .replace("%name%", name));
                    Lang.sendTo(target, Lang.NAME_MOUNT.toString()
                            .replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " ")))
                            .replace("%name%", name));
                    return true;
                }

                else if (args.length >= 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
                        return true;
                    }

                    LivingPet pet = ec.PH.getPet(target);
                    if (pet == null) {
                        Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
                        return true;
                    }

                    String name = StringUtil.replaceStringWithColours(StringUtil.combineSplit(2, args, " "));
                    if (name.length() > 32) {
                        Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
                        return true;
                    }
                    pet.setName(name);
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