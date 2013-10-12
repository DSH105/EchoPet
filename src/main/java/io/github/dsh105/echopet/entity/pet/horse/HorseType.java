package com.github.dsh105.echopet.entity.pet.horse;


public enum HorseType {
	
	NORMAL(0),
	DONKEY(1),
	MULE(2),
	ZOMBIE(3),
	SKELETON(4);
	
	private int id;
	HorseType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
}
