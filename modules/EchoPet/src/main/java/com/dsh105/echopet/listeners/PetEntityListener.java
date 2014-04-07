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

import com.dsh105.echopet.compat.api.entity.ICraftPet;
import com.dsh105.echopet.compat.api.event.PetAttackEvent;
import com.dsh105.echopet.compat.api.event.PetDamageEvent;
import com.dsh105.echopet.compat.api.event.PetInteractEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class PetEntityListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity e = event.getEntity();
        if (e instanceof ICraftPet) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawnUnBlock(CreatureSpawnEvent event) {
        Entity e = event.getEntity();
        if (e instanceof ICraftPet) {
            if (event.isCancelled()) {
                event.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onDismount(VehicleExitEvent event) {
        Entity e = event.getVehicle();
        if (e instanceof ICraftPet) {
            if (((ICraftPet) e).getPet().isOwnerRiding() && !((ICraftPet) e).getPet().isOwnerInMountingProcess()) {
                Lang.sendTo(((ICraftPet) e).getPet().getOwner(), Lang.RIDE_PET_OFF.toString());
                ((ICraftPet) e).getPet().ownerRidePet(false);
            }
        }
    }

    @EventHandler
    public void onPetEnterPortal(EntityPortalEvent event) {
        Entity e = event.getEntity();
        if (e instanceof ICraftPet) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity e = event.getEntity();
        if (e instanceof ICraftPet) {
            ICraftPet craftPet = (ICraftPet) e;
            PetDamageEvent damageEvent = new PetDamageEvent(craftPet.getPet(), event.getCause(), event.getDamage());
            EchoPet.getPlugin().getServer().getPluginManager().callEvent(damageEvent);
            event.setDamage(damageEvent.getDamage());
            event.setCancelled(damageEvent.isCancelled());
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity e = event.getEntity();
        if (e instanceof ICraftPet) {
            Entity damager = event.getDamager();
            if (damager instanceof Player) {
                PetInteractEvent iEvent = new PetInteractEvent(((ICraftPet) e).getPet(), (Player) damager, PetInteractEvent.Action.LEFT_CLICK, true);
                EchoPet.getPlugin().getServer().getPluginManager().callEvent(iEvent);
                event.setCancelled(iEvent.isCancelled());
            }
        } else if (event.getDamager() instanceof ICraftPet) {
            ICraftPet craftPet = (ICraftPet) event.getDamager();
            PetAttackEvent attackEvent = new PetAttackEvent(craftPet.getPet(), e, event.getDamage());
            EchoPet.getPlugin().getServer().getPluginManager().callEvent(attackEvent);
            event.setDamage(attackEvent.getDamage());
            event.setCancelled(attackEvent.isCancelled());
        }
    }

    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        Entity e = event.getEntity();
        if (e instanceof ICraftPet) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockForm(EntityBlockFormEvent event) {
        Entity e = event.getEntity();
        if (e instanceof ICraftPet && event.getNewState().getType().equals(Material.SNOW)) {
            event.setCancelled(true);
            event.getNewState().setType(Material.AIR);
        }
    }
}
