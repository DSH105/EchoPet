package io.github.dsh105.echopet.entity.type.human;

import com.dsh105.dshutils.logger.Logger;
import com.dsh105.dshutils.util.ReflectionUtil;
import io.github.dsh105.echopet.entity.*;
import net.minecraft.server.v1_7_R2.Packet;
import net.minecraft.server.v1_7_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R2.World;
import net.minecraft.util.com.mojang.authlib.GameProfile;

@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.HUMAN)
public class EntityHumanPet extends EntityPacketPet {

    protected GameProfile profile;
    protected int equipmentId = 0;

    public EntityHumanPet(World world) {
        super(world);
    }

    public EntityHumanPet(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    public Packet createPacket() {
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
        this.profile = new GameProfile(this.id + "", this.pet.getPetName());
        try {
            ReflectionUtil.setValue(packet, "a", this.id);
            ReflectionUtil.setValue(packet, "b", this.profile);
            ReflectionUtil.setValue(packet, "c", (int) this.locX * 32);
            ReflectionUtil.setValue(packet, "d", (int) this.locY * 32);
            ReflectionUtil.setValue(packet, "e", (int) this.locZ * 32);
            ReflectionUtil.setValue(packet, "f", this.angle(this.pitch));
            ReflectionUtil.setValue(packet, "g", this.angle(this.yaw));
            ReflectionUtil.setValue(packet, "h", this.equipmentId);
            ReflectionUtil.setValue(packet, "i", this.dw);
            return packet;
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create Human Pet packet.", e, true);
        }
        return null;
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
}
