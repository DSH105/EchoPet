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

package com.dsh105.echopetv3.api.plugin;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.IdentUtil;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopetv3.api.config.Data;
import com.dsh105.echopetv3.api.config.Lang;
import com.dsh105.echopetv3.api.config.PetSettings;
import com.dsh105.echopetv3.api.config.Settings;
import com.dsh105.echopetv3.api.entity.AttributeAccessor;
import com.dsh105.echopetv3.api.entity.PetData;
import com.dsh105.echopetv3.api.entity.PetType;
import com.dsh105.echopetv3.api.entity.pet.Pet;
import com.dsh105.echopetv3.api.hook.WorldGuardProvider;
import org.bukkit.entity.Player;

import java.util.*;

public class SimplePetManager implements PetManager {

    private HashMap<String, ArrayList<Pet>> IDENT_TO_PET_MAP = new HashMap<>();

    private void modify(Pet pet, boolean add) {
        ArrayList<Pet> existing = IDENT_TO_PET_MAP.get(pet.getOwnerIdent());
        if (existing == null) {
            existing = new ArrayList<>();
        }
        if (add) {
            existing.add(pet);
        } else {
            existing.remove(pet);
        }
        IDENT_TO_PET_MAP.put(pet.getOwnerIdent(), existing);
    }

    @Override
    public List<Pet> getPetsOfType(PetType petType) {
        List<Pet> petsOfType = new ArrayList<>();
        for (String playerIdent : IDENT_TO_PET_MAP.keySet()) {
            petsOfType.addAll(getPetsFor(playerIdent));
        }
        return Collections.unmodifiableList(petsOfType);
    }

    @Override
    public List<Pet> getPetsFor(String playerIdent) {
        List<Pet> pets = IDENT_TO_PET_MAP.get(playerIdent);
        if (pets == null) {
            pets = new ArrayList<>();
        }
        return Collections.unmodifiableList(pets);
    }

    @Override
    public List<Pet> getPetsFor(Player player) {
        return getPetsFor(IdentUtil.getIdentificationForAsString(player));
    }

    @Override
    public Map<String, Pet> getPetNameMapFor(String playerIdent) {
        Map<String, Pet> petNameMap = new HashMap<>();
        List<Pet> pets = getPetsFor(playerIdent);
        int index = 0;
        for (Pet pet : pets) {
            String name = pet.getName();
            if (petNameMap.containsKey(name)) {
                name = name + "-" + index++;
            }
            petNameMap.put(name, pet);
        }
        return Collections.unmodifiableMap(petNameMap);
    }

    @Override
    public Map<String, Pet> getPetNameMapFor(Player player) {
        return getPetNameMapFor(IdentUtil.getIdentificationForAsString(player));
    }

    @Override
    public Pet getPetByName(String playerIdent, String petName) {
        return getPetNameMapFor(playerIdent).get(petName);
    }

    @Override
    public Pet getPetByName(Player player, String petName) {
        return getPetByName(IdentUtil.getIdentificationForAsString(player), petName);
    }

    @Override
    public List<Pet> getAllPets() {
        ArrayList<Pet> pets = new ArrayList<>();
        for (ArrayList<Pet> petsForPlayer : IDENT_TO_PET_MAP.values()) {
            pets.addAll(petsForPlayer);
        }
        return pets;
    }

    @Override
    public void removePets(String playerIdent) {
        List<Pet> pets = getPetsFor(playerIdent);
        IDENT_TO_PET_MAP.remove(playerIdent);
        for (Pet pet : pets) {
            save(pet);
            pet.despawn(true);
        }
    }

    @Override
    public void removePets(Player player) {
        removePets(IdentUtil.getIdentificationForAsString(player));
    }

    @Override
    public void removePet(Pet pet) {
        removePet(pet, true);
    }

    @Override
    public void removePet(Pet pet, boolean makeDeathSound) {
        modify(pet, false);
        pet.despawn(makeDeathSound);
    }

    @Override
    public Pet load(Player player, boolean sendMessage) {
        Pet pet = create(player);

        if (pet != null && sendMessage) {
            Lang.PET_LOADED.send(player, "petname", pet.getName());
        }
        return pet;
    }

    @Override
    public Pet create(Player owner, PetType type, boolean sendFailMessage) {
        String failMessage;
        if (!PetSettings.ENABLE.getValue(type.storageName())) {
            failMessage = Lang.PET_TYPE_DISABLED.getValue("type", type.humanName());
        } else if (!EchoPet.getProvider(WorldGuardProvider.class).allowPets(owner.getLocation())) {
            failMessage = Lang.PETS_DISABLED_HERE.getValue();
        } else {
            Pet pet = type.getNewPetInstance(owner);
            forceData(pet);
            modify(pet, true);
            return pet;
        }

        if (sendFailMessage) {
            owner.sendMessage(failMessage);
        }
        return null;
    }

    @Override
    public Pet create(Player owner) {
        if (!Settings.LOAD_SAVED_PETS.getValue()) {
            Lang.PET_LOADED.send(owner);
            return null;
        }

        String ident = IdentUtil.getIdentificationForAsString(owner);

        PetType type = PetType.valueOf(Data.PET_TYPE.getValue(ident));
        Pet pet = create(owner, type, true);
        if (pet == null) {
            return null;
        }

        String name = Data.PET_NAME.getValue(ident);
        if (name == null) {
            name = PetSettings.DEFAULT_NAME.getValue(type.storageName());
        }
        pet.setName(name);

        for (String value : Data.PET_DATA.getValue(ident)) {
            PetData petData = PetData.valueOf(value);
            pet.setDataValue(petData);
        }

        PetType riderType = PetType.valueOf(Data.RIDER_TYPE.getValue(ident));
        if (riderType != null) {
            Pet rider = pet.spawnRider(riderType, true);

            if (rider != null) {
                String riderName = Data.RIDER_NAME.getValue(ident);
                if (riderName == null) {
                    riderName = PetSettings.DEFAULT_NAME.getValue(riderType.storageName());
                }
                rider.setName(riderName);

                for (String value : Data.RIDER_DATA.getValue(ident)) {
                    PetData petData = PetData.valueOf(value);
                    rider.setDataValue(petData);
                }
            }
        }

        // Re-force everything again
        forceData(pet);

        return pet;
    }

    @Override
    public void forceData(Pet pet) {
        StringBuilder sb = new StringBuilder();

        for (PetData petData : PetData.valid()) {
            if (PetSettings.FORCE_DATA.getValue(pet.getType().storageName(), petData.storageName())) {
                pet.setDataValue(petData);
                sb.append(petData.storageName()).append(", ");
            }

            if (PetSettings.FORCE_DATA.getValue(pet.getRider().getType().storageName(), petData.storageName())) {
                pet.getRider().setDataValue(petData);
                sb.append(petData.storageName()).append(" (Rider), ");
            }
        }

        if (sb.length() > 0) {
            sb.delete(sb.length() - 2, sb.length());
            if (Settings.SEND_FORCE_MESSAGE.getValue()) {
                Lang.DATA_FORCED.getValue("data", sb.toString());
            }
        }
    }

    @Override
    public void save(Pet pet) {
        clear(pet);

        String ident = pet.getOwnerIdent();
        Data.PET_TYPE.setValue(pet.getType().storageName(), ident);
        Data.PET_NAME.setValue(pet.getName(), ident);

        List<PetData> activeData = AttributeAccessor.getActiveDataValues(pet);
        if (!activeData.isEmpty()) {
            ArrayList<String> converted = new ArrayList<>();
            for (PetData petData : activeData) {
                converted.add(petData.storageName());
            }
            Data.PET_DATA.setValue(converted.toArray(StringUtil.EMPTY_STRING_ARRAY));
        }

        if (pet.getRider() != null) {
            Pet rider = pet.getRider();
            Data.RIDER_TYPE.setValue(rider.getType().storageName(), ident);
            Data.RIDER_NAME.setValue(rider.getName(), ident);

            List<PetData> activeRiderData = AttributeAccessor.getActiveDataValues(rider);
            if (!activeRiderData.isEmpty()) {
                ArrayList<String> converted = new ArrayList<>();
                for (PetData petData : activeRiderData) {
                    converted.add(petData.storageName());
                }
                Data.RIDER_DATA.setValue(converted.toArray(StringUtil.EMPTY_STRING_ARRAY));
            }
        }
    }

    @Override
    public void clear(Pet pet) {
        clear(pet.getOwnerIdent());
    }

    @Override
    public void clear(Player player) {
        clear(IdentUtil.getIdentificationForAsString(player));
    }

    @Override
    public void clear(String playerIdent) {
        Data.SECTION.setValue((String) null, playerIdent);
    }
}