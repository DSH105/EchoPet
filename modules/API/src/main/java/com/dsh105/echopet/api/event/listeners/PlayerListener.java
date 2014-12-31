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

package com.dsh105.echopet.api.event.listeners;

import com.captainbern.minecraft.conversion.BukkitUnwrapper;
import com.dsh105.commodus.container.ItemStackContainer;
import com.dsh105.echopet.api.configuration.Lang;
import com.dsh105.echopet.api.configuration.MenuSettings;
import com.dsh105.echopet.api.configuration.Settings;
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.event.Listen;
import com.dsh105.echopet.api.event.NullSpongeEvent;
import com.dsh105.echopet.api.hook.VanishNoPacketDependency;
import com.dsh105.echopet.api.inventory.PetSelector;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.api.plugin.LoadCallback;
import com.dsh105.echopet.bridge.*;
import com.dsh105.echopet.bridge.container.EventContainer;
import com.dsh105.echopet.util.PetUtil;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spongepowered.api.event.entity.EntityChangeHealthEvent;
import org.spongepowered.api.event.player.PlayerChangeWorldEvent;

import java.util.Iterator;
import java.util.List;

public class PlayerListener {

    @Listen(bukkit = org.bukkit.event.player.PlayerJoinEvent.class, sponge = org.spongepowered.api.event.player.PlayerJoinEvent.class)
    public void onJoin(EventContainer event) {
        final PlayerBridge player = event.get(PlayerBridge.class);
        if (EchoPet.getCore().isUpdateAvailable() && EchoPet.getBridge(MessageBridge.class).isPermitted(player.get(), "echopet.update")) {
            Lang.UPDATE_AVAILABLE.send(player);
        }

        Inventory inventory = player.getInventory();

        if (MenuSettings.SELECTOR_ONJOIN_CLEAR.getValue()) {
            inventory.clear();
        } else if (inventory.contains(PetSelector.prepare().getClickItem())) {
            inventory.remove(PetSelector.prepare().getClickItem());
        }

        if (inventory.getContents().length < inventory.getSize()) {
            if (MenuSettings.SELECTOR_ONJOIN_ENABLE.getValue() && (!MenuSettings.SELECTOR_ONJOIN_USE_PERM.getValue() || player.isPermitted(MenuSettings.SELECTOR_ONJOIN_PERM.getValue()))) {
                int slot = MenuSettings.SELECTOR_ONJOIN_SLOT.getValue();
                ItemStack existing = inventory.getItem(slot);
                inventory.setItem(slot, PetSelector.prepare().getClickItem());
                if (existing != null) {
                    inventory.addItem(existing);
                }
            }
        }

        // TODO: human pets?
        /*for (Pet pet : EchoPet.getManager().getPetsOfType(PetType.HUMAN)) {
            ((HumanPet) pet).updatePosition();
        }*/

        EchoPet.getBridge(SchedulerBridge.class).runLater(false, 20L, new Runnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    LoadCallback<List<Pet>> callback = null;
                    if (EchoPet.getDependency(VanishNoPacketDependency.class) != null) {
                        callback = new LoadCallback<List<Pet>>() {
                            @Override
                            public void call(List<Pet> loadedPets) {
                                if (!loadedPets.isEmpty()) {
                                    if (EchoPet.getDependency(VanishNoPacketDependency.class).isVanished(player.getName())) {
                                        for (Pet pet : loadedPets) {
                                            pet.setShouldVanish(true);
                                            pet.getModifier().setInvisible(true);
                                        }
                                    }
                                }
                            }
                        };
                    }

                    EchoPet.getManager().load(player.getUID(), Settings.SEND_LOAD_MESSAGE.getValue(), callback);
                }
            }
        });
    }

    @Listen(
            bukkit = {
                    org.bukkit.event.player.PlayerQuitEvent.class,
                    org.bukkit.event.entity.PlayerDeathEvent.class
            },
            sponge = {
                    org.spongepowered.api.event.player.PlayerQuitEvent.class,
                    org.spongepowered.api.event.player.PlayerDeathEvent.class
            }
    )
    public void onRemove(EventContainer event) {
        EchoPet.getManager().removePets(event.get(PlayerBridge.class).getUID());
    }

    // TODO: no sponge event?
    @Listen(bukkit = PlayerRespawnEvent.class, sponge = NullSpongeEvent.class)
    public void onRespawn(final EventContainer event) {
        EchoPet.getBridge(SchedulerBridge.class).runLater(false, 20L, new Runnable() {
            @Override
            public void run() {
                PlayerBridge player = event.get(PlayerBridge.class);
                if (player.isOnline()) {
                    EchoPet.getManager().load(player.getUID(), false);
                }
            }
        });
    }

    @Listen(bukkit = PlayerChangedWorldEvent.class, sponge = PlayerChangeWorldEvent.class)
    public void onWorldChange(final EventContainer event) {
        final PlayerBridge player = event.get(PlayerBridge.class);
        EchoPet.getManager().removePets(player.getUID());
        EchoPet.getBridge(SchedulerBridge.class).runLater(false, 20L, new Runnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    EchoPet.getManager().loadPets(player.getUID());
                }
            }
        });

    }

    @Listen(bukkit = EntityDamageEvent.class, sponge = EntityChangeHealthEvent.class)
    public void onDamage(EventContainer event) {
        PlayerBridge player = event.get(PlayerBridge.class);
        // is fall damage
        if (event.get(boolean.class)) {
            List<Pet> pets = EchoPet.getManager().getPetsFor(player.getUID());
            if (!pets.isEmpty()) {
                for (Pet pet : pets) {
                    // Cancel fall damage when the owner is riding the pet
                    if (pet.isOwnerRiding()) {
                        event.setCancelled(true);
                        break;
                    }
                }
            }
        }

        // TODO: other events on left click?
    }

    @Listen(bukkit = org.bukkit.event.player.PlayerInteractEntityEvent.class, sponge = org.spongepowered.api.event.player.PlayerInteractEntityEvent.class)
    public void onInteract(EventContainer event) {
        PlayerBridge player = event.get(PlayerBridge.class);
        Object entity = event.get("entity");
        ItemStackContainer itemInHand = event.get(ItemStackContainer.class); // TODO: get(ItemContainer.class)

        // TODO: basic itemstack container?
        if (itemInHand != null && itemInHand.isSimilar(PetSelector.getInventory().getInteractIcon().getStack())) {
            PetSelector.getInventory().show(player);
            event.setCancelled(true);
            return;
        }

        if (PetUtil.isPetEntity(entity)) {
            event.setCancelled(true);
            EntityPet entityPet = (EntityPet) BukkitUnwrapper.getInstance().unwrap(entity);
            entityPet.getPet().onInteract(player);
        }
    }

    @Listen(bukkit = org.bukkit.event.player.PlayerDropItemEvent.class, sponge = org.spongepowered.api.event.player.PlayerDropItemEvent.class)
    public void onDropItem(EventContainer event) {
        if (!MenuSettings.SELECTOR_ALLOW_DROP.getValue()) {
            ItemStackContainer single = event.get(ItemStackContainer.class);
            ItemStackContainer selector = PetSelector.getInventory().getInteractIcon().getStack();
            if (single != null) {
                if (single.isSimilar(selector)) {
                    event.setCancelled(true);
                }

            }

            List<ItemStackContainer> stacks = event.get(List.class);
            Iterator<ItemStackContainer> iterator = stacks.iterator();
            while (iterator.hasNext()) {
                ItemStackContainer stack = iterator.next();
                if (stack.isSimilar(PetSelector.getInventory().getInteractIcon().getStack()) {
                    iterator.remove();
                }
            }
        }
    }
}