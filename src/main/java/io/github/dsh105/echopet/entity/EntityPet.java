package io.github.dsh105.echopet.entity;

import com.dsh105.dshutils.logger.Logger;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.api.event.PetAttackEvent;
import io.github.dsh105.echopet.api.event.PetRideJumpEvent;
import io.github.dsh105.echopet.api.event.PetRideMoveEvent;
import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.entity.pathfinder.PetGoalSelector;
import io.github.dsh105.echopet.entity.pathfinder.goals.PetGoalFloat;
import io.github.dsh105.echopet.entity.pathfinder.goals.PetGoalFollowOwner;
import io.github.dsh105.echopet.entity.pathfinder.goals.PetGoalLookAtPlayer;
import io.github.dsh105.echopet.menu.main.MenuOption;
import io.github.dsh105.echopet.menu.main.PetMenu;
import io.github.dsh105.echopet.util.MenuUtil;
import net.minecraft.server.v1_7_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public abstract class EntityPet extends EntityCreature implements EntityOwnable, IAnimal {

    public boolean vnp;

    public EntityLiving goalTarget = null;
    protected Pet pet;

    protected int particle = 0;
    protected int particleCounter = 0;

    protected static Field FIELD_JUMP = null;
    protected double jumpHeight;
    protected float rideSpeed;

    public PetGoalSelector petGoalSelector;

    public EntityPet(World world) {
        super(world);
    }

    public EntityPet(World world, Pet pet) {
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
        this.jumpHeight = EchoPetPlugin.getInstance().options.getRideJumpHeight(this.getPet().getPetType());
        this.rideSpeed = EchoPetPlugin.getInstance().options.getRideSpeed(this.getPet().getPetType());
        this.setPathfinding();
    }

    public PetType getEntityPetType() {
        EntityPetType entityPetType = this.getClass().getAnnotation(EntityPetType.class);
        if (entityPetType != null) {
            return entityPetType.petType();
        }
        return null;
    }

    protected void resize(boolean flag) {
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

    public Pet getPet() {
        return this.pet;
    }

    public Player getPlayerOwner() {
        return pet.getOwner();
    }

    @Override
    public String getOwnerName() {
        return this.getPlayerOwner().getName();
    }

    @Override
    public Entity getOwner() {
        return ((CraftPlayer) this.getPlayerOwner()).getHandle();
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

    public boolean attack(Entity entity) {
        return this.attack(entity, (float) this.getPet().getPetType().getAttackDamage());
    }

    public boolean attack(Entity entity, float damage) {
        return this.attack(entity, DamageSource.mobAttack(this), f);
    }

    public boolean attack(Entity entity, DamageSource damageSource, float damage) {
        PetAttackEvent attackEvent = new PetAttackEvent(this.getPet(), entity.getBukkitEntity(), damageSource, f);
        EchoPetPlugin.getInstance().getServer().getPluginManager().callEvent(attackEvent);
        if (!attackEvent.isCancelled()) {
            if (entity instanceof EntityPlayer) {
                if (!(EchoPetPlugin.getInstance().options.getConfig().getBoolean("canAttackPlayers", false))) {
                    return false;
                }
            }
            return entity.damageEntity(damageSource, (float) attackEvent.getDamage());
        }
        return false;
    }

    public void setPathfinding() {
        try {
            this.petGoalSelector = new PetGoalSelector(this.getPet());
            this.getNavigation().b(true);

            petGoalSelector.addGoal(new PetGoalFloat(this), 0);
            petGoalSelector.addGoal(new PetGoalFollowOwner(this, this.getSizeCategory().getStartWalk(getPet().getPetType()), this.getSizeCategory().getStopWalk(getPet().getPetType()), this.getSizeCategory().getTeleport(getPet().getPetType())), 1);
            petGoalSelector.addGoal(new PetGoalLookAtPlayer(this, EntityHuman.class, 8.0F), 2);

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
            this.bukkitEntity = this.getEntityPetType().getNewCraftInstance(this);
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

    // EntityInsentient
    @Override
    public boolean a(EntityHuman human) {
        if (human.getBukkitEntity() == this.getPlayerOwner().getPlayer()) {
            if (EchoPetPlugin.getInstance().options.getConfig().getBoolean("pets." + this.getPet().getPetType().toString().toLowerCase().replace("_", " ") + ".interactMenu", true)) {
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

        if (this.getPlayerOwner() == null || !this.getPlayerOwner().isOnline() || Bukkit.getPlayerExact(this.getPlayerOwner().getName()) == null) {
            PetHandler.getInstance().removePet(this.getPet(), true);
        }

        if (((CraftPlayer) this.getPlayerOwner()).getHandle().isInvisible() != this.isInvisible() && !this.vnp) {
            this.setInvisible(!this.isInvisible());
        }

        if (this.isInvisible()) {
            //Particle.MAGIC_CRITIAL.sendToPlayer(this.getLocation(), this.getPlayerOwner());
            //Particle.WITCH_MAGIC.sendToPlayer(this.getLocation(), this.getPlayerOwner());
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

        if (this.getPlayerOwner().isFlying() && EchoPetPlugin.getInstance().options.canFly(this.getPet().getPetType())) {
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
        EchoPetPlugin.getInstance().getServer().getPluginManager().callEvent(moveEvent);
        if (moveEvent.isCancelled()) {
            return;
        }

        this.i(this.rideSpeed);
        super.e(moveEvent.getSidewardMotionSpeed(), moveEvent.getForwardMotionSpeed());

        PetType pt = this.getPet().getPetType();
        if (FIELD_JUMP != null) {
            if (EchoPetPlugin.getInstance().options.canFly(pt)) {
                try {
                    if (((Player) (human.getBukkitEntity())).isFlying()) {
                        ((Player) (human.getBukkitEntity())).setFlying(false);
                    }
                    if (FIELD_JUMP.getBoolean(this.passenger)) {
                        PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.jumpHeight);
                        EchoPetPlugin.getInstance().getServer().getPluginManager().callEvent(rideEvent);
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
                        EchoPetPlugin.getInstance().getServer().getPluginManager().callEvent(rideEvent);
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

    public abstract SizeCategory getSizeCategory();

    // Entity
    @Override
    public void h() {
        super.h();
        //this.C();
        try {
            onLive();
        } catch (Exception e) {
        }
    }

    // EntityLiving
    @Override
    protected void c() {
        super.c();
        try {
            initDatawatcher();
        } catch (Exception e) {
        }
    }

    // Entity
    @Override
    protected void a(int i, int j, int k, Block block) {
        super.a(i, j, k, block);
        try {
            makeStepSound(i, j, k, block);
        } catch (Exception e) {
        }
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
                PetHandler.getInstance().loadRiderFromFile(this.getPet());
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