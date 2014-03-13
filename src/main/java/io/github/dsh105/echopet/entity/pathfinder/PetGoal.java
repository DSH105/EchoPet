package io.github.dsh105.echopet.entity.pathfinder;

public abstract class PetGoal {

    public abstract PetGoalType getType();

    public abstract String getDefaultKey();

    public abstract boolean shouldStart(); //a

    public boolean shouldContinue() { //b
        return shouldStart();
    }

    public void start() { //c
    }

    public void finish() { //d
    }

    public boolean isContinuous() {
        return true;
    }

    public abstract void tick(); //e
}
