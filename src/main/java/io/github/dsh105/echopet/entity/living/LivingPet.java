package io.github.dsh105.echopet.entity.living;

import io.github.dsh105.dshutils.Particle;
import io.github.dsh105.echopet.EchoPet;
import io.github.dsh105.echopet.api.event.PetSpawnEvent;
import io.github.dsh105.echopet.api.event.PetTeleportEvent;
import io.github.dsh105.echopet.entity.living.data.PetData;
import io.github.dsh105.echopet.entity.living.data.PetType;
import io.github.dsh105.dshutils.logger.Logger;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.StringUtil;
import net.minecraft.server.v1_7_R1.World;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class LivingPet {

    private boolean isMount = false;
    public boolean ownerIsMounting = false;

    private Player owner;
    private EntityLivingPet pet;
    private PetType petType;
    private LivingPet mount;
    private CraftLivingPet craftPet;

    private boolean ownerIsRiding = false;
    private boolean isHat = false;

    private String name;
    public ArrayList<PetData> dataTrue = new ArrayList<PetData>();

    public LivingPet(Player owner, PetType petType) {
        this.owner = owner;
        this.petType = petType;
        this.pet = this.createPet();
        this.teleportToOwner();
        setName(petType.getDefaultName(owner.getName()));
    }

    /**
     * Gets the Ride Mode state of this {@link LivingPet}
     *
     * @return true if the owner is riding
     */
    public boolean isOwnerRiding() {
        return this.ownerIsRiding;
    }

    /**
     * Gets the Hat Mode state of this {@link LivingPet}
     *
     * @return true if Hat Mode is active
     */
    public boolean isPetHat() {
        return this.isHat;
    }

    /**
     * Sets the Ride Mode state of this {@link LivingPet}
     *
     * @param flag true if your wish for the owner to ride their {@link LivingPet}
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
            if (this.mount != null) {
                this.mount.removePet(false);
            }
            new BukkitRunnable() {
                public void run() {
                    ((CraftPlayer) owner).getHandle().mount(pet);
                    ownerIsMounting = false;
                    if (getEntityPet() instanceof EntityNoClipPet) {
                        ((EntityNoClipPet) getEntityPet()).noClip(false);
                    }
                }
            }.runTaskLater(EchoPet.getInstance(), 5L);
        }
        this.ownerIsRiding = flag;
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
     * Sets the Hat Mode state of this {@link LivingPet}
     *
     * @param flag true if your wish for this {@link LivingPet} to be a Hat
     */
    public void setAsHat(boolean flag) {
        if (this.ownerIsRiding) {
            this.ownerRidePet(false);
        }
        this.teleportToOwner();
        if (!flag) {
            if (this.mount != null) {
                this.mount.getCraftPet().getHandle().mount(null);
                this.craftPet.getHandle().mount(null);
                this.mount.getCraftPet().getHandle().mount(this.craftPet.getHandle());
            } else {
                this.craftPet.getHandle().mount(null);
            }
        } else {
            if (this.mount != null) {
                this.mount.getCraftPet().getHandle().mount(null);
                this.craftPet.getHandle().mount(((CraftPlayer) this.owner).getHandle());
                this.craftPet.setPassenger(this.mount.getCraftPet());
            } else {
                this.craftPet.getHandle().mount(((CraftPlayer) this.owner).getHandle());
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

    protected EntityLivingPet createPet() {
        Location l = owner.getLocation();
        PetSpawnEvent spawnEvent = new PetSpawnEvent(this, l);
        EchoPet.getInstance().getServer().getPluginManager().callEvent(spawnEvent);
        if (spawnEvent.isCancelled()) {
            return null;
        }
        l = spawnEvent.getSpawnLocation();
        PetType pt = this.petType;
        World mcWorld = ((CraftWorld) l.getWorld()).getHandle();
        EntityLivingPet entityPet = pt.getNewEntityInstance(mcWorld, this);

        entityPet.setPositionRotation(l.getX(), l.getY(), l.getZ(), this.getOwner().getLocation().getYaw(), this.getOwner().getLocation().getPitch());
        if (!l.getChunk().isLoaded()) {
            l.getChunk().load();
        }
        if (!mcWorld.addEntity(entityPet, SpawnReason.CUSTOM)) {
            owner.sendMessage(EchoPet.getInstance().prefix + ChatColor.YELLOW + "Failed to spawn pet entity. Maybe WorldGuard is blocking it?");
            EchoPet.getInstance().PH.removePet(this, true);
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
     * Teleports this {@link LivingPet} to its owner
     */
    public void teleportToOwner() {
        this.teleport(this.owner.getLocation());
    }

    /**
     * Teleports this {@link LivingPet} to a {@link Location}
     *
     * @param to {@link Location} to teleport the {@link LivingPet} to
     */
    public void teleport(Location to) {
        PetTeleportEvent teleportEvent = new PetTeleportEvent(this.pet.getPet(), this.pet.getLocation(), to);
        EchoPet.getInstance().getServer().getPluginManager().callEvent(teleportEvent);
        if (teleportEvent.isCancelled()) {
            return;
        }
        Location l = teleportEvent.getTo();
        if (l.getWorld() == this.getLocation().getWorld()) {
            if (mount != null) {
                mount.getCraftPet().eject();
                mount.getCraftPet().teleport(l);
            }
            craftPet.teleport(l);
            if (mount != null) {
                craftPet.setPassenger(mount.getCraftPet());
            }
        }
    }

    /**
     * Set the name tag of this {@link LivingPet}
     *
     * @param s new name of the {@link LivingPet}
     */
    public void setName(String s) {
        s = StringUtil.replaceStringWithColours(s).replace("\'", "'");
        name = s;
        LivingEntity le = craftPet;
        le.setCustomName(s);
        le.setCustomNameVisible((Boolean) EchoPet.getInstance().options.getConfigOption("pets." + petType.toString().toLowerCase().replace("_", " ") + ".tagVisible", true));
    }

    /**
     * Gets the {@link Location} of this {@link LivingPet}
     *
     * @return
     */
    public Location getLocation() {
        return this.craftPet.getLocation();
    }

    /**
     * Gets this {@link LivingPet}'s name
     *
     * @return this {@link LivingPet}'s name
     */
    public String getPetName() {
        return this.name;
    }

    /**
     * Get this {@link LivingPet}'s name without colours translated
     *
     * @return the name of this {@link LivingPet}
     */
    public String getNameToString() {
        return StringUtil.replaceColoursWithString(name).replace("'", "\'");
    }

    /**
     * Gets this {@link LivingPet}'s owner
     *
     * @return a {@link Player} object that owns this {@link LivingPet}
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Gets the {@link CraftLivingPet} for this {@link LivingPet}
     *
     * @return a {@link CraftLivingPet} object for this {@link LivingPet}
     */
    public CraftLivingPet getCraftPet() {
        return this.craftPet;
    }

    protected void setCraftPet(CraftLivingPet cp) {
        this.craftPet = cp;
    }

    /**
     * Gets the {@link EntityLivingPet} for this {@link LivingPet}
     *
     * @return a {@link EntityLivingPet} object for this {@link LivingPet}
     */
    public EntityLivingPet getEntityPet() {
        return this.pet;
    }

    /**
     * Gets this {@link LivingPet}'s mount
     *
     * @return {@link LivingPet} object mounted on this {@link LivingPet}, null if one does not exist
     */
    public LivingPet getMount() {
        return mount;
    }

    /**
     * Remove this {@link LivingPet}'s mount
     */
    public void removeMount() {
        if (mount != null) {
            mount.removePet(true);
            this.mount = null;
        }
    }

    /**
     * Remove this {@link LivingPet}
     * <p/>
     * Kills this {@link LivingPet} and removes any mounts
     */
    public void removePet(boolean makeSound) {
        try {
            Particle.CLOUD.sendTo(this.craftPet.getLocation());
            Particle.LAVA_SPARK.sendTo(this.craftPet.getLocation());
            removeMount();
            pet.remove(makeSound);
            craftPet.remove();
        } catch (Exception e) {
        }
    }

    /**
     * Get active {@link PetData} for this {@link LivingPet}
     *
     * @return An {@link ArrayList} of {@link PetData} for this {@link LivingPet}
     */
    public ArrayList<PetData> getActiveData() {
        return this.dataTrue;
    }

    /**
     * Creates a Mount for this {@link LivingPet}
     *
     * @param pt              the {@link PetType} used to create a mount
     * @param sendFailMessage whether to send a message to the owner if mounts are disabled
     * @return a new {@link LivingPet} object that is mounting this {@link LivingPet}
     */
    public LivingPet createMount(final PetType pt, boolean sendFailMessage) {
        if (!EchoPet.getInstance().options.allowMounts(this.petType)) {
            if (sendFailMessage) {
                Lang.sendTo(this.owner, Lang.MOUNTS_DISABLED.toString().replace("%type%", StringUtil.capitalise(this.petType.toString())));
            }
            return null;
        }
        if (this.ownerIsRiding) {
            this.ownerRidePet(false);
        }
        if (this.mount != null) {
            this.removeMount();
        }
        LivingPet p = pt.getNewPetInstance(owner, pt);
        mount = p;
        p.isMount = true;
        new BukkitRunnable() {
            public void run() {
                craftPet.setPassenger(mount.getCraftPet());
                EchoPet.getInstance().SPH.saveToDatabase(mount, true);
            }
        }.runTaskLater(EchoPet.getInstance(), 5L);

        return this.mount;
    }

    /**
     * Get this {@link LivingPet}'s type
     *
     * @return {@link PetType} for this {@link LivingPet}
     */
    public PetType getPetType() {
        return this.petType;
    }

    protected boolean isPetType(PetType pt) {
        return pt.equals(this.petType);
    }

    /**
     * Gets whether this {@link LivingPet} is a Mount
     *
     * @return true if it is a mount, false if not
     */
    public boolean isMount() {
        return this.isMount;
    }
}