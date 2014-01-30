package io.github.dsh105.echopet.entity.inanimate;

import com.dsh105.dshutils.Particle;
import com.dsh105.dshutils.logger.Logger;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.api.event.PetSpawnEvent;
import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class InanimatePet extends Pet {

    private EntityInanimatePet pet;
    private CraftInanimatePet craftPet;

    public InanimatePet(Player owner, PetType petType) {
        super(owner, petType);
        this.pet = this.initiatePet();
        this.setName(petType.getDefaultName(owner.getName()));
        this.teleportToOwner();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        if (this.getEntityPet().hasInititiated()) {
            this.getEntityPet().updatePacket();
        }
    }

    @Override
    public void teleport(Location to) {
        super.teleport(to);
        if (this.getEntityPet().hasInititiated()) {
            this.getEntityPet().updatePacket();
        }
    }

    @Override
    protected EntityInanimatePet initiatePet() {
        Location l = this.getOwner().getLocation();
        PetSpawnEvent spawnEvent = new PetSpawnEvent(this, l);
        EchoPetPlugin.getInstance().getServer().getPluginManager().callEvent(spawnEvent);
        if (spawnEvent.isCancelled()) {
            return null;
        }
        l = spawnEvent.getSpawnLocation();
        PetType pt = this.getPetType();
        net.minecraft.server.v1_7_R1.World mcWorld = ((CraftWorld) l.getWorld()).getHandle();
        EntityInanimatePet entityPet = (EntityInanimatePet) pt.getNewEntityPetInstance(mcWorld, this);

        entityPet.setPositionRotation(l.getX(), l.getY(), l.getZ(), this.getOwner().getLocation().getYaw(), this.getOwner().getLocation().getPitch());
        if (!l.getChunk().isLoaded()) {
            l.getChunk().load();
        }
        if (!mcWorld.addEntity(entityPet, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            this.getOwner().sendMessage(EchoPetPlugin.getInstance().prefix + ChatColor.YELLOW + "Failed to spawn pet entity. Maybe WorldGuard is blocking it?");
            EchoPetPlugin.getInstance().PH.removePet(this, true);
            spawnEvent.setCancelled(true);
        }
        try {
            Particle.MAGIC_RUNES.sendTo(l);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
        }
        return entityPet;
    }

    /**
     * Gets the {@link io.github.dsh105.echopet.entity.inanimate.EntityInanimatePet} for this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return a {@link io.github.dsh105.echopet.entity.inanimate.EntityInanimatePet} object for this {@link io.github.dsh105.echopet.entity.Pet}
     */
    @Override
    public EntityInanimatePet getEntityPet() {
        return this.pet;
    }

    /**
     * Gets the {@link io.github.dsh105.echopet.entity.inanimate.CraftInanimatePet} for this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return a {@link io.github.dsh105.echopet.entity.inanimate.CraftInanimatePet} object for this {@link io.github.dsh105.echopet.entity.Pet}
     */
    @Override
    public CraftInanimatePet getCraftPet() {
        return this.craftPet;
    }

    @Override
    public CraftPet setCraftPet(CraftPet craftPet) {
        if (craftPet instanceof CraftInanimatePet) {
            this.craftPet = (CraftInanimatePet) craftPet;
        }
        return this.craftPet;
    }
}