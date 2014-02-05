package io.github.dsh105.echopet.util;

import io.github.dsh105.echopet.entity.PetData;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public enum Perm {

    ADMIN("echopet.petadmin"),
    ADMIN_CALL("echopet.petadmin.call"),
    ADMIN_DEFAULT_REMOVE("echopet.petadmin.default.remove"),
    ADMIN_DEFAULT_SET_CURRENT("echopet.petadmin.default.set.current"),
    ADMIN_DEFAULT_SET_PETTYPE("echopet.petadmin!default.set.type"),
    ADMIN_HAT("echopet.petadmin!hat"),
    ADMIN_RIDE("echopet.petadmin!ride"),
    ADMIN_HIDE("echopet.petadmin.hide"),
    ADMIN_INFO("echopet.petadmin.info"),
    ADMIN_MENU("echopet.petadmin.menu"),
    ADMIN_NAME("echopet.petadmin.name"),
    ADMIN_PETTYPE("echopet.petadmin!type"),
    ADMIN_RELOAD("echopet.petadmin.reload"),
    ADMIN_REMOVE("echopet.petadmin.remove"),
    ADMIN_SHOW("echopet.petadmin.show"),
    ADMIN_SELECT("echopet.petadmin.select"),
    ADMIN_SELECTOR("echopet.petadmin.selector"),

    BASE("echopet.pet"),
    LIST("echopet.pet.list"),
    BASE_CALL("echopet.pet.call"),
    BASE_DEFAULT_REMOVE("echopet.pet.default.remove"),
    BASE_DEFAULT_SET_CURRENT("echopet.pet.default.set.current"),
    BASE_DEFAULT_SET_PETTYPE("echopet.pet!default.set.type"),
    BASE_HAT("echopet.pet!hat"),
    BASE_HIDE("echopet.pet.hide"),
    BASE_RIDE("echopet.pet!ride"),
    BASE_INFO("echopet.pet.info"),
    BASE_MENU("echopet.pet.menu"),
    BASE_NAME("echopet.pet.name"),
    BASE_PETTYPE("echopet.pet!type"),
    BASE_REMOVE("echopet.pet.remove"),
    BASE_SHOW("echopet.pet.show"),
    BASE_SELECT("echopet.pet.select"),
    BASE_SELECTOR("echopet.pet.selector"),;

    String perm;

    Perm(String perm) {
        this.perm = perm;
    }

    public boolean hasPerm(CommandSender sender, boolean sendMessage, boolean allowConsole) {
        if (sender instanceof Player) {
            return hasPerm(((Player) sender), sendMessage);
        } else {
            if (!allowConsole && sendMessage) {
                Lang.sendTo(sender, Lang.IN_GAME_ONLY.toString());
            }
            return allowConsole;
        }
    }

    public static boolean hasTypePerm(CommandSender sender, boolean sendMessage, Perm base, boolean allowConsole, PetType petType) {
        if (sender instanceof Player) {
            return hasTypePerm(((Player) sender), sendMessage, base, petType);
        } else {
            if (!allowConsole && sendMessage) {
                Lang.sendTo(sender, Lang.IN_GAME_ONLY.toString());
            }
            return allowConsole;
        }
    }

    public static boolean hasDataPerm(CommandSender sender, boolean sendMessage, PetType petType, PetData petData, boolean allowConsole) {
        if (sender instanceof Player) {
            return hasDataPerm(((Player) sender), sendMessage, petType, petData);
        } else {
            if (!allowConsole && sendMessage) {
                Lang.sendTo(sender, Lang.IN_GAME_ONLY.toString());
            }
            return allowConsole;
        }
    }

    public boolean hasPerm(Player player, boolean sendMessage) {
        if (player.hasPermission(this.perm)) {
            return true;
        }
        if (sendMessage) {
            Lang.sendTo(player, Lang.NO_PERMISSION.toString().replace("%perm%", this.perm));
        }
        //ConsoleLogger.log(Logger.LogLevel.NORMAL, player.getName() + " was denied access to command. " + perm + " permission needed.");
        return false;
    }

    public static boolean hasTypePerm(Player player, boolean sendMessage, Perm base, PetType petType) {
        String perm = "echopet.pet." + base.perm.split("!")[1] + "." + petType.toString().toLowerCase();
        if (player.hasPermission(perm)) {
            return true;
        }
        if (sendMessage) {
            Lang.sendTo(player, Lang.NO_PERMISSION.toString().replace("%perm%", perm));
        }
        //ConsoleLogger.log(Logger.LogLevel.NORMAL, player.getName() + " was denied access to command. " + perm + " permission needed.");
        return false;
    }

    public static boolean hasDataPerm(Player player, boolean sendMessage, PetType petType, PetData petData) {
        boolean hasTypePerm = hasTypePerm(player, sendMessage, Perm.BASE_PETTYPE, petType);
        if (!hasTypePerm) {
            return false;
        }
        String dataPerm = "echopet.pet.type." + petType.toString().toLowerCase() + "." + petData.getConfigOptionString().toLowerCase();
        if (player.hasPermission(dataPerm)) {
            return true;
        }

        if (sendMessage) {
            Lang.sendTo(player, Lang.NO_PERMISSION.toString().replace("%perm%", dataPerm));
        }
        //ConsoleLogger.log(Logger.LogLevel.NORMAL, player.getName() + " was denied access to command. " + dataPerm + " permission needed.");
        return false;
    }
}