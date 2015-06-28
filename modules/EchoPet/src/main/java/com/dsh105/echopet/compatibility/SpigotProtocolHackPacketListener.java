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

package com.dsh105.echopet.compatibility;

import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.dsh105.echopet.EchoPetPlugin;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import com.google.common.base.Throwables;
import org.bukkit.DyeColor;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;

import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;

public class SpigotProtocolHackPacketListener extends PacketAdapter {

    private final EchoPetPlugin echoPetPlugin;
    private final String entityIdMetadataKey = "EchoPet-ID";
    private final EnumMap<DyeColor, Integer> convertedDyeColors = new EnumMap<DyeColor, Integer>(DyeColor.class) {
        {
            put(DyeColor.WHITE, 15);
            put(DyeColor.ORANGE, 14);
            put(DyeColor.MAGENTA, 13);
            put(DyeColor.LIGHT_BLUE, 12);
            put(DyeColor.YELLOW, 11);
            put(DyeColor.LIME, 10);
            put(DyeColor.PINK, 9);
            put(DyeColor.GRAY, 8);
            put(DyeColor.SILVER, 7);
            put(DyeColor.CYAN, 6);
            put(DyeColor.PURPLE, 5);
            put(DyeColor.BLUE, 4);
            put(DyeColor.BROWN, 3);
            put(DyeColor.GREEN, 2);
            put(DyeColor.RED, 1);
            put(DyeColor.BLACK, 0);
        }
    };

    public SpigotProtocolHackPacketListener(EchoPetPlugin echoPetPlugin) {
        super(echoPetPlugin,
                ListenerPriority.HIGHEST,
                PacketType.Play.Server.SPAWN_ENTITY_LIVING,
                PacketType.Play.Server.ENTITY_METADATA);
        this.echoPetPlugin = echoPetPlugin;
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    public void shutdown() {
        ProtocolLibrary.getProtocolManager().removePacketListener(this);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        if (!isPlayerRunningv1_8(player)) {
            return;
        }

        // These packets share the same instance when they are broadcasted to a group of players.
        // The solution is to clone each packet.
        PacketContainer newPacketContainer = event.getPacket().deepClone();
        event.setPacket(newPacketContainer);

        if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
            WrapperPlayServerSpawnEntityLiving wrapperPlayServerSpawnEntityLiving = new WrapperPlayServerSpawnEntityLiving(newPacketContainer);

            EntityType entityType = wrapperPlayServerSpawnEntityLiving.getType();
            if (entityType != null) {
                // We need to store the entity metadata onSpawn, since getType() does not resolve properly.
                // Always set the meta, even if the player is not on 1.8. Fixes more players joining and crashing.
                Entity entity = wrapperPlayServerSpawnEntityLiving.getEntity(event);
                if (entity != null && !entity.hasMetadata(this.entityIdMetadataKey)) {
                    entity.setMetadata(this.entityIdMetadataKey, new FixedMetadataValue(this.echoPetPlugin, entityType.name()));
                }

                wrapperPlayServerSpawnEntityLiving.setMetadata(
                        new WrappedDataWatcher(
                                fixMetadata(entityType, wrapperPlayServerSpawnEntityLiving.getMetadata().getWatchableObjects())));
            }
        } else if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
            WrapperPlayServerEntityMetadata wrapperPlayServerEntityMetadata = new WrapperPlayServerEntityMetadata(newPacketContainer);

            // getType() does not resolve properly, returning UNKNOWN. So use the cached metadata tag added on spawn.
            Entity entity = wrapperPlayServerEntityMetadata.getEntity(event);
            if (entity != null && entity.hasMetadata(this.entityIdMetadataKey)) {
                EntityType entityType = EntityType.valueOf(entity.getMetadata(this.entityIdMetadataKey).get(0).asString());
                wrapperPlayServerEntityMetadata.setEntityMetadata(
                        fixMetadata(entityType, wrapperPlayServerEntityMetadata.getEntityMetadata()));
            }
        }
    }

    private List<WrappedWatchableObject> fixMetadata(EntityType entityType, List<WrappedWatchableObject> wrappedWatchableObjectList) {
        if (entityType == null || entityType.getEntityClass() == null || wrappedWatchableObjectList == null) {
            return wrappedWatchableObjectList;
        }

        if (Ageable.class.isAssignableFrom(entityType.getEntityClass()) && hasKey(12, wrappedWatchableObjectList)) {
            Object object = getKeyValue(12, wrappedWatchableObjectList);
            if (object instanceof Integer) {
                removeKey(12, wrappedWatchableObjectList);
                wrappedWatchableObjectList.add(new WrappedWatchableObject(12, Byte.valueOf((byte) (int) (Integer) object)));
            }
        }
        if (Wolf.class.isAssignableFrom(entityType.getEntityClass()) && hasKey(20, wrappedWatchableObjectList)) {
            Object object = getKeyValue(20, wrappedWatchableObjectList);

            if (object instanceof Byte) {
                DyeColor color = DyeColor.getByWoolData((byte) ((Byte) object & 0xF));
                removeKey(20, wrappedWatchableObjectList);
                wrappedWatchableObjectList.add(new WrappedWatchableObject(20, Byte.valueOf((byte) ((this.convertedDyeColors.get(color)) & 0xF))));
            }
        }
        if (Enderman.class.isAssignableFrom(entityType.getEntityClass()) && hasKey(16, wrappedWatchableObjectList)) {
            Object object = getKeyValue(16, wrappedWatchableObjectList);
            if (object instanceof Byte) {
                removeKey(16, wrappedWatchableObjectList);
                wrappedWatchableObjectList.add(new WrappedWatchableObject(16, Short.valueOf((Byte) object)));
            }
        }

        return wrappedWatchableObjectList;
    }

    private boolean hasKey(int key, List<WrappedWatchableObject> wrappedWatchableObjectList) {
        for (Iterator<WrappedWatchableObject> wrappedWatchableObjectIterator = wrappedWatchableObjectList.iterator(); wrappedWatchableObjectIterator.hasNext(); ) {
            WrappedWatchableObject next = wrappedWatchableObjectIterator.next();
            if (next.getIndex() == key) {
                return true;
            }
        }
        return false;
    }

    private Object getKeyValue(int key, List<WrappedWatchableObject> wrappedWatchableObjectList) {
        for (Iterator<WrappedWatchableObject> wrappedWatchableObjectIterator = wrappedWatchableObjectList.iterator(); wrappedWatchableObjectIterator.hasNext(); ) {
            WrappedWatchableObject next = wrappedWatchableObjectIterator.next();
            if (next.getIndex() == key) {
                return next.getValue();
            }
        }
        return null;
    }

    private void removeKey(int key, List<WrappedWatchableObject> wrappedWatchableObjectList) {
        for (Iterator<WrappedWatchableObject> wrappedWatchableObjectIterator = wrappedWatchableObjectList.iterator(); wrappedWatchableObjectIterator.hasNext(); ) {
            WrappedWatchableObject next = wrappedWatchableObjectIterator.next();
            if (next.getIndex() == key) {
                wrappedWatchableObjectIterator.remove();
                break;
            }
        }
    }

    private boolean isPlayerRunningv1_8(Player player) {
        try {
            Object nmsPlayer = ReflectionUtil.getEntityHandle(player);
            Object playerConnection = ReflectionUtil.getField(nmsPlayer.getClass(), "playerConnection", nmsPlayer);
            Object networkManager = ReflectionUtil.getField(playerConnection.getClass(), "networkManager", playerConnection);

            Method getVersionMethod = ReflectionUtil.getMethod(networkManager.getClass(), "getVersion");
            getVersionMethod.setAccessible(true);
            return (Integer) ReflectionUtil.invokeMethod(getVersionMethod, networkManager) > 5;
        } catch (Exception exception) {
            throw Throwables.propagate(exception);
        }
    }
}
