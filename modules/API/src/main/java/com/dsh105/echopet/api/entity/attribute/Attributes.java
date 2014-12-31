package com.dsh105.echopet.api.entity.attribute;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.reflection.Reflection;
import com.dsh105.echopet.api.entity.Entity;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.Traits;
import com.dsh105.echopet.api.entity.Voice;
import com.dsh105.echopet.api.entity.entitypet.EntityAgeablePet;
import com.dsh105.echopet.api.entity.entitypet.EntityEquipablePet;
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import com.dsh105.echopet.api.entity.entitypet.EntityRangedPet;
import com.dsh105.echopet.api.entity.pet.AgeablePet;
import com.dsh105.echopet.api.entity.pet.EquipablePet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.entity.pet.RangedPet;
import org.bukkit.DyeColor;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Villager;
import org.spongepowered.api.entity.living.meta.*;
import org.spongepowered.api.entity.living.villager.Careers;
import org.spongepowered.api.entity.living.villager.Profession;
import org.spongepowered.api.entity.living.villager.Professions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class Attributes {

    private static final SortedMap<Integer, AttributeEnumBridge> ORDINAL_MAP = new TreeMap<>();
    private static final Map<AttributeEnumBridge, Integer> INVERSE_ORDINAL_MAP = new HashMap<>();
    private static final Map<Class<? extends AttributeEnumBridge>, Map<String, AttributeEnumBridge>> PER_CLASS_NAME_MAP = new HashMap<>();
    private static final Map<Class<? extends AttributeEnumBridge>, Map<AttributeEnumBridge, String>> INVERSE_PER_CLASS_NAME_MAP = new HashMap<>();
    private static final Map<Class<? extends AttributeEnumBridge>, Map<Integer, AttributeEnumBridge>> PER_CLASS_ORDINAL_MAP = new HashMap<>();
    private static final Map<Class<? extends AttributeEnumBridge>, Map<AttributeEnumBridge, Integer>> INVERSE_PER_CLASS_ORDINAL_MAP = new HashMap<>();

    private static final Map<Class<? extends Pet>, Traits> TRAITS_MAP = new HashMap<>();
    private static final Map<Class<? extends Pet>, Voice> VOICE_MAP = new HashMap<>();
    private static final Map<Class<? extends EntityPet>, Entity> ENTITY_TRAITS_MAP = new HashMap<>();
    private static final Map<Class<? extends Pet>, List<Class<? extends Pet>>> PET_CLASS_MAP = new HashMap<>();
    private static final Map<Class<? extends EntityPet>, List<Class<? extends EntityPet>>> ENTITY_CLASS_MAP = new HashMap<>();
    private static final Map<Class<? extends Pet>, Class<? extends EntityPet>> PET_TO_ENTITY_CLASS_MAP = new HashMap<>();
    private static final Map<Class<? extends EntityPet>, Class<? extends Pet>> INVERSE_PET_TO_ENTITY_CLASS_MAP = new HashMap<>();

    static {
        // initialise everything
        Class<?>[] nestedClasses = Attributes.class.getDeclaredClasses();
        for (int i = 0; i < nestedClasses.length; i += 100) {
            Class<?> nestedClass = nestedClasses[i];

            if (AttributeEnumBridge.class.isAssignableFrom(nestedClass)) {

                Class<? extends AttributeEnumBridge> entityEnumBridge = (Class<? extends AttributeEnumBridge>) nestedClass;

                Map<String, AttributeEnumBridge> nameMap = new HashMap<>();
                Map<Integer, AttributeEnumBridge> ordinalMap = new HashMap<>();
                Field[] candidateFields = entityEnumBridge.getDeclaredFields();
                for (int j = 0; j < candidateFields.length; j++) {
                    Field field = candidateFields[j];
                    if (AttributeEnumBridge.class.isAssignableFrom(field.getType())) {
                        AttributeEnumBridge enumBridge = (AttributeEnumBridge) Reflection.getFieldValue(field, (Object) null);
                        nameMap.put(field.getName(), enumBridge);
                        ordinalMap.put(j, enumBridge);

                        ORDINAL_MAP.put(i + j, enumBridge);
                    }
                }

                PER_CLASS_NAME_MAP.put(entityEnumBridge, Collections.unmodifiableMap(nameMap));
                PER_CLASS_ORDINAL_MAP.put(entityEnumBridge, Collections.unmodifiableMap(ordinalMap));

                INVERSE_PER_CLASS_NAME_MAP.put(entityEnumBridge, Collections.unmodifiableMap(GeneralUtil.invertMap(nameMap)));
                INVERSE_PER_CLASS_ORDINAL_MAP.put(entityEnumBridge, Collections.unmodifiableMap(GeneralUtil.invertMap(ordinalMap)));
            }
        }

        INVERSE_ORDINAL_MAP.putAll(GeneralUtil.invertMap(ORDINAL_MAP));

        for (PetType petType : PetType.values()) {
            List<Class<? extends Pet>> petClasses = new ArrayList<>();
            List<Class<? extends EntityPet>> entityClasses = new ArrayList<>();

            // Lowest entity class
            Class<? extends EntityPet> lowEntityClass = petType.getEntityClass();
            // Lowest pet class
            Class<? extends Pet> lowPetClass = petType.getPetClass();

            // highest type interface; marked with @Traits
            Class<? extends Pet> annotatedPetClass = lowPetClass;
            // highest entity type interface; marked with @Entity
            Class<? extends EntityPet> annotatedEntityClass = lowEntityClass;

            List<Class<? extends Pet>> ignoredPetClasses = Arrays.asList(Pet.class, AgeablePet.class, EquipablePet.class, RangedPet.class);

            for (Class<?> iface : lowPetClass.getInterfaces()) {
                if (ignoredPetClasses.contains(iface) || !Pet.class.isAssignableFrom(iface)) {
                    continue;
                }
                Class<? extends Pet> petIface = (Class<? extends Pet>) iface;
                if (petIface.getAnnotation(Traits.class) != null) {
                    annotatedPetClass = petIface;
                } else {
                    petClasses.add(petIface);
                }
            }

            List<Class<? extends EntityPet>> ignoredEntityClasses = Arrays.asList(EntityPet.class, EntityAgeablePet.class, EntityEquipablePet.class, EntityRangedPet.class);

            for (Class<?> iface : lowEntityClass.getInterfaces()) {
                if (ignoredEntityClasses.contains(iface) || !EntityPet.class.isAssignableFrom(iface)) {
                    continue;
                }
                Class<? extends EntityPet> entityIface = (Class<? extends EntityPet>) iface;
                if (entityIface.getAnnotation(Entity.class) != null) {
                    annotatedEntityClass = entityIface;
                } else {
                    entityClasses.add(entityIface);
                }
            }

            TRAITS_MAP.put(annotatedPetClass, annotatedEntityClass.getAnnotation(Traits.class));
            // may be null
            VOICE_MAP.put(annotatedPetClass, annotatedEntityClass.getAnnotation(Voice.class));

            ENTITY_TRAITS_MAP.put(annotatedEntityClass, annotatedEntityClass.getAnnotation(Entity.class));

            PET_CLASS_MAP.put(annotatedPetClass, petClasses);
            ENTITY_CLASS_MAP.put(annotatedEntityClass, entityClasses);

            PET_TO_ENTITY_CLASS_MAP.put(annotatedPetClass, annotatedEntityClass);
        }

        INVERSE_PET_TO_ENTITY_CLASS_MAP.putAll(GeneralUtil.invertMap(PET_TO_ENTITY_CLASS_MAP));
    }

    public static <T extends Pet> Class<T> getPetClass(Class<T> petClass) {
        for (Class<? extends Pet> c :PET_CLASS_MAP.keySet()) {
            if (c.equals(petClass) || PET_CLASS_MAP.get(c).contains(petClass)) {
                return (Class<T>) c;
            }
        }
        return null;
    }

    public static <E extends EntityPet> Class<? extends Pet<?, E>> getPetClassOf(Class<E> entityPetClass) {
        return (Class<? extends Pet<?, E>>) INVERSE_PET_TO_ENTITY_CLASS_MAP.get(entityPetClass);
    }

    public static <P extends Pet> Class<? extends EntityPet<P>> getEntityClassOf(Class<P> petClass) {
        return (Class<? extends EntityPet<P>>) PET_TO_ENTITY_CLASS_MAP.get(petClass);
    }

    public static <T extends EntityPet> Class<T> getEntityClass(Class<T> petEntityClass) {
        for (Class<? extends EntityPet> c : ENTITY_CLASS_MAP.keySet()) {
            if (ENTITY_CLASS_MAP.get(c).contains(petEntityClass)) {
                return (Class<T>) c;
            }
        }
        return null;
    }

    public static Voice getVoice(PetType petType) {
        Class<? extends Pet> petClass = getPetClass(petType.getPetClass());
        if (petClass != null) {
            return VOICE_MAP.get(petClass);
        }
        return null;
    }

    public static Traits getTraits(PetType petType) {
        Class<? extends Pet> petClass = getPetClass(petType.getPetClass());
        if (petClass != null) {
            return TRAITS_MAP.get(petClass);
        }
        return null;
    }

    public static String getIdleSound(PetType petType) {
        Voice voice = getVoice(petType);
        return voice != null ? voice.idle() : "default";
    }

    public static String getHurtSound(PetType petType) {
        Voice voice = getVoice(petType);
        return voice != null ? voice.hurt() : ""; // TODO: idle?
    }

    public static String getDeathSound(PetType petType) {
        Voice voice = getVoice(petType);
        return voice != null ? voice.death() : "default";
    }

    public static String getStepSound(PetType petType) {
        Voice voice = getVoice(petType);
        return voice != null ? voice.step() : "";
    }

    public static Class<?> getClassByName(String name) {
        for (Class<?> candidate : Attributes.class.getDeclaredClasses()) {
            if (candidate.getSimpleName().equals(name)) {
                return candidate;
            }
        }
        return null;
    }

    // TODO: write tests to see that these are always in order
    public static List<AttributeEnumBridge> values() {
        List<AttributeEnumBridge> enumBridges = new ArrayList<>();
        Collections.addAll(ORDINAL_MAP.values());
        return Collections.unmodifiableList(enumBridges);
    }

    public static EntityAttribute valueOf(String className, String fieldName) {
        Class<?> enclosingClass = getClassByName(className);
        if (enclosingClass == null) {
            return null;
        }

        if (enclosingClass.isEnum()) {
            return (EntityAttribute) Enum.valueOf((Class<? extends Enum<?>>) enclosingClass, fieldName.toUpperCase().replace(" ", "_"));
        } else if (AttributeEnumBridge.class.isAssignableFrom(enclosingClass)) {
            return valueOf((Class<AttributeEnumBridge>) enclosingClass, fieldName);
        }
        return null;
    }

    public static <E extends AttributeEnumBridge> List<E> values(Class<E> attributeEnumBridgeType) {
        List<E> enumBridges = new ArrayList<>();
        enumBridges.addAll((Collection<? extends E>) PER_CLASS_NAME_MAP.get(attributeEnumBridgeType).values());
        return Collections.unmodifiableList(enumBridges);
    }

    public static <E extends AttributeEnumBridge> Map<String, E> valuesByName(Class<E> attributeEnumBridgeType) {
        Map<String, E> values = (Map<String, E>) PER_CLASS_NAME_MAP.get(attributeEnumBridgeType);
        if (values == null) {
            values = new HashMap<>();
        }
        return Collections.unmodifiableMap(values);
    }

    public static <E extends AttributeEnumBridge> E valueOf(Class<E> attributeEnumBridgeType, String name) {
        return valueOf(attributeEnumBridgeType, name, true);
    }

    public static <E extends AttributeEnumBridge> E valueOf(Class<E> attributeEnumBridgeType, String name, boolean allowTransformation) {
        AttributeEnumBridge enumBridge = valuesByName(attributeEnumBridgeType).get(name);
        return enumBridge != null ? (E) enumBridge : (allowTransformation ? valuesByName(attributeEnumBridgeType).get(name.toUpperCase().replace(" ", "_")) : null);
    }

    public static <E extends AttributeEnumBridge> E valueOf(Class<E> entityEnumBridgeType, int ordinal) {
        return (E) PER_CLASS_ORDINAL_MAP.get(entityEnumBridgeType).get(ordinal);
    }

    public static String nameOf(Class<? extends AttributeEnumBridge> attributeEnumBridgeType, AttributeEnumBridge attributeEnumBridge) {
        return INVERSE_PER_CLASS_NAME_MAP.get(attributeEnumBridgeType).get(attributeEnumBridge);
    }

    public static int ordinalOf(Class<? extends AttributeEnumBridge> attributeEnumBridgeType, AttributeEnumBridge attributeEnumBridge) {
        return INVERSE_PER_CLASS_ORDINAL_MAP.get(attributeEnumBridgeType).get(attributeEnumBridge);
    }

    public static int ordinalOf(AttributeEnumBridge attributeEnumBridge) {
        return INVERSE_ORDINAL_MAP.get(attributeEnumBridge);
    }

    public static AttributeEnumBridge valueOf(int ordinal) {
        return ORDINAL_MAP.get(ordinal);
    }

    /*
     * Attributes and enum bridges
     */

    public static enum Attribute implements EntityAttribute {
        ANGRY, ASLEEP, BABY, CHESTED, FIRE, IGNITION, POWER, ROSE, SADDLE, SCREAMING, SHEARED, SHIELD, TAME, VILLAGER, WITHER;

        @Override
        public AttributeType getType() {
            return AttributeType.SWITCH;
        }
    }

    public static enum SlimeSize implements EntityAttribute {
        SMALL(1), MEDIUM(2), LARGE(4);

        private int size;

        SlimeSize(int size) {
            this.size = size;
        }

        public static SlimeSize getBySize(int size) {
            for (SlimeSize slimeSize : values()) {
                if (slimeSize.getSize() == size) {
                    return slimeSize;
                }
            }
            return null;
        }

        public int getSize() {
            return size;
        }

        @Override
        public AttributeType getType() {
            return AttributeType.SLIME_SIZE;
        }


    }

    public static enum HorseArmour implements EntityAttribute {
        NONE, IRON, GOLD, DIAMOND;

        @Override
        public AttributeType getType() {
            return AttributeType.HORSE_ARMOUR;
        }
    }

    public static class Color extends AttributeEnumBridge<DyeColor, org.spongepowered.api.entity.living.meta.DyeColor> {

        public static final Color
                WHITE = new Color(DyeColor.WHITE, DyeColors.WHITE),
                ORANGE = new Color(DyeColor.ORANGE, DyeColors.ORANGE),
                MAGENTA = new Color(DyeColor.MAGENTA, DyeColors.MAGENTA),
                LIGHT_BLUE = new Color(DyeColor.LIGHT_BLUE, DyeColors.LIGHT_BLUE),
                YELLOW = new Color(DyeColor.YELLOW, DyeColors.YELLOW),
                LIME = new Color(DyeColor.LIME, DyeColors.LIME),
                PINK = new Color(DyeColor.PINK, DyeColors.PINK),
                GRAY = new Color(DyeColor.GRAY, DyeColors.GRAY),
                SILVER = new Color(DyeColor.SILVER, DyeColors.SILVER),
                CYAN = new Color(DyeColor.CYAN, DyeColors.CYAN),
                PURPLE = new Color(DyeColor.PURPLE, DyeColors.PURPLE),
                BLUE = new Color(DyeColor.BLUE, DyeColors.BLUE),
                BROWN = new Color(DyeColor.BROWN, DyeColors.BROWN),
                GREEN = new Color(DyeColor.GREEN, DyeColors.GREEN),
                RED = new Color(DyeColor.RED, DyeColors.RED),
                BLACK = new Color(DyeColor.BLACK, DyeColors.BLACK);

        public Color(DyeColor bukkitEquivalent, org.spongepowered.api.entity.living.meta.DyeColor spongeEquivalent) {
            super(bukkitEquivalent, spongeEquivalent);
        }

        public static Color[] values() {
            return values(Color.class);
        }

        @Override
        public AttributeType getType() {
            return AttributeType.COLOR;
        }

        public static Color valueOf(String name) {
            return valueOf(Color.class, name);
        }

        public static Color valueOf(String name, boolean allowTransformation) {
            return valueOf(Color.class, name, allowTransformation);
        }

        public static Color valueOf(int ordinal) {
            return valueOf(Color.class, ordinal);
        }
    }

    public static class HorseColor extends AttributeEnumBridge<Horse.Color, org.spongepowered.api.entity.living.meta.HorseColor> {

        public static final HorseColor
                WHITE = new HorseColor(Horse.Color.WHITE, HorseColors.WHITE),
                CREAMY = new HorseColor(Horse.Color.CREAMY, HorseColors.CREAMY),
                CHESTNUT = new HorseColor(Horse.Color.CHESTNUT, HorseColors.CHESTNUT),
                BROWN = new HorseColor(Horse.Color.BROWN, HorseColors.BROWN),
                BLACK = new HorseColor(Horse.Color.BLACK, HorseColors.BLACK),
                GRAY = new HorseColor(Horse.Color.GRAY, HorseColors.GRAY),
                DARK_BROWN = new HorseColor(Horse.Color.DARK_BROWN, HorseColors.DARK_BROWN);

        public HorseColor(Horse.Color bukkitEquivalent, org.spongepowered.api.entity.living.meta.HorseColor spongeEquivalent) {
            super(bukkitEquivalent, spongeEquivalent);
        }

        public static HorseColor[] values() {
            return values(HorseColor.class);
        }

        @Override
        public AttributeType getType() {
            return AttributeType.HORSE_COLOUR;
        }

        public static HorseColor valueOf(String name) {
            return valueOf(HorseColor.class, name);
        }

        public static HorseColor valueOf(String name, boolean allowTransformation) {
            return valueOf(HorseColor.class, name, allowTransformation);
        }

        public static HorseColor valueOf(int ordinal) {
            return valueOf(HorseColor.class, ordinal);
        }

    }

    public static class HorseStyle extends AttributeEnumBridge<Horse.Style, org.spongepowered.api.entity.living.meta.HorseStyle> {

        public static final AttributeEnumBridge
                NONE = new HorseStyle(Horse.Style.NONE, HorseStyles.NONE),
                WHITE = new HorseStyle(Horse.Style.WHITE, HorseStyles.WHITE),
                WHITEFIELD = new HorseStyle(Horse.Style.WHITEFIELD, HorseStyles.WHITEFIELD),
                WHITE_DOTS = new HorseStyle(Horse.Style.WHITE_DOTS, HorseStyles.WHITE_DOTS),
                BLACK_DOTS = new HorseStyle(Horse.Style.BLACK_DOTS, HorseStyles.BLACK_DOTS);

        public HorseStyle(Horse.Style bukkitEquivalent, org.spongepowered.api.entity.living.meta.HorseStyle spongeEquivalent) {
            super(bukkitEquivalent, spongeEquivalent);
        }

        public static HorseStyle[] values() {
            return values(HorseStyle.class);
        }

        @Override
        public AttributeType getType() {
            return AttributeType.HORSE_STYLE;
        }

        public static HorseStyle valueOf(String name) {
            return valueOf(HorseStyle.class, name);
        }

        public static HorseStyle valueOf(String name, boolean allowTransformation) {
            return valueOf(HorseStyle.class, name, allowTransformation);
        }

        public static HorseStyle valueOf(int ordinal) {
            return valueOf(HorseStyle.class, ordinal);
        }

    }

    public static class HorseVariant extends AttributeEnumBridge<Horse.Variant, org.spongepowered.api.entity.living.meta.HorseVariant> {

        public static final HorseVariant
                NORMAL = new HorseVariant(Horse.Variant.HORSE, HorseVariants.HORSE),
                DONKEY = new HorseVariant(Horse.Variant.DONKEY, HorseVariants.DONKEY),
                MULE = new HorseVariant(Horse.Variant.MULE, HorseVariants.MULE),
                ZOMBIE = new HorseVariant(Horse.Variant.UNDEAD_HORSE, HorseVariants.UNDEAD_HORSE),
                SKELETON = new HorseVariant(Horse.Variant.SKELETON_HORSE, HorseVariants.SKELETON_HORSE);

        public HorseVariant(Horse.Variant bukkitEquivalent, org.spongepowered.api.entity.living.meta.HorseVariant spongeEquivalent) {
            super(bukkitEquivalent, spongeEquivalent);
        }

        public static HorseVariant[] values() {
            return values(HorseVariant.class);
        }

        @Override
        public AttributeType getType() {
            return AttributeType.HORSE_VARIANT;
        }

    }

    public static class OcelotType extends AttributeEnumBridge<Ocelot.Type, org.spongepowered.api.entity.living.meta.OcelotType> {

        public static final OcelotType
                WILD = new OcelotType(Ocelot.Type.WILD_OCELOT, OcelotTypes.WILD_OCELOT),
                BLACK = new OcelotType(Ocelot.Type.BLACK_CAT, OcelotTypes.BLACK_CAT),
                RED = new OcelotType(Ocelot.Type.RED_CAT, OcelotTypes.RED_CAT),
                SIAMESE = new OcelotType(Ocelot.Type.SIAMESE_CAT, OcelotTypes.SIAMESE_CAT);

        public OcelotType(Ocelot.Type bukkitEquivalent, org.spongepowered.api.entity.living.meta.OcelotType spongeEquivalent) {
            super(bukkitEquivalent, spongeEquivalent);
        }

        public static OcelotType[] values() {
            return values(OcelotType.class);
        }

        @Override
        public AttributeType getType() {
            return AttributeType.OCELOT_TYPE;
        }

        public static OcelotType valueOf(String name) {
            return valueOf(OcelotType.class, name);
        }

        public static OcelotType valueOf(String name, boolean allowTransformation) {
            return valueOf(OcelotType.class, name, allowTransformation);
        }

        public static OcelotType valueOf(int ordinal) {
            return valueOf(OcelotType.class, ordinal);
        }

    }

    public static class SkeletonType extends AttributeEnumBridge<Skeleton.SkeletonType, org.spongepowered.api.entity.living.meta.SkeletonType> {

        public static final SkeletonType
                NORMAL = new SkeletonType(Skeleton.SkeletonType.NORMAL, SkeletonTypes.NORMAL),
                WITHER = new SkeletonType(Skeleton.SkeletonType.WITHER, SkeletonTypes.WITHER);

        public SkeletonType(Skeleton.SkeletonType bukkitEquivalent, org.spongepowered.api.entity.living.meta.SkeletonType spongeEquivalent) {
            super(bukkitEquivalent, spongeEquivalent);
        }

        public static SkeletonType[] values() {
            return values(SkeletonType.class);
        }

        @Override
        public AttributeType getType() {
            return AttributeType.SKELETON_TYPE;
        }

        public static SkeletonType valueOf(String name) {
            return valueOf(SkeletonType.class, name);
        }

        public static SkeletonType valueOf(String name, boolean allowTransformation) {
            return valueOf(SkeletonType.class, name, allowTransformation);
        }

        public static SkeletonType valueOf(int ordinal) {
            return valueOf(SkeletonType.class, ordinal);
        }

    }

    public static class VillagerProfession extends AttributeEnumBridge<Villager.Profession, Profession> {

        public static final VillagerProfession
                FARMER = new VillagerProfession(Villager.Profession.FARMER, Professions.FARMER),
                LIBRARIAN = new VillagerProfession(Villager.Profession.LIBRARIAN, Professions.LIBRARIAN),
                PRIEST = new VillagerProfession(Villager.Profession.PRIEST, Professions.PRIEST),
                BLACKSMITH = new VillagerProfession(Villager.Profession.BLACKSMITH, Professions.BLACKSMITH),
                BUTCHER = new VillagerProfession(Villager.Profession.BUTCHER, Professions.BUTCHER);

        public VillagerProfession(Villager.Profession bukkitEquivalent, Profession spongeEquivalent) {
            super(bukkitEquivalent, spongeEquivalent);
        }

        public static VillagerProfession[] values() {
            return values(VillagerProfession.class);
        }

        @Override
        public AttributeType getType() {
            return AttributeType.PROFESSION;
        }

        public static VillagerProfession valueOf(String name) {
            return valueOf(VillagerProfession.class, name);
        }

        public static VillagerProfession valueOf(String name, boolean allowTransformation) {
            return valueOf(VillagerProfession.class, name, allowTransformation);
        }

        public static VillagerProfession valueOf(int ordinal) {
            return valueOf(VillagerProfession.class, ordinal);
        }
        
    }

    public static class VillagerCareer extends AttributeEnumBridge<Object, Object> {

        public static final VillagerCareer
                FARMER = new VillagerCareer(null, Careers.FARMER),
                FISHERMAN = new VillagerCareer(null, Careers.FISHERMAN),
                SHEPHERD = new VillagerCareer(null, Careers.SHEPHERD),
                FLETCHER = new VillagerCareer(null, Careers.FLETCHER),
                LIBRARIAN = new VillagerCareer(null, Careers.LIBRARIAN),
                CLERIC = new VillagerCareer(null, Careers.CLERIC),
                ARMORER = new VillagerCareer(null, Careers.ARMORER),
                WEAPON_SMITH = new VillagerCareer(null, Careers.WEAPON_SMITH),
                TOOL_SMITH = new VillagerCareer(null, Careers.TOOL_SMITH),
                BUTCHER = new VillagerCareer(null, Careers.BUTCHER),
                LEATHERWORKER = new VillagerCareer(null, Careers.LEATHERWORKER);

        public VillagerCareer(Object bukkitEquivalent, Object spongeEquivalent) {
            super(bukkitEquivalent, spongeEquivalent);
        }

        public static VillagerCareer[] values() {
            return values(VillagerCareer.class);
        }

        @Override
        public AttributeType getType() {
            return AttributeType.CAREER;
        }

        public static VillagerCareer valueOf(String name) {
            return valueOf(VillagerCareer.class, name);
        }

        public static VillagerCareer valueOf(String name, boolean allowTransformation) {
            return valueOf(VillagerCareer.class, name, allowTransformation);
        }

        public static VillagerCareer valueOf(int ordinal) {
            return valueOf(VillagerCareer.class, ordinal);
        }

    }
}