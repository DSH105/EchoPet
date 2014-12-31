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

package com.dsh105.echopet.api.entity;

import com.dsh105.commodus.StringUtil;
import com.dsh105.commodus.container.ItemStackContainer;
import com.dsh105.commodus.reflection.Reflection;
import com.dsh105.echopet.api.configuration.PetSettings;
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.util.Perm;
import com.dsh105.interact.Interact;
import com.dsh105.interact.api.CommandIcon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum PetType {

    BAT(65),
    BLAZE(61),
    CAVE_SPIDER(59),
    CHICKEN(93),
    COW(92),
    CREEPER(50),
    ENDER_DRAGON(63, "dragon_egg"),
    ENDERMAN(58),
    GHAST(56),
    GIANT(53),
    HORSE(100),
    HUMAN(54, "skull"),
    IRON_GOLEM(99, "pumpkin"),
    MAGMA_CUBE(62),
    MOOSHROOM(96),
    OCELOT(98),
    PIG(90),
    ZOMBIE_PIGMAN(57),
    SHEEP(91),
    SILVERFISH(60),
    SKELETON(51),
    SLIME(55),
    SNOWMAN(97, "snowball"),
    SPIDER(52),
    SQUID(94),
    VILLAGER(120),
    WITCH(66),
    WITHER(64, "nether_start"),
    WOLF(95),
    ZOMBIE(54);


    private int registrationId;
    private Class<? extends Pet> petClass;
    private Class<? extends EntityPet> entityClass;

    private String command;
    private String materialId;
    private short materialMeta;

    PetType(int registrationId) {
        this(registrationId, "spawn_egg", 0);
    }

    PetType(int registrationId, String materialId) {
        this(registrationId, materialId, 0);
    }

    PetType(int registrationId, String materialId, int materialMeta) {
        this.registrationId = registrationId;
        this.materialId = materialId;
        this.materialMeta = (short) materialMeta;
        this.command = "pet " + storageName();

        String classIdentifier = humanName().replace(" ", "");
        this.entityClass = (Class<? extends EntityPet>) Reflection.getClass(EchoPet.INTERNAL_NMS_PATH + ".entity.type.EchoEntity" + classIdentifier + "Pet");
        this.petClass = (Class<? extends Pet>) Reflection.getClass("com.dsh105.echopet.api.entity.pet.type.Echo" + classIdentifier + "Pet");
    }

    public static List<PetType> sortAlphabetically() {
        List<PetType> types = Arrays.asList(values());
        Collections.sort(types);
        return types;
    }

    public int getRegistrationId() {
        return this.registrationId;
    }

    public String getCommand() {
        return command;
    }

    public String getMaterial() {
        return materialId;
    }

    public short getMaterialMeta() {
        return materialMeta;
    }

    public CommandIcon getIcon() {
        return Interact.commandIcon().command(command).permission(Perm.TYPE.replace("<type>", storageName())).name(humanName()).of(ItemStackContainer.of(materialId, materialMeta, 1)).build();
    }

    public String getDefaultName(String name) {
        return PetSettings.DEFAULT_NAME.getValue(storageName()).replaceAll("(user|owner)", name).replaceAll("(userApos|ownerApos)", name + "\'s");
    }

    public Class<? extends EntityPet> getEntityClass() {
        return this.entityClass;
    }

    public Class<? extends Pet> getPetClass() {
        return this.petClass;
    }

    public String storageName() {
        return toString().toLowerCase().replace("_", "");
    }

    public String humanName() {
        return StringUtil.capitalise(toString().toLowerCase().replace("_", " "));
    }
}