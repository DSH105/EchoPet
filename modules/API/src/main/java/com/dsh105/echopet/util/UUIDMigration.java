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

import com.dsh105.commodus.StringUtil;
import com.dsh105.commodus.UUIDFetcher;
import com.dsh105.commodus.configuration.Config;
import com.dsh105.echopet.api.plugin.EchoPet;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class UUIDMigration {

    public static void migrateConfig(final Config config) {
        // TODO: hmm
        ConfigurationSection cs = config.getConfigurationSection("autosave");
        if (cs != null) {
            final LinkedHashMap<String, LinkedHashMap<String, Object>> keyToValueMap = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
            for (String key : cs.getKeys(false)) {
                try {
                    StringUtil.convertUUID(key);
                    continue;
                } catch (IllegalArgumentException e) {
                    // Do nothing and keep migrating
                }
                ConfigurationSection petSection = config.getConfigurationSection("autosave." + key);
                for (String fullPath : petSection.getKeys(true)) {
                    LinkedHashMap<String, Object> existingMap = keyToValueMap.get(key);
                    if (existingMap == null) {
                        existingMap = new LinkedHashMap<>();
                    }
                    existingMap.put(fullPath, petSection.get(fullPath));
                    keyToValueMap.put(key, existingMap);
                    config.set("autosave." + key, null);
                }
            }


            new BukkitRunnable() {

                @Override
                public void run() {
                    try {
                        final Map<String, UUID> uuidMap = new UUIDFetcher(new ArrayList<String>(keyToValueMap.keySet())).call();

                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                for (Map.Entry<String, UUID> entry : uuidMap.entrySet()) {
                                    for (Map.Entry<String, Object> valueEntries : keyToValueMap.get(entry.getKey()).entrySet()) {
                                        config.set("autosave." + entry.getValue() + "." + valueEntries.getKey(), valueEntries.getValue());
                                    }
                                }
                                config.saveConfig();
                            }
                        }.runTask(EchoPet.getCore());

                    } catch (Exception ignored) {
                    }
                }

            }.runTaskAsynchronously(EchoPet.getCore());
        }
    }
}
