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

package com.dsh105.echopet.compat.nms.v1_8_R3.entity;

import com.dsh105.echopet.compat.api.entity.IPet;
import net.minecraft.server.v1_8_R3.World;


public abstract class EntityNoClipPet extends EntityPet {

    public EntityNoClipPet(World world, IPet pet) {
        super(world, pet);
    }

    protected EntityNoClipPet(World world) {
        super(world);
    }

    public void noClip(boolean flag) {
        this.noclip = flag;
    }
}
