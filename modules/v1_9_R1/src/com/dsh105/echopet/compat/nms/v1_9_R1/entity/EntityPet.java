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

package com.dsh105.echopet.compat.nms.v1_9_R1.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.dsh105.echopet.compat.api.ai.PetGoalSelector;
import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.event.PetAttackEvent;
import com.dsh105.echopet.compat.api.event.PetRideJumpEvent;
import com.dsh105.echopet.compat.api.event.PetRideMoveEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.Logger;
import com.dsh105.echopet.compat.api.util.MenuUtil;
import com.dsh105.echopet.compat.api.util.Perm;
import com.dsh105.echopet.compat.api.util.menu.MenuOption;
import com.dsh105.echopet.compat.api.util.menu.PetMenu;
import com.dsh105.echopet.compat.nms.v1_9_R1.NMSEntityUtil;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.ai.PetGoalFloat;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.ai.PetGoalFollowOwner;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.ai.PetGoalLookAtPlayer;

import net.minecraft.server.v1_9_R1.*;

public abstract class EntityPet extends EntityCreature implements IAnimal, IEntityPet {

    protected IPet pet;
	public PetGoalSelector petGoalSelector;

	protected Field FIELD_JUMP = null;
    protected double jumpHeight;

    protected float rideSpeed;
    public EntityLiving goalTarget = null;
    public boolean shouldVanish;

    public EntityPet(World world) {
        super(world);
    }

    public EntityPet(World world, IPet pet) {
        super(world);
        this.pet = pet;
        this.initiateEntityPet();
    }

    private void initiateEntityPet() {
        this.resetEntitySize();
        this.fireProof = true;
        if (this.FIELD_JUMP == null) {
            try {
				this.FIELD_JUMP = EntityLiving.class.getDeclaredField("bc");// Usually right below lastDamage float. Has 3 floats after it.
                this.FIELD_JUMP.setAccessible(true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        this.getBukkitEntity().setMaxHealth(pet.getPetType().getMaxHealth());
        this.setHealth((float) pet.getPetType().getMaxHealth());
        this.jumpHeight = EchoPet.getOptions().getRideJumpHeight(this.getPet().getPetType());
        this.rideSpeed = EchoPet.getOptions().getRideSpeed(this.getPet().getPetType());
        this.setPathfinding();
    }

    public PetType getEntityPetType() {
        EntityPetType entityPetType = this.getClass().getAnnotation(EntityPetType.class);
        if (entityPetType != null) {
            return entityPetType.petType();
        }
        return null;
    }


    public void resizeBoundingBox(boolean flag) {
        EntitySize es = this.getClass().getAnnotation(EntitySize.class);
        if (es != null) {
            this.setSize(flag ? (es.width() / 2) : es.width(), flag ? (es.height() / 2) : es.height());
        }
    }


    public void resetEntitySize() {
        EntitySize es = this.getClass().getAnnotation(EntitySize.class);
        if (es != null) {
            this.setSize(es.width(), es.height());
        }
    }


    public void setEntitySize(float width, float height) {
        this.setSize(width, height);
    }


    public boolean isPersistent() {
        return true;
    }

    public IPet getPet() {
        return this.pet;
    }

    public Player getPlayerOwner() {
        return pet.getOwner();
    }

    public Location getLocation() {
        return this.pet.getLocation();
    }

    public void setVelocity(Vector vel) {
        this.motX = vel.getX();
        this.motY = vel.getY();
        this.motZ = vel.getZ();
        this.velocityChanged = true;
    }

    public Random random() {
        return this.random;
    }


    public PetGoalSelector getPetGoalSelector() {
        return petGoalSelector;
    }


    public boolean isDead() {
        return dead;
    }


    public void setShouldVanish(boolean flag) {
        this.shouldVanish = flag;
    }


    public void setTarget(LivingEntity livingEntity) {
        this.setGoalTarget(((CraftLivingEntity) livingEntity).getHandle());
    }


    public LivingEntity getTarget() {
        return (LivingEntity) this.getGoalTarget().getBukkitEntity();
    }

    public boolean attack(Entity entity) {
        return this.attack(entity, (float) this.getPet().getPetType().getAttackDamage());
    }

    public boolean attack(Entity entity, float damage) {
        return this.attack(entity, DamageSource.mobAttack(this), damage);
    }

    public boolean attack(Entity entity, DamageSource damageSource, float damage) {
        PetAttackEvent attackEvent = new PetAttackEvent(this.getPet(), entity.getBukkitEntity(), damage);
        EchoPet.getPlugin().getServer().getPluginManager().callEvent(attackEvent);
        if (!attackEvent.isCancelled()) {
            if (entity instanceof EntityPlayer) {
                if (!(EchoPet.getConfig().getBoolean("canAttackPlayers", false))) {
                    return false;
                }
            }
            return entity.damageEntity(damageSource, (float) attackEvent.getDamage());
        }
        return false;
    }

    public void setPathfinding() {
        try {
            NMSEntityUtil.clearGoals(this);
            this.petGoalSelector = new PetGoalSelector();

            petGoalSelector.addGoal(new PetGoalFloat(this), 0);
            petGoalSelector.addGoal(new PetGoalFollowOwner(this, this.getSizeCategory().getStartWalk(getPet().getPetType()), this.getSizeCategory().getStopWalk(getPet().getPetType()), this.getSizeCategory().getTeleport(getPet().getPetType())), 1);
            petGoalSelector.addGoal(new PetGoalLookAtPlayer(this, EntityHuman.class), 2);

        } catch (Exception e) {
			e.printStackTrace();
            Logger.log(Logger.LogLevel.WARNING, "Could not add PetGoals to Pet AI.", e, true);
        }
    }


    public CraftCreature getBukkitEntity() {
        return (CraftCreature) super.getBukkitEntity();
    }

    // well then...it's now 'final'
    
    /*
	// Overriden from EntityInsentient - Most importantly overrides pathfinding selectors
	
	protected final void doTick() {
	    super.doTick();
	    ++this.ticksFarFromPlayer;
	
	    this.D();
	
	    this.getEntitySenses().a();
	
	    // If this ever happens...
	    if (this.petGoalSelector == null) {
	        this.remove(false);
	        return;
	    }
	    this.petGoalSelector.updateGoals();
	
	    this.navigation.k();
	
	    this.E();
	
	    this.getControllerMove().c();
	
	    this.getControllerLook().a();
	
	    this.getControllerJump().b();
	}
	*/


    public boolean onInteract(Player p) {
		if(p.getUniqueId().equals(getPlayerOwner().getUniqueId())){
			// if (IdentUtil.areIdentical(p, getPlayerOwner())) {
            if (EchoPet.getConfig().getBoolean("pets." + this.getPet().getPetType().toString().toLowerCase().replace("_", " ") + ".interactMenu", true) && Perm.BASE_MENU.hasPerm(this.getPlayerOwner(), false, false)) {
                ArrayList<MenuOption> options = MenuUtil.createOptionList(getPet().getPetType());
                int size = this.getPet().getPetType() == PetType.HORSE ? 18 : 9;
                PetMenu menu = new PetMenu(getPet(), options, size);
                menu.open(false);
            }
            return true;
        }
        return false;
    }


	public EnumInteractionResult a(EntityHuman human, Vec3D vec3d, ItemStack itemstack, EnumHand enumhand){
		return onInteract((Player) human.getBukkitEntity()) ? EnumInteractionResult.SUCCESS : EnumInteractionResult.FAIL;
    }

    public void setPositionRotation(double d0, double d1, double d2, float f, float f1) {
        super.setPositionRotation(d0, d1, d2, f, f1);
    }

    public void setLocation(Location l) {
        this.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
        this.world = ((CraftWorld) l.getWorld()).getHandle();
    }

    public void teleport(Location l) {
        this.getPet().getCraftPet().teleport(l);
    }


	public void remove(boolean makeSound){
        if (this.bukkitEntity != null) {
            bukkitEntity.remove();
        }
        if (makeSound) {
			SoundEffect sound = this.getDeathSound();
            if (sound != null) {
				a(sound, 1.0F, 1.0F);// was makeSound in 1.8
				/*
				  public void a(SoundEffect soundeffect, float f, float f1)
				  {
				  if (!ad()) {
				  this.world.a(null, this.locX, this.locY, this.locZ, soundeffect, bz(), f, f1);
				  }
				  }
				 */
            }
        }
    }

    public void onLive() {
        if (this.pet == null) {
            this.remove(false);
            return;
        }

        if (this.getPlayerOwner() == null || !this.getPlayerOwner().isOnline()) {
            EchoPet.getManager().removePet(this.getPet(), true);
            return;
        }

		if(pet.isOwnerRiding() && this.passengers.size() == 0 && !pet.isOwnerInMountingProcess()){
            pet.ownerRidePet(false);
        }

        if (((CraftPlayer) this.getPlayerOwner()).getHandle().isInvisible() != this.isInvisible() && !this.shouldVanish) {
            this.setInvisible(!this.isInvisible());
        }

        if (((CraftPlayer) this.getPlayerOwner()).getHandle().isSneaking() != this.isSneaking()) {
            this.setSneaking(!this.isSneaking());
        }

        if (((CraftPlayer) this.getPlayerOwner()).getHandle().isSprinting() != this.isSprinting()) {
            this.setSprinting(!this.isSprinting());
        }

        if (this.getPet().isHat()) {
            this.lastYaw = this.yaw = (this.getPet().getPetType() == PetType.ENDERDRAGON ? this.getPlayerOwner().getLocation().getYaw() - 180 : this.getPlayerOwner().getLocation().getYaw());
        }

        if (this.getPlayerOwner().isFlying() && EchoPet.getOptions().canFly(this.getPet().getPetType())) {
            Location petLoc = this.getLocation();
            Location ownerLoc = this.getPlayerOwner().getLocation();
            Vector v = ownerLoc.toVector().subtract(petLoc.toVector());

            double x = v.getX();
            double y = v.getY();
            double z = v.getZ();

            Vector vo = this.getPlayerOwner().getLocation().getDirection();
            if (vo.getX() > 0) {
                x -= 1.5;
            } else if (vo.getX() < 0) {
                x += 1.5;
            }
            if (vo.getZ() > 0) {
                z -= 1.5;
            } else if (vo.getZ() < 0) {
                z += 1.5;
            }

            this.setVelocity(new Vector(x, y, z).normalize().multiply(0.3F));
        }
    }

	// EntityInsentient

	public void g(float sideMot, float forwMot){// Look at EntityHorse for shit
		// bu() is passenger shit. Minecraft changed it from 1 passenger to a list
		if(bu().isEmpty() || !(this.bu().get(0) instanceof EntityHuman)){
            super.g(sideMot, forwMot);
			this.P = 0.5F;
            return;
        }
		Entity passenger = this.bu().get(0);
		if(((EntityHuman) bu().get(0)).getBukkitEntity() != this.getPlayerOwner().getPlayer()){
            super.g(sideMot, forwMot);
			this.P = 0.5F;
            return;
        }

		this.P = 1.0F;

		this.lastYaw = this.yaw = passenger.yaw;
		this.pitch = passenger.pitch * 0.5F;
        this.setYawPitch(this.yaw, this.pitch);
        this.aI = this.aG = this.yaw;

		sideMot = ((EntityLiving) passenger).bd * 0.5F;
		forwMot = ((EntityLiving) passenger).be;

        if (forwMot <= 0.0F) {
            forwMot *= 0.25F;
        }
        sideMot *= 0.75F;

        PetRideMoveEvent moveEvent = new PetRideMoveEvent(this.getPet(), forwMot, sideMot);
        EchoPet.getPlugin().getServer().getPluginManager().callEvent(moveEvent);
        if (moveEvent.isCancelled()) {
            return;
        }

		/*
		 * Search for 'getBoolean("NoAI")'. few methods down
		    public void l(float f){
			    super.l(f);
			    o(f);
			}
		 */
		this.l(this.rideSpeed);
		super.g(moveEvent.getSidewardMotionSpeed(), moveEvent.getForwardMotionSpeed());
        PetType pt = this.getPet().getPetType();
		if(FIELD_JUMP != null && !bu().isEmpty()){
            if (EchoPet.getOptions().canFly(pt)) {
                try {
					if(((Player) (passenger.getBukkitEntity())).isFlying()){
						((Player) (passenger.getBukkitEntity())).setFlying(false);
                    }
					if(FIELD_JUMP.getBoolean(passenger)){
                        PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.jumpHeight);
                        EchoPet.getPlugin().getServer().getPluginManager().callEvent(rideEvent);
                        if (!rideEvent.isCancelled()) {
                            this.motY = 0.5F;
                        }
                    }
                } catch (IllegalArgumentException e) {
					Logger.log(Logger.LogLevel.WARNING, "Failed to initiate Pet Flying Motion for " + this.getPlayerOwner().getName() + "'s Pet.", e, true);
                } catch (IllegalAccessException e) {
					Logger.log(Logger.LogLevel.WARNING, "Failed to initiate Pet Flying Motion for " + this.getPlayerOwner().getName() + "'s Pet.", e, true);
                } catch (IllegalStateException e) {
					Logger.log(Logger.LogLevel.WARNING, "Failed to initiate Pet Flying Motion for " + this.getPlayerOwner().getName() + "'s Pet.", e, true);
                }
            } else if (this.onGround) {
                try {
					if(FIELD_JUMP.getBoolean(passenger)){
                        PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.jumpHeight);
                        EchoPet.getPlugin().getServer().getPluginManager().callEvent(rideEvent);
                        if (!rideEvent.isCancelled()) {
                            this.motY = rideEvent.getJumpHeight();
                            doJumpAnimation();
                        }
                    }
                } catch (IllegalArgumentException e) {
                    Logger.log(Logger.LogLevel.WARNING, "Failed to initiate Pet Jumping Motion for " + this.getPlayerOwner().getName() + "'s Pet.", e, true);
                } catch (IllegalAccessException e) {
					Logger.log(Logger.LogLevel.WARNING, "Failed to initiate Pet Jumping Motion for " + this.getPlayerOwner().getName() + "'s Pet.", e, true);
                } catch (IllegalStateException e) {
					Logger.log(Logger.LogLevel.WARNING, "Failed to initiate Pet Jumping Motion for " + this.getPlayerOwner().getName() + "'s Pet.", e, true);
                }
            }
        }
    }

    // EntityInsentient

	protected SoundEffect G(){
        return this.getIdleSound();
    }

    // EntityInsentient

	protected SoundEffect bS(){
        return this.getDeathSound();
    }

	protected abstract SoundEffect getIdleSound(); // idle sound

	protected abstract SoundEffect getDeathSound(); // death sound


    public abstract SizeCategory getSizeCategory();


    // Entity

	public void m(){// Search for "entityBaseTick" the method calling the method its in uses it
		super.m();
        onLive();
        
        if (this.petGoalSelector == null) {
            this.remove(false);
            return;
        }
        this.petGoalSelector.updateGoals();
    }

	protected void i(){// Registers all the values into datawatcher
		super.i();
		initDatawatcher();
		// We don't need datawatcher stuff from EntityCreature, EntityInsentinent, or EntityLiving.
    }

    // Entity

    protected void a(BlockPosition blockposition, Block block) {
        super.a(blockposition, block);
        this.a(blockposition.getX(), blockposition.getY(), blockposition.getZ(), block);
    }

    protected void a(int i, int j, int k, Block block) {
        super.a(new BlockPosition(i, j, k), block);
        makeStepSound(i, j, k, block);
    }

    protected void makeStepSound(int i, int j, int k, Block block) {
        this.makeStepSound();
    }

	protected void initDatawatcher(){}

	protected void makeStepSound(){}

	protected void doJumpAnimation(){}



    public void b(NBTTagCompound nbttagcompound) {
        // Do nothing with NBT
        // Pets should not be stored to world save files
    }


    public boolean c(NBTTagCompound nbttagcompound) {
        // Do nothing with NBT
        // Pets should not be stored to world save files
        return false;
    }


    public void a(NBTTagCompound nbttagcompound) {
        // Do nothing with NBT
        // Pets should not be stored to world save files

        /*super.a(nbttagcompound);
        String owner = nbttagcompound.getString("EchoPet_OwnerName");
        PetType pt = this.getEntityPetType();
        if (pt != null) {
            this.pet = pt.getNewPetInstance(owner, this);
            if (this.pet != null) {
                EchoPet.getManager().loadRiderFromFile(this.getPet());
                this.initiateEntityPet();
            }
        }*/
    }


    public boolean d(NBTTagCompound nbttagcompound) {
        // Do nothing with NBT
        // Pets should not be stored to world save files
        return false;
    }


    public void e(NBTTagCompound nbttagcompound) {
        // Do nothing with NBT
        // Pets should not be stored to world save files
    }
}
