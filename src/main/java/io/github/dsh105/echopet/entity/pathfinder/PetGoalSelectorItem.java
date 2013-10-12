package com.github.dsh105.echopet.entity.pathfinder;

public class PetGoalSelectorItem {
	
	public PetGoal a;
	public int b;
	final PetGoalSelector c;
	
	public PetGoalSelectorItem(PetGoalSelector goalSelector, int i, PetGoal goal) {
		this.a = goal;
		this.b = i;
		this.c  = goalSelector;
	}
	
	public PetGoalSelectorItem(PetGoalSelector goalSelector, PetGoal goal) {
		this.a = goal;
		this.c  = goalSelector;
	}
	
}
