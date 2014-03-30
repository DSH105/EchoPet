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

import com.dsh105.dshutils.util.GeometryUtil;
import com.dsh105.dshutils.util.StringUtil;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.api.entity.nms.ICraftPet;
import io.github.dsh105.echopet.api.entity.nms.IEntityPacketPet;
import io.github.dsh105.echopet.api.event.PetInteractEvent;
import io.github.dsh105.echopet.config.ConfigOptions;
import io.github.dsh105.echopet.api.PetHandler;
import io.github.dsh105.echopet.api.entity.pet.Pet;
import io.github.dsh105.echopet.menu.selector.SelectorLayout;
import io.github.dsh105.echopet.menu.selector.SelectorMenu;
import io.github.dsh105.echopet.mysql.SQLPetHandler;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.WorldUtil;
import org.bukkit.ChatColor;
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

import java.util.Iterator;

public class PetOwnerListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack itemStack = event.getItem();
        if (itemStack != null && itemStack.isSimilar(SelectorLayout.getSelectorItem())) {
            new SelectorMenu().showTo(p);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        Entity e = event.getRightClicked();
        if (e instanceof ICraftPet) {
            Pet pet = ((ICraftPet) e).getPet();
            event.setCancelled(true);
            PetInteractEvent iEvent = new PetInteractEvent(pet, p, PetInteractEvent.Action.RIGHT_CLICK, false);
            EchoPetPlugin.getInstance().getServer().getPluginManager().callEvent(iEvent);
            if (!iEvent.isCancelled()) {
                pet.getEntityPet().onInteract(p);
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
        final Pet pi = PetHandler.getInstance().getPet(p);
        Iterator<Pet> i = PetHandler.getInstance().getPets().iterator();
        while (i.hasNext()) {
            Pet pet = i.next();
            if (pet.getEntityPet() instanceof IEntityPacketPet && ((IEntityPacketPet) pet.getEntityPet()).hasInititiated()) {
                if (GeometryUtil.getNearbyEntities(event.getTo(), 50).contains(pet)) {
                    ((IEntityPacketPet) pet.getEntityPet()).updatePosition();
                }
            }
        }
        if (pi != null) {
            if (!WorldUtil.allowPets(event.getTo())) {
                Lang.sendTo(p, Lang.PETS_DISABLED_HERE.toString().replace("%world%", StringUtil.capitalise(event.getTo().getWorld().getName())));
                PetHandler.getInstance().saveFileData("autosave", pi);
                SQLPetHandler.getInstance().saveToDatabase(pi, false);
                PetHandler.getInstance().removePet(pi, false);
                return;
            }
            //if (pi.ownerIsMounting) return;
            if (event.getFrom().getWorld() == event.getTo().getWorld()) {
                /*PetHandler.getInstance().saveFileData("autosave", pi);
                SQLPetHandler.getInstance().saveToDatabase(pi, false);
                PetHandler.getInstance().removePet(pi, false);
                if (!WorldUtil.allowPets(event.getTo())) {
                    Lang.sendTo(p, Lang.PETS_DISABLED_HERE.toString().replace("%world%", StringUtil.capitalise(event.getTo().getWorld().getName())));
                    return;
                }
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        PetHandler.getInstance().loadPets(p, false, false, false);
                    }

                }.runTaskLater(EchoPetPlugin.getInstance(), 20L);*/
            } else {
                /*PetHandler.getInstance().saveFileData("autosave", pi);
                SQLPetHandler.getInstance().saveToDatabase(pi, false);
                PetHandler.getInstance().removePet(pi, false);
                if (!WorldUtil.allowPets(event.getTo())) {
                    Lang.sendTo(p, Lang.PETS_DISABLED_HERE.toString().replace("%world%", StringUtil.capitalise(event.getTo().getWorld().getName())));
                    return;
                }
                Lang.sendTo(p, Lang.DIMENSION_CHANGE.toString());
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        PetHandler.getInstance().loadPets(p, false, false, false);
                    }

                }.runTaskLater(EchoPetPlugin.getInstance(), 20L);*/
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        Pet pi = PetHandler.getInstance().getPet(p);
        if (pi != null) {
            //ec.PH.saveFileData("autosave", pi);
            PetHandler.getInstance().saveFileData("autosave", pi);
            SQLPetHandler.getInstance().saveToDatabase(pi, false);
            PetHandler.getInstance().removePet(pi, true);
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().isSimilar(SelectorLayout.getSelectorItem()) && !(ConfigOptions.instance.getConfig().getBoolean("petSelector.allowDrop", true))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        EchoPetPlugin ec = EchoPetPlugin.getInstance();
        final Player p = event.getPlayer();
        Inventory inv = p.getInventory();
        if (ec.update && p.hasPermission("echopet.update")) {
            p.sendMessage(ec.prefix + ChatColor.GOLD + "An update is available: " + ec.name + " (" + ec.size + " bytes).");
            p.sendMessage(ec.prefix + ChatColor.GOLD + "Type /ecupdate to update.");
        }

        for (ItemStack item : inv.getContents()) {
            if (item != null && item.isSimilar(SelectorLayout.getSelectorItem())) {
                inv.remove(item);
            }
        }

        if (ec.options.getConfig().getBoolean("petSelector.clearInvOnJoin", false)) {
            inv.clear();
        }
        if (ec.options.getConfig().getBoolean("petSelector.giveOnJoin.enable", true)
                && ((ec.options.getConfig().getBoolean("petSelector.giveOnJoin.usePerm", false) && p.hasPermission(ec.options.getConfig().getString("petSelector.giveOnJoin.perm", "echopet.selector.join")))
                || !(ec.options.getConfig().getBoolean("petSelector.giveOnJoin.usePerm", false)))) {
            int slot = (ec.options.getConfig().getInt("petSelector.giveOnJoin.slot", 9)) - 1;
            ItemStack i = inv.getItem(slot);
            ItemStack selector = SelectorLayout.getSelectorItem();
            if (i != null) {
                inv.clear(slot);
                inv.setItem(slot, selector);
                inv.addItem(i);
            } else {
                inv.setItem(slot, selector);
            }
        }


        final boolean sendMessage = ec.options.getConfig().getBoolean("sendLoadMessage", true);

        new BukkitRunnable() {

            @Override
            public void run() {
                if (p != null && p.isOnline()) {
                    Pet pet = PetHandler.getInstance().loadPets(p, true, sendMessage, false);
                    if (pet != null) {
                        if (EchoPetPlugin.getInstance().getVanishProvider().isVanished(p)) {
                            pet.getEntityPet().setShouldVanish(true);
                            pet.getEntityPet().setInvisible(true);
                        }
                    }
                }
            }

        }.runTaskLater(ec, 20);

        Iterator<Pet> i = PetHandler.getInstance().getPets().iterator();
        while (i.hasNext()) {
            Pet pet = i.next();
            if (pet.getEntityPet() instanceof IEntityPacketPet && ((IEntityPacketPet) pet.getEntityPet()).hasInititiated()) {
                if (GeometryUtil.getNearbyEntities(event.getPlayer().getLocation(), 50).contains(pet)) {
                    ((IEntityPacketPet) pet.getEntityPet()).updatePosition();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        Pet pet = PetHandler.getInstance().getPet(p);
        if (pet != null) {
            PetHandler.getInstance().saveFileData("autosave", pet);
            SQLPetHandler.getInstance().saveToDatabase(pet, false);
            PetHandler.getInstance().removePet(pet, true);
            //p.sendMessage(Lang.REMOVE_PET_DEATH.toString());
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        PetHandler.getInstance().loadPets(p, true, false, true);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        final Player p = event.getPlayer();
        final Pet pi = PetHandler.getInstance().getPet(p);
        if (pi != null) {
            PetHandler.getInstance().saveFileData("autosave", pi);
            SQLPetHandler.getInstance().saveToDatabase(pi, false);
            PetHandler.getInstance().removePet(pi, false);
            new BukkitRunnable() {

                @Override
                public void run() {
                    PetHandler.getInstance().loadPets(p, false, false, false);
                }

            }.runTaskLater(EchoPetPlugin.getInstance(), 20L);
        }
    }
}
