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
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import com.dsh105.echopet.api.entity.entitypet.type.EntityEnderDragonPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.event.bukkit.PetInteractEvent;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.api.plugin.EchoPetAPI;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.vehicle.VehicleExitEvent;

/**
 * Covers all Pet events
 * <p/>
 * Listeners are set to {@link org.bukkit.event.EventPriority#HIGHEST} - other plugins shouldn't have a say in what
 * happens unless it is through a custom EchoPet event
 */
public class PetListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (EchoPetAPI.isPetEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawnUnBlock(CreatureSpawnEvent event) {
        if (EchoPetAPI.isPetEntity(event.getEntity()) && event.isCancelled()) {
            event.setCancelled(false);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDismount(VehicleExitEvent event) {
        Entity entity = event.getVehicle();
        Object nmsEntity = BukkitUnwrapper.getInstance().unwrap(entity);
        if (nmsEntity instanceof EntityPet) {
            Pet pet = ((EntityPet) nmsEntity).getPet();
            if (pet.isOwnerRiding() && !pet.isOwnerInMountingProcess()) {
                Lang.PET_RIDE_OFF.send(pet.getOwner(), pet.getName());
                pet.setOwnerRiding(false);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPetEnterPortal(EntityPortalEvent event) {
        if (EchoPetAPI.isPetEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            Object nmsEntity = BukkitUnwrapper.getInstance().unwrap(entity);
            if (nmsEntity instanceof EntityPet) {
                Pet pet = ((EntityPet) nmsEntity).getPet();
                Entity damager = event.getDamager();
                if (damager instanceof Player) {
                    PetInteractEvent interactEvent = new PetInteractEvent(pet, (Player) damager, PetInteractEvent.Action.LEFT_CLICK, true);
                    EchoPet.getCore().getServer().getPluginManager().callEvent(interactEvent);
                    event.setCancelled(interactEvent.isCancelled());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityInteract(EntityInteractEvent event) {
        if (EchoPetAPI.isPetEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockForm(EntityBlockFormEvent event) {
        if (event.getNewState().getType().equals(Material.SNOW) && EchoPetAPI.isPetEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            if (BukkitUnwrapper.getInstance().unwrap(entity) instanceof EntityEnderDragonPet) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (EchoPetAPI.isPetEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent event) {
        if (EchoPetAPI.isPetEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if (EchoPetAPI.isPetEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            if (BukkitUnwrapper.getInstance().unwrap(entity) instanceof EntityEnderDragonPet) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSlimeSplit(SlimeSplitEvent event) {
        if (EchoPetAPI.isPetEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if (EchoPetAPI.isPetEntity(event.getEntity())) {
            event.setDroppedExp(0);
            event.getDrops().clear();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityTame(EntityTameEvent event) {
        if (EchoPetAPI.isPetEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (EchoPetAPI.isPetEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPigZap(PigZapEvent event) {
        if (EchoPetAPI.isPetEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSheepRegrowWool(SheepRegrowWoolEvent event) {
        if (EchoPetAPI.isPetEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        if (EchoPetAPI.isPetEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityCombust(EntityCombustEvent event) {
        if (EchoPetAPI.isPetEntity(event.getEntity())) {
            event.setCancelled(true);
        }
    }
}