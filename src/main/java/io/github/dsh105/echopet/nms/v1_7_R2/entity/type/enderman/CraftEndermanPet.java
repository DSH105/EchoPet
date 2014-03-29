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

package io.github.dsh105.echopet.nms.v1_7_R2.entity.type.enderman;

import io.github.dsh105.echopet.nms.v1_7_R2.entity.CraftPet;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.EntityPet;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.EntityPetType;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.craftbukkit.v1_7_R2.util.CraftMagicNumbers;
import org.bukkit.entity.Enderman;
import org.bukkit.material.MaterialData;

@EntityPetType(petType = PetType.ENDERMAN)
public class CraftEndermanPet extends CraftPet implements Enderman {

    public CraftEndermanPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }

    @Override
    public MaterialData getCarriedMaterial() {
        EntityPet e = getHandle();
        if (e instanceof EntityEndermanPet) {
            EntityEndermanPet endermanPet = (EntityEndermanPet) e;
            return CraftMagicNumbers.getMaterial(endermanPet.getCarried()).getNewData((byte) endermanPet.getCarriedData());
        }
        return null;
    }

    @Override
    public void setCarriedMaterial(MaterialData data) {
        /*EntityPet e = getHandle();
        if (e instanceof EntityEndermanPet) {
            EntityEndermanPet endermanPet = (EntityEndermanPet) e;
            endermanPet.setCarried(CraftMagicNumbers.getBlock(data.getItemTypeId()));
            endermanPet.setCarriedData(data.getData());
        }*/
    }
}