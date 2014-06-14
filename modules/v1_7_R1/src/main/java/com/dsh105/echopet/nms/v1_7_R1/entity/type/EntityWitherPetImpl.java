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

package com.dsh105.echopet.nms.v1_7_R1.entity.type;

import com.dsh105.echopet.api.entity.*;
import com.dsh105.echopet.api.entity.nms.type.EntityWitherPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.util.ParticleUtil;
import com.dsh105.echopet.util.protocol.wrapper.WrapperPacketWorldParticles;
import com.dsh105.echopet.nms.v1_7_R1.entity.EntityPetImpl;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 0.9F, height = 4.0F)
@EntityPetData(petType = PetType.WITHER)
public class EntityWitherPetImpl extends EntityPetImpl implements EntityWitherPet {

    public EntityWitherPetImpl(World world) {
        super(world);
    }

    public EntityWitherPetImpl(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(17, new Integer(0));
        this.datawatcher.a(18, new Integer(0));
        this.datawatcher.a(19, new Integer(0));
        this.datawatcher.a(20, new Integer(0));
    }

    public void setShielded(boolean flag) {
        this.datawatcher.watch(20, new Integer((flag ? 1 : 0)));
        this.setHealth((float) (flag ? 150 : 300));
    }

    @Override
    protected String getIdleSound() {
        return "mob.wither.idle";
    }

    @Override
    protected String getDeathSound() {
        return "mob.wither.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.LARGE;
    }

    @Override
    public void onLive() {
        super.onLive();
        if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
            ParticleUtil.show(WrapperPacketWorldParticles.ParticleType.VOID, this.getLocation());
        }
    }
}