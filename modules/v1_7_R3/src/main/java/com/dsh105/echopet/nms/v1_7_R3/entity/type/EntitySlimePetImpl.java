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

package com.dsh105.echopet.nms.v1_7_R3.entity.type;

import com.dsh105.echopet.api.entity.*;
import com.dsh105.echopet.api.entity.nms.type.EntitySlimePet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.util.ParticleUtil;
import com.dsh105.echopet.util.protocol.wrapper.WrapperPacketWorldParticles;
import com.dsh105.echopet.nms.v1_7_R3.entity.EntityPetImpl;
import net.minecraft.server.v1_7_R3.World;

@EntitySize(width = 0.6F, height = 0.6F)
@EntityPetData(petType = PetType.SLIME)
public class EntitySlimePetImpl extends EntityPetImpl implements EntitySlimePet {

    int jumpDelay;

    public EntitySlimePetImpl(World world) {
        super(world);
    }

    public EntitySlimePetImpl(World world, Pet pet) {
        super(world, pet);
        int i = 1 << this.random.nextInt(3);
        this.setSize(i);
        this.jumpDelay = this.random.nextInt(15) + 10;
    }

    @Override
    public void setSize(int i) {
        this.datawatcher.watch(16, new Byte((byte) i));
        EntitySize es = this.getClass().getAnnotation(EntitySize.class);
        this.a(es.width() * (float) i, es.height() * (float) i);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.setHealth(this.getMaxHealth());
    }

    public int getSize() {
        return this.datawatcher.getByte(16);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Byte((byte) 1));
    }

    @Override
    protected String getIdleSound() {
        return "";
    }

    @Override
    protected String getDeathSound() {
        return "mob.slime." + (this.getSize() > 1 ? "big" : "small");
    }

    @Override
    public void onLive() {
        super.onLive();

        if (this.onGround && this.jumpDelay-- <= 0) {
            this.jumpDelay = this.random.nextInt(15) + 10;
            this.makeSound(this.getDeathSound(), this.be(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            getControllerJump().a();
        }

        if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
            if (this instanceof EntityMagmaCubePetImpl) {
                ParticleUtil.show(WrapperPacketWorldParticles.ParticleType.FIRE, this.getLocation());
            } else {
                ParticleUtil.show(WrapperPacketWorldParticles.ParticleType.SLIME_SPLAT, this.getLocation());
            }
        }
    }

    @Override
    public SizeCategory getSizeCategory() {
        if (this.getSize() == 1) {
            return SizeCategory.TINY;
        } else if (this.getSize() == 4) {
            return SizeCategory.LARGE;
        } else {
            return SizeCategory.REGULAR;
        }
    }
}