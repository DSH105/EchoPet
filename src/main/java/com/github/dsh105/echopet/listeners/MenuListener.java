package com.github.dsh105.echopet.listeners;

import java.util.ArrayList;
import java.util.Iterator;

import com.github.dsh105.echopet.data.PetHandler;
import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.logger.Logger;
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
			if (slot < 0 || slot >= inv.getSize() || inv.getItem(slot) == null) {
				return;
			}
		} catch (Exception e) {return;}

		if (event.getSlotType() == InventoryType.SlotType.RESULT) {
			try {
				for (int i = 1; i <= 4; i++) {
					if (inv.getItem(slot) != null && inv.getItem(i) != null && inv.getItem(i).equals(SelectorItem.SELECTOR.getItem())) {
						player.updateInventory();
						event.setCancelled(true);
						break;
					}
				}
			} catch (Exception e) {
				return;
			}
		}
		if (title.equals("Pet Selector")) {
			try {
				if (slot <= 44 && inv.getItem(slot) != null) {
					if (inv.getItem(slot).equals(SelectorItem.CLOSE.getItem())) {
						player.closeInventory();
					}
					String cmd = EchoPet.getPluginInstance().cmdString;
					if (inv.getItem(slot).equals(SelectorItem.TOGGLE.getItem())) {
						Pet pet = EchoPet.getPluginInstance().PH.getPet(player);
						if (pet != null) {
							if (StringUtil.hpp("echopet.pet", "hide", player)) {
								player.performCommand(cmd + " hide");
								player.closeInventory();
							}
						}
						else {
							if (StringUtil.hpp("echopet.pet", "show", player)) {
								player.performCommand(cmd + " show");
								player.closeInventory();
							}
						}
					}
					if (inv.getItem(slot).equals(SelectorItem.CALL.getItem())) {
						if (StringUtil.hpp("echopet.pet", "call", player)) {
							player.performCommand(cmd + " call");
							player.closeInventory();
						}
					}
					if (inv.getItem(slot).equals(SelectorItem.RIDE.getItem())) {
						Pet pet = EchoPet.getPluginInstance().PH.getPet(player);
						if (pet != null) {
							if (player.hasPermission("echopet.pet.ride.*") || StringUtil.hpp("echopet.pet", "ride." + PetUtil.getPetPerm(pet.getPetType()), player)) {
								player.performCommand(cmd + " ride");
								player.closeInventory();
							}
						}
					}
					if (inv.getItem(slot).equals(SelectorItem.HAT.getItem())) {
						Pet pet = EchoPet.getPluginInstance().PH.getPet(player);
						if (pet != null) {
							if (player.hasPermission("echopet.pet.hat.*") || StringUtil.hpp("echopet.pet", "hat." + PetUtil.getPetPerm(pet.getPetType()), player)) {
								player.performCommand(cmd + " hat");
								player.closeInventory();
							}
						}
					}
					if (inv.getItem(slot).equals(SelectorItem.MENU.getItem())) {
						if (StringUtil.hpp("echopet.pet", "menu", player)) {
							player.closeInventory();
							player.performCommand(cmd + " menu");
						}
					}
					for (PetItem i : PetItem.values()) {
						if (inv.getItem(slot).equals(i.getItem(player))) {
							if (player.hasPermission("echopet.pet.type.*") || StringUtil.hpp("echopet.pet", "type." + PetUtil.getPetPerm(i.petType), player)) {
								Pet pet = PetHandler.getInstance().createPet(player, i.petType, true);
								if (pet != null) {
									ec.PH.saveFileData("autosave", pet);
									ec.SPH.saveToDatabase(pet, false);
									Lang.sendTo(player, Lang.CREATE_PET.toString()
											.replace("%type%", StringUtil.capitalise(i.petType.toString().replace("_", ""))));
								}
								player.closeInventory();
							}
						}
					}
					event.setCancelled(true);
				}
			} catch (Exception e) {
				Logger.log(Logger.LogLevel.SEVERE, "Encountered severe error whilst handling InventoryClickEvent.", e, true);
				event.setCancelled(true);
			}
		}


		final Pet pet = EchoPet.getPluginInstance().PH.getPet(player);
		if (pet == null) {
			return;
		}

		int size = (title.equals("EchoPet DataMenu - Color") || pet.getPetType() == PetType.HORSE) ? 18 : 9;

		if (slot <= size && inv.getItem(slot) != null) {

			try {
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
									if (StringUtil.hpp("echopet.pet.data", pd.getConfigOptionString().toLowerCase(), player)) {
										if (pet.getActiveData().contains(pd)) {
											PetHandler.getInstance().setData(pet, pd, false);
											try {
												Particle.RED_SMOKE.sendToLocation(pet.getLocation());
											} catch (Exception e) {}
										}
										else {
											PetHandler.getInstance().setData(pet, pd, true);
											try {
												Particle.SPARKLE.sendToLocation(pet.getLocation());
											} catch (Exception e) {}
										}
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
						event.setCancelled(true);
						new BukkitRunnable() {
							public void run() {
								int size = pet.getPetType() == PetType.HORSE ? 18 : 9;
								PetMenu menu = new PetMenu(pet, MenuUtil.createOptionList(pet.getPetType()), size);
								menu.open(false);
							}
						}.runTaskLater(ec, 1L);
						return;
					}
					for (DataMenuItem dmi : DataMenuItem.values()) {
						if (inv.getItem(slot).equals(dmi.getItem())) {
							PetData pd = dmi.getDataLink();
							if (StringUtil.hpp("echopet.pet.data", pd.getConfigOptionString().toLowerCase(), player)) {
								PetHandler.getInstance().setData(pet, pd, true);
							}
						}
					}
					event.setCancelled(true);
				}
			} catch (Exception e) {
				Logger.log(Logger.LogLevel.SEVERE, "Encountered severe error whilst handling InventoryClickEvent.", e, true);
				event.setCancelled(true);
			}

		}
	}
}