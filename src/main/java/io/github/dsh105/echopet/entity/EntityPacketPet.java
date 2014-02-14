package io.github.dsh105.echopet.entity;

import com.dsh105.dshutils.logger.Logger;
import com.dsh105.dshutils.util.ReflectionUtil;
import net.minecraft.server.v1_7_R1.*;
import org.bukkit.Location;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public abstract class EntityPacketPet extends EntityPet {

    private PacketPlayOutEntityMetadata metaPacket;
    protected DataWatcher dw;
    protected byte b0 = 0;
    protected boolean init;
    protected int id;

    public EntityPacketPet(World world) {
        super(world);
    }

    public EntityPacketPet(World world, Pet pet) {
        super(world, pet);
        Field f = null;
        try {
            f = Entity.class.getDeclaredField("id");
            f.setAccessible(true);
            this.id = (Integer) f.get(this);
            this.dw = new DataWatcher(this);
        } catch (NoSuchFieldException e) {
            Logger.log(Logger.LogLevel.WARNING, "Error creating new EntityPacketPet.", e, true);
            this.remove(false);
        } catch (SecurityException e) {
            Logger.log(Logger.LogLevel.WARNING, "Error creating new EntityPacketPet.", e, true);
            this.remove(false);
        } catch (IllegalArgumentException e) {
            Logger.log(Logger.LogLevel.WARNING, "Error creating new EntityPacketPet.", e, true);
            this.remove(false);
        } catch (IllegalAccessException e) {
            Logger.log(Logger.LogLevel.WARNING, "Error creating new EntityPacketPet.", e, true);
            this.remove(false);
        }
    }

    @Override
    public void remove(boolean makeSound) {
        bukkitEntity.remove();
    }

    @Override
    public void onLive() {
        super.onLive();
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

    protected byte angle(float f) {
        return (byte) (f * 256.0F / 360.0F);
    }

    public abstract Packet createPacket();

    public void updatePacket() {
        ReflectionUtil.sendPacket(new Location(this.world.getWorld(), this.locX, this.locY, this.locZ), this.createPacket());
    }

    private void updateDatawatcher() {
        this.dw.watch(0, (Object) (byte) this.b0/*(this.isInvisible() ? 32 : this.isSneaking() ? 2 : this.isSprinting() ? 8 : 0)*/);
        this.dw.watch(1, (Object) (short) 0);
        this.dw.watch(8, (Object) (byte) 0);
        this.dw.watch(10, (Object) (String) this.pet.getPetName());
        this.metaPacket = new PacketPlayOutEntityMetadata(this.id, this.dw, true);
        ReflectionUtil.sendPacket(new Location(this.world.getWorld(), this.locX, this.locY, this.locZ), this.metaPacket);
    }

    public boolean hasInititiated() {
        return this.init;
    }

    private void init() {
        this.dw.a(0, (Object) (byte) 0);
        this.dw.a(1, (Object) (short) 0);
        this.dw.a(8, (Object) (byte) 0);
        this.dw.a(10, (Object) (String) "Human Pet");
        try {
            this.metaPacket = new PacketPlayOutEntityMetadata(this.id, this.dw, true);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create Metadata Packet for Human Pet.", e, true);
        }
        this.updatePacket();
    }
}