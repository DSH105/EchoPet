package com.github.dsh105.echopet.listeners;

import com.github.dsh105.echopet.data.PetHandler;
import com.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.vanish.event.VanishStatusChangeEvent;

/**
 * Project by DSH105
 */

public class VanishListener implements Listener {

	@EventHandler
	public void onVanish(VanishStatusChangeEvent event) {
		Player p = event.getPlayer();
		Pet pet = PetHandler.getInstance().getPet(p);
		if (pet != null) {
			pet.getEntityPet().vnp = event.isVanishing();
			pet.getEntityPet().setInvisible(event.isVanishing());
		}
	}
}