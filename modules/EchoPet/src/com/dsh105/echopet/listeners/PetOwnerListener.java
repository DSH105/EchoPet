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

package com.dsh105.echopet.listeners;

import com.dsh105.commodus.GeometryUtil;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.compat.api.config.ConfigOptions;
import com.dsh105.echopet.compat.api.entity.IEntityPacketPet;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.event.PetInteractEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.Lang;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import com.dsh105.echopet.compat.api.util.WorldUtil;
import com.dsh105.echopet.compat.api.util.menu.SelectorLayout;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
            SelectorLayout.getSelectorMenu().showTo(p);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        Entity e = event.getRightClicked();
        if (p.getItemInHand() != null && p.getItemInHand().isSimilar(SelectorLayout.getSelectorItem())) {
            SelectorLayout.getSelectorMenu().showTo(p);
            event.setCancelled(true);
            return;
        }

        if (ReflectionUtil.getEntityHandle(e) instanceof IEntityPet) {
            IPet pet = ((IEntityPet) ReflectionUtil.getEntityHandle(e)).getPet();
            event.setCancelled(true);
            PetInteractEvent iEvent = new PetInteractEvent(pet, p, PetInteractEvent.Action.RIGHT_CLICK, false);
            EchoPet.getPlugin().getServer().getPluginManager().callEvent(iEvent);
            if (!iEvent.isCancelled()) {
                pet.getEntityPet().onInteract(p);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                IPet pet = EchoPet.getManager().getPet(p);
                if (pet != null && pet.isOwnerRiding()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        final Player p = event.getPlayer();
        final IPet pi = EchoPet.getManager().getPet(p);
        Iterator<IPet> i = EchoPet.getManager().getPets().iterator();
        while (i.hasNext()) {
            IPet pet = i.next();
            if (pet.getEntityPet() instanceof IEntityPacketPet && ((IEntityPacketPet) pet.getEntityPet()).hasInititiated()) {
                if (GeometryUtil.getNearbyEntities(event.getTo(), 50).contains(pet)) {
                    ((IEntityPacketPet) pet.getEntityPet()).updatePosition();
                }
            }
        }
        if (pi != null) {
            if (!WorldUtil.allowPets(event.getTo())) {
                Lang.sendTo(p, Lang.PETS_DISABLED_HERE.toString().replace("%world%", StringUtil.capitalise(event.getTo().getWorld().getName())));
                EchoPet.getManager().saveFileData("autosave", pi);
                EchoPet.getSqlManager().saveToDatabase(pi, false);
                EchoPet.getManager().removePet(pi, false);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        IPet pi = EchoPet.getManager().getPet(p);
        if (pi != null) {
            //ec.PH.saveFileData("autosave", pi);
            EchoPet.getManager().saveFileData("autosave", pi);
            EchoPet.getSqlManager().saveToDatabase(pi, false);
            EchoPet.getManager().removePet(pi, true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().isSimilar(SelectorLayout.getSelectorItem()) && !(ConfigOptions.instance.getConfig().getBoolean("petSelector.allowDrop", true))) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player p = event.getPlayer();
        Inventory inv = p.getInventory();
        if (EchoPet.getPlugin().isUpdateAvailable() && p.hasPermission("echopet.update")) {
            p.sendMessage(EchoPet.getPrefix() + ChatColor.GOLD + "An update is available: " + EchoPet.getPlugin().getUpdateName() + " (" + EchoPet.getPlugin().getUpdateSize() + " bytes).");
            p.sendMessage(EchoPet.getPrefix() + ChatColor.GOLD + "Type /ecupdate to update.");
        }

        for (ItemStack item : inv.getContents()) {
            if (item != null && item.isSimilar(SelectorLayout.getSelectorItem())) {
                inv.remove(item);
            }
        }

        if (EchoPet.getConfig().getBoolean("petSelector.clearInvOnJoin", false)) {
            inv.clear();
        }
        if (EchoPet.getConfig().getBoolean("petSelector.giveOnJoin.enable", true)
                && ((EchoPet.getConfig().getBoolean("petSelector.giveOnJoin.usePerm", false) && p.hasPermission(EchoPet.getConfig().getString("petSelector.giveOnJoin.perm", "echopet.selector.join")))
                || !(EchoPet.getConfig().getBoolean("petSelector.giveOnJoin.usePerm", false)))) {
            int slot = (EchoPet.getConfig().getInt("petSelector.giveOnJoin.slot", 9)) - 1;
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


        final boolean sendMessage = EchoPet.getConfig().getBoolean("sendLoadMessage", true);

        new BukkitRunnable() {

            @Override
            public void run() {
                if (p != null && p.isOnline()) {
                    IPet pet = EchoPet.getManager().loadPets(p, true, sendMessage, false);
                    if (pet != null) {
                        if (EchoPet.getPlugin().getVanishProvider().isVanished(p)) {
                            pet.getEntityPet().setShouldVanish(true);
                            pet.getEntityPet().setInvisible(true);
                        }
                    }
                }
            }

        }.runTaskLater(EchoPet.getPlugin(), 20);

        Iterator<IPet> i = EchoPet.getManager().getPets().iterator();
        while (i.hasNext()) {
            IPet pet = i.next();
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
        IPet pet = EchoPet.getManager().getPet(p);
        if (pet != null) {
            EchoPet.getManager().saveFileData("autosave", pet);
            EchoPet.getSqlManager().saveToDatabase(pet, false);
            EchoPet.getManager().removePet(pet, true);
            //p.sendMessage(Lang.REMOVE_PET_DEATH.toString());
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        final Player p = event.getPlayer();
        new BukkitRunnable() {

            @Override
            public void run() {
                EchoPet.getManager().loadPets(p, true, false, true);
            }

        }.runTaskLater(EchoPet.getPlugin(), 20L);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        final Player p = event.getPlayer();
        final IPet pi = EchoPet.getManager().getPet(p);
        if (pi != null) {
            EchoPet.getManager().saveFileData("autosave", pi);
            EchoPet.getSqlManager().saveToDatabase(pi, false);
            EchoPet.getManager().removePet(pi, false);
            new BukkitRunnable() {

                @Override
                public void run() {
                    EchoPet.getManager().loadPets(p, false, false, false);
                }

            }.runTaskLater(EchoPet.getPlugin(), 20L);
        }
    }
}
