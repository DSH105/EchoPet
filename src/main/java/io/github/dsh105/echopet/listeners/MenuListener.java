package io.github.dsh105.echopet.listeners;

import io.github.dsh105.dshutils.Particle;
import io.github.dsh105.dshutils.logger.Logger;
import io.github.dsh105.dshutils.util.EnumUtil;
import io.github.dsh105.dshutils.util.StringUtil;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.living.PetData;
import io.github.dsh105.echopet.menu.main.DataMenu;
import io.github.dsh105.echopet.menu.main.DataMenu.DataMenuType;
import io.github.dsh105.echopet.menu.main.DataMenuItem;
import io.github.dsh105.echopet.menu.main.MenuItem;
import io.github.dsh105.echopet.menu.main.PetMenu;
import io.github.dsh105.echopet.menu.selector.PetItem;
import io.github.dsh105.echopet.menu.selector.SelectorItem;
import io.github.dsh105.echopet.mysql.SQLPetHandler;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.MenuUtil;
import io.github.dsh105.echopet.util.permissions.Perm;
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
        if (player.getOpenInventory().getTitle().equals("Pet Selector") || player.getOpenInventory().getTitle().contains("EchoPet DataMenu")) {
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
                    String cmd = EchoPetPlugin.getInstance().cmdString;
                    if (inv.getItem(slot).equals(SelectorItem.TOGGLE.getItem())) {
                        Pet pet = EchoPetPlugin.getInstance().PH.getPet(player);
                        if (pet != null) {
                            if (Perm.BASE_HIDE.hasPerm(player, true, false)) {
                                player.performCommand(cmd + " hide");
                                player.closeInventory();
                            }
                        } else {
                            if (Perm.BASE_SHOW.hasPerm(player, true, false)) {
                                player.performCommand(cmd + " show");
                                player.closeInventory();
                            }
                        }
                    }
                    if (inv.getItem(slot).equals(SelectorItem.CALL.getItem())) {
                        if (Perm.BASE_CALL.hasPerm(player, true, false)) {
                            player.performCommand(cmd + " call");
                            player.closeInventory();
                        }
                    }
                    if (inv.getItem(slot).equals(SelectorItem.RIDE.getItem())) {
                        Pet pet = EchoPetPlugin.getInstance().PH.getPet(player);
                        if (pet != null) {
                            if (Perm.hasTypePerm(player, true, Perm.BASE_RIDE, pet.getPetType())) {
                                player.performCommand(cmd + " ride");
                                player.closeInventory();
                            }
                        }
                    }
                    if (inv.getItem(slot).equals(SelectorItem.HAT.getItem())) {
                        Pet pet = EchoPetPlugin.getInstance().PH.getPet(player);
                        if (pet != null) {
                            if (Perm.hasTypePerm(player, true, Perm.BASE_HAT, pet.getPetType())) {
                                player.performCommand(cmd + " hat");
                                player.closeInventory();
                            }
                        }
                    }
                    if (inv.getItem(slot).equals(SelectorItem.MENU.getItem())) {
                        if (Perm.BASE_MENU.hasPerm(player, true, false)) {
                            player.closeInventory();
                            player.performCommand(cmd + " menu");
                        }
                    }
                    for (PetItem i : PetItem.values()) {
                        if (inv.getItem(slot).equals(i.getItem(player))) {
                            if (Perm.hasTypePerm(player, true, Perm.BASE_PETTYPE, i.petType)) {
                                Pet pet = PetHandler.getInstance().createPet(player, i.petType, true);
                                if (pet != null) {
                                    PetHandler.getInstance().saveFileData("autosave", pet);
                                    SQLPetHandler.getInstance().saveToDatabase(pet, false);
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


        final Pet pet = EchoPetPlugin.getInstance().PH.getPet(player);
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
                                    if (Perm.hasDataPerm(player, true, pet.getPetType(), pd)) {
                                        if (pet.getPetData().contains(pd)) {
                                            PetHandler.getInstance().setData(pet, pd, false);
                                            try {
                                                Particle.RED_SMOKE.sendTo(pet.getLocation());
                                            } catch (Exception e) {
                                            }
                                        } else {
                                            PetHandler.getInstance().setData(pet, pd, true);
                                            try {
                                                Particle.SPARKLE.sendTo(pet.getLocation());
                                            } catch (Exception e) {
                                            }
                                        }
                                    }
                                } else {
                                    if (mi.toString().equals("HAT")) {
                                        if (Perm.hasTypePerm(player, true, Perm.BASE_HAT, pet.getPetType())) {
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
                                        if (Perm.hasTypePerm(player, true, Perm.BASE_RIDE, pet.getPetType())) {
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
                                        this.runTaskLater(EchoPetPlugin.getInstance(), 1L);
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
                            public void run() {
                                int size = pet.getPetType() == PetType.HORSE ? 18 : 9;
                                PetMenu menu = new PetMenu(pet, MenuUtil.createOptionList(pet.getPetType()), size);
                                menu.open(false);
                            }
                        }.runTaskLater(EchoPetPlugin.getInstance(), 1L);
                        return;
                    }
                    for (DataMenuItem dmi : DataMenuItem.values()) {
                        if (inv.getItem(slot).equals(dmi.getItem())) {
                            PetData pd = dmi.getDataLink();
                            if (Perm.hasDataPerm(player, true, pet.getPetType(), pd)) {
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