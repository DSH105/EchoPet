package com.dsh105.echopet.nms.v1_7_R4.entity.type;

import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.entitypet.EntityPetModifier;
import com.dsh105.echopet.api.entity.entitypet.type.EntityHorsePet;
import com.dsh105.echopet.api.entity.pet.type.HorsePet;
import com.dsh105.echopet.nms.v1_7_R4.entity.EchoEntityPetBase;
import com.dsh105.echopet.nms.v1_7_R4.entity.EchoEntityPetHandle;
import net.minecraft.server.v1_7_R4.*;

public class EchoEntityHorsePet extends EntityHorse implements IAnimal, EchoEntityPetHandle, EntityHorsePet {

    private EntityPetModifier<HorsePet> modifier;

    public EchoEntityHorsePet(World world) {
        super(world);
    }

    public EchoEntityHorsePet(World world, HorsePet pet) {
        super(world);
        this.modifier = new EchoEntityPetBase<>(pet, this);
    }

    /*
     * Implementation of methods required for use by EchoPet
     */

    @Override
    public Attributes.HorseColor getColor() {
        return Attributes.HorseColor.values()[this.getVariant() & 0xFF];
    }

    @Override
    public Attributes.HorseStyle getStyle() {
        return Attributes.HorseStyle.values()[this.getVariant() >>> 8];
    }

    @Override
    public Attributes.HorseArmour getArmour() {
        return Attributes.HorseArmour.values()[this.cl()];
    }

    @Override
    public boolean isSaddled() {
        return this.cu();
    }

    @Override
    public boolean isChested() {
        return this.hasChest();
    }

    @Override
    public void setHorseVariant(Attributes.HorseVariant variant) {
        if (variant != Attributes.HorseVariant.NORMAL) {
            setArmour(Attributes.HorseArmour.NONE);
        }
        this.setType(variant.ordinal());
    }

    @Override
    public Attributes.HorseVariant getHorseVariant() {
        return Attributes.HorseVariant.values()[this.getType()];
    }

    @Override
    public void setColor(Attributes.HorseColor color) {
        this.setVariant(color.ordinal() & 0xFF | getStyle().ordinal() << 8);
    }

    @Override
    public void setStyle(Attributes.HorseStyle style) {
        this.setVariant(getColor().ordinal() & 0xFF | style.ordinal() << 8);
    }

    @Override
    public void setArmour(Attributes.HorseArmour armour) {
        if (getHorseVariant() == Attributes.HorseVariant.NORMAL) {
            this.datawatcher.watch(DATAWATCHER_HORSE_ARMOUR, armour.ordinal());
        }
    }

    @Override
    public void setSaddled(boolean flag) {
        this.n(flag);
    }

    @Override
    public void setChested(boolean flag) {
        this.setHasChest(flag);
    }

    @Override
    public void animation(int animationId, boolean flag) {
        int value = this.datawatcher.getInt(16);

        if (flag) {
            this.datawatcher.watch(DATAWATCHER_ANIMATION, value | animationId);
        } else {
            this.datawatcher.watch(DATAWATCHER_ANIMATION, value & ~animationId);
        }
    }

    @Override
    public void setBaby(boolean flag) {
        this.setAge(flag ? Integer.MIN_VALUE : 0);
    }

    @Override
    public void resetLove() {
        this.cf();
    }

    @Override
    public HorsePet getPet() {
        return modifier.getPet();
    }

    @Override
    public EntityPetModifier<HorsePet> getModifier() {
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

    @Override
    public void setTame(boolean flag) {
        // nope; avoid any other 'tame' interactions
        super.setTame(false);
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

    /*
     * EntityHorse stuff
     */

    @Override
    public void g(EntityHuman entityhuman) {
        // no horse inventories; yet
    }
}