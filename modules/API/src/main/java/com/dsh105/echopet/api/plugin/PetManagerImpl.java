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

import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.api.configuration.Data;
import com.dsh105.echopet.api.configuration.Lang;
import com.dsh105.echopet.api.configuration.PetSettings;
import com.dsh105.echopet.api.configuration.Settings;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.attribute.AttributeManager;
import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.attribute.EntityAttribute;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.hook.WorldGuardDependency;
import com.dsh105.echopet.bridge.Ident;
import com.dsh105.echopet.bridge.MessageBridge;
import com.dsh105.echopet.bridge.PlayerBridge;
import com.dsh105.echopet.util.StringForm;

import java.util.*;

public class PetManagerImpl implements PetManager {

    /**
     * Maps player owner ID to a list of active pets
     */
    private final HashMap<UUID, List<Pet>> OWNER_ID_TO_PET_MAP = new HashMap<>();

    /**
     * Maps pet unique ID to pet
     */
    private final HashMap<UUID, Pet> PET_ID_TO_PET_MAP = new HashMap<>();

    /**
     * Maps player owner ID to [pet name to pet ID]
     */
    private final HashMap<UUID, HashMap<String, UUID>> OWNER_ID_TO_PET_NAME_MAP = new HashMap<>();

    private void modify(Pet pet, boolean add) {
        List<Pet> existing = OWNER_ID_TO_PET_MAP.get(pet.getOwnerUID());
        if (existing == null) {
            existing = new ArrayList<>();
        }

        if (add) {
            existing.add(pet);
            PET_ID_TO_PET_MAP.put(pet.getPetId(), pet);
        } else {
            existing.remove(pet);
            PET_ID_TO_PET_MAP.remove(pet.getPetId());
        }
        OWNER_ID_TO_PET_MAP.put(pet.getOwnerUID(), existing);
    }

    @Override
    public List<Pet> getPetsOfType(PetType petType) {
        List<Pet> petsOfType = new ArrayList<>();
        for (UUID playerUID : OWNER_ID_TO_PET_MAP.keySet()) {
            petsOfType.addAll(getPetsFor(playerUID));
        }
        return Collections.unmodifiableList(petsOfType);
    }

    @Override
    public List<Pet> getPetsFor(UUID playerUID) {
        List<Pet> pets = OWNER_ID_TO_PET_MAP.get(playerUID);
        if (pets == null) {
            pets = new ArrayList<>();
        }
        return Collections.unmodifiableList(pets);
    }

    @Override
    public void mapPetName(Pet pet) {
        HashMap<String, UUID> petNameMap = OWNER_ID_TO_PET_NAME_MAP.get(pet.getOwnerUID());
        if (petNameMap == null) {
            petNameMap = new HashMap<>();
        }
        petNameMap.put(pet.getName(), pet.getPetId());
        OWNER_ID_TO_PET_NAME_MAP.put(pet.getOwnerUID(), petNameMap);
    }

    @Override
    public void unmapPetNames(UUID playerUID) {
        OWNER_ID_TO_PET_NAME_MAP.remove(playerUID);
    }

    @Override
    public void unmapPetName(UUID playerUID, String name) {
        HashMap<String, UUID> petNameMap = OWNER_ID_TO_PET_NAME_MAP.get(playerUID);
        if (petNameMap != null) {
            petNameMap.remove(name);
            OWNER_ID_TO_PET_NAME_MAP.put(playerUID, petNameMap);
        }
    }

    @Override
    public void updatePetNameMap(UUID playerUID) {
        Map<String, UUID> petNameMap = getPetNameMapFor(playerUID);
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

        OWNER_ID_TO_PET_NAME_MAP.put(playerUID, petNameMapClone);
    }

    @Override
    public Map<String, UUID> getPetNameMapFor(UUID playerUID) {
        HashMap<String, UUID> petNameMap = OWNER_ID_TO_PET_NAME_MAP.get(playerUID);
        if (petNameMap == null) {
            petNameMap = new HashMap<>();
        }
        return Collections.unmodifiableMap(petNameMap);
    }

    @Override
    public Pet getPetByName(UUID playerUID, String petName) {
        return getPetById(getPetNameMapFor(playerUID).get(petName));
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
        for (UUID ownerUID : OWNER_ID_TO_PET_MAP.keySet()) {
            for (Pet pet : new ArrayList<>(getPetsFor(ownerUID))) {
                removePet(pet);
            }
        }
    }

    @Override
    public List<Pet> getAllPets() {
        List<Pet> pets = new ArrayList<>();
        for (List<Pet> mappedPets : OWNER_ID_TO_PET_MAP.values()) {
            pets.addAll(mappedPets);
        }
        return Collections.unmodifiableList(pets);
    }

    @Override
    public void removePets(UUID playerUID) {
        List<Pet> pets = getPetsFor(playerUID);
        OWNER_ID_TO_PET_MAP.remove(playerUID);
        for (Pet pet : pets) {
            save(pet);
            pet.remove(true);
        }
    }

    @Override
    public void removePet(Pet pet) {
        removePet(pet, true);
    }

    @Override
    public void removePet(Pet pet, boolean makeDeathSound) {
        modify(pet, false);
        pet.remove(makeDeathSound);
    }
    @Override
    public void load(UUID playerUID, final boolean sendMessage) {
        load(playerUID, sendMessage, null);
    }

    @Override
    public void load(UUID playerUID, final boolean sendMessage, final LoadCallback<List<Pet>> callback) {
        final Object player = Ident.get().getPlayer(playerUID);
        if (player == null) {
            throw new IllegalArgumentException("Invalid UUID (player not found): " + playerUID);
        }
        loadPets(playerUID, new LoadCallback<List<Pet>>() {
            @Override
            public void call(List<Pet> loadedPets) {
                if (sendMessage && !loadedPets.isEmpty()) {
                    if (loadedPets.size() == 1) {
                        Lang.PET_LOADED.send(player, "name", loadedPets.get(0).getName());
                    } else {
                        Lang.PETS_LOADED.send(player, "number", loadedPets.size() + "");
                    }
                }
                if (callback != null) {
                    callback.call(loadedPets);
                }
            }
        });
    }

    @Override
    public Pet loadRider(Pet pet) {
        String petId = pet.getPetId().toString();
        String savedRider = Data.RIDER_TYPE.getValue(pet.getOwnerUIDAsString(), petId);
        if (savedRider != null) {
            PetType riderType = PetType.valueOf(savedRider.toUpperCase());
            Pet rider = pet.spawnRider(riderType, true);

            if (rider != null) {
                String riderName = Data.RIDER_NAME.getValue(pet.getOwnerUIDAsString(), petId);
                if (riderName == null) {
                    riderName = PetSettings.DEFAULT_NAME.getValue(riderType.storageName());
                }
                rider.setName(riderName);

                for (String attributeType : Data.RIDER_ATTRIBUTES.getValue(pet.getOwnerUIDAsString(), petId)) {
                    for (String value : Data.RIDER_ATTRIBUTES_BY_TYPE.getValue(pet.getOwnerUIDAsString(), petId, attributeType)) {
                        EntityAttribute attribute = Attributes.valueOf(attributeType, value);
                        if (attribute != null) {
                            rider.setAttribute(attribute);
                        }
                    }
                }
            }
            forceData(rider);
        }
        return pet;
    }

    @Override
    public Pet create(UUID playerUID, PetType type, boolean sendFailMessage) {
        Object player = Ident.get().getPlayer(playerUID);
        if (player == null) {
            throw new IllegalArgumentException("Invalid UUID (player not found): " + playerUID);
        }
        String failMessage;
        if (!PetSettings.ENABLE.getValue(type.storageName())) {
            failMessage = Lang.PET_TYPE_DISABLED.getValue("type", type.humanName());
        } else if (!EchoPet.getDependency(WorldGuardDependency.class).allowPets(PlayerBridge.of(playerUID).getLocation())) {
            failMessage = Lang.PETS_DISABLED_HERE.getValue();
        } else {
            Pet pet = EchoPet.getPetRegistry().spawn(type, playerUID);
            if (pet != null) {
                forceData(pet);
                modify(pet, true);
                mapPetName(pet);
                return pet;
            }
            failMessage = Lang.FAILED_SPAWN.getValue();
        }

        if (sendFailMessage) {
            EchoPet.getBridge(MessageBridge.class).send(player, Lang.PREFIX.getValue(), failMessage);
        }
        return null;
    }

    @Override
    public void loadPets(UUID playerUID) {
        loadPets(playerUID, null);
    }

    @Override
    public void loadPets(UUID playerUID, LoadCallback<List<Pet>> callback) {
        List<Pet> loadedPets = new ArrayList<>();
        /*List<Map<?, ?>> keys = EchoPet.getConfig(ConfigType.DATA).getMapList(Data.SECTION.getPath(Ident.get().getUID(owner).toString()));
        if (keys != null) {
            for (Map<?, ?> map : keys) {
                for (Map.Entry<?, ?> entry : map.entrySet()) {

                }
            }
        }*/
        // TODO: this
        ConfigurationSection section = EchoPet.getOptions(Data.class).getConfig().getConfigurationSection(Data.SECTION.getPath(IdentUtil.getIdentificationForAsString(owner)));
        if (section != null) {
            for (String key : section.getKeys(false)) {
                Pet pet = loadPet(playerUID, StringUtil.convertUUID(key));
                if (pet != null) {
                    loadedPets.add(pet);
                }
            }
        }
        if (callback != null) {
            callback.call(Collections.unmodifiableList(loadedPets));
        }
    }

    @Override
    public void loadPet(UUID playerUID, UUID petUniqueId, LoadCallback<Pet> callback) {
        if (!Settings.LOAD_SAVED_PETS.getValue()) {
            Lang.PET_NOT_LOADED.send(playerUID);
            return;
        }

        PetType type = PetType.valueOf(Data.PET_TYPE.getValue(playerUID.toString(), petUniqueId.toString()).toUpperCase());
        Pet pet = create(playerUID, type, true);
        if (pet == null) {
            return;
        }

        try {
            String name = Data.PET_NAME.getValue(playerUID.toString(), petUniqueId.toString());
            if (name == null) {
                name = PetSettings.DEFAULT_NAME.getValue(type.storageName());
            }
            pet.setName(name);

            for (String attributeType : Data.PET_ATTRIBUTES.getValue(playerUID.toString(), petUniqueId.toString())) {
                for (String value : Data.PET_ATTRIBUTES_BY_TYPE.getValue(playerUID.toString(), petUniqueId.toString(), attributeType)) {
                    EntityAttribute attribute = Attributes.valueOf(attributeType, value);
                    if (attribute != null) {
                        pet.setAttribute(attribute);
                    }
                }
            }

            loadRider(pet);

            // Re-force everything again
            forceData(pet);
        } catch (Exception e) {
            pet.onError(e);
            return;
        }

        callback.call(pet);
    }

    @Override
    public void forceData(Pet pet) {
        StringBuilder sb = new StringBuilder();

        for (EntityAttribute entityAttribute : AttributeManager.getModifier(pet).getValidAttributes()) {
            StringForm stringForm = StringForm.create(entityAttribute);
            if (PetSettings.FORCE_DATA.getValue(pet.getType().storageName(), entityAttribute.getType().getConfigName(), stringForm.getConfigName())) {
                pet.setAttribute(entityAttribute);
                sb.append(stringForm.getCaptalisedName()).append(", ");
            }

            if (pet.getRider() != null) {
                if (PetSettings.FORCE_DATA.getValue(pet.getRider().getType().storageName(), entityAttribute.getType().getConfigName(), stringForm.getConfigName())) {
                    pet.setAttribute(entityAttribute);
                    sb.append(stringForm.getCaptalisedName()).append(" (Rider), ");
                }
            }
        }

        if (sb.length() > 0) {
            sb.delete(sb.length() - 2, sb.length());
            if (Settings.SEND_FORCE_MESSAGE.getValue()) {
                Lang.DATA_FORCED.send(pet.getOwner(), "data", sb.toString());
            }
        }
    }

    @Override
    public void save(Pet pet) {
        clear(pet);

        String ident = pet.getOwnerUIDAsString();
        String petId = pet.getPetId().toString();

        Data.PET_TYPE.setValue(pet.getType().storageName(), ident, petId);
        Data.PET_NAME.setValue(pet.getName(), ident, petId);
        saveAttributes(pet);

        if (pet.getRider() != null) {
            Pet rider = pet.getRider();
            Data.RIDER_TYPE.setValue(rider.getType().storageName(), ident, petId);
            Data.RIDER_NAME.setValue(rider.getName(), ident, petId);

            saveAttributes(pet.getRider());
        }
    }

    private void saveAttributes(Pet pet) {
        List<EntityAttribute> activeAttributes = pet.getActiveAttributes();
        Map<String, ArrayList<String>> convertedData = new HashMap<>();
        if (!activeAttributes.isEmpty()) {
            for (EntityAttribute entityAttribute : activeAttributes) {
                ArrayList<String> attributeTypes = convertedData.get(entityAttribute.getType().getConfigName());
                if (attributeTypes == null) {
                    attributeTypes = new ArrayList<>();
                }
                attributeTypes.add(StringForm.create(entityAttribute).getConfigName());
                convertedData.put(entityAttribute.getType().getConfigName(), attributeTypes);
            }
        }
        for (Map.Entry<String, ArrayList<String>> entry : convertedData.entrySet()) {
            Data.PET_ATTRIBUTES_BY_TYPE.setValue(entry.getValue(), pet.getOwnerUIDAsString(), pet.getPetId().toString(), entry.getKey());
        }
    }

    @Override
    public void clear(Pet pet) {
        Data.PET_SECTION.setValue(null, pet.getOwnerUIDAsString(), pet.getPetId().toString());
        unmapPetName(pet.getOwnerUID(), pet.getName());
    }

    @Override
    public void clear(UUID playerUID) {
        Data.SECTION.setValue(null, playerUID.toString());
        unmapPetNames(playerUID);
    }
}