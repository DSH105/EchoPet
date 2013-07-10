package com.github.dsh105.echopet.entity.pathfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

public class PetGoalSelector {
	
	private Map<String, PetGoalSelectorItem> a = new HashMap<String, PetGoalSelectorItem>();
	private ArrayList<PetGoalSelectorItem> b = new ArrayList<PetGoalSelectorItem>();
	private ArrayList<PetGoalSelectorItem> c = new ArrayList<PetGoalSelectorItem>();
	
	public void addGoal(int i, String s, PetGoal goal) {
		PetGoalSelectorItem goalItem = new PetGoalSelectorItem(this, i, goal);
		if (a.containsKey(goalItem)) {
			return;
		}
		a.put(s, goalItem);
		b.add(i, goalItem);
	}
	
	public void addGoal(String s, PetGoal goal) {
		PetGoalSelectorItem goalItem = new PetGoalSelectorItem(this, goal);
		if (a.containsKey(goalItem)) {
			return;
		}
		a.put(s, goalItem);
		b.add(goalItem);
	}
	
	public void removeGoal(String s) {
		if (a.containsKey(s)) {
			PetGoalSelectorItem goalItem = a.get(s);
			PetGoal goal = goalItem.a;
			b.remove(goalItem);
			a.remove(goalItem);
			if (c.contains(goalItem)) {
				goal.d();
			}
			c.remove(goalItem);
		}
	}
	
	public PetGoal getGoal(String s) {
		return a.get(s).a;
	}
	
	public void removeGoals() {
		a.clear();
		b.clear();
		for (PetGoalSelectorItem goalItem : c) {
			PetGoal goal = goalItem.a;
			goal.d();
		}
		c.clear();
	}
	
	public void run() {
		ListIterator<PetGoalSelectorItem> i = b.listIterator();
		while (i.hasNext()) {
			PetGoalSelectorItem goalItem = i.next();
			PetGoal goal = goalItem.a;
			if (!c.contains(goalItem)) {
				if (goal.a()) {
					c.add(goalItem);
				}
			}
		}
		
		ListIterator<PetGoalSelectorItem> i2 = c.listIterator();
		while (i2.hasNext()) {
			PetGoalSelectorItem goalItem = i2.next();
			PetGoal goal = goalItem.a;
			if (goal.b()) {
				goal.d();
				i2.remove();
			}
		}
		
		ListIterator<PetGoalSelectorItem> i3 = c.listIterator();
		while (i3.hasNext()) {
			PetGoalSelectorItem goalItem = i3.next();
			PetGoal goal = goalItem.a;
			goal.e();
		}
	}
}