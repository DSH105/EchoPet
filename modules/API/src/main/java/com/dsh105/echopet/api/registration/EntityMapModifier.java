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

package com.dsh105.echopet.api.registration;

import java.util.HashMap;
import java.util.Map;

public class EntityMapModifier<K, V> {

    private final Map<K, V> map;
    private Map<V, Map<K, V>> results = new HashMap<>();

    public EntityMapModifier(Map<K, V> map) {
        this.map = map;
    }

    public Map<K, V> getMap() {
        return map;
    }

    public boolean clear(V value) {
        if (!results.containsKey(value)) {
            return false;
        }

        for (K key : results.get(value).keySet()) {
            map.remove(key);
        }
        return true;
    }

    public boolean add(V value) {
        if (!results.containsKey(value)) {
            return false;
        }

        for (Map.Entry<K, V> entry : results.get(value).entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return true;
    }

    public Map<K, V> requestMappings(V value) {
        if (results.containsKey(value)) {
            return results.get(value);
        }

        Map<K, V> result = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key1 = entry.getKey();
            V value1 = entry.getValue();
            if (value.equals(value1)) {
                result.put(key1, value1);
            }
        }
        results.put(value, result);
        return result;
    }
}