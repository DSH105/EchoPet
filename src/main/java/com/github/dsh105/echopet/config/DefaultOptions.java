package com.github.dsh105.echopet.config;

import org.bukkit.entity.Player;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.data.PetData;
import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.util.StringUtil;


public class DefaultOptions {
	
	private EchoPet ec;
	
	public DefaultOptions(EchoPet ec) {
		this.ec = ec;
	}
	
	public boolean allowPetType(PetType petType) {
		return ec.getMainConfig().getBoolean("pets."
				+ petType.toString().toLowerCase().replace("_", " ") + ".enable", true);
	}
	
	public boolean allowMounts(PetType petType) {
		return ec.getMainConfig().getBoolean("pets."
				+ petType.toString().toLowerCase().replace("_", " ") + ".allow.mounts", true);
	}
	
	public boolean allowData(PetType type, PetData data) {
		return ec.getMainConfig().getBoolean("pets." + type.toString().toLowerCase().replace("_", " ")
				+ ".allow." + data.getConfigOptionString(), true);
	}
	
	public boolean forceData(PetType type, PetData data) {
		return ec.getMainConfig().getBoolean("pets." + type.toString().toLowerCase().replace("_", " ")
				+ ".force." + data.getConfigOptionString(), false);
	}
	
	public Object getConfigOption(String s) {
		return ec.getMainConfig().get(s);
	}
	
	public Object getConfigOption(String s, Object def) {
		return ec.getMainConfig().get(s, def);
	}
	
	public boolean autoLoadPets(Player p) {
		if (p.hasPermission("echopet.override")) return true;
		return ec.getMainConfig().getBoolean("autoLoadSavedPets", true);
	}
	
	public boolean shouldHaveEquipment(PetType petType) {
		if (petType == PetType.PIGZOMBIE || petType == PetType.SKELETON || petType == PetType.ZOMBIE) {
			return ec.getMainConfig().getBoolean("pets." + petType.toString().toLowerCase().replace("_", " ") + ".hasEquipment", true);
		}
		return false;
	}
	
	public String getCommandString() {
		return ec.getMainConfig().getString("commandString", "pet");
	}
	
	public float getRideSpeed() {
		return (float) ec.getMainConfig().getDouble("rideSpeed", 0.35D);
	}

	public double getRideJumpHeight() {
		return ec.getMainConfig().getDouble("rideJump", 0.5D);
	}
	
	public boolean useSql() {
		return ec.getMainConfig().getBoolean("sql.use", false);
	}
	
	public boolean sqlOverride() {
		if (useSql()) {
			return ec.getMainConfig().getBoolean("sql.overrideFile");
		}
		return false;
	}
	
	public void setDefaultValues(YAMLConfig config) {
		try {
			config.set("commandString", config.getString("commandString","pet"));
			
			config.set("autoUpdate", config.getBoolean("autoUpdate", false), "If set to true, EchoPet will automatically download and install", "new updates.");
			config.set("checkForUpdates", config.getBoolean("checkForUpdates", true), "If -autoUpdate- is set to false, EchoPet will notify certain", "players of new updates if they are available (if set to true).");
			
			config.set("sql.overrideFile", config.getBoolean("sql.overrideFile", true), "If true, Pets saved to a MySQL Database will override", "those saved to a file (Default and AutoSave Pets)");
			config.set("sql.timeout", config.getInt("sql.timeout", 30));
			config.set("sql.use", config.getBoolean("sql.use", false));
			config.set("sql.host", config.getString("sql.host", "localhost"));
			config.set("sql.port", config.getInt("sql.port", 3306));
			config.set("sql.database", config.getString("sql.database", "EchoPet"));
			config.set("sql.username", config.getString("sql.username", "none"));
			config.set("sql.password", config.getString("sql.password", "none"));
			
			config.set("debug", config.getBoolean("debug", false), "If true, EchoPet will print errors to the console. Useful for", "debugging certain aspects of the plugin.");
			config.set("autoSave", config.getBoolean("autoSave", true), "If true, EchoPet will autosave all pet data to prevent data", "loss in the event of a server crash.");
			config.set("autoSaveTimer", config.getInt("autoSaveTimer", 180), "Interval between autosave of pet data (in seconds).");
			config.set("requireDefaultPerm", config.getBoolean("requireDefaultPerm", false), "If true, players will need the -echopet.echopet-", "permission to perform commands.");
			config.set("petTagVisible", config.getBoolean("petTagVisible", true), "Pet name tags are always visible (true) or only when a", "player is looking at them (false)");
			config.set("petMenuOnInteract", config.getBoolean("petMenuOnInteract", true), "Boolean value defining whether to allow", "the PetMenu to be opened by interaction (right click)");
			config.set("startWalkDistance", config.getInt("startWalkDistance", 12), "Distance away from the player before their starts walking.");
			config.set("stopWalkDistance", config.getInt("stopWalkDistance", 8), "Distance away from the player before their stops walking.");
			config.set("teleportDistance", config.getInt("teleportDistance", 30), "Distance away from the player before their pet teleports.");
			config.set("flyTeleport", config.getBoolean("flyTeleport", false), "If set to true, when the player is flying, their pet will", "continually teleport towards them.");
			config.set("canAttackPlayers", config.get("canAttackPlayers", false), "If true, Pets can damage players.");
			config.set("autoLoadSavedPets", config.getBoolean("autoLoadSavedPets", true), "Auto-load pets from last session", "If set to false, pets will still be loaded for players", "with the -echopet.override- permission");
			config.set("autoLoadDefaultPets", config.getBoolean("autoLoadDefaultPets", true), "If false, default pets will not be loaded for players");
			config.set("rideSpeed", config.getDouble("rideSpeed", 0.35D), "The speed the pet travels when ridden by players.");
			config.set("rideJump", config.getDouble("rideJump", 0.5D), "Jump height for ridden pets.");
			config.set("sendForceMessage", config.getBoolean("sendForceMessage", true), "For all values forced (below), EchoPet will notify the player", "(if set to true).");
			for (PetType petType : PetType.values()) {
				config.set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".enable", config.getBoolean("pets." + petType.toString().toLowerCase().replace("_", " ") + ".enable", true));
				config.set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".defaultName", config.getString("pets." + petType.toString().toLowerCase().replace("_", " ") + ".defaultName", petType.getDefaultName()));
				config.set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".attackDamage", config.getDouble("pets." + petType.toString().toLowerCase().replace("_", " ") + ".attackDamage", petType.getAttackDamage()));
				config.set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".allow.mounts", config.getBoolean("pets." + petType.toString().toLowerCase().replace("_", " ") + ".allow.mounts", true));
				for (PetData dataType : PetData.values()) {
					if (petType.isDataAllowed(dataType)) {
						config.set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".allow."
							+ dataType.getConfigOptionString(),
							config.get("pets." + petType.toString().toLowerCase().replace("_", " ") + ".allow."
							+ dataType.getConfigOptionString(), true));
						config.set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".force."
								+ dataType.getConfigOptionString(),
								config.get("pets." + petType.toString().toLowerCase().replace("_", " ") + ".force."
										+ dataType.getConfigOptionString(), false));
					}
				}
				if (petType == PetType.PIGZOMBIE || petType == PetType.SKELETON || petType == PetType.ZOMBIE) {
					config.set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".hasEquipment",
							config.get("pets." + petType.toString().toLowerCase().replace("_", " ") + ".hasEquipment", true));
				}
			}
			config.saveConfig();
		} catch (Exception e) {ec.debug(e, "Failed to generate default Configuration File.");}
	}

	/*public void addConfigComments(YAMLConfig config) {
		for (String key : config.getKeys()) {
			if (config.get(key) != null) {
				Object value = config.get(key);
				String[] comments = ConfigComments.getCommentsFor(key);
				if (comments != null) {
					config.set(key, value, comments);
				}
				else {
					config.set(key, value);
				}
			}
			for (String key2 : config.getConfigurationSection(key).getKeys(true)) {
				if (config.get(key + "." + key2) != null) {
					Object value = config.get(key);
					String[] comments = ConfigComments.getCommentsFor(key);
					if (comments != null) {
						config.set(key, value, comments);
					}
					else {
						config.set(key, value);
					}
				}
			}
		}
	}*/
}