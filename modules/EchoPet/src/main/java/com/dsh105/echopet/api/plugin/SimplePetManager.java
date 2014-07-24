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

package com.dsh105.echopet.api.plugin;

import com.dsh105.commodus.IdentUtil;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.api.config.Data;
import com.dsh105.echopet.api.config.Lang;
import com.dsh105.echopet.api.config.PetSettings;
import com.dsh105.echopet.api.config.Settings;
import com.dsh105.echopet.api.entity.AttributeAccessor;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.hook.WorldGuardProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class SimplePetManager implements PetManager {

    /**
     * Maps player owner to a list of active pets
     */
    private HashMap<String, ArrayList<Pet>> IDENT_TO_PET_MAP = new HashMap<>();

    /**
     * Maps pet unique ID to pet
     */
    private HashMap<UUID, Pet> PET_ID_TO_PET_MAP = new HashMap<>();

    /**
     * Maps player ident to [pet name to pet ID]
     */
    private HashMap<String, HashMap<String, UUID>> IDENT_TO_PET_NAME_MAP = new HashMap<>();

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

        PET_ID_TO_PET_MAP.put(pet.getPetId(), pet);

        HashMap<String, UUID> existingPetName = IDENT_TO_PET_NAME_MAP.get(pet.getOwnerIdent());
        if (existingPetName == null) {
            existingPetName = new HashMap<>();
        }
        existingPetName.put(pet.getName(), pet.getPetId());
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
    public void mapPetName(Pet pet) {
        HashMap<String, UUID> petNameMap = IDENT_TO_PET_NAME_MAP.get(pet.getOwnerIdent());
        petNameMap.put(pet.getName(), pet.getPetId());
        IDENT_TO_PET_NAME_MAP.put(pet.getOwnerIdent(), petNameMap);
    }

    @Override
    public void unmapPetName(String playerIdent, String name) {
        HashMap<String, UUID> petNameMap = IDENT_TO_PET_NAME_MAP.get(playerIdent);
        petNameMap.remove(name);
        IDENT_TO_PET_NAME_MAP.put(playerIdent, petNameMap);
    }

    @Override
    public void updatePetNameMap(String playerIdent) {
        Map<String, UUID> petNameMap = getPetNameMapFor(playerIdent);
        HashMap<String, UUID> petNameMapClone = new HashMap<>();
        if (petNameMap.isEmpty()) {
            return;
        }

        for (Map.Entry<String, UUID> entry : petNameMap.entrySet()) {
            Pet pet = getPetById(entry.getValue());
            if (pet != null) {
                petNameMapClone.put(pet.getName(), entry.getValue());
            }
        }

        IDENT_TO_PET_NAME_MAP.put(playerIdent, petNameMapClone);
    }

    @Override
    public Map<String, UUID> getPetNameMapFor(String playerIdent) {
        HashMap<String, UUID> petNameMap = IDENT_TO_PET_NAME_MAP.get(playerIdent);
        if (petNameMap == null) {
            petNameMap = new HashMap<>();
        }
        return Collections.unmodifiableMap(petNameMap);
    }

    @Override
    public Map<String, UUID> getPetNameMapFor(Player player) {
        return getPetNameMapFor(IdentUtil.getIdentificationForAsString(player));
    }

    @Override
    public Pet getPetByName(String playerIdent, String petName) {
        return getPetById(getPetNameMapFor(playerIdent).get(petName));
    }

    @Override
    public Pet getPetByName(Player player, String petName) {
        return getPetByName(IdentUtil.getIdentificationForAsString(player), petName);
    }

    @Override
    public Map<UUID, Pet> getPetUniqueIdMap() {
        return Collections.unmodifiableMap(PET_ID_TO_PET_MAP);
    }

    @Override
    public Pet getPetById(UUID uniqueId) {
        return getPetUniqueIdMap().get(uniqueId);
    }

    @Override
    public void removeAllPets() {
        for (String ident : IDENT_TO_PET_MAP.keySet()) {
            for (Pet pet : getPetsFor(ident)) {
                removePet(pet);
            }
        }
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
    public List<Pet> load(Player player, boolean sendMessage) {
        List<Pet> loadedPets = loadPets(player);

        if (sendMessage && !loadedPets.isEmpty()) {
            if (loadedPets.size() == 1) {
                Lang.PET_LOADED.send(player, "name", loadedPets.get(0).getName());
            } else {
                Lang.PETS_LOADED.send(player, "number", loadedPets.size()  + "");
            }
        }

        return loadedPets;
    }

    @Override
    public Pet loadRider(Pet pet) {
        String petId = pet.getPetId().toString();
        PetType riderType = PetType.valueOf(Data.RIDER_TYPE.getValue(pet.getOwnerIdent(), petId));
        if (riderType != null) {
            Pet rider = pet.spawnRider(riderType, true);

            if (rider != null) {
                String riderName = Data.RIDER_NAME.getValue(pet.getOwnerIdent(), petId);
                if (riderName == null) {
                    riderName = PetSettings.DEFAULT_NAME.getValue(riderType.storageName());
                }
                rider.setName(riderName);

                for (String value : Data.RIDER_DATA.getValue(pet.getOwnerIdent(), petId)) {
                    PetData petData = PetData.valueOf(value);
                    rider.setDataValue(petData);
                }
            }
            forceData(rider);
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
    public List<Pet> loadPets(Player owner) {
        ArrayList<Pet> loadedPets = new ArrayList<>();
        ConfigurationSection section = EchoPet.getCore().getSettings(Data.class).getConfig().getConfigurationSection(Data.SECTION.getPath(IdentUtil.getIdentificationForAsString(owner)));
        if (section != null) {
            for (String key : section.getKeys(false)) {
                Pet pet = loadPet(owner, key);
                if (pet != null) {
                    loadedPets.add(pet);
                }
            }
        }
        return Collections.unmodifiableList(loadedPets);
    }

    @Override
    public Pet loadPet(Player owner, String petUniqueId) {
        if (!Settings.LOAD_SAVED_PETS.getValue()) {
            Lang.PET_LOADED.send(owner);
            return null;
        }

        String ident = IdentUtil.getIdentificationForAsString(owner);

        PetType type = PetType.valueOf(Data.PET_TYPE.getValue(ident, petUniqueId));
        Pet pet = create(owner, type, true);
        if (pet == null) {
            return null;
        }

        String name = Data.PET_NAME.getValue(ident, petUniqueId);
        if (name == null) {
            name = PetSettings.DEFAULT_NAME.getValue(type.storageName());
        }
        pet.setName(name);

        for (String value : Data.PET_DATA.getValue(ident, petUniqueId)) {
            PetData petData = PetData.valueOf(value);
            pet.setDataValue(petData);
        }

        loadRider(pet);

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
        String petId = pet.getPetId().toString();

        Data.PET_TYPE.setValue(pet.getType().storageName(), ident, petId);
        Data.PET_NAME.setValue(pet.getName(), ident, petId);

        List<PetData> activeData = AttributeAccessor.getActiveDataValues(pet);
        if (!activeData.isEmpty()) {
            ArrayList<String> converted = new ArrayList<>();
            for (PetData petData : activeData) {
                converted.add(petData.storageName());
            }
            Data.PET_DATA.setValue(converted.toArray(StringUtil.EMPTY_STRING_ARRAY), ident, petId);
        }

        if (pet.getRider() != null) {
            Pet rider = pet.getRider();
            Data.RIDER_TYPE.setValue(rider.getType().storageName(), ident, petId);
            Data.RIDER_NAME.setValue(rider.getName(), ident, petId);

            List<PetData> activeRiderData = AttributeAccessor.getActiveDataValues(rider);
            if (!activeRiderData.isEmpty()) {
                ArrayList<String> converted = new ArrayList<>();
                for (PetData petData : activeRiderData) {
                    converted.add(petData.storageName());
                }
                Data.RIDER_DATA.setValue(converted.toArray(StringUtil.EMPTY_STRING_ARRAY), ident, petId);
            }
        }
    }

    @Override
    public void clear(Pet pet) {
        Data.PET_SECTION.setValue((String) null, pet.getOwnerIdent(), pet.getPetId());
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