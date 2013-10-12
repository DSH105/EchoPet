package io.github.dsh105.echopet.menu.main;

import java.util.ArrayList;

import io.github.dsh105.echopet.EchoPet;
import io.github.dsh105.echopet.api.event.PetMenuOpenEvent;
import io.github.dsh105.echopet.data.PetData;
import io.github.dsh105.echopet.menu.Menu;
import io.github.dsh105.echopet.util.EnumUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import io.github.dsh105.echopet.entity.pet.Pet;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.StringUtil;


public class PetMenu implements Menu {
	
	Inventory inv;
	private int size;
	private Pet pet;
	private ArrayList<MenuOption> options = new ArrayList<MenuOption>();
	
	public PetMenu(Pet pet, ArrayList<MenuOption> options, int size) {
		this.pet = pet;
		this.size = size;
		this.inv = Bukkit.createInventory(pet.getOwner(), size, "EchoPet DataMenu");
		this.options = options;
		for (MenuOption o : this.options) {
			if (o.item.getMenuType() == DataMenu.DataMenuType.BOOLEAN) {
				MenuItem mi = o.item;
				if (EnumUtil.isEnumType(PetData.class, mi.toString())) {
					PetData pd = PetData.valueOf(mi.toString());
					if (pet.getActiveData().contains(pd)) {
						this.inv.setItem(o.position, o.item.getBoolean(false));
					}
					else {
						this.inv.setItem(o.position, o.item.getBoolean(true));
					}
				}
				else {
					if (mi.toString().equals("HAT")) {
						if (pet.isPetHat()) {
							this.inv.setItem(o.position, o.item.getBoolean(false));
						}
						else {
							this.inv.setItem(o.position, o.item.getBoolean(true));
						}
					}
					if (mi.toString().equals("RIDE")) {
						if (pet.isOwnerRiding()) {
							this.inv.setItem(o.position, o.item.getBoolean(false));
						}
						else {
							this.inv.setItem(o.position, o.item.getBoolean(true));
						}
					}
				}
			}
			else {
				this.inv.setItem(o.position, o.item.getItem());
			}
		}
		int book = size - 1;
		this.inv.setItem(book, DataMenuItem.CLOSE.getItem());
	}
	
	public void open(boolean sendMessage) {
		PetMenuOpenEvent menuEvent = new PetMenuOpenEvent(this.pet, PetMenuOpenEvent.MenuType.MAIN);
		EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(menuEvent);
		if (menuEvent.isCancelled()) {
			return;
		}
		this.pet.getOwner().openInventory(this.inv);
		if (sendMessage) {
			Lang.sendTo(this.pet.getOwner(), Lang.OPEN_MENU.toString().replace("%type%", StringUtil.capitalise(this.pet.getPetType().toString().replace("_", " "))));
		}
	}
}
