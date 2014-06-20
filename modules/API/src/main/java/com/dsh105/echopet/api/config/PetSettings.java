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

package com.dsh105.echopet.api.config;

import com.dsh105.commodus.config.Options;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.echopet.api.entity.AttributeAccessor;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.PetType;

public class PetSettings extends Options {

    public PetSettings(YAMLConfig config) {
        super(config);
        this.lockValue(ALLOW_RIDERS, false, PetType.ENDER_DRAGON);
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
                    value = petType.getAttackDamage();
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

    public static final Setting<Boolean> ENABLE = new Setting<Boolean>(ConfigType.PETS_CONFIG, "pets.%s.enable", true);
    public static final Setting<Boolean> TAG_VISIBLE = new Setting<Boolean>(ConfigType.PETS_CONFIG, "pets.%s.tagVisible", true);
    public static final Setting<String> DEFAULT_NAME = new Setting<String>(ConfigType.PETS_CONFIG, "pets.%s.defaultName", String.class);
    public static final Setting<Float> START_FOLLOW_DISTANCE = new Setting<Float>(ConfigType.PETS_CONFIG, "pets.%s.startFollowDistance", 12.0F);
    public static final Setting<Float> STOP_FOLLOW_DISTANCE = new Setting<Float>(ConfigType.PETS_CONFIG, "pets.%s.stopFollowDistance", 4.0F);
    public static final Setting<Float> TELEPORT_DISTANCE = new Setting<Float>(ConfigType.PETS_CONFIG, "pets.%s.teleportDistance", 30.0F);
    public static final Setting<Boolean> DAMAGE_PLAYERS = new Setting<Boolean>(ConfigType.PETS_CONFIG, "pets.%s.ai.damagePlayers", false);
    public static final Setting<Double> LOCK_RANGE = new Setting<Double>(ConfigType.PETS_CONFIG, "pets.%s.ai.lockRange", 10.0D);
    public static final Setting<Integer> TICKS_BETWEEN_ATTACKS = new Setting<Integer>(ConfigType.PETS_CONFIG, "pets.%s.ai.ticksBetweenAttacks", 20);
    public static final Setting<Double> DAMAGE_DEALT = new Setting<Double>(ConfigType.PETS_CONFIG, "pets.%s.ai.damage", Double.class);
    public static final Setting<Boolean> LOSE_HEALTH = new Setting<Boolean>(ConfigType.PETS_CONFIG, "pets.%s.ai.loseHealth", false);
    public static final Setting<Double> HEALTH = new Setting<Double>(ConfigType.PETS_CONFIG, "pets.%s.ai.health", Double.class);
    public static final Setting<Double> RIDE_SPEED = new Setting<Double>(ConfigType.PETS_CONFIG, "pets.%s.rideSpeed", 0.25D);
    public static final Setting<Double> JUMP_HEIGHT = new Setting<Double>(ConfigType.PETS_CONFIG, "pets.%s.jumpHeight", 0.6D);
    public static final Setting<Boolean> CAN_FLY = new Setting<Boolean>(ConfigType.PETS_CONFIG, "pets.%s.canFly", Boolean.class);
    public static final Setting<Boolean> ALLOW_RIDERS = new Setting<Boolean>(ConfigType.PETS_CONFIG, "pets.%s.allow.riders", true);
    public static final Setting<Boolean> ALLOW_DATA = new Setting<Boolean>(ConfigType.PETS_CONFIG, "pets.%s.allow.%s", true);
    public static final Setting<Boolean> FORCE_DATA = new Setting<Boolean>(ConfigType.PETS_CONFIG, "pets.%s.force.%s", true);
}