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

package io.github.dsh105.echopet.api.entity.pet;

import com.dsh105.dshutils.Particle;
import com.dsh105.dshutils.util.StringUtil;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.api.entity.EntityPetType;
import io.github.dsh105.echopet.api.entity.PetData;
import io.github.dsh105.echopet.api.entity.PetType;
import io.github.dsh105.echopet.api.entity.nms.ICraftPet;
import io.github.dsh105.echopet.api.entity.nms.IEntityNoClipPet;
import io.github.dsh105.echopet.api.entity.nms.IEntityPet;
import io.github.dsh105.echopet.api.event.PetTeleportEvent;
import io.github.dsh105.echopet.api.PetHandler;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.PetNames;
import io.github.dsh105.echopet.util.StringSimplifier;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public abstract class Pet {

    private IEntityPet entityPet;
    private PetType petType;

    private String owner;
    private Pet rider;
    private String name;
    private ArrayList<PetData> petData = new ArrayList<PetData>();

    private boolean isRider = false;

    public boolean ownerIsMounting = false;
    private boolean ownerRiding = false;
    private boolean isHat = false;

    public Pet(String owner, IEntityPet entityPet) {
        this.owner = owner;
        this.setPetType();
        this.entityPet = entityPet;
        this.setPetName(this.getPetType().getDefaultName(this.getNameOfOwner()));
    }

    public Pet(Player owner) {
        if (owner != null) {
            this.owner = owner.getName();
            this.setPetType();
            this.entityPet = EchoPetPlugin.getSpawnUtil().spawn(this, owner);
            if (this.entityPet != null) {
                this.setPetName(this.getPetType().getDefaultName(this.getNameOfOwner()));
                this.teleportToOwner();
            }
        }
    }

    protected void setPetType() {
        EntityPetType entityPetType = this.getClass().getAnnotation(EntityPetType.class);
        if (entityPetType != null) {
            this.petType = entityPetType.petType();
        }
    }

    /**
     * Gets the {@link io.github.dsh105.echopet.api.entity.nms.IEntityPet} for this {@link Pet}
     *
     * @return a {@link io.github.dsh105.echopet.api.entity.nms.IEntityPet} object for this {@link Pet}
     */
    public IEntityPet getEntityPet() {
        return this.entityPet;
    }

    /**
     * Gets the {@link io.github.dsh105.echopet.api.entity.nms.ICraftPet} for this {@link Pet}
     *
     * @return a {@link io.github.dsh105.echopet.api.entity.nms.ICraftPet} object for this {@link Pet}
     */
    public ICraftPet getCraftPet() {
        return this.getEntityPet() == null ? null : this.getEntityPet().getBukkitEntity();
    }

    public Location getLocation() {
        return this.getCraftPet().getLocation();
    }

    /**
     * Get the owner of this {@link Pet}
     *
     * @return {@link org.bukkit.entity.Player} that owns this {@link Pet}
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
     * @return Name of the {@link org.bukkit.entity.Player} that owns this {@link Pet}
     */
    public String getNameOfOwner() {
        return this.owner;
    }

    /**
     * Get the {@link io.github.dsh105.echopet.api.entity.PetType} of this {@link Pet}
     *
     * @return {@link io.github.dsh105.echopet.api.entity.PetType} of this {@link Pet}
     */
    public PetType getPetType() {
        return this.petType;
    }

    /**
     * Gets whether this {@link Pet} is a rider on another {@link Pet}
     *
     * @return true if this {@link Pet} is a rider
     */
    public boolean isRider() {
        return this.isRider;
    }

    /**
     * Get this {@link Pet}'s rider
     *
     * @return {@link Pet} riding this {@link Pet}
     */
    public Pet getRider() {
        return this.rider;
    }

    /**
     * Get the name of this {@link Pet}
     *
     * @return name of this {@link Pet}
     */
    public String getPetName() {
        return name;
    }

    /**
     * Get this LivingPet's name without colours translated
     *
     * @return the name of this {@link Pet}
     */
    public String getPetNameWithoutColours() {
        return StringUtil.replaceColoursWithString(this.getPetName());
    }

    /**
     * Set the name tag of this {@link Pet}
     *
     * @param name new name of this {@link Pet}
     */
    public boolean setPetName(String name) {
        return this.setPetName(name, true);
    }

    /**
     * Set the name tag of this {@link Pet}
     *
     * @param name            new name of this {@link Pet}
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
     * Get active {@link PetData} for this {@link Pet}
     *
     * @return An {@link java.util.ArrayList} of {@link PetData} for this {@link Pet}
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
     * Remove this {@link Pet}
     * <p/>
     * Kills this {@link Pet} and removes any riders
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
     * Teleports this {@link Pet} to its owner
     */
    public void teleportToOwner() {
        if (this.getOwner() == null || this.getOwner().getLocation() == null) {
            this.removePet(false);
            return;
        }
        this.teleport(this.getOwner().getLocation());
    }

    /**
     * Teleports this {@link Pet} to a {@link org.bukkit.Location}
     *
     * @param to {@link org.bukkit.Location} to teleport the {@link Pet} to
     */
    public void teleport(Location to) {
        if (this.getEntityPet() == null || this.getEntityPet().isDead()) {
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
     * Gets the Ride Mode state of this {@link Pet}
     *
     * @return true if the owner is riding
     */
    public boolean isOwnerRiding() {
        return this.ownerRiding;
    }

    /**
     * Gets the Hat Mode state of this {@link Pet}
     *
     * @return true if Hat Mode is active
     */
    public boolean isHat() {
        return this.isHat;
    }

    /**
     * Sets the Ride Mode state of this {@link Pet}
     *
     * @param flag true if your wish for the owner to ride their {@link Pet}
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
            this.getOwner().getVehicle().setPassenger(null);
            if (this.getEntityPet() instanceof IEntityNoClipPet) {
                ((IEntityNoClipPet) this.getEntityPet()).noClip(true);
            }
            ownerIsMounting = false;
        } else {
            if (this.getRider() != null) {
                this.getRider().removePet(false);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    getCraftPet().setPassenger(getOwner());
                    ownerIsMounting = false;
                    if (getEntityPet() instanceof IEntityNoClipPet) {
                        ((IEntityNoClipPet) getEntityPet()).noClip(false);
                    }
                }
            }.runTaskLater(EchoPetPlugin.getInstance(), 5L);
        }
        this.teleportToOwner();
        this.getEntityPet().resizeBoundingBox(flag);
        this.ownerRiding = flag;
        Particle.PORTAL.sendTo(this.getLocation());
        Location l = this.getLocation().clone();
        l.setY(l.getY() - 1D);
        Particle.BLOCK_DUST.sendDataParticle(l, l.getBlock().getTypeId(), 0);
    }

    /**
     * Sets the Hat Mode state of this {@link Pet}
     *
     * @param flag true if your wish for this {@link Pet} to be a Hat
     */
    public void setAsHat(boolean flag) {
        if (this.isHat == flag) {
            return;
        }
        if (this.ownerRiding) {
            this.ownerRidePet(false);
        }
        this.teleportToOwner();
        //Entity craftPet = ((Entity) this.getCraftPet().getHandle());
        if (!flag) {
            if (this.getRider() != null) {
                if (getRider().getCraftPet().getVehicle() != null) {
                    getRider().getCraftPet().getVehicle().setPassenger(null);
                }
                //Entity rider = ((Entity) this.getRider().getCraftPet().getHandle());
                //rider.mount(null);
                if (this.getCraftPet().getVehicle() != null) {
                    this.getCraftPet().getVehicle().setPassenger(null);
                }
                this.getCraftPet().setPassenger(getRider().getCraftPet());
                //craftPet.mount(null);
                //rider.mount(craftPet);
            } else {
                if (this.getCraftPet().getVehicle() != null) {
                    this.getCraftPet().getVehicle().setPassenger(null);
                }
            }
        } else {
            if (this.getRider() != null) {
                //Entity rider = ((Entity) this.getRider().getCraftPet().getHandle());
                //rider.mount(null);
                if (getRider().getCraftPet().getVehicle() != null) {
                    getRider().getCraftPet().getVehicle().setPassenger(null);
                }
                this.getOwner().setPassenger(this.getCraftPet());
                //craftPet.mount(((CraftPlayer) this.getOwner()).getHandle());
                this.getCraftPet().setPassenger(this.getRider().getCraftPet());
            } else {
                //craftPet.mount(((CraftPlayer) this.getOwner()).getHandle());
                this.getOwner().setPassenger(this.getCraftPet());
            }
        }
        this.getEntityPet().resizeBoundingBox(flag);
        this.isHat = flag;
        Particle.PORTAL.sendTo(this.getLocation());
        Location l = this.getLocation().clone();
        l.setY(l.getY() - 1D);
        Particle.BLOCK_DUST.sendDataParticle(l, l.getBlock().getTypeId(), 0);
    }

    /**
     * Creates a Rider for this {@link Pet}
     *
     * @param pt              the {@link PetType} used to create a rider for
     * @param sendFailMessage whether to send a message to the owner if riders are disabled
     * @return a new Pet object that is riding this {@link Pet}
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