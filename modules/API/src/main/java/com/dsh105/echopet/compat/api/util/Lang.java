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

import com.dsh105.echopet.compat.api.event.EchoPetSendMessageEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Lang {

    PREFIX("prefix", "&4[&cEchoPet&4]&r "),

    HELP("help", "&eUse &6/%cmd% &efor help."),
    NO_PERMISSION("no_permission", "&6%perm% &epermission needed to do that."),
    PETS_DISABLED_HERE("pets_disabled_here", "&ePets are not allowed here."),
    ENTER_PET_DISABLED_REGION("enter_pet_disabled_region", "&eEntering region that does not allow Pets. Yours has been hidden."),
    ADMIN_COMMAND_ERROR("admin_cmd_error", "&eError for input string: &6%cmd%&e. Use /" + EchoPet.getPlugin().getAdminCommandString() + " for help"),
    COMMAND_ERROR("cmd_error", "&eError for input string: &6%cmd%&e. Use /" + EchoPet.getPlugin().getCommandString() + " for help."),
    IN_GAME_ONLY("in_game_only", "&6%cmd% &ecan only be used in-game."),
    STRING_ERROR("string_error", "&eError parsing String: [&6%string%&e]. Please revise command arguments."),
    HELP_INDEX_TOO_BIG("help_index_too_big", "&3Page &b%index% &3does not exist."),
    TIP_HOVER_PREVIEW("hover_tip", "&e&oHover over to see more information about this command. Click to insert it into the chat window."),

    DIMENSION_CHANGE("dimension_change", "&eDimension change initiated. Warping space and time to retrieve your Pet..."),
    AUTOSAVE_PET_LOAD("autosave_pet_load", "&eYour last active pet (&6%petname%&e) now follows close behind you."),
    DEFAULT_PET_LOAD("default_pet_load", "&eYour default pet &e(&6%petname%&e) now follows close behind you."),
    DATABASE_PET_LOAD("sql_pet_load", "&eYour Saved Pet (&6%petname%&e) now follows close behind you."),

    NO_PET("no_pet", "&eYou don't currently have a pet."),
    PET_CALL("pet_call", "&eYour Pet has been called to your side."),
    NO_HIDDEN_PET("no_hidden_pet", "&eYour Pet is not currently hidden."),
    SHOW_PET("show_pet", "&eYour hidden Pet magically reappears!"),
    HIDE_PET("hide_pet", "&eYour Pet has been hidden."),
    NO_RIDER("no_rider", "&eYour pet does not have a rider."),
    PET_NAME_TOO_LONG("name_length", "&ePet names cannot be longer than &632 &echaracters."),
    NAME_RIDER("name_rider", "&eYour &6%type%&e's rider has been named to &r%name%&e."),
    NAME_PET("name_pet", "&eYour &6%type% &ehas been named &r%name%&e."),
    NAME_PET_PROMPT("name_pet_prompt", "&eWhat would you like to name your pet?"),
    NAME_NOT_ALLOWED("name_not_allowed", "&eYou are not permitted to name your pet &6%name%&e."),
    REMOVE_PET("remove_pet", "&eYour pet has been removed."),
    REMOVE_PET_DEATH("remove_pet_death", "&eUpon your death, your pet has despawned."),
    REMOVE_RIDER("remove_rider", "&eYour pet's rider has been removed."),
    CREATE_PET("create_pet", "&eA &6%type% &enow follows close behind you."),
    CREATE_PET_WITH_RIDER("create_pet_with_rider", "&eA &6%type% &enow follows close behind you with a &6%mtype% &erider."),
    CHANGE_RIDER("change_rider", "&eYour pet's rider has been changed to a &6%type%&e."),
    RIDE_PET_ON("ride_pet_on", "&eYou are now riding your pet. Use &6WASD &eand the &6Space Bar &eto control it."),
    RIDE_PET_OFF("ride_pet_off", "&eYou are no longer riding your pet."),
    HAT_PET_ON("hat_pet_on", "&eYour pet now rides on your head."),
    HAT_PET_OFF("hat_pet_off", "&eYour pet no longer rides on your head."),
    OPEN_MENU("open_menu", "&eOpening Data Menu for your &6%type%&e..."),
    OPEN_SELECTOR("open_selector", "&eOpening Pet Selector..."),
    ADD_SELECTOR("add_selector", "&eThe &6Pet Selector &ehas been added to your inventory."),

    DATA_FORCE_MESSAGE("data_force_message", "&eThe following data types have been forced by the server: &6%data%"),

    RIDERS_DISABLED("riders_disabled", "&eRiders are disabled for &6%type%"),
    DATA_TYPE_DISABLED("data_type_disabled", "&6%data% &edata type is disabled."),
    PET_TYPE_DISABLED("pet_type_disabled", "&6%type% &epet type is disabled."),
    HUMAN_PET_DISABLED("human_pet_disabled", "&6Human Pets are not compatible with this version of Minecraft."),
    INVALID_PET_TYPE("invalid_pet_type", "&6%type% &eis an invalid pet type."),
    INVALID_PET_DATA_TYPE("invalid_pet_data_type", "&6%data% &eis an invalid pet data type."),
    INVALID_PET_DATA_TYPE_FOR_PET("invalid_pet_data_type_for_pet", "&6%data% &e is invalid for the &6%type% &epet type."),

    NO_DEFAULT("no_default", "&eYou do not currently have a default pet set."),
    REMOVE_DEFAULT("remove_default", "&eDefault pet removed successfully."),
    SET_DEFAULT("set_default", "&eYour default pet has been set to &6%type%&e."),
    SET_DEFAULT_WITH_RIDER("set_default_with_rider", "&eYour default pet has been set to &6%type%&e with a &6%mtype% &erider."),
    SET_DEFAULT_TO_CURRENT("set_default_current", "&eYour default pet has been set to your current pet"),

    ADMIN_CHANGE_RIDER("admin_change_rider", "&6%player%&e's pet's rider has been changed to a &6%type%&e."),
    ADMIN_SET_DEFAULT_WITH_RIDER("admin_set_default_with_rider", "&6%player%&e's default pet has been set to &6%type%&e with a &6%mtype% &erider."),
    ADMIN_SET_DEFAULT("admin_set_default", "&6%player%&e's default pet has been set to &6%type%&e."),
    ADMIN_SET_DEFAULT_TO_CURRENT("admin_set_default_current", "&6%player%&e's default pet has been set to their current pet"),
    ADMIN_REMOVE_DEFAULT("admin_remove_default", "&6%player%&e's default pet removed successfully."),
    ADMIN_NO_DEFAULT("admin_no_default", "&6%player% &edoes not currently have a default pet set."),
    ADMIN_REMOVE_RIDER("admin_remove_rider", "&6%player%&e's pet's rider has been removed."),
    ADMIN_CREATE_PET_WITH_RIDER("admin_create_pet_with_rider", "&eA &6%type% &enow follows close behind &6%player% &ewith a &6%mtype% &erider."),
    ADMIN_CREATE_PET("admin_create_pet", "&eA &6%type% &enow follows close behind &6%player%&e."),
    ADMIN_RIDE_PET_ON("admin_ride_pet_on", "&6%player% &eis now riding their pet."),
    ADMIN_RIDE_PET_OFF("admin_ride_pet_off", "&6%player% &eis no longer riding their pet."),
    ADMIN_HAT_PET_ON("admin_hat_pet_on", "&6%player%&e's pet now rides on their head."),
    ADMIN_HAT_PET_OFF("admin_hat_pet_off", "&6%player%&e's pet no longer rides on their head."),
    ADMIN_NULL_PLAYER("admin_null_player", "&6%player% &eis not online or does not exist."),
    ADMIN_NULL_PLAYER_DATA("admin_null_player_data", "&ePet data for &6%player% &edoes not exist."),
    ADMIN_PET_REMOVED("admin_pet_removed", "&6%player%&e's Pet has been removed."),
    ADMIN_NAME_RIDER("admin_name_rider", "&6%player%&e's &ePet's rider has been named to &r%name%&e."),
    ADMIN_NAME_PET("admin_name_pet", "&6%player%&e's &6%type% &ehas been named &r%name%&e."),
    ADMIN_NAME_PET_PROMPT("admin_name_pet_prompt", "&eWhat would you like to name &6%player%&e's pet?"),
    ADMIN_NO_RIDER("admin_no_rider", "&6%player%&e's &ePet does not have a rider."),
    ADMIN_NO_PET("admin_has_no_pet", "&6%player% &edoes not currently have a Pet."),
    ADMIN_OPEN_MENU("admin_open_menu", "&eOpening Data Menu for &6%player%&e's &6%type%&e..."),
    ADMIN_PET_CALL("admin_pet_call", "&6%player%&e's Pet has been called to their side."),
    ADMIN_NO_HIDDEN_PET("admin_no_hidden_pet", "&6%player%&e's Pet is not currently hidden."),
    ADMIN_SHOW_PET("admin_show_pet", "&6%player%&e's hidden Pet magically reappears!"),
    ADMIN_HIDE_PET("admin_hide_pet", "&6%player%&e's Pet has been hidden."),
    ADMIN_ADD_SELECTOR("admin_add_selector", "&eThe &6Pet Selector &ehas been added to &6%player%&e's inventory."),
    ADMIN_OPEN_SELECTOR("admin_open_selector", "&eThe &6Pet Selector &eMenu has been opened for &6%player%."),
    ADMIN_CLOSE_SELECTOR("admin_close_selector", "&eThe &6Pet Selector &eMenu has been closed for &6%player%."),
    ADMIN_RELOAD_CONFIG("admin_reload_config", "&eConfiguration File reloaded.");

    private String path;
    private String def;
    private String[] desc;

    Lang(String path, String def, String... desc) {
        this.path = path;
        this.def = def;
        this.desc = desc;
    }

    public String[] getDescription() {
        return this.desc;
    }

    public String getPath() {
        return this.path;
    }

    public static void sendTo(CommandSender sender, String message) {
        EchoPetSendMessageEvent event = new EchoPetSendMessageEvent(message, sender);
        EchoPet.getPlugin().getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            String msg = event.getMessageToSend();
            if (msg != null && !msg.equalsIgnoreCase("") && !msg.equalsIgnoreCase(" ") && !msg.equalsIgnoreCase("none")) {
                event.getRecipient().sendMessage(EchoPet.getPrefix() + msg);
            }
        }
    }

    public static void sendTo(Player p, String message) {
        EchoPetSendMessageEvent event = new EchoPetSendMessageEvent(message, p);
        EchoPet.getPlugin().getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            String msg = event.getMessageToSend();
            if (msg != null && !msg.equalsIgnoreCase("") && !msg.equalsIgnoreCase(" ") && !(msg.equalsIgnoreCase("none"))) {
                event.getRecipient().sendMessage(EchoPet.getPrefix() + msg);
            }
        }
    }

    @Override
    public String toString() {
        String result = EchoPet.getPlugin().getLangConfig().getString(this.path, this.def);
        if (result != null && result != "" && result != "none") {
            return ChatColor.translateAlternateColorCodes('&', EchoPet.getPlugin().getLangConfig().getString(this.path, this.def));
        } else {
            return "";
        }
    }

    public String toString_() {
        return EchoPet.getPlugin().getLangConfig().getString(this.path, this.def);
    }
}
