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

import com.dsh105.dshutils.logger.ConsoleLogger;
import com.dsh105.dshutils.logger.Logger;
import com.dsh105.dshutils.util.StringUtil;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.PetData;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.PetType;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.pathfinder.PetGoal;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.pathfinder.goals.PetGoalMeleeAttack;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.pathfinder.goals.PetGoalFloat;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.pathfinder.goals.PetGoalFollowOwner;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.pathfinder.goals.PetGoalLookAtPlayer;
import io.github.dsh105.echopet.menu.main.MenuOption;
import io.github.dsh105.echopet.menu.main.PetMenu;
import io.github.dsh105.echopet.menu.selector.SelectorMenu;
import io.github.dsh105.echopet.mysql.SQLPetHandler;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.MenuUtil;
//import net.minecraft.server.v1_7_R2.EntityHuman;
import io.github.dsh105.echopet.util.ReflectionUtil;
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
     * Gives a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} to the specified {@link Player}
     * <p/>
     * Pets will be spawned immediately next to the target player, linked until it is removed.
     *
     * @param player      the {@link org.bukkit.entity.Player} that will be provided with a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}
     * @param petType     the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.PetType} (type of {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}) that will be given to the player
     * @param sendMessage defines if the plugin sends a message to the target {@link Player}
     * @return the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} created
     */
    public Pet givePet(Player player, PetType petType, boolean sendMessage) {
        if (player != null && petType != null) {
            Pet pet = EchoPetPlugin.getInstance().PH.createPet(player, petType, sendMessage);
            if (pet == null) {
                ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to give " + petType.toString() + " to " + player.getName() + " through the EchoPetAPI. Maybe this PetType is disabled in the Config.yml?");
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
     * Removes a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} if the {@link org.bukkit.entity.Player} has one active
     *
     * @param player      the {@link org.bukkit.entity.Player} to remove their {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} from
     * @param sendMessage defines if the plugin sends a message to the target {@link org.bukkit.entity.Player}
     */
    public void removePet(Player player, boolean sendMessage, boolean save) {
        EchoPetPlugin.getInstance().PH.removePets(player.getName(), true);
        if (save) {
            if (hasPet(player)) {
                PetHandler.getInstance().saveFileData("autosave", PetHandler.getInstance().getPet(player));
                SQLPetHandler.getInstance().saveToDatabase(PetHandler.getInstance().getPet(player), false);
            }
        }
        if (sendMessage) {
            Lang.sendTo(player, Lang.REMOVE_PET.toString());
        }
    }

    /**
     * Checks if a {@link org.bukkit.entity.Player} has a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}
     *
     * @param player the {@link org.bukkit.entity.Player} used to check for {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}
     * @return true if {@link org.bukkit.entity.Player} has a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}, false if not
     */
    public boolean hasPet(Player player) {
        return EchoPetPlugin.getInstance().PH.getPet(player) != null;
    }

    /**
     * Gets a {@link org.bukkit.entity.Player}'s {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}
     *
     * @param player the {@link org.bukkit.entity.Player} to get the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} of
     * @return the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} instance linked to the {@link org.bukkit.entity.Player}
     */
    public Pet getPet(Player player) {
        return EchoPetPlugin.getInstance().PH.getPet(player);
    }

    /**
     * Gets all active {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}
     *
     * @return an array of all active {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}s
     */

    public Pet[] getAllPets() {
        ArrayList<Pet> pets = EchoPetPlugin.getInstance().PH.getPets();
        return pets.toArray(new Pet[pets.size()]);
    }

    /**
     * Teleports a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} to a {@link org.bukkit.Location}
     *
     * @param pet      the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} to be teleported
     * @param location the {@link org.bukkit.Location} to teleport the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} to
     * @return success of teleportation
     */
    public boolean teleportPet(Pet pet, Location location) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to teleport Pet to Location through the EchoPetAPI. {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} cannot be null.");
            return false;
        }
        if (pet.isHat() || pet.isOwnerRiding()) {
            return false;
        }
        return pet.getCraftPet().teleport(location);
    }

    /**
     * Save a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} to file or an SQL Database
     *
     * @param pet      {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} to be saved
     * @param saveType whether to save to file or SQL database
     * @return success of save
     */
    public boolean savePet(Pet pet, SaveType saveType) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to save Pet file through the EchoPetAPI. Pet cannot be null.");
            return false;
        }

        if (saveType == SaveType.SQL) {
            SQLPetHandler.getInstance().saveToDatabase(pet, false);
        } else if (saveType == SaveType.FILE) {
            PetHandler.getInstance().saveFileData("autosave", pet);
        }
        return true;
    }

    /**
     * Adds {@link PetData} to a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}
     *
     * @param pet     the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} to add the data to
     * @param petData {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.PetData} to add to the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}
     */
    public void addData(Pet pet, PetData petData) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to add PetData [" + petData.toString() + "] to Pet through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        EchoPetPlugin.getInstance().PH.setData(pet, new PetData[]{petData}, true);
    }

    /**
     * Removes {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.PetData} from a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}
     *
     * @param pet     the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} to remove the data from
     * @param petData {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.PetData} to remove to the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}
     */
    public void removeData(Pet pet, PetData petData) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to remove PetData [" + petData.toString() + "] from Pet through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        EchoPetPlugin.getInstance().PH.setData(pet, new PetData[]{petData}, false);
    }

    /**
     * Checks if a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} has specific {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.PetData}
     *
     * @param pet     the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} to search
     * @param petData the {@link PetData} searched for in the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} instance
     * @return true if the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} has the specified {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.PetData}
     */
    public boolean hasData(Pet pet, PetData petData) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to check PetData [" + petData.toString() + "] of Pet through the EchoPetAPI. Pet cannot be null.");
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
        new SelectorMenu().showTo(player);
        if (sendMessage) {
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
        Pet pet = PetHandler.getInstance().getPet(player);
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
     * Set a target for the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} to attack
     *
     * @param pet    the attacker
     * @param target the {@link org.bukkit.entity.LivingEntity} for the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} to attack
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
        if (pet.getEntityPet().petGoalSelector.getGoal("Attack") != null) {
            pet.getCraftPet().setTarget(target);
        }
    }

    /**
     * Get the {@link org.bukkit.entity.LivingEntity} that a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} is targeting
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
     * Add a predefined {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.pathfinder.PetGoal} to a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} from the API
     *
     * @param pet      the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} to add the goal to
     * @param goalType type of goal
     */
    public void addGoal(Pet pet, GoalType goalType) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to add PetGoal to Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        if (goalType == GoalType.ATTACK) {
            pet.getEntityPet().petGoalSelector.addGoal(new PetGoalMeleeAttack(pet.getEntityPet(), EchoPetPlugin.getInstance().options.getConfig().getDouble("attack.lockRange", 0.0D), EchoPetPlugin.getInstance().options.getConfig().getInt("attack.ticksBetweenAttacks", 20)), 3);
        } else if (goalType == GoalType.FLOAT) {
            pet.getEntityPet().petGoalSelector.addGoal(new PetGoalFloat(pet.getEntityPet()), 0);
        } else if (goalType == GoalType.FOLLOW_OWNER) {
            pet.getEntityPet().petGoalSelector.addGoal(new PetGoalFollowOwner(pet.getEntityPet(), pet.getEntityPet().getSizeCategory().getStartWalk(pet.getPetType()), pet.getEntityPet().getSizeCategory().getStopWalk(pet.getPetType()), pet.getEntityPet().getSizeCategory().getTeleport(pet.getPetType())), 1);
        } else if (goalType == GoalType.LOOK_AT_PLAYER) {
            pet.getEntityPet().petGoalSelector.addGoal(new PetGoalLookAtPlayer(pet.getEntityPet(), ReflectionUtil.getClass("EntityHuman"), 8.0F), 2);
        }
    }

    /**
     * Add an implementation of {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.pathfinder.PetGoal} to a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}
     *
     * @param pet        the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} to add the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.pathfinder.PetGoal} to
     * @param goal       the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.pathfinder.PetGoal} to add
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
        pet.getEntityPet().petGoalSelector.addGoal(identifier, goal, priority);
    }

    /**
     * Remove a predefined goal from a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}'s AI
     *
     * @param pet      {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} to remove the goal from
     * @param goalType type of goal
     */
    public void removeGoal(Pet pet, GoalType goalType) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to remove PetGoal from Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        pet.getEntityPet().petGoalSelector.removeGoal(goalType.getGoalString());
    }

    /**
     * Remove a goal from a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}'s AI
     * <p/>
     * The goal is identified using a string, initiated when the goal is added to the {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}
     *
     * @param pet        {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} to remove the goal from
     * @param identifier String that identifies a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.pathfinder.PetGoal}
     */
    public void removeGoal(Pet pet, String identifier) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to remove PetGoal from Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        pet.getEntityPet().petGoalSelector.removeGoal(identifier);
    }

    /**
     * Remove a goal from a {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}'s AI
     *
     * @param pet     {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet} to remove the goal from
     * @param petGoal {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.pathfinder.PetGoal} to remove
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
        pet.getEntityPet().petGoalSelector.removeGoal(petGoal);
    }

    /**
     * {@link Enum} of predefined {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.pathfinder.PetGoal}s
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
     * Types used for saving {@link io.github.dsh105.echopet.nms.v1_7_R2.entity.Pet}s
     */
    public enum SaveType {
        SQL, FILE;
    }
}

