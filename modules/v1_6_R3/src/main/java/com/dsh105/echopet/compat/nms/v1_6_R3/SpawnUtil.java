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

package com.dsh105.echopet.compat.nms.v1_6_R3;

import com.dsh105.commodus.particle.Particle;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.event.PetPreSpawnEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.ISpawnUtil;
import com.dsh105.echopet.compat.nms.v1_6_R3.entity.EntityPet;
import net.minecraft.server.v1_6_R3.World;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SpawnUtil implements ISpawnUtil {

    @Override
    public EntityPet spawn(IPet pet, Player owner) {
        Location l = owner.getLocation();
        PetPreSpawnEvent spawnEvent = new PetPreSpawnEvent(pet, l);
        EchoPet.getPlugin().getServer().getPluginManager().callEvent(spawnEvent);
        if (spawnEvent.isCancelled()) {
            owner.sendMessage(EchoPet.getPrefix() + ChatColor.YELLOW + "Pet spawn was cancelled externally.");
            EchoPet.getManager().removePet(pet, true);
            return null;
        }
        l = spawnEvent.getSpawnLocation();
        World mcWorld = ((CraftWorld) l.getWorld()).getHandle();
        EntityPet entityPet = (EntityPet) pet.getPetType().getNewEntityPetInstance(mcWorld, pet);

        entityPet.setLocation(new Location(mcWorld.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch()));
        if (!l.getChunk().isLoaded()) {
            l.getChunk().load();
        }
        if (!mcWorld.addEntity(entityPet, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            owner.sendMessage(EchoPet.getPrefix() + ChatColor.YELLOW + "Failed to spawn pet entity.");
            EchoPet.getManager().removePet(pet, true);
        } else {
            Particle.MAGIC_RUNES.builder().at(l).show();
        }
        return entityPet;
    }
}