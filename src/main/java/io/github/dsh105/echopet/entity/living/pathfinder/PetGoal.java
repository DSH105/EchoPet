package io.github.dsh105.echopet.entity.living.pathfinder;

public abstract class PetGoal {

    public abstract boolean shouldStart();

    public boolean shouldFinish() {
        return !shouldStart();
    }

    public void start() {
    }

    public void finish() {
    }

    public void tick() {
    }
}
