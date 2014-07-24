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

import com.dsh105.echopet.api.entity.entitypet.type.EntitySnowmanPet;
import com.dsh105.echopet.api.entity.pet.type.SnowmanPet;
import com.dsh105.echopet.nms.v1_7_R2.entity.EntityPetBase;
import net.minecraft.server.v1_7_R2.World;

public class EntitySnowmanPetBase extends EntityPetBase<SnowmanPet> implements EntitySnowmanPet {

    public EntitySnowmanPetBase(World world, SnowmanPet pet) {
        super(world, pet);
    }
}