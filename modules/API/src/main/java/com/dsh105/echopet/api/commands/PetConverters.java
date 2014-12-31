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

package com.dsh105.echopet.api.commands;

import com.dsh105.commodus.Affirm;
import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.api.configuration.Lang;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.attribute.EntityAttribute;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.bridge.Ident;
import com.dsh105.influx.conversion.ConversionException;
import com.dsh105.influx.conversion.Converter;
import com.dsh105.influx.conversion.UnboundConverter;
import com.dsh105.influx.dispatch.CommandContext;
import com.dsh105.influx.syntax.ContextualVariable;

import java.util.*;

public class PetConverters {

    private static final Map<UUID, UUID> PLAYER_TO_PET_ID = new HashMap<>();

    public static void selectPet(UUID ownerUID, UUID petUniqueId) {
        Affirm.notNull(ownerUID, "Pet owner ID must not be null.");
        if (petUniqueId == null) {
            PLAYER_TO_PET_ID.remove(ownerUID);
        }
        PLAYER_TO_PET_ID.put(ownerUID, petUniqueId);
    }

    private static Object getTarget(ContextualVariable variable) throws ConversionException {
        return getTarget(variable.getContext());
    }

    private static Object getTarget(CommandContext context) throws ConversionException {
        Object target = null;
        String targetName = context.var("target");
        if (targetName != null) {
            target = Ident.get().getByName(targetName, false);
        }

        if (target == null) {
            if (!Ident.get().isPlayer(context.sender())) {
                throw new ConversionException("Target player not specified!");
            }
            target = context.sender();
        }
        return target;
    }
    
    public static class Selected extends UnboundConverter<Pet> {

        public Selected() {
            super(Pet.class);
        }

        @Override
        public Pet convert(CommandContext<?> context) throws ConversionException {
            Object sender = context.sender();
            UUID senderUID = Ident.get().getUID(sender);
            UUID petUniqueId = PLAYER_TO_PET_ID.get(senderUID);
            Pet pet = null;
            if (petUniqueId != null) {
                pet = EchoPet.getManager().getPetById(petUniqueId);
            }

            if (pet == null) {
                pet = new OnlyPet().convert(context);
                if (pet == null) {
                    context.respond(Lang.NO_SELECTED_PET.getValue());
                }
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
            Object target = getTarget(variable);
            UUID targetUID = Ident.get().getUID(target);
            PetType petType;
            try {
                petType = PetType.valueOf(variable.getConsumedArguments()[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ConversionException(Lang.INVALID_PET_TYPE.getValue("type", variable.getConsumedArguments()[0]));
            }

            return EchoPet.getManager().create(targetUID, petType, true);
        }
    }

    public static class OnlyPet extends UnboundConverter<Pet> {

        public OnlyPet() {
            super(Pet.class);
        }

        @Override
        public Pet convert(CommandContext<?> context) throws ConversionException {
            Object target = getTarget(context);
            UUID targetUID = Ident.get().getUID(target);
            List<Pet> pets = EchoPet.getManager().getPetsFor(targetUID);
            if (pets.size() <= 0) {
                throw new ConversionException(Lang.NO_PETS_FOUND.getValue());
            } else if (pets.size() > 1) {
                return null;
            }

            return pets.get(0);
        }
    }
}