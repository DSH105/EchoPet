package com.dsh105.echopet.compat.nms.v1_8_R3.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityEndermitePet;
import com.dsh105.echopet.compat.nms.v1_8_R3.entity.EntityPet;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.World;

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
    protected void makeStepSound() {
        this.makeSound("mob.silverfish.step", 0.15F, 1.0F);
    }
    
    @Override
    protected String getIdleSound() {
        return "mob.silverfish.say";
    }

    @Override
    protected String getDeathSound() {
        return "mob.silverfish.hit";
    }

    @Override
    public void onLive() {
        super.onLive();
        for (int i = 0; i < 2; i++) {
            this.world.addParticle(EnumParticle.PORTAL, this.locX + (this.random.nextDouble() - 0.5D) * this.width, this.locY + this.random.nextDouble() * this.length, this.locZ + (this.random.nextDouble() - 0.5D) * this.width, (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D, new int[0]);
        }
    }
}