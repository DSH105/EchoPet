package com.github.dsh105.echopet.entity.pathfinder;

public abstract class PetGoal {
	
	public abstract boolean a(); // shouldStart
	
	public boolean b() {return !a();} //shouldFinish
	
	public void c() {} //start
	
	public void d() {} //finish
	
	public void e() {} //tick over/run
}
