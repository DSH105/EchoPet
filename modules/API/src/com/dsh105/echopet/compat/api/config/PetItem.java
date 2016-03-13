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

package com.dsh105.echopet.compat.api.config;

import org.bukkit.Material;

import com.dsh105.echopet.compat.api.entity.PetType;

public enum PetItem {

	BAT(PetType.BAT, Material.MONSTER_EGG, "Bat Pet", "bat"),
	BLAZE(PetType.BLAZE, Material.MONSTER_EGG, "Blaze Pet", "blaze"),
	CAVESPIDER(PetType.CAVESPIDER, Material.MONSTER_EGG, "Cave Spider Pet", "cavespider"),
	CHICKEN(PetType.CHICKEN, Material.MONSTER_EGG, "Chicken Pet", "chicken"),
	COW(PetType.COW, Material.MONSTER_EGG, "Cow Pet", "cow"),
	CREEPER(PetType.CREEPER, Material.MONSTER_EGG, "Creeper Pet", "creeper"),
	ENDERDRAGON(PetType.ENDERDRAGON, Material.DRAGON_EGG, "EnderDragon Pet", "enderdragon"),
	ENDERMAN(PetType.ENDERMAN, Material.MONSTER_EGG, "Enderman Pet", "enderman"),
	ENDERMITE(PetType.ENDERMITE, Material.MONSTER_EGG, "Endermite Pet", "endermite"),
	GHAST(PetType.GHAST, Material.MONSTER_EGG, "Ghast Pet", "ghast"),
	GIANT(PetType.GIANT, Material.MONSTER_EGG, "Giant Pet", "giant"),
	GUARDIAN(PetType.GUARDIAN, Material.MONSTER_EGG, "Guardian Pet", "guardian"),
	HORSE(PetType.HORSE, Material.MONSTER_EGG, "Horse Pet", "horse"),
	HUMAN(PetType.HUMAN, Material.SKULL_ITEM, 3, "Human Pet", "human"),
	IRONGOLEM(PetType.IRONGOLEM, Material.PUMPKIN, "Iron Golem Pet", "irongolem"),
	MAGMACUBE(PetType.MAGMACUBE, Material.MONSTER_EGG, "Magma Cube Pet", "magmacube"),
	MUSHROOMCOW(PetType.MUSHROOMCOW, Material.MONSTER_EGG, "Mushroom Cow Pet", "mushroomcow"),
	OCELOT(PetType.OCELOT, Material.MONSTER_EGG, "Ocelot Pet", "ocelot"),
	PIG(PetType.PIG, Material.MONSTER_EGG, "Pig Pet", "pig"),
	PIGZOMBIE(PetType.PIGZOMBIE, Material.MONSTER_EGG, "PigZombie Pet", "pigzombie"),
	RABBIT(PetType.RABBIT, Material.MONSTER_EGG, "Rabbit Pet", "rabbit"),
	SHEEP(PetType.SHEEP, Material.MONSTER_EGG, "Sheep Pet", "sheep"),
	SHULKER(PetType.SHULKER, Material.MONSTER_EGG, "Shulker Pet", "shulker"),
	SILVERFISH(PetType.SILVERFISH, Material.MONSTER_EGG, "Silverfish Pet", "silverfish"),
	SKELETON(PetType.SKELETON, Material.MONSTER_EGG, "Skeleton Pet", "skeleton"),
	SLIME(PetType.SLIME, Material.MONSTER_EGG, "Slime Pet", "slime"),
	SNOWMAN(PetType.SNOWMAN, Material.SNOW_BALL, "Snowman Pet", "snowman"),
	SPIDER(PetType.SPIDER, Material.MONSTER_EGG, "Spider Pet", "spider"),
	SQUID(PetType.SQUID, Material.MONSTER_EGG, "Squid Pet", "squid"),
	VILLAGER(PetType.VILLAGER, Material.MONSTER_EGG, "Villager Pet", "villager"),
	WITCH(PetType.WITCH, Material.MONSTER_EGG, "Witch Pet", "witch"),
	WITHER(PetType.WITHER, Material.NETHER_STAR, "Wither Pet", "wither"),
	WOLF(PetType.WOLF, Material.MONSTER_EGG, "Wolf Pet", "wolf"),
	ZOMBIE(PetType.ZOMBIE, Material.MONSTER_EGG, "Zombie Pet", "zombie");

    private String cmd;
    public PetType petType;
    private Material mat;
	private short materialData;
    private String name;

	PetItem(PetType petType, Material mat, String name, String cmd){
		this(petType, mat, 0, name, cmd);
	}

	PetItem(PetType petType, Material mat, int materialData, String name, String cmd){
        this.cmd = "pet " + cmd;
        this.petType = petType;
        this.mat = mat;
		this.materialData = (short) materialData;
        this.name = name;
    }

    public String getCommand() {
        return cmd;
    }

    public PetType getPetType() {
        return petType;
    }

    public Material getMat() {
        return mat;
    }

	public short getMaterialData(){
		return materialData;
	}

    public String getName() {
        return name;
    }
}
