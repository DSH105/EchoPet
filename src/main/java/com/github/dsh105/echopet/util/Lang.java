package com.github.dsh105.echopet.util;


import org.bukkit.ChatColor;

import com.github.dsh105.echopet.EchoPet;

public enum Lang {
	
	COMMAND_ERROR("cmd_error", "&eError for input string: &6%cmd%&e. Use /pet for help."),
	IN_GAME_ONLY("in_game_only", "&6%cmd% &ecan only be used in-game."),
	
	DIMENSION_CHANGE("dimension_change", "&eYour pet cannot co-exist in two dimensions. It has been removed and will remain in the previous world."),
	AUTOSAVE_PET_LOAD("autosave_pet_load", "&eYour last active pet (&6%petname%&e) from this World (&6%world%&e) now follows close behind you."),
	DEFAULT_PET_LOAD("default_pet_load", "&eYour default pet &e(&6%petname%&e) now follows close behind you."),
	
	NO_PET("no_pet", "&eYou don't currently have a pet."),
	NO_MOUNT("no_mount", "&eYour pet does not have a mount."),
	PET_NAME_TOO_LONG("name_length", "&ePet names cannot be longer than &632 &echaracters."),
	NAME_MOUNT("name_mount", "&eYour &6%type%&e's mount has been named to &r%name%&e."),
	NAME_PET("name_pet", "&eYour &6%type% &ehas been named &r%name%&e."),
	REMOVE_PET("remove_pet", "&eYour pet has been removed."),
	REMOVE_PET_DEATH("remove_pet_death", "&eUpon your death, your pet has despawned."),
	REMOVE_MOUNT("remove_mount", "&eYour pet's mount has been removed."),
	CREATE_PET("create_pet", "&eA &6%type% &enow follows close behind you."),
	CREATE_PET_WITH_MOUNT("create_pet_with_mount", "&eA &6%type% &enow follows close behind you with a &6%mtype% &emount."),
	CHANGE_MOUNT("change_mount", "&eYour pet's mount has been changed to a &6%type%&e."),
	RIDE_PET_ON("ride_pet_on", "&eYou are now riding your pet. Use &6WASD &eand the &6Space Bar &eto control it."),
	RIDE_PET_OFF("ride_pet_off", "&eYou are no longer riding your pet."),
	HAT_PET_ON("hat_pet_on", "&eYour pet now rides on your head."),
	HAT_PET_OFF("hat_pet_off", "&eYour pet no longer rides on your head."),
	OPEN_MENU("open_menu", "&eOpening Data Menu for your &6%type%&e..."),
	
	DATA_FORCE_MESSAGE("data_force_message", "&eThe following data types have been forced by the server: &6%data%"),
	
	MOUNTS_DISABLED("mounts_disabled", "&eMounts are disabled for &6%type%"),
	DATA_TYPE_DISABLED("data_type_disabled", "&6%data% &edata type is disabled."),
	PET_TYPE_DISABLED("pet_type_disabled", "%6%type% &epet type is disabled."),
	INVALID_PET_TYPE("invalid_pet_type", "&6%type% &eis an invalid pet type."),
	INVALID_PET_DATA_TYPE("invalid_pet_data_type", "&6%data% &eis an invalid pet data type."),
	INVALID_PET_DATA_TYPE_FOR_PET("invalid_pet_data_type_for_pet", "&6%data% &e is invalid for the &6%type% &epet type."),
	
	NO_DEFAULT("no_default", "&eYou do not currently have a default pet set."),
	REMOVE_DEFAULT("remove_default", "&eDefault pet removed successfully."),
	SET_DEFAULT("set_default", "&eYour default pet has been set to &6%type%&e."),
	SET_DEFAULT_WITH_MOUNT("set_default_with_mount", "&eYour default pet has been set to &6%type%&e with a &6%mtype% &emount."),
	SET_DEFAULT_TO_CURRENT("set_default_current", "&eYour default pet has been set to your current pet");
	
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
	
	@Override
	public String toString() {
		return EchoPet.getPluginInstance().prefix + ChatColor.translateAlternateColorCodes('&',
				EchoPet.getPluginInstance().getLangConfig().getString(this.path, this.def));
	}
	
	public String toString_() {
		return EchoPet.getPluginInstance().getLangConfig().getString(this.path, this.def);
	}
}
