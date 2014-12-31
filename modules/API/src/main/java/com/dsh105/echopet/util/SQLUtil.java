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

package com.dsh105.echopet.util;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.Transformer;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.attribute.EntityAttribute;

import java.util.ArrayList;
import java.util.List;

public class SQLUtil {

    public static long serializeData(List<PetData> petData) {
        return serializeOrdinals(GeneralUtil.transform(petData, new Transformer<PetData, Integer>() {
            @Override
            public Integer transform(PetData transmutable) {
                return transmutable.ordinal();
            }
        }));
    }

    public static long serializeAttributes(List<EntityAttribute> petData) {
        return serializeOrdinals(GeneralUtil.transform(petData, new Transformer<EntityAttribute, Integer>() {
            @Override
            public Integer transform(EntityAttribute transmutable) {
                return transmutable.getType().getValue(transmutable).ordinal();
            }
        }));
    }

    public static long serializeOrdinals(List<Integer> ordinals) {
        long bitmask = 0;
        for (int ordinal : ordinals) {
            bitmask |= (1 << ordinal);
        }
        return bitmask;
    }

    public static List<PetData> deserializeData(long bitmask) {
        List<PetData> result = new ArrayList<>();
        for (PetData data : PetData.valid()) {
            if ((bitmask & (1 << data.ordinal())) != 0) {
                result.add(data);
            }
        }
        return result;
    }

    public static List<EntityAttribute> deserializeAttributes(long bitmask) {
        List<EntityAttribute> result = new ArrayList<>();
        for (EntityAttribute entityAttribute : Attributes.values()) {
            if ((bitmask & (1 << entityAttribute.getType().getValue(entityAttribute).ordinal())) != 0) {
                result.add(entityAttribute);
            }
        }
        return result;
    }
}