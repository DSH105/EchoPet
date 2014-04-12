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

package com.dsh105.echopet.compat.nms.v1_7_R3.entity;

import com.dsh105.dshutils.logger.Logger;
import com.dsh105.echopet.compat.api.ai.PetGoalSelector;
import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.event.PetAttackEvent;
import com.dsh105.echopet.compat.api.event.PetRideJumpEvent;
import com.dsh105.echopet.compat.api.event.PetRideMoveEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.MenuUtil;
import com.dsh105.echopet.compat.api.util.menu.MenuOption;
import com.dsh105.echopet.compat.api.util.menu.PetMenu;
import com.dsh105.echopet.compat.nms.v1_7_R3.entity.ai.PetGoalFloat;
import com.dsh105.echopet.compat.nms.v1_7_R3.entity.ai.PetGoalFollowOwner;
import com.dsh105.echopet.compat.nms.v1_7_R3.entity.ai.PetGoalLookAtPlayer;
import net.minecraft.server.v1_7_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public abstract class EntityPet extends EntityCreature implements IAnimal, IEntityPet {

    protected IPet pet;
    public PetGoalSelector petGoalSelector;

    protected int particle = 0;
    protected int particleCounter = 0;
    protected static Field FIELD_JUMP = null;
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
        this.setSize();
        this.fireProof = true;
        if (this.FIELD_JUMP == null) {
            try {
                this.FIELD_JUMP = EntityLiving.class.getDeclaredField("bc");
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

    @Override
    public void resizeBoundingBox(boolean flag) {
        EntitySize es = this.getClass().getAnnotation(EntitySize.class);
        if (es != null) {
            this.setSize(flag ? (es.width() / 2) : es.width(), flag ? (es.height() / 2) : es.height());
        }
    }

    protected void setSize() {
        EntitySize es = this.getClass().getAnnotation(EntitySize.class);
        if (es != null) {
            this.setSize(es.width(), es.height());
        }
    }

    protected void setSize(float width, float height) {
        this.a(width, height);
    }

    @Override
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

    @Override
    public PetGoalSelector getPetGoalSelector() {
        return petGoalSelector;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public void setShouldVanish(boolean flag) {
        this.shouldVanish = flag;
    }

    @Override
    public void setTarget(LivingEntity livingEntity) {
        this.setGoalTarget(((CraftLivingEntity) livingEntity).getHandle());
    }

    @Override
    public LivingEntity getTarget() {
        return (LivingEntity) this.getGoalTarget().getBukkitEntity();
    }

    public boolean attack(Entity entity) {
        return this.attack(entity, (float) this.getPet().getPetType().getAttackDamage());
    }

    public boolean attack(Entity entity, float damage) {
        return this.attack(entity, DamageSource.mobAttack(this), f);
    }

    public boolean attack(Entity entity, DamageSource damageSource, float damage) {
        PetAttackEvent attackEvent = new PetAttackEvent(this.getPet(), entity.getBukkitEntity(), f);
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
            this.petGoalSelector = new PetGoalSelector();
            this.getNavigation().b(true);

            petGoalSelector.addGoal(new PetGoalFloat(this), 0);
            petGoalSelector.addGoal(new PetGoalFollowOwner(this, this.getSizeCategory().getStartWalk(getPet().getPetType()), this.getSizeCategory().getStopWalk(getPet().getPetType()), this.getSizeCategory().getTeleport(getPet().getPetType())), 1);
            petGoalSelector.addGoal(new PetGoalLookAtPlayer(this, EntityHuman.class), 2);

        } catch (Exception e) {
            Logger.log(Logger.LogLevel.WARNING, "Could not add PetGoals to Pet AI.", e, true);
        }
    }

    // EntityInsentient
    @Override
    public boolean bj() {
        return true;
    }

    @Override
    public CraftPet getBukkitEntity() {
        if (this.bukkitEntity == null) {
            this.bukkitEntity = (CraftPet) this.getEntityPetType().getNewCraftInstance(this);
        }
        return (CraftPet) this.bukkitEntity;
    }

    // Overriden from EntityInsentient - Most importantly overrides pathfinding selectors
    @Override
    protected void bm() {
        super.bm();
        ++this.aU;

        this.w();

        this.getEntitySenses().a();

        this.petGoalSelector.updateGoals();

        this.getNavigation().f();

        this.bo();

        this.getControllerMove().c();
        this.getControllerLook().a();
        this.getControllerJump().b();
    }

    @Override
    public boolean onInteract(Player p) {
        return this.a(((CraftPlayer) p).getHandle());
    }

    // EntityInsentient
    @Override
    public boolean a(EntityHuman human) {
        if (human.getBukkitEntity() == this.getPlayerOwner().getPlayer()) {
            if (EchoPet.getConfig().getBoolean("pets." + this.getPet().getPetType().toString().toLowerCase().replace("_", " ") + ".interactMenu", true)) {
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
        this.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
        this.world = ((CraftWorld) l.getWorld()).getHandle();
    }

    public void teleport(Location l) {
        this.getPet().getCraftPet().teleport(l);
    }

    @Override
    public void remove(boolean makeSound) {
        if (this.bukkitEntity != null) {
            bukkitEntity.remove();
        }
        if (makeSound) {
            String sound = this.getDeathSound();
            if (sound != null) {
                makeSound(this.getDeathSound(), 1.0F, 1.0F);
            }
        }
    }

    public void onLive() {
        if (this.pet == null) {
            this.remove(false);
        }

        if (this.getPlayerOwner() == null || !this.getPlayerOwner().isOnline()) {
            EchoPet.getManager().removePet(this.getPet(), true);
            return;
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

        if (this.particle == this.particleCounter) {
            this.particle = 0;
            this.particleCounter = this.random.nextInt(50);
        } else {
            this.particle++;
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
    @Override
    public void e(float sideMot, float forwMot) {
        if (this.passenger == null || !(this.passenger instanceof EntityHuman)) {
            super.e(sideMot, forwMot);
            // https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/EntityHorse.java#L914
            this.W = 0.5F;
            return;
        }
        EntityHuman human = (EntityHuman) this.passenger;
        if (human.getBukkitEntity() != this.getPlayerOwner().getPlayer()) {
            super.e(sideMot, forwMot);
            this.W = 0.5F;
            return;
        }

        this.W = 1.0F;

        this.lastYaw = this.yaw = this.passenger.yaw;
        this.pitch = this.passenger.pitch * 0.5F;
        this.b(this.yaw, this.pitch);
        this.aO = this.aM = this.yaw;

        sideMot = ((EntityLiving) this.passenger).bd * 0.5F;
        forwMot = ((EntityLiving) this.passenger).be;

        if (forwMot <= 0.0F) {
            forwMot *= 0.25F;
        }
        sideMot *= 0.75F;

        PetRideMoveEvent moveEvent = new PetRideMoveEvent(this.getPet(), forwMot, sideMot);
        EchoPet.getPlugin().getServer().getPluginManager().callEvent(moveEvent);
        if (moveEvent.isCancelled()) {
            return;
        }

        this.i(this.rideSpeed);
        super.e(moveEvent.getSidewardMotionSpeed(), moveEvent.getForwardMotionSpeed());

        PetType pt = this.getPet().getPetType();
        if (FIELD_JUMP != null) {
            if (EchoPet.getOptions().canFly(pt)) {
                try {
                    if (((Player) (human.getBukkitEntity())).isFlying()) {
                        ((Player) (human.getBukkitEntity())).setFlying(false);
                    }
                    if (FIELD_JUMP.getBoolean(this.passenger)) {
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
                    if (FIELD_JUMP.getBoolean(this.passenger)) {
                        PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.jumpHeight);
                        EchoPet.getPlugin().getServer().getPluginManager().callEvent(rideEvent);
                        if (!rideEvent.isCancelled()) {
                            this.motY = rideEvent.getJumpHeight();
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
    @Override
    protected String t() {
        return this.getIdleSound();
    }

    // EntityInsentient
    @Override
    protected String aT() {
        return this.getDeathSound();
    }

    protected abstract String getIdleSound(); //idle sound

    protected abstract String getDeathSound(); //death sound

    @Override
    public abstract SizeCategory getSizeCategory();

    // Entity
    @Override
    public void h() {
        super.h();
        //this.C();
        onLive();
    }

    // EntityLiving
    @Override
    protected void c() {
        super.c();
        initDatawatcher();
    }

    // Entity
    @Override
    protected void a(int i, int j, int k, Block block) {
        super.a(i, j, k, block);
        makeStepSound(i, j, k, block);
    }

    protected void makeStepSound(int i, int j, int k, Block block) {
        this.makeStepSound();
    }

    protected void initDatawatcher() {
    }

    protected void makeStepSound() {
    }


    @Override
    public void b(NBTTagCompound nbttagcompound) {
        // Do nothing with NBT
        // Pets should not be stored to world save files
    }

    @Override
    public boolean c(NBTTagCompound nbttagcompound) {
        // Do nothing with NBT
        // Pets should not be stored to world save files
        return false;
    }

    @Override
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

    @Override
    public boolean d(NBTTagCompound nbttagcompound) {
        // Do nothing with NBT
        // Pets should not be stored to world save files
        return false;
    }

    @Override
    public void e(NBTTagCompound nbttagcompound) {
        // Do nothing with NBT
        // Pets should not be stored to world save files
    }
}