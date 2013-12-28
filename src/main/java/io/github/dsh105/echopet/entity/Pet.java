package io.github.dsh105.echopet.entity;

import io.github.dsh105.dshutils.Particle;
import io.github.dsh105.dshutils.logger.Logger;
import io.github.dsh105.echopet.EchoPet;
import io.github.dsh105.echopet.api.event.PetTeleportEvent;
import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.EntityNoClipPet;
import io.github.dsh105.echopet.entity.living.PetData;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.dshutils.util.StringUtil;
import net.minecraft.server.v1_7_R1.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public abstract class Pet {

    private Player owner;
    private PetType petType;
    private Pet mount;
    private String name;
    private ArrayList<PetData> petData = new ArrayList<PetData>();

    private boolean isMount = false;
    private boolean isLiving;

    public boolean ownerIsMounting = false;
    private boolean ownerRiding = false;
    private boolean isHat = false;

    public Pet(Player owner, PetType petType) {
        this.owner = owner;
        this.petType = petType;
        this.isLiving = this.petType.getEntityClass().equals(EntityLivingPet.class);
    }

    protected abstract EntityPet initiatePet();

    /**
     * Gets the {@link EntityPet} for this {@link Pet}
     *
     * @return a {@link EntityPet} object for this {@link Pet}
     */
    public abstract EntityPet getEntityPet();

    /**
     * Gets the {@link CraftPet} for this {@link Pet}
     *
     * @return a {@link CraftPet} object for this {@link Pet}
     */
    public abstract CraftPet getCraftPet();

    public abstract CraftPet setCraftPet(CraftPet craftPet);

    public Location getLocation() {
        return this.getCraftPet().getLocation();
    }

    /**
     * Get the owner of this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return {@link org.bukkit.entity.Player} that owns this {@link io.github.dsh105.echopet.entity.Pet}
     */
    public Player getOwner() {
        return owner;
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
     * Get whether this {@link io.github.dsh105.echopet.entity.Pet} is living or not
     *
     * @return whether this pet is an instance of {@link io.github.dsh105.echopet.entity.living.LivingPet} or {@link io.github.dsh105.echopet.entity.inanimate.InanimatePet}
     */
    public boolean isLiving() {
        return isLiving;
    }

    /**
     * Gets whether this {@link io.github.dsh105.echopet.entity.Pet} is a mount on another {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return true if this {@link io.github.dsh105.echopet.entity.Pet} is mounted
     */
    public boolean isMount() {
        return isMount;
    }

    /**
     * Get this {@link io.github.dsh105.echopet.entity.Pet}'s mount
     *
     * @return {@link io.github.dsh105.echopet.entity.Pet} object mounted on this {@link io.github.dsh105.echopet.entity.Pet}
     */
    public Pet getMount() {
        return this.mount;
    }

    /**
     * Get the name of this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return name of this {@link io.github.dsh105.echopet.entity.Pet}
     */
    public String getName() {
        return name;
    }

    /**
     * Get this LivingPet's name without colours translated
     *
     * @return the name of this {@link io.github.dsh105.echopet.entity.living.LivingPet}
     */
    public String getNameWithoutColours() {
        return StringUtil.replaceColoursWithString(this.getName());
    }

    /**
     * Set the name tag of this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @param name new name of this {@link io.github.dsh105.echopet.entity.Pet}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get active {@link io.github.dsh105.echopet.entity.living.PetData} for this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return An {@link java.util.ArrayList} of {@link io.github.dsh105.echopet.entity.living.PetData} for this {@link io.github.dsh105.echopet.entity.Pet}
     */
    public ArrayList<PetData> getPetData() {
        return this.petData;
    }

    /**
     * Remove this Pet's mount
     */
    public void removeMount() {
        if (mount != null) {
            mount.removePet(true);
            this.mount = null;
        }
    }

    /**
     * Remove this {@link io.github.dsh105.echopet.entity.Pet}
     * <p/>
     * Kills this {@link io.github.dsh105.echopet.entity.Pet} and removes any mounts
     */
    public void removePet(boolean makeSound) {
        try {
            Particle.CLOUD.sendTo(this.getCraftPet().getLocation());
            Particle.LAVA_SPARK.sendTo(this.getCraftPet().getLocation());
            removeMount();
            this.getEntityPet().remove(makeSound);
            this.getCraftPet().remove();
        } catch (Exception e) {
        }
    }

    /**
     * Teleports this {@link io.github.dsh105.echopet.entity.Pet} to its owner
     */
    public void teleportToOwner() {
        this.teleport(this.owner.getLocation());
    }

    /**
     * Teleports this {@link io.github.dsh105.echopet.entity.Pet} to a {@link org.bukkit.Location}
     *
     * @param to {@link org.bukkit.Location} to teleport the {@link io.github.dsh105.echopet.entity.Pet} to
     */
    public void teleport(Location to) {
        PetTeleportEvent teleportEvent = new PetTeleportEvent(this, this.getLocation(), to);
        EchoPet.getInstance().getServer().getPluginManager().callEvent(teleportEvent);
        if (teleportEvent.isCancelled()) {
            return;
        }
        Location l = teleportEvent.getTo();
        if (l.getWorld() == this.getLocation().getWorld()) {
            if (this.getMount() != null) {
                this.getMount().getCraftPet().eject();
                this.getMount().getCraftPet().teleport(l);
            }
            this.getCraftPet().teleport(l);
            if (this.getMount() != null) {
                this.getCraftPet().setPassenger(this.getMount().getCraftPet());
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
        if (this.isHat) {
            this.setAsHat(false);
        }
        this.teleportToOwner();
        if (!flag) {
            ((CraftPlayer) this.owner).getHandle().mount(null);
            if (this.getEntityPet() instanceof EntityNoClipPet) {
                ((EntityNoClipPet) this.getEntityPet()).noClip(true);
            }
        } else {
            if (this.getMount() != null) {
                this.getMount().removePet(false);
            }
            new BukkitRunnable() {
                public void run() {
                    ((CraftPlayer) owner).getHandle().mount((Entity) getEntityPet());
                    ownerIsMounting = false;
                    if (getEntityPet() instanceof EntityNoClipPet) {
                        ((EntityNoClipPet) getEntityPet()).noClip(false);
                    }
                }
            }.runTaskLater(EchoPet.getInstance(), 5L);
        }
        this.ownerRiding = flag;
        try {
            Particle.PORTAL.sendTo(this.getLocation());
            Location l = this.getLocation().clone();
            l.setY(l.getY() - 1D);
            Particle.BLOCK_DUST.sendDataParticle(l, l.getBlock().getTypeId(), 0);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
        }
    }

    /**
     * Sets the Hat Mode state of this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @param flag true if your wish for this {@link io.github.dsh105.echopet.entity.Pet} to be a Hat
     */
    public void setAsHat(boolean flag) {
        if (this.ownerRiding) {
            this.ownerRidePet(false);
        }
        this.teleportToOwner();
        Entity craftPet = ((Entity) this.getCraftPet().getHandle());
        if (!flag) {
            if (this.getMount() != null) {
                Entity mount = ((Entity) this.getMount().getCraftPet().getHandle());
                mount.mount(null);
                craftPet.mount(null);
                mount.mount(craftPet);
            } else {
                craftPet.mount(null);
            }
        } else {
            if (this.getMount() != null) {
                Entity mount = ((Entity) this.getMount().getCraftPet().getHandle());
                mount.mount(null);
                craftPet.mount(((CraftPlayer) this.owner).getHandle());
                this.getCraftPet().setPassenger(this.getMount().getCraftPet());
            } else {
                craftPet.mount(((CraftPlayer) this.owner).getHandle());
            }
        }
        this.isHat = flag;
        try {
            Particle.PORTAL.sendTo(this.getLocation());
            Location l = this.getLocation().clone();
            l.setY(l.getY() - 1D);
            Particle.BLOCK_DUST.sendDataParticle(l, l.getBlock().getTypeId(), 0);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
        }
    }

    /**
     * Creates a Mount for this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @param pt              the {@link PetType} used to create a mount
     * @param sendFailMessage whether to send a message to the owner if mounts are disabled
     * @return a new Pet object that is mounting this {@link io.github.dsh105.echopet.entity.Pet}
     */
    public Pet createMount(final PetType pt, boolean sendFailMessage) {
        if (!EchoPet.getInstance().options.allowMounts(this.petType)) {
            if (sendFailMessage) {
                Lang.sendTo(this.owner, Lang.MOUNTS_DISABLED.toString().replace("%type%", StringUtil.capitalise(this.petType.toString())));
            }
            return null;
        }
        if (this.isOwnerRiding()) {
            this.ownerRidePet(false);
        }
        if (this.mount != null) {
            this.removeMount();
        }
        Pet p = pt.getNewPetInstance(owner, pt);
        this.mount = p;
        p.isMount = true;
        new BukkitRunnable() {
            public void run() {
                getCraftPet().setPassenger(mount.getCraftPet());
                EchoPet.getInstance().SPH.saveToDatabase(mount, true);
            }
        }.runTaskLater(EchoPet.getInstance(), 5L);

        return this.mount;
    }
}