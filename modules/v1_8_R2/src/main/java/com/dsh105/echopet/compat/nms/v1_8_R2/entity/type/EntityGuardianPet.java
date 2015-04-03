package com.dsh105.echopet.compat.nms.v1_8_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityGuardianPet;
import com.dsh105.echopet.compat.nms.v1_8_R2.entity.EntityPet;
import net.minecraft.server.v1_8_R2.World;

@EntitySize(width = 0.85F, height = 0.85F)
@EntityPetType(petType = PetType.GUARDIAN)
public class EntityGuardianPet extends EntityPet implements IEntityGuardianPet {

    public EntityGuardianPet(World world) {
        super(world);
    }

    public EntityGuardianPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected String getIdleSound() {
        return isElder() ? "mob.guardian.elder.idle" : !V() ? "mob.guardian.land.idle" : "mob.guardian.idle";
    }

    @Override
    protected String getDeathSound() {
        return isElder() ? "mob.guardian.elder.hit" : !V() ? "mob.guardian.land.hit" : "mob.guardian.hit";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return isElder() ? SizeCategory.GIANT : SizeCategory.LARGE;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Integer(0));
        //this.datawatcher.a(17, new Integer(0)); //attack target
    }

    @Override
    public void onLive() {
        super.onLive();
        if (onGround && this.random.nextInt(20) < 2) {
            this.motY += 0.2D;
            this.onGround = false;
            this.ai = true; // airborne
        }
    }

    @Override
    public boolean isElder() {
        return retrieve(4);
    }

    @Override
    public void setElder(boolean flag) {
        update(4, flag);
    }
    
    private boolean retrieve(int i) {
        return (this.datawatcher.getInt(16) & i) != 0;
    }

    private void update(int i, boolean flag) {
        int existing = this.datawatcher.getInt(16);
        
        if (flag) {
            this.datawatcher.watch(16, Integer.valueOf(existing | i));
        } else {
            this.datawatcher.watch(16, Integer.valueOf(existing & (i ^ 0xFFFFFFFF)));
        }
    }
}