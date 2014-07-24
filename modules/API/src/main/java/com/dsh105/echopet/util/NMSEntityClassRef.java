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

/*
 * Copyright (C) EntityAPI Team
 *
 * This file is part of EntityAPI.
 *
 * EntityAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EntityAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EntityAPI.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.util;

import com.captainbern.minecraft.reflection.MinecraftReflection;
import org.bukkit.entity.*;

import java.util.HashMap;
import java.util.Map;

public class NMSEntityClassRef {

    private static HashMap<Class<?>, Class<?>> CLASS_MAP = new HashMap<>();

    static {
        CLASS_MAP.put(Entity.class, MinecraftReflection.getMinecraftClass("Entity"));
        CLASS_MAP.put(Ageable.class, MinecraftReflection.getMinecraftClass("EntityAgeable"));
        CLASS_MAP.put(Animals.class, MinecraftReflection.getMinecraftClass("EntityAnimal"));
        CLASS_MAP.put(Creature.class, MinecraftReflection.getMinecraftClass("EntityCreature"));
        CLASS_MAP.put(Flying.class, MinecraftReflection.getMinecraftClass("EntityFlying"));
        CLASS_MAP.put(LivingEntity.class, MinecraftReflection.getMinecraftClass("EntityLiving"));
        CLASS_MAP.put(Monster.class, MinecraftReflection.getMinecraftClass("EntityMonster"));

        CLASS_MAP.put(Bat.class, MinecraftReflection.getMinecraftClass("EntityBat"));
        CLASS_MAP.put(Blaze.class, MinecraftReflection.getMinecraftClass("EntityBlaze"));
        CLASS_MAP.put(CaveSpider.class, MinecraftReflection.getMinecraftClass("EntityCaveSpider"));
        CLASS_MAP.put(Chicken.class, MinecraftReflection.getMinecraftClass("EntityChicken"));
        CLASS_MAP.put(Cow.class, MinecraftReflection.getMinecraftClass("EntityCow"));
        CLASS_MAP.put(Creeper.class, MinecraftReflection.getMinecraftClass("EntityCreeper"));
        CLASS_MAP.put(EnderDragon.class, MinecraftReflection.getMinecraftClass("EntityEnderDragon"));
        CLASS_MAP.put(Enderman.class, MinecraftReflection.getMinecraftClass("EntityEnderman"));
        CLASS_MAP.put(Ghast.class, MinecraftReflection.getMinecraftClass("EntityGhast"));
        CLASS_MAP.put(Giant.class, MinecraftReflection.getMinecraftClass("EntityGiant"));
        CLASS_MAP.put(Golem.class, MinecraftReflection.getMinecraftClass("EntityGolem"));
        CLASS_MAP.put(Horse.class, MinecraftReflection.getMinecraftClass("EntityHorse"));
        CLASS_MAP.put(HumanEntity.class, MinecraftReflection.getMinecraftClass("EntityHuman"));
        CLASS_MAP.put(IronGolem.class, MinecraftReflection.getMinecraftClass("EntityIronGolem"));
        CLASS_MAP.put(MagmaCube.class, MinecraftReflection.getMinecraftClass("EntityMagmaCube"));
        CLASS_MAP.put(MushroomCow.class, MinecraftReflection.getMinecraftClass("EntityMushroomCow"));
        CLASS_MAP.put(Ocelot.class, MinecraftReflection.getMinecraftClass("EntityOcelot"));
        CLASS_MAP.put(Pig.class, MinecraftReflection.getMinecraftClass("EntityPig"));
        CLASS_MAP.put(PigZombie.class, MinecraftReflection.getMinecraftClass("EntityPigZombie"));
        CLASS_MAP.put(Player.class, MinecraftReflection.getMinecraftClass("EntityPlayer"));
        CLASS_MAP.put(Sheep.class, MinecraftReflection.getMinecraftClass("EntitySheep"));
        CLASS_MAP.put(Silverfish.class, MinecraftReflection.getMinecraftClass("EntitySilverfish"));
        CLASS_MAP.put(Skeleton.class, MinecraftReflection.getMinecraftClass("EntitySkeleton"));
        CLASS_MAP.put(Slime.class, MinecraftReflection.getMinecraftClass("EntitySlime"));
        CLASS_MAP.put(Snowman.class, MinecraftReflection.getMinecraftClass("EntitySnowman"));
        CLASS_MAP.put(Spider.class, MinecraftReflection.getMinecraftClass("EntitySpider"));
        CLASS_MAP.put(Squid.class, MinecraftReflection.getMinecraftClass("EntitySquid"));
        CLASS_MAP.put(Villager.class, MinecraftReflection.getMinecraftClass("EntityVillager"));
        CLASS_MAP.put(WaterMob.class, MinecraftReflection.getMinecraftClass("EntityWaterAnimal"));
        CLASS_MAP.put(Witch.class, MinecraftReflection.getMinecraftClass("EntityWitch"));
        CLASS_MAP.put(Wither.class, MinecraftReflection.getMinecraftClass("EntityWither"));
        CLASS_MAP.put(Wolf.class, MinecraftReflection.getMinecraftClass("EntityWolf"));
        CLASS_MAP.put(Zombie.class, MinecraftReflection.getMinecraftClass("EntityZombie"));
    }

    public static Class<?> getNMSClass(Class<? extends Entity> bukkitClass) {
        Class result = null;
        Class superClass = bukkitClass;
        while (result == null && superClass != Object.class) {
            result = CLASS_MAP.get(bukkitClass);
            superClass = superClass.getSuperclass();
        }
        return result;
    }

    public static Class<? extends Entity> getBukkitClass(Class<?> nmsClass) {
        Class result = null;
        Class superClass = nmsClass;
        while (result == null && superClass != Object.class) {
            for (Map.Entry<Class<?>, Class<?>> entry : CLASS_MAP.entrySet()) {
                if (entry.getValue() == nmsClass) {
                    result = entry.getKey();
                }
            }
            superClass = superClass.getSuperclass();
        }
        return result;
    }
}