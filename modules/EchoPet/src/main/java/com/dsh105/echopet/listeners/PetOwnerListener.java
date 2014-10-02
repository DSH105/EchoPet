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

import com.captainbern.minecraft.conversion.BukkitUnwrapper;
import com.dsh105.echopet.api.config.Lang;
import com.dsh105.echopet.api.config.MenuSettings;
import com.dsh105.echopet.api.config.Settings;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.entity.pet.type.HumanPet;
import com.dsh105.echopet.api.event.PetInteractEvent;
import com.dsh105.echopet.api.hook.VanishProvider;
import com.dsh105.echopet.api.inventory.PetSelector;
import com.dsh105.echopet.api.plugin.EchoPet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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

import java.util.List;

public class PetOwnerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (entity instanceof LivingEntity) {
            Object nmsEntity = BukkitUnwrapper.getInstance().unwrap(entity);
            if (nmsEntity instanceof EntityPet) {
                Pet pet = ((EntityPet) nmsEntity).getPet();
                event.setCancelled(true);
                PetInteractEvent interactEvent = new PetInteractEvent(pet, player, PetInteractEvent.Action.RIGHT_CLICK, false);
                EchoPet.getCore().getServer().getPluginManager().callEvent(interactEvent);
                if (!interactEvent.isCancelled()) {
                    pet.onInteract(player);
                    return;
                }
            }
        }

        if (player.getItemInHand() != null && player.getItemInHand().isSimilar(PetSelector.getLayout().getClickItem())) {
            PetSelector.getLayout().toMenu(EchoPet.getCore()).show(player);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                List<Pet> pets = EchoPet.getManager().getPetsFor(player);
                if (!pets.isEmpty()) {
                    for (Pet pet : pets) {
                        if (pet.isOwnerRiding()) {
                            event.setCancelled(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        EchoPet.getManager().removePets(player);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDropItem(PlayerDropItemEvent event) {
        if (!MenuSettings.SELECTOR_ALLOW_DROP.getValue() && event.getItemDrop().getItemStack().isSimilar(PetSelector.prepare().getClickItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (EchoPet.getCore().isUpdateAvailable() && player.hasPermission("echopet.update")) {
            Lang.UPDATE_AVAILABLE.send(player);
        }

        Inventory inventory = player.getInventory();

        if (MenuSettings.SELECTOR_ONJOIN_CLEAR.getValue()) {
            inventory.clear();
        } else if (inventory.contains(PetSelector.prepare().getClickItem())) {
            inventory.remove(PetSelector.prepare().getClickItem());
        }

        if (inventory.getContents().length < inventory.getSize()) {
            if (MenuSettings.SELECTOR_ONJOIN_ENABLE.getValue() && (!MenuSettings.SELECTOR_ONJOIN_USE_PERM.getValue() || player.hasPermission(MenuSettings.SELECTOR_ONJOIN_PERM.getValue()))) {
                int slot = MenuSettings.SELECTOR_ONJOIN_SLOT.getValue();
                ItemStack existing = inventory.getItem(slot);
                inventory.setItem(slot, PetSelector.prepare().getClickItem());
                if (existing != null) {
                    inventory.addItem(existing);
                }
            }
        }

        for (Pet pet : EchoPet.getManager().getPetsOfType(PetType.HUMAN)) {
            ((HumanPet) pet).updatePosition();
        }


        final boolean sendMessage = EchoPet.getConfig().getBoolean("sendLoadMessage", true);

        new BukkitRunnable() {

            @Override
            public void run() {
                if (player != null && player.isOnline()) {
                    List<Pet> pets = EchoPet.getManager().load(player, Settings.SEND_LOAD_MESSAGE.getValue());
                    if (!pets.isEmpty()) {
                        if (EchoPet.getCore().getProvider(VanishProvider.class).isVanished(player)) {
                            for (Pet pet : pets) {
                                pet.setShouldVanish(true);
                                pet.getEntity().setInvisible(true);
                            }
                        }
                    }
                }
            }

        }.runTaskLater(EchoPet.getCore(), 20L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        EchoPet.getManager().removePets(event.getEntity());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        new BukkitRunnable() {

            @Override
            public void run() {
                if (player != null && player.isOnline()) {
                    EchoPet.getManager().load(player, false);
                }
            }

        }.runTaskLater(EchoPet.getCore(), 20L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        EchoPet.getManager().removePets(player);
        new BukkitRunnable() {

            @Override
            public void run() {
                if (player != null && player.isOnline()) {
                    EchoPet.getManager().load(player, false);
                }
            }

        }.runTaskLater(EchoPet.getCore(), 20L);
    }
}