package io.github.dsh105.echopet.entity.pathfinder;

/*
 * From EntityAPI :)
 */

public enum PetGoalType {

    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN;

    // NMS Goals have stored integers to check compatibility -> goal.j()
    // This enum is used to compare these goals easily and more friendly

    ;

    public boolean isCompatibleWith(PetGoalType type) {
        return (this.ordinal() & type.ordinal()) == 0;
    }
}