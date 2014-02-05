package io.github.dsh105.echopet.entity.pathfinder;

public class PetGoalSelectorItem {

    public PetGoal petGoal;
    public int priority;
    final PetGoalSelector goalSelector;

    public PetGoalSelectorItem(PetGoalSelector goalSelector, int priority, PetGoal goal) {
        this.petGoal = goal;
        this.priority = priority;
        this.goalSelector = goalSelector;
    }

    public PetGoalSelectorItem(PetGoalSelector goalSelector, PetGoal goal) {
        this.petGoal = goal;
        this.goalSelector = goalSelector;
    }

}
