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

package io.github.dsh105.echopet.entity;

import com.dsh105.dshutils.Particle;
import com.dsh105.dshutils.util.StringUtil;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.api.event.PetPreSpawnEvent;
import io.github.dsh105.echopet.api.event.PetTeleportEvent;
import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.PetNames;
import io.github.dsh105.echopet.util.StringSimplifier;
import net.minecraft.server.v1_7_R2.Entity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public abstract class Pet {

    private EntityPet entityPet;
    private PetType petType;

    private String owner;
    private Pet rider;
    private String name;
    private ArrayList<PetData> petData = new ArrayList<PetData>();

    private boolean isRider = false;

    public boolean ownerIsMounting = false;
    private boolean ownerRiding = false;
    private boolean isHat = false;

    public Pet(String owner, EntityPet entityPet) {
        this.owner = owner;
        this.setPetType();
        this.entityPet = entityPet;
        this.setPetName(this.getPetType().getDefaultName(this.getNameOfOwner()));
        //this.teleportToOwner();
    }

    public Pet(Player owner) {
        if (owner != null) {
            this.owner = owner.getName();
            this.setPetType();
            this.entityPet = this.initiateEntityPet(owner);
            if (this.entityPet != null) {
                this.setPetName(this.getPetType().getDefaultName(this.getNameOfOwner()));
                this.teleportToOwner();
            }
        }
    }

    protected EntityPet initiateEntityPet() {
        return this.initiateEntityPet(this.getOwner());
    }

    protected EntityPet initiateEntityPet(Player owner) {
        if (owner != null) {
            Location l = owner.getLocation();
            PetPreSpawnEvent spawnEvent = new PetPreSpawnEvent(this, l);
            EchoPetPlugin.getInstance().getServer().getPluginManager().callEvent(spawnEvent);
            if (spawnEvent.isCancelled()) {
                owner.sendMessage(EchoPetPlugin.getInstance().prefix + ChatColor.YELLOW + "Pet spawn was cancelled externally.");
                EchoPetPlugin.getInstance().PH.removePet(this, true);
                return null;
            }
            l = spawnEvent.getSpawnLocation();
            net.minecraft.server.v1_7_R2.World mcWorld = ((CraftWorld) l.getWorld()).getHandle();
            EntityPet entityPet = this.getPetType().getNewEntityPetInstance(mcWorld, this);

            entityPet.setPositionRotation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
            if (!l.getChunk().isLoaded()) {
                l.getChunk().load();
            }
            if (!mcWorld.addEntity(entityPet, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
                owner.sendMessage(EchoPetPlugin.getInstance().prefix + ChatColor.YELLOW + "Failed to spawn pet entity.");
                EchoPetPlugin.getInstance().PH.removePet(this, true);
            } else {
                Particle.MAGIC_RUNES.sendTo(l);
            }
            return entityPet;
        }
        return null;
    }

    protected void setPetType() {
        EntityPetType entityPetType = this.getClass().getAnnotation(EntityPetType.class);
        if (entityPetType != null) {
            this.petType = entityPetType.petType();
        }
    }

    /**
     * Gets the {@link EntityPet} for this {@link Pet}
     *
     * @return a {@link EntityPet} object for this {@link Pet}
     */
    public EntityPet getEntityPet() {
        return this.entityPet;
    }

    /**
     * Gets the {@link CraftPet} for this {@link Pet}
     *
     * @return a {@link CraftPet} object for this {@link Pet}
     */
    public CraftPet getCraftPet() {
        return this.getEntityPet() == null ? null : this.getEntityPet().getBukkitEntity();
    }

    public Location getLocation() {
        return this.getCraftPet().getLocation();
    }

    /**
     * Get the owner of this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return {@link org.bukkit.entity.Player} that owns this {@link io.github.dsh105.echopet.entity.Pet}
     */
    public Player getOwner() {
        if (this.owner == null) {
            return null;
        }
        return Bukkit.getPlayerExact(owner);
    }

    /**
     * Returns the name of this Pet's owner
     *
     * @return Name of the {@link org.bukkit.entity.Player} that owns this {@link io.github.dsh105.echopet.entity.Pet}
     */
    public String getNameOfOwner() {
        return this.owner;
    }

    /**
     * Get the {@link io.github.dsh105.echopet.entity.PetType} of this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return {@link io.github.dsh105.echopet.entity.PetType} of this {@link io.github.dsh105.echopet.entity.Pet}
     */
    public PetType getPetType() {
        return this.petType;
    }

    /**
     * Gets whether this {@link io.github.dsh105.echopet.entity.Pet} is a rider on another {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return true if this {@link io.github.dsh105.echopet.entity.Pet} is a rider
     */
    public boolean isRider() {
        return this.isRider;
    }

    /**
     * Get this {@link io.github.dsh105.echopet.entity.Pet}'s rider
     *
     * @return {@link io.github.dsh105.echopet.entity.Pet} riding this {@link io.github.dsh105.echopet.entity.Pet}
     */
    public Pet getRider() {
        return this.rider;
    }

    /**
     * Get the name of this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return name of this {@link io.github.dsh105.echopet.entity.Pet}
     */
    public String getPetName() {
        return name;
    }

    /**
     * Get this LivingPet's name without colours translated
     *
     * @return the name of this {@link io.github.dsh105.echopet.entity.Pet}
     */
    public String getPetNameWithoutColours() {
        return StringUtil.replaceColoursWithString(this.getPetName());
    }

    /**
     * Set the name tag of this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @param name new name of this {@link io.github.dsh105.echopet.entity.Pet}
     */
    public boolean setPetName(String name) {
        return this.setPetName(name, true);
    }

    /**
     * Set the name tag of this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @param name            new name of this {@link io.github.dsh105.echopet.entity.Pet}
     * @param sendFailMessage if true, sends a message to the owner on fail
     */
    public boolean setPetName(String name, boolean sendFailMessage) {
        if (PetNames.allow(name, this)) {
            this.name = ChatColor.translateAlternateColorCodes('&', name);
            if (EchoPetPlugin.getInstance().getMainConfig().getBoolean("stripDiacriticsFromNames", true)) {
                this.name = StringSimplifier.stripDiacritics(this.name);
            }
            if (this.name == null || this.name.equalsIgnoreCase("")) {
                this.name = this.petType.getDefaultName(this.owner);
            }
            if (this.getCraftPet() != null) {
                this.getCraftPet().setCustomName(this.name);
                this.getCraftPet().setCustomNameVisible(EchoPetPlugin.getInstance().options.getConfig().getBoolean("pets." + this.getPetType().toString().toLowerCase().replace("_", " ") + ".tagVisible", true));
            }
            return true;
        } else {
            if (sendFailMessage) {
                if (this.getOwner() != null) {
                    Lang.sendTo(this.getOwner(), Lang.NAME_NOT_ALLOWED.toString().replace("%name%", name));
                }
            }
            return false;
        }
    }

    /**
     * Get active {@link PetData} for this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return An {@link java.util.ArrayList} of {@link PetData} for this {@link io.github.dsh105.echopet.entity.Pet}
     */
    public ArrayList<PetData> getPetData() {
        return this.petData;
    }

    /**
     * Remove this Pet's rider
     */
    public void removeRider() {
        if (rider != null) {
            rider.removePet(true);
            this.rider = null;
        }
    }

    /**
     * Remove this {@link io.github.dsh105.echopet.entity.Pet}
     * <p/>
     * Kills this {@link io.github.dsh105.echopet.entity.Pet} and removes any riders
     */
    public void removePet(boolean makeSound) {
        if (this.getCraftPet() != null) {
            Particle.CLOUD.sendTo(this.getCraftPet().getLocation());
            Particle.LAVA_SPARK.sendTo(this.getCraftPet().getLocation());
        }
        removeRider();
        if (this.getEntityPet() != null) {
            this.getEntityPet().remove(makeSound);
        }
    }

    /**
     * Teleports this {@link io.github.dsh105.echopet.entity.Pet} to its owner
     */
    public void teleportToOwner() {
        if (this.getOwner() == null || this.getOwner().getLocation() == null) {
            this.removePet(false);
            return;
        }
        this.teleport(this.getOwner().getLocation());
    }

    /**
     * Teleports this {@link io.github.dsh105.echopet.entity.Pet} to a {@link org.bukkit.Location}
     *
     * @param to {@link org.bukkit.Location} to teleport the {@link io.github.dsh105.echopet.entity.Pet} to
     */
    public void teleport(Location to) {
        if (this.entityPet == null || this.entityPet.dead) {
            EchoPetPlugin.getInstance().PH.saveFileData("autosave", this);
            EchoPetPlugin.getInstance().SPH.saveToDatabase(this, false);
            PetHandler.getInstance().removePet(this, false);
            PetHandler.getInstance().createPetFromFile("autosave", this.getOwner());
            return;
        }
        PetTeleportEvent teleportEvent = new PetTeleportEvent(this, this.getLocation(), to);
        EchoPetPlugin.getInstance().getServer().getPluginManager().callEvent(teleportEvent);
        if (teleportEvent.isCancelled()) {
            return;
        }
        Location l = teleportEvent.getTo();
        if (l.getWorld() == this.getLocation().getWorld()) {
            if (this.getRider() != null) {
                this.getRider().getCraftPet().eject();
                this.getRider().getCraftPet().teleport(l);
            }
            this.getCraftPet().teleport(l);
            if (this.getRider() != null) {
                this.getCraftPet().setPassenger(this.getRider().getCraftPet());
            }
        }
    }

    /**
     * Gets the Ride Mode state of this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return true if the owner is riding
     */
    public boolean isOwnerRiding() {
        return this.ownerRiding;
    }

    /**
     * Gets the Hat Mode state of this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return true if Hat Mode is active
     */
    public boolean isHat() {
        return this.isHat;
    }

    /**
     * Sets the Ride Mode state of this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @param flag true if your wish for the owner to ride their {@link io.github.dsh105.echopet.entity.Pet}
     */
    public void ownerRidePet(boolean flag) {
        this.ownerIsMounting = true;
        if (this.ownerRiding == flag) {
            return;
        }
        if (this.isHat) {
            this.setAsHat(false);
        }
        if (!flag) {
            ((CraftPlayer) this.getOwner()).getHandle().mount(null);
            if (this.getEntityPet() instanceof EntityNoClipPet) {
                ((EntityNoClipPet) this.getEntityPet()).noClip(true);
            }
            ownerIsMounting = false;
        } else {
            if (this.getRider() != null) {
                this.getRider().removePet(false);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    ((CraftPlayer) getOwner()).getHandle().mount(getEntityPet());
                    ownerIsMounting = false;
                    if (getEntityPet() instanceof EntityNoClipPet) {
                        ((EntityNoClipPet) getEntityPet()).noClip(false);
                    }
                }
            }.runTaskLater(EchoPetPlugin.getInstance(), 5L);
        }
        this.teleportToOwner();
        this.getEntityPet().resize(flag);
        this.ownerRiding = flag;
        Particle.PORTAL.sendTo(this.getLocation());
        Location l = this.getLocation().clone();
        l.setY(l.getY() - 1D);
        Particle.BLOCK_DUST.sendDataParticle(l, l.getBlock().getTypeId(), 0);
    }

    /**
     * Sets the Hat Mode state of this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @param flag true if your wish for this {@link io.github.dsh105.echopet.entity.Pet} to be a Hat
     */
    public void setAsHat(boolean flag) {
        if (this.isHat == flag) {
            return;
        }
        if (this.ownerRiding) {
            this.ownerRidePet(false);
        }
        this.teleportToOwner();
        Entity craftPet = ((Entity) this.getCraftPet().getHandle());
        if (!flag) {
            if (this.getRider() != null) {
                Entity rider = ((Entity) this.getRider().getCraftPet().getHandle());
                rider.mount(null);
                craftPet.mount(null);
                rider.mount(craftPet);
            } else {
                craftPet.mount(null);
            }
        } else {
            if (this.getRider() != null) {
                Entity rider = ((Entity) this.getRider().getCraftPet().getHandle());
                rider.mount(null);
                craftPet.mount(((CraftPlayer) this.getOwner()).getHandle());
                this.getCraftPet().setPassenger(this.getRider().getCraftPet());
            } else {
                craftPet.mount(((CraftPlayer) this.getOwner()).getHandle());
            }
        }
        this.getEntityPet().resize(flag);
        this.isHat = flag;
        Particle.PORTAL.sendTo(this.getLocation());
        Location l = this.getLocation().clone();
        l.setY(l.getY() - 1D);
        Particle.BLOCK_DUST.sendDataParticle(l, l.getBlock().getTypeId(), 0);
    }

    /**
     * Creates a Rider for this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @param pt              the {@link PetType} used to create a rider for
     * @param sendFailMessage whether to send a message to the owner if riders are disabled
     * @return a new Pet object that is riding this {@link io.github.dsh105.echopet.entity.Pet}
     */
    public Pet createRider(final PetType pt, boolean sendFailMessage) {
        if (pt == PetType.HUMAN) {
            if (sendFailMessage) {
                Lang.sendTo(this.getOwner(), Lang.RIDERS_DISABLED.toString().replace("%type%", StringUtil.capitalise(this.getPetType().toString())));
            }
            return null;
        }
        if (!EchoPetPlugin.getInstance().options.allowRidersFor(this.getPetType())) {
            if (sendFailMessage) {
                Lang.sendTo(this.getOwner(), Lang.RIDERS_DISABLED.toString().replace("%type%", StringUtil.capitalise(this.getPetType().toString())));
            }
            return null;
        }
        if (this.isOwnerRiding()) {
            this.ownerRidePet(false);
        }
        if (this.rider != null) {
            this.removeRider();
        }
        Pet newRider = pt.getNewPetInstance(this.getOwner());
        if (newRider != null) {
            this.rider = newRider;
            newRider.isRider = true;
            new BukkitRunnable() {
                @Override
                public void run() {
                    getCraftPet().setPassenger(Pet.this.rider.getCraftPet());
                    EchoPetPlugin.getInstance().SPH.saveToDatabase(Pet.this.rider, true);
                }
            }.runTaskLater(EchoPetPlugin.getInstance(), 5L);
        }

        return this.rider;
    }
}