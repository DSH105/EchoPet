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

import com.captainbern.minecraft.reflection.MinecraftReflection;
import com.captainbern.reflection.ClassTemplate;
import com.captainbern.reflection.Reflection;
import com.captainbern.reflection.SafeField;
import com.captainbern.reflection.accessor.FieldAccessor;
import com.captainbern.reflection.matcher.Matchers;
import com.dsh105.echopet.api.entity.pet.Pet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityAIModifier {

    private static final List<FieldAccessor<?>> GOAL_SELECTORS;
    private static final List<FieldAccessor<List>> GOAL_LISTS;

    static {
        List<FieldAccessor<?>> selectors = new ArrayList<>();
        List<FieldAccessor<List>> goalLists = new ArrayList<>();

        ClassTemplate<?> pathfinderGoalSelector = new Reflection().reflect(MinecraftReflection.getMinecraftClass("PathfinderGoalSelector"));
        ClassTemplate<?> entityInsentient = new Reflection().reflect(MinecraftReflection.getMinecraftClass("EntityInsentient"));

        List<SafeField<?>> goalSelectors = entityInsentient.getSafeFields(Matchers.withExactType(pathfinderGoalSelector.getReflectedClass()));
        for (SafeField<?> goalSelector : goalSelectors) {
            selectors.add(goalSelector.getAccessor());
        }

        List<SafeField<?>> goalListFields = pathfinderGoalSelector.getSafeFields(Matchers.withExactType(List.class));
        for (SafeField<?> goalListField : goalListFields) {
            goalLists.add(((SafeField<List>) goalListField).getAccessor());
        }

        GOAL_SELECTORS = Collections.unmodifiableList(selectors);
        GOAL_LISTS = Collections.unmodifiableList(goalLists);
    }

    private List<List> goalLists = new ArrayList<>();

    public EntityAIModifier(Pet pet) {
        for (FieldAccessor<?> goalSelector : GOAL_SELECTORS) {
            for (FieldAccessor<List> goalList : GOAL_LISTS) {
                goalLists.add(goalList.get(goalSelector.get(pet.getEntity())));
            }
        }
    }

    public void update() {
        for (List goalList : goalLists) {
            if (!goalList.isEmpty()) {
                goalList.clear();
            }
        }
    }
}