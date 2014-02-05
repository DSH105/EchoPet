package io.github.dsh105.echopet.menu.main;

import com.dsh105.dshutils.util.StringUtil;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.api.event.PetMenuOpenEvent;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;


public class DataMenu implements Menu {

    Inventory inv;
    private Pet pet;

    public DataMenu(MenuItem mi, Pet pet) {
        this.pet = pet;
        int size = mi == MenuItem.COLOR ? 18 : 9;
        this.inv = Bukkit.createInventory(pet.getOwner(), size, "EchoPet DataMenu - " + StringUtil.capitalise(mi.toString().replace("_", " ")));
        this.setItems(mi.getMenuType(), size);
    }

    @Override
    public void open(boolean sendMessage) {
        PetMenuOpenEvent menuEvent = new PetMenuOpenEvent(this.pet.getOwner(), PetMenuOpenEvent.MenuType.DATA);
        EchoPetPlugin.getInstance().getServer().getPluginManager().callEvent(menuEvent);
        if (menuEvent.isCancelled()) {
            return;
        }
        Player p = this.pet.getOwner();
        if (p != null) {
            p.openInventory(this.inv);
        }
    }

    public void setItems(DataMenuType type, int size) {
        int i = 0;
        for (DataMenuItem mi : DataMenuItem.values()) {
            if (mi.getType() == type) {
                this.inv.setItem(i, mi.getItem());
                i++;
            }
        }
        this.inv.setItem((size - 1), DataMenuItem.BACK.getItem());
    }

    public enum DataMenuType {
        BOOLEAN,
        CAT_TYPE,
        COLOR,
        PROFESSION,
        SIZE,
        OTHER,
        HORSE_TYPE,
        HORSE_VARIANT,
        HORSE_MARKING,
        HORSE_ARMOUR;
    }
}