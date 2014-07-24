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

import com.captainbern.minecraft.reflection.MinecraftReflection;
import com.captainbern.reflection.Reflection;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.api.config.PetSettings;
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.plugin.EchoPet;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum PetType {

    // Aggressive mobs
    BAT(65, 6D, 3D, 65, true),
    BLAZE(61, 20D, 6D, 6, false),
    CAVE_SPIDER(59, 12D, 5D, 59, false),
    CREEPER(50, 20D, 6D, 50, false),
    ENDER_DRAGON(63, 200D, 0D, Material.DRAGON_EGG, false),
    ENDERMAN(58, 40D, 6D, 58, false),
    GHAST(56, 10D, 7D, 56, false),
    GIANT(53, 100D, 0D, 54, false),
    MAGMA_CUBE(62, 20D, 5D, 62, false),
    PIG_ZOMBIE(57, 20D, 6D, 57, false),
    SILVERFISH(60, 8D, 4D, 60, false),
    SKELETON(51, 20D, 5D, 5, false),
    SLIME(55, 20D, 4D, 55, false),
    SPIDER(52, 16D, 5D, 52, false),
    CHICKEN(93, 4D, 3D, 93, true),
    COW(92, 10D, 4D, 92, true),
    WITCH(66, 26D, 5D, 66, false),
    WITHER(64, 300D, 8D, Material.NETHER_STAR, false),
    ZOMBIE(54, 20D, 5D, 54, false),

    // Passive mobs
    HORSE(100, 30D, 4D, 50, true),
    IRON_GOLEM(99, 100D, 7D, Material.PUMPKIN, true),
    MUSHROOM_COW(96, 10D, 3D, 96, true),
    OCELOT(98, 10D, 4D, 98, true),
    PIG(90, 10D, 3D, 90, true),
    SHEEP(91, 8D, 3D, 9, true),
    SNOWMAN(97, 4D, 4D, Material.SNOW_BALL, true),
    SQUID(94, 10D, 4D, 94, true),
    VILLAGER(120, 20D, 4D, 120, true),
    WOLF(95, 20D, 6D, 95, true),

    HUMAN(54, 20D, 6D, Material.SKULL_ITEM, 3, true);

    private int registrationId;
    private double maxHealth;
    private double attackDamage;
    private Class<? extends Pet> petClass;
    private Class<? extends EntityPet> entityClass;

    private String command;
    private Material material;
    private short materialData;

    PetType(int registrationId, double maxHealth, double attackDamage, Material material, boolean passive) {
        init(registrationId, maxHealth, attackDamage, material, 0, passive);
    }

    PetType(int registrationId, double maxHealth, double attackDamage, Material material, int materialData, boolean passive) {
        init(registrationId, maxHealth, attackDamage, material, materialData, passive);
    }

    PetType(int registrationId, double maxHealth, double attackDamage, int materialData, boolean passive) {
        init(registrationId, maxHealth, attackDamage, Material.MONSTER_EGG, materialData, passive);
    }

    private void init(int registrationId, double maxHealth, double attackDamage, Material material, int materialData, boolean passive) {
        this.registrationId = registrationId;
        this.maxHealth = maxHealth;
        this.attackDamage = attackDamage;
        this.material = material;
        this.materialData = (short) materialData;

        String classIdentifier = humanName().replace(" ", "");
        this.entityClass = new Reflection().reflect(EchoPet.INTERNAL_NMS_PATH + ".entity.type.Entity" + classIdentifier + "PetBase").getReflectedClass();
        this.petClass = new Reflection().reflect("com.dsh105.echopet.api.entity.pet.type." + classIdentifier + "PetBase").getReflectedClass();

        this.command = "pet " + storageName();
    }

    public int getRegistrationId() {
        return this.registrationId;
    }

    public double getMaxHealth() {
        return this.maxHealth;
    }

    public String getCommand() {
        return command;
    }

    public Material getMaterial() {
        return material;
    }

    public short getMaterialData() {
        return materialData;
    }

    public String getDefaultName(String name) {
        return PetSettings.DEFAULT_NAME.getValue(storageName()).replaceAll("(user|owner)", name).replaceAll("(userApos|ownerApos)", name + "\'s");
    }

    public <T extends LivingEntity, S extends EntityPet> S getNewEntityPetInstance(Object world, Pet<T, S> pet) {
        return (S) new Reflection().reflect(this.entityClass).getSafeConstructor(MinecraftReflection.getMinecraftClass("World"), pet.getClass()).getAccessor().invoke(world, pet);
    }

    public Pet getNewPetInstance(Player owner) {
        if (owner != null) {
            return new Reflection().reflect(this.petClass).getSafeConstructor(Player.class).getAccessor().invoke(owner);
        }
        return null;
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

    public static List<PetType> sortAlphabetically() {
        List<PetType> types = Arrays.asList(values());
        Collections.sort(types);
        return types;
    }
}