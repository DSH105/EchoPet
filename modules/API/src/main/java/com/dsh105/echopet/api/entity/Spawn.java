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
import com.dsh105.echopet.api.entity.nms.EntityPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.event.PetPreSpawnEvent;
import com.dsh105.echopet.api.plugin.EchoPet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class Spawn {

    public static EntityPet spawn(Pet pet) {
        PetPreSpawnEvent spawnEvent = new PetPreSpawnEvent(pet, pet.getOwner().getLocation());
        Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
        if (spawnEvent.isCancelled()) {
            EchoPet.getManager().removePet(pet, true);
            return null;
        }
        Location spawnLocation = spawnEvent.getSpawnLocation();
        Object mcWorld = new Reflection().reflect(World.class).getSafeFieldByName("getHandle").getAccessor().get(spawnLocation.getWorld());
        EntityPet entityPet = pet.getPetType().getNewEntityPetInstance(mcWorld, pet);
        entityPet.setLocation(spawnLocation);
        if (!spawnLocation.getChunk().isLoaded()) {
            spawnLocation.getChunk().load();
        }
        if (!((Boolean) new Reflection().reflect(MinecraftReflection.getMinecraftClass("World")).getSafeMethod("addEntity", MinecraftReflection.getMinecraftClass("Entity"), CreatureSpawnEvent.SpawnReason.class).getAccessor().invoke(entityPet, CreatureSpawnEvent.SpawnReason.CUSTOM))) {
            Lang.PET_SPAWN_BLOCKED.send(pet.getOwner());
            EchoPet.getManager().removePet(pet, true);
            return null;
        }
        return entityPet;
    }
}