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

package io.github.dsh105.echopet.listeners;

import com.dsh105.dshutils.Particle;
import com.dsh105.dshutils.logger.Logger;
import com.dsh105.dshutils.util.EnumUtil;
import io.github.dsh105.echopet.compat.api.config.SelectorLayout;
import io.github.dsh105.echopet.compat.api.entity.IPet;
import io.github.dsh105.echopet.compat.api.entity.PetData;
import io.github.dsh105.echopet.compat.api.entity.PetType;
import io.github.dsh105.echopet.compat.api.plugin.EchoPet;
import io.github.dsh105.echopet.compat.api.util.Lang;
import io.github.dsh105.echopet.compat.api.util.menu.DataMenu;
import io.github.dsh105.echopet.compat.api.util.menu.DataMenu.DataMenuType;
import io.github.dsh105.echopet.compat.api.util.menu.DataMenuItem;
import io.github.dsh105.echopet.compat.api.util.menu.MenuItem;
import io.github.dsh105.echopet.compat.api.util.menu.PetMenu;
import io.github.dsh105.echopet.compat.api.util.MenuUtil;
import io.github.dsh105.echopet.compat.api.util.Perm;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class MenuListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().contains("EchoPet DataMenu")) {
            event.setCancelled(true);
        }

        Inventory inv = event.getInventory();

        String title = event.getView().getTitle();
        int slot = event.getRawSlot();

        try {
            if (slot < 0 || slot >= inv.getSize() || inv.getItem(slot) == null) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        if (event.getSlotType() == InventoryType.SlotType.RESULT) {
            try {
                for (int i = 1; i <= 4; i++) {
                    if (inv.getItem(slot) != null && inv.getItem(i) != null && inv.getItem(i).isSimilar(SelectorLayout.getSelectorItem())) {
                        player.updateInventory();
                        event.setCancelled(true);
                        break;
                    }
                }
            } catch (Exception e) {
                return;
            }
        }

        final IPet pet = EchoPet.getManager().getPet(player);
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
                    for (MenuItem mi : MenuItem.values()) {
                        if (inv.getItem(slot).equals(mi.getItem()) || inv.getItem(slot).equals(mi.getBoolean(true)) || inv.getItem(slot).equals(mi.getBoolean(false))) {
                            if (mi.getMenuType() == DataMenuType.BOOLEAN) {
                                if (EnumUtil.isEnumType(PetData.class, mi.toString().toUpperCase())) {
                                    PetData pd = PetData.valueOf(mi.toString());
                                    if (Perm.hasDataPerm(player, true, pet.getPetType(), pd, false)) {
                                        if (pet.getPetData().contains(pd)) {
                                            EchoPet.getManager().setData(pet, pd, false);
                                            Particle.RED_SMOKE.sendTo(pet.getLocation());
                                        } else {
                                            EchoPet.getManager().setData(pet, pd, true);
                                            Particle.SPARKLE.sendTo(pet.getLocation());
                                        }
                                    }
                                } else {
                                    if (mi.toString().equals("HAT")) {
                                        if (Perm.hasTypePerm(player, true, Perm.BASE_HAT, false, pet.getPetType())) {
                                            if (!pet.isHat()) {
                                                pet.setAsHat(true);
                                                Lang.sendTo(pet.getOwner(), Lang.HAT_PET_ON.toString());
                                            } else {
                                                pet.setAsHat(false);
                                                Lang.sendTo(pet.getOwner(), Lang.HAT_PET_OFF.toString());
                                            }
                                        }
                                    }
                                    if (mi.toString().equals("RIDE")) {
                                        if (Perm.hasTypePerm(player, true, Perm.BASE_RIDE, false, pet.getPetType())) {
                                            if (!pet.isOwnerRiding()) {
                                                pet.ownerRidePet(true);
                                                inv.setItem(slot, mi.getBoolean(false));
                                                Lang.sendTo(pet.getOwner(), Lang.RIDE_PET_ON.toString());
                                            } else {
                                                pet.ownerRidePet(false);
                                                inv.setItem(slot, mi.getBoolean(true));
                                                Lang.sendTo(pet.getOwner(), Lang.RIDE_PET_OFF.toString());
                                            }
                                        }
                                    }
                                }
                            } else {
                                player.closeInventory();
                                class OpenMenu extends BukkitRunnable {

                                    MenuItem mi;

                                    public OpenMenu(MenuItem mi) {
                                        this.mi = mi;
                                        this.runTaskLater(EchoPet.getPlugin(), 1L);
                                    }

                                    @Override
                                    public void run() {
                                        DataMenu dm = new DataMenu(mi, pet);
                                        dm.open(false);
                                    }

                                }

                                new OpenMenu(mi);
                            }
                        }
                    }
                    event.setCancelled(true);
                } else if (title.startsWith("EchoPet DataMenu - ")) {
                    if (inv.getItem(slot).equals(DataMenuItem.BACK.getItem())) {
                        player.closeInventory();
                        event.setCancelled(true);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                int size = pet.getPetType() == PetType.HORSE ? 18 : 9;
                                PetMenu menu = new PetMenu(pet, MenuUtil.createOptionList(pet.getPetType()), size);
                                menu.open(false);
                            }
                        }.runTaskLater(EchoPet.getPlugin(), 1L);
                        return;
                    }
                    for (DataMenuItem dmi : DataMenuItem.values()) {
                        if (inv.getItem(slot).equals(dmi.getItem())) {
                            PetData pd = dmi.getDataLink();
                            if (Perm.hasDataPerm(player, true, pet.getPetType(), pd, false)) {
                                EchoPet.getManager().setData(pet, pd, true);
                            }
                        }
                    }
                    event.setCancelled(true);
                }
            } catch (IllegalArgumentException e) {
                Logger.log(Logger.LogLevel.SEVERE, "Encountered severe error whilst handling InventoryClickEvent.", e, true);
                event.setCancelled(true);
            } catch (IllegalStateException e) {
                Logger.log(Logger.LogLevel.SEVERE, "Encountered severe error whilst handling InventoryClickEvent.", e, true);
                event.setCancelled(true);
            }

        }
    }
}