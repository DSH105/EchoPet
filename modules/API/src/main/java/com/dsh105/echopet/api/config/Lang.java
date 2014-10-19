/*
 * This file is part of Echopet.
 *
 * Echopet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Echopet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Echopet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.api.config;

import com.dsh105.commodus.config.Options;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.echopet.api.plugin.EchoPetCore;

public class Lang extends Options {

    public Lang(YAMLConfig config) {
        super(config);
    }

    @Override
    public void setDefaults() {
        for (LangSetting setting : Setting.getOptions(Lang.class, LangSetting.class)) {
            set(setting);
        }
    }

    public static final LangSetting
            PREFIX = new LangSetting("prefix", EchoPetCore.DEFAULT_PREFIX, "Plugin prefix appended to all messages."),

    UPDATE_NOT_AVAILABLE = new LangSetting("update_not_available", "{c1}An update is not available.", "Sent when an update is not available"),
            UPDATE_AVAILABLE = new LangSetting("update_available", "{c1}An update is available.\n{c1}Type \"{c2}/echopet update{c1}\" to update", "Sent when an update is not available"),
            PLUGIN_INFORMATION = new LangSetting("plugin_information", "{c1}Running EchoPet v{c2}%version%{c1}. Use {c2}/" + Settings.COMMAND.getValue() + " help {c1}for help."),
            PET_COMMAND = new LangSetting("pet_command", "{c1}Use {c2}/" + Settings.COMMAND.getValue() + " help {c1}for help."),
            CONFIGS_RELOADED = new LangSetting("configs_reloaded", "{c1}Configuration files reloaded."),
            COMMAND_HOVER_TIP = new LangSetting("commandhover_tip", "&e&oHover over to see more information about each command. Click to insert it into the chat window.", "Reminds players to hover over commands to see more information"),
            HOVER_TIP = new LangSetting("hover_tip", "&e&oHover over to see more information."),
            HELP_INDEX_TOO_BIG = new LangSetting("help_index_too_big", "{c1}Page {c2}%index% {c1}does not exist."),

    PETS_DISABLED_HERE = new LangSetting("pets_disabled_here", "{c1}pets are not allowed here.", "Sent when pets are disabled in a certain area"),
            PET_TYPE_DISABLED = new LangSetting("pet_type_disabled", "{c2}%type% {c1}pets are disabled."),
            FAILED_SPAWN = new LangSetting("failed_spawn", "{c1}Failed to spawn pet: an unexpected error was encountered."),
            RIDERS_DISABLED = new LangSetting("riders_disabled", "{c1}Riders are disabled for {c2}%type% {c1}pets."),
            ENTER_PET_DISABLED_REGION = new LangSetting("enter_pet_disabled_region", "{c1}This region disables pets. Yours has been hidden."),
            INVALID_PET_TYPE = new LangSetting("invalid_pet_type", "{c2}%type% {c1}is not a valid pet type. See \"{c2}/pet list{c1}\" {c1}for a list of pet types."),
            INVALID_PET_DATA = new LangSetting("invalid_pet_data", "{c1}Invalid data types entered: {c2}%data%"),
            NAME_NOT_ALLOWED = new LangSetting("name_not_allowed", "{c1}You are not permitted to name your pet {c2}%name%{c1}."),
            DATA_FORCED = new LangSetting("data_forced", "{c1}The following data types have been forced: {c2}%data%"),

    PET_LOADED = new LangSetting("pet_loaded", "{c2}%name% {c1}was retrieved from your last session.", "Sent when a pet is loaded."),
            PETS_LOADED = new LangSetting("pets_loaded", "{c2}%number% {c1}pets were retrieved from your last session.", "Sent when a pet is loaded."),
            PET_NOT_LOADED = new LangSetting("pet_not_loaded", "{c1}Pet loading cancelled by the server.", "Sent when pet loading is disabled."),
            PET_NOT_LOADED_UNEXPECTED = new LangSetting("pet_not_loaded_unexpected", "{c1}Pet loading unexpectedly cancelled.", "Sent when pet loading is disabled."),
            SPAWN_BLOCKED = new LangSetting("spawn_blocked", "{c1}Your pet was blocked from spawning by another plugin.", "Sent when a pet could not be safely spawned."),
            PET_NOT_FOUND = new LangSetting("pet_not_found", "{c1}Your pet, {c2}%name%{c1}, could not be found. Use \"{c2}/" + Settings.COMMAND.getValue() + " info{c1}\" to see your active pets."),
            NO_PETS_FOUND = new LangSetting("no_pets_found", "{c1}You don't have any pets."),
            NO_RIDER_FOUND = new LangSetting("no_rider_found", "{c1}Your pet, {c2}%name%{c1}, does not have a rider."),
            MORE_PETS_FOUND = new LangSetting("more_pets_found", "{c1}You have more than one pet active. Please specify a pet name using \"{c2}/" + Settings.COMMAND + " %command%{c1}\" Use \"{c2}/" + Settings.COMMAND + " info{c1} for pet names to reference."),
            PET_CREATED = new LangSetting("pet_created", "{c1}Your new {c2}%type% {c1}pet follows behind you."),
            RIDER_CREATED = new LangSetting("rider_created", "{c1}Your new {c2}%type% {c1}pet rides on {c2}%name%{c1}."),
            RIDER_REMOVED = new LangSetting("rider_removed", "The rider of your pet, {c2}%name% has been removed."),
            PET_REMOVED = new LangSetting("pet_removed", "Your pet, {c2}%name% has been removed."),
            PET_ALREADY_SITTING = new LangSetting("pet_already_sitting", "Your pet, {c2}%name%{c1}, is already sitting."),
            PET_ALREADY_NOT_SITTING = new LangSetting("pet_already_not_sitting", "Your pet, {c2}%name%{c1}, is not currently sitting."),
            PET_SITTING = new LangSetting("pet_sitting", "Your pet, {c2}%name%{c1}, is now sitting."),
            PET_NOT_SITTING = new LangSetting("pet_not_sitting", "Your pet, {c2}%name%{c1}, is no longer sitting."),
            NAME_PET = new LangSetting("name_pet", "{c1}Your pet, {c2}%name%{c1}, has been named &r%newname%{c1}."),
            PET_CALLED = new LangSetting("pet_called", "{c1}Your pet, {c2}%name%{c1}, has been called."),
            PET_HAT_ON = new LangSetting("pet_hat_on", "{c1}Your pet, {c2}%name%{c1}, has been placed on your head."),
            PET_HAT_OFF = new LangSetting("pet_hat_off", "{c1}Your pet, {c2}%name%{c1}, has been taken off your head."),
            PET_RIDE_ON = new LangSetting("pet_ride_on", "{c1}You are now riding your pet, {c2}%name%{c1}."),
            PET_RIDE_OFF = new LangSetting("pet_ride_off", "{c1}You are no longer riding your pet, {c2}%name%{c1}."),
            PET_HIDDEN = new LangSetting("pet_hidden", "{c1}Your pet, {c2}%name% {c1}, has been hidden."),
            PET_SHOWN = new LangSetting("pet_shown", "{c1}Your pet, {c2}%name% {c1}, has come out of hiding."),

    NAME_PET_PROMPT = new LangSetting("name_pet_prompt", "{c1}What would you like to name your pet?"),

    SELECTOR_ITEM_ADDED = new LangSetting("selector_item_added", "The Selector item has been added to your inventory."),

    ADMIN_NAME_PET_PROMPT = new LangSetting("admin_name_pet_prompt", "{c1}What would you like to name {c2}%player%{c1}'s pet?"),
            ADMIN_NAME_PET = new LangSetting("admin_name_pet", "{c2}%player%{c1}'s pet, {c2}%name%{c1}, has been named &r%newname%{c1}.");

    /*NO_PET = new LangSetting("no_pet", "{c1}You don't currently have a pet."),
            PET_CALL = new LangSetting("pet_call", "{c1}Your pet has been called to your side."),
            NO_HIDDEN_PET = new LangSetting("no_hidden_pet", "{c1}Your pet is not currently hidden."),
            SHOW_PET = new LangSetting("show_pet", "{c1}Your hidden pet magically reappears!"),
            HIDE_PET = new LangSetting("hide_pet", "{c1}Your pet has been hidden."),
            NO_RIDER = new LangSetting("no_rider", "{c1}Your pet does not have a rider."),
            PET_NAME_TOO_LONG = new LangSetting("name_length", "{c1}pet names cannot be longer than {c2}32 {c1}characters."),
            NAME_RIDER = new LangSetting("name_rider", "{c1}Your {c2}%type%{c1}'s rider has been named to &r%name%{c1}."),
            NAME_PET = new LangSetting("name_pet", "{c1}Your {c2}%type% {c1}has been named &r%name%{c1}."),
            NAME_PET_PROMPT = new LangSetting("name_pet_prompt", "{c1}What would you like to name your pet?"),
            NAME_NOT_ALLOWED = new LangSetting("name_not_allowed", "{c1}You are not permitted to name your pet {c2}%name%{c1}."),
            REMOVE_PET = new LangSetting("remove_pet", "{c1}Your pet has been removed."),
            REMOVE_PET_DEATH = new LangSetting("remove_pet_death", "{c1}Upon your death, your pet has despawned."),
            REMOVE_RIDER = new LangSetting("remove_rider", "{c1}Your pet's rider has been removed."),
            CREATE_PET = new LangSetting("create_pet", "{c1}A {c2}%type% {c1}now follows close behind you."),
            CREATE_PET_WITH_RIDER = new LangSetting("create_pet_with_rider", "{c1}A {c2}%type% {c1}now follows close behind you with a {c2}%mtype% {c1}rider."),
            CHANGE_RIDER = new LangSetting("change_rider", "{c1}Your pet's rider has been changed to a {c2}%type%{c1}."),
            RIDE_PET_ON = new LangSetting("ride_pet_on", "{c1}You are now riding your pet. Use {c2}WASD {c1}and the {c2}Space Bar {c1}to control it."),
            RIDE_PET_OFF = new LangSetting("ride_pet_off", "{c1}You are no longer riding your pet."),
            HAT_PET_ON = new LangSetting("hat_pet_on", "{c1}Your pet now rides on your head."),
            HAT_PET_OFF = new LangSetting("hat_pet_off", "{c1}Your pet no longer rides on your head."),
            OPEN_MENU = new LangSetting("open_menu", "{c1}Opening Data Menu for your {c2}%type%{c1}..."),
            OPEN_SELECTOR = new LangSetting("open_selector", "{c1}Opening pet Selector..."),
            ADD_SELECTOR = new LangSetting("add_selector", "{c1}The {c2}pet Selector {c1}has been added to your inventory."),

    DATA_FORCED = new LangSetting("data_forced", "{c1}The following data types have been forced: {c2}%data%"),

    RIDERS_DISABLED = new LangSetting("riders_disabled", "{c1}Riders are disabled for {c2}%type%"),
            DATA_TYPE_DISABLED = new LangSetting("data_type_disabled", "{c2}%data% {c1}data type is disabled."),
            PET_TYPE_DISABLED = new LangSetting("pet_type_disabled", "{c2}%type% {c1}pets are disabled."),
            HUMAN_PET_DISABLED = new LangSetting("human_pet_disabled", "{c2}Human pets {c1}are not compatible with this version of Minecraft."),
            INVALID_PET_TYPE = new LangSetting("invalid_pet_type", "{c2}%type% {c1}is not a valid pet type. See {c2}\"/pet list\" {c1}for a list of pet types."),
            INVALID_PET_DATA = new LangSetting("invalid_pet_data", "{c1}Invalid data types entered: {c2}%data%"),

    NO_DEFAULT = new LangSetting("no_default", "{c1}You do not currently have a default pet set."),
            REMOVE_DEFAULT = new LangSetting("remove_default", "{c1}Default pet removed successfully."),
            SET_DEFAULT = new LangSetting("set_default", "{c1}Your default pet has been set to {c2}%type%{c1}."),
            SET_DEFAULT_WITH_RIDER = new LangSetting("set_default_with_rider", "{c1}Your default pet has been set to {c2}%type%{c1} with a {c2}%mtype% {c1}rider."),
            SET_DEFAULT_TO_CURRENT = new LangSetting("set_default_current", "{c1}Your default pet has been set to your current pet"),

    ADMIN_CHANGE_RIDER = new LangSetting("admin_change_rider", "{c2}%player%{c1}'s pet's rider has been changed to a {c2}%type%{c1}."),
            ADMIN_SET_DEFAULT_WITH_RIDER = new LangSetting("admin_set_default_with_rider", "{c2}%player%{c1}'s default pet has been set to {c2}%type%{c1} with a {c2}%mtype% {c1}rider."),
            ADMIN_SET_DEFAULT = new LangSetting("admin_set_default", "{c2}%player%{c1}'s default pet has been set to {c2}%type%{c1}."),
            ADMIN_SET_DEFAULT_TO_CURRENT = new LangSetting("admin_set_default_current", "{c2}%player%{c1}'s default pet has been set to their current pet"),
            ADMIN_REMOVE_DEFAULT = new LangSetting("admin_remove_default", "{c2}%player%{c1}'s default pet removed successfully."),
            ADMIN_NO_DEFAULT = new LangSetting("admin_no_default", "{c2}%player% {c1}does not currently have a default pet set."),
            ADMIN_REMOVE_RIDER = new LangSetting("admin_remove_rider", "{c2}%player%{c1}'s pet's rider has been removed."),
            ADMIN_CREATE_PET_WITH_RIDER = new LangSetting("admin_create_pet_with_rider", "{c1}A {c2}%type% {c1}now follows close behind {c2}%player% {c1}with a {c2}%mtype% {c1}rider."),
            ADMIN_CREATE_PET = new LangSetting("admin_create_pet", "{c1}A {c2}%type% {c1}now follows close behind {c2}%player%{c1}."),
            ADMIN_RIDE_PET_ON = new LangSetting("admin_ride_pet_on", "{c2}%player% {c1}is now riding their pet."),
            ADMIN_RIDE_PET_OFF = new LangSetting("admin_ride_pet_off", "{c2}%player% {c1}is no longer riding their pet."),
            ADMIN_HAT_PET_ON = new LangSetting("admin_hat_pet_on", "{c2}%player%{c1}'s pet now rides on their head."),
            ADMIN_HAT_PET_OFF = new LangSetting("admin_hat_pet_off", "{c2}%player%{c1}'s pet no longer rides on their head."),
            ADMIN_NULL_PLAYER = new LangSetting("admin_null_player", "{c2}%player% {c1}is not online or does not exist."),
            ADMIN_NULL_PLAYER_DATA = new LangSetting("admin_null_player_data", "{c1}pet data for {c2}%player% {c1}does not exist."),
            ADMIN_PET_REMOVED = new LangSetting("admin_pet_removed", "{c2}%player%{c1}'s pet has been removed."),
            ADMIN_NAME_RIDER = new LangSetting("admin_name_rider", "{c2}%player%{c1}'s {c1}pet's rider has been named to &r%name%{c1}."),
            ADMIN_NAME_PET = new LangSetting("admin_name_pet", "{c2}%player%{c1}'s {c2}%type% {c1}has been named &r%name%{c1}."),
            ADMIN_NAME_PET_PROMPT = new LangSetting("admin_name_pet_prompt", "{c1}What would you like to name {c2}%player%{c1}'s pet?"),
            ADMIN_NO_RIDER = new LangSetting("admin_no_rider", "{c2}%player%{c1}'s {c1}pet does not have a rider."),
            ADMIN_NO_PET = new LangSetting("admin_has_no_pet", "{c2}%player% {c1}does not currently have a pet."),
            ADMIN_OPEN_MENU = new LangSetting("admin_open_menu", "{c1}Opening Data Menu for {c2}%player%{c1}'s {c2}%type%{c1}..."),
            ADMIN_PET_CALL = new LangSetting("admin_pet_call", "{c2}%player%{c1}'s pet has been called to their side."),
            ADMIN_NO_HIDDEN_PET = new LangSetting("admin_no_hidden_pet", "{c2}%player%{c1}'s pet is not currently hidden."),
            ADMIN_SHOW_PET = new LangSetting("admin_show_pet", "{c2}%player%{c1}'s hidden pet magically reappears!"),
            ADMIN_HIDE_PET = new LangSetting("admin_hide_pet", "{c2}%player%{c1}'s pet has been hidden."),
            ADMIN_ADD_SELECTOR = new LangSetting("admin_add_selector", "{c1}The {c2}pet Selector {c1}has been added to {c2}%player%{c1}'s inventory."),
            ADMIN_OPEN_SELECTOR = new LangSetting("admin_open_selector", "{c1}The {c2}pet Selector {c1}Menu has been opened for {c2}%player%."),
            ADMIN_CLOSE_SELECTOR = new LangSetting("admin_close_selector", "{c1}The {c2}pet Selector {c1}Menu has been closed for {c2}%player%."),
            ADMIN_RELOAD_CONFIG = new LangSetting("admin_reload_config", "{c1}Configuration File reloaded.");*/
    ;
}