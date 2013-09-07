package com.github.dsh105.echopet.entity.pet;

import java.util.ArrayList;

import com.github.dsh105.echopet.api.event.PetTeleportEvent;
import com.github.dsh105.echopet.entity.pet.enderdragon.EntityEnderDragonPet;
import com.github.dsh105.echopet.util.Lang;
import net.minecraft.server.v1_6_R2.*;
import org.bukkit.craftbukkit.v1_6_R2.*;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.api.event.PetSpawnEvent;
import com.github.dsh105.echopet.data.PetData;
import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.util.Particle;
import com.github.dsh105.echopet.util.StringUtil;

public class Pet {

	private boolean isMount = false;
	public boolean ownerIsMounting = false;

	private Player owner;
	private EntityPet pet;
	private PetType petType;
	private Pet mount;
	private CraftPet craftPet;

	private boolean ownerIsRiding = false;
	private boolean isHat = false;

	private String name;
	public ArrayList<PetData> dataTrue = new ArrayList<PetData>();
	public ArrayList<PetData> dataFalse = new ArrayList<PetData>();

	public Pet(Player owner, PetType petType) {
		this.owner = owner;
		this.petType = petType;
		this.pet = this.createPet();
		this.teleportToOwner();
		setName(petType.getDefaultName(owner.getName()));
	}

	/**
	 * Gets the Ride Mode state of this {@link Pet}
	 *
	 * @return true if the owner is riding
	 */
	public boolean isOwnerRiding() {
		return this.ownerIsRiding;
	}

	/**
	 * Gets the Hat Mode state of this {@link Pet}
	 *
	 * @return true if Hat Mode is active
	 */
	public boolean isPetHat() {
		return this.isHat;
	}

	/**
	 * Sets the Ride Mode state of this {@link Pet}
	 *
	 * @param flag true if your wish for the owner to ride their {@link Pet}
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
		}
		else {
			if (this.mount != null) {
				this.mount.removePet();
			}
			new BukkitRunnable() {
				public void run() {
					((CraftPlayer) owner).getHandle().mount(pet);
					ownerIsMounting = false;
					if (getEntityPet() instanceof EntityNoClipPet) {
						((EntityNoClipPet) getEntityPet()).noClip(false);
					}
				}
			}.runTaskLater(EchoPet.getPluginInstance(), 5L);
		}
		this.ownerIsRiding = flag;
		try {
			Particle.PORTAL.sendToLocation(this.getLocation());
		} catch (Exception e) {
			EchoPet.getPluginInstance().debug(e, "Particle effect failed.");
		}
	}

	/**
	 * Sets the Hat Mode state of this {@link Pet}
	 *
	 * @param flag true if your wish for this {@link Pet} to be a Hat
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
			}
			else {
				this.craftPet.getHandle().mount(null);
			}
		}
		else {
			if (this.mount != null) {
				this.mount.getCraftPet().getHandle().mount(null);
				this.craftPet.getHandle().mount(((CraftPlayer) this.owner).getHandle());
				this.craftPet.setPassenger(this.mount.getCraftPet());
			}
			else {
				this.craftPet.getHandle().mount(((CraftPlayer) this.owner).getHandle());
			}
		}
		this.isHat = flag;
		try {
			Particle.PORTAL.sendToLocation(this.getLocation());
		} catch (Exception e) {
			EchoPet.getPluginInstance().debug(e, "Particle effect failed.");
		}
	}
	
	protected EntityPet createPet() {
		Location l = owner.getLocation();
		PetSpawnEvent spawnEvent = new PetSpawnEvent(this, l);
		EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(spawnEvent);
		if (spawnEvent.isCancelled()) {
			return null;
		}
		l = spawnEvent.getSpawnLocation();
		PetType pt = this.petType;
		World mcWorld = ((CraftWorld) l.getWorld()).getHandle();
		EntityPet entityPet = pt.getNewEntityInstance(mcWorld, this);
		
		entityPet.setPositionRotation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
		if (!l.getChunk().isLoaded()) {
			l.getChunk().load();
		}
		if (!mcWorld.addEntity(entityPet, SpawnReason.CUSTOM)) {
			owner.sendMessage(EchoPet.getPluginInstance().prefix + ChatColor.YELLOW + "Failed to spawn pet entity. Maybe WorldGuard is blocking it?");
			EchoPet.getPluginInstance().PH.removePet(this);
			spawnEvent.setCancelled(true);
		}
		try {
			Particle.MAGIC_RUNES.sendToLocation(l);
		} catch (Exception e) {
			EchoPet.getPluginInstance().debug(e, "Particle effect failed.");
		}
		return entityPet;
	}

	/**
	 * Teleports this {@link Pet} to its owner
	 */
	public void teleportToOwner() {
		this.teleport(this.owner.getLocation());
	}

	/**
	 * Teleports this {@link Pet} to a {@link Location}
	 *
	 * @param to {@link Location} to teleport the {@link Pet} to
	 */
	public void teleport(Location to) {
		PetTeleportEvent teleportEvent = new PetTeleportEvent(this.pet.getPet(), this.pet.getLocation(), to);
		EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(teleportEvent);
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
	 * Set the name tag of this {@link Pet}
	 *
	 * @param s new name of the {@link Pet}
	 */
	public void setName(String s) {
		s = StringUtil.replaceStringWithColours(s);
		name = s;
		LivingEntity le = craftPet;
		le.setCustomName(s);
		le.setCustomNameVisible((Boolean) EchoPet.getPluginInstance().DO.getConfigOption("petTagVisible", true));
	}

	/**
	 * Gets the {@link Location} of this {@link Pet}
	 * @return
	 */
	public Location getLocation() {
		return this.craftPet.getLocation();
	}

	/**
	 * Gets this {@link Pet}'s name
	 *
	 * @return this {@link Pet}'s name
	 */
	public String getPetName() {
		return this.name;
	}

	/**
	 * Get this {@link Pet}'s name without colours translated
	 *
	 * @return the name of this {@link Pet}
	 */
	public String getNameToString() {
		return StringUtil.replaceColoursWithString(name);
	}

	/**
	 * Gets this {@link Pet}'s owner
	 *
	 * @return a {@link Player} object that owns this {@link Pet}
	 */
	public Player getOwner() {
		return this.owner;
	}

	/**
	 * Gets the {@link CraftPet} for this {@link Pet}
	 *
	 * @return a {@link CraftPet} object for this {@link Pet}
	 */
	public CraftPet getCraftPet() {
		return this.craftPet;
	}
	
	protected void setCraftPet(CraftPet cp) {
		this.craftPet = cp;
	}

	/**
	 * Gets the {@link EntityPet} for this {@link Pet}
	 *
	 * @return a {@link EntityPet} object for this {@link Pet}
	 */
	public EntityPet getEntityPet() {
		return this.pet;
	}

	/**
	 * Gets this {@link Pet}'s mount
	 *
	 * @return {@link Pet} object mounted on this {@link Pet}, null if one does not exist
	 */
	public Pet getMount() {
		return mount;
	}

	/**
	 * Remove this {@link Pet}'s mount
	 */
	public void removeMount() {
		if (mount != null) {
			mount.removePet();
			this.mount = null;
		}
	}

	/**
	 * Remove this {@link Pet}
	 * <p>
	 * Kills this {@link Pet} and removes any mounts
	 */
	public void removePet() {
		try {
			Particle.CLOUD.sendToLocation(this.craftPet.getLocation());
			Particle.LAVA_SPARK.sendToLocation(this.craftPet.getLocation());
			removeMount();
			pet.remove();
			craftPet.remove();
		} catch (Exception e) {}
	}

	/**
	 * Get active {@link PetData} for this {@link Pet}
	 *
	 * @param b get data on (true) or off (false)
	 * @return An {@link ArrayList} of {@link PetData} for this {@link Pet}
	 */
	public ArrayList<PetData> getAllData(boolean b) {
		return b ? this.dataTrue : this.dataFalse;
	}

	/**
	 * Creates a Mount for this {@link Pet}
	 *
	 * @param pt the {@link PetType} used to create a mount
	 * @param sendFailMessage whether to send a message to the owner if mounts are disabled
	 * @return a new {@link Pet} object that is mounting this {@link Pet}
	 */
	public Pet createMount(final PetType pt, boolean sendFailMessage) {
		if (!EchoPet.getPluginInstance().DO.allowMounts(this.petType)) {
			if (sendFailMessage) {
				this.owner.sendMessage(Lang.MOUNTS_DISABLED.toString().replace("%type%", StringUtil.capitalise(this.petType.toString())));
			}
			return null;
		}
		if (this.ownerIsRiding) {
			this.ownerRidePet(false);
		}
		if (this.mount != null) {
			this.removeMount();
		}
		new BukkitRunnable() {
			public void run() {
				Pet p = pt.getNewPetInstance(owner, pt);
				mount = p;
				craftPet.setPassenger(mount.getCraftPet());
				p.isMount = true;
				EchoPet.getPluginInstance().SPH.saveToDatabase(mount, true);
			}
		}.runTaskLater(EchoPet.getPluginInstance(), 5L);

		return this.mount;
	}

	/**
	 * Get this {@link Pet}'s type
	 *
	 * @return {@link PetType} for this {@link Pet}
	 */
	public PetType getPetType() {
		return this.petType;
	}
	
	protected boolean isPetType(PetType pt) {
		return pt.equals(this.petType);
	}

	/**
	 * Gets whether this {@link Pet} is a Mount
	 *
	 * @return true if it is a mount, false if not
	 */
	public boolean isMount() {
		return this.isMount;
	}
}