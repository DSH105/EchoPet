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

package com.dsh105.echopet.api.configuration;

import com.dsh105.commodus.configuration.Config;
import com.dsh105.commodus.configuration.Option;
import com.dsh105.commodus.configuration.OptionSet;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.attribute.AttributeManager;
import com.dsh105.echopet.api.entity.attribute.EntityAttribute;
import com.dsh105.echopet.util.StringForm;

public class PetSettings extends OptionSet {

    public static final Option<Boolean> ENABLE = option("pets.%s.enable", true);
    public static final Option<Boolean> TAG_VISIBLE = option("pets.%s.tagVisible", true);
    public static final Option<String> DEFAULT_NAME = optionString("pets.%s.defaultName");
    public static final Option<Double> START_FOLLOW_DISTANCE = option("pets.%s.startFollowDistance", 12.0D);
    public static final Option<Double> STOP_FOLLOW_DISTANCE = option("pets.%s.stopFollowDistance", 4.0D);
    public static final Option<Double> TELEPORT_DISTANCE = option("pets.%s.teleportDistance", 30.0D);
    public static final Option<Boolean> DAMAGE_PLAYERS = option("pets.%s.ai.damagePlayers", false);
    public static final Option<Double> LOCK_RANGE = option("pets.%s.ai.lockRange", 10.0D);
    public static final Option<Integer> TICKS_BETWEEN_ATTACKS = option("pets.%s.ai.ticksBetweenAttacks", 20);
    public static final Option<Double> DAMAGE_DEALT = option(double.class, "pets.%s.ai.damage");
    public static final Option<Boolean> LOSE_HEALTH = option("pets.%s.ai.loseHealth", false);
    public static final Option<Double> HEALTH = option(double.class, "pets.%s.ai.health");
    public static final Option<Double> RIDE_SPEED = option("pets.%s.rideSpeed", 0.25D);
    public static final Option<Double> JUMP_HEIGHT = option("pets.%s.jumpHeight", 0.6D);
    public static final Option<Boolean> CAN_FLY = option(boolean.class, "pets.%s.canFly");
    public static final Option<Boolean> ALLOW_RIDERS = option("pets.%s.allow.riders", true);
    public static final Option<Boolean> ALLOW_DATA = option("pets.%s.allow.%s.%s", true);
    public static final Option<Boolean> FORCE_DATA = option("pets.%s.force.%s.%s", false);

    public PetSettings(Config config) {
        super(config);
        lock(ALLOW_RIDERS, false, PetType.ENDER_DRAGON.storageName());
    }

    @Override
    public void setDefaults() {
        for (PetType petType : PetType.values()) {
            try {
                for (Option option : getOptions()) {
                    if (option.equals(ALLOW_DATA) || option.equals(FORCE_DATA)) {
                        for (EntityAttribute entityAttribute : AttributeManager.getModifier(petType).getValidAttributes()) {
                            setDefault(option, petType.storageName(), StringForm.create(entityAttribute).getConfigName());
                        }
                        continue;
                    }

                    if (option.equals(DEFAULT_NAME)) {
                        set(option, petType.humanName() + " Pet");
                    } else if (option.equals(DAMAGE_DEALT)) {
                        // TODO
                    } else if (option.equals(HEALTH)) {
                        // TODO
                    } else if (option.equals(CAN_FLY)) {
                        set(option, petType == PetType.BAT || petType == PetType.BLAZE || petType == PetType.GHAST || petType == PetType.SQUID || petType == PetType.WITHER);
                    } else if (option.equals(ALLOW_RIDERS)) {
                        set(option, petType != PetType.ENDER_DRAGON);
                        ;
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to set configuration defaults", e);
            }
        }
    }
}