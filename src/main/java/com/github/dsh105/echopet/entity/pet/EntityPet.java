package com.github.dsh105.echopet.entity.pet;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.github.dsh105.echopet.api.event.PetAttackEvent;
import com.github.dsh105.echopet.api.event.PetRideJumpEvent;
import com.github.dsh105.echopet.api.event.PetRideMoveEvent;
import com.github.dsh105.echopet.data.PetHandler;
import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.logger.Logger;
import com.github.dsh105.echopet.util.Particle;
import net.minecraft.server.v1_6_R3.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.entity.pathfinder.PetGoalSelector;
import com.github.dsh105.echopet.entity.pathfinder.goals.PetGoalFloat;
import com.github.dsh105.echopet.entity.pathfinder.goals.PetGoalFollowOwner;
import com.github.dsh105.echopet.entity.pathfinder.goals.PetGoalLookAtPlayer;
import com.github.dsh105.echopet.menu.main.MenuOption;
import com.github.dsh105.echopet.menu.main.PetMenu;
import com.github.dsh105.echopet.util.MenuUtil;
import org.bukkit.util.Vector;
import org.kitteh.vanish.staticaccess.VanishNoPacket;

public abstract class EntityPet extends EntityCreature implements IMonster {

	public boolean vnp;

	public EntityLiving goalTarget = null;
	protected Pet pet;

	protected int particle = 0;
	protected int particleCounter = 0;
	
	protected Field jump = null;
	protected double jumpHeight;
	protected float rideSpeed;
	
	public PetGoalSelector petGoalSelector;
	
	public EntityPet(World world) {
		super(world);
		this.remove();
	}
	
	public EntityPet(World world, Pet pet) {
		super(world);
		try {
			pet.setCraftPet(new CraftPet(world.getServer(), this));
			this.pet = pet;
			((LivingEntity) this.getBukkitEntity()).setMaxHealth(pet.getPetType().getMaxHealth());
			this.setHealth(pet.getPetType().getMaxHealth());
			this.jumpHeight = EchoPet.getPluginInstance().options.getRideJumpHeight(this.getPet().getPetType());
			this.rideSpeed = EchoPet.getPluginInstance().options.getRideSpeed(this.getPet().getPetType());
			this.jump = EntityLiving.class.getDeclaredField("bd");
	        this.jump.setAccessible(true);
			setPathfinding();
		} catch (Exception e) {
			Logger.log(Logger.LogLevel.WARNING, "Error creating new EntityPet.", e, true);
			this.remove();
		}
	}

	public boolean attack(Entity entity) {
		return this.attack(entity, (float) this.getPet().getPetType().getAttackDamage());
	}

	public boolean attack(Entity entity, float f) {
		return this.attack(entity, DamageSource.mobAttack(this), f);
	}

	public boolean attack(Entity entity, DamageSource damageSource, float f) {
		PetAttackEvent attackEvent = new PetAttackEvent(this.getPet(), entity.getBukkitEntity(), damageSource, f);
		EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(attackEvent);
		if (!attackEvent.isCancelled()) {
			if (entity instanceof EntityPlayer) {
				if (!((Boolean) EchoPet.getPluginInstance().options.getConfigOption("canAttackPlayers", false))) {
					return false;
				}
			}
			return entity.damageEntity(damageSource, (float) attackEvent.getDamage());
		}
		return false;
	}
	
	public void setPathfinding() {
		try {
			this.petGoalSelector = new PetGoalSelector();
			this.getNavigation().b(true);
			
			petGoalSelector.addGoal("Float", new PetGoalFloat(this));

			petGoalSelector.addGoal("FollowOwner", new PetGoalFollowOwner(this, this.getSizeCategory().getStartWalk(getPet().getPetType()), this.getSizeCategory().getStopWalk(getPet().getPetType()), this.getSizeCategory().getTeleport(getPet().getPetType())));
			petGoalSelector.addGoal("LookAtPlayer", new PetGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
			
		} catch (Exception e) {
			Logger.log(Logger.LogLevel.WARNING, "Could not add PetGoals to EntityPet AI.", e, true);
		}
	}

	public Pet getPet() {
		return this.pet;
	}
	
	public Player getOwner() {
		return pet.getOwner();
	}
	
	public Location getLocation() {
		//return new Location(this.world.getWorld(), this.locX, this.locY, this.locZ);
		return this.pet.getLocation();
	}

	public boolean bf() {
		return true;
	}
	
	@Override
	public CraftEntity getBukkitEntity() {
		if (this.bukkitEntity == null) {
			this.bukkitEntity = new CraftPet(this.world.getServer(), this);
		}
		return this.bukkitEntity;
	}

	@Override
	protected String r() {
		return this.getIdleSound();
	}

	@Override
	protected String aP() {
		return this.getDeathSound();
	}

	protected abstract String getIdleSound(); //idle sound

	protected abstract String getDeathSound(); //death sound
	
	public abstract SizeCategory getSizeCategory();
	
	// Overriden from EntityLiving - Most importantly overrides pathfinding selectors
	@Override
	protected void bi() {
		++this.aV;
		//this.world.methodProfiler.a("checkDespawn");
		
		this.u();
		//this.world.methodProfiler.b();
		
		//this.world.methodProfiler.a("sensing");
		this.getEntitySenses().a();
		//this.bq.a() - 1.6
		//this.world.methodProfiler.b();
		
		//this.world.methodProfiler.a("targetSelector");
		//this.targetSelector.a();
		//this.world.methodProfiler.b();
		
		//this.world.methodProfiler.a("goalSelector");
		//this.goalSelector.a();
		this.petGoalSelector.run(); // This is where I tick my own pathfinder selector
		//this.world.methodProfiler.b();
		
		//this.world.methodProfiler.a("navigation");
		//this.getNavigation().e(); //1.5.2
		this.getNavigation().f();
		//this.world.methodProfiler.b();
		
		//this.world.methodProfiler.a("mob tick");
		//this.bp(); - 1.5.2
		this.bk(); //bg() - 1.6.1
		//this.world.methodProfiler.b();
		
		//this.world.methodProfiler.a("controls");
		
		//this.world.methodProfiler.a("move");
		this.getControllerMove().c();
		
		//this.world.methodProfiler.c("look");
		this.getControllerLook().a();
		//this.h.a() - 1.6
		
		//this.world.methodProfiler.c("jump");
		this.getControllerJump().b();
		//this.world.methodProfiler.b();
		//this.world.methodProfiler.b();
	}
	
	//Entity
	//public boolean a_(EntityHuman human) { 1.5
	public boolean a(EntityHuman human) { //1.6
		/*if (super.a_(human)) {
			return false;
		}*/
		
		if (human.getBukkitEntity() == this.getOwner().getPlayer()) {
			if ((Boolean) EchoPet.getPluginInstance().options.getConfigOption("pets." + this.getPet().getPetType().toString().toLowerCase().replace("_", " ") + ".interactMenu", true)) {
				ArrayList<MenuOption> options = MenuUtil.createOptionList(getPet().getPetType());
				int size = this.getPet().getPetType() == PetType.HORSE ? 18 : 9;
				PetMenu menu = new PetMenu(getPet(), options, size);
				menu.open(true);
			}
			return true;
		}
		return false;
	}

	public void setLocation(Location l) {
		this.setLocation(l.getX(), l.getY(), l.getZ(), l.getPitch(), l.getYaw());
		this.world = ((CraftWorld) l.getWorld()).getHandle();
	}
	
	public void teleport(Location l) {
		this.getPet().getCraftPet().teleport(l);
	}
	
	public void remove() {
		bukkitEntity.remove();
		makeSound(this.aP(), 1.0F, 1.0F);
	}
	
	@Override
	public void e(float f, float f1) { //f = sidewards, f1 = forwards/backwards

		// Can only jump over half slabs if the rider is not the owner
		// I like this idea
		// https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/EntityHorse.java#L914
		if (this.passenger == null || !(this.passenger instanceof EntityHuman)) {
			super.e(f, f1);
			this.Y = 0.5F;
			return;
		}
		EntityHuman human = (EntityHuman) this.passenger;
		if (human.getBukkitEntity() != this.getOwner().getPlayer()) {
			super.e(f, f1);
			this.Y = 0.5F;
			return;
		}

		this.Y = 1.0F;
		
		this.lastYaw = this.yaw = this.passenger.yaw;
        this.pitch = this.passenger.pitch * 0.5F;
        this.b(this.yaw, this.pitch);
        this.aP = this.aN = this.yaw;
        
        f = ((EntityLiving) this.passenger).be * 0.5F;
        f1 = ((EntityLiving) this.passenger).bf;
        
        if (f1 <= 0.0F) {
            f1 *= 0.25F;
        }
        f *= 0.75F;

		PetRideMoveEvent moveEvent = new PetRideMoveEvent(this.getPet(), f1, f);
		EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(moveEvent);
		if (moveEvent.isCancelled()) {
			return;
		}

        this.i(this.rideSpeed);
        super.e(moveEvent.getSidewardMotionSpeed(), moveEvent.getForwardMotionSpeed());

        //https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/EntityLiving.java#L1322-L1334

		PetType pt = this.getPet().getPetType();
        if (jump != null) {
	        if (EchoPet.getPluginInstance().options.canFly(pt)) {
		        try {
			        if (((Player) (human.getBukkitEntity())).isFlying()) {
				        ((Player) (human.getBukkitEntity())).setFlying(false);
			        }
			        if (jump.getBoolean(this.passenger)) {
				        PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.jumpHeight);
				        EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(rideEvent);
				        if (!rideEvent.isCancelled()) {
					        this.motY = 0.5F;
				        }
			        }
		        } catch (Exception e) {
			        Logger.log(Logger.LogLevel.WARNING, "Failed to initiate Pet Flying Motion for " + this.getOwner().getName() + "'s Pet.", e, true);
		        }
	        }
	        else if (this.onGround) {
		        try {
			        if (jump.getBoolean(this.passenger)) {
				        PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.jumpHeight);
				        EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(rideEvent);
				        if (!rideEvent.isCancelled()) {
					        this.motY = rideEvent.getJumpHeight();
				        }
			        }
		        } catch (Exception e) {
			        Logger.log(Logger.LogLevel.WARNING, "Failed to initiate Pet Jumping Motion for " + this.getOwner().getName() + "'s Pet.", e, true);
		        }
	        }
        }
	}

	@Override
	public void l_() {
		super.l_();
		try {
			onLive();
		} catch (Exception e) {}
	}

	@Override
	protected void a() {
		super.a();
		try {
			initDatawatcher();
		} catch(Exception e) {}
	}

	@Override
	protected void a(int i, int j, int k, int l) {
		super.a(i, j, k, l);
		try {
			makeStepSound(i, j, k, l);
		} catch (Exception e) {}
	}

	protected void makeStepSound(int i, int j, int k, int l) {
		this.makeStepSound();
	}

	protected void initDatawatcher() {}
	protected void makeStepSound() {}

	public void onLive() {
		if (this.getOwner() == null || !this.getOwner().isOnline() || Bukkit.getPlayerExact(this.getOwner().getName()) == null) {
			PetHandler.getInstance().removePet(this.getPet());
		}

		if (((CraftPlayer) this.getOwner()).getHandle().isInvisible() != this.isInvisible() && !this.vnp) {
			this.setInvisible(!this.isInvisible());
		}

		if (this.isInvisible()) {
			try {
				Particle.MAGIC_CRITIAL_SMALL.sendToPlayer(this.getLocation(), this.getOwner());
				Particle.WITCH_MAGIC_SMALL.sendToPlayer(this.getLocation(), this.getOwner());
			} catch (Exception e) {}
		}

		if (this.getPet().isPetHat()) {

			this.lastYaw = this.yaw = (this.getPet().getPetType() == PetType.ENDERDRAGON ? this.getOwner().getLocation().getYaw() - 180 : this.getOwner().getLocation().getYaw());
		}

		if (this.particle == this.particleCounter) {
			this.particle = 0;
			this.particleCounter = this.random.nextInt(50);
		}
		else {
			this.particle++;
		}

		if (this.getOwner().isFlying() && EchoPet.getPluginInstance().options.canFly(this.getPet().getPetType())) {
			Location petLoc = this.getLocation();
			Location ownerLoc = this.getOwner().getLocation();
			Vector v = ownerLoc.toVector().subtract(petLoc.toVector());

			double x = v.getX();
			double y = v.getY();
			double z = v.getZ();

			Vector vo = this.getOwner().getLocation().getDirection();
			if (vo.getX() > 0) {
				x = x - 1.5;
			} else if (vo.getX() < 0) {
				x = x + 1.5;
			}
			if (vo.getZ() > 0) {
				z = z - 1.5;
			} else if (vo.getZ() < 0) {
				z = z + 1.5;
			}

			this.motX = x;
			this.motY = y;
			this.motZ = z;

			this.motX *= 0.7;
			this.motY *= 0.7;
			this.motZ *= 0.7;
		}
	}
}