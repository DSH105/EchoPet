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

package io.github.dsh105.echopet.commands;

import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.util.FancyItemUtil;
import io.github.dsh105.echopet.util.Perm;
import io.github.dsh105.echopet.util.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public enum HelpEntry {

    PET("<type>:[data],[data];[name]", false, new String[] {"echopet.pet.type.<type>"}, "Changes your current pet", "Each data value is separated by a comma", "Pet names can be entered using a semi-colon"),
    WITH_MOUNT("<type>:[data],[data];[name]", false, new String[] {"echopet.pet.type.<type>", "echopet.pet.type.<rider_type>"}, "Changes your current pet and adds a rider", "Each data value is separated by a comma", "Pet names can be entered using a semi-colon"),
    NAME("name [name]", true, new String[] {"echopet.pet.name"}, "Set the name of your pet", "If no arguments are provided, EchoPet will ask for one separately", "Names can be more than one word but no longer than 64 characters"),
    REMOVE("remove", true, new String[] {"echopet.pet.remove"}, "Removes your current pet"),
    RIDER("rider <type>:[data],[data];name", false, new String[] {"echopet.pet.type.<rider_type>"}, "Changes the rider of your pet", "Each data value is separated by a comma", "Pet names can be entered using a semi-colon"),
    RIDER_NAME("name rider [name]", true, new String[] {"echopet.pet.name"}, "Set the name of your pet's rider", "If no arguments are provided, EchoPet will ask for one separately", "Names can be more than one word but no longer than 64 characters"),
    RIDER_REMOVE("rider remove", true, new String[] {"echopet.pet.remove"}, "Remove your pet's rider"),
    LIST("list", true, new String[] {"echopet.pet.list"}, "View available pet types"),
    INFO("info", true, new String[] {"echopet.pet.info"}, "View information on your pet"),
    DEFAULT_SET("default set <type>:[data],[data] [rider]:[data],[data]", false, new String[] {"echopet.pet.default.set.type.<type>", "echopet.pet.default.set.type.<rider>"}, "Set the default pet for when you log in."),
    DEFAULT_SET_CURRENT("default set current", true, new String[] {"echopet.pet.default.set.current"}, "Set your default pet to your active pet"),
    DEFAULT_REMOVE("default remove", true, new String[]{"echopet.pet.default.remove"}, "Remove your default pet"),
    RIDE("ride", false, new String[] {"echopet.pet.ride.<type>"}, "Ride your pet"),
    HAT("hat", false, new String[] {"echopet.pet.hat.<type>"}, "Place your pet on your head"),
    CALL("call", true, new String[] {"echopet.pet.call"}, "Call your pet"),
    MENU("menu", true, new String[] {"echopet.pet.menu"}, "Open the Data Menu GUI for your pet"),
    SHOW("show", true, new String[] {"echopet.pet.show"}, "Show your hidden pet", "Reloads your pet from the save files"),
    HIDE("hide", true, new String[] {"echopet.pet.hide"}, "Hides your pet"),
    TOGGLE("toggle", true, new String[] {"echopet.pet.toggle"}, "Toggles the hide status of your pet"),
    SELECT("select", true, new String[] {"echopet.pet.select"}, "Opens the Pet Selection GUI Menu"),
    SELECTOR("selector", true, new String[] {"echopet.pet.selector"}, "Gives you the Pet Selection GUI Menu item"),
    ;

    private String commandArguments;
    private boolean canCheckPerm;
    private String[] permission;
    private String[] description;

    HelpEntry(String commandArguments, boolean canCheckPerm, String permission[], String... description) {
        this.commandArguments = commandArguments;
        this.canCheckPerm = canCheckPerm;
        this.permission = permission;
        this.description = description;
    }

    public String getCommandArguments() {
        return commandArguments;
    }

    public String[] getPermissions() {
        return permission;
    }

    public String[] getDescription() {
        return description;
    }

    public FancyMessage getFancyMessage(CommandSender sender) {
        ArrayList<String> description = new ArrayList<String>();
        description.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Usage for /" + EchoPetPlugin.getInstance().cmdString + " " + this.getCommandArguments() + ":");
        for (String s : this.getDescription()) {
            description.add("• " + ChatColor.YELLOW + s);
        }
        if (sender != null && this.canCheckPerm) {
            boolean hasPerm = false;
            for (String perm : this.getPermissions()) {
                if (Perm.hasPerm(sender, perm, false, false)) {
                    hasPerm = true;
                    break;
                }
            }
            if (hasPerm) {
                description.add(hasPerm ? ChatColor.GREEN + "You may use this command" : ChatColor.RED + "You do not have permission to use this command");
            }
        }
        String cmd = "/" + EchoPetPlugin.getInstance().cmdString + " " + this.getCommandArguments();
        return new FancyMessage(ChatColor.YELLOW + "• " + ChatColor.GOLD + cmd).itemTooltip(FancyItemUtil.getItem(description.toArray(new String[description.size()]))).suggest(cmd);
    }

    public FancyMessage getFancyMessage() {
        return this.getFancyMessage(null);
    }
}