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

package com.dsh105.echopet.compat.api.config;

import com.dsh105.commodus.config.Options;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.echopet.compat.api.entity.AttributeAccessor;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;

public class Lang extends Options {

    public Lang(YAMLConfig config) {
        super(config);
    }

    @Override
    public void setDefaults() {
        for (LangSetting setting : Setting.getOptions(Lang.class, LangSetting.class)) {
            set(setting.getPath(), setting.getDefaultValue(), setting.getComments());
        }
    }

    public static final LangSetting PREFIX = new LangSetting("prefix", "&4[&cEchoPet&4]&r ", "Plugin prefix appended to all messages.");

    /*
     * General help messages
     */
    public static final LangSetting HELP = new LangSetting("help", "&eUse &6/%cmd% &efor help.", "Command help message");

    public static final LangSetting NO_PERMISSION = new LangSetting("no_permission", "&6%perm% &epermission needed to do that.", "No permission message");
    public static final LangSetting ADMIN_COMMAND_ERROR = new LangSetting("invalid_command", "&eInvalid command: &6%cmd%&e. Use /" + Settings.COMMAND.getValue() + " for help");
    public static final LangSetting COMMAND_ERROR = new LangSetting("admin_invalid_command", "&eInvalid command: &6%cmd%&e. Use /" + Settings.COMMAND.getValue() + "admin for help");
    public static final LangSetting PETS_DISABLED_HERE = new LangSetting("pets_disabled_here", "&ePets are not allowed here.", "Sent when pets are disabled in a certain area");
    public static final LangSetting UPDATE_NOT_AVAILABLE = new LangSetting("update_not_available", "&eAn update is not available.");

    public static final LangSetting ENTER_PET_DISABLED_REGION = new LangSetting("enter_pet_disabled_region", "&eEntering region that does not allow Pets. Yours has been hidden.");
    public static final LangSetting IN_GAME_ONLY = new LangSetting("in_game_only", "&6%cmd% &ecan only be used in-game.");
    public static final LangSetting STRING_ERROR = new LangSetting("string_error", "&eError parsing String: [&6%string%&e]. Please revise command arguments.");
    public static final LangSetting HELP_INDEX_TOO_BIG = new LangSetting("help_index_too_big", "&3Page &b%index% &3does not exist.");
    public static final LangSetting TIP_HOVER_PREVIEW = new LangSetting("hover_tip", "&e&oHover over to see more information about this command. Click to insert it into the chat window.");
    public static final LangSetting DIMENSION_CHANGE = new LangSetting("dimension_change", "&eDimension change initiated. Warping space and time to retrieve your Pet...");

    public static final LangSetting AUTOSAVE_PET_LOAD = new LangSetting("autosave_pet_load", "&eYour last active pet (&6%petname%&e) now follows close behind you.");
    public static final LangSetting DEFAULT_PET_LOAD = new LangSetting("default_pet_load", "&eYour default pet &e(&6%petname%&e) now follows close behind you.");
    public static final LangSetting DATABASE_PET_LOAD = new LangSetting("sql_pet_load", "&eYour Saved Pet (&6%petname%&e) now follows close behind you.");
    public static final LangSetting PET_SPAWN_BLOCKED = new LangSetting("pet_spawn_blocked", "&eYour pet was blocked from spawning externally.");

    public static final LangSetting NO_PET = new LangSetting("no_pet", "&eYou don't currently have a pet.");
    public static final LangSetting PET_CALL = new LangSetting("pet_call", "&eYour Pet has been called to your side.");
    public static final LangSetting NO_HIDDEN_PET = new LangSetting("no_hidden_pet", "&eYour Pet is not currently hidden.");
    public static final LangSetting SHOW_PET = new LangSetting("show_pet", "&eYour hidden Pet magically reappears!");
    public static final LangSetting HIDE_PET = new LangSetting("hide_pet", "&eYour Pet has been hidden.");
    public static final LangSetting NO_RIDER = new LangSetting("no_rider", "&eYour pet does not have a rider.");
    public static final LangSetting PET_NAME_TOO_LONG = new LangSetting("name_length", "&ePet names cannot be longer than &632 &echaracters.");
    public static final LangSetting NAME_RIDER = new LangSetting("name_rider", "&eYour &6%type%&e's rider has been named to &r%name%&e.");
    public static final LangSetting NAME_PET = new LangSetting("name_pet", "&eYour &6%type% &ehas been named &r%name%&e.");
    public static final LangSetting NAME_PET_PROMPT = new LangSetting("name_pet_prompt", "&eWhat would you like to name your pet?");
    public static final LangSetting NAME_NOT_ALLOWED = new LangSetting("name_not_allowed", "&eYou are not permitted to name your pet &6%name%&e.");
    public static final LangSetting REMOVE_PET = new LangSetting("remove_pet", "&eYour pet has been removed.");
    public static final LangSetting REMOVE_PET_DEATH = new LangSetting("remove_pet_death", "&eUpon your death, your pet has despawned.");
    public static final LangSetting REMOVE_RIDER = new LangSetting("remove_rider", "&eYour pet's rider has been removed.");
    public static final LangSetting CREATE_PET = new LangSetting("create_pet", "&eA &6%type% &enow follows close behind you.");
    public static final LangSetting CREATE_PET_WITH_RIDER = new LangSetting("create_pet_with_rider", "&eA &6%type% &enow follows close behind you with a &6%mtype% &erider.");
    public static final LangSetting CHANGE_RIDER = new LangSetting("change_rider", "&eYour pet's rider has been changed to a &6%type%&e.");
    public static final LangSetting RIDE_PET_ON = new LangSetting("ride_pet_on", "&eYou are now riding your pet. Use &6WASD &eand the &6Space Bar &eto control it.");
    public static final LangSetting RIDE_PET_OFF = new LangSetting("ride_pet_off", "&eYou are no longer riding your pet.");
    public static final LangSetting HAT_PET_ON = new LangSetting("hat_pet_on", "&eYour pet now rides on your head.");
    public static final LangSetting HAT_PET_OFF = new LangSetting("hat_pet_off", "&eYour pet no longer rides on your head.");
    public static final LangSetting OPEN_MENU = new LangSetting("open_menu", "&eOpening Data Menu for your &6%type%&e...");
    public static final LangSetting OPEN_SELECTOR = new LangSetting("open_selector", "&eOpening Pet Selector...");
    public static final LangSetting ADD_SELECTOR = new LangSetting("add_selector", "&eThe &6Pet Selector &ehas been added to your inventory.");

    public static final LangSetting DATA_FORCE_MESSAGE = new LangSetting("data_force_message", "&eThe following data types have been forced by the server: &6%data%");

    public static final LangSetting RIDERS_DISABLED = new LangSetting("riders_disabled", "&eRiders are disabled for &6%type%");
    public static final LangSetting DATA_TYPE_DISABLED = new LangSetting("data_type_disabled", "&6%data% &edata type is disabled.");
    public static final LangSetting PET_TYPE_DISABLED = new LangSetting("pet_type_disabled", "&6%type% &epet type is disabled.");
    public static final LangSetting HUMAN_PET_DISABLED = new LangSetting("human_pet_disabled", "&6Human Pets &eare not compatible with this version of Minecraft.");
    public static final LangSetting INVALID_PET_TYPE = new LangSetting("invalid_pet_type", "&6%type% &eis an invalid pet type.");
    public static final LangSetting INVALID_PET_DATA_TYPE = new LangSetting("invalid_pet_data_type", "&6%data% &eis an invalid pet data type.");
    public static final LangSetting INVALID_PET_DATA_TYPE_FOR_PET = new LangSetting("invalid_pet_data_type_for_pet", "&6%data% &e is invalid for the &6%type% &epet type.");

    public static final LangSetting NO_DEFAULT = new LangSetting("no_default", "&eYou do not currently have a default pet set.");
    public static final LangSetting REMOVE_DEFAULT = new LangSetting("remove_default", "&eDefault pet removed successfully.");
    public static final LangSetting SET_DEFAULT = new LangSetting("set_default", "&eYour default pet has been set to &6%type%&e.");
    public static final LangSetting SET_DEFAULT_WITH_RIDER = new LangSetting("set_default_with_rider", "&eYour default pet has been set to &6%type%&e with a &6%mtype% &erider.");
    public static final LangSetting SET_DEFAULT_TO_CURRENT = new LangSetting("set_default_current", "&eYour default pet has been set to your current pet");

    public static final LangSetting ADMIN_CHANGE_RIDER = new LangSetting("admin_change_rider", "&6%player%&e's pet's rider has been changed to a &6%type%&e.");
    public static final LangSetting ADMIN_SET_DEFAULT_WITH_RIDER = new LangSetting("admin_set_default_with_rider", "&6%player%&e's default pet has been set to &6%type%&e with a &6%mtype% &erider.");
    public static final LangSetting ADMIN_SET_DEFAULT = new LangSetting("admin_set_default", "&6%player%&e's default pet has been set to &6%type%&e.");
    public static final LangSetting ADMIN_SET_DEFAULT_TO_CURRENT = new LangSetting("admin_set_default_current", "&6%player%&e's default pet has been set to their current pet");
    public static final LangSetting ADMIN_REMOVE_DEFAULT = new LangSetting("admin_remove_default", "&6%player%&e's default pet removed successfully.");
    public static final LangSetting ADMIN_NO_DEFAULT = new LangSetting("admin_no_default", "&6%player% &edoes not currently have a default pet set.");
    public static final LangSetting ADMIN_REMOVE_RIDER = new LangSetting("admin_remove_rider", "&6%player%&e's pet's rider has been removed.");
    public static final LangSetting ADMIN_CREATE_PET_WITH_RIDER = new LangSetting("admin_create_pet_with_rider", "&eA &6%type% &enow follows close behind &6%player% &ewith a &6%mtype% &erider.");
    public static final LangSetting ADMIN_CREATE_PET = new LangSetting("admin_create_pet", "&eA &6%type% &enow follows close behind &6%player%&e.");
    public static final LangSetting ADMIN_RIDE_PET_ON = new LangSetting("admin_ride_pet_on", "&6%player% &eis now riding their pet.");
    public static final LangSetting ADMIN_RIDE_PET_OFF = new LangSetting("admin_ride_pet_off", "&6%player% &eis no longer riding their pet.");
    public static final LangSetting ADMIN_HAT_PET_ON = new LangSetting("admin_hat_pet_on", "&6%player%&e's pet now rides on their head.");
    public static final LangSetting ADMIN_HAT_PET_OFF = new LangSetting("admin_hat_pet_off", "&6%player%&e's pet no longer rides on their head.");
    public static final LangSetting ADMIN_NULL_PLAYER = new LangSetting("admin_null_player", "&6%player% &eis not online or does not exist.");
    public static final LangSetting ADMIN_NULL_PLAYER_DATA = new LangSetting("admin_null_player_data", "&ePet data for &6%player% &edoes not exist.");
    public static final LangSetting ADMIN_PET_REMOVED = new LangSetting("admin_pet_removed", "&6%player%&e's Pet has been removed.");
    public static final LangSetting ADMIN_NAME_RIDER = new LangSetting("admin_name_rider", "&6%player%&e's &ePet's rider has been named to &r%name%&e.");
    public static final LangSetting ADMIN_NAME_PET = new LangSetting("admin_name_pet", "&6%player%&e's &6%type% &ehas been named &r%name%&e.");
    public static final LangSetting ADMIN_NAME_PET_PROMPT = new LangSetting("admin_name_pet_prompt", "&eWhat would you like to name &6%player%&e's pet?");
    public static final LangSetting ADMIN_NO_RIDER = new LangSetting("admin_no_rider", "&6%player%&e's &ePet does not have a rider.");
    public static final LangSetting ADMIN_NO_PET = new LangSetting("admin_has_no_pet", "&6%player% &edoes not currently have a Pet.");
    public static final LangSetting ADMIN_OPEN_MENU = new LangSetting("admin_open_menu", "&eOpening Data Menu for &6%player%&e's &6%type%&e...");
    public static final LangSetting ADMIN_PET_CALL = new LangSetting("admin_pet_call", "&6%player%&e's Pet has been called to their side.");
    public static final LangSetting ADMIN_NO_HIDDEN_PET = new LangSetting("admin_no_hidden_pet", "&6%player%&e's Pet is not currently hidden.");
    public static final LangSetting ADMIN_SHOW_PET = new LangSetting("admin_show_pet", "&6%player%&e's hidden Pet magically reappears!");
    public static final LangSetting ADMIN_HIDE_PET = new LangSetting("admin_hide_pet", "&6%player%&e's Pet has been hidden.");
    public static final LangSetting ADMIN_ADD_SELECTOR = new LangSetting("admin_add_selector", "&eThe &6Pet Selector &ehas been added to &6%player%&e's inventory.");
    public static final LangSetting ADMIN_OPEN_SELECTOR = new LangSetting("admin_open_selector", "&eThe &6Pet Selector &eMenu has been opened for &6%player%.");
    public static final LangSetting ADMIN_CLOSE_SELECTOR = new LangSetting("admin_close_selector", "&eThe &6Pet Selector &eMenu has been closed for &6%player%.");
    public static final LangSetting ADMIN_RELOAD_CONFIG = new LangSetting("admin_reload_config", "&eConfiguration File reloaded.");
}