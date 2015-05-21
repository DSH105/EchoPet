package com.dsh105.echopet.compat.nms.v1_8_R3.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityRabbitPet;
import com.dsh105.echopet.compat.nms.v1_8_R3.entity.EntityAgeablePet;
import com.dsh105.echopet.compat.nms.v1_8_R3.entity.EntityPet;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.entity.Rabbit;

@EntitySize(width = 0.6F, height = 0.7F)
@EntityPetType(petType = PetType.RABBIT)
public class EntityRabbitPet extends EntityAgeablePet implements IEntityRabbitPet {

    private int jumpDelay;

    public EntityRabbitPet(World world) {
        super(world);
    }

    public EntityRabbitPet(World world, IPet pet) {
        super(world, pet);
        this.jumpDelay = this.random.nextInt(15) + 10;
    }

    @Override
    protected String getIdleSound() {
        return "mob.rabbit.idle";
    }

    @Override
    protected String getDeathSound() {
        return "mob.rabbit.hurt";
    }

    @Override
    public Rabbit.Type getRabbitType() {
        return TypeMapping.fromMagic(this.datawatcher.getByte(18));
    }
    
    @Override
    public void setRabbitType(Rabbit.Type type) {
        this.datawatcher.watch(18, Byte.valueOf((byte) TypeMapping.toMagic(type)));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(18, Byte.valueOf((byte)0));
    }
    
    @Override
    public void onLive() {
        super.onLive();
        // same as the slime
        if (this.onGround && this.jumpDelay-- <= 0) {
            getControllerJump().a();
            this.jumpDelay = this.random.nextInt(15) + 10;
            this.world.broadcastEntityEffect(this, (byte) 1);
        }
    }
    
    static class TypeMapping {
        
        private static final int[] NMS_TYPES = new int[Rabbit.Type.values().length];
        private static final Rabbit.Type[] INVERSE = new Rabbit.Type[Rabbit.Type.values().length];
        
        static {
            set(Rabbit.Type.BROWN, 0);
            set(Rabbit.Type.WHITE, 1);
            set(Rabbit.Type.BLACK, 2);
            set(Rabbit.Type.BLACK_AND_WHITE, 3);
            set(Rabbit.Type.GOLD, 4);
            set(Rabbit.Type.SALT_AND_PEPPER, 5);
            set(Rabbit.Type.THE_KILLER_BUNNY, 99);
        }
        
        private static void set(Rabbit.Type type, int magicValue) {
            NMS_TYPES[type.ordinal()] = magicValue;
            if (magicValue < INVERSE.length) {
                INVERSE[magicValue] = type;
            }
        }
        
        protected static Rabbit.Type fromMagic(int magicValue) {
            if (magicValue < INVERSE.length) {
                return INVERSE[magicValue];
            } else if (magicValue == 99) {
                return Rabbit.Type.THE_KILLER_BUNNY;
            }
            // a default
            return Rabbit.Type.BROWN;
        }
        
        protected static int toMagic(Rabbit.Type type) {
            return NMS_TYPES[type.ordinal()];
        }
        
    }
}
