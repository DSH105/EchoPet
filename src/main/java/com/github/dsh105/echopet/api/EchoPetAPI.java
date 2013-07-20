package com.github.dsh105.echopet.api;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.data.PetData;
import com.github.dsh105.echopet.util.Lang;
import com.github.dsh105.echopet.util.StringUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.entity.pet.Pet;

public class EchoPetAPI {

	/**
	 * Gives a Pet to the specified {@link Player}
	 * <p>
	 * Pets will be spawned immediately next to the target player, linked until it is removed.
	 *
	 * @param player the {@link Player} that will be provided with a Pet
	 * @param petType the {@link PetType} (type of Pet) that will be given to the player
	 * @param sendMessage defines if the plugin sends a message to the target {@link Player}
	 * @return the Pet created
	 */
	public Pet givePet(Player player, PetType petType, boolean sendMessage) {
		if (player != null && petType != null) {
			Pet pet = EchoPet.getPluginInstance().PH.createPet(player, petType);
			if (sendMessage) {
				player.sendMessage(Lang.CREATE_PET.toString().replace("%type%", StringUtil.capitalise(petType.toString())));
			}
			return pet;
		}
		return null;
	}

	/**
	 * Removes a Pet if the {@link Player} has one active
	 *
	 * @param player the {@link Player} to remove their Pet from
	 * @param sendMessage defines if the plugin sends a message to the target {@link Player}
	 */
	public void removePet(Player player, boolean sendMessage) {
		EchoPet.getPluginInstance().PH.removePets(player);
	}

	/**
	 * Checks if a {@link Player} has a Pet
	 *
	 * @param player the {@link Player} used to check for Pet
	 * @return true if {@link Player} has a Pet, false if not
	 */
	public boolean hasPet(Player player) {
		return EchoPet.getPluginInstance().PH.getPet(player) != null;
	}

	/**
	 * Gets a {@link Player}'s Pet
	 *
	 * @param player the {@link Player} to get the Pet of
	 * @return the {@link Pet} instance linked to the {@link Player}
	 */
	public Pet getPet(Player player) {
		return EchoPet.getPluginInstance().PH.getPet(player);
	}

	/**
	 * Teleports a {@link Pet} to a {@link Location}
	 *
	 * @param pet the {@link Pet} to be teleported
	 * @param location the {@link Location} to teleport the {@link Pet} to
	 * @return success of teleportation
	 */
	public boolean teleportPet(Pet pet, Location location) {
		if (pet.isPetHat() || pet.isOwnerRiding()) {
			return false;
		}
		return pet.getCraftPet().teleport(location);
	}

	/**
	 * Adds {@link PetData} to a {@link Pet}
	 *
	 * @param pet the {@link Pet} to add the data to
	 * @param petData {@link PetData} to add to the {@link Pet}
	 */
	public void addData(Pet pet, PetData petData) {
		EchoPet.getPluginInstance().PH.setData(pet, new PetData[] {petData}, true);
	}

	/**
	 * Removes {@link PetData} from a {@link Pet}
	 *
	 * @param pet the {@link Pet} to remove the data from
	 * @param petData {@link PetData} to remove to the {@link Pet}
	 */
	public void removeData(Pet pet, PetData petData) {
		EchoPet.getPluginInstance().PH.setData(pet, new PetData[] {petData}, false);
	}

	/**
	 * Checks if a {@link Pet} has specific {@link PetData}
	 *
	 * @param pet the {@link Pet} to search
	 * @param petData the {@link PetData} searched for in the {@link Pet} instance
	 * @return true if the {@link Pet} has the specified {@link PetData}
	 */
	public boolean hasData(Pet pet, PetData petData) {
		return pet.getAllData(true).contains(petData);
	}
}