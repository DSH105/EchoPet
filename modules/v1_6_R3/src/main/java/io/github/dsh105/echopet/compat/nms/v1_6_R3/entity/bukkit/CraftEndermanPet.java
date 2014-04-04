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

package io.github.dsh105.echopet.compat.nms.v1_6_R3.entity.bukkit;

import io.github.dsh105.echopet.compat.api.entity.EntityPetType;
import io.github.dsh105.echopet.compat.api.entity.PetType;
import io.github.dsh105.echopet.compat.api.util.ReflectionUtil;
import io.github.dsh105.echopet.compat.api.util.reflection.SafeMethod;
import io.github.dsh105.echopet.compat.nms.v1_6_R3.entity.CraftPet;
import io.github.dsh105.echopet.compat.nms.v1_6_R3.entity.EntityPet;
import io.github.dsh105.echopet.compat.nms.v1_6_R3.entity.type.EntityEndermanPet;
import org.bukkit.Material;
import org.bukkit.entity.Enderman;
import org.bukkit.material.MaterialData;

@EntityPetType(petType = PetType.ENDERMAN)
public class CraftEndermanPet extends CraftPet implements Enderman {

    public CraftEndermanPet(EntityPet entity) {
        super(entity);
    }

    @Override
    public MaterialData getCarriedMaterial() {
        EntityPet e = getHandle();
        if (e instanceof EntityEndermanPet) {
            EntityEndermanPet endermanPet = (EntityEndermanPet) e;
            Material mat = new SafeMethod<Material>(ReflectionUtil.getCBCClass("util.CraftMagicNumbers"), "getMaterial", ReflectionUtil.getNMSClass("Block")).invoke(null, endermanPet.getCarried());
            return new SafeMethod<MaterialData>(Material.class, "getNewData", Byte.class).invoke(mat, (byte) endermanPet.getCarriedData());
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