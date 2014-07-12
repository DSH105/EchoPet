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

package com.dsh105.echopet.nms.v1_6_R3.entity.type;

import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.SizeCategory;
import com.dsh105.echopet.api.entity.nms.type.EntityEndermanPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.nms.v1_6_R3.entity.EntityPetImpl;
import com.dsh105.echopet.util.protocol.wrapper.WrapperPacketWorldParticles;
import net.minecraft.server.v1_6_R3.Block;
import net.minecraft.server.v1_6_R3.World;

@EntitySize(width = 0.6F, height = 2.9F)
@EntityPetData(petType = PetType.ENDERMAN)
public class EntityEndermanPetImpl extends EntityPetImpl implements EntityEndermanPet {

    public EntityEndermanPetImpl(World world) {
        super(world);
    }

    public EntityEndermanPetImpl(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    public void setScreaming(boolean flag) {
        this.datawatcher.watch(18, Byte.valueOf((byte) (flag ? 1 : 0)));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Byte((byte) 0));
        this.datawatcher.a(17, new Byte((byte) 0));
        this.datawatcher.a(18, new Byte((byte) 0));
    }

    @Override
    protected String getIdleSound() {
        return this.isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
    }

    public boolean isScreaming() {
        return this.datawatcher.getByte(18) > 0;
    }

    public void setCarried(int block) {
        this.datawatcher.watch(16, Byte.valueOf((byte) (block & 255)));
    }

    public Block getCarried() {
        return Block.byId[this.datawatcher.getByte(16)];
    }

    public void setCarriedData(int i) {
        this.datawatcher.watch(17, Byte.valueOf((byte) (i & 255)));
    }

    public int getCarriedData() {
        return this.datawatcher.getByte(17);
    }

    @Override
    protected String getDeathSound() {
        return "mob.enderman.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }

    @Override
    public void onLive() {
        super.onLive();
        if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
            ParticleUtil.show(WrapperPacketWorldParticles.ParticleType.PORTAL, this.getLocation());
        }
    }
}
