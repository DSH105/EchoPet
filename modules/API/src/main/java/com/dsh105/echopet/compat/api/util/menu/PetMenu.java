/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.api.util.menu;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.event.PetMenuOpenEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class PetMenu {

    Inventory inv;
    private int size;
    private IPet pet;
    private ArrayList<MenuOption> options = new ArrayList<MenuOption>();

    public PetMenu(IPet pet, ArrayList<MenuOption> options, int size) {
        this.pet = pet;
        this.size = size;
        this.inv = Bukkit.createInventory(pet.getOwner(), size, "EchoPet DataMenu");
        this.options = options;
        for (MenuOption o : this.options) {
            if (o.item.getMenuType() == DataMenu.DataMenuType.BOOLEAN) {
                MenuItem mi = o.item;
                if (GeneralUtil.isEnumType(PetData.class, mi.toString())) {
                    PetData pd = PetData.valueOf(mi.toString());
                    if (pet.getPetData().contains(pd)) {
                        this.inv.setItem(o.position, o.item.getBoolean(false));
                    } else {
                        this.inv.setItem(o.position, o.item.getBoolean(true));
                    }
                } else {
                    if (mi.toString().equals("HAT")) {
                        if (pet.isHat()) {
                            this.inv.setItem(o.position, o.item.getBoolean(false));
                        } else {
                            this.inv.setItem(o.position, o.item.getBoolean(true));
                        }
                    }
                    if (mi.toString().equals("RIDE")) {
                        if (pet.isOwnerRiding()) {
                            this.inv.setItem(o.position, o.item.getBoolean(false));
                        } else {
                            this.inv.setItem(o.position, o.item.getBoolean(true));
                        }
                    }
                }
            } else {
                this.inv.setItem(o.position, o.item.getItem());
            }
        }
        int book = size - 1;
        this.inv.setItem(book, DataMenuItem.CLOSE.getItem());
    }

    public void open(boolean sendMessage) {
        PetMenuOpenEvent menuEvent = new PetMenuOpenEvent(this.pet.getOwner(), PetMenuOpenEvent.MenuType.MAIN);
        EchoPet.getPlugin().getServer().getPluginManager().callEvent(menuEvent);
        if (menuEvent.isCancelled()) {
            return;
        }
        this.pet.getOwner().openInventory(this.inv);
        if (sendMessage) {
            Lang.sendTo(this.pet.getOwner(), Lang.OPEN_MENU.toString().replace("%type%", StringUtil.capitalise(this.pet.getPetType().toString().replace("_", " "))));
        }
    }
}
