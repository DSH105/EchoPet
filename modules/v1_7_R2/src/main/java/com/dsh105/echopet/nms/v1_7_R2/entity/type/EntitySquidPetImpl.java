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

package com.dsh105.echopet.nms.v1_7_R2.entity.type;

import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.SizeCategory;
import com.dsh105.echopet.api.entity.nms.type.EntitySquidPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.nms.v1_7_R2.entity.EntityPetImpl;
import com.dsh105.echopet.util.protocol.wrapper.WrapperPacketWorldParticles;
import net.minecraft.server.v1_7_R2.World;

@EntitySize(width = 0.95F, height = 0.95F)
@EntityPetData(petType = PetType.SQUID)
public class EntitySquidPetImpl extends EntityPetImpl implements EntitySquidPet {

    public EntitySquidPetImpl(World world) {
        super(world);
    }

    public EntitySquidPetImpl(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    protected String getIdleSound() {
        return "";
    }

    @Override
    protected String getDeathSound() {
        return null;
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }

    @Override
    public void onLive() {
        super.onLive();
        if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
            if (this.L()) {
                ParticleUtil.show(WrapperPacketWorldParticles.ParticleType.BUBBLE, this.getLocation());
            }
            ParticleUtil.show(WrapperPacketWorldParticles.ParticleType.SPLASH, this.getLocation());
        }
    }
}