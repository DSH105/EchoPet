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

import java.util.*;

/*
 * From EntityAPI :)
 * Also means I coded it <3
 */

public class PetMind implements Mind {

    private Pet pet;
    private int delay = 0;

    private EntityAIModifier aiModifier;

    private Map<String, BehaviourContainer> goalMap = new HashMap<>();
    private List<BehaviourContainer> goals = new ArrayList<>();
    private List<BehaviourContainer> activeGoals = new ArrayList<>();

    public PetMind(Pet pet) {
        this.pet = pet;
        this.aiModifier = new EntityAIModifier(pet);
    }

    @Override
    public void addGoal(Behaviour behaviour, int priority) {
        this.addGoal(behaviour.getDefaultKey(), behaviour, priority);
    }

    @Override
    public void addGoal(String key, Behaviour behaviour, int priority) {
        BehaviourContainer goalItem = new BehaviourContainer(priority, behaviour);
        if (this.goalMap.containsKey(key)) {
            return;
        }
        this.goalMap.put(key, goalItem);
        this.goals.add(goalItem);
        behaviour.setPet(pet);
    }

    @Override
    public void addAndReplaceGoal(String key, Behaviour behaviour, int priority) {
        if (this.goalMap.containsKey(key)) {
            this.removeGoal(key);
        }
        this.addGoal(key, behaviour, priority);
    }

    @Override
    public void removeGoal(Behaviour behaviour) {
        Iterator<BehaviourContainer> iterator = this.goals.iterator();

        while (iterator.hasNext()) {
            BehaviourContainer goalItem = iterator.next();
            Behaviour behaviour1 = goalItem.getBehaviour();

            if (behaviour1 == behaviour) {
                if (this.activeGoals.contains(goalItem)) {
                    behaviour1.finish();
                    this.activeGoals.remove(goalItem);
                }

                iterator.remove();
                behaviour1.setPet(null);
            }
        }
    }

    @Override
    public void removeGoal(String key) {
        Iterator<Map.Entry<String, BehaviourContainer>> iterator = this.goalMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, BehaviourContainer> entry = iterator.next();
            BehaviourContainer goalItem = entry.getValue();
            Behaviour behaviour1 = goalItem.getBehaviour();

            if (key.equals(entry.getKey())) {
                if (this.activeGoals.contains(goalItem)) {
                    behaviour1.finish();
                    this.activeGoals.remove(goalItem);
                }
                if (this.goals.contains(goalItem)) {
                    this.goals.remove(goalItem);
                }

                iterator.remove();
                behaviour1.setPet(null);
            }
        }
    }

    @Override
    public void clearGoals(String key) {
        this.goalMap.clear();
        this.goals.clear();

        Iterator<BehaviourContainer> iterator = this.activeGoals.iterator();

        while (iterator.hasNext()) {
            BehaviourContainer goalItem = iterator.next();
            goalItem.getBehaviour().finish();
            goalItem.getBehaviour().setPet(null);

        }
        this.activeGoals.clear();
    }

    @Override
    public Behaviour getGoal(String key) {
        Iterator<Map.Entry<String, BehaviourContainer>> iterator = this.goalMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, BehaviourContainer> entry = iterator.next();
            BehaviourContainer goalItem = entry.getValue();
            Behaviour behaviour = goalItem.getBehaviour();

            if (key.equals(entry.getKey())) {
                return behaviour;
            }
        }
        return null;
    }

    @Override
    public void updateGoals() {
        this.aiModifier.update();

        Iterator<BehaviourContainer> iterator;
        if (this.delay++ % 3 == 0) {

            iterator = this.goals.iterator();

            while (iterator.hasNext()) {
                BehaviourContainer goalItem = iterator.next();
                if (this.activeGoals.contains(goalItem)) {
                    if (this.canUse(goalItem) && goalItem.getBehaviour().shouldContinue()) {
                        continue;
                    }
                    goalItem.getBehaviour().finish();
                    this.activeGoals.remove(goalItem);
                } else {
                    if (this.canUse(goalItem) && goalItem.getBehaviour().shouldStart()) {
                        goalItem.getBehaviour().start();
                        this.activeGoals.add(goalItem);
                    }
                }

            }

            this.delay = 0;
        } else {
            iterator = this.activeGoals.iterator();

            while (iterator.hasNext()) {
                BehaviourContainer goalItem = iterator.next();
                if (!goalItem.getBehaviour().shouldContinue()) {
                    goalItem.getBehaviour().finish();
                    iterator.remove();
                }
            }
        }

        iterator = this.activeGoals.iterator();

        while (iterator.hasNext()) {
            BehaviourContainer goalItem = iterator.next();
            goalItem.getBehaviour().tick();
        }
    }

    private boolean canUse(BehaviourContainer goalItem) {
        Iterator<BehaviourContainer> iterator = this.goals.iterator();

        while (iterator.hasNext()) {
            BehaviourContainer goalItem1 = iterator.next();
            if (goalItem1 != goalItem) {
                if (goalItem.getPriority() > goalItem1.getPriority()) {
                    if (!this.areCompatible(goalItem, goalItem1) && this.activeGoals.contains(goalItem1)) {
                        return false;
                    }
                    //goal.i() -> isContinuous
                } else if (!goalItem1.getBehaviour().isContinuous() && this.activeGoals.contains(goalItem1)) {
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean areCompatible(BehaviourContainer goalItem, BehaviourContainer goalItem1) {
        return goalItem.getBehaviour().getType().isCompatibleWith(goalItem1.getBehaviour().getType());
    }
}