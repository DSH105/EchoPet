package com.dsh105.echopet.nms.v1_7_R4.entity.type;

import com.dsh105.commodus.container.ItemStackContainer;
import com.dsh105.echopet.api.entity.entitypet.EntityPetModifier;
import com.dsh105.echopet.api.entity.entitypet.type.EntityZombiePigmanPet;
import com.dsh105.echopet.api.entity.pet.type.ZombiePigmanPet;
import com.dsh105.echopet.nms.v1_7_R4.entity.EchoEntityPetBase;
import com.dsh105.echopet.nms.v1_7_R4.entity.EchoEntityPetHandle;
import net.minecraft.server.v1_7_R4.*;
import net.minecraft.server.v1_7_R4.World;
import org.bukkit.*;
import org.bukkit.inventory.*;

public class EchoEntityZombiePigmanPet extends EntityPigZombie implements IAnimal, EchoEntityPetHandle, EntityZombiePigmanPet {

    private EntityPetModifier<ZombiePigmanPet> modifier;

    public EchoEntityZombiePigmanPet(World world) {
        super(world);
    }

    public EchoEntityZombiePigmanPet(World world, ZombiePigmanPet pet) {
        super(world);
        this.modifier = new EchoEntityPetBase<>(pet, this);
    }

    /*
     * Implementation of methods required for use by EchoPet
     */

    @Override
    public ZombiePigmanPet getPet() {
        return modifier.getPet();
    }

    @Override
    public EntityPetModifier<ZombiePigmanPet> getModifier() {
        return modifier;
    }

    @Override
    public void applyDefaultItems() {
        getPet().setWeapon(ItemStackContainer.of(new org.bukkit.inventory.ItemStack(org.bukkit.Material.GOLD_SPADE)));
    }

    @Override
    public void resetLove() {

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

    /*
     * EntityZombie stuff
     */

    @Override
    public boolean cc() {
        // nope, not converting
        return false;
    }

    @Override
    protected void cd() {
        // nope, not converting
    }

    @Override
    protected void a(int i) {
        // nope, not converting
    }
}