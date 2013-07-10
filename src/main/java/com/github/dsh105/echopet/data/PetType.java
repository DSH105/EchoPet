package com.github.dsh105.echopet.data;

import java.util.Arrays;
import java.util.List;

import net.minecraft.server.v1_6_R2.World;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.bat.BatPet;
import com.github.dsh105.echopet.entity.pet.bat.EntityBatPet;
import com.github.dsh105.echopet.entity.pet.blaze.BlazePet;
import com.github.dsh105.echopet.entity.pet.blaze.EntityBlazePet;
import com.github.dsh105.echopet.entity.pet.cavespider.CaveSpiderPet;
import com.github.dsh105.echopet.entity.pet.cavespider.EntityCaveSpiderPet;
import com.github.dsh105.echopet.entity.pet.chicken.ChickenPet;
import com.github.dsh105.echopet.entity.pet.chicken.EntityChickenPet;
import com.github.dsh105.echopet.entity.pet.cow.CowPet;
import com.github.dsh105.echopet.entity.pet.cow.EntityCowPet;
import com.github.dsh105.echopet.entity.pet.creeper.CreeperPet;
import com.github.dsh105.echopet.entity.pet.creeper.EntityCreeperPet;
import com.github.dsh105.echopet.entity.pet.enderman.EndermanPet;
import com.github.dsh105.echopet.entity.pet.enderman.EntityEndermanPet;
import com.github.dsh105.echopet.entity.pet.ghast.EntityGhastPet;
import com.github.dsh105.echopet.entity.pet.ghast.GhastPet;
import com.github.dsh105.echopet.entity.pet.horse.EntityHorsePet;
import com.github.dsh105.echopet.entity.pet.horse.HorsePet;
import com.github.dsh105.echopet.entity.pet.irongolem.EntityIronGolemPet;
import com.github.dsh105.echopet.entity.pet.irongolem.IronGolemPet;
import com.github.dsh105.echopet.entity.pet.magmacube.EntityMagmaCubePet;
import com.github.dsh105.echopet.entity.pet.magmacube.MagmaCubePet;
import com.github.dsh105.echopet.entity.pet.mushroomcow.EntityMushroomCowPet;
import com.github.dsh105.echopet.entity.pet.mushroomcow.MushroomCowPet;
import com.github.dsh105.echopet.entity.pet.ocelot.EntityOcelotPet;
import com.github.dsh105.echopet.entity.pet.ocelot.OcelotPet;
import com.github.dsh105.echopet.entity.pet.pig.EntityPigPet;
import com.github.dsh105.echopet.entity.pet.pig.PigPet;
import com.github.dsh105.echopet.entity.pet.pigzombie.EntityPigZombiePet;
import com.github.dsh105.echopet.entity.pet.pigzombie.PigZombiePet;
import com.github.dsh105.echopet.entity.pet.sheep.EntitySheepPet;
import com.github.dsh105.echopet.entity.pet.sheep.SheepPet;
import com.github.dsh105.echopet.entity.pet.silverfish.EntitySilverfishPet;
import com.github.dsh105.echopet.entity.pet.silverfish.SilverfishPet;
import com.github.dsh105.echopet.entity.pet.skeleton.EntitySkeletonPet;
import com.github.dsh105.echopet.entity.pet.skeleton.SkeletonPet;
import com.github.dsh105.echopet.entity.pet.slime.EntitySlimePet;
import com.github.dsh105.echopet.entity.pet.slime.SlimePet;
import com.github.dsh105.echopet.entity.pet.snowman.EntitySnowmanPet;
import com.github.dsh105.echopet.entity.pet.snowman.SnowmanPet;
import com.github.dsh105.echopet.entity.pet.spider.EntitySpiderPet;
import com.github.dsh105.echopet.entity.pet.spider.SpiderPet;
import com.github.dsh105.echopet.entity.pet.squid.EntitySquidPet;
import com.github.dsh105.echopet.entity.pet.squid.SquidPet;
import com.github.dsh105.echopet.entity.pet.villager.EntityVillagerPet;
import com.github.dsh105.echopet.entity.pet.villager.VillagerPet;
import com.github.dsh105.echopet.entity.pet.witch.EntityWitchPet;
import com.github.dsh105.echopet.entity.pet.witch.WitchPet;
import com.github.dsh105.echopet.entity.pet.wither.EntityWitherPet;
import com.github.dsh105.echopet.entity.pet.wither.WitherPet;
import com.github.dsh105.echopet.entity.pet.wolf.EntityWolfPet;
import com.github.dsh105.echopet.entity.pet.wolf.WolfPet;
import com.github.dsh105.echopet.entity.pet.zombie.EntityZombiePet;
import com.github.dsh105.echopet.entity.pet.zombie.ZombiePet;

public enum PetType {
	
	/*
	 * Enumeration of valid pet types
	 * Stores valid pet data types
	 */
	
	// Aggressive mobs
	BLAZE(EntityBlazePet.class, BlazePet.class, "Blaze Pet", 20, EntityType.BLAZE, PetData.FIRE),
	CAVESPIDER(EntityCaveSpiderPet.class, CaveSpiderPet.class, "Cave Spider Pet", 12, EntityType.CAVE_SPIDER),
	CREEPER(EntityCreeperPet.class, CreeperPet.class, "Creeper Pet", 20, EntityType.CREEPER, PetData.POWER),
	ENDERMAN(EntityEndermanPet.class, EndermanPet.class, "Enderman Pet", 40, EntityType.ENDERMAN, PetData.SCREAMING),
	GHAST(EntityGhastPet.class, GhastPet.class, "Ghast Pet", 10, EntityType.GHAST),
	MAGMACUBE(EntityMagmaCubePet.class, MagmaCubePet.class, "Magma Cube Pet", 20, EntityType.MAGMA_CUBE, PetData.SMALL, PetData.MEDIUM, PetData.LARGE),
	PIGZOMBIE(EntityPigZombiePet.class, PigZombiePet.class, "Pig Zombie Pet", 20, EntityType.PIG_ZOMBIE, PetData.BABY, PetData.VILLAGER),
	SILVERFISH(EntitySilverfishPet.class, SilverfishPet.class, "Silverfish Pet", 8, EntityType.SILVERFISH),
	SKELETON(EntitySkeletonPet.class, SkeletonPet.class, "Skeleton Pet", 20, EntityType.SKELETON, PetData.WITHER),
	SLIME(EntitySlimePet.class, SlimePet.class, "Slime Pet", 20, EntityType.SLIME, PetData.SMALL, PetData.MEDIUM, PetData.LARGE),
	SPIDER(EntitySpiderPet.class, SpiderPet.class, "Spider Pet", 16, EntityType.SPIDER),
	WITCH(EntityWitchPet.class, WitchPet.class, "Witch Pet", 26, EntityType.WITCH),
	WITHER(EntityWitherPet.class, WitherPet.class, "Wither Pet", 300, EntityType.WITHER, PetData.SHIELD),
	ZOMBIE(EntityZombiePet.class, ZombiePet.class, "Zombie Pet", 20, EntityType.ZOMBIE, PetData.BABY, PetData.VILLAGER),
	
	// Passive mobs
	BAT(EntityBatPet.class, BatPet.class, "Bat Pet", 6, EntityType.BAT),
	CHICKEN(EntityChickenPet.class, ChickenPet.class, "Chicken Pet", 4, EntityType.CHICKEN, PetData.BABY),
	COW(EntityCowPet.class, CowPet.class, "Cow Pet", 10, EntityType.COW, PetData.BABY),
	HORSE(EntityHorsePet.class, HorsePet.class, "Horse Pet", 30, EntityType.HORSE, /*PetData.BABY,*/ PetData.CHESTED,    // I tested baby horses and they just turned invisible. It also resets all of their existing data. I'll have to find a workaround for this...
			PetData.NORMAL, PetData.DONKEY,
			PetData.MULE, PetData.SKELETON, PetData.ZOMBIE, PetData.WHITE,
			PetData.CREAMY, PetData.CHESTNUT, PetData.BROWN, PetData.BLACK,
			PetData.GRAY, PetData.DARKBROWN, PetData.NONE, PetData.SOCKS,
			PetData.WHITEPATCH, PetData.WHITESPOT, PetData.BLACKSPOT,
			PetData.IRON, PetData.GOLD, PetData.DIAMOND),
	IRONGOLEM(EntityIronGolemPet.class, IronGolemPet.class, "Iron Golem Pet", 100, EntityType.IRON_GOLEM),
	MUSHROOMCOW(EntityMushroomCowPet.class, MushroomCowPet.class, "Mushroom Cow Pet", 10, EntityType.MUSHROOM_COW, PetData.BABY),
	OCELOT(EntityOcelotPet.class, OcelotPet.class, "Ocelot Pet", 10, EntityType.OCELOT, PetData.BABY, PetData.BLACK, PetData.RED, PetData.SIAMESE, PetData.WILD),
	PIG(EntityPigPet.class, PigPet.class, "Pig Pet", 10, EntityType.PIG, PetData.BABY, PetData.SADDLE),
	SNOWMAN(EntitySnowmanPet.class, SnowmanPet.class, "Snowman Pet", 4, EntityType.SNOWMAN),
	SHEEP(EntitySheepPet.class, SheepPet.class, "Sheep Pet", 8, EntityType.SHEEP, PetData.BABY, PetData.SHEARED,
			PetData.BLACK, PetData.BLUE, PetData.BROWN,
			PetData.CYAN, PetData.GRAY, PetData.GREEN,
			PetData.LIGHTBLUE, PetData.LIME, PetData.MAGENTA,
			PetData.ORANGE, PetData.PINK, PetData.PURPLE,
			PetData.SILVER, PetData.WHITE, PetData.YELLOW),
	SQUID(EntitySquidPet.class, SquidPet.class, "Squid Pet", 10, EntityType.SQUID),
	WOLF(EntityWolfPet.class, WolfPet.class, "Wolf Pet", 20, EntityType.WOLF, PetData.BABY, PetData.TAMED, PetData.ANGRY,
			PetData.BLACK, PetData.BLUE, PetData.BROWN,
			PetData.CYAN, PetData.GRAY, PetData.GREEN,
			PetData.LIGHTBLUE, PetData.LIME, PetData.MAGENTA,
			PetData.ORANGE, PetData.PINK, PetData.PURPLE,
			PetData.SILVER, PetData.WHITE, PetData.YELLOW),
	VILLAGER(EntityVillagerPet.class, VillagerPet.class, "Villager Pet", 20, EntityType.VILLAGER, PetData.BABY, PetData.BLACKSMITH, PetData.BUTCHER, PetData.FARMER, PetData.LIBRARIAN, PetData.PRIEST),
	
	
	/*
	 * It is currently undecided whether to include Wisp and Human Pets.
	 * May cause major issues, especially seeing as the Wisp is not based off a living entity.
	 */
	
	//WISP(EntityWispPet.class, WispPet.class, "Wisp Pet", EntityType.EXPERIENCE_ORB),
	//HUMAN(EntityHumanPet.class, HumanPet.class, "Human Pet", 20, EntityType.UNKNOWN)
	;
	
	private Class<? extends EntityPet> entityClass;
	private Class<? extends Pet> craftClass;
	private String defaultName;
	private int maxHealth;
	private EntityType entityType;
	private List<PetData> allowedData;
	PetType(Class<? extends EntityPet> entityClass, Class<? extends Pet> craftClass, String defaultName, int maxHealth, EntityType entityType, PetData... allowedData) {
		this.entityClass = entityClass;
		this.craftClass = craftClass;
		this.allowedData = Arrays.asList(allowedData);
		this.maxHealth = maxHealth;
		this.entityType = entityType;
		this.defaultName = defaultName;
	}
	
	public int getMaxHealth() {
		return this.maxHealth;
	}
	
	public String getDefaultName(Player p) {
		return EchoPet.getPluginInstance().getMainConfig().getString("pets." + this.toString().toLowerCase().replace("_", " ") + ".defaultName", this.defaultName).replace("(user)", p.getName()).replace("(userApos)", p.getName() + "'s");
		//return this.defaultName;
	}
	
	public String getDefaultName() {
		return this.defaultName;
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

	public EntityPet getNewEntityInstance(World world, Pet pet) {
		EntityPet ePet = null;
		try {
			Object o = entityClass.getConstructor(World.class, Pet.class).newInstance(world, pet);
			if (o instanceof EntityPet) {
				ePet = (EntityPet) o;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ePet;
	}
	
	public Pet getNewPetInstance(Player owner, PetType pt) {
		Pet p = null;
		try {
			Object o = craftClass.getConstructor(Player.class, PetType.class).newInstance(owner, pt);
			if (o instanceof Pet) {
				p = (Pet) o;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public Class<? extends EntityPet> getEntityClass() {
		return this.entityClass;
	}
	
	public Class<? extends Pet> getPetClass() {
		return this.craftClass;
	}
}