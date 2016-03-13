package com.dsh105.echopet.compat.nms.v1_9_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityGuardianPet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityPet;

import net.minecraft.server.v1_9_R1.*;

@EntitySize(width = 0.85F, height = 0.85F)
@EntityPetType(petType = PetType.GUARDIAN)
public class EntityGuardianPet extends EntityPet implements IEntityGuardianPet {

	private static final DataWatcherObject<Byte> a = DataWatcher.a(EntityGuardian.class, DataWatcherRegistry.a);
	private static final DataWatcherObject<Integer> b = DataWatcher.a(EntityGuardian.class, DataWatcherRegistry.b);

    public EntityGuardianPet(World world) {
        super(world);
    }

    public EntityGuardianPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
	protected SoundEffect getIdleSound(){
		if(isElder()){ return isInWater() ? SoundEffects.aD : SoundEffects.aE; }
		return isInWater() ? SoundEffects.ce : SoundEffects.cf;
    }

    @Override
	protected SoundEffect getDeathSound(){
		if(isElder()){ return isInWater() ? SoundEffects.aG : SoundEffects.aH; }
		return isInWater() ? SoundEffects.ch : SoundEffects.ci;
    }

    @Override
    public SizeCategory getSizeCategory() {
        return isElder() ? SizeCategory.GIANT : SizeCategory.LARGE;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
		this.datawatcher.register(a, Byte.valueOf((byte) 0));
		this.datawatcher.register(b, Integer.valueOf(0));
    }

    @Override
    public void onLive() {
        super.onLive();
    }

    @Override
    public boolean isElder() {
		return a(4);
    }

    @Override
    public void setElder(boolean flag) {
		a(4, flag);
    }

	private boolean a(int paramInt){
		return (((Byte) this.datawatcher.get(a)).byteValue() & paramInt) != 0;
	}

	private void a(int paramInt, boolean paramBoolean){
		int i = ((Byte) this.datawatcher.get(a)).byteValue();
		if(paramBoolean){
			this.datawatcher.set(a, Byte.valueOf((byte) (i | paramInt)));
		}else{
			this.datawatcher.set(a, Byte.valueOf((byte) (i & (paramInt ^ 0xFFFFFFFF))));
		}
	}
}
