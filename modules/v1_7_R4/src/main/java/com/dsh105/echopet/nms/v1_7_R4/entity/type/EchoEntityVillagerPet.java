package com.dsh105.echopet.nms.v1_7_R4.entity.type;

import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.entitypet.EntityPetModifier;
import com.dsh105.echopet.api.entity.entitypet.type.EntityVillagerPet;
import com.dsh105.echopet.api.entity.pet.type.VillagerPet;
import com.dsh105.echopet.nms.v1_7_R4.entity.EchoEntityPetBase;
import com.dsh105.echopet.nms.v1_7_R4.entity.EchoEntityPetHandle;
import net.minecraft.server.v1_7_R4.*;

public class EchoEntityVillagerPet extends EntityVillager implements IAnimal, EchoEntityPetHandle, EntityVillagerPet {

    private EntityPetModifier<VillagerPet> modifier;

    public EchoEntityVillagerPet(World world) {
        super(world);
    }

    public EchoEntityVillagerPet(World world, VillagerPet pet) {
        super(world);
        this.modifier = new EchoEntityPetBase<>(pet, this);
    }

    /*
     * Implementation of methods required for use by EchoPet
     */

    @Override
    public void setBaby(boolean flag) {
        this.setAge(flag ? Integer.MIN_VALUE : 0);
    }

    @Override
    public void setVillagerProfession(Attributes.VillagerProfession profession) {
        super.setProfession(profession.ordinal());
    }

    @Override
    public Attributes.VillagerProfession getVillagerProfession() {
        return Attributes.VillagerProfession.valueOf(super.getProfession());
    }

    @Override
    public void setCareer(Attributes.VillagerCareer career) {
        // TODO
    }

    @Override
    public Attributes.VillagerCareer getCareer() {
        // TODO
        return null;
    }

    @Override
    public void resetLove() {
        // only for animals (but present in AgeablePet)
    }

    @Override
    public VillagerPet getPet() {
        return modifier.getPet();
    }

    @Override
    public EntityPetModifier<VillagerPet> getModifier() {
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
     * EntityAnimal stuff
     */

    @Override
    protected void a(Entity entity, float f) {
        // do nothing
    }

    @Override
    protected Entity findTarget() {
        return null;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable entityageable) {
        return null;
    }
}