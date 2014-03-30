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

package io.github.dsh105.echopet.nms.v1_7_R2;

import com.dsh105.dshutils.Particle;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.api.event.PetPreSpawnEvent;
import io.github.dsh105.echopet.nms.ISpawnUtil;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.EntityPet;
import io.github.dsh105.echopet.api.entity.pet.Pet;
import net.minecraft.server.v1_7_R2.World;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SpawnUtil implements ISpawnUtil {

    @Override
    public EntityPet spawn(Pet pet, Player owner) {
        Location l = owner.getLocation();
        PetPreSpawnEvent spawnEvent = new PetPreSpawnEvent(pet, l);
        EchoPetPlugin.getInstance().getServer().getPluginManager().callEvent(spawnEvent);
        if (spawnEvent.isCancelled()) {
            owner.sendMessage(EchoPetPlugin.getInstance().prefix + ChatColor.YELLOW + "Pet spawn was cancelled externally.");
            EchoPetPlugin.getInstance().PH.removePet(pet, true);
            return null;
        }
        l = spawnEvent.getSpawnLocation();
        World mcWorld = ((CraftWorld) l.getWorld()).getHandle();
        EntityPet entityPet = (EntityPet) pet.getPetType().getNewEntityPetInstance(mcWorld, pet);

        entityPet.setPositionRotation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
        if (!l.getChunk().isLoaded()) {
            l.getChunk().load();
        }
        if (!mcWorld.addEntity(entityPet, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            owner.sendMessage(EchoPetPlugin.getInstance().prefix + ChatColor.YELLOW + "Failed to spawn pet entity.");
            EchoPetPlugin.getInstance().PH.removePet(pet, true);
        } else {
            Particle.MAGIC_RUNES.sendTo(l);
        }
        return entityPet;
    }
}