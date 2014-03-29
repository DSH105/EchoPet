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

import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.api.event.PetAttackEvent;
import io.github.dsh105.echopet.api.event.PetDamageEvent;
import io.github.dsh105.echopet.api.event.PetInteractEvent;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.CraftPet;
import io.github.dsh105.echopet.util.Lang;
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
        if (e instanceof CraftPet) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawnUnBlock(CreatureSpawnEvent event) {
        Entity e = event.getEntity();
        if (e instanceof CraftPet) {
            if (event.isCancelled()) {
                event.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onDismount(VehicleExitEvent event) {
        Entity e = event.getVehicle();
        if (e instanceof CraftPet) {
            if (((CraftPet) e).getPet().isOwnerRiding() && !((CraftPet) e).getPet().ownerIsMounting) {
                Lang.sendTo(((CraftPet) e).getPet().getOwner(), Lang.RIDE_PET_OFF.toString());
                ((CraftPet) e).getPet().ownerRidePet(false);
            }
        }
    }

    @EventHandler
    public void onPetEnterPortal(EntityPortalEvent event) {
        Entity e = event.getEntity();
        if (e instanceof CraftPet) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity e = event.getEntity();
        if (e instanceof CraftPet) {
            CraftPet craftPet = (CraftPet) e;
            PetDamageEvent damageEvent = new PetDamageEvent(craftPet.getPet(), event.getCause(), event.getDamage());
            EchoPetPlugin.getInstance().getServer().getPluginManager().callEvent(damageEvent);
            event.setDamage(damageEvent.getDamage());
            event.setCancelled(damageEvent.isCancelled());
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity e = event.getEntity();
        if (e instanceof CraftPet) {
            Entity damager = event.getDamager();
            if (damager instanceof Player) {
                PetInteractEvent iEvent = new PetInteractEvent(((CraftPet) e).getPet(), (Player) damager, PetInteractEvent.Action.LEFT_CLICK, true);
                EchoPetPlugin.getInstance().getServer().getPluginManager().callEvent(iEvent);
                event.setCancelled(iEvent.isCancelled());
            }
        } else if (event.getDamager() instanceof CraftPet) {
            CraftPet craftPet = (CraftPet) event.getDamager();
            PetAttackEvent attackEvent = new PetAttackEvent(craftPet.getPet(), e, event.getDamage());
            EchoPetPlugin.getInstance().getServer().getPluginManager().callEvent(attackEvent);
            event.setDamage(attackEvent.getDamage());
            event.setCancelled(attackEvent.isCancelled());
        }
    }

    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        Entity e = event.getEntity();
        if (e instanceof CraftPet) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockForm(EntityBlockFormEvent event) {
        Entity e = event.getEntity();
        if (e instanceof CraftPet && event.getNewState().getType().equals(Material.SNOW)) {
            event.setCancelled(true);
            event.getNewState().setType(Material.AIR);
        }
    }
}
