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

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.compat.api.ai.*;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.reflection.SafeConstructor;
import com.dsh105.echopet.compat.api.util.Lang;
import com.dsh105.echopet.compat.api.util.MenuUtil;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import com.dsh105.echopet.compat.api.util.menu.MenuOption;
import com.dsh105.echopet.compat.api.util.menu.PetMenu;
import com.dsh105.echopet.compat.api.util.menu.SelectorLayout;

public class EchoPetAPI {

    private static EchoPetAPI instance;

    public static EchoPetAPI getAPI() {
        if (instance == null) {
            instance = new EchoPetAPI();
        }
        return instance;
    }

    /**
     * Gives a {@link com.dsh105.echopet.api.pet.Pet} to the specified {@link Player}
     * <p/>
     * Pets will be spawned immediately next to the target player, linked until it is removed.
     *
     * @param player      the {@link org.bukkit.entity.Player} that will be provided with a {@link
     *                    com.dsh105.echopet.api.pet.Pet}
     * @param petType     the {@link com.dsh105.echopet.compat.api.entity.PetType} (type of {@link
     *                    com.dsh105.echopet.api.pet.Pet}) that will be given to the player
     * @param sendMessage defines if the plugin sends a message to the target {@link Player}
     * @return the {@link com.dsh105.echopet.api.pet.Pet} created
     */
    public IPet givePet(Player player, PetType petType, boolean sendMessage) {
        if (player != null && petType != null) {
            IPet pet = EchoPet.getManager().createPet(player, petType, sendMessage);
            if (pet == null) {
                EchoPet.LOG.severe("Failed to give " + petType.toString() + " to " + player.getName() + " through the EchoPetAPI. Maybe this PetType is disabled in the Config.yml?");
                return null;
            }
            if (sendMessage) {
                Lang.sendTo(player, Lang.CREATE_PET.toString().replace("%type%", StringUtil.capitalise(petType.toString())));
            }
            return pet;
        }
        return null;
    }

    /**
     * Removes a {@link com.dsh105.echopet.api.pet.Pet} if the {@link org.bukkit.entity.Player} has one active
     *
     * @param player      the {@link org.bukkit.entity.Player} to remove their {@link com.dsh105.echopet.api.pet.Pet}
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
     * Checks if a {@link org.bukkit.entity.Player} has a {@link com.dsh105.echopet.api.pet.Pet}
     *
     * @param player the {@link org.bukkit.entity.Player} used to check for {@link com.dsh105.echopet.api.pet.Pet}
     * @return true if {@link org.bukkit.entity.Player} has a {@link com.dsh105.echopet.api.pet.Pet}, false if not
     */
    public boolean hasPet(Player player) {
        return EchoPet.getManager().getPet(player) != null;
    }

    /**
     * Gets a {@link org.bukkit.entity.Player}'s {@link com.dsh105.echopet.api.pet.Pet}
     *
     * @param player the {@link org.bukkit.entity.Player} to get the {@link com.dsh105.echopet.api.pet.Pet} of
     * @return the {@link com.dsh105.echopet.api.pet.Pet} instance linked to the {@link org.bukkit.entity.Player}
     */
    public IPet getPet(Player player) {
        return EchoPet.getManager().getPet(player);
    }

    /**
     * Gets all active {@link com.dsh105.echopet.api.pet.Pet}
     *
     * @return an array of all active {@link com.dsh105.echopet.api.pet.Pet}s
     */

    public IPet[] getAllPets() {
        ArrayList<IPet> pets = EchoPet.getManager().getPets();
        return pets.toArray(new IPet[pets.size()]);
    }

    /**
     * Teleports a {@link com.dsh105.echopet.api.pet.Pet} to a {@link org.bukkit.Location}
     *
     * @param pet      the {@link com.dsh105.echopet.api.pet.Pet} to be teleported
     * @param location the {@link org.bukkit.Location} to teleport the {@link com.dsh105.echopet.api.pet.Pet} to
     * @return success of teleportation
     */
    public boolean teleportPet(IPet pet, Location location) {
        if (pet == null) {
            EchoPet.LOG.severe("Failed to teleport Pet to Location through the EchoPetAPI. {@link com.dsh105.echopet.api.pet.Pet} cannot be null.");
            return false;
        }
        if (pet.isHat() || pet.isOwnerRiding()) {
            return false;
        }
        return pet.teleport(location);
    }

    /**
     * Save a {@link com.dsh105.echopet.api.pet.Pet} to file or an SQL Database
     *
     * @param pet      {@link com.dsh105.echopet.api.pet.Pet} to be saved
     * @param saveType whether to save to file or SQL database
     * @return success of save
     */
    public boolean savePet(IPet pet, SaveType saveType) {
        if (pet == null) {
            EchoPet.LOG.severe("Failed to save Pet file through the EchoPetAPI. Pet cannot be null.");
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
     * Adds {@link com.dsh105.echopet.compat.api.entity.PetData} to a {@link com.dsh105.echopet.api.pet.Pet}
     *
     * @param pet     the {@link com.dsh105.echopet.api.pet.Pet} to add the data to
     * @param petData {@link com.dsh105.echopet.compat.api.entity.PetData} to add to the {@link
     *                com.dsh105.echopet.api.pet.Pet}
     */
    public void addData(IPet pet, PetData petData) {
        if (pet == null) {
            EchoPet.LOG.severe("Failed to add PetData [" + petData.toString() + "] to Pet through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        EchoPet.getManager().setData(pet, new PetData[]{petData}, true);
    }

    /**
     * Removes {@link PetData} from a {@link com.dsh105.echopet.api.pet.Pet}
     *
     * @param pet     the {@link com.dsh105.echopet.api.pet.Pet} to remove the data from
     * @param petData {@link PetData} to remove to the {@link com.dsh105.echopet.api.pet.Pet}
     */
    public void removeData(IPet pet, PetData petData) {
        if (pet == null) {
            EchoPet.LOG.severe("Failed to remove PetData [" + petData.toString() + "] from Pet through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        EchoPet.getManager().setData(pet, new PetData[]{petData}, false);
    }

    /**
     * Checks if a {@link com.dsh105.echopet.api.pet.Pet} has specific {@link PetData}
     *
     * @param pet     the {@link com.dsh105.echopet.api.pet.Pet} to search
     * @param petData the {@link PetData} searched for in the {@link com.dsh105.echopet.api.pet.Pet} instance
     * @return true if the {@link com.dsh105.echopet.api.pet.Pet} has the specified {@link PetData}
     */
    public boolean hasData(IPet pet, PetData petData) {
        if (pet == null) {
            EchoPet.LOG.severe("Failed to check PetData [" + petData.toString() + "] of Pet through the EchoPetAPI. Pet cannot be null.");
            return false;
        }
        return pet.getPetData().contains(petData);
    }

    /**
     * Opens the Pet Selector GUI Menu
     *
     * @param player      {@link org.bukkit.entity.Player} to view the Menu
     * @param sendMessage defines if the plugin sends a message to the target {@link org.bukkit.entity.Player}
     */
    public void openPetSelector(Player player, boolean sendMessage) {
        SelectorLayout.getSelectorMenu().showTo(player);
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
        IPet pet = EchoPet.getManager().getPet(player);
        if (pet == null) {
            return;
        }
        ArrayList<MenuOption> options = MenuUtil.createOptionList(pet.getPetType());
        PetMenu menu = new PetMenu(pet, options, pet.getPetType() == PetType.HORSE ? 18 : 9);
        menu.open(sendMessage);
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
     * Set a target for the {@link com.dsh105.echopet.api.pet.Pet} to attack
     *
     * @param pet    the attacker
     * @param target the {@link org.bukkit.entity.LivingEntity} for the {@link com.dsh105.echopet.api.pet.Pet} to
     *               attack
     */
    public void setAttackTarget(IPet pet, LivingEntity target) {
        if (pet == null) {
            EchoPet.LOG.severe("Failed to set attack target for Pet through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        if (target == null) {
            EchoPet.LOG.severe("Failed to set attack target for Pet through the EchoPetAPI. Target cannot be null.");
            return;
        }
        if (pet.getEntityPet().getPetGoalSelector().getGoal("Attack") != null) {
            pet.getCraftPet().setTarget(target);
        }
    }

    /**
     * Get the {@link org.bukkit.entity.LivingEntity} that a {@link com.dsh105.echopet.api.pet.Pet} is targeting
     *
     * @param pet the attacker
     * @return {@link org.bukkit.entity.LivingEntity} being attacked, null if none
     */
    public LivingEntity getAttackTarget(IPet pet) {
        if (pet == null) {
            EchoPet.LOG.severe("Failed to get attack target for Pet through the EchoPetAPI. Pet cannot be null.");
        }
        return pet.getCraftPet().getTarget();
    }

    /**
     * Add a predefined {@link com.dsh105.echopet.compat.api.ai.PetGoal} to a {@link com.dsh105.echopet.api.pet.Pet}
     * from the API
     *
     * @param pet      the {@link com.dsh105.echopet.api.pet.Pet} to add the goal to
     * @param goalType type of goal
     */
    public void addGoal(IPet pet, GoalType goalType) {
        if (pet == null) {
            EchoPet.LOG.severe("Failed to add PetGoal to Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        if (goalType == GoalType.ATTACK) {
            pet.getEntityPet().getPetGoalSelector().addGoal(new SafeConstructor<APetGoalMeleeAttack>(ReflectionUtil.getVersionedClass("entity.ai.PetGoalMeleeAttack"), ReflectionUtil.getVersionedClass("entity.EntityPet"), double.class, int.class).newInstance(pet.getEntityPet(), EchoPet.getConfig().getDouble("attack.lockRange", 0.0D), EchoPet.getConfig().getInt("attack.ticksBetweenAttacks", 20)), 3);
        } else if (goalType == GoalType.FLOAT) {
            pet.getEntityPet().getPetGoalSelector().addGoal(new SafeConstructor<APetGoalFloat>(ReflectionUtil.getVersionedClass("entity.ai.PetGoalFloat"), ReflectionUtil.getVersionedClass("entity.EntityPet")).newInstance(pet.getEntityPet()), 0);
        } else if (goalType == GoalType.FOLLOW_OWNER) {
            pet.getEntityPet().getPetGoalSelector().addGoal(new SafeConstructor<APetGoalFollowOwner>(ReflectionUtil.getVersionedClass("entity.ai.PetGoalFollowOwner"), ReflectionUtil.getVersionedClass("entity.EntityPet"), double.class, double.class, double.class).newInstance(pet.getEntityPet(), pet.getEntityPet().getSizeCategory().getStartWalk(pet.getPetType()), pet.getEntityPet().getSizeCategory().getStopWalk(pet.getPetType()), pet.getEntityPet().getSizeCategory().getTeleport(pet.getPetType())), 1);
        } else if (goalType == GoalType.LOOK_AT_PLAYER) {
            pet.getEntityPet().getPetGoalSelector().addGoal(new SafeConstructor<APetGoalLookAtPlayer>(ReflectionUtil.getVersionedClass("entity.ai.PetGoalLookAtPlayer"), ReflectionUtil.getVersionedClass("entity.EntityPet"), Class.class, float.class).newInstance(pet.getEntityPet(), ReflectionUtil.getNMSClass("EntityHuman"), 8.0F), 2);
        }
    }

    /**
     * Add an implementation of {@link com.dsh105.echopet.compat.api.ai.PetGoal} to a {@link
     * com.dsh105.echopet.api.pet.Pet}
     *
     * @param pet        the {@link com.dsh105.echopet.api.pet.Pet} to add the {@link com.dsh105.echopet.compat.api.ai.PetGoal}
     *                   to
     * @param goal       the {@link com.dsh105.echopet.compat.api.ai.PetGoal} to add
     * @param identifier a {@link java.lang.String} to identify the goal
     */
    public void addGoal(IPet pet, PetGoal goal, String identifier, int priority) {
        if (pet == null) {
            EchoPet.LOG.severe("Failed to add PetGoal to Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        if (goal == null) {
            EchoPet.LOG.severe("Failed to add PetGoal to Pet AI through the EchoPetAPI. Goal cannot be null.");
            return;
        }
        pet.getEntityPet().getPetGoalSelector().addGoal(identifier, goal, priority);
    }

    /**
     * Remove a predefined goal from a {@link com.dsh105.echopet.api.pet.Pet}'s AI
     *
     * @param pet      {@link com.dsh105.echopet.api.pet.Pet} to remove the goal from
     * @param goalType type of goal
     */
    public void removeGoal(IPet pet, GoalType goalType) {
        if (pet == null) {
            EchoPet.LOG.severe("Failed to remove PetGoal from Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        pet.getEntityPet().getPetGoalSelector().removeGoal(goalType.getGoalString());
    }

    /**
     * Remove a goal from a {@link com.dsh105.echopet.api.pet.Pet}'s AI
     * <p/>
     * The goal is identified using a string, initiated when the goal is added to the {@link
     * com.dsh105.echopet.api.pet.Pet}
     *
     * @param pet        {@link com.dsh105.echopet.api.pet.Pet} to remove the goal from
     * @param identifier String that identifies a {@link com.dsh105.echopet.compat.api.ai.PetGoal}
     */
    public void removeGoal(IPet pet, String identifier) {
        if (pet == null) {
            EchoPet.LOG.severe("Failed to remove PetGoal from Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        pet.getEntityPet().getPetGoalSelector().removeGoal(identifier);
    }

    /**
     * Remove a goal from a {@link com.dsh105.echopet.api.pet.Pet}'s AI
     *
     * @param pet     {@link com.dsh105.echopet.api.pet.Pet} to remove the goal from
     * @param petGoal {@link com.dsh105.echopet.compat.api.ai.PetGoal} to remove
     */
    public void removeGoal(IPet pet, PetGoal petGoal) {
        if (pet == null) {
            EchoPet.LOG.severe("Failed to remove PetGoal from Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        if (petGoal == null) {
            EchoPet.LOG.severe("Failed to remove PetGoal from Pet AI through the EchoPetAPI. Goal cannot be null.");
            return;
        }
        pet.getEntityPet().getPetGoalSelector().removeGoal(petGoal);
    }

    /**
     * {@link Enum} of predefined {@link com.dsh105.echopet.compat.api.ai.PetGoal}s
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
     * Types used for saving {@link com.dsh105.echopet.api.pet.Pet}s
     */
    public enum SaveType {
        SQL, FILE
    }
}

