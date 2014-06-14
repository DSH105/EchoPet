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

import com.dsh105.echopet.api.entity.*;
import com.dsh105.echopet.api.entity.nms.type.EntityCreeperPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.util.ParticleUtil;
import com.dsh105.echopet.util.protocol.wrapper.WrapperPacketWorldParticles;
import com.dsh105.echopet.nms.v1_6_R3.entity.EntityPetImpl;
import net.minecraft.server.v1_6_R3.World;

@EntitySize(width = 0.6F, height = 1.9F)
@EntityPetData(petType = PetType.CREEPER)
public class EntityCreeperPetImpl extends EntityPetImpl implements EntityCreeperPet {

    public EntityCreeperPetImpl(World world) {
        super(world);
    }

    public EntityCreeperPetImpl(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    public void setPowered(boolean flag) {
        this.datawatcher.watch(17, Byte.valueOf((byte) (flag ? 1 : 0)));
    }

    @Override
    public void setIgnited(boolean flag) {
        this.datawatcher.watch(18, Byte.valueOf((byte) (flag ? 1 : 0)));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, Byte.valueOf((byte) -1));
        this.datawatcher.a(17, Byte.valueOf((byte) 0));
        this.datawatcher.a(18, Byte.valueOf((byte) 0));
    }

    @Override
    protected String getIdleSound() {
        return "";
    }

    @Override
    protected String getDeathSound() {
        return "mob.creeper.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }

    @Override
    public void onLive() {
        super.onLive();
        if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
            ParticleUtil.show(WrapperPacketWorldParticles.ParticleType.SMOKE, this.getLocation());
        }
    }
}
