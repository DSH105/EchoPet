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
import com.dsh105.echopet.api.entity.nms.type.EntityCaveSpiderPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.util.ParticleUtil;
import com.dsh105.echopet.util.protocol.wrapper.WrapperPacketWorldParticles;
import com.dsh105.echopet.nms.v1_7_R3.entity.EntityPetImpl;
import net.minecraft.server.v1_7_R3.World;

@EntitySize(width = 0.7F, height = 0.5F)
@EntityPetData(petType = PetType.CAVESPIDER)
public class EntityCaveSpiderPetImpl extends EntityPetImpl implements EntityCaveSpiderPet {

    EntityCaveSpiderPetImpl(World world) {
        super(world);
    }

    public EntityCaveSpiderPetImpl(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Byte((byte) 0));
    }

    @Override
    protected void makeStepSound() {
        makeSound("mob.spider.step", 0.15F, 1.0F);
    }

    @Override
    protected String getIdleSound() {
        return "mob.spider.say";
    }

    @Override
    protected String getDeathSound() {
        return "mob.spider.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }

    @Override
    public void onLive() {
        super.onLive();
        if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
            ParticleUtil.show(WrapperPacketWorldParticles.ParticleType.SPELL_AMBIENT, this.getLocation());
        }
    }
}
