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

import com.dsh105.echopet.api.ai.*;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.inventory.DataMenu;
import com.dsh105.echopet.api.inventory.PetSelector;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.reflection.SafeConstructor;
import com.dsh105.echopet.util.ReflectionUtil;
import com.dsh105.echopetv3.api.config.Lang;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class EchoPetAPI {

    private static EchoPetAPI instance;

    public static EchoPetAPI getAPI() {
        if (instance == null) {
            instance = new EchoPetAPI();
        }
        return instance;
    }

    /**
     * Gives a {@link src.main.java.com.dsh105.echopet.api.pet.Pet} to the specified {@link Player}
     * <p/>
     * Pets will be spawned immediately next to the target player, linked until it is removed.
     *
     * @param player      the {@link org.bukkit.entity.Player} that will be provided with a {@link
     *                    src.main.java.com.dsh105.echopet.api.pet.Pet}
     * @param petType     the {@link com.dsh105.echopet.api.entity.PetType} (type of {@link
     *                    src.main.java.com.dsh105.echopet.api.pet.Pet}) that will be given to the player
     * @param sendMessage defines if the plugin sends a message to the target {@link Player}
     * @return the {@link src.main.java.com.dsh105.echopet.api.pet.Pet} created
     */
    public Pet givePet(Player player, PetType petType, boolean sendMessage) {
        if (player != null && petType != null) {
            Pet pet = EchoPet.getManager().createPet(player, petType, sendMessage);
            if (pet == null) {
                ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to give " + petType.humanName() + " to " + player.getName() + " through the EchoPetAPI. Maybe this PetType is disabled in the Config.yml?");
                return null;
            }
            if (sendMessage) {
                Lang.sendTo(player, Lang.CREATE_PET.toString().replace("%type%", petType.humanName()));
            }
            return pet;
        }
        return null;
    }

    /**
     * Removes a {@link src.main.java.com.dsh105.echopet.api.pet.Pet} if the {@link org.bukkit.entity.Player} has one
     * active
     *
     * @param player      the {@link org.bukkit.entity.Player} to remove their {@link src.main.java.com.dsh105.echopet.api.pet.Pet}
     *                    from
     * @param sendMessage defines if the plugin sends a message to the target {@link org.bukkit.entity.Player}
     */
    public void removePet(Player player, boolean sendMessage, boolean save) {
        EchoPet.getManager().removePets(player, true);
        if (save) {
            if (hasPet(player)) {
                EchoPet.getManager().saveFileData("autosave", EchoPet.getManager().getPet(player));
                EchoPet.getSqlManager().saveToDatabase(EchoPet.getManager().getPet(player), false);
            }
        }
        if (sendMessage) {
            Lang.sendTo(player, Lang.REMOVE_PET.toString());
        }
    }

    /**
     * Checks if a {@link org.bukkit.entity.Player} has a {@link src.main.java.com.dsh105.echopet.api.pet.Pet}
     *
     * @param player the {@link org.bukkit.entity.Player} used to check for {@link src.main.java.com.dsh105.echopet.api.pet.Pet}
     * @return true if {@link org.bukkit.entity.Player} has a {@link src.main.java.com.dsh105.echopet.api.pet.Pet},
     * false if not
     */
    public boolean hasPet(Player player) {
        return EchoPet.getManager().getPet(player) != null;
    }

    /**
     * Gets a {@link org.bukkit.entity.Player}'s {@link src.main.java.com.dsh105.echopet.api.pet.Pet}
     *
     * @param player the {@link org.bukkit.entity.Player} to get the {@link src.main.java.com.dsh105.echopet.api.pet.Pet}
     *               of
     * @return the {@link src.main.java.com.dsh105.echopet.api.pet.Pet} instance linked to the {@link
     * org.bukkit.entity.Player}
     */
    public Pet getPet(Player player) {
        return EchoPet.getManager().getPet(player);
    }

    /**
     * Gets all active {@link src.main.java.com.dsh105.echopet.api.pet.Pet}
     *
     * @return an array of all active {@link src.main.java.com.dsh105.echopet.api.pet.Pet}s
     */

    public Pet[] getAllPets() {
        ArrayList<Pet> pets = EchoPet.getManager().getPets();
        return pets.toArray(new Pet[pets.size()]);
    }

    /**
     * Teleports a {@link src.main.java.com.dsh105.echopet.api.pet.Pet} to a {@link org.bukkit.Location}
     *
     * @param pet      the {@link src.main.java.com.dsh105.echopet.api.pet.Pet} to be teleported
     * @param location the {@link org.bukkit.Location} to teleport the {@link src.main.java.com.dsh105.echopet.api.pet.Pet}
     *                 to
     * @return success of teleportation
     */
    public boolean teleportPet(Pet pet, Location location) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to teleport Pet to Location through the EchoPetAPI. {@link com.dsh105.echopet.api.entity.pet.Pet} cannot be null.");
            return false;
        }
        if (pet.isHat() || pet.isOwnerRiding()) {
            return false;
        }
        return pet.teleport(location);
    }

    /**
     * Save a {@link src.main.java.com.dsh105.echopet.api.pet.Pet} to file or an SQL Database
     *
     * @param pet      {@link src.main.java.com.dsh105.echopet.api.pet.Pet} to be saved
     * @param saveType whether to save to file or SQL database
     * @return success of save
     */
    public boolean savePet(Pet pet, SaveType saveType) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to save Pet file through the EchoPetAPI. Pet cannot be null.");
            return false;
        }

        if (saveType == SaveType.SQL) {
            EchoPet.getSqlManager().saveToDatabase(pet, false);
        } else if (saveType == SaveType.FILE) {
            EchoPet.getManager().saveFileData("autosave", pet);
        }
        return true;
    }

    /**
     * Adds {@link com.dsh105.echopet.api.entity.PetData} to a {@link src.main.java.com.dsh105.echopet.api.pet.Pet}
     *
     * @param pet     the {@link src.main.java.com.dsh105.echopet.api.pet.Pet} to add the data to
     * @param petData {@link com.dsh105.echopet.api.entity.PetData} to add to the {@link
     *                src.main.java.com.dsh105.echopet.api.pet.Pet}
     */
    public void addData(Pet pet, PetData petData) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to add PetData [" + petData.toString() + "] to Pet through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        pet.setDataValue(petData);
    }

    /**
     * Removes {@link PetData} from a {@link src.main.java.com.dsh105.echopet.api.pet.Pet}
     *
     * @param pet     the {@link src.main.java.com.dsh105.echopet.api.pet.Pet} to remove the data from
     * @param petData {@link PetData} to remove to the {@link src.main.java.com.dsh105.echopet.api.pet.Pet}
     */
    public void removeData(Pet pet, PetData petData) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to remove PetData [" + petData.toString() + "] from Pet through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        pet.setDataValue(false, petData);
        EchoPet.getManager().setData(pet, new PetData[]{petData}, false);
    }

    /**
     * Checks if a {@link src.main.java.com.dsh105.echopet.api.pet.Pet} has specific {@link PetData}
     *
     * @param pet     the {@link src.main.java.com.dsh105.echopet.api.pet.Pet} to search
     * @param petData the {@link PetData} searched for in the {@link src.main.java.com.dsh105.echopet.api.pet.Pet}
     *                instance
     * @return true if the {@link src.main.java.com.dsh105.echopet.api.pet.Pet} has the specified {@link PetData}
     */
    public boolean hasData(Pet pet, PetData petData) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to check PetData [" + petData.toString() + "] of Pet through the EchoPetAPI. Pet cannot be null.");
            return false;
        }
        return pet.getac().contains(petData);
    }

    /**
     * Opens the Pet Selector GUI Menu
     *
     * @param player      {@link org.bukkit.entity.Player} to view the Menu
     * @param sendMessage defines if the plugin sends a message to the target {@link org.bukkit.entity.Player}
     */
    public void openPetSelector(Player player, boolean sendMessage) {
        PetSelector.prepare().show(player);
        if (false) {
            Lang.sendTo(player, Lang.OPEN_SELECTOR.toString());
        }
    }

    /**
     * Opens the Pet Selector GUI Menu
     *
     * @param player {@link org.bukkit.entity.Player} to view the menu
     */
    public void openPetSelector(Player player) {
        this.openPetSelector(player, false);
    }

    /**
     * Opens the Pet Data GUI Menu
     *
     * @param player      {@link org.bukkit.entity.Player} to view the Menu
     * @param sendMessage defines if the plugin sends a message to the target {@link org.bukkit.entity.Player}
     */
    public void openPetDataMenu(Player player, boolean sendMessage) {
        Pet pet = EchoPet.getManager().getPet(player);
        if (pet == null) {
            return;
        }
        DataMenu.prepare(pet).show(player);
    }

    /**
     * Opens the Pet Data GUI Menu
     *
     * @param player {@link org.bukkit.entity.Player} to view the menu
     */
    public void openPetDataMenu(Player player) {
        this.openPetDataMenu(player, false);
    }

    /**
     * Set a target for the {@link src.main.java.com.dsh105.echopet.api.pet.Pet} to attack
     *
     * @param pet    the attacker
     * @param target the {@link org.bukkit.entity.LivingEntity} for the {@link src.main.java.com.dsh105.echopet.api.pet.Pet}
     *               to attack
     */
    public void setAttackTarget(Pet pet, LivingEntity target) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to set attack target for Pet through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        if (target == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to set attack target for Pet through the EchoPetAPI. Target cannot be null.");
            return;
        }
        if (pet.getEntityPet().getPetGoalSelector().getGoal("Attack") != null) {
            pet.getCraftPet().setTarget(target);
        }
    }

    /**
     * Get the {@link org.bukkit.entity.LivingEntity} that a {@link src.main.java.com.dsh105.echopet.api.pet.Pet} is
     * targeting
     *
     * @param pet the attacker
     * @return {@link org.bukkit.entity.LivingEntity} being attacked, null if none
     */
    public LivingEntity getAttackTarget(Pet pet) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to get attack target for Pet through the EchoPetAPI. Pet cannot be null.");
        }
        return pet.getCraftPet().getTarget();
    }

    /**
     * Add a predefined {@link com.dsh105.echopet.api.ai.PetGoal} to a {@link
     * src.main.java.com.dsh105.echopet.api.pet.Pet} from the API
     *
     * @param pet      the {@link src.main.java.com.dsh105.echopet.api.pet.Pet} to add the goal to
     * @param goalType type of goal
     */
    public void addGoal(Pet pet, GoalType goalType) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to add PetGoal to Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        if (goalType == GoalType.ATTACK) {
            pet.getEntityPet().getPetGoalSelector().addGoal(new SafeConstructor<PetGoalMeleeAttack>(ReflectionUtil.getVersionedClass("ai.PetGoalMeleeAttackImpl"), ReflectionUtil.getVersionedClass("entity.EntityPet"), double.class, int.class).newInstance(pet.getEntityPet(), EchoPet.getConfig().getDouble("attack.lockRange", 0.0D), EchoPet.getConfig().getInt("attack.ticksBetweenAttacks", 20)), 3);
        } else if (goalType == GoalType.FLOAT) {
            pet.getEntityPet().getPetGoalSelector().addGoal(new SafeConstructor<PetGoalFloat>(ReflectionUtil.getVersionedClass("ai.PetGoalFloatImpl"), ReflectionUtil.getVersionedClass("entity.EntityPet")).newInstance(pet.getEntityPet()), 0);
        } else if (goalType == GoalType.FOLLOW_OWNER) {
            pet.getEntityPet().getPetGoalSelector().addGoal(new SafeConstructor<PetGoalFollowOwner>(ReflectionUtil.getVersionedClass("ai.PetGoalFollowOwnerImpl"), ReflectionUtil.getVersionedClass("entity.EntityPet"), double.class, double.class, double.class).newInstance(pet.getEntityPet(), pet.getEntityPet().getSizeCategory().startFollowDistance(pet.getPetType()), pet.getEntityPet().getSizeCategory().stopFollowDistance(pet.getPetType()), pet.getEntityPet().getSizeCategory().teleportDistance(pet.getPetType())), 1);
        } else if (goalType == GoalType.LOOK_AT_PLAYER) {
            pet.getEntityPet().getPetGoalSelector().addGoal(new SafeConstructor<PetGoalLookAtPlayer>(ReflectionUtil.getVersionedClass("ai.PetGoalLookAtPlayerImpl"), ReflectionUtil.getVersionedClass("entity.EntityPet"), Class.class, float.class).newInstance(pet.getEntityPet(), ReflectionUtil.getNMSClass("EntityHuman"), 8.0F), 2);
        }
    }

    /**
     * Add an implementation of {@link com.dsh105.echopet.api.ai.PetGoal} to a {@link
     * src.main.java.com.dsh105.echopet.api.pet.Pet}
     *
     * @param pet        the {@link src.main.java.com.dsh105.echopet.api.pet.Pet} to add the {@link
     *                   com.dsh105.echopet.api.ai.PetGoal} to
     * @param goal       the {@link com.dsh105.echopet.api.ai.PetGoal} to add
     * @param identifier a {@link java.lang.String} to identify the goal
     */
    public void addGoal(Pet pet, PetGoal goal, String identifier, int priority) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to add PetGoal to Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        if (goal == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to ad PetGoal to Pet AI through the EchoPetAPI. Goal cannot be null.");
            return;
        }
        pet.getEntityPet().getPetGoalSelector().addGoal(identifier, goal, priority);
    }

    /**
     * Remove a predefined goal from a {@link src.main.java.com.dsh105.echopet.api.pet.Pet}'s AI
     *
     * @param pet      {@link src.main.java.com.dsh105.echopet.api.pet.Pet} to remove the goal from
     * @param goalType type of goal
     */
    public void removeGoal(Pet pet, GoalType goalType) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to remove PetGoal from Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        pet.getEntityPet().getPetGoalSelector().removeGoal(goalType.getGoalString());
    }

    /**
     * Remove a goal from a {@link src.main.java.com.dsh105.echopet.api.pet.Pet}'s AI
     * <p/>
     * The goal is identified using a string, initiated when the goal is added to the {@link
     * src.main.java.com.dsh105.echopet.api.pet.Pet}
     *
     * @param pet        {@link src.main.java.com.dsh105.echopet.api.pet.Pet} to remove the goal from
     * @param identifier String that identifies a {@link com.dsh105.echopet.api.ai.PetGoal}
     */
    public void removeGoal(Pet pet, String identifier) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to remove PetGoal from Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        pet.getEntityPet().getPetGoalSelector().removeGoal(identifier);
    }

    /**
     * Remove a goal from a {@link src.main.java.com.dsh105.echopet.api.pet.Pet}'s AI
     *
     * @param pet     {@link src.main.java.com.dsh105.echopet.api.pet.Pet} to remove the goal from
     * @param petGoal {@link com.dsh105.echopet.api.ai.PetGoal} to remove
     */
    public void removeGoal(Pet pet, PetGoal petGoal) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to remove PetGoal from Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        if (petGoal == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to remove PetGoal from Pet AI through the EchoPetAPI. Goal cannot be null.");
            return;
        }
        pet.getEntityPet().getPetGoalSelector().removeGoal(petGoal);
    }

    /**
     * {@link Enum} of predefined {@link com.dsh105.echopet.api.ai.PetGoal}s
     */
    public enum GoalType {
        ATTACK("Attack"),
        FLOAT("Float"),
        FOLLOW_OWNER("Float"),
        LOOK_AT_PLAYER("LookAtPlayer");

        private String goalString;

        GoalType(String goalString) {
            this.goalString = goalString;
        }

        public String getGoalString() {
            return this.goalString;
        }
    }

    /**
     * Types used for saving {@link src.main.java.com.dsh105.echopet.api.pet.Pet}s
     */
    public enum SaveType {
        SQL, FILE
    }
}

