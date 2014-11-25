package com.dsh105.echopet.nms.v1_7_R4.entity.type;

import com.dsh105.echopet.api.entity.entitypet.EntityPetModifier;
import com.dsh105.echopet.api.entity.entitypet.type.EntityEnderDragonPet;
import com.dsh105.echopet.api.entity.pet.type.EnderDragonPet;
import com.dsh105.echopet.nms.v1_7_R4.entity.EchoEntityPetBase;
import com.dsh105.echopet.nms.v1_7_R4.entity.EchoEntityPetHandle;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

public class EchoEntityEnderDragonPet extends EntityEnderDragon implements IAnimal, EchoEntityPetHandle, EntityEnderDragonPet {

    private EntityPetModifier<EnderDragonPet> modifier;

    public EchoEntityEnderDragonPet(World world) {
        super(world);
    }

    public EchoEntityEnderDragonPet(World world, EnderDragonPet pet) {
        super(world);
        this.modifier = new EchoEntityPetBase<>(pet, this);
        modifier.setNoClipEnabled(true);
    }

    /*
     * Implementation of methods required for use by EchoPet
     */

    @Override
    public EnderDragonPet getPet() {
        return modifier.getPet();
    }

    @Override
    public EntityPetModifier<EnderDragonPet> getModifier() {
        return modifier;
    }

    @Override
    public boolean isTargetingEnabled() {
        return this.bz;
    }

    @Override
    public void enableTargeting(boolean flag) {
        this.bz = flag;
    }

    @Override
    public void setHurtTicks(int value) {
        this.hurtTicks = value;
    }

    @Override
    public void setTargetCrystal(org.bukkit.entity.EnderCrystal entity) {
        this.bC = ((org.bukkit.craftbukkit.v1_7_R4.entity.CraftEnderCrystal) entity).getHandle();
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
    public void mobTick() {
        super.bp();
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
        super.bn();

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
        return getPet().getIdleSound();
    }

    @Override
    protected String aT() {
        return getPet().getHurtSound();
    }

    @Override
    protected String aU() {
        return getPet().getDeathSound();
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
     * EntityEnderDragon stuff
     */

    @Override
    public void tickMovement() {
        super.e();
    }

    @Override
    public boolean a(EntityComplexPart entitycomplexpart, DamageSource damagesource, float f) {
        return false;
    }

    @Override
    protected void aF() {
        // Prevent any crazy death explosions
    }

    @Override
    public void updatePosition(double x, double y, double z) {

    }

    @Override
    public void target() {
        this.bz = false;
        if (this.random.nextInt(2) == 0 && !this.world.players.isEmpty()) {
            if (this.random.nextInt(50) <= 40 && this.getPet().getOwner() != null) {
                getPet().setTarget((((CraftPlayer) this.getPet().getOwner()).getHandle()).getBukkitEntity());
            } else {
                getPet().setTarget(((Entity) this.world.players.get(this.random.nextInt(this.world.players.size()))).getBukkitEntity());
            }
        } else {
            boolean flag;

            do {
                this.h = 0.0D;
                this.i = (double) (70.0F + this.random.nextFloat() * 50.0F);
                this.j = 0.0D;
                this.h += (double) (this.random.nextFloat() * 120.0F - 60.0F);
                this.j += (double) (this.random.nextFloat() * 120.0F - 60.0F);
                double d0 = this.locX - this.h;
                double d1 = this.locY - this.i;
                double d2 = this.locZ - this.j;

                flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
            } while (!flag);

            getPet().setTarget(null);
        }
    }
}