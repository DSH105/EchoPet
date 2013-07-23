package com.github.dsh105.echopet.data;

import java.util.ArrayList;

public enum PetData {
	
	/*
	 * Enumeration of valid pet data values
	 */

	ANGRY("angry", Type.BOOLEAN),
	BABY("baby", Type.BOOLEAN),
	BLACK("black", Type.COLOR, Type.CAT, Type.HORSE_VARIANT),
	BLACKSMITH("blacksmith", Type.PROF),
	BLACKSPOT("blackSpot", Type.HORSE_MARKING),
	BLUE("blue", Type.COLOR),
	BROWN("brown", Type.COLOR, Type.HORSE_VARIANT),
	BUTCHER("butcher", Type.PROF),
	CHESTED("chested", Type.BOOLEAN),
	CHESTNUT("chestnut", Type.HORSE_VARIANT),
	CREAMY("creamy", Type.HORSE_VARIANT),
	CYAN("cyan", Type.COLOR),
	DARKBROWN("darkbrown", Type.HORSE_VARIANT),
	DIAMOND("diamond", Type.HORSE_ARMOUR),
	DONKEY("donkey", Type.HORSE_TYPE),
	FARMER("farmer", Type.PROF),
	FIRE("fire", Type.BOOLEAN),
	GRAY("gray", Type.COLOR, Type.HORSE_VARIANT),
	GREEN("green", Type.COLOR),
	GOLD("gold", Type.HORSE_ARMOUR),
	IRON("iron", Type.HORSE_ARMOUR),
	LARGE("large", Type.SIZE),
	LIBRARIAN("librarian", Type.PROF),
	LIGHTBLUE("lightBlue", Type.COLOR),
	LIME("lime", Type.COLOR),
	MAGENTA("magenta", Type.COLOR),
	MEDIUM("medium", Type.SIZE),
	MULE("mule", Type.HORSE_TYPE),
	NOARMOUR("noarmour", Type.HORSE_ARMOUR),
	NONE("noMarking", Type.HORSE_MARKING),
	NORMAL("normal", Type.HORSE_TYPE),
	ORANGE("orange", Type.COLOR),
	PINK("pink", Type.COLOR),
	POWER("powered", Type.BOOLEAN),
	PRIEST("priest", Type.PROF),
	PURPLE("purple", Type.COLOR),
	RED("red", Type.CAT, Type.COLOR),
	SADDLE("saddle", Type.BOOLEAN),
	SCREAMING("screaming", Type.BOOLEAN),
	SHEARED("sheared", Type.BOOLEAN),
	SHIELD("shield", Type.BOOLEAN),
	SIAMESE("siamese",  Type.CAT),
	SILVER("silver", Type.COLOR),
	SKELETON("skeleton", Type.HORSE_TYPE),
	SMALL("small", Type.SIZE),
	SOCKS("whiteSocks", Type.HORSE_MARKING),
	TAMED("tamed", Type.BOOLEAN),
	VILLAGER("villager", Type.BOOLEAN),
	WHITEPATCH("whitePatch", Type.HORSE_MARKING),
	WHITESPOT("whiteSpot", Type.HORSE_MARKING),
	WHITE("white", Type.COLOR, Type.HORSE_VARIANT),
	WILD("wild", Type.CAT),
	WITHER("wither", Type.BOOLEAN),
	YELLOW("yellow", Type.COLOR),
	ZOMBIE("zombie", Type.HORSE_TYPE);



	
	private String configOptionString;
	private ArrayList<Type> t = new ArrayList<Type>();
	PetData(String configOptionString, Type... t) {
		this.configOptionString = configOptionString;
		for (Type ty : t) {
			this.t.add(ty);
		}
	}
	
	public String getConfigOptionString() {
		return this.configOptionString;
	}
	
	public ArrayList<Type> getTypes() {
		return this.t;
	}
	
	public boolean isType(Type t) {
		return this.t.contains(t);
	}
	
	public enum Type {
		BOOLEAN, OTHER, COLOR, CAT, SIZE, PROF, HORSE_TYPE, HORSE_VARIANT, HORSE_MARKING, HORSE_ARMOUR;
	}
}