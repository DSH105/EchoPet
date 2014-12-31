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

package com.dsh105.echopet.api.event.listeners.platform;

import com.dsh105.commodus.container.ItemStackContainer;
import com.dsh105.commodus.container.PositionContainer;
import com.dsh105.echopet.api.configuration.MenuSettings;
import com.dsh105.echopet.api.inventory.PetSelector;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.bridge.Ident;
import com.dsh105.echopet.bridge.PlayerBridge;
import com.dsh105.echopet.bridge.container.EventContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kitteh.vanish.event.VanishStatusChangeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BukkitListener implements Listener {

    /*
     * Chunk listeners
     */

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put(List.class, Arrays.asList(event.getChunk().getEntities())));
    }

    /*
     * Player listeners
     */

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        PlayerBridge player = PlayerBridge.of(event.getPlayer());
        Inventory inventory = event.getPlayer().getInventory();

        ItemStack selector = PetSelector.getInventory().getInteractIcon().asBukkit();
        if (MenuSettings.SELECTOR_ONJOIN_CLEAR.getValue()) {
            inventory.clear();
        } else if (inventory.contains(selector)) {
            inventory.remove(selector);
        }

        if (inventory.getContents().length < inventory.getSize()) {
            if (MenuSettings.SELECTOR_ONJOIN_ENABLE.getValue() && (!MenuSettings.SELECTOR_ONJOIN_USE_PERM.getValue() || player.isPermitted(MenuSettings.SELECTOR_ONJOIN_PERM.getValue()))) {
                int slot = MenuSettings.SELECTOR_ONJOIN_SLOT.getValue();
                ItemStack existing = inventory.getItem(slot);
                inventory.setItem(slot, selector);
                if (existing != null) {
                    inventory.addItem(existing);
                }
            }
        }
        
        EchoPet.getEventManager().post(
                EventContainer.from(event)
                        .put(PlayerBridge.class, player)
        );
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        EchoPet.getEventManager().post(
                EventContainer.from(event)
                        .put(PlayerBridge.class, PlayerBridge.of(event.getPlayer()))
        );
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        EchoPet.getEventManager().post(
                EventContainer.from(event)
                        .put(PlayerBridge.class, PlayerBridge.of(event.getEntity()))
        );
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRespawn(PlayerRespawnEvent event) {
        EchoPet.getEventManager().post(
                EventContainer.from(event)
                        .put(PlayerBridge.class, PlayerBridge.of(event.getPlayer()))
        );
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        EchoPet.getEventManager().post(
                EventContainer.from(event)
                        .put(PlayerBridge.class, PlayerBridge.of(event.getPlayer()))
        );
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            EchoPet.getEventManager().post(
                    EventContainer.from(event)
                            .put(PlayerBridge.class, PlayerBridge.of((Player) event.getEntity()))
                            .put(boolean.class, event.getCause() == EntityDamageEvent.DamageCause.FALL)
            );
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEntityEvent event) {
        EchoPet.getEventManager().post(
                EventContainer.from(event)
                        .put(PlayerBridge.class, PlayerBridge.of(event.getPlayer()))
                        .put("entity", event.getRightClicked())
                        .put(ItemStackContainer.class, ItemStackContainer.of(event.getPlayer().getItemInHand()))
        );
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDropItem(PlayerDropItemEvent event) {
        EchoPet.getEventManager().post(
                EventContainer.from(event)
                        .put(PlayerBridge.class, PlayerBridge.of(event.getPlayer()))
                        .put(ItemStackContainer.class, ItemStackContainer.of(event.getItemDrop().getItemStack()))
        );
    }

    /*
     * Pet listeners
     */

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void blockSpawn(CreatureSpawnEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getEntity()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void allowSpawn(CreatureSpawnEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getEntity()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDismount(VehicleExitEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getExited()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getEntity()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getEntity()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPortal(EntityPortalEnterEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getEntity()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(EntityInteractEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getEntity()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChangeBlock(EntityChangeBlockEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getEntity()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockForm(EntityBlockFormEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getEntity()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTarget(EntityTargetEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getEntity()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getEntity()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSlimeSplit(SlimeSplitEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getEntity()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTame(EntityTameEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getEntity()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onRegainHealth(EntityRegainHealthEvent event) {
        EchoPet.getEventManager().post(
                EventContainer.from(event)
                        .put("entity", event.getEntity())
                        .put(double.class, event.getAmount())
        );
    }


    /*
     * Dependency listeners
     */

    // Registered via BukkitWorldGuardDependency
    public static class RegionListener implements Listener {

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onPlayerMove(PlayerMoveEvent event) {
            EchoPet.getEventManager().post(
                    EventContainer.from(event)
                            .put(PlayerBridge.class, PlayerBridge.of(event.getPlayer()))
                            .put(PositionContainer.class, PositionContainer.from(event.getTo()))
            );
        }
    }

    // Registered via BukkitVanishNoPacketDependency
    public static class VanishListener implements Listener {

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onVanish(VanishStatusChangeEvent event) {
            EchoPet.getEventManager().post(
                    EventContainer.from(event)
                            .put(UUID.class, Ident.get().getUID(event.getPlayer()))
                            .put(boolean.class, event.isVanishing())
            );
        }
    }
}