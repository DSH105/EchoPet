package com.github.dsh105.echopet.listeners;

import java.util.ArrayList;
import java.util.Iterator;

import com.github.dsh105.echopet.data.PetHandler;
import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.menu.main.*;
import com.github.dsh105.echopet.menu.selector.PetItem;
import com.github.dsh105.echopet.menu.selector.SelectorItem;
import com.github.dsh105.echopet.util.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.data.PetData;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.menu.main.DataMenu.DataMenuType;

public class MenuListener implements Listener {
	
	private EchoPet ec;
	
	public MenuListener(EchoPet ec) {
		this.ec = ec;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();


		Inventory inv = event.getInventory();

		String title = event.getView().getTitle();
		int slot = event.getRawSlot();

		try {
			if (slot < 0 || slot > inv.getSize()) {
				return;
			}
		} catch (Exception e) {return;}

		if (event.getSlotType() == InventoryType.SlotType.RESULT || event.getSlotType() == InventoryType.SlotType.FUEL) {
			for (int i = 1; i <= 4; i++) {
				if (inv.getItem(slot) != null && inv.getItem(i) != null && inv.getItem(i).equals(SelectorItem.SELECTOR.getItem())) {
					player.updateInventory();
					event.setCancelled(true);
					break;
				}
			}
		}

		if (title.equals("Pet Selector")) {
			if (slot <= 44 && inv.getItem(slot) != null) {
				if (inv.getItem(slot).equals(SelectorItem.CLOSE.getItem())) {
					player.closeInventory();
				}
				if (inv.getItem(slot).equals(SelectorItem.TOGGLE.getItem())) {
					Pet pet = EchoPet.getPluginInstance().PH.getPet(player);
					if (pet != null) {
						if (StringUtil.hpp("echopet.pet", "hide", player)) {
							player.performCommand("pet hide");
							player.closeInventory();
						}
					}
					else {
						if (StringUtil.hpp("echopet.pet", "show", player)) {
							player.performCommand("pet show");
							player.closeInventory();
						}
					}
				}
				if (inv.getItem(slot).equals(SelectorItem.CALL.getItem())) {
					if (StringUtil.hpp("echopet.pet", "call", player)) {
						player.performCommand("pet call");
						player.closeInventory();
					}
				}
				if (inv.getItem(slot).equals(SelectorItem.RIDE.getItem())) {
					Pet pet = EchoPet.getPluginInstance().PH.getPet(player);
					if (pet != null) {
						if (player.hasPermission("echopet.pet.ride.*") || StringUtil.hpp("echopet.pet", "ride." + PetUtil.getPetPerm(pet.getPetType()), player)) {
							player.performCommand("pet ride");
							player.closeInventory();
						}
					}
				}
				if (inv.getItem(slot).equals(SelectorItem.HAT.getItem())) {
					Pet pet = EchoPet.getPluginInstance().PH.getPet(player);
					if (pet != null) {
						if (player.hasPermission("echopet.pet.hat.*") || StringUtil.hpp("echopet.pet", "hat." + PetUtil.getPetPerm(pet.getPetType()), player)) {
							player.performCommand("pet hat");
							player.closeInventory();
						}
					}
				}
				if (inv.getItem(slot).equals(SelectorItem.MENU.getItem())) {
					if (StringUtil.hpp("echopet.pet", "menu", player)) {
						player.performCommand("pet menu");
						player.closeInventory();
					}
				}
				for (PetItem i : PetItem.values()) {
					if (inv.getItem(slot).equals(i.getItem(player))) {
						if (player.hasPermission("echopet.pet.type.*") || StringUtil.hpp("echopet.pet", "type." + PetUtil.getPetPerm(i.petType), player)) {
							PetHandler.getInstance().createPet(player, i.petType, true);
							Lang.sendTo(player, Lang.CREATE_PET.toString()
									.replace("%type%", StringUtil.capitalise(i.petType.toString().replace("_", ""))));
							player.closeInventory();
						}
					}
				}
				event.setCancelled(true);
			}
		}

		final Pet pet = EchoPet.getPluginInstance().PH.getPet(player);
		if (pet == null) {
			return;
		}

		int size = (title.equals("EchoPet DataMenu - Color") || pet.getPetType() == PetType.HORSE) ? 18 : 9;

		WaitingMenuData wmd = WaitingMenuData.waiting.get(pet);
		if (wmd == null) {
			wmd = new WaitingMenuData(pet);
		}

		if (slot <= size && inv.getItem(slot) != null) {
			if (title.equals("EchoPet DataMenu")) {
				if (inv.getItem(slot).equals(DataMenuItem.CLOSE.getItem())) {
					player.closeInventory();
					event.setCancelled(true);
					return;
				}
				for (final MenuItem mi : MenuItem.values()) {
					if (inv.getItem(slot).equals(mi.getItem()) || inv.getItem(slot).equals(mi.getBoolean(true)) || inv.getItem(slot).equals(mi.getBoolean(false))) {
						if (mi.getMenuType() == DataMenuType.BOOLEAN) {
							if (EnumUtil.isEnumType(PetData.class, mi.toString())) {
								PetData pd = PetData.valueOf(mi.toString());
								if (pet.getAllData(true).contains(pd)) {
									wmd.petDataFalse.add(pd);
								}
								else {
									wmd.petDataTrue.add(pd);
								}
							}
							else {
								if (mi.toString().equals("HAT")) {
									if (player.hasPermission("echopet.pet.hat.*") || StringUtil.hpp("echopet.pet", "hat." + PetUtil.getPetPerm(pet.getPetType()), player)) {
										if (!pet.isPetHat()) {
											pet.setAsHat(true);
											Lang.sendTo(pet.getOwner(), Lang.HAT_PET_ON.toString());
										}
										else {
											pet.setAsHat(false);
											Lang.sendTo(pet.getOwner(), Lang.HAT_PET_OFF.toString());
										}
									}
								}
								if (mi.toString().equals("RIDE")) {
									if (player.hasPermission("echopet.pet.ride.*") || StringUtil.hpp("echopet.pet", "ride." + PetUtil.getPetPerm(pet.getPetType()), player)) {
										if (!pet.isOwnerRiding()) {
											pet.ownerRidePet(true);
											inv.setItem(slot, mi.getBoolean(false));
											Lang.sendTo(pet.getOwner(), Lang.RIDE_PET_ON.toString());
										}
										else {
											pet.ownerRidePet(false);
											inv.setItem(slot, mi.getBoolean(true));
											Lang.sendTo(pet.getOwner(), Lang.RIDE_PET_OFF.toString());
										}
									}
								}
							}
						}
						else {
							player.closeInventory();
							new BukkitRunnable() {
								public void run() {
									DataMenu dm = new DataMenu(mi, pet);
									dm.open(false);
								}
							}.runTaskLater(ec, 1L);
						}
					}
				}
				event.setCancelled(true);
			}
			else if (title.startsWith("EchoPet DataMenu - ")) {
				if (inv.getItem(slot).equals(DataMenuItem.BACK.getItem())) {
					player.closeInventory();
					new BukkitRunnable() {
						public void run() {
							int size = pet.getPetType() == PetType.HORSE ? 18 : 9;
							PetMenu menu = new PetMenu(pet, MenuUtil.createOptionList(pet.getPetType()), size);
							menu.open(false);
						}
					}.runTaskLater(ec, 1L);
					event.setCancelled(true);
					return;
				}
				for (DataMenuItem dmi : DataMenuItem.values()) {
					if (inv.getItem(slot).equals(dmi.getItem())) {
						wmd.petDataTrue.add(dmi.getDataLink());
					}
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
			if (!wmd.petDataTrue.isEmpty()) {
				Iterator<PetData> i = wmd.petDataTrue.listIterator();
				while (i.hasNext()) {
					PetData dataTemp = i.next();
					if (!StringUtil.hpp("echopet.pet.data", dataTemp.getConfigOptionString().toLowerCase(), player)) {
						i.remove();
					}
				}

				ec.PH.setData(pet, wmd.petDataTrue.toArray(new PetData[wmd.petDataTrue.size()]), true);
				try {
					Particle.FIRE.sendToLocation(pet.getLocation());
				} catch (Exception e) {
					ec.debug(e, "Particle Effect failed.");
				}
			}
			
			if (!wmd.petDataFalse.isEmpty()) {
				Iterator<PetData> i2 = wmd.petDataFalse.listIterator();
				while (i2.hasNext()) {
					PetData dataTemp = i2.next();
					if (!StringUtil.hpp("echopet.pet.data", dataTemp.getConfigOptionString().toLowerCase(), player)) {
						i2.remove();
					}
				}

				ec.PH.setData(pet, wmd.petDataFalse.toArray(new PetData[wmd.petDataFalse.size()]), false);
				try {
					Particle.RAINBOW_SMOKE.sendToLocation(pet.getLocation());
				} catch (Exception e) {
					ec.debug(e, "Particle Effect failed.");
				}
			}

			WaitingMenuData.waiting.remove(wmd);
		}
		
	}
}