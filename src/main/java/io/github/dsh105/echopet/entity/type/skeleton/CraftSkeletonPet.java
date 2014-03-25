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

package io.github.dsh105.echopet.entity.type.skeleton;

import io.github.dsh105.echopet.entity.*;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.entity.Skeleton;

@EntityPetType(petType = PetType.SKELETON)
public class CraftSkeletonPet extends CraftPet implements Skeleton {

    public CraftSkeletonPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }

    @Override
    public SkeletonType getSkeletonType() {
        Pet p = this.getPet();
        if (p instanceof SkeletonPet) {
            return ((SkeletonPet) p).isWither() ? SkeletonType.WITHER : SkeletonType.NORMAL;
        }
        return null;
    }

    @Override
    public void setSkeletonType(SkeletonType skeletonType) {
        /*Pet p = this.getPet();
        if (p instanceof SkeletonPet) {
            ((SkeletonPet) p).setWither(skeletonType == SkeletonType.WITHER);
        }*/
    }
}