package io.github.dsh105.echopet.entity.pathfinder;

import io.github.dsh105.echopet.entity.Pet;

import java.util.*;

/*
 * From EntityAPI :)
 * Also means I coded it <3
 */

public class PetGoalSelector {

    private Map<String, PetGoalSelectorItem> goalMap = new HashMap<String, PetGoalSelectorItem>();
    private ArrayList<PetGoalSelectorItem> goals = new ArrayList<PetGoalSelectorItem>();
    private ArrayList<PetGoalSelectorItem> activeGoals = new ArrayList<PetGoalSelectorItem>();
    private Pet pet;
    private int delay = 0;

    public PetGoalSelector(Pet pet) {
        this.pet = pet;
    }

    public void addGoal(PetGoal petGoal, int priority) {
        this.addGoal(petGoal.getDefaultKey(), petGoal, priority);
    }

    public void addGoal(String key, PetGoal petGoal, int priority) {
        PetGoalSelectorItem goalItem = new PetGoalSelectorItem(priority, petGoal);
        if (this.goalMap.containsKey(key)) {
            return;
        }
        this.goalMap.put(key, goalItem);
        this.goals.add(goalItem);
    }

    public void addAndReplaceGoal(String key, PetGoal petGoal, int priority) {
        if (this.goalMap.containsKey(key)) {
            this.removeGoal(key);
        }
        this.addGoal(key, petGoal, priority);
    }

    public void removeGoal(PetGoal petGoal) {
        Iterator<PetGoalSelectorItem> iterator = this.goals.iterator();

        while (iterator.hasNext()) {
            PetGoalSelectorItem goalItem = iterator.next();
            PetGoal petGoal1 = goalItem.getPetGoal();

            if (petGoal1 == petGoal) {
                if (this.activeGoals.contains(goalItem)) {
                    petGoal1.finish();
                    this.activeGoals.remove(goalItem);
                }

                iterator.remove();
            }
        }
    }

    public void removeGoal(String key) {
        Iterator<Map.Entry<String, PetGoalSelectorItem>> iterator = this.goalMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, PetGoalSelectorItem> entry = iterator.next();
            PetGoalSelectorItem goalItem = entry.getValue();
            PetGoal petGoal1 = goalItem.getPetGoal();

            if (key.equals(entry.getKey())) {
                if (this.activeGoals.contains(goalItem)) {
                    petGoal1.finish();
                    this.activeGoals.remove(goalItem);
                }
                if (this.goals.contains(goalItem)) {
                    this.goals.remove(goalItem);
                }

                iterator.remove();
            }
        }
    }

    public void clearGoals(String key) {
        this.goalMap.clear();
        this.goals.clear();

        Iterator<PetGoalSelectorItem> iterator = this.activeGoals.iterator();

        while (iterator.hasNext()) {
            iterator.next().getPetGoal().finish();
        }
        this.activeGoals.clear();
    }

    public PetGoal getGoal(String key) {
        Iterator<Map.Entry<String, PetGoalSelectorItem>> iterator = this.goalMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, PetGoalSelectorItem> entry = iterator.next();
            PetGoalSelectorItem goalItem = entry.getValue();
            PetGoal petGoal = goalItem.getPetGoal();

            if (key.equals(entry.getKey())) {
                return petGoal;
            }
        }
        return null;
    }

    public void updateGoals() {
        if (this.delay++ % 3 == 0) {
            Iterator<PetGoalSelectorItem> iterator = this.goals.iterator();

            while (iterator.hasNext()) {
                PetGoalSelectorItem goalItem = iterator.next();
                if (this.activeGoals.contains(goalItem)) {
                    if (this.canUse(goalItem) && goalItem.getPetGoal().shouldContinue()) {
                        continue;
                    }
                    goalItem.getPetGoal().finish();
                    this.activeGoals.remove(goalItem);
                }

            }

            this.delay = 0;
        } else {
            Iterator<PetGoalSelectorItem> iterator = this.activeGoals.iterator();

            while (iterator.hasNext()) {
                PetGoalSelectorItem goalItem = iterator.next();
                if (!goalItem.getPetGoal().shouldContinue()) {
                    goalItem.getPetGoal().finish();
                    iterator.remove();
                }
            }
        }

        Iterator<PetGoalSelectorItem> iterator = this.activeGoals.iterator();

        while (iterator.hasNext()) {
            PetGoalSelectorItem goalItem = iterator.next();
            goalItem.getPetGoal().tick();
        }
    }

    private boolean canUse(PetGoalSelectorItem goalItem) {
        Iterator<PetGoalSelectorItem> iterator = this.goals.iterator();

        while (iterator.hasNext()) {
            PetGoalSelectorItem goalItem1 = iterator.next();
            if (goalItem1 != goalItem) {
                if (goalItem.getPriority() > goalItem1.getPriority()) {
                    if (!this.areCompatible(goalItem, goalItem1) && this.activeGoals.contains(goalItem1)) {
                        return false;
                    }
                    //goal.i() -> isContinuous
                } else if (!goalItem1.getPetGoal().isContinuous() && this.activeGoals.contains(goalItem1)) {
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean areCompatible(PetGoalSelectorItem goalItem, PetGoalSelectorItem goalItem1) {
        return goalItem.getPetGoal().getType().isCompatibleWith(goalItem1.getPetGoal().getType());
    }
}