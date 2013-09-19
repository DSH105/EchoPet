package com.github.dsh105.echopet.listeners;

import com.github.dsh105.echopet.api.event.PetInteractEvent;
import com.github.dsh105.echopet.data.PetHandler;
import com.github.dsh105.echopet.menu.selector.PetSelector;
import com.github.dsh105.echopet.menu.selector.SelectorItem;
import com.github.dsh105.echopet.mysql.SQLPetHandler;
import com.github.dsh105.echopet.util.StringUtil;
import com.github.dsh105.echopet.util.WorldUtil;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.entity.pet.CraftPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.util.Lang;

public class PetOwnerListener implements Listener {
	
	private EchoPet ec;
	
	public PetOwnerListener(EchoPet ec) {
		this.ec = ec;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack itemStack = event.getItem();
		if (itemStack != null && itemStack.isSimilar(SelectorItem.SELECTOR.getItem())) {
			PetSelector petSelector = new PetSelector(45, p);
			petSelector.open(false);
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		Entity e = event.getRightClicked();
		if (e instanceof CraftPet) {
			Pet pet = ((CraftPet) e).getPet();
			event.setCancelled(true);
			PetInteractEvent iEvent = new PetInteractEvent(pet, p, PetInteractEvent.Action.RIGHT_CLICK, false);
			EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(iEvent);
			if (!iEvent.isCancelled()) {
				pet.getEntityPet().a(((CraftPlayer) p).getHandle());
			}
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
				Pet pet = PetHandler.getInstance().getPet(p);
				if (pet != null && pet.isOwnerRiding()) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerTeleport(final PlayerTeleportEvent event) {
		final Player p = event.getPlayer();
		final Pet pi = ec.PH.getPet(p);
		if (pi != null) {
			if (pi.ownerIsMounting) return;
			if (event.getFrom().getWorld() == event.getTo().getWorld()) {
				PetHandler.getInstance().saveFileData("autosave", pi);
				SQLPetHandler.getInstance().saveToDatabase(pi, false);
				PetHandler.getInstance().removePet(pi);
				new BukkitRunnable() {

					@Override
					public void run() {
						PetHandler.getInstance().loadPets(p, false, false, false);
					}

				}.runTaskLater(EchoPet.getPluginInstance(), 20L);
			}
			else {
				if (!WorldUtil.allowPets(event.getTo().getWorld().getName())) {
					Lang.sendTo(p, Lang.WORLD_DISABLED.toString().replace("%world%", StringUtil.capitalise(event.getTo().getWorld().getName())));
					return;
				}
				PetHandler.getInstance().saveFileData("autosave", pi);
				SQLPetHandler.getInstance().saveToDatabase(pi, false);
				ec.PH.removePet(pi);
				Lang.sendTo(p, Lang.DIMENSION_CHANGE.toString());
				new BukkitRunnable() {

					@Override
					public void run() {
						PetHandler.getInstance().loadPets(p, false, false, false);
					}

				}.runTaskLater(EchoPet.getPluginInstance(), 20L);
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		Pet pi = ec.PH.getPet(p);
		if (pi != null) {
			//ec.PH.saveFileData("autosave", pi);
			ec.PH.saveFileData("autosave", pi);
			ec.SPH.saveToDatabase(pi, false);
			ec.PH.removePet(pi);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		Inventory inv = p.getInventory();
		if (ec.update && p.hasPermission("echopet.update")) {
			p.sendMessage(ec.prefix + ChatColor.GOLD + "An update is available: " + ec.name + " (" + ec.size + " bytes).");
			p.sendMessage(ec.prefix + ChatColor.GOLD + "Type /ecupdate to update.");
		}

		for (int j = 0; j <= 35; j++) {
			ItemStack item = inv.getItem(j);
			if (item != null && item.isSimilar(SelectorItem.SELECTOR.getItem())) {
				inv.clear(j);
			}
		}

		if ((Boolean) ec.DO.getConfigOption("petSelector.clearInvOnJoin", false)) {
			inv.clear();
		}
		if ((Boolean) ec.DO.getConfigOption("petSelector.giveOnJoin.enable", true)
				&& (((Boolean) ec.DO.getConfigOption("petSelector.giveOnJoin.usePerm", true) && p.hasPermission((String) ec.DO.getConfigOption("petSelector.giveOnJoin.perm", "echopet.selector.join")))
				|| !((Boolean) ec.DO.getConfigOption("petSelector.giveOnJoin.usePerm", true)))) {
			int slot = ((Integer) ec.DO.getConfigOption("petSelector.giveOnJoin.slot", 9)) - 1;
			ItemStack i = inv.getItem(slot);
			ItemStack selector = SelectorItem.SELECTOR.getItem();
			if (i != null) {
				inv.clear(slot);
				inv.setItem(slot, selector);
				inv.addItem(i);
			}
			else {
				inv.setItem(slot, selector);
			}
		}
        final boolean sendMessage = ((Boolean) ec.DO.getConfigOption("sendPetLoadMessage", true));

            new BukkitRunnable() {
			
			public void run() {
				PetHandler.getInstance().loadPets(p, true, sendMessage, false);
			}
			
		}.runTaskLater(ec, 20);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		Pet pet = ec.PH.getPet(p); 
		if (pet != null) {
			ec.PH.saveFileData("autosave", pet);
			ec.SPH.saveToDatabase(pet, false);
			ec.PH.removePet(pet);
			//p.sendMessage(Lang.REMOVE_PET_DEATH.toString());
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player p = event.getPlayer();
		PetHandler.getInstance().loadPets(p, true, false, true);
	}
}