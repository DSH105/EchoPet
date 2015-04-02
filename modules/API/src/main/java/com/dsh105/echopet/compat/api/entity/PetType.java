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

package com.dsh105.echopet.compat.api.entity;

import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

public enum PetType {

    // Aggressive mobs
    BLAZE("Blaze", 61, "Blaze Pet", 20D, 6D, EntityType.BLAZE, PetData.FIRE),
    CAVESPIDER("CaveSpider", 59, "Cave Spider Pet", 12D, 5D, EntityType.CAVE_SPIDER),
    CREEPER("Creeper", 50, "Creeper Pet", 20D, 6D, EntityType.CREEPER, PetData.POWER),
    ENDERDRAGON("EnderDragon", 63, "EnderDragon Pet", 200D, 0D, EntityType.ENDER_DRAGON),
    ENDERMAN("Enderman", 58, "Enderman Pet", 40D, 6D, EntityType.ENDERMAN, PetData.SCREAMING),
    ENDERMITE("Endermite", 67, "Endermite Pet", 2D, 2D, EntityType.ENDERMITE),
    GHAST("Ghast", 56, "Ghast Pet", 10D, 7D, EntityType.GHAST),
    GIANT("Giant", 53, "Giant Pet", 100D, 0D, EntityType.GIANT),
    GUARDIAN("Guardian", 68, "Guardian Pet", 20D, 10D, EntityType.GUARDIAN, PetData.ELDER),
    MAGMACUBE("MagmaCube", 62, "Magma Cube Pet", 20D, 5D, EntityType.MAGMA_CUBE, PetData.SMALL, PetData.MEDIUM, PetData.LARGE),
    PIGZOMBIE("PigZombie", 57, "Pig Zombie Pet", 20D, 6D, EntityType.PIG_ZOMBIE, PetData.BABY),
    SILVERFISH("Silverfish", 60, "Silverfish Pet", 8D, 4D, EntityType.SILVERFISH),
    SKELETON("Skeleton", 51, "Skeleton Pet", 20D, 5D, EntityType.SKELETON, PetData.WITHER),
    SLIME("Slime", 55, "Slime Pet", 20D, 4D, EntityType.SLIME, PetData.SMALL, PetData.MEDIUM, PetData.LARGE),
    SPIDER("Spider", 52, "Spider Pet", 16D, 5D, EntityType.SPIDER),
    WITCH("Witch", 66, "Witch Pet", 26D, 5D, EntityType.WITCH),
    WITHER("Wither", 64, "Wither Pet", 300D, 8D, EntityType.WITHER, PetData.SHIELD),
    ZOMBIE("Zombie", 54, "Zombie Pet", 20D, 5D, EntityType.ZOMBIE, PetData.BABY, PetData.VILLAGER),

    // Passive mobs
    BAT("Bat", 65, "Bat Pet", 6D, 3D, EntityType.BAT),
    CHICKEN("Chicken", 93, "Chicken Pet", 4D, 3D, EntityType.CHICKEN, PetData.BABY),
    COW("Cow", 92, "Cow Pet", 10D, 4D, EntityType.COW, PetData.BABY),
    HORSE("Horse", 100, "Horse Pet", 30D, 4D, EntityType.HORSE, PetData.BABY, PetData.CHESTED, PetData.SADDLE,
          PetData.NORMAL, PetData.DONKEY,
          PetData.MULE, PetData.SKELETON, PetData.ZOMBIE, PetData.WHITE,
          PetData.CREAMY, PetData.CHESTNUT, PetData.BROWN, PetData.BLACK,
          PetData.GRAY, PetData.DARKBROWN, PetData.NONE, PetData.SOCKS,
          PetData.WHITEPATCH, PetData.WHITESPOT, PetData.BLACKSPOT,
          PetData.NOARMOUR, PetData.IRON, PetData.GOLD, PetData.DIAMOND),
    IRONGOLEM("IronGolem", 99, "Iron Golem Pet", 100D, 7D, EntityType.IRON_GOLEM),
    MUSHROOMCOW("MushroomCow", 96, "Mushroom Cow Pet", 10D, 3D, EntityType.MUSHROOM_COW, PetData.BABY),
    OCELOT("Ocelot", 98, "Ocelot Pet", 10D, 4D, EntityType.OCELOT, PetData.BABY, PetData.BLACK, PetData.RED, PetData.SIAMESE, PetData.WILD),
    PIG("Pig", 90, "Pig Pet", 10D, 3D, EntityType.PIG, PetData.BABY, PetData.SADDLE),
    RABBIT("Rabbit", 101, "Rabbit Pet", 8D, 3D, EntityType.RABBIT, PetData.BROWN, PetData.WHITE, PetData.BLACK, PetData.BLACK_AND_WHITE, PetData.SALT_AND_PEPPER, PetData.THE_KILLER_BUNNY),
    SHEEP("Sheep", 91, "Sheep Pet", 8D, 3D, EntityType.SHEEP, PetData.BABY, PetData.SHEARED,
          PetData.BLACK, PetData.BLUE, PetData.BROWN,
          PetData.CYAN, PetData.GRAY, PetData.GREEN,
          PetData.LIGHTBLUE, PetData.LIME, PetData.MAGENTA,
          PetData.ORANGE, PetData.PINK, PetData.PURPLE, PetData.RED,
          PetData.SILVER, PetData.WHITE, PetData.YELLOW),
    SNOWMAN("Snowman", 97, "Snowman Pet", 4D, 4D, EntityType.SNOWMAN),
    SQUID("Squid", 94, "Squid Pet", 10D, 4D, EntityType.SQUID),
    VILLAGER("Villager", 120, "Villager Pet", 20D, 4D, EntityType.VILLAGER, PetData.BABY, PetData.BLACKSMITH, PetData.BUTCHER, PetData.FARMER, PetData.LIBRARIAN, PetData.PRIEST),
    WOLF("Wolf", 95, "Wolf Pet", 20D, 6D, EntityType.WOLF, PetData.BABY, PetData.TAMED, PetData.ANGRY,
         PetData.BLACK, PetData.BLUE, PetData.BROWN,
         PetData.CYAN, PetData.GRAY, PetData.GREEN,
         PetData.LIGHTBLUE, PetData.LIME, PetData.MAGENTA,
         PetData.ORANGE, PetData.PINK, PetData.PURPLE, PetData.RED,
         PetData.SILVER, PetData.WHITE, PetData.YELLOW),

    HUMAN("Human", 54, "Human Pet", 20D, 6D, EntityType.UNKNOWN);

    private Class<? extends IEntityPet> entityClass;
    private Class<? extends IPet> petClass;
    private String defaultName;
    private double maxHealth;
    private double attackDamage;
    private EntityType entityType;
    private List<PetData> allowedData;
    private int id;

    PetType(String classIdentifier, int registrationId, String defaultName, double maxHealth, double attackDamage, EntityType entityType, PetData... allowedData) {
        try {
            this.entityClass = (Class<? extends IEntityPet>) Class.forName(ReflectionUtil.COMPAT_NMS_PATH + ".entity.type.Entity" + classIdentifier + "Pet");
        } catch (ClassNotFoundException e) {
            // do nothing
        }
        this.petClass = ReflectionUtil.getClass("com.dsh105.echopet.api.pet.type." + classIdentifier + "Pet");
        this.id = registrationId;
        this.allowedData = ImmutableList.copyOf(allowedData);
        this.maxHealth = maxHealth;
        this.attackDamage = attackDamage;
        this.entityType = entityType;
        this.defaultName = defaultName;
    }

    public int getRegistrationId() {
        return this.id;
    }

    public double getMaxHealth() {
        return this.maxHealth;
    }

    public String getDefaultName(String name) {
        return EchoPet.getConfig().getString("pets." + this.toString().toLowerCase().replace("_", " ") + ".defaultName", this.defaultName).replace("(user)", name).replace("(userApos)", name + "'s");
    }

    public String getDefaultName() {
        return this.defaultName;
    }

    public double getAttackDamage() {
        return EchoPet.getConfig().getDouble("pets." + this.toString().toLowerCase().replace("_", " ") + ".attackDamage", this.attackDamage);
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public List<PetData> getAllowedDataTypes() {
        return this.allowedData;
    }

    public boolean isDataAllowed(PetData data) {
        return getAllowedDataTypes().contains(data);
    }

    public IEntityPet getNewEntityPetInstance(Object world, IPet pet) {
        return EchoPet.getPetRegistry().getRegistrationEntry(pet.getPetType()).createEntityPet(world, pet);
    }

    public IPet getNewPetInstance(Player owner) {
        if (owner != null) {
            return EchoPet.getPetRegistry().spawn(this, owner);
        }
        return null;
    }

    public Class<? extends IEntityPet> getEntityClass() {
        return this.entityClass;
    }

    public Class<? extends IPet> getPetClass() {
        return this.petClass;
    }
}