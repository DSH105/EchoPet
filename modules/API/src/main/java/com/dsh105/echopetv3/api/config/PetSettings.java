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

package com.dsh105.echopetv3.api.config;

import com.dsh105.commodus.ServerUtil;
import com.dsh105.commodus.Version;
import com.dsh105.commodus.config.Options;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.echopetv3.api.entity.AttributeAccessor;
import com.dsh105.echopetv3.api.entity.PetData;
import com.dsh105.echopetv3.api.entity.PetType;

public class PetSettings extends Options {

    public PetSettings(YAMLConfig config) {
        super(config);
        this.lockValue(ALLOW_RIDERS, false, PetType.ENDER_DRAGON.storageName());
        if (new Version("1.7.8").isIdentical()) {
            // Lock human pets - they crash 1.7.8 clients
            this.lockValue(ENABLE, false, PetType.HUMAN.storageName());
        }
    }

    @Override
    public void setDefaults() {
        for (PetType petType : PetType.values()) {
            for (Setting setting : Setting.getOptions(PetSettings.class, Setting.class)) {

                if (setting.equals(ALLOW_DATA) || setting.equals(FORCE_DATA)) {
                    for (PetData data : AttributeAccessor.getRegisteredData(petType)) {
                        set(setting.getPath(petType.storageName(), data.storageName()), setting.equals(ALLOW_RIDERS));
                    }
                    continue;
                }

                Object value = setting.getDefaultValue();
                if (setting.equals(DEFAULT_NAME)) {
                    value = petType.humanName() + " Pet";
                } else if (setting.equals(DAMAGE_DEALT)) {
                    //value = petType.getAttackDamage();
                } else if (setting.equals(HEALTH)) {
                    value = petType.getMaxHealth();
                } else if (setting.equals(CAN_FLY)) {
                    value = petType == PetType.BAT || petType == PetType.BLAZE || petType == PetType.GHAST || petType == PetType.SQUID || petType == PetType.WITHER;
                } else if (setting.equals(ALLOW_RIDERS)) {
                    value = petType != PetType.ENDER_DRAGON;
                }
                set(setting.getPath(petType.storageName()), value, setting.getComments());
            }
        }
    }

    public static final Setting<Boolean> ENABLE = new Setting<>(ConfigType.PETS, "pets.%s.enable", true);
    public static final Setting<Boolean> TAG_VISIBLE = new Setting<>(ConfigType.PETS, "pets.%s.tagVisible", true);
    public static final Setting<String> DEFAULT_NAME = new Setting<>(ConfigType.PETS, "pets.%s.defaultName");
    public static final Setting<Float> START_FOLLOW_DISTANCE = new Setting<>(ConfigType.PETS, "pets.%s.startFollowDistance", 12.0F);
    public static final Setting<Float> STOP_FOLLOW_DISTANCE = new Setting<>(ConfigType.PETS, "pets.%s.stopFollowDistance", 4.0F);
    public static final Setting<Float> TELEPORT_DISTANCE = new Setting<>(ConfigType.PETS, "pets.%s.teleportDistance", 30.0F);
    public static final Setting<Boolean> DAMAGE_PLAYERS = new Setting<>(ConfigType.PETS, "pets.%s.ai.damagePlayers", false);
    public static final Setting<Double> LOCK_RANGE = new Setting<>(ConfigType.PETS, "pets.%s.ai.lockRange", 10.0D);
    public static final Setting<Integer> TICKS_BETWEEN_ATTACKS = new Setting<>(ConfigType.PETS, "pets.%s.ai.ticksBetweenAttacks", 20);
    public static final Setting<Double> DAMAGE_DEALT = new Setting<>(ConfigType.PETS, "pets.%s.ai.damage");
    public static final Setting<Boolean> LOSE_HEALTH = new Setting<>(ConfigType.PETS, "pets.%s.ai.loseHealth", false);
    public static final Setting<Double> HEALTH = new Setting<>(ConfigType.PETS, "pets.%s.ai.health");
    public static final Setting<Double> RIDE_SPEED = new Setting<>(ConfigType.PETS, "pets.%s.rideSpeed", 0.25D);
    public static final Setting<Double> JUMP_HEIGHT = new Setting<>(ConfigType.PETS, "pets.%s.jumpHeight", 0.6D);
    public static final Setting<Boolean> CAN_FLY = new Setting<>(ConfigType.PETS, "pets.%s.canFly");
    public static final Setting<Boolean> ALLOW_RIDERS = new Setting<>(ConfigType.PETS, "pets.%s.allow.riders", true);
    public static final Setting<Boolean> ALLOW_DATA = new Setting<>(ConfigType.PETS, "pets.%s.allow.%s", true);
    public static final Setting<Boolean> FORCE_DATA = new Setting<>(ConfigType.PETS, "pets.%s.force.%s", true);
}