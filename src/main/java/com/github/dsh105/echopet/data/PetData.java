package com.github.dsh105.echopet.data;

import java.util.ArrayList;

public enum PetData {
	
	/*
	 * Enumeration of valid pet data values
	 */
	
	NORMAL("normal", Type.HORSE_TYPE),
	DONKEY("donkey", Type.HORSE_TYPE),
	MULE("mule", Type.HORSE_TYPE),
	SKELETON("skeleton", Type.HORSE_TYPE),
	ZOMBIE("zombie", Type.HORSE_TYPE),
	CHESTED("chested", Type.BOOLEAN),
	
	//WHITE,
	CREAMY("creamy", Type.HORSE_VARIANT),
	CHESTNUT("chestnut", Type.HORSE_VARIANT),
	//BROWN,
	//BLACK,
	//GRAY,
	DARKBROWN("darkbrown", Type.HORSE_VARIANT),
	
	NOARMOUR("noarmour", Type.HORSE_ARMOUR),
	IRON("iron", Type.HORSE_ARMOUR),
	GOLD("gold", Type.HORSE_ARMOUR),
	DIAMOND("diamond", Type.HORSE_ARMOUR),
	
	NONE("noMarking", Type.HORSE_MARKING),
	SOCKS("whiteSocks", Type.HORSE_MARKING),
	WHITEPATCH("whitePatch", Type.HORSE_MARKING),
	WHITESPOT("whiteSpot", Type.HORSE_MARKING),
	BLACKSPOT("blackSpot", Type.HORSE_MARKING),
	
	ANGRY("angry", Type.BOOLEAN),
	BABY("baby", Type.BOOLEAN),
	FIRE("fire", Type.BOOLEAN),
	LARGE("large", Type.SIZE),
	MEDIUM("medium", Type.SIZE),
	POWER("powered", Type.BOOLEAN),
	SADDLE("saddle", Type.BOOLEAN),
	SCREAMING("screaming", Type.BOOLEAN),
	SHEARED("sheared", Type.BOOLEAN),
	SHIELD("shield", Type.BOOLEAN),
	SMALL("small", Type.SIZE),
	TAMED("tamed", Type.BOOLEAN),
	WITHER("wither", Type.BOOLEAN),
	VILLAGER("villager", Type.BOOLEAN),
	
	BLACK("black", Type.COLOR, Type.CAT, Type.HORSE_VARIANT),
	BLUE("blue", Type.COLOR),
	BROWN("brown", Type.COLOR, Type.HORSE_VARIANT),
	CYAN("cyan", Type.COLOR),
	GRAY("gray", Type.COLOR, Type.HORSE_VARIANT),
	GREEN("green", Type.COLOR),
	LIGHTBLUE("lightBlue", Type.COLOR),
	LIME("lime", Type.COLOR),
	MAGENTA("magenta", Type.COLOR),
	ORANGE("orange", Type.COLOR),
	PINK("pink", Type.COLOR),
	PURPLE("purple", Type.COLOR),
	RED("red", Type.CAT, Type.COLOR),
	SILVER("silver", Type.COLOR),
	WHITE("white", Type.COLOR, Type.HORSE_VARIANT),
	YELLOW("yellow", Type.COLOR),

	//BLACK
	SIAMESE("siamese",  Type.CAT),
	WILD("wild", Type.CAT),
	
	BLACKSMITH("blacksmith", Type.PROF),
	BUTCHER("butcher", Type.PROF),
	FARMER("farmer", Type.PROF),
	LIBRARIAN("librarian", Type.PROF),
	PRIEST("priest", Type.PROF);
	
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