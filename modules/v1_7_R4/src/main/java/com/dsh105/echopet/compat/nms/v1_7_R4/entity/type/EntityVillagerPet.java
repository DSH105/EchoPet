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

package com.dsh105.echopet.compat.nms.v1_7_R4.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVillagerPet;
import com.dsh105.echopet.compat.api.util.ParticleUtil;
import com.dsh105.echopet.compat.api.util.protocol.wrapper.WrapperPacketWorldParticles;
import com.dsh105.echopet.compat.nms.v1_7_R4.entity.EntityAgeablePet;
import net.minecraft.server.v1_7_R4.World;

@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.VILLAGER)
public class EntityVillagerPet extends EntityAgeablePet implements IEntityVillagerPet {

    public EntityVillagerPet(World world) {
        super(world);
    }

    public EntityVillagerPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public void setProfession(int i) {
        this.datawatcher.watch(16, i);
    }

    @Override
    protected String getIdleSound() {
        return this.random.nextBoolean() ? "mob.villager.haggle" : "mob.villager.idle";
    }

    @Override
    protected String getDeathSound() {
        return "mob.villager.death";
    }

    @Override
    public void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Integer(0));
    }

    @Override
    public void onLive() {
        super.onLive();
        if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
            ParticleUtil.show(WrapperPacketWorldParticles.ParticleType.SPARKLE, this.getLocation());
        }
    }
}
