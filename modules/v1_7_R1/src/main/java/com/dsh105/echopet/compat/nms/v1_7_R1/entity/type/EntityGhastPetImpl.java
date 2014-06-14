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

package com.dsh105.echopet.compat.nms.v1_7_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.nms.type.EntityGhastPet;
import com.dsh105.echopet.compat.api.entity.pet.Pet;
import com.dsh105.echopet.compat.nms.v1_7_R1.entity.EntityPetImpl;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 4.0F, height = 4.0F)
@EntityPetData(petType = PetType.GHAST)
public class EntityGhastPetImpl extends EntityPetImpl implements EntityGhastPet {

    public EntityGhastPetImpl(World world) {
        super(world);
    }

    public EntityGhastPetImpl(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    protected String getIdleSound() {
        return "mob.ghast.moan";
    }

    @Override
    protected String getDeathSound() {
        return "mob.ghast.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.OVERSIZE;
    }
}