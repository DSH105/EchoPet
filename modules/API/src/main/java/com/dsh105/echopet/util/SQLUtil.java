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

import com.dsh105.echopet.api.entity.PetData;

import java.util.ArrayList;
import java.util.List;

public class SQLUtil {

    public static long serializePetData(List<PetData> data) {
        long bitmask = 0;
        for (PetData petData : data) {
            bitmask |= (1 << petData.ordinal());
        }
        return bitmask;
    }

    public static List<PetData> deserializePetData(long bitmask) {
        List<PetData> result = new ArrayList<PetData>();
        for (PetData petData : PetData.valid()) {
            if ((bitmask & (1 << petData.ordinal())) != 0) {
                result.add(petData);
            }
        }
        return result;
    }
}