package com.github.dsh105.echopet.listeners;

import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.data.PetData;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.menu.DataMenu;
import com.github.dsh105.echopet.menu.DataMenu.DataMenuType;
import com.github.dsh105.echopet.menu.DataMenuItem;
import com.github.dsh105.echopet.menu.MenuItem;
import com.github.dsh105.echopet.menu.PetMenu;
import com.github.dsh105.echopet.menu.WaitingMenuData;
import com.github.dsh105.echopet.util.EnumUtil;
import com.github.dsh105.echopet.util.Lang;
import com.github.dsh105.echopet.util.MenuUtil;
import com.github.dsh105.echopet.util.Particle;
import com.github.dsh105.echopet.util.StringUtil;

public class MenuListener implements Listener {
	
	private EchoPet ec;
	
	public MenuListener(EchoPet ec) {
		this.ec = ec;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryOpen(InventoryOpenEvent event) {
		Player player = (Player) event.getPlayer();
		final Pet pet = EchoPet.getPluginInstance().PH.getPet(player);
		if (pet == null) {
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		final Pet pet = EchoPet.getPluginInstance().PH.getPet(player);
		if (pet == null) {
			return;
		}
		
		Inventory inv = event.getInventory();
		String title = event.getView().getTitle();
		int slot = event.getRawSlot();
		int size = (title.equals("EchoPet DataMenu - Color")) ? 17 : 8;
		if (slot <= size && inv.getItem(slot) != null) {
			if (title.equals("EchoPet DataMenu")) {
				if (inv.getItem(slot).equals(DataMenuItem.CLOSE.getItem())) {
					player.closeInventory();
					event.setCancelled(true);
					return;
				}
				for (final MenuItem mi : MenuItem.values()) {
					if (inv.getItem(slot).equals(mi.getItem())) {
						player.closeInventory();
						new BukkitRunnable() {
							public void run() {
								DataMenu dm = new DataMenu(mi, pet);
								dm.open(false);
							}
						}.runTaskLater(ec, 1L);
					}
				}
				event.setCancelled(true);
			}
			else if (title.startsWith("EchoPet DataMenu - ")) {
				if (inv.getItem(slot).equals(DataMenuItem.BACK.getItem())) {
					player.closeInventory();
					new BukkitRunnable() {
						public void run() {
							PetMenu menu = new PetMenu(pet, MenuUtil.createOptionList(pet.getPetType()));
							menu.open(false);
						}
					}.runTaskLater(ec, 1L);
					event.setCancelled(true);
					return;
				}

				String[] split = title.split(" - ");
				WaitingMenuData wmd = WaitingMenuData.waiting.get(pet);
				if (wmd == null) {
					wmd = new WaitingMenuData(pet);
				}
				try {
					if (split[1].equalsIgnoreCase("ride")) {
						if (StringUtil.hpp("echopet.pet", "ride", player)) {
							if (!pet.isOwnerRiding() && inv.getItem(slot).equals(DataMenuItem.BOOLEAN_TRUE.getItem())) {
								pet.ownerRidePet(true);
								pet.getOwner().sendMessage(Lang.RIDE_PET_ON.toString());
							}
							else if (pet.isOwnerRiding() && inv.getItem(slot).equals(DataMenuItem.BOOLEAN_FALSE.getItem())) {
								pet.ownerRidePet(false);
								pet.getOwner().sendMessage(Lang.RIDE_PET_OFF.toString());
							}
						}
					}
					else if (split[1].equalsIgnoreCase("hat")) {
						if (StringUtil.hpp("echopet.pet", "hat", player)) {
							if (!pet.isPetHat() && inv.getItem(slot).equals(DataMenuItem.BOOLEAN_TRUE.getItem())) {
								pet.setAsHat(true);
								pet.getOwner().sendMessage(Lang.HAT_PET_ON.toString());
							}
							else if (pet.isPetHat() && inv.getItem(slot).equals(DataMenuItem.BOOLEAN_FALSE.getItem())) {
								pet.setAsHat(false);
								pet.getOwner().sendMessage(Lang.HAT_PET_OFF.toString());
							}
						}
					}
					else if (EnumUtil.isEnumType(PetData.class, split[1].toUpperCase())) {
						if (inv.getItem(slot).equals(DataMenuItem.BACK.getItem())) {
							player.closeInventory();
							new BukkitRunnable() {
								public void run() {
									PetMenu menu = new PetMenu(pet, MenuUtil.createOptionList(pet.getPetType()));
									menu.open(false);
								}
							}.runTaskLater(ec, 1L);
							return;
						}
						PetData pd = PetData.valueOf(split[1].toUpperCase());
						for (DataMenuItem dmi : DataMenuItem.values()) {
							if (inv.getItem(slot).equals(dmi.getItem())) {
								if (dmi.getType() == DataMenuType.BOOLEAN) {
									if (dmi == DataMenuItem.BOOLEAN_TRUE) {
										wmd.petDataTrue.add(pd);
									}
									else if (dmi == DataMenuItem.BOOLEAN_FALSE) {
										wmd.petDataFalse.add(pd);
									}
								}
							}
						}
					}
					else {
						for (DataMenuItem dmi : DataMenuItem.values()) {
							if (inv.getItem(slot).equals(dmi.getItem())) {
								wmd.petDataTrue.add(dmi.getDataLink());
							}
						}
					}
				} catch (Exception e) {
					EchoPet.getPluginInstance().debug(e, "Encountered error whilst handling InventoryClick event for EchoPet DataMenu (" + player.getName() + ")");
				}
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		Pet pet = EchoPet.getPluginInstance().PH.getPet(player);
		if (pet == null) {
			return;
		}
		
		if (!event.getView().getTitle().contains("EchoPet DataMenu")) {
			return;
		}
		
		WaitingMenuData wmd = WaitingMenuData.waiting.get(pet);
		
		if (wmd != null) {
			Iterator<PetData> i = wmd.petDataTrue.listIterator();
			while (i.hasNext()) {
				PetData dataTemp = i.next();
				if (!StringUtil.hpp("echopet.pet.data", dataTemp.getConfigOptionString().toLowerCase(), player)) {
					wmd.petDataTrue.remove(dataTemp);
				}
			}
			Iterator<PetData> i2 = wmd.petDataFalse.listIterator();
			while (i2.hasNext()) {
				PetData dataTemp = i2.next();
				if (!StringUtil.hpp("echopet.pet.data", dataTemp.getConfigOptionString().toLowerCase(), player)) {
					wmd.petDataFalse.remove(dataTemp);
				}
			}
			
			if (!wmd.petDataTrue.isEmpty()) {
				ec.PH.setData(pet, wmd.petDataTrue.toArray(new PetData[wmd.petDataFalse.size()]), true);
				try {
					Particle.FIRE.sendToLocation(pet.getLocation());
				} catch (Exception e) {
					ec.debug(e, "Particle Effect failed.");
				}
			}
			
			if (!wmd.petDataFalse.isEmpty()) {
				ec.PH.setData(pet, wmd.petDataTrue.toArray(new PetData[wmd.petDataTrue.size()]), false);
				try {
					Particle.RAINBOW_SMOKE.sendToLocation(pet.getLocation());
				} catch (Exception e) {
					ec.debug(e, "Particle Effect failed.");
				}
			}
		}
		
	}
}