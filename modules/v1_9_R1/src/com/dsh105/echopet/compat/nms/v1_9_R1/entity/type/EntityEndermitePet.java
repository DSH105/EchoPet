package com.dsh105.echopet.compat.nms.v1_9_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityEndermitePet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityPet;

import net.minecraft.server.v1_9_R1.*;

@EntitySize(width = 0.4F, height = 0.3F)
@EntityPetType(petType = PetType.ENDERMITE)
public class EntityEndermitePet extends EntityPet implements IEntityEndermitePet {

    public EntityEndermitePet(World world) {
        super(world);
    }

    public EntityEndermitePet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.TINY;
    }
    
    @Override
	protected void a(BlockPosition paramBlockPosition, Block paramBlock){
		a(SoundEffects.bd, 0.15F, 1.0F);
    }
    
    @Override
	protected SoundEffect getIdleSound(){
		return SoundEffects.ba;
    }

    @Override
	protected SoundEffect getDeathSound(){
		return SoundEffects.bb;
    }

    @Override
    public void onLive() {
        super.onLive();
        for (int i = 0; i < 2; i++) {
            this.world.addParticle(EnumParticle.PORTAL, this.locX + (this.random.nextDouble() - 0.5D) * this.width, this.locY + this.random.nextDouble() * this.length, this.locZ + (this.random.nextDouble() - 0.5D) * this.width, (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D, new int[0]);
        }
    }
}