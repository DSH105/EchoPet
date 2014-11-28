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

package com.dsh105.echopet.nms.v1_7_R3.entity;

import com.dsh105.commodus.IdentUtil;
import com.dsh105.echopet.api.config.PetSettings;
import com.dsh105.echopet.api.entity.ai.PetGoalSelector;
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.event.bukkit.PetAttackEvent;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.nms.v1_7_R3.NMSEntityUtil;
import net.minecraft.server.v1_7_R3.*;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public class EntityPetBase<T extends Pet> extends EntityCreature implements IAnimal, EntityPet<T> {

    private T pet;
    private PetGoalSelector petGoalSelector;

    public EntityPetBase(World world) {
        super(world);
    }

    public EntityPetBase(World world, T pet) {
        super(world);
        this.pet = pet;
    }

    // So it won't despawn if there are no nearby players
    @Override
    public boolean isPersistent() {
        return true;
    }

    // Whether to use the new NMS AI or not
    @Override
    protected boolean bj() {
        return true;
    }

    // 'on tick' function
    @Override
    protected void bm() {
        super.bm();

        // Check despawn - Persistent entities won't 'age' or despawn when there are no players around
        this.w();

        // Clear senses
        this.getEntitySenses().a();

        // Update/run our custom AI
        if (this.petGoalSelector == null) {
            this.getBukkitEntity().remove();
            return;
        }
        this.petGoalSelector.updateGoals();

        // Some navigation updates
        this.getNavigation().f();

        // Mob tick
        this.bo();

        // Other movement updates
        this.getControllerMove().c();
        this.getControllerLook().a();
        this.getControllerJump().b();
    }

    @Override
    public void move(double x, double y, double z) {
        if (pet.isStationary()) {
            return;
        }
        super.move(x, y, z);
    }

    @Override
    public void g(double x, double y, double z) {
        if (pet.isStationary()) {
            super.g(0, 0, 0);
            return;
        }
        super.g(x, y, z);
    }

    @Override
    protected boolean a(EntityHuman entityhuman) {
        if (entityhuman.getBukkitEntity() instanceof org.bukkit.entity.Player) {
            try {
                return onInteract((org.bukkit.entity.Player) entityhuman.getBukkitEntity());
            } catch (Exception e) {
                EchoPet.LOG.severe("Uh oh. Something bad happened");
                e.printStackTrace();
                pet.despawn(false);
            }
        }
        // Nope!
        return false;
    }

    @Override
    public void e(float sideMotion, float forwardMotion) {
        try {
            // Call the ride function
            pet.onRide(sideMotion, forwardMotion);
        } catch (Exception e) {
            EchoPet.LOG.severe("Uh oh. Something bad happened");
            e.printStackTrace();
            pet.despawn(false);
        }
    }

    // 'on live' function
    @Override
    public void h() {
        super.h();
        try {
            pet.onLive();
            onLive();
        } catch (Exception e) {
            EchoPet.LOG.severe("Uh oh. Something bad happened");
            e.printStackTrace();
            pet.despawn(false);
        }
    }

    @Override
    public void onLive() {

    }

    @Override
    protected String t() {
        return pet.getIdleSound();
    }

    @Override
    protected String aS() {
        return pet.getHurtSound();
    }

    @Override
    protected String aT() {
        return pet.getDeathSound();
    }

    @Override
    protected void c() {
        super.c();
        try {
            initDatawatcher();
        } catch (Exception e) {
            EchoPet.LOG.severe("Uh oh. Something bad happened");
            e.printStackTrace();
            pet.despawn(false);
        }
    }

    protected void initDatawatcher() {

    }

    @Override
    protected void a(int i, int j, int k, Block block) {
        super.a(i, j, k, block);
        makeStepSound(i, j, k, block);
    }

    protected void makeStepSound(int i, int j, int k, Block block) {
        pet.makeStepSound();
    }

    /*
     * NBT stuff - Do nothing with NBT
     * Pets should not be stored to world save files
     *
     */

    @Override
    public void b(NBTTagCompound nbttagcompound) {

    }

    @Override
    public boolean c(NBTTagCompound nbttagcompound) {
        return false;
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {

    }

    @Override
    public boolean d(NBTTagCompound nbttagcompound) {
        return false;
    }

    @Override
    public void e(NBTTagCompound nbttagcompound) {

    }

    /*
     * Into our EntityPet methods
     */

    @Override
    public CraftEntity getBukkitEntity() {
        return super.getBukkitEntity();
    }

    @Override
    public T getPet() {
        return pet;
    }

    @Override
    public void setMotionX(double motX) {
        this.motX = motX;
    }

    @Override
    public void setMotionY(double motY) {
        this.motY = motY;
    }

    @Override
    public void setMotionZ(double motZ) {
        this.motZ = motZ;
    }

    @Override
    public double getMotionX() {
        return motX;
    }

    @Override
    public double getMotionY() {
        return motY;
    }

    @Override
    public double getMotionZ() {
        return motZ;
    }

    @Override
    public PetGoalSelector getGoalSelector() {
        return petGoalSelector;
    }

    @Override
    public void setGoalSelector(PetGoalSelector goalSelector) {
        this.petGoalSelector = goalSelector;
        this.getNavigation().b(true);
        this.getNavigation().e(true); // For float goal
    }

    @Override
    public boolean canSee(org.bukkit.entity.Entity entity) {
        return getEntitySenses().canSee(((CraftEntity) entity).getHandle());
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public boolean inLava() {
        return this.O();
    }

    @Override
    public boolean inWater() {
        return this.L();
    }

    @Override
    public boolean isWet() {
        return this.K();
    }

    @Override
    public void setNoClipEnabled(boolean flag) {
        this.X = flag;
    }

    @Override
    public void setTarget(org.bukkit.entity.LivingEntity livingEntity) {
        setGoalTarget(((CraftLivingEntity) livingEntity).getHandle());
    }

    @Override
    public org.bukkit.entity.LivingEntity getTarget() {
        return (LivingEntity) this.getGoalTarget().getBukkitEntity();
    }

    @Override
    public boolean attack(org.bukkit.entity.LivingEntity entity) {
        // TODO
        return false;
    }

    @Override
    public boolean attack(org.bukkit.entity.LivingEntity entity, float damage) {
        return attack(entity, DamageSource.mobAttack((EntityLiving) ((CraftEntity) entity).getHandle()), damage);
    }

    private boolean attack(org.bukkit.entity.LivingEntity entity, DamageSource damageSource, float damage) {
        PetAttackEvent attackEvent = new PetAttackEvent(pet, entity, damage);
        EchoPet.getCore().getServer().getPluginManager().callEvent(attackEvent);
        if (!attackEvent.isCancelled()) {
            if (entity instanceof EntityPlayer) {
                if (!(PetSettings.DAMAGE_PLAYERS.getValue())) {
                    return false;
                }
            }
            return ((CraftEntity) entity).getHandle().damageEntity(damageSource, (float) attackEvent.getDamage());
        }
        return false;
    }

    @Override
    public org.bukkit.entity.Entity getPassenger() {
        return passenger == null ? null : passenger.getBukkitEntity();
    }

    @Override
    public void setFireProof(boolean flag) {
        this.fireProof = flag;
    }

    @Override
    public void setYaw(float yaw) {
        this.lastYaw = this.yaw = yaw;
    }

    @Override
    public void setBlockClimbHeight(float height) {
        this.W = height;
    }

    @Override
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public void applyPitchAndYawChanges(float pitch, float yaw) {
        this.lastYaw = this.yaw = yaw;
        this.pitch = pitch;
        this.b(this.yaw, this.pitch);
        this.aO = this.aM = this.yaw;
    }

    @Override
    public float getPassengerSideMotion() {
        return ((EntityLiving) this.passenger).bd;
    }

    @Override
    public float getPassengerForwardMotion() {
        return ((EntityLiving) this.passenger).be;
    }

    @Override
    public void setSpeed(float value) {
        this.i(value);
    }

    @Override
    public String getJumpField() {
        // TODO: this isn't compatible with Cauldron :\
        return "bc";
    }

    @Override
    public boolean isGrounded() {
        return onGround;
    }

    @Override
    public double getLocX() {
        return locX;
    }

    @Override
    public double getLocY() {
        return locY;
    }

    @Override
    public double getLocZ() {
        return locZ;
    }

    @Override
    public void setLocX(double value) {
        this.locX = value;
    }

    @Override
    public void setLocY(double value) {
        this.locY = value;
    }

    @Override
    public void setLocZ(double value) {
        this.locZ = value;
    }

    @Override
    public float getLength() {
        return length;
    }

    @Override
    public float getSoundVolume() {
        return be();
    }

    @Override
    public void jump() {
        getControllerJump().a();
    }

    @Override
    public void lookAt(org.bukkit.entity.Entity entity, float headYaw) {
        getControllerLook().a(((CraftEntity) entity).getHandle(), headYaw, NMSEntityUtil.getMaxHeadRotation(this));
    }

    @Override
    public void lookAt(org.bukkit.entity.Entity entity, float headYaw, float headPitch) {
        getControllerLook().a(((CraftEntity) entity).getHandle(), headYaw, headPitch);
    }

    @Override
    public void lookAt(double x, double y, double z, float headYaw, float headPitch) {
        getControllerLook().a(x, y, z, headYaw, headPitch);
    }

    @Override
    public void setLocation(org.bukkit.Location location) {
        setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public void modifyBoundingBox(float width, float height) {
        this.a(width, height);
    }

    @Override
    public org.bukkit.entity.Entity findPlayer(double range) {
        return this.world.findNearbyPlayer(this, range).getBukkitEntity();
    }

    @Override
    public org.bukkit.entity.Entity findEntity(Class<?> nmsTypeClass, Object boundingBox) {
        return this.world.a(nmsTypeClass, (AxisAlignedBB) boundingBox, this).getBukkitEntity();
    }

    @Override
    public Object growBoundingBox(double d0, double d1, double d2) {
        return this.boundingBox.grow(d0, d1, d2);
    }

    @Override
    public float distanceTo(org.bukkit.entity.Entity entity) {
        return this.e(((CraftEntity) entity).getHandle());
    }

    @Override
    public float getPathfindingRadius() {
        return (float) getAttributeInstance(GenericAttributes.b).getValue();
    }

    @Override
    public void setPathfindingRadius(float pathfindingRadius) {
        getAttributeInstance(GenericAttributes.b).setValue(pathfindingRadius);
    }

    @Override
    public void navigateTo(org.bukkit.entity.Entity entity, double speed) {
        PathEntity path = world.findPath(this, ((CraftEntity) entity).getHandle(), getPathfindingRadius(), true, false, false, true);
        navigateTo(path, speed);
    }

    @Override
    public void navigateTo(int x, int y, int z, double speed) {
        PathEntity path = world.a(this, x, y, z, getPathfindingRadius(), true, false, false, true);
        navigateTo(path, speed);
    }

    private void navigateTo(PathEntity path, double speed) {
        setPathEntity(path);
        getNavigation().a(path, speed);
    }

    @Override
    public void stopNavigating() {
        getNavigation().h();
    }

    @Override
    public boolean isNavigating() {
        return getNavigation().g();
    }

    @Override
    public int getMaxHeadRotation() {
        return NMSEntityUtil.getMaxHeadRotation(this);
    }

    @Override
    public void updateMotion(float sideMotion, float forwardMotion) {
        super.e(sideMotion, forwardMotion);
    }

    @Override
    public boolean onInteract(org.bukkit.entity.Player player) {
        if (IdentUtil.areIdentical(player, pet.getOwner())) {
            pet.onInteract(player);
            return true;
        }
        return false;
    }
}