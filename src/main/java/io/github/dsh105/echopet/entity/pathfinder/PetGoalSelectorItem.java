package io.github.dsh105.echopet.entity.pathfinder;

public class PetGoalSelectorItem {

    private PetGoal petGoal;
    private int priority;

    public PetGoalSelectorItem(int priority, PetGoal goal) {
        this.petGoal = goal;
        this.priority = priority;
    }

    public PetGoal getPetGoal() {
        return petGoal;
    }

    public int getPriority() {
        return priority;
    }
}
