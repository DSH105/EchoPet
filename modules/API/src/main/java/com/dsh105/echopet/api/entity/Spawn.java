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

package com.dsh105.echopet.api.entity;

import com.captainbern.minecraft.reflection.MinecraftReflection;
import com.captainbern.reflection.Reflection;
import com.dsh105.echopet.api.config.Lang;
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.event.PetPreSpawnEvent;
import com.dsh105.echopet.api.plugin.EchoPet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class Spawn {

    public static <T extends LivingEntity, S extends EntityPet> S spawn(Pet<T, S> pet) {
        PetPreSpawnEvent spawnEvent = new PetPreSpawnEvent(pet, pet.getOwner().getLocation());
        Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
        if (spawnEvent.isCancelled()) {
            EchoPet.getManager().removePet(pet);
            return null;
        }
        Location spawnLocation = spawnEvent.getSpawnLocation();
        System.out.println(MinecraftReflection.getCraftBukkitClass("CraftWorld").getCanonicalName());
        Object mcWorld = new Reflection().reflect(MinecraftReflection.getCraftBukkitClass("CraftWorld")).getSafeMethod("getHandle").getAccessor().invoke(spawnLocation.getWorld());
        S entityPet = EchoPet.getPetRegistry().getRegistrationEntry(pet.getType()).createEntityPet(mcWorld, pet);
        entityPet.setLocation(spawnLocation);
        if (!spawnLocation.getChunk().isLoaded()) {
            spawnLocation.getChunk().load();
        }
        if (!((Boolean) new Reflection().reflect(MinecraftReflection.getMinecraftClass("World")).getSafeMethod("addEntity", MinecraftReflection.getMinecraftClass("Entity"), CreatureSpawnEvent.SpawnReason.class).getAccessor().invoke(mcWorld, entityPet, CreatureSpawnEvent.SpawnReason.CUSTOM))) {
            Lang.SPAWN_BLOCKED.send(pet.getOwner());
            EchoPet.getManager().removePet(pet);
            return null;
        }
        return entityPet;
    }
}