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

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.Transformer;
import com.dsh105.commodus.container.ItemStackContainer;
import com.dsh105.commodus.container.PositionContainer;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.bridge.PlayerBridge;
import com.dsh105.echopet.bridge.container.EventContainer;
import org.spongepowered.api.entity.EntityInteractionType;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.*;
import org.spongepowered.api.event.player.*;
import org.spongepowered.api.event.world.ChunkUnloadEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.event.Order;
import org.spongepowered.api.util.event.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpongeListener {

    /*
     * Chunk listeners
     */

    @Subscribe(order = Order.POST)
    public void onChunkUnload(ChunkUnloadEvent event) {
        EchoPet.getEventManager().post(
                EventContainer.from(event)
                        .put(List.class, Arrays.asList(event.getChunk().getEntities()))
        );
    }

    /*
     * Player listeners
     */

    @Subscribe(order = Order.POST)
    public void onJoin(PlayerJoinEvent event) {
        // TODO: Port to Sponge inventory API (not yet complete)
        /*PlayerBridge player = PlayerBridge.of(event.getPlayer());
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
        }*/
        
        EchoPet.getEventManager().post(
                EventContainer.from(event)
                        .put(PlayerBridge.class, PlayerBridge.of(event.getPlayer()))
        );
    }

    @Subscribe(order = Order.POST)
    public void onQuit(PlayerQuitEvent event) {
        EchoPet.getEventManager().post(
                EventContainer.from(event)
                        .put(PlayerBridge.class, PlayerBridge.of(event.getPlayer()))
        );
    }

    @Subscribe(order = Order.POST)
    public void onDeath(PlayerDeathEvent event) {
        EchoPet.getEventManager().post(
                EventContainer.from(event)
                        .put(PlayerBridge.class, PlayerBridge.of(event.getPlayer()))
        );
    }
    
    // TODO: respawn

    @Subscribe(order = Order.POST)
    public void onWorldChange(PlayerChangeWorldEvent event) {
        EchoPet.getEventManager().post(
                EventContainer.from(event)
                        .put(PlayerBridge.class, PlayerBridge.of(event.getPlayer()))
        );
    }

    @Subscribe(order = Order.LAST)
    public void onPlayerDamage(EntityChangeHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            boolean fallDamage = false;
            if (event.getCause().isPresent()) {
                if (event.getCause().get().getReason().isPresent()) {
                    fallDamage = false; // FIXME
                }
            }
            EchoPet.getEventManager().post(
                    EventContainer.from(event)
                            .put(PlayerBridge.class, PlayerBridge.of((Player) event.getEntity()))
                            .put(boolean.class, fallDamage)
            );
        }
    }

    @Subscribe(order = Order.LAST)
    public void onInteract(PlayerInteractEntityEvent event) {
        if (event.getInteractionType() == EntityInteractionType.RIGHT_CLICK) {
            EchoPet.getEventManager().post(
                    EventContainer.from(event)
                            .put(PlayerBridge.class, PlayerBridge.of(event.getPlayer()))
                            .put("entity", event.getEntity())
                            .put(ItemStackContainer.class, ItemStackContainer.of(event.getPlayer().getItemInHand().orNull()))
            );
        }
    }

    @Subscribe
    public void onDropItem(PlayerDropItemEvent event) {
        EchoPet.getEventManager().post(
                EventContainer.from(event)
                        .put(PlayerBridge.class, PlayerBridge.of(event.getPlayer()))
                        .put(List.class, GeneralUtil.transform(new ArrayList<>(event.getDroppedStacks()), new Transformer<ItemStack, ItemStackContainer>() {
                            @Override
                            public ItemStackContainer transform(ItemStack transmutable) {
                                return ItemStackContainer.of(transmutable);
                            }
                        }))
        );
    }

    /*
     * Pet listeners
     */


    @Subscribe(order = Order.FIRST)
    public void blockSpawn(EntitySpawnEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getEntity()));
    }

    @Subscribe(order = Order.LAST)
    public void allowSpawn(EntitySpawnEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getEntity()));
    }

    @Subscribe(order = Order.POST)
    public void onDismount(EntityDismountEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getDismounted()));
    }

    // TODO: EntityExplodeEvent

    // TODO: EntityPortalEnterEvent

    // TODO: EntityInteractEvent

    @Subscribe(order = Order.LAST)
    public void onChangeBlock(EntityChangeBlockEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getEntity()));
    }

    // TODO: EntityTargetEvent

    // TODO: ExplosionPrimeEvent

    // TODO: SlimeSplitEvent

    @Subscribe(order = Order.LAST)
    public void onTame(EntityTameEvent event) {
        EchoPet.getEventManager().post(EventContainer.from(event).put("entity", event.getEntity()));
    }

    @Subscribe(order = Order.LAST)
    public void onChangeHealth(EntityChangeHealthEvent event) {
        EchoPet.getEventManager().post(
                EventContainer.from(event)
                        .put("entity", event.getEntity())
                        .put(double.class, event.getNewHealth() - event.getOldHealth())
        );
    }

    // TODO: Registered via SpongeWorldGuardDependency
    public static class RegionListener {

        @Subscribe(order = Order.POST)
        public void onPlayerMove(PlayerMoveEvent event) {
            EchoPet.getEventManager().post(
                    EventContainer.from(event)
                            .put(PlayerBridge.class, PlayerBridge.of(event.getPlayer()))
                            .put(PositionContainer.class, PositionContainer.from(event.getNewLocation()))
            );
        }
    }
}