package com.github.dsh105.echopet.entity.pet;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.server.v1_6_R2.EntityCreature;
import net.minecraft.server.v1_6_R2.EntityHuman;
import net.minecraft.server.v1_6_R2.EntityLiving;
import net.minecraft.server.v1_6_R2.IMonster;
import net.minecraft.server.v1_6_R2.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.entity.pathfinder.PetGoalSelector;
import com.github.dsh105.echopet.entity.pathfinder.goals.PetGoalFloat;
import com.github.dsh105.echopet.entity.pathfinder.goals.PetGoalFly;
import com.github.dsh105.echopet.entity.pathfinder.goals.PetGoalFollowOwner;
import com.github.dsh105.echopet.entity.pathfinder.goals.PetGoalLookAtPlayer;
import com.github.dsh105.echopet.entity.pathfinder.goals.PetGoalRandomLookaround;
import com.github.dsh105.echopet.menu.MenuOption;
import com.github.dsh105.echopet.menu.PetMenu;
import com.github.dsh105.echopet.util.MenuUtil;

public abstract class EntityPet extends EntityCreature implements IMonster {
	
	public EntityLiving goalTarget = null;
	protected Pet pet;
	
	private Field jump = null;
	private double jumpHeight;
	private float rideSpeed;
	
	public PetGoalSelector petGoalSelector;
	
	/*public EntityPet(World world) {
		super(world);
		this.die();
	}*/
	
	public EntityPet(World world, Pet pet) {
		super(world);
		try {
			pet.setCraftPet(new CraftPet(world.getServer(), this));
			//pet.setCraftPet(pet.getPetType().getNewCraftInstance(this));
			//setPet(pet);
			this.pet = pet;
			((LivingEntity) this.getBukkitEntity()).setMaxHealth(pet.getPetType().getMaxHealth());
			this.setHealth(pet.getPetType().getMaxHealth());
			this.jumpHeight = EchoPet.getPluginInstance().DO.getRideJumpHeight();
			this.rideSpeed = EchoPet.getPluginInstance().DO.getRideSpeed();
			this.jump = EntityLiving.class.getDeclaredField("bd");
	        this.jump.setAccessible(true);
			setPathfinding();
		} catch (Exception e) {
			EchoPet.getPluginInstance().severe(e, "Error creating new pet entity.");
			this.remove();
		}
	}
	
	public void setPathfinding() {
		try {
			this.petGoalSelector = new PetGoalSelector();
			this.getNavigation().b(true);
			
			petGoalSelector.addGoal("Float", new PetGoalFloat(this));
			//petGoalSelector.addGoal("Ride", new PetGoalPassenger(this, EchoPet.getPluginInstance().DO.getRideSpeed(), EchoPet.getPluginInstance().DO.getRideJumpHeight()));
			
			petGoalSelector.addGoal("FollowOwner", new PetGoalFollowOwner(this, this.getSizeCategory().getStartWalk(), this.getSizeCategory().getStopWalk(), this.getSizeCategory().getTeleport()));
			petGoalSelector.addGoal("LookAtPlayer", new PetGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
			petGoalSelector.addGoal("RandomLookAround", new PetGoalRandomLookaround(this));
			
			if ((Boolean) EchoPet.getPluginInstance().DO.getConfigOption("flyTeleport", false)) {
				petGoalSelector.addGoal("Fly", new PetGoalFly(this));
			}
			//petGoalSelector.addGoal("RandomStroll", new PetGoalRandomStroll(this));
			
		} catch (Exception e) {
			EchoPet.getPluginInstance().severe(e, "Error creating new pet entity.");
		}
	}
	
	/*public void setPet(Pet pet) {
		if (pet != null) {
			this.pet = pet;
			((LivingEntity) this.getBukkitEntity()).setMaxHealth(pet.getPetType().getMaxHealth());
			this.setHealth(pet.getPetType().getMaxHealth());
		}
	}*/
	
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

	//getMaxHealth() - 1.5.2
	//ax() - 1.6.1
	@Override
	protected void ay() {
		super.ay();
	}
	
	@Override
	public CraftEntity getBukkitEntity() {
		if (this.bukkitEntity == null) {
			this.bukkitEntity = new CraftPet(this.world.getServer(), this);
		}
		return this.bukkitEntity;
	}
	
	//bb() - 1.6.1
	@Override
	public boolean be() {return true;}
	
	//bb() - 1.5.2
	protected abstract String r(); //idle sound
	
	//aL() - 1.6.1
	protected abstract String aO(); //death sound
	
	public abstract SizeCategory getSizeCategory();
	
	// Overriden from EntityLiving - Most importantly overrides pathfinding selectors
	//1.6.1 - EntityInsentient
	//be() - 1.6.1
	@Override
	protected void bh() {
		//++this.bC; - 1.5.2 age
		++this.aV; //1.6 age
		//this.world.methodProfiler.a("checkDespawn");
		
		this.bo(); //bo() - 1.6.1
		//this.world.methodProfiler.b();
		
		//this.world.methodProfiler.a("sensing");
		this.getEntitySenses().a(); // Gets the field 'bP', as in EntityLiving class
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
		this.bj(); //bg() - 1.6.1
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
			ArrayList<MenuOption> options = MenuUtil.createOptionList(getPet().getPetType());
			PetMenu menu = new PetMenu(getPet(), options);
			menu.open(true);
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
	}
	
	public void travelDimension() {
		byte b0;
		if (this.world.worldProvider.dimension == -1) {
			b0 = 0;
		}
		else {
			b0 = -1;
		}
		this.b(b0);
		this.setLocation(this.getOwner().getLocation());
	}
	
	@Override
	public void e(float f, float f1) { //f1 = sidewards, f1 = forwards/backwards
		if (this.passenger == null || !(this.passenger instanceof EntityHuman)) {
			super.e(f, f1);
			return;
		}
		EntityHuman human = (EntityHuman) this.passenger;
		if (human.getBukkitEntity() != this.getOwner().getPlayer()) {
			super.e(f, f1);
			return;
		}
		
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
        
        this.i(this.rideSpeed);
        super.e(f, f1);
        
        //https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/EntityLiving.java#L1322-L1334
        
        if (jump != null && this.onGround) {
        	try {
    			if (jump.getBoolean(this.passenger)) {
    				this.motY = this.jumpHeight;
    			}
    		} catch (Exception e) {
    			EchoPet.getPluginInstance().severe(e, "Failed to initiate Pet Jumping Motion for " + this.getOwner().getName() + "'s Pet.");
    		}
        }
	}
}