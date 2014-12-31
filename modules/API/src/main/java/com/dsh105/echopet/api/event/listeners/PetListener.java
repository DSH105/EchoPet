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
import com.dsh105.echopet.api.configuration.Lang;
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import com.dsh105.echopet.api.entity.entitypet.type.EntityEnderDragonPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.event.Listen;
import com.dsh105.echopet.api.event.NullSpongeEvent;
import com.dsh105.echopet.bridge.container.EventContainer;
import com.dsh105.echopet.util.PetUtil;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.spongepowered.api.event.entity.EntityChangeHealthEvent;
import org.spongepowered.api.event.entity.EntityDismountEvent;
import org.spongepowered.api.event.entity.EntitySpawnEvent;

public class PetListener {

    // TODO: clear drops on death?

    /*
     * Prevent everything else from cancelling pet spawning
     */

    @Listen(bukkit = CreatureSpawnEvent.class, sponge = EntitySpawnEvent.class)
    public void blockSpawn(EventContainer event) {
        if (PetUtil.isPetEntity(event.get("entity"))) {
            event.setCancelled(true);
        }
    }

    @Listen(bukkit = CreatureSpawnEvent.class, sponge = EntitySpawnEvent.class)
    public void allowSpawn(EventContainer event) {
        if (PetUtil.isPetEntity(event.get("entity"))) {
            event.setCancelled(false);
        }
    }

    /*
     * Other listeners
     */

    @Listen(bukkit = VehicleExitEvent.class, sponge = EntityDismountEvent.class)
    public void onDismount(EventContainer event) {
        Object entity = event.get("entity");
        if (PetUtil.isPetEntity(entity)) {
            Pet pet = ((EntityPet) entity).getPet();
            if (pet.isOwnerRiding() && !pet.isOwnerInMountingProcess()) {
                pet.setOwnerRiding(false);
                Lang.PET_RIDE_OFF.send(pet.getOwner(), "%name%", pet.getName());
            }
        }
    }

    @Listen(bukkit = EntityDamageByEntityEvent.class, sponge = EntityChangeHealthEvent.class)
    public void onDamage(EventContainer event) {
        // TODO
        Object entity = event.get("entity");
        if (PetUtil.isPetEntity(entity)) {
            Pet pet = ((EntityPet) BukkitUnwrapper.getInstance().unwrap(entity)).getPet();
            // TODO: call interact event..?
        }
    }

    // TODO: shoot bow, pig zap, sheep regrow wool, leash entity, combust
    // TODO: Sponge events aren't complete
    @Listen(
            bukkit = {
                    EntityExplodeEvent.class,
                    EntityPortalEnterEvent.class,
                    EntityInteractEvent.class,
                    org.bukkit.event.entity.EntityChangeBlockEvent.class,
                    EntityBlockFormEvent.class,
                    EntityTargetEvent.class,
                    ExplosionPrimeEvent.class,
                    SlimeSplitEvent.class,
                    org.bukkit.event.entity.EntityTameEvent.class
            },
            sponge = {
                    NullSpongeEvent.class,// TODO: perhaps EntityDeathEvent with a reason? EchoPet may already deal with this internally
                    NullSpongeEvent.class,
                    NullSpongeEvent.class,
                    org.spongepowered.api.event.entity.EntityChangeBlockEvent.class,
                    // above covers two Bukkit events
                    NullSpongeEvent.class, // target
                    NullSpongeEvent.class, // explosion prime
                    NullSpongeEvent.class, // slime split
                    org.spongepowered.api.event.entity.EntityTameEvent.class
            }
    )
    public void cancel(EventContainer event) {
        if (PetUtil.isPetEntity(event.get("entity"))) {
            event.setCancelled(true);
        }
    }

    @Listen(bukkit = EntityRegainHealthEvent.class, sponge = EntityChangeHealthEvent.class)
    public void onDragonRegainHealth(EventContainer event) {
        // ensure it is indeed a positive health change
        if (event.get(double.class) > 0 && PetUtil.isPetEntity(event.get("entity"), EntityEnderDragonPet.class)) {
            event.setCancelled(true);
        }
    }

    // TODO: ;\
    @Listen(bukkit = EntityExplodeEvent.class, sponge = NullSpongeEvent.class)
    public void onDragonExplode(EventContainer event) {
        // ensure it is indeed a positive health change
        if (PetUtil.isPetEntity(event.get("entity"), EntityEnderDragonPet.class)) {
            event.setCancelled(true);
        }
    }
}