package io.github.dsh105.echopet.entity.inanimate;

import io.github.dsh105.dshutils.logger.Logger;
import io.github.dsh105.dshutils.util.ReflectionUtil;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import net.minecraft.server.v1_7_R1.*;
import org.bukkit.Location;

import java.lang.reflect.Field;

public abstract class EntityInanimatePet extends EntityPet {

    private Packet packet;
    private PacketPlayOutEntityMetadata metaPacket;
    private DataWatcher dw;
    protected byte b0 = 0;
    protected boolean init;
    private int id;

    public EntityInanimatePet(World world, InanimatePet pet) {
        super(world, pet);
        try {
            Field f = Entity.class.getDeclaredField("id");
            f.setAccessible(true);
            this.id = (Integer) f.get(this);
            this.dw = new DataWatcher(this);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.WARNING, "Error creating new EntityInanimatePet.", e, true);
            this.remove(false);
        }
    }

    @Override
    public LivingPet getPet() {
        Pet p = super.getPet();
        if (p instanceof LivingPet) {
            return (LivingPet) super.getPet();
        }
        return null;
    }

    @Override
    public void remove(boolean makeSound) {
        bukkitEntity.remove();
    }

    @Override
    public CraftInanimatePet getBukkitEntity() {
        if (this.bukkitEntity == null || !(this.bukkitEntity instanceof CraftInanimatePet)) {
            CraftInanimatePet craftPet = new CraftInanimatePet(this.world.getServer(), this);
            this.bukkitEntity = craftPet;
            return craftPet;
        }
        return (CraftInanimatePet) this.bukkitEntity;
    }

    @Override
    public void onLive() {
        if (!this.init) {
            this.init();
            this.init = true;
        }
        this.updateDatawatcher();
    }

    public abstract Packet createPacket();

    public abstract void updatePacket();

    private void updateDatawatcher() {
        this.dw.watch(0, (Object) (byte) this.b0/*(this.isInvisible() ? 32 : this.isSneaking() ? 2 : this.isSprinting() ? 8 : 0)*/);
        this.dw.watch(1, (Object) (short) 0);
        this.dw.watch(8, (Object) (byte) 0);
        this.dw.watch(10, (Object) (String) this.pet.getName());
        try {
            this.metaPacket = new PacketPlayOutEntityMetadata(this.id, this.dw, true);
            ReflectionUtil.sendPacket(new Location(this.world.getWorld(), this.locX, this.locY, this.locZ), this.metaPacket);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create Metadata Packet for Human Pet.", e, true);
        }
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