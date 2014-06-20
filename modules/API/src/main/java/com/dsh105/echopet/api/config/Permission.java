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

package com.dsh105.echopet.api.config;

import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Player;

public class Permission {

    public static Permission
            PET = new Permission("echopet.pet"),
            PET_TYPE = new Permission(PET, "type.%s"),
            PET_HAT = new Permission(PET, "hat.%s"),
            PET_RIDE = new Permission(PET, "ride.%s"),
            PET_LIST = new Permission(PET, "list"),
            PET_CALL = new Permission(PET, "call"),
            PET_INFO = new Permission(PET, "info"),
            PET_MENU = new Permission(PET, "menu"),
            PET_NAME = new Permission(PET, "name"),
            PET_REMOVE = new Permission(PET, "remove"),
            PET_SHOW = new Permission(PET, "show"),
            PET_HIDE = new Permission(PET, "hide"),
            PET_TOGGLE = new Permission(PET, "toggle"),
            PET_SELECT = new Permission(PET, "select"),
            PET_SELECTOR = new Permission(PET, "selector"),
            PET_DEFAULT_REMOVE = new Permission(PET, "default.remove"),
            PET_DEFAULT_SET_CURRENT = new Permission(PET, "default.set.current"),
            PET_DEFAULT_SET = new Permission(PET, "default.set.type.%s");
    private String perm;

    public Permission(String perm) {
        this.perm = perm;
    }

    public Permission(Permission parent, String appendix) {
        this.perm = parent.perm + "." + appendix;
    }

    public String getPerm(String... replacements) {
        return String.format(perm, replacements);
    }

    public boolean hasPerm(Player player, boolean sendMessage, String... replacements) {
        if (!player.hasPermission(getPerm(replacements))) {
            if (sendMessage) {
                Lang.NO_PERMISSION.send(player, "%perm%", getPerm(replacements));
            }
            return false;
        }
        return true;
    }

    public boolean hasPerm(Player player, String... replacements) {
        return hasPerm(player, true, replacements);
    }

    public boolean hasPerm(CommandSender sender, boolean allowConsole, boolean sendMessage, String... replacements) {
        if (sender instanceof Player) {
            return hasPerm((Player) sender, sendMessage, replacements);
        }
        if (!allowConsole && sendMessage) {
            Lang.IN_GAME_ONLY.send(sender);
        }
        return allowConsole;
    }

    public boolean hasPerm(CommandSender sender, boolean allowConsole, String... replacements) {
        return hasPerm(sender, allowConsole, true, replacements);
    }

    public boolean hasPerm(Conversable conversable, boolean allowConsole, boolean sendMessage, String... replacements) {
        if (conversable instanceof CommandSender) {
            return hasPerm((CommandSender) conversable, allowConsole, sendMessage, replacements);
        }
        return false;
    }

    public boolean hasPerm(Conversable conversable, boolean allowConsole, String... replacements) {
        return hasPerm(conversable, allowConsole, false, replacements);
    }

    public static class Admin {

        public static Permission
                UPDATE = new Permission("echopet.update"),
                ADMIN = new Permission("echopet.petadmin"),
                ADMIN_RELOAD = new Permission(ADMIN, "reload"),
                ADMIN_TYPE = new Permission(ADMIN, "type.%s"),
                ADMIN_HAT = new Permission(ADMIN, "hat.%s"),
                ADMIN_RIDE = new Permission(ADMIN, "ride.%s"),
                ADMIN_LIST = new Permission(ADMIN, "list"),
                ADMIN_CALL = new Permission(ADMIN, "call"),
                ADMIN_INFO = new Permission(ADMIN, "info"),
                ADMIN_MENU = new Permission(ADMIN, "menu"),
                ADMIN_NAME = new Permission(ADMIN, "name"),
                ADMIN_REMOVE = new Permission(ADMIN, "remove"),
                ADMIN_SHOW = new Permission(ADMIN, "show"),
                ADMIN_HIDE = new Permission(ADMIN, "hide"),
                ADMIN_TOGGLE = new Permission(ADMIN, "toggle"),
                ADMIN_SELECT = new Permission(ADMIN, "select"),
                ADMIN_SELECTOR = new Permission(ADMIN, "selector"),
                ADMIN_DEFAULT_REMOVE = new Permission(ADMIN, "default.remove"),
                ADMIN_DEFAULT_SET_CURRENT = new Permission(ADMIN, "default.set.current"),
                ADMIN_DEFAULT_SET = new Permission(ADMIN, "default.set.type.%s");
    }

}