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

package io.github.dsh105.echopet.api;

import com.dsh105.dshutils.logger.Logger;
import com.dsh105.dshutils.util.EnumUtil;
import com.dsh105.dshutils.util.StringUtil;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.api.entity.*;
import io.github.dsh105.echopet.api.entity.IAgeablePet;
import io.github.dsh105.echopet.api.entity.pet.type.*;
import io.github.dsh105.echopet.api.entity.pet.Pet;
import io.github.dsh105.echopet.api.entity.PetType;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.PetUtil;
import io.github.dsh105.echopet.util.WorldUtil;
import org.bukkit.DyeColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager.Profession;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;


public class PetHandler {

    private static EchoPetPlugin ec;

    private ArrayList<Pet> pets = new ArrayList<Pet>();

    public PetHandler(EchoPetPlugin ec) {
        PetHandler.ec = ec;
    }

    public static PetHandler getInstance() {
        return ec.PH;
    }

    public ArrayList<Pet> getPets() {
        return pets;
    }

    public Pet loadPets(Player p, boolean findDefault, boolean sendMessage, boolean checkWorldOverride) {
        EchoPetPlugin ec = EchoPetPlugin.getInstance();
        if (ec.options.sqlOverride()) {
            Pet pet = ec.SPH.createPetFromDatabase(p.getName());
            if (pet == null) {
                return null;
            } else {
                if (sendMessage) {
                    Lang.sendTo(p, Lang.DATABASE_PET_LOAD.toString().replace("%petname%", pet.getPetName().toString()));
                }
            }
            return pet;
        } else if (ec.getPetConfig().get("default." + p.getName() + ".pet.type") != null && findDefault) {
            Pet pi = ec.PH.createPetFromFile("default", p);
            if (pi == null) {
                return null;
            } else {
                if (sendMessage) {
                    Lang.sendTo(p, Lang.DEFAULT_PET_LOAD.toString().replace("%petname%", pi.getPetName().toString()));
                }
            }
            return pi;
        } else if ((checkWorldOverride && ec.options.getConfig().getBoolean("multiworldLoadOverride", true)) || ec.options.getConfig().getBoolean("loadSavedPets", true)) {
            if (ec.getPetConfig().get("autosave." + p.getName() + ".pet.type") != null) {
                Pet pi = ec.PH.createPetFromFile("autosave", p);
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

    public void removeAllPets() {
        Iterator<Pet> i = pets.listIterator();
        while (i.hasNext()) {
            Pet p = i.next();
            saveFileData("autosave", p);
            ec.SPH.saveToDatabase(p, false);
            p.removePet(true);
            i.remove();
        }
    }

    public Pet createPet(Player owner, PetType petType, boolean sendMessageOnFail) {
        removePets(owner.getName(), true);
        if (!WorldUtil.allowPets(owner.getLocation())) {
            if (sendMessageOnFail) {
                Lang.sendTo(owner, Lang.PETS_DISABLED_HERE.toString().replace("%world%", StringUtil.capitalise(owner.getWorld().getName())));
            }
            return null;
        }
        if (!ec.options.allowPetType(petType)) {
            if (sendMessageOnFail) {
                Lang.sendTo(owner, Lang.PET_TYPE_DISABLED.toString().replace("%type%", StringUtil.capitalise(petType.toString())));
            }
            return null;
        }
        Pet pi = petType.getNewPetInstance(owner);
        //Pet pi = new Pet(owner, petType);
        forceAllValidData(pi);
        pets.add(pi);
        //saveFileData("autosave", pi);
        //ec.SPH.saveToDatabase(pi, false);
        return pi;
    }

    public Pet createPet(Player owner, PetType petType, PetType riderType, boolean sendFailMessage) {
        removePets(owner.getName(), true);
        if (!WorldUtil.allowPets(owner.getLocation())) {
            if (sendFailMessage) {
                Lang.sendTo(owner, Lang.PETS_DISABLED_HERE.toString().replace("%world%", StringUtil.capitalise(owner.getWorld().getName())));
            }
            return null;
        }
        if (!ec.options.allowPetType(petType)) {
            if (sendFailMessage) {
                Lang.sendTo(owner, Lang.PET_TYPE_DISABLED.toString().replace("%type%", StringUtil.capitalise(petType.toString())));
            }
            return null;
        }
        Pet pi = petType.getNewPetInstance(owner);
        //Pet pi = new Pet(owner, petType);
        pi.createRider(riderType, true);
        forceAllValidData(pi);
        pets.add(pi);
        //saveFileData("autosave", pi);
        //ec.SPH.saveToDatabase(pi, false);
        return pi;
    }

    public Pet getPet(Player player) {
        for (Pet pi : pets) {
            if (pi.getNameOfOwner().equals(player.getName())) {
                return pi;
            }
        }
        return null;
    }

    public Pet getPet(Entity pet) {
        for (Pet pi : pets) {
            if (pi.getEntityPet().equals(pet) || pi.getRider().getEntityPet().equals(pet)) {
                return pi;
            }
            if (pi.getCraftPet().equals(pet) || pi.getRider().getCraftPet().equals(pet)) {
                return pi;
            }
        }
        return null;
    }

    // *May* add support for multiple pets
    /*public List<Pet> getPets(Player player) {
        List<Pet> tempList = new ArrayList<Pet>();
		
		for (Pet pi : pets) {
			if (pi.getPlayerOwner() == player) tempList.add(pi);
		}
		
		return tempList.isEmpty() ? null : tempList;
	}
	
	public int getMaxPets() {
		return ec.getConfig().getInt("maxPets");
	}*/

    // Force all data specified in config file and notify player.
    public void forceAllValidData(Pet pi) {
        ArrayList<PetData> tempData = new ArrayList<PetData>();
        for (PetData data : PetData.values()) {
            if (ec.options.forceData(pi.getPetType(), data)) {
                tempData.add(data);
            }
        }
        setData(pi, tempData.toArray(new PetData[tempData.size()]), true);

        ArrayList<PetData> tempRiderData = new ArrayList<PetData>();
        if (pi.getRider() != null) {
            for (PetData data : PetData.values()) {
                if (ec.options.forceData(pi.getPetType(), data)) {
                    tempRiderData.add(data);
                }
            }
            setData(pi.getRider(), tempRiderData.toArray(new PetData[tempData.size()]), true);
        }

        if (ec.options.getConfig().getBoolean("sendForceMessage", true)) {
            String dataToString = " ";
            if (!tempRiderData.isEmpty()) {
                dataToString = PetUtil.dataToString(tempData);
            } else {
                dataToString = PetUtil.dataToString(tempData, tempRiderData);
            }
            if (dataToString != " ") {
                Lang.sendTo(pi.getOwner(), Lang.DATA_FORCE_MESSAGE.toString().replace("%data%", dataToString));
            }
        }
    }

    public void updateFileData(String type, Pet pet, ArrayList<PetData> list, boolean b) {
        ec.SPH.updateDatabase(pet.getNameOfOwner(), list, b, pet.isRider());
        String w = pet.getOwner().getWorld().getName();
        String path = type + "." + w + "." + pet.getNameOfOwner();
        for (PetData pd : list) {
            ec.getPetConfig().set(path + ".pet.data." + pd.toString().toLowerCase(), b);
        }
        ec.getPetConfig().saveConfig();
    }

    public Pet createPetFromFile(String type, Player p) {
        if (ec.options.getConfig().getBoolean("loadSavedPets", true)) {
            String path = type + "." + p.getName();
            if (ec.getPetConfig().get(path) != null) {
                PetType petType = PetType.valueOf(ec.getPetConfig().getString(path + ".pet.type"));
                String name = ec.getPetConfig().getString(path + ".pet.name");
                if (name.equalsIgnoreCase("") || name == null) {
                    name = petType.getDefaultName(p.getName());
                }
                if (petType == null) return null;
                if (!ec.options.allowPetType(petType)) {
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
                ConfigurationSection cs = ec.getPetConfig().getConfigurationSection(path + ".pet.data");
                if (cs != null) {
                    for (String key : cs.getKeys(false)) {
                        if (EnumUtil.isEnumType(PetData.class, key.toUpperCase())) {
                            PetData pd = PetData.valueOf(key.toUpperCase());
                            data.add(pd);
                        } else {
                            Logger.log(Logger.LogLevel.WARNING, "Error whilst loading data Pet Save Data for " + pi.getNameOfOwner() + ". Unknown enum type: " + key + ".", true);
                        }
                    }
                }

                if (!data.isEmpty()) {
                    setData(pi, data.toArray(new PetData[data.size()]), true);
                }

                this.loadRiderFromFile(type, pi);

                forceAllValidData(pi);
                return pi;
            }
        }
        return null;
    }

    public void loadRiderFromFile(Pet pet) {
        this.loadRiderFromFile("autosave", pet);
    }

    public void loadRiderFromFile(String type, Pet pet) {
        if (pet.getNameOfOwner() != null && pet.getOwner() != null) {
            String path = type + "." + pet.getNameOfOwner();
            if (ec.getPetConfig().get(path + ".rider.type") != null) {
                PetType riderPetType = PetType.valueOf(ec.getPetConfig().getString(path + ".rider.type"));
                String riderName = ec.getPetConfig().getString(path + ".rider.name");
                if (riderName.equalsIgnoreCase("") || riderName == null) {
                    riderName = riderPetType.getDefaultName(pet.getNameOfOwner());
                }
                if (riderPetType == null) return;
                if (ec.options.allowRidersFor(pet.getPetType())) {
                    Pet rider = pet.createRider(riderPetType, true);
                    if (rider != null && rider.getEntityPet() != null) {
                        rider.setPetName(riderName);
                        ArrayList<PetData> riderData = new ArrayList<PetData>();
                        ConfigurationSection mcs = ec.getPetConfig().getConfigurationSection(path + ".rider.data");
                        if (mcs != null) {
                            for (String key : mcs.getKeys(false)) {
                                if (EnumUtil.isEnumType(PetData.class, key.toUpperCase())) {
                                    PetData pd = PetData.valueOf(key.toUpperCase());
                                    riderData.add(pd);
                                } else {
                                    Logger.log(Logger.LogLevel.WARNING, "Error whilst loading data Pet Rider Save Data for " + pet.getNameOfOwner() + ". Unknown enum type: " + key + ".", true);
                                }
                            }
                        }
                        if (!riderData.isEmpty()) {
                            setData(pet, riderData.toArray(new PetData[riderData.size()]), true);
                        }
                    }
                }
            }
        }
    }

    public void removePets(String player, boolean makeDeathSound) {
        Iterator<Pet> i = pets.listIterator();
        while (i.hasNext()) {
            Pet p = i.next();
            if (p.getNameOfOwner().equals(player)) {
                p.removePet(makeDeathSound);
                i.remove();
            }
        }
    }

    public void removePet(Pet pi, boolean makeDeathSound) {
        removePets(pi.getNameOfOwner(), makeDeathSound);
    }

    public void saveFileData(String type, Pet pi) {
        clearFileData(type, pi);
        try {
            String oName = pi.getNameOfOwner();
            String path = type + "." + oName;
            PetType petType = pi.getPetType();

            ec.getPetConfig().set(path + ".pet.type", petType.toString());
            ec.getPetConfig().set(path + ".pet.name", pi.getPetNameWithoutColours());

            for (PetData pd : pi.getPetData()) {
                ec.getPetConfig().set(path + ".pet.data." + pd.toString().toLowerCase(), true);
            }

            if (pi.getRider() != null) {
                PetType riderType = pi.getRider().getPetType();

                ec.getPetConfig().set(path + ".rider.type", riderType.toString());
                ec.getPetConfig().set(path + ".rider.name", pi.getRider().getPetNameWithoutColours());
                for (PetData pd : pi.getRider().getPetData()) {
                    ec.getPetConfig().set(path + ".rider.data." + pd.toString().toLowerCase(), true);
                }
            }
        } catch (Exception e) {
        }
        ec.getPetConfig().saveConfig();
    }

    public void saveFileData(String type, Player p, UnorganisedPetData UPD, UnorganisedPetData UMD) {
        this.saveFileData(type, p.getName(), UPD, UMD);
    }

    public void saveFileData(String type, String name, UnorganisedPetData UPD, UnorganisedPetData UMD) {
        clearFileData(type, name);
        PetType pt = UPD.petType;
        PetData[] data = UPD.petDataList.toArray(new PetData[UPD.petDataList.size()]);
        String petName = UPD.petName;
        if (UPD.petName == null || UPD.petName.equalsIgnoreCase("")) {
            petName = pt.getDefaultName(name);
        }
        PetType riderType = UMD.petType;
        PetData[] riderData = UMD.petDataList.toArray(new PetData[UMD.petDataList.size()]);
        String riderName = UMD.petName;
        if (UMD.petName == null || UMD.petName.equalsIgnoreCase("")) {
            riderName = pt.getDefaultName(name);
        }

        String path = type + "." + name;
        try {
            ec.getPetConfig().set(path + ".pet.type", pt.toString());
            ec.getPetConfig().set(path + ".pet.name", petName);

            for (PetData pd : data) {
                ec.getPetConfig().set(path + ".pet.data." + pd.toString().toLowerCase(), true);
            }

            if (riderData != null && riderType != null) {
                ec.getPetConfig().set(path + ".rider.type", riderType.toString());
                ec.getPetConfig().set(path + ".rider.name", riderName);
                for (PetData pd : riderData) {
                    ec.getPetConfig().set(path + ".rider.data." + pd.toString().toLowerCase(), true);
                }

            }
        } catch (Exception e) {
        }
        ec.getPetConfig().saveConfig();
    }

    public void saveFileData(String type, Player p, UnorganisedPetData UPD) {
        this.saveFileData(type, p.getName(), UPD);
    }

    public void saveFileData(String type, String name, UnorganisedPetData UPD) {
        clearFileData(type, name);
        PetType pt = UPD.petType;
        PetData[] data = UPD.petDataList.toArray(new PetData[UPD.petDataList.size()]);
        String petName = UPD.petName;

        String path = type + "." + name;
        try {
            ec.getPetConfig().set(path + ".pet.type", pt.toString());
            ec.getPetConfig().set(path + ".pet.name", petName);

            for (PetData pd : data) {
                ec.getPetConfig().set(path + ".pet.data." + pd.toString().toLowerCase(), true);
            }

        } catch (Exception e) {
        }
        ec.getPetConfig().saveConfig();
    }

    public void clearAllFileData() {
        for (String key : ec.getPetConfig().getKeys(true)) {
            if (ec.getPetConfig().get(key) != null) {
                ec.getPetConfig().set(key, null);
            }
        }
        ec.getPetConfig().saveConfig();
    }

    public void clearFileData(String type, Pet pi) {
        clearFileData(type, pi.getNameOfOwner());
    }

    public void clearFileData(String type, String pName) {
        String path = type + "." + pName;
        ec.getPetConfig().set(path, null);
        /*if (ec.getPetConfig().get(path + ".pet.type") != null) {
            for (String key : ec.getPetConfig().getConfigurationSection(path).getKeys(false)) {
				for (String key1 : ec.getPetConfig().getConfigurationSection(path + "." + key).getKeys(false)) {
					if (ec.getPetConfig().get(path + "." + key + "." + key1) != null) {
						ec.getPetConfig().set(path + "." + key + "." + key1, null);
					}
				}
				if (ec.getPetConfig().get(path + "." + key) != null) {
					ec.getPetConfig().set(path + "." + key, null);
				}
			}
		}*/
        ec.getPetConfig().saveConfig();
    }

    public void clearFileData(String type, Player p) {
        clearFileData(type, p.getName());
    }

    public void setData(Pet pet, PetData[] data, boolean b) {
        PetType petType = pet.getPetType();
        for (PetData pd : data) {
            setData(pet, pd, b);
        }
    }

    public void setData(Pet pet, PetData pd, boolean b) {
        PetType petType = pet.getPetType();
        if (petType.isDataAllowed(pd)) {
            if (pd == PetData.BABY) {
                if (petType == PetType.ZOMBIE) {
                    ((ZombiePet) pet).setBaby(b);
                } else if (petType == PetType.PIGZOMBIE) {
                    ((PigZombiePet) pet).setBaby(b);
                } else {
                    ((IAgeablePet) pet).setBaby(b);
                }
            }

            if (pd == PetData.POWER) {
                ((CreeperPet) pet).setPowered(b);
            }

            if (pd.isType(PetData.Type.SIZE)) {
                int i = 1;
                if (pd == PetData.MEDIUM) {
                    i = 2;
                } else if (pd == PetData.LARGE) {
                    i = 4;
                }
                if (petType == PetType.SLIME) {
                    ((SlimePet) pet).setSize(i);
                }
                if (petType == PetType.MAGMACUBE) {
                    ((MagmaCubePet) pet).setSize(i);
                }
            }

            if (pd.isType(PetData.Type.CAT) && petType == PetType.OCELOT) {
                try {
                    org.bukkit.entity.Ocelot.Type t = org.bukkit.entity.Ocelot.Type.valueOf(pd.toString() + (pd == PetData.WILD ? "_OCELOT" : "_CAT"));
                    if (t != null) {
                        ((OcelotPet) pet).setCatType(t);
                    }
                } catch (Exception e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Encountered exception whilst attempting to convert PetData to Ocelot.Type.", e, true);
                }
            }

            if (pd == PetData.ANGRY) {
                ((WolfPet) pet).setAngry(b);
            }

            if (pd == PetData.TAMED) {
                ((WolfPet) pet).setTamed(b);
            }

            if (pd.isType(PetData.Type.PROF)) {
                Profession p = Profession.valueOf(pd.toString());
                if (p != null) {
                    ((VillagerPet) pet).setProfession(p);
                }
            }

            if (pd.isType(PetData.Type.COLOUR) && (petType == PetType.SHEEP || petType == PetType.WOLF)) {
                String s = pd == PetData.LIGHTBLUE ? "LIGHT_BLUE" : pd.toString();
                try {
                    DyeColor dc = DyeColor.valueOf(s);
                    if (dc != null) {
                        if (petType == PetType.SHEEP) {
                            ((SheepPet) pet).setColor(dc);
                        } else if (petType == PetType.WOLF) {
                            ((WolfPet) pet).setCollarColor(dc);
                        }
                    }
                } catch (Exception e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Encountered exception whilst attempting to convert PetData to DyeColor.", e, true);
                }
            }

            if (pd == PetData.WITHER) {
                ((SkeletonPet) pet).setWither(b);
            }

            if (pd == PetData.VILLAGER) {
                if (petType == PetType.ZOMBIE) {
                    ((ZombiePet) pet).setVillager(b);
                } else if (petType == PetType.PIGZOMBIE) {
                    ((PigZombiePet) pet).setVillager(b);
                }
            }

            if (pd == PetData.FIRE) {
                ((BlazePet) pet).setOnFire(b);
            }

            if (pd == PetData.SADDLE) {
                if (petType == PetType.PIG) {
                    ((PigPet) pet).setSaddle(b);
                } else if (petType == PetType.HORSE) {
                    ((HorsePet) pet).setSaddled(b);
                }
            }

            if (pd == PetData.SHEARED) {
                ((SheepPet) pet).setSheared(b);
            }

            if (pd == PetData.SCREAMING) {
                ((EndermanPet) pet).setScreaming(b);
            }

            if (pd == PetData.SHIELD) {
                ((WitherPet) pet).setShielded(b);
            }


            if (petType == PetType.HORSE) {
                if (pd == PetData.CHESTED) {
                    ((HorsePet) pet).setChested(b);
                }

                if (pd.isType(PetData.Type.HORSE_TYPE)) {
                    try {
                        HorseType h = HorseType.valueOf(pd.toString());
                        if (h != null) {
                            ((HorsePet) pet).setHorseType(h);
                        }
                    } catch (Exception e) {
                        Logger.log(Logger.LogLevel.WARNING, "Encountered exception whilst attempting to convert PetData to Horse.Type.", e, true);
                    }
                }

                if (pd.isType(PetData.Type.HORSE_VARIANT)) {
                    try {
                        HorseVariant v = HorseVariant.valueOf(pd.toString());
                        if (v != null) {
                            HorseMarking m = ((HorsePet) pet).getMarking();
                            if (m == null) {
                                m = HorseMarking.NONE;
                            }
                            ((HorsePet) pet).setVariant(v, m);
                        }
                    } catch (Exception e) {
                        Logger.log(Logger.LogLevel.WARNING, "Encountered exception whilst attempting to convert PetData to Horse.Variant.", e, true);
                    }
                }

                if (pd.isType(PetData.Type.HORSE_MARKING)) {
                    try {
                        HorseMarking m;
                        if (pd == PetData.WHITEPATCH) {
                            m = HorseMarking.WHITE_PATCH;
                        } else if (pd == PetData.WHITESPOT) {
                            m = HorseMarking.WHITE_SPOTS;
                        } else if (pd == PetData.BLACKSPOT) {
                            m = HorseMarking.BLACK_SPOTS;
                        } else {
                            m = HorseMarking.valueOf(pd.toString());
                        }
                        if (m != null) {
                            HorseVariant v = ((HorsePet) pet).getVariant();
                            if (v == null) {
                                v = HorseVariant.WHITE;
                            }
                            ((HorsePet) pet).setVariant(v, m);
                        }
                    } catch (Exception e) {
                        Logger.log(Logger.LogLevel.WARNING, "Encountered exception whilst attempting to convert PetData to Horse.Marking.", e, true);
                    }
                }

                if (pd.isType(PetData.Type.HORSE_ARMOUR)) {
                    try {
                        HorseArmour a;
                        if (pd == PetData.NOARMOUR) {
                            a = HorseArmour.NONE;
                        } else {
                            a = HorseArmour.valueOf(pd.toString());
                        }
                        if (a != null) {
                            ((HorsePet) pet).setArmour(a);
                        }
                    } catch (Exception e) {
                        Logger.log(Logger.LogLevel.WARNING, "Encountered exception whilst attempting to convert PetData to Horse.Armour.", e, true);
                    }
                }
            }
            ListIterator<PetData> i = pet.getPetData().listIterator();
            while (i.hasNext()) {
                PetData petData = i.next();
                if (petData != pd) {
                    ListIterator<PetData.Type> i2 = pd.getTypes().listIterator();
                    while (i2.hasNext()) {
                        PetData.Type type = i2.next();
                        if (type != PetData.Type.BOOLEAN && petData.isType(type)) {
                            i.remove();
                            break;
                        }
                    }
                }
            }

            if (b) {
                if (!pet.getPetData().contains(pd)) {
                    pet.getPetData().add(pd);
                }
            } else {
                if (pet.getPetData().contains(pd)) {
                    pet.getPetData().remove(pd);
                }
            }
        }
    }
}