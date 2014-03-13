package io.github.dsh105.echopet.entity;

import com.dsh105.dshutils.Particle;
import com.dsh105.dshutils.util.StringUtil;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.api.event.PetPreSpawnEvent;
import io.github.dsh105.echopet.api.event.PetTeleportEvent;
import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.util.Lang;
import net.minecraft.server.v1_7_R1.Entity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Pet {

    private EntityPet entityPet;
    private PetType petType;

    private String owner;
    private Pet mount;
    private String name;
    private ArrayList<PetData> petData = new ArrayList<PetData>();

    private boolean isMount = false;

    public boolean ownerIsMounting = false;
    private boolean ownerRiding = false;
    private boolean isHat = false;

    public Pet(String owner, EntityPet entityPet) {
        this.owner = owner;
        this.setPetType();
        this.entityPet = entityPet;
        this.setPetName(this.getPetType().getDefaultName(this.getNameOfOwner()));
        this.teleportToOwner();
    }

    public Pet(String owner) {
        this.owner = owner;
        this.setPetType();
        this.entityPet = this.initiatePet();
        this.setPetName(this.getPetType().getDefaultName(this.getNameOfOwner()));
        this.teleportToOwner();
    }

    protected EntityPet initiatePet() {
        Location l = this.getOwner().getLocation();
        PetPreSpawnEvent spawnEvent = new PetPreSpawnEvent(this, l);
        EchoPetPlugin.getInstance().getServer().getPluginManager().callEvent(spawnEvent);
        if (spawnEvent.isCancelled()) {
            return null;
        }
        l = spawnEvent.getSpawnLocation();
        net.minecraft.server.v1_7_R1.World mcWorld = ((CraftWorld) l.getWorld()).getHandle();
        EntityPet entityPet = this.getPetType().getNewEntityPetInstance(mcWorld, this);

        entityPet.setPositionRotation(l.getX(), l.getY(), l.getZ(), this.getOwner().getLocation().getYaw(), this.getOwner().getLocation().getPitch());
        if (!l.getChunk().isLoaded()) {
            l.getChunk().load();
        }
        if (!mcWorld.addEntity(entityPet, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            this.getOwner().sendMessage(EchoPetPlugin.getInstance().prefix + ChatColor.YELLOW + "Failed to spawn pet entity.");
            EchoPetPlugin.getInstance().PH.removePet(this, true);
        } else {
            try {
                Particle.MAGIC_RUNES.sendTo(l);
            } catch (Exception ex) {
                Logger.getLogger(Pet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return entityPet;
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
        return this.getEntityPet().getBukkitEntity();
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
    public void setPetName(String name) {
        this.name = StringUtil.replaceStringWithColours(name);
        this.getCraftPet().setCustomName(this.name);
        this.getCraftPet().setCustomNameVisible(EchoPetPlugin.getInstance().options.getConfig().getBoolean("pets." + this.getPetType().toString().toLowerCase().replace("_", " ") + ".tagVisible", true));
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
        Particle.CLOUD.sendTo(this.getCraftPet().getLocation());
        Particle.LAVA_SPARK.sendTo(this.getCraftPet().getLocation());
        removeMount();
        this.getEntityPet().remove(makeSound);
    }

    /**
     * Teleports this {@link io.github.dsh105.echopet.entity.Pet} to its owner
     */
    public void teleportToOwner() {
        if (this.getOwner() != null && this.getOwner().getLocation() != null) {
            this.teleport(this.getOwner().getLocation());
        }
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
            if (this.getMount() != null) {
                this.getMount().removePet(false);
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
        this.ownerRiding = flag;
        try {
            Particle.PORTAL.sendTo(this.getLocation());
        } catch (Exception ex) {
            Logger.getLogger(Pet.class.getName()).log(Level.SEVERE, null, ex);
        }
        Location l = this.getLocation().clone();
        l.setY(l.getY() - 1D);
        try {
            Particle.BLOCK_DUST.sendDataParticle(l, l.getBlock().getTypeId(), 0);
        } catch (Exception ex) {
            Logger.getLogger(Pet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sets the Hat Mode state of this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @param flag true if your wish for this {@link io.github.dsh105.echopet.entity.Pet} to be a Hat
     */
    @SuppressWarnings("deprecation")
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
                craftPet.mount(((CraftPlayer) this.getOwner()).getHandle());
                this.getCraftPet().setPassenger(this.getMount().getCraftPet());
            } else {
                craftPet.mount(((CraftPlayer) this.getOwner()).getHandle());
            }
        }
        this.isHat = flag;
        try {
            Particle.PORTAL.sendTo(this.getLocation());
        } catch (Exception ex) {
            Logger.getLogger(Pet.class.getName()).log(Level.SEVERE, null, ex);
        }
        Location l = this.getLocation().clone();
        l.setY(l.getY() - 1D);
        try {
            Particle.BLOCK_DUST.sendDataParticle(l, l.getBlock().getTypeId(), 0);
        } catch (Exception ex) {
            Logger.getLogger(Pet.class.getName()).log(Level.SEVERE, null, ex);
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
        if (!EchoPetPlugin.getInstance().options.allowMounts(this.getPetType())) {
            if (sendFailMessage) {
                Lang.sendTo(this.getOwner(), Lang.MOUNTS_DISABLED.toString().replace("%type%", StringUtil.capitalise(this.getPetType().toString())));
            }
            return null;
        }
        if (this.isOwnerRiding()) {
            this.ownerRidePet(false);
        }
        if (this.mount != null) {
            this.removeMount();
        }
        Pet p = pt.getNewPetInstance(this.getNameOfOwner());
        this.mount = p;
        p.isMount = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                getCraftPet().setPassenger(mount.getCraftPet());
                EchoPetPlugin.getInstance().SPH.saveToDatabase(mount, true);
            }
        }.runTaskLater(EchoPetPlugin.getInstance(), 5L);

        return this.mount;
    }
}
