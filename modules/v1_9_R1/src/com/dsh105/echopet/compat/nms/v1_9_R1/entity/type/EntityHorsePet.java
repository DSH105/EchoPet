/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.nms.v1_9_R1.entity.type;

import java.util.UUID;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorsePet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityAgeablePet;
import com.google.common.base.Optional;

import net.minecraft.server.v1_9_R1.*;

@EntitySize(width = 1.4F, height = 1.6F)
@EntityPetType(petType = PetType.HORSE)
public class EntityHorsePet extends EntityAgeablePet implements IEntityHorsePet {

	private static final DataWatcherObject<Byte> bE = DataWatcher.a(EntityHorse.class, DataWatcherRegistry.a);// Visual??
	private static final DataWatcherObject<Integer> bF = DataWatcher.a(EntityHorse.class, DataWatcherRegistry.b);// type
	private static final DataWatcherObject<Integer> bG = DataWatcher.a(EntityHorse.class, DataWatcherRegistry.b);// variant
	private static final DataWatcherObject<Optional<UUID>> bH = DataWatcher.a(EntityHorse.class, DataWatcherRegistry.m);
	private static final DataWatcherObject<Integer> bI = DataWatcher.a(EntityHorse.class, DataWatcherRegistry.b);// armor
    private int rearingCounter = 0;
    int stepSoundCount = 0;

    public EntityHorsePet(World world) {
        super(world);
    }

    public EntityHorsePet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public void setSaddled(boolean flag) {
		this.setHorseVisual(4, flag);
    }

    @Override
    public void setType(HorseType t) {
        if (t != HorseType.NORMAL) {
            this.setArmour(HorseArmour.NONE);
        }
		this.datawatcher.set(bF, Integer.valueOf(t.getId()));
    }

    @Override
    public void setVariant(HorseVariant v, HorseMarking m) {
		this.datawatcher.set(bG, m.getId(v));
    }

    @Override
    public void setArmour(HorseArmour a) {
		this.datawatcher.set(bI, Integer.valueOf(a.getId()));
    }

    @Override
    public void setChested(boolean flag) {
        this.setHorseVisual(8, flag);
    }

    @Override
    public boolean attack(Entity entity) {
        boolean flag = super.attack(entity);
        if (flag) {
			setHorseVisual(64, true);
			if(getType().g()){
				this.a(SoundEffects.co, 1.0F, 1.0F);
			}else{
				this.a(SoundEffects.ay, 1.0F, 1.0F);
			}
        }
        return flag;
    }

	public EnumHorseType getType(){
		return EnumHorseType.a(((Integer) this.datawatcher.get(bF)).intValue());
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
		this.datawatcher.register(bE, Byte.valueOf((byte) 0));
		this.datawatcher.register(bF, Integer.valueOf(EnumHorseType.HORSE.k()));
		this.datawatcher.register(bG, Integer.valueOf(0));
		this.datawatcher.register(bH, Optional.absent());
		this.datawatcher.register(bI, Integer.valueOf(EnumHorseArmor.NONE.a()));
    }

    @Override
	protected SoundEffect getIdleSound(){
		return SoundEffects.cn;
    }

    @Override
    protected void makeStepSound(int i, int j, int k, Block block) {
		SoundEffectType soundeffecttype = block.w();

        if (this.world.getType(new BlockPosition(i, j + 1, k)) == Blocks.SNOW) {
			soundeffecttype = Blocks.SNOW_LAYER.w();
        }

		if(!block.getBlockData().getMaterial().isLiquid()){
			EnumHorseType enumhorsetype = getType();
			if((isVehicle()) && (!enumhorsetype.g())){
				this.stepSoundCount += 1;
				if((this.stepSoundCount > 5) && (this.stepSoundCount % 3 == 0)){
					a(SoundEffects.ct, soundeffecttype.a() * 0.15F, soundeffecttype.b());
					if((enumhorsetype == EnumHorseType.HORSE) && (this.random.nextInt(10) == 0)){
						a(SoundEffects.cq, soundeffecttype.a() * 0.6F, soundeffecttype.b());
					}
				}else if(this.stepSoundCount <= 5){
					a(SoundEffects.cz, soundeffecttype.a() * 0.15F, soundeffecttype.b());
				}
			}else if(soundeffecttype == SoundEffectType.a){
				a(SoundEffects.cz, soundeffecttype.a() * 0.15F, soundeffecttype.b());
			}else{
				a(SoundEffects.cy, soundeffecttype.a() * 0.15F, soundeffecttype.b());
			}
        }
    }

    @Override
    public void g(float sideMot, float forwMot) {
        super.g(sideMot, forwMot);
        if (forwMot <= 0.0F) {
            this.stepSoundCount = 0;
        }
    }

    @Override
	protected SoundEffect getDeathSound(){
		return SoundEffects.cr;
    }

    @Override
    public SizeCategory getSizeCategory() {
        if (this.isBaby()) {
            return SizeCategory.TINY;
        } else {
            return SizeCategory.LARGE;
        }
    }

    @Override
    public void onLive() {
        super.onLive();
        if (rearingCounter > 0 && ++rearingCounter > 20) {
			setHorseVisual(64, false);
        }
    }

    @Override
    protected void doJumpAnimation() {
		this.a(SoundEffects.cu, 0.4F, 1.0F);
        this.rearingCounter = 1;
		setHorseVisual(64, true);
    }

	/**
	 * 2: Is tamed
	 * 4: Saddle
	 * 8: Has Chest
	 * 16: Bred
	 * 32: Eating haystack
	 * 64: Rear
	 * 128: Mouth open
	 */
	private boolean getHorseVisual(int i){
		return (((Byte) this.datawatcher.get(bE)).byteValue() & i) != 0;
	}
	
	/**
	 * 2: Is tamed
	 * 4: Saddle
	 * 8: Has Chest
	 * 16: Bred
	 * 32: Eating haystack
	 * 64: Rear
	 * 128: Mouth open
	 */
	private void setHorseVisual(int i, boolean flag){
		byte b0 = ((Byte) this.datawatcher.get(bE)).byteValue();
		if(flag){
			this.datawatcher.set(bE, Byte.valueOf((byte) (b0 | i)));
		}else{
			this.datawatcher.set(bE, Byte.valueOf((byte) (b0 & (i ^ 0xFFFFFFFF))));
		}
	}
}
