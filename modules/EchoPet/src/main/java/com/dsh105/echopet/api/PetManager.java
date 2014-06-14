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

package com.dsh105.echopet.api;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.PlayerIdent;
import com.dsh105.echopet.api.config.ConfigType;
import com.dsh105.echopet.api.config.PetSettings;
import com.dsh105.echopet.api.config.Settings;
import com.dsh105.echopet.api.entity.*;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.entity.type.pet.*;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.api.plugin.IPetManager;
import com.dsh105.echopet.api.plugin.PetStorage;
import com.dsh105.echopet.util.Lang;
import com.dsh105.echopet.util.PetUtil;
import com.dsh105.echopet.util.ReflectionUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;


public class PetManager implements IPetManager {

    private ArrayList<Pet> pets = new ArrayList<Pet>();

    @Override
    public ArrayList<Pet> getPets() {
        return pets;
    }

    @Override
    public Pet loadPets(Player p, boolean findDefault, boolean sendMessage, boolean checkWorldOverride) {
        if (Settings.SQL_ENABLE.getValue() && Settings.SQL_OVERRIDE.getValue()) {
            Pet pet = EchoPet.getSqlManager().createPetFromDatabase(p);
            if (pet == null) {
                return null;
            } else {
                if (sendMessage) {
                    Lang.sendTo(p, Lang.DATABASE_PET_LOAD.toString().replace("%petname%", pet.getPetName().toString()));
                }
            }
            return pet;
        } else if (EchoPet.getConfig(ConfigType.DATA).get("default." + PlayerIdent.getIdentificationFor(p) + ".pet.type") != null && findDefault) {
            Pet pi = this.createPetFromFile("default", p);
            if (pi == null) {
                return null;
            } else {
                if (sendMessage) {
                    Lang.sendTo(p, Lang.DEFAULT_PET_LOAD.toString().replace("%petname%", pi.getPetName().toString()));
                }
            }
            return pi;
        } else if ((checkWorldOverride && Settings.LOAD_SAVED_PETS_ON_WORLD_CHANGE.getValue() || Settings.LOAD_SAVED_PETS.getValue()) {
            if (EchoPet.getConfig(ConfigType.DATA).get("autosave." + PlayerIdent.getIdentificationFor(p) + ".pet.type") != null) {
                Pet pi = this.createPetFromFile("autosave", p);
                if (pi == null) {
                    return null;
                } else {
                    if (sendMessage) {
                        Lang.sendTo(p, Lang.AUTOSAVE_PET_LOAD.toString().replace("%petname%", pi.getPetName().toString()));
                    }
                }
                return pi;
            }
        }
        return null;
    }

    @Override
    public void removeAllPets() {
        Iterator<Pet> i = pets.listIterator();
        while (i.hasNext()) {
            Pet p = i.next();
            saveFileData("autosave", p);
            EchoPet.getSqlManager().saveToDatabase(p, false);
            p.removePet(true);
            i.remove();
        }
    }

    @Override
    public Pet createPet(Player owner, PetType petType, boolean sendMessageOnFail) {
        if (ReflectionUtil.BUKKIT_VERSION_NUMERIC == 178 && petType == PetType.HUMAN) {
            if (sendMessageOnFail) {
                Lang.sendTo(owner, Lang.HUMAN_PET_DISABLED.toString());
            }
            return null;
        }
        removePets(owner, true);
        if (!EchoPet.getPlugin().getWorldGuardProvider().allowPets(owner.getLocation())) {
            if (sendMessageOnFail) {
                Lang.sendTo(owner, Lang.PETS_DISABLED_HERE.toString().replace("%world%", StringUtil.capitalise(owner.getWorld().getName())));
            }
            return null;
        }
        if (!PetSettings.ENABLE.getValue(petType.storageName())) {
            if (sendMessageOnFail) {
                Lang.sendTo(owner, Lang.PET_TYPE_DISABLED.toString().replace("%type%", petType.humanName()));
            }
            return null;
        }
        Pet pi = petType.getNewPetInstance(owner);
        forceAllValidData(pi);
        pets.add(pi);
        return pi;
    }

    @Override
    public Pet createPet(Player owner, PetType petType, PetType riderType) {
        if (ReflectionUtil.BUKKIT_VERSION_NUMERIC == 178 && (petType == PetType.HUMAN) || riderType == PetType.HUMAN) {
            Lang.sendTo(owner, Lang.HUMAN_PET_DISABLED.toString());
            return null;
        }
        removePets(owner, true);
        if (!EchoPet.getPlugin().getWorldGuardProvider().allowPets(owner.getLocation())) {
            Lang.sendTo(owner, Lang.PETS_DISABLED_HERE.toString().replace("%world%", StringUtil.capitalise(owner.getWorld().getName())));
            return null;
        }
        if (!PetSettings.ENABLE.getValue(petType.storageName())) {
            Lang.sendTo(owner, Lang.PET_TYPE_DISABLED.toString().replace("%type%", petType.humanName()));
            return null;
        }
        Pet pi = petType.getNewPetInstance(owner);
        pi.createRider(riderType, true);
        forceAllValidData(pi);
        pets.add(pi);
        return pi;
    }

    @Override
    public Pet getPet(Player player) {
        for (Pet pi : pets) {
            if (PlayerIdent.getIdentificationFor(player).equals(pi.getOwnerIdentification())) {
                return pi;
            }
        }
        return null;
    }

    @Override
    public Pet getPet(Entity pet) {
        for (Pet pi : pets) {
            Pet rider = pi.getRider();
            if (pi.getEntityPet().equals(pet) || (rider != null && rider.getEntityPet().equals(pet))) {
                return pi;
            }
            if (pi.getCraftPet().equals(pet) || (rider != null && rider.getCraftPet().equals(pet))) {
                return pi;
            }
        }
        return null;
    }

    // Force all data specified in config file and notify player.
    @Override
    public void forceAllValidData(Pet pi) {
        ArrayList<PetData> tempData = new ArrayList<PetData>();
        for (PetData data : PetData.values()) {
            if (PetSettings.FORCE_DATA.getValue(pi.getPetType().storageName(), data.storageName())) {
                tempData.add(data);
            }
        }
        pi.setDataValue(tempData.toArray(new PetData[tempData.size()]));

        ArrayList<PetData> tempRiderData = new ArrayList<PetData>();
        if (pi.getRider() != null) {
            for (PetData data : PetData.values()) {
                if (PetSettings.FORCE_DATA.getValue(pi.getPetType().storageName(), data.storageName())) {
                    tempRiderData.add(data);
                }
            }
            pi.getRider().setDataValue(tempRiderData.toArray(new PetData[tempData.size()]));
        }

        if (Settings.SEND_FORCE_MESSAGE.getValue()) {
            String dataToString = tempRiderData.isEmpty() ? PetUtil.dataToString(tempData, tempRiderData) : PetUtil.dataToString(tempData);
            ;
            if (dataToString != null) {
                Lang.sendTo(pi.getOwner(), Lang.DATA_FORCE_MESSAGE.toString().replace("%data%", dataToString));
            }
        }
    }

    @Override
    public void updateFileData(String type, Pet pet, ArrayList<PetData> list, boolean b) {
        EchoPet.getSqlManager().saveToDatabase(pet, pet.isRider());
        String w = pet.getOwner().getWorld().getName();
        String path = type + "." + w + "." + pet.getOwnerIdentification();
        for (PetData pd : list) {
            EchoPet.getConfig(ConfigType.DATA).set(path + ".pet.data." + pd.toString().toLowerCase(), b);
        }
        EchoPet.getConfig(ConfigType.DATA).saveConfig();
    }

    @Override
    public Pet createPetFromFile(String type, Player p) {
        if (Settings.LOAD_SAVED_PETS.getValue()) {
            String path = type + "." + PlayerIdent.getIdentificationFor(p);
            if (EchoPet.getConfig(ConfigType.DATA).get(path) != null) {
                PetType petType = PetType.valueOf(EchoPet.getConfig(ConfigType.DATA).getString(path + ".pet.type"));
                String name = EchoPet.getConfig(ConfigType.DATA).getString(path + ".pet.name");
                if (name.equalsIgnoreCase("") || name == null) {
                    name = petType.getDefaultName(p.getName());
                }
                if (petType == null) return null;
                if (!PetSettings.ENABLE.getValue(petType.storageName())) {
                    return null;
                }
                Pet pi = this.createPet(p, petType, true);
                if (pi == null) {
                    return null;
                }
                //Pet pi = petType.getNewPetInstance(p, petType);
                //Pet pi = new Pet(p, petType);

                pi.setPetName(name);

                ArrayList<PetData> data = new ArrayList<PetData>();
                ConfigurationSection cs = EchoPet.getConfig(ConfigType.DATA).getConfigurationSection(path + ".pet.data");
                if (cs != null) {
                    for (String key : cs.getKeys(false)) {
                        if (GeneralUtil.isEnumType(PetData.class, key.toUpperCase())) {
                            PetData pd = PetData.valueOf(key.toUpperCase());
                            data.add(pd);
                        } else {
                            Logger.log(Logger.LogLevel.WARNING, "Error whilst loading data Pet Save Data for " + pi.getNameOfOwner() + ". Unknown enum type: " + key + ".", true);
                        }
                    }
                }

                if (!data.isEmpty()) {
                    pi.setDataValue(data.toArray(new PetData[data.size()]))
                }

                this.loadRiderFromFile(type, pi);

                forceAllValidData(pi);
                return pi;
            }
        }
        return null;
    }

    @Override
    public void loadRiderFromFile(Pet pet) {
        this.loadRiderFromFile("autosave", pet);
    }

    @Override
    public void loadRiderFromFile(String type, Pet pet) {
        if (pet.getOwner() != null) {
            String path = type + "." + pet.getOwnerIdentification();
            if (EchoPet.getConfig(ConfigType.DATA).get(path + ".rider.type") != null) {
                PetType riderPetType = PetType.valueOf(EchoPet.getConfig(ConfigType.DATA).getString(path + ".rider.type"));
                String riderName = EchoPet.getConfig(ConfigType.DATA).getString(path + ".rider.name");
                if (riderName.equalsIgnoreCase("") || riderName == null) {
                    riderName = riderPetType.getDefaultName(pet.getNameOfOwner());
                }
                if (riderPetType == null) return;
                if (PetSettings.ALLOW_RIDERS.getValue(pet.getPetType().storageName())) {
                    Pet rider = pet.createRider(riderPetType, true);
                    if (rider != null && rider.getEntityPet() != null) {
                        rider.setPetName(riderName);
                        ArrayList<PetData> riderData = new ArrayList<PetData>();
                        ConfigurationSection mcs = EchoPet.getConfig(ConfigType.DATA).getConfigurationSection(path + ".rider.data");
                        if (mcs != null) {
                            for (String key : mcs.getKeys(false)) {
                                if (GeneralUtil.isEnumType(PetData.class, key.toUpperCase())) {
                                    PetData pd = PetData.valueOf(key.toUpperCase());
                                    riderData.add(pd);
                                } else {
                                    Logger.log(Logger.LogLevel.WARNING, "Error whilst loading data Pet Rider Save Data for " + pet.getNameOfOwner() + ". Unknown enum type: " + key + ".", true);
                                }
                            }
                        }
                        if (!riderData.isEmpty()) {
                            pet.setDataValue(riderData.toArray(new PetData[riderData.size()]));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void removePets(Player player, boolean makeDeathSound) {
        Iterator<Pet> i = pets.listIterator();
        while (i.hasNext()) {
            Pet p = i.next();
            if (PlayerIdent.getIdentificationFor(player).equals(p.getOwnerIdentification())) {
                p.removePet(makeDeathSound);
                i.remove();
            }
        }
    }

    @Override
    public void removePet(Pet pi, boolean makeDeathSound) {
        Iterator<Pet> i = pets.listIterator();
        while (i.hasNext()) {
            Pet p = i.next();
            if (pi != null && p != null) {
                if (pi.getOwnerIdentification().equals(p.getOwnerIdentification())) {
                    p.removePet(makeDeathSound);
                    i.remove();
                }
            }
        }
    }

    @Override
    public void saveFileData(String type, Pet pet) {
        clearFileData(type, pet);

        String path = type + "." + pet.getOwnerIdentification();
        PetType petType = pet.getPetType();

        EchoPet.getConfig(ConfigType.DATA).set(path + ".pet.type", petType.storageName());
        EchoPet.getConfig(ConfigType.DATA).set(path + ".pet.name", pet.getPetNameWithoutColours());

        for (PetData pd : pet.getActiveData()) {
            EchoPet.getConfig(ConfigType.DATA).set(path + ".pet.data." + pd.toString().toLowerCase(), true);
        }

        if (pet.getRider() != null) {
            PetType riderType = pet.getRider().getPetType();

            EchoPet.getConfig(ConfigType.DATA).set(path + ".rider.type", riderType.storageName());
            EchoPet.getConfig(ConfigType.DATA).set(path + ".rider.name", pet.getRider().getPetNameWithoutColours());
            for (PetData pd : pet.getRider().getActiveData()) {
                EchoPet.getConfig(ConfigType.DATA).set(path + ".rider.data." + pd.toString().toLowerCase(), true);
            }
        }
        EchoPet.getConfig(ConfigType.DATA).saveConfig();
    }

    @Override
    public void saveFileData(String type, Player p, PetStorage UPD, PetStorage UMD) {
        clearFileData(type, p);
        PetType pt = UPD.petType;
        PetData[] data = UPD.petDataList.toArray(new PetData[UPD.petDataList.size()]);
        String petName = UPD.petName;
        if (UPD.petName == null || UPD.petName.equalsIgnoreCase("")) {
            petName = pt.getDefaultName(p.getName());
        }
        PetType riderType = UMD.petType;
        PetData[] riderData = UMD.petDataList.toArray(new PetData[UMD.petDataList.size()]);
        String riderName = UMD.petName;
        if (UMD.petName == null || UMD.petName.equalsIgnoreCase("")) {
            riderName = pt.getDefaultName(p.getName());
        }

        String path = type + "." + PlayerIdent.getIdentificationFor(p);
        EchoPet.getConfig(ConfigType.DATA).set(path + ".pet.type", pt.storageName());
        EchoPet.getConfig(ConfigType.DATA).set(path + ".pet.name", petName);

        for (PetData pd : data) {
            EchoPet.getConfig(ConfigType.DATA).set(path + ".pet.data." + pd.toString().toLowerCase(), true);
        }

        if (riderData != null && riderType != null) {
            EchoPet.getConfig(ConfigType.DATA).set(path + ".rider.type", riderType.storageName());
            EchoPet.getConfig(ConfigType.DATA).set(path + ".rider.name", riderName);
            for (PetData pd : riderData) {
                EchoPet.getConfig(ConfigType.DATA).set(path + ".rider.data." + pd.toString().toLowerCase(), true);
            }

        }
        EchoPet.getConfig(ConfigType.DATA).saveConfig();
    }

    @Override
    public void saveFileData(String type, Player p, PetStorage UPD) {
        clearFileData(type, p);
        PetType pt = UPD.petType;
        PetData[] data = UPD.petDataList.toArray(new PetData[UPD.petDataList.size()]);
        String petName = UPD.petName;

        String path = type + "." + PlayerIdent.getIdentificationFor(p);
        EchoPet.getConfig(ConfigType.DATA).set(path + ".pet.type", pt.storageName());
        EchoPet.getConfig(ConfigType.DATA).set(path + ".pet.name", petName);

        for (PetData pd : data) {
            EchoPet.getConfig(ConfigType.DATA).set(path + ".pet.data." + pd.toString().toLowerCase(), true);
        }
        EchoPet.getConfig(ConfigType.DATA).saveConfig();
    }

    @Override
    public void clearAllFileData() {
        for (String key : EchoPet.getConfig(ConfigType.DATA).getKeys(true)) {
            if (EchoPet.getConfig(ConfigType.DATA).get(key) != null) {
                EchoPet.getConfig(ConfigType.DATA).set(key, null);
            }
        }
        EchoPet.getConfig(ConfigType.DATA).saveConfig();
    }

    @Override
    public void clearFileData(String type, Pet pi) {
        EchoPet.getConfig(ConfigType.DATA).set(type + "." + pi.getOwnerIdentification(), null);
        EchoPet.getConfig(ConfigType.DATA).saveConfig();
    }

    @Override
    public void clearFileData(String type, Player p) {
        EchoPet.getConfig(ConfigType.DATA).set(type + "." + PlayerIdent.getIdentificationFor(p), null);
        EchoPet.getConfig(ConfigType.DATA).saveConfig();
    }
}
