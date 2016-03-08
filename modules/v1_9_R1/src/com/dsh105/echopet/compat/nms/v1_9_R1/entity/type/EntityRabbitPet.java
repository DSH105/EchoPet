package com.dsh105.echopet.compat.nms.v1_9_R1.entity.type;

import org.bukkit.entity.Rabbit;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityRabbitPet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityAgeablePet;

import net.minecraft.server.v1_9_R1.*;

@EntitySize(width = 0.6F, height = 0.7F)
@EntityPetType(petType = PetType.RABBIT)
public class EntityRabbitPet extends EntityAgeablePet implements IEntityRabbitPet {

	private static final DataWatcherObject<Integer> bv = DataWatcher.a(EntityRabbit.class, DataWatcherRegistry.b);
    private int jumpDelay;

    public EntityRabbitPet(World world) {
        super(world);
    }

    public EntityRabbitPet(World world, IPet pet) {
        super(world, pet);
        this.jumpDelay = this.random.nextInt(15) + 10;
    }

    @Override
	protected SoundEffect getIdleSound(){
		return SoundEffects.eo;
    }

    @Override
	protected SoundEffect getDeathSound(){
		return SoundEffects.eq;
    }

    @Override
    public Rabbit.Type getRabbitType() {
		return TypeMapping.fromMagic(((Integer) this.datawatcher.get(bv)).intValue());
    }
    
    @Override
    public void setRabbitType(Rabbit.Type type) {
		this.datawatcher.set(bv, Integer.valueOf(type.ordinal()));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
		this.datawatcher.register(bv, Integer.valueOf(0));
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
