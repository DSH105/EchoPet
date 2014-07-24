/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.api.entity.ai;

import com.dsh105.echopet.api.entity.pet.Pet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * From EntityAPI :)
 * Also means I coded it <3
 */

public class SimplePetGoalSelector implements PetGoalSelector {

    private Pet pet;
    private int delay = 0;

    private Map<String, PetGoalSelectorItem> goalMap = new HashMap<>();
    private ArrayList<PetGoalSelectorItem> goals = new ArrayList<>();
    private ArrayList<PetGoalSelectorItem> activeGoals = new ArrayList<>();

    public SimplePetGoalSelector(Pet pet) {
        this.pet = pet;
    }

    @Override
    public void addGoal(PetGoal petGoal, int priority) {
        this.addGoal(petGoal.getDefaultKey(), petGoal, priority);
    }

    @Override
    public void addGoal(String key, PetGoal petGoal, int priority) {
        PetGoalSelectorItem goalItem = new PetGoalSelectorItem(priority, petGoal);
        if (this.goalMap.containsKey(key)) {
            return;
        }
        this.goalMap.put(key, goalItem);
        this.goals.add(goalItem);
        petGoal.setPet(pet);
    }

    @Override
    public void addAndReplaceGoal(String key, PetGoal petGoal, int priority) {
        if (this.goalMap.containsKey(key)) {
            this.removeGoal(key);
        }
        this.addGoal(key, petGoal, priority);
    }

    @Override
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
                petGoal1.setPet(null);
            }
        }
    }

    @Override
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
                petGoal1.setPet(null);
            }
        }
    }

    @Override
    public void clearGoals(String key) {
        this.goalMap.clear();
        this.goals.clear();

        Iterator<PetGoalSelectorItem> iterator = this.activeGoals.iterator();

        while (iterator.hasNext()) {
            PetGoalSelectorItem goalItem = iterator.next();
            goalItem.getPetGoal().finish();
            goalItem.getPetGoal().setPet(null);

        }
        this.activeGoals.clear();
    }

    @Override
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

    @Override
    public void updateGoals() {
        Iterator<PetGoalSelectorItem> iterator;
        if (this.delay++ % 3 == 0) {

            iterator = this.goals.iterator();

            while (iterator.hasNext()) {
                PetGoalSelectorItem goalItem = iterator.next();
                if (this.activeGoals.contains(goalItem)) {
                    if (this.canUse(goalItem) && goalItem.getPetGoal().shouldContinue()) {
                        continue;
                    }
                    goalItem.getPetGoal().finish();
                    this.activeGoals.remove(goalItem);
                } else {
                    if (this.canUse(goalItem) && goalItem.getPetGoal().shouldStart()) {
                        goalItem.getPetGoal().start();
                        this.activeGoals.add(goalItem);
                    }
                }

            }

            this.delay = 0;
        } else {
            iterator = this.activeGoals.iterator();

            while (iterator.hasNext()) {
                PetGoalSelectorItem goalItem = iterator.next();
                if (!goalItem.getPetGoal().shouldContinue()) {
                    goalItem.getPetGoal().finish();
                    iterator.remove();
                }
            }
        }

        iterator = this.activeGoals.iterator();

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