package com.github.dsh105.echopet.entity.pet;

import java.util.ArrayList;
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
	
	private Player owner;
	private EntityPet pet;
	private PetType petType;
	private Pet mount = null;
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
		setName(petType.getDefaultName(owner));
	}
	
	public boolean isOwnerRiding() {
		return this.ownerIsRiding;
	}
	
	public boolean isPetHat() {
		return this.isHat;
	}
	
	public void ownerRidePet(boolean flag) {
		if (this.isHat) {
			this.setAsHat(false);
		}
		if (!flag) {
			((CraftPlayer) this.owner).getHandle().mount(null);
		}
		else {
			if (this.mount != null) {
				this.mount.removePet();
			}
			new BukkitRunnable() {
				public void run() {
					((CraftPlayer) owner).getHandle().mount(pet);
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
	
	public void setAsHat(boolean flag) {
		if (this.ownerIsRiding) {
			this.ownerRidePet(false);
		}
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
	
	public EntityPet createPet() {
		PetType pt = this.petType;
		Location l = owner.getLocation();;
		World mcWorld = ((CraftWorld) l.getWorld()).getHandle();
		EntityPet entityPet = pt.getNewEntityInstance(mcWorld, this);
		
		entityPet.setPositionRotation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
		if (!l.getChunk().isLoaded()) {
			l.getChunk().load();
		}
		if (!mcWorld.addEntity(entityPet, SpawnReason.CUSTOM)) {
			owner.sendMessage(EchoPet.getPluginInstance().prefix + ChatColor.YELLOW + "Failed to spawn pet entity. Maybe WorldGuard is blocking it?");
		}
		
		Event spawnEvent = new PetSpawnEvent(this, l);
		EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(spawnEvent);
		try {
			Particle.MAGIC_RUNES.sendToLocation(l);
		} catch (Exception e) {
			EchoPet.getPluginInstance().debug(e, "Particle effect failed.");
		}
		return entityPet;
	}
	
	public void teleportToOwner() {
		Location l = owner.getLocation();
		if (l.getWorld() != this.getLocation().getWorld()) {
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
	
	public void setName(String s) {
		s = StringUtil.replaceStringWithColours(s);
		name = s;
		LivingEntity le = craftPet;
		le.setCustomName(s);
		le.setCustomNameVisible((Boolean) EchoPet.getPluginInstance().DO.getConfigOption("petTagVisible", true));
	}
	
	public Location getLocation() {
		return this.craftPet.getLocation();
	}
	
	public String getPetName() {
		return this.name;
	}
	
	public String getNameToString() {
		return StringUtil.replaceColoursWithString(name);
	}
	
	public Player getOwner() {
		return this.owner;
	}
	
	public CraftPet getCraftPet() {
		return this.craftPet;
	}
	
	public void setCraftPet(CraftPet cp) {
		this.craftPet = cp;
	}
	
	public EntityPet getEntityPet() {
		return this.pet;
	}
	
	public Pet getMount() {
		return mount;
	}
	
	public void removeMount() {
		if (mount != null) {
			mount.removePet();
			this.mount = null;
		}
	}
	
	public void removePet() {
		try {
			Particle.CLOUD.sendToLocation(this.craftPet.getLocation());
			Particle.LAVA_SPARK.sendToLocation(this.craftPet.getLocation());
			removeMount();
			pet.remove();
			craftPet.remove();
		} catch (Exception e) {}
	}
	
	public ArrayList<PetData> getAllData(boolean b) {
		return b ? this.dataTrue : this.dataFalse;
	}
	
	public Pet createMount(final PetType pt) {
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
			}
		}.runTaskLater(EchoPet.getPluginInstance(), 5L);
		return this.mount;
	}
	
	public PetType getPetType() {
		return this.petType;
	}
	
	public boolean isPetType(PetType pt) {
		return pt.equals(this.petType);
	}
}