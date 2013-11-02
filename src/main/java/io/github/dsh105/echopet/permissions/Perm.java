package io.github.dsh105.echopet.permissions;

import io.github.dsh105.echopet.entity.living.data.PetData;
import io.github.dsh105.echopet.entity.living.data.PetType;
import io.github.dsh105.echopet.util.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Project by DSH105
 */

public enum Perm {

    ADMIN(PermType.ADMIN, "echopet.petadmin", ""),
    ADMIN_CALL(PermType.ADMIN, "echopet.petadmin.call", ""),
    ADMIN_DEFAULT_REMOVE(PermType.ADMIN, "echopet.petadmin.default.remove", "", "echopet.petadmin.default.*"),
    ADMIN_DEFAULT_SET_CURRENT(PermType.ADMIN, "echopet.petadmin.default.set.current", "", "echopet.petadmin.default.*"),
    ADMIN_DEFAULT_SET_PETTYPE(PermType.ADMIN, "echopet.petadmin!default.set.type", "", "echopet.petadmin.default.*", "echopet.petadmin.default.set.type.*"),
    ADMIN_HAT(PermType.ADMIN, "echopet.petadmin!hat", "", "echopet.petadmin.hat.*"),
    ADMIN_HIDE(PermType.ADMIN, "echopet.petadmin.hide", ""),
    ADMIN_INFO(PermType.ADMIN, "echopet.petadmin.info", ""),
    ADMIN_MENU(PermType.ADMIN, "echopet.petadmin.menu", ""),
    ADMIN_NAME(PermType.ADMIN, "echopet.petadmin.name", ""),
    ADMIN_PETTYPE(PermType.ADMIN, "echopet.petadmin!type", ""),
    ADMIN_RELOAD(PermType.ADMIN, "echopet.petadmin.reload", ""),
    ADMIN_REMOVE(PermType.ADMIN, "echopet.petadmin.remove", ""),
    ADMIN_RIDE(PermType.ADMIN, "echopet.petadmin!ride", "", "echopet.petadmin.ride.*"),
    ADMIN_SHOW(PermType.ADMIN, "echopet.petadmin.show", ""),
    ADMIN_SELECT(PermType.ADMIN, "echopet.petadmin.select", ""),
    ADMIN_SELECTOR(PermType.ADMIN, "echopet.petadmin.selector", ""),

    BASE(PermType.BASE, "echopet.pet", ""),
    BASE_CALL(PermType.BASE, "echopet.pet.call", ""),
    BASE_DEFAULT_REMOVE(PermType.BASE, "echopet.pet.default.remove", "", "echopet.pet.default.*"),
    BASE_DEFAULT_SET_CURRENT(PermType.BASE, "echopet.pet.default.set.current", "", "echopet.pet.default.*"),
    BASE_DEFAULT_SET_PETTYPE(PermType.BASE, "echopet.pet!default.set.type", "", "echopet.pet.default.*", "echopet.pet.default.set.type.*"),
    BASE_HAT(PermType.BASE, "echopet.pet!hat", "", "echopet.pet.hat.*"),
    BASE_HIDE(PermType.BASE, "echopet.pet.hide", ""),
    BASE_INFO(PermType.BASE, "echopet.pet.info", ""),
    BASE_MENU(PermType.BASE, "echopet.pet.menu", ""),
    BASE_NAME(PermType.BASE, "echopet.pet.name", ""),
    BASE_PETTYPE(PermType.BASE, "echopet.pet!type", "", "echopet.pet.type.*"),
    BASE_REMOVE(PermType.BASE, "echopet.pet.remove", ""),
    BASE_RIDE(PermType.BASE, "echopet.pet!ride", "", "echopet.pet.ride.*"),
    BASE_SHOW(PermType.BASE, "echopet.pet.show", ""),
    BASE_SELECT(PermType.BASE, "echopet.pet.select", ""),
    BASE_SELECTOR(PermType.BASE, "echopet.pet.selector", ""),
    ;

    PermType permType;
    String perm;
    String requiredPerm;
    ArrayList<String> hierarchy = new ArrayList<String>();

    Perm(PermType permType, String perm, String requiredPerm, String... hierarchy) {
        this.permType = permType;
        this.perm = perm;
        this.requiredPerm = requiredPerm;
        for (String s : hierarchy) {
            this.hierarchy.add(s);
            this.hierarchy.add("echopet.*");
            this.hierarchy.add(this.permType.getPerm());
        }
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
        boolean hasRequiredPerm = this.requiredPerm.equalsIgnoreCase("") ? true : player.hasPermission(this.requiredPerm);
        if (!(player.hasPermission(this.perm) && hasRequiredPerm)) {
            for (String s : this.hierarchy) {
                if (player.hasPermission(s)) {
                    return true;
                }
            }
            if (sendMessage) {
                Lang.sendTo(player, Lang.NO_PERMISSION.toString().replace("%perm%", this.perm));
            }
            return false;
        } else {
            return true;
        }
    }

    public static boolean hasTypePerm(Player player, boolean sendMessage, Perm base, PetType petType) {
        String perm = "echopet.pet." + base.perm.split("!")[1] + "." + petType.toString().toLowerCase();
        if (!(player.hasPermission(perm))) {
            for (String s : base.hierarchy) {
                if (player.hasPermission(s)) {
                    return true;
                }
            }
            if (sendMessage) {
                Lang.sendTo(player, Lang.NO_PERMISSION.toString().replace("%perm%", perm));
            }
            return false;
        } else {
            return true;
        }
    }

    public static boolean hasDataPerm(Player player, boolean sendMessage, PetType petType, PetData petData) {
        boolean hpp = hasTypePerm(player, sendMessage, Perm.BASE_PETTYPE, petType);
        if (!hpp) {
            return false;
        }

        String dataPerm = "echopet.pet.type." + petType.toString().toLowerCase() + "." + petData.getConfigOptionString().toLowerCase();
        boolean hdp = player.hasPermission(dataPerm) || player.hasPermission("echopet.pet.type." + petType.toString().toLowerCase() + ".*");
        if (!hdp) {
            Lang.sendTo(player, Lang.NO_PERMISSION.toString().replace("%perm%", dataPerm));
            return false;
        }

        return true;
    }
}