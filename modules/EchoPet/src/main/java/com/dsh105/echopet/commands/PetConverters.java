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

package com.dsh105.echopet.commands;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.api.config.Lang;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.influx.conversion.ConversionException;
import com.dsh105.influx.conversion.Converter;
import com.dsh105.influx.conversion.UnboundConverter;
import com.dsh105.influx.dispatch.CommandContext;
import com.dsh105.influx.syntax.ContextualVariable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PetConverters {

    private static Player getTarget(ContextualVariable variable) throws ConversionException {
        return getTarget(variable.getContext());
    }

    private static Player getTarget(CommandContext context) throws ConversionException {
        Player target = null;
        String targetName = context.var("target");
        if (targetName != null) {
            target = Bukkit.getPlayer(targetName);
        }

        if (target == null) {
            if (!(context.sender() instanceof Player)) {
                throw new ConversionException("Target player not specified!");
            }
            target = (Player) context.sender();
        }
        return target;
    }

    public class Create extends Converter<Pet> {

        public Create() {
            super(Pet.class, 1, 3);
        }

        @Override
        public Pet convert(ContextualVariable variable) throws ConversionException {
            Player target = getTarget(variable);
            PetType petType;
            try {
                petType = PetType.valueOf(variable.getConsumedArguments()[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ConversionException(Lang.INVALID_PET_TYPE.getValue("type", variable.getConsumedArguments()[0]));
            }

            ArrayList<String> invalidData = new ArrayList<>();
            ArrayList<PetData> validData = new ArrayList<>();
            String name = null;

            for (int i = 1; i < 3; i++) {
                String value = variable.getConsumedArguments()[i];

                if (value.toLowerCase().startsWith("name:")) {
                    name = value.substring(5, value.length());
                } else if (value.toLowerCase().startsWith("data:")) {
                    value = value.substring(5, value.length());
                    for (String candidate : value.split(",")) {
                        if (candidate.isEmpty()) {
                            continue;
                        }

                        if (GeneralUtil.isEnumType(PetData.class, candidate)) {
                            invalidData.add(candidate);
                            continue;
                        }
                        validData.add(PetData.valueOf(candidate));
                    }
                }
            }

            Pet pet = EchoPet.getManager().create(target, petType, true);
            if (pet == null) {
                return null;
            }

            if (!invalidData.isEmpty()) {
                variable.getContext().respond(Lang.INVALID_PET_DATA.getValue("data", StringUtil.combine("{c1}, {c2}", invalidData)));
            }
            if (!validData.isEmpty()) {
                pet.setDataValue(validData.toArray(new PetData[0]));
            }
            if (name != null && !name.isEmpty()) {
                pet.setName(name);
            }
            return pet;
        }
    }

    public static class CreateType extends Converter<Pet> {

        public CreateType() {
            super(Pet.class);
        }

        @Override
        public Pet convert(ContextualVariable variable) throws ConversionException {
            System.out.println("Something is happening...");
            Player target = getTarget(variable);
            PetType petType;
            try {
                System.out.println("Creating " + variable.getConsumedArguments()[0]);
                petType = PetType.valueOf(variable.getConsumedArguments()[0].toUpperCase());
                System.out.println("Yup!");
            } catch (IllegalArgumentException e) {
                throw new ConversionException(Lang.INVALID_PET_TYPE.getValue("type", variable.getConsumedArguments()[0]));
            }

            return EchoPet.getManager().create(target, petType, true);
        }
    }

    public class FindPet extends Converter<Pet> {

        public FindPet() {
            super(Pet.class);
        }

        @Override
        public Pet convert(ContextualVariable variable) throws ConversionException {
            if (variable.getConsumedValue().isEmpty()) {
                Pet pet = new OnlyPet().convert(variable.getContext());
                if (pet == null) {
                    variable.getContext().respond(Lang.MORE_PETS_FOUND.getValue("command", variable.getContext().getInput().replaceAll("^pet(admin)*", variable.getContext().getArguments()[0] + " <pet_name>")));
                }
                return pet;
            }
            return new ByName().convert(variable);
        }
    }

    public class ByName extends Converter<Pet> {

        public ByName() {
            super(Pet.class);
        }

        @Override
        public Pet convert(ContextualVariable variable) throws ConversionException {
            Player target = getTarget(variable);
            Pet pet = EchoPet.getManager().getPetByName(target, variable.getConsumedValue());
            if (pet == null) {
                throw new ConversionException(Lang.PET_NOT_FOUND.getValue("name", variable.getConsumedValue()));
            }
            return pet;
        }
    }

    public class OnlyPet extends UnboundConverter<Pet> {

        public OnlyPet() {
            super(Pet.class);
        }

        @Override
        public Pet convert(CommandContext<?> context) throws ConversionException {
            Player target = getTarget(context);
            List<Pet> pets = EchoPet.getManager().getPetsFor(target);
            if (pets.size() <= 0) {
                throw new ConversionException(Lang.NO_PETS_FOUND.getValue());
            } else if (pets.size() > 1) {
                return null;
            }

            return pets.get(0);
        }
    }
}