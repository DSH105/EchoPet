package io.github.dsh105.echopet.menu.selector;

import io.github.dsh105.echopet.EchoPet;
import io.github.dsh105.echopet.api.event.PetSelectMenuOpenEvent;
import io.github.dsh105.echopet.menu.Menu;
import io.github.dsh105.echopet.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;


public class PetSelector implements Menu {

    Inventory inv;
    private int size;
    private Player viewer;

    public PetSelector(int size, Player viewer) {
        this.inv = Bukkit.createInventory(viewer, size, "Pet Selector");
        this.size = size;
        this.viewer = viewer;
        setup();
    }

    public void setup() {
        int count = 0;
        for (PetItem item : PetItem.values()) {

            if (EchoPet.getInstance().options.allowPetType(item.petType)) {
                this.inv.setItem(count, item.getItem(this.viewer));
                count++;
            }
        }

        this.inv.setItem(this.size - 1, SelectorItem.CLOSE.getItem());
        this.inv.setItem(this.size - 3, SelectorItem.TOGGLE.getItem());
        this.inv.setItem(this.size - 4, SelectorItem.CALL.getItem());

        this.inv.setItem(this.size - 6, SelectorItem.HAT.getItem());
        this.inv.setItem(this.size - 7, SelectorItem.RIDE.getItem());

        this.inv.setItem(this.size - 9, SelectorItem.MENU.getItem());
    }

    public void open(boolean sendMessage) {
        PetSelectMenuOpenEvent menuEvent = new PetSelectMenuOpenEvent(this.viewer);
        EchoPet.getInstance().getServer().getPluginManager().callEvent(menuEvent);
        if (menuEvent.isCancelled()) {
            return;
        }
        this.viewer.openInventory(this.inv);
        if (sendMessage) {
            Lang.sendTo(this.viewer, Lang.OPEN_SELECTOR.toString());
        }
    }
}