package io.github.dsh105.echopet.entity;

import com.dsh105.dshutils.logger.Logger;
import com.google.common.collect.ImmutableList;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.entity.type.bat.BatPet;
import io.github.dsh105.echopet.entity.type.bat.CraftBatPet;
import io.github.dsh105.echopet.entity.type.bat.EntityBatPet;
import io.github.dsh105.echopet.entity.type.blaze.BlazePet;
import io.github.dsh105.echopet.entity.type.blaze.CraftBlazePet;
import io.github.dsh105.echopet.entity.type.blaze.EntityBlazePet;
import io.github.dsh105.echopet.entity.type.cavespider.CaveSpiderPet;
import io.github.dsh105.echopet.entity.type.cavespider.CraftCaveSpiderPet;
import io.github.dsh105.echopet.entity.type.cavespider.EntityCaveSpiderPet;
import io.github.dsh105.echopet.entity.type.chicken.ChickenPet;
import io.github.dsh105.echopet.entity.type.chicken.CraftChickenPet;
import io.github.dsh105.echopet.entity.type.chicken.EntityChickenPet;
import io.github.dsh105.echopet.entity.type.cow.CowPet;
import io.github.dsh105.echopet.entity.type.cow.CraftCowPet;
import io.github.dsh105.echopet.entity.type.cow.EntityCowPet;
import io.github.dsh105.echopet.entity.type.creeper.CraftCreeperPet;
import io.github.dsh105.echopet.entity.type.creeper.CreeperPet;
import io.github.dsh105.echopet.entity.type.creeper.EntityCreeperPet;
import io.github.dsh105.echopet.entity.type.enderdragon.CraftEnderDragonPet;
import io.github.dsh105.echopet.entity.type.enderdragon.EnderDragonPet;
import io.github.dsh105.echopet.entity.type.enderdragon.EntityEnderDragonPet;
import io.github.dsh105.echopet.entity.type.enderman.CraftEndermanPet;
import io.github.dsh105.echopet.entity.type.enderman.EndermanPet;
import io.github.dsh105.echopet.entity.type.enderman.EntityEndermanPet;
import io.github.dsh105.echopet.entity.type.ghast.CraftGhastPet;
import io.github.dsh105.echopet.entity.type.ghast.EntityGhastPet;
import io.github.dsh105.echopet.entity.type.ghast.GhastPet;
import io.github.dsh105.echopet.entity.type.giant.CraftGiantPet;
import io.github.dsh105.echopet.entity.type.giant.EntityGiantPet;
import io.github.dsh105.echopet.entity.type.giant.GiantPet;
import io.github.dsh105.echopet.entity.type.horse.CraftHorsePet;
import io.github.dsh105.echopet.entity.type.horse.EntityHorsePet;
import io.github.dsh105.echopet.entity.type.horse.HorsePet;
import io.github.dsh105.echopet.entity.type.human.CraftHumanPet;
import io.github.dsh105.echopet.entity.type.human.EntityHumanPet;
import io.github.dsh105.echopet.entity.type.human.HumanPet;
import io.github.dsh105.echopet.entity.type.irongolem.CraftIronGolemPet;
import io.github.dsh105.echopet.entity.type.irongolem.EntityIronGolemPet;
import io.github.dsh105.echopet.entity.type.irongolem.IronGolemPet;
import io.github.dsh105.echopet.entity.type.magmacube.CraftMagmaCubePet;
import io.github.dsh105.echopet.entity.type.magmacube.EntityMagmaCubePet;
import io.github.dsh105.echopet.entity.type.magmacube.MagmaCubePet;
import io.github.dsh105.echopet.entity.type.mushroomcow.CraftMushroomCowPet;
import io.github.dsh105.echopet.entity.type.mushroomcow.EntityMushroomCowPet;
import io.github.dsh105.echopet.entity.type.mushroomcow.MushroomCowPet;
import io.github.dsh105.echopet.entity.type.ocelot.CraftOcelotPet;
import io.github.dsh105.echopet.entity.type.ocelot.EntityOcelotPet;
import io.github.dsh105.echopet.entity.type.ocelot.OcelotPet;
import io.github.dsh105.echopet.entity.type.pig.CraftPigPet;
import io.github.dsh105.echopet.entity.type.pig.EntityPigPet;
import io.github.dsh105.echopet.entity.type.pig.PigPet;
import io.github.dsh105.echopet.entity.type.pigzombie.CraftPigZombiePet;
import io.github.dsh105.echopet.entity.type.pigzombie.EntityPigZombiePet;
import io.github.dsh105.echopet.entity.type.pigzombie.PigZombiePet;
import io.github.dsh105.echopet.entity.type.sheep.CraftSheepPet;
import io.github.dsh105.echopet.entity.type.sheep.EntitySheepPet;
import io.github.dsh105.echopet.entity.type.sheep.SheepPet;
import io.github.dsh105.echopet.entity.type.silverfish.CraftSilverfishPet;
import io.github.dsh105.echopet.entity.type.silverfish.EntitySilverfishPet;
import io.github.dsh105.echopet.entity.type.silverfish.SilverfishPet;
import io.github.dsh105.echopet.entity.type.skeleton.CraftSkeletonPet;
import io.github.dsh105.echopet.entity.type.skeleton.EntitySkeletonPet;
import io.github.dsh105.echopet.entity.type.skeleton.SkeletonPet;
import io.github.dsh105.echopet.entity.type.slime.CraftSlimePet;
import io.github.dsh105.echopet.entity.type.slime.EntitySlimePet;
import io.github.dsh105.echopet.entity.type.slime.SlimePet;
import io.github.dsh105.echopet.entity.type.snowman.CraftSnowmanPet;
import io.github.dsh105.echopet.entity.type.snowman.EntitySnowmanPet;
import io.github.dsh105.echopet.entity.type.snowman.SnowmanPet;
import io.github.dsh105.echopet.entity.type.spider.CraftSpiderPet;
import io.github.dsh105.echopet.entity.type.spider.EntitySpiderPet;
import io.github.dsh105.echopet.entity.type.spider.SpiderPet;
import io.github.dsh105.echopet.entity.type.squid.CraftSquidPet;
import io.github.dsh105.echopet.entity.type.squid.EntitySquidPet;
import io.github.dsh105.echopet.entity.type.squid.SquidPet;
import io.github.dsh105.echopet.entity.type.villager.CraftVillagerPet;
import io.github.dsh105.echopet.entity.type.villager.EntityVillagerPet;
import io.github.dsh105.echopet.entity.type.villager.VillagerPet;
import io.github.dsh105.echopet.entity.type.witch.CraftWitchPet;
import io.github.dsh105.echopet.entity.type.witch.EntityWitchPet;
import io.github.dsh105.echopet.entity.type.witch.WitchPet;
import io.github.dsh105.echopet.entity.type.wither.CraftWitherPet;
import io.github.dsh105.echopet.entity.type.wither.EntityWitherPet;
import io.github.dsh105.echopet.entity.type.wither.WitherPet;
import io.github.dsh105.echopet.entity.type.wolf.CraftWolfPet;
import io.github.dsh105.echopet.entity.type.wolf.EntityWolfPet;
import io.github.dsh105.echopet.entity.type.wolf.WolfPet;
import io.github.dsh105.echopet.entity.type.zombie.CraftZombiePet;
import io.github.dsh105.echopet.entity.type.zombie.EntityZombiePet;
import io.github.dsh105.echopet.entity.type.zombie.ZombiePet;
import net.minecraft.server.v1_7_R1.World;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public enum PetType {

	/*
     * Enumeration of valid pet types
	 * Stores valid pet data types
	 */

    // Aggressive mobs
    BLAZE(EntityBlazePet.class, BlazePet.class, CraftBlazePet.class, 61, "Blaze Pet", 20D, 6D, EntityType.BLAZE, PetData.FIRE),
    CAVESPIDER(EntityCaveSpiderPet.class, CaveSpiderPet.class, CraftCaveSpiderPet.class, 59, "Cave Spider Pet", 12D, 5D, EntityType.CAVE_SPIDER),
    CREEPER(EntityCreeperPet.class, CreeperPet.class, CraftCreeperPet.class, 50, "Creeper Pet", 20D, 6D, EntityType.CREEPER, PetData.POWER),
    ENDERDRAGON(EntityEnderDragonPet.class, EnderDragonPet.class, CraftEnderDragonPet.class, 63, "EnderDragon Pet", 200D, 0D, EntityType.ENDER_DRAGON),
    ENDERMAN(EntityEndermanPet.class, EndermanPet.class, CraftEndermanPet.class, 58, "Enderman Pet", 40D, 6D, EntityType.ENDERMAN, PetData.SCREAMING),
    GHAST(EntityGhastPet.class, GhastPet.class, CraftGhastPet.class, 56, "Ghast Pet", 10D, 7D, EntityType.GHAST),
    GIANT(EntityGiantPet.class, GiantPet.class, CraftGiantPet.class, 53, "Giant Pet", 100D, 0D, EntityType.GIANT),
    MAGMACUBE(EntityMagmaCubePet.class, MagmaCubePet.class, CraftMagmaCubePet.class, 62, "Magma Cube Pet", 20D, 5D, EntityType.MAGMA_CUBE, PetData.SMALL, PetData.MEDIUM, PetData.LARGE),
    PIGZOMBIE(EntityPigZombiePet.class, PigZombiePet.class, CraftPigZombiePet.class, 57, "Pig Zombie Pet", 20D, 6D, EntityType.PIG_ZOMBIE, PetData.BABY, PetData.VILLAGER),
    SILVERFISH(EntitySilverfishPet.class, SilverfishPet.class, CraftSilverfishPet.class, 60, "Silverfish Pet", 8D, 4D, EntityType.SILVERFISH),
    SKELETON(EntitySkeletonPet.class, SkeletonPet.class, CraftSkeletonPet.class, 51, "Skeleton Pet", 20D, 5D, EntityType.SKELETON, PetData.WITHER),
    SLIME(EntitySlimePet.class, SlimePet.class, CraftSlimePet.class, 55, "Slime Pet", 20D, 4D, EntityType.SLIME, PetData.SMALL, PetData.MEDIUM, PetData.LARGE),
    SPIDER(EntitySpiderPet.class, SpiderPet.class, CraftSpiderPet.class, 52, "Spider Pet", 16D, 5D, EntityType.SPIDER),
    WITCH(EntityWitchPet.class, WitchPet.class, CraftWitherPet.class, 66, "Witch Pet", 26D, 5D, EntityType.WITCH),
    WITHER(EntityWitherPet.class, WitherPet.class, CraftWitchPet.class, 64, "Wither Pet", 300D, 8D, EntityType.WITHER, PetData.SHIELD),
    ZOMBIE(EntityZombiePet.class, ZombiePet.class, CraftZombiePet.class, 54, "Zombie Pet", 20D, 5D, EntityType.ZOMBIE, PetData.BABY, PetData.VILLAGER),

    // Passive mobs
    BAT(EntityBatPet.class, BatPet.class, CraftBatPet.class, 65, "Bat Pet", 6D, 3D, EntityType.BAT),
    CHICKEN(EntityChickenPet.class, ChickenPet.class, CraftChickenPet.class, 93, "Chicken Pet", 4D, 3D, EntityType.CHICKEN, PetData.BABY),
    COW(EntityCowPet.class, CowPet.class, CraftCowPet.class, 92, "Cow Pet", 10D, 4D, EntityType.COW, PetData.BABY),
    HORSE(EntityHorsePet.class, HorsePet.class, CraftHorsePet.class, 100, "Horse Pet", 30D, 4D, EntityType.HORSE, PetData.BABY, PetData.CHESTED, PetData.SADDLE,
            PetData.NORMAL, PetData.DONKEY,
            PetData.MULE, PetData.SKELETON, PetData.ZOMBIE, PetData.WHITE,
            PetData.CREAMY, PetData.CHESTNUT, PetData.BROWN, PetData.BLACK,
            PetData.GRAY, PetData.DARKBROWN, PetData.NONE, PetData.SOCKS,
            PetData.WHITEPATCH, PetData.WHITESPOT, PetData.BLACKSPOT,
            PetData.NOARMOUR, PetData.IRON, PetData.GOLD, PetData.DIAMOND),
    IRONGOLEM(EntityIronGolemPet.class, IronGolemPet.class, CraftIronGolemPet.class, 99, "Iron Golem Pet", 100D, 7D, EntityType.IRON_GOLEM),
    MUSHROOMCOW(EntityMushroomCowPet.class, MushroomCowPet.class, CraftMushroomCowPet.class, 96, "Mushroom Cow Pet", 10D, 3D, EntityType.MUSHROOM_COW, PetData.BABY),
    OCELOT(EntityOcelotPet.class, OcelotPet.class, CraftOcelotPet.class, 98, "Ocelot Pet", 10D, 4D, EntityType.OCELOT, PetData.BABY, PetData.BLACK, PetData.RED, PetData.SIAMESE, PetData.WILD),
    PIG(EntityPigPet.class, PigPet.class, CraftPigPet.class, 90, "Pig Pet", 10D, 3D, EntityType.PIG, PetData.BABY, PetData.SADDLE),
    SHEEP(EntitySheepPet.class, SheepPet.class, CraftSheepPet.class, 91, "Sheep Pet", 8D, 3D, EntityType.SHEEP, PetData.BABY, PetData.SHEARED,
            PetData.BLACK, PetData.BLUE, PetData.BROWN,
            PetData.CYAN, PetData.GRAY, PetData.GREEN,
            PetData.LIGHTBLUE, PetData.LIME, PetData.MAGENTA,
            PetData.ORANGE, PetData.PINK, PetData.PURPLE, PetData.RED,
            PetData.SILVER, PetData.WHITE, PetData.YELLOW),
    SNOWMAN(EntitySnowmanPet.class, SnowmanPet.class, CraftSnowmanPet.class, 97, "Snowman Pet", 4D, 4D, EntityType.SNOWMAN),
    SQUID(EntitySquidPet.class, SquidPet.class, CraftSquidPet.class, 94, "Squid Pet", 10D, 4D, EntityType.SQUID),
    VILLAGER(EntityVillagerPet.class, VillagerPet.class, CraftVillagerPet.class, 120, "Villager Pet", 20D, 4D, EntityType.VILLAGER, PetData.BABY, PetData.BLACKSMITH, PetData.BUTCHER, PetData.FARMER, PetData.LIBRARIAN, PetData.PRIEST),
    WOLF(EntityWolfPet.class, WolfPet.class, CraftWolfPet.class, 95, "Wolf Pet", 20D, 6D, EntityType.WOLF, PetData.BABY, PetData.TAMED, PetData.ANGRY,
            PetData.BLACK, PetData.BLUE, PetData.BROWN,
            PetData.CYAN, PetData.GRAY, PetData.GREEN,
            PetData.LIGHTBLUE, PetData.LIME, PetData.MAGENTA,
            PetData.ORANGE, PetData.PINK, PetData.PURPLE, PetData.RED,
            PetData.SILVER, PetData.WHITE, PetData.YELLOW),

    HUMAN(EntityHumanPet.class, HumanPet.class, CraftHumanPet.class, 54, "Human Pet", 20D, 6D, EntityType.UNKNOWN);

    private Class<? extends EntityPet> entityClass;
    private Class<? extends Pet> petClass;
    private Class<? extends CraftPet> craftClass;
    private String defaultName;
    private double maxHealth;
    private double attackDamage;
    private EntityType entityType;
    private List<PetData> allowedData;
    private int id;

    PetType(Class<? extends EntityPet> entityClass, Class<? extends Pet> petClass, Class<? extends CraftPet> craftClass, int registrationId, String defaultName, double maxHealth, double attackDamage, EntityType entityType, PetData... allowedData) {
        this.entityClass = entityClass;
        this.petClass = petClass;
        this.craftClass = craftClass;
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
        return EchoPetPlugin.getInstance().getMainConfig().getString("pets." + this.toString().toLowerCase().replace("_", " ") + ".defaultName", this.defaultName).replace("(user)", name).replace("(userApos)", name + "'s");
    }

    public String getDefaultName() {
        return this.defaultName;
    }

    public double getAttackDamage() {
        return EchoPetPlugin.getInstance().getMainConfig().getDouble("pets." + this.toString().toLowerCase().replace("_", " ") + ".attackDamage", this.attackDamage);
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

    public EntityPet getNewEntityPetInstance(World world, Pet pet) {
        EntityPet ePet = null;
        try {
            Object o = this.entityClass.getConstructor(World.class, Pet.class).newInstance(world, pet);
            if (o instanceof EntityPet) {
                ePet = (EntityPet) o;
            }
        } catch (NoSuchMethodException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new EntityPet instance.", e, true);
        } catch (SecurityException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new EntityPet instance.", e, true);
        } catch (InstantiationException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new EntityPet instance.", e, true);
        } catch (IllegalAccessException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new EntityPet instance.", e, true);
        } catch (IllegalArgumentException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new EntityPet instance.", e, true);
        } catch (InvocationTargetException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new EntityPet instance.", e, true);
        }
        return ePet;
    }

    public Pet getNewPetInstance(Player owner) {
        Pet p = null;
        try {
            Object o = petClass.getConstructor(Player.class).newInstance(owner);
            if (o instanceof Pet) {
                p = (Pet) o;
            }
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new Pet instance for " + owner + ".", e, true);
        }
        return p;
    }

    public Pet getNewPetInstance(String owner, EntityPet entityPet) {
        Pet p = null;
        try {
            Object o = petClass.getConstructor(String.class, EntityPet.class).newInstance(owner, entityPet);
            if (o instanceof Pet) {
                p = (Pet) o;
            }
        } catch (NoSuchMethodException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new Pet instance.", e, true);
        } catch (SecurityException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new Pet instance.", e, true);
        } catch (InstantiationException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new Pet instance.", e, true);
        } catch (IllegalAccessException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new Pet instance.", e, true);
        } catch (IllegalArgumentException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new Pet instance.", e, true);
        } catch (InvocationTargetException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new Pet instance.", e, true);
        }
        return p;
    }

    public CraftPet getNewCraftInstance(EntityPet entityPet) {
        CraftPet p = null;
        try {
            Object o = this.craftClass.getConstructor(CraftServer.class, EntityPet.class).newInstance(entityPet.world.getServer(), entityPet);
            if (o instanceof CraftPet) {
                p = (CraftPet) o;
            }
        } catch (NoSuchMethodException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new CraftPet instance [" + entityPet.getPet().getPetType().toString() + "].", e, true);
        } catch (SecurityException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new CraftPet instance [" + entityPet.getPet().getPetType().toString() + "].", e, true);
        } catch (InstantiationException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new CraftPet instance [" + entityPet.getPet().getPetType().toString() + "].", e, true);
        } catch (IllegalAccessException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new CraftPet instance [" + entityPet.getPet().getPetType().toString() + "].", e, true);
        } catch (IllegalArgumentException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new CraftPet instance [" + entityPet.getPet().getPetType().toString() + "].", e, true);
        } catch (InvocationTargetException e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create new CraftPet instance [" + entityPet.getPet().getPetType().toString() + "].", e, true);
        }
        return p;
    }

    public Class<? extends EntityPet> getEntityClass() {
        return this.entityClass;
    }

    public Class<? extends Pet> getPetClass() {
        return this.petClass;
    }

    public Class<? extends CraftPet> getCraftClass() {
        return this.craftClass;
    }
}