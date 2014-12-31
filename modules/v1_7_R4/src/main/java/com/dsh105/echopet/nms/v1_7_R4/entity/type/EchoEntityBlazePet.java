package com.dsh105.echopet.nms.v1_7_R4.entity.type;

import com.dsh105.commodus.Affirm;
import com.dsh105.echopet.api.entity.entitypet.EntityPetModifier;
import com.dsh105.echopet.api.entity.entitypet.type.EntityBlazePet;
import com.dsh105.echopet.api.entity.pet.type.BlazePet;
import com.dsh105.echopet.nms.v1_7_R4.entity.EchoEntityPetBase;
import com.dsh105.echopet.nms.v1_7_R4.entity.EchoEntityPetHandle;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public class EchoEntityBlazePet extends EntityBlaze implements IAnimal, EchoEntityPetHandle, EntityBlazePet {

    private EntityPetModifier<BlazePet> modifier;

    public EchoEntityBlazePet(World world) {
        super(world);
    }

    public EchoEntityBlazePet(World world, BlazePet pet) {
        super(world);
        this.modifier = new EchoEntityPetBase<>(pet, this);
    }

    /*
     * Implementation of methods required for use by EchoPet
     */

    @Override
    public void rangedAttack(Object entity, float speed) {
        Affirm.checkInstanceOf(LivingEntity.class, entity);
        super.a(((CraftLivingEntity) entity).getHandle(), speed);
    }

    @Override
    public void setOnFire(boolean flag) {
        this.a(flag);
    }

    @Override
    public boolean isOnFire() {
        return this.isBurning();
    }

    @Override
    public BlazePet getPet() {
        return modifier.getPet();
    }

    @Override
    public EntityPetModifier<BlazePet> getModifier() {
        return modifier;
    }

    @Override
    public void checkDespawn() {
        super.w();
    }

    @Override
    public void incrementAge() {
        ++this.aU;
    }

    @Override
    public EntitySenses getEntitySenses() {
        return super.getEntitySenses();
    }

    @Override
    public Navigation getNavigation() {
        return super.getNavigation();
    }

    @Override
    public void mobTick() {
        super.bp();
    }

    @Override
    public ControllerMove getControllerMove() {
        return super.getControllerMove();
    }

    @Override
    public ControllerJump getControllerJump() {
        return super.getControllerJump();
    }

    @Override
    public ControllerLook getControllerLook() {
        return super.getControllerLook();
    }

    @Override
    public void applyPitchAndYawChanges(float f, float f1) {
        super.b(f, f1);
    }

    @Override
    public void updateMotion(float sideMotion, float forwardMotion) {
        super.e(sideMotion, forwardMotion);
    }

    @Override
    public void modifyBoundingBox(float f, float f1) {
        super.a(f, f1);
    }

    @Override
    public void setFireProof(boolean flag) {
        this.fireProof = flag;
    }

    @Override
    public boolean isFireProof() {
        return this.fireProof;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public float getSoundVolume() {
        return super.bf();
    }

    /*
     * Overridden entity methods
     */

    // So it won't despawn if there are no nearby players
    @Override
    public boolean isPersistent() {
        return modifier.isPersistent();
    }

    // Whether to use the new NMS AI or not
    @Override
    public boolean bk() {
        return modifier == null || modifier.useNewAi();
    }

    // 'on tick' function
    @Override
    protected void bn() {
        modifier.onTick();
    }

    @Override
    public void move(double x, double y, double z) {
        if (getPet().isStationary()) {
            return;
        }
        super.move(x, y, z);
    }

    @Override
    public void g(double x, double y, double z) {
        if (getPet().isStationary()) {
            super.g(0, 0, 0);
            return;
        }
        super.g(x, y, z);
    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        if (entityhuman.getBukkitEntity() instanceof org.bukkit.entity.Player) {
            try {
                return getPet().onInteract((org.bukkit.entity.Player) entityhuman.getBukkitEntity());
            } catch (Exception e) {
                getPet().onError(e);
            }
        }
        // Nope!
        return false;
    }

    @Override
    public void e(float sideMotion, float forwardMotion) {
        try {
            // Call the ride function
            getPet().onRide(sideMotion, forwardMotion);
        } catch (Exception e) {
            getPet().onError(e);
        }
    }

    // 'on live' function
    @Override
    public void h() {
        super.h();
        try {
            getPet().onLive();
        } catch (Exception e) {
            getPet().onError(e);
        }
    }

    @Override
    protected String t() {
        return getPet().getIdleSound().equals("default") ? super.t() : getPet().getIdleSound();
    }

    @Override
    protected String aT() {
        return getPet().getHurtSound().equals("default") ? super.aT() : getPet().getHurtSound();
    }

    @Override
    protected String aU() {
        return getPet().getDeathSound().equals("default") ? super.aT() : getPet().getDeathSound();
    }

    @Override
    protected void a(int i, int j, int k, Block block) {
        super.a(i, j, k, block);
        makeStepSound(i, j, k, block);
    }

    protected void makeStepSound(int i, int j, int k, Block block) {
        getPet().makeStepSound();
    }

    @Override
    public boolean n(Entity entity) {
        // cancel entity attacks
        return false;
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
     * EntityMonster stuff
     */

    @Override
    protected Entity findTarget() {
        return null;
    }

    @Override
    protected void a(Entity entity, float f) {
        // do nothing
    }
}