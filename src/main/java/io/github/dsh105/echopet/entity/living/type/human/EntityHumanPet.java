package io.github.dsh105.echopet.entity.living.type.human;

import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.living.SizeCategory;
import io.github.dsh105.echopet.logger.Logger;
import io.github.dsh105.echopet.util.ReflectionUtil;
import net.minecraft.server.v1_6_R3.*;
import org.bukkit.Location;

public class EntityHumanPet extends EntityLivingPet {

    private Packet20NamedEntitySpawn packet;
    private Packet40EntityMetadata metaPacket;
    private DataWatcher dw;
    protected int equipmentId = 0;
    protected byte b0 = 0;
    protected boolean init;

    public EntityHumanPet(World world) {
        super(world);
    }

    public EntityHumanPet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.6F, 0.9F);
        this.fireProof = true;
        this.dw = new DataWatcher();
        this.packet = new Packet20NamedEntitySpawn();
    }

    private void createPacket() {
        packet.a = this.getBukkitEntity().getEntityId();
        packet.b = this.pet.getPetName();
        packet.c = (int) this.locX * 32;
        packet.d = (int) this.locY * 32;
        packet.e = (int) this.locZ * 32;
        packet.f = this.angle(this.pitch);
        packet.g = this.angle(this.yaw);
        packet.h = this.equipmentId;
        try {
            ReflectionUtil.setValue(this.packet, "i", this.dw);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create Human Pet packet.", e, true);
        }
    }

    private void updateDatawatcher() {
        this.dw.watch(0, (Object) (byte) this.b0/*(this.isInvisible() ? 32 : this.isSneaking() ? 2 : this.isSprinting() ? 8 : 0)*/);
        this.dw.watch(1, (Object) (short) 0);
        this.dw.watch(8, (Object) (byte) 0);
        this.dw.watch(10, (Object) (String) this.pet.getPetName());
        this.metaPacket = new Packet40EntityMetadata(id, this.dw, true);
        try {
            ReflectionUtil.sendPacket(new Location(this.world.getWorld(), this.locX, this.locY, this.locZ), this.metaPacket);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create Metadata Packet for Human Pet.", e, true);
        }
    }

    protected void updatePacket() {
        this.createPacket();
        try {
            ReflectionUtil.sendPacket(new Location(this.world.getWorld(), this.locX, this.locY, this.locZ), this.packet);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create Metadata Packet for Human Pet.", e, true);
        }
    }

    private void init() {
        this.dw.a(0, (Object) (byte) 0);
        this.dw.a(1, (Object) (short) 0);
        this.dw.a(8, (Object) (byte) 0);
        this.dw.a(10, (Object) (String) "Human Pet");
        this.metaPacket = new Packet40EntityMetadata(id, this.dw, true);
        this.updatePacket();
    }

    private byte angle(float f) {
        return (byte) (f * 256.0F / 360.0F);
    }

    @Override
    public void l_() {
        super.l_();
        if (this.isInvisible()) {
            this.b0 = 32;
        } else if (this.isSneaking()) {
            this.b0 = 2;
        } else if (this.isSprinting()) {
            this.b0 = 8;
        } else {
            this.b0 = 0;
        }
        if (!this.init) {
            this.init();
            this.init = true;
        }
        this.updateDatawatcher();
    }

    @Override
    protected String getIdleSound() {
        return "random.breathe";
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("step.grass", 0.15F, 1.0F);
    }

    @Override
    protected String getDeathSound() {
        return "random.classic_hurt";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }

    protected void animate(int i) {
        Packet18ArmAnimation animation = new Packet18ArmAnimation();
        animation.a = this.getBukkitEntity().getEntityId();
        animation.b = i;
        try {
            ReflectionUtil.sendPacket(new Location(this.world.getWorld(), this.locX, this.locY, this.locZ), animation);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create Animation Packet for Human Pet.", e, true);
        }
    }


    @Override
    public void remove() {
        this.animate(2);
        super.remove();
    }
}