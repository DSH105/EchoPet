package com.dsh105.echopet.compat.nms.v1_8_R1.util;

import com.captainbern.minecraft.reflection.MinecraftFields;
import com.captainbern.minecraft.reflection.MinecraftReflection;
import com.captainbern.reflection.Reflection;
import com.captainbern.reflection.SafeField;
import com.captainbern.reflection.accessor.FieldAccessor;
import com.google.common.collect.MapMaker;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentMap;

/**
 * From https://github.com/CaptainBern/Reflection/blob/master/Minecraft-Reflection/src/main/java/com/captainbern/minecraft/reflection/MinecraftMethods.java
 * <p/>
 * "Channel" class import is modified due to Spigot's remapping of certain util classes.
 * <p/>
 * The class noted above is also included in the shaded dependencies of EchoPet so that plugin compatibility is maintained
 * with previous server versions (from 1.6.4 -> 1.7.10).
 */
public class MinecraftMethods {

    protected static volatile ConcurrentMap<Player, Channel> channelCache = new MapMaker().weakKeys().makeMap();
    private static FieldAccessor<Channel> CHANNEL_ACCESSOR;

    public static void sendPacket(Player player, Object handle) {
        Channel channel = channelCache.get(player);

        if (channel == null) {
            if (CHANNEL_ACCESSOR == null) {
                Class<?> networkManager = MinecraftReflection.getNetworkManagerClass();

                try {

                    SafeField<Channel> candidate = new Reflection().reflect(networkManager).getSafeFieldByType(Channel.class);

                    if (candidate == null)
                        throw new RuntimeException("Failed to find the Channel field!");

                    CHANNEL_ACCESSOR = candidate.getAccessor();

                } catch (Exception e) {
                    throw new RuntimeException("Failed to find the channel field!", e);
                }
            }

            channel = CHANNEL_ACCESSOR.get(MinecraftFields.getNetworkManager(player));
            channelCache.put(player, channel);
        }

        if (!MinecraftReflection.getPacketClass().isAssignableFrom(handle.getClass()))
            throw new IllegalArgumentException(handle + " is not a valid packet class!");

        channel.writeAndFlush(handle);
    }
}
