package io.github.dsh105.echopet.api;

import io.github.dsh105.echopet.EchoPet;
import io.github.dsh105.echopet.data.PetData;
import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.data.PetType;
import io.github.dsh105.echopet.entity.pathfinder.PetGoal;
import io.github.dsh105.echopet.entity.pathfinder.goals.PetGoalAttack;
import io.github.dsh105.echopet.entity.pathfinder.goals.PetGoalFloat;
import io.github.dsh105.echopet.entity.pathfinder.goals.PetGoalFollowOwner;
import io.github.dsh105.echopet.entity.pathfinder.goals.PetGoalLookAtPlayer;
import io.github.dsh105.echopet.entity.pet.Pet;
import io.github.dsh105.echopet.logger.ConsoleLogger;
import io.github.dsh105.echopet.logger.Logger;
import io.github.dsh105.echopet.menu.main.MenuOption;
import io.github.dsh105.echopet.menu.main.PetMenu;
import io.github.dsh105.echopet.menu.selector.PetSelector;
import io.github.dsh105.echopet.mysql.SQLPetHandler;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.MenuUtil;
import io.github.dsh105.echopet.util.StringUtil;
import net.minecraft.server.v1_6_R3.EntityHuman;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class EchoPetAPI {

    /**
     * Gives a {@link Pet} to the specified {@link Player}
     * <p/>
     * Pets will be spawned immediately next to the target player, linked until it is removed.
     *
     * @param player      the {@link Player} that will be provided with a Pet
     * @param petType     the {@link PetType} (type of Pet) that will be given to the player
     * @param sendMessage defines if the plugin sends a message to the target {@link Player}
     * @return the Pet created
     */
    public Pet givePet(Player player, PetType petType, boolean sendMessage) {
        if (player != null && petType != null) {
            Pet pet = EchoPet.getPluginInstance().PH.createPet(player, petType, sendMessage);
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
     * Removes a {@link Pet} if the {@link Player} has one active
     *
     * @param player      the {@link Player} to remove their Pet from
     * @param sendMessage defines if the plugin sends a message to the target {@link Player}
     */
    public void removePet(Player player, boolean sendMessage, boolean save) {
        EchoPet.getPluginInstance().PH.removePets(player);
        if (save) {
            if (hasPet(player)) {
                PetHandler.getInstance().saveFileData("autosave", PetHandler.getInstance().getPet(player));
                SQLPetHandler.getInstance().saveToDatabase(PetHandler.getInstance().getPet(player), false);
            }
        }
    }

    /**
     * Checks if a {@link Player} has a {@link Pet}
     *
     * @param player the {@link Player} used to check for Pet
     * @return true if {@link Player} has a Pet, false if not
     */
    public boolean hasPet(Player player) {
        return EchoPet.getPluginInstance().PH.getPet(player) != null;
    }

    /**
     * Gets a {@link Player}'s {@link Pet}
     *
     * @param player the {@link Player} to get the Pet of
     * @return the {@link Pet} instance linked to the {@link Player}
     */
    public Pet getPet(Player player) {
        return EchoPet.getPluginInstance().PH.getPet(player);
    }

    /**
     * Gets all active {@link Pet}
     *
     * @return an array of all active {@link Pet}s
     */

    public Pet[] getAllPets() {
        ArrayList<Pet> pets = EchoPet.getPluginInstance().PH.getPets();
        return pets.toArray(new Pet[pets.size()]);
    }

    /**
     * Teleports a {@link Pet} to a {@link Location}
     *
     * @param pet      the {@link Pet} to be teleported
     * @param location the {@link Location} to teleport the {@link Pet} to
     * @return success of teleportation
     */
    public boolean teleportPet(Pet pet, Location location) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to teleport Pet to Location through the EchoPetAPI. Pet cannot be null.");
            return false;
        }
        if (pet.isPetHat() || pet.isOwnerRiding()) {
            return false;
        }
        return pet.getCraftPet().teleport(location);
    }

    /**
     * Adds {@link PetData} to a {@link Pet}
     *
     * @param pet     the {@link Pet} to add the data to
     * @param petData {@link PetData} to add to the {@link Pet}
     */
    public void addData(Pet pet, PetData petData) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to add PetData [" + petData.toString() + "] to Pet through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        EchoPet.getPluginInstance().PH.setData(pet, new PetData[]{petData}, true);
    }

    /**
     * Removes {@link PetData} from a {@link Pet}
     *
     * @param pet     the {@link Pet} to remove the data from
     * @param petData {@link PetData} to remove to the {@link Pet}
     */
    public void removeData(Pet pet, PetData petData) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to remove PetData [" + petData.toString() + "] from Pet through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        EchoPet.getPluginInstance().PH.setData(pet, new PetData[]{petData}, false);
    }

    /**
     * Checks if a {@link Pet} has specific {@link PetData}
     *
     * @param pet     the {@link Pet} to search
     * @param petData the {@link PetData} searched for in the {@link Pet} instance
     * @return true if the {@link Pet} has the specified {@link PetData}
     */
    public boolean hasData(Pet pet, PetData petData) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to check PetData [" + petData.toString() + "] of Pet through the EchoPetAPI. Pet cannot be null.");
            return false;
        }
        return pet.getActiveData().contains(petData);
    }

    /**
     * Opens the Pet Selector GUI Menu
     *
     * @param player {@link Player} to view the Menu
     * @param sendMessage defines if the plugin sends a message to the target {@link Player}
     */
    public void openPetSelector(Player player, boolean sendMessage) {
        PetSelector petSelector = new PetSelector(45, player);
        petSelector.open(sendMessage);
    }

    /**
     * Opens the Pet Selector GUI Menu
     *
     * @param player {@link Player} to view the menu
     */
    public void openPetSelector(Player player) {
        this.openPetSelector(player, false);
    }

    /**
     * Opens the Pet Data GUI Menu
     *
     * @param player {@link Player} to view the Menu
     * @param sendMessage defines if the plugin sends a message to the target {@link Player}
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
     * @param player {@link Player} to view the menu
     */
    public void openPetDataMenu(Player player) {
        this.openPetDataMenu(player, false);
    }

    /**
     * Set a target for the {@link Pet} to attack
     *
     * @param pet    the attacker
     * @param target the {@link LivingEntity} for the {@link Pet} to attack
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
        if (pet.getEntityPet().petGoalSelector.getGoal(PetGoalAttack.class) != null) {
            pet.getCraftPet().setTarget(target);
        }
    }

    /**
     * Get the {@link LivingEntity} that a {@link Pet} is targetting
     *
     * @param pet the attacker
     * @return {@link LivingEntity} being attacked, null if none
     */
    public LivingEntity getAttackTarget(Pet pet) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to get attack target for Pet through the EchoPetAPI. Pet cannot be null.");
        }
        return pet.getCraftPet().getTarget();
    }

    /**
     * Add a predefined goal to a {@link Pet} from the API
     *
     * @param pet      the {@link Pet} to add the goal to
     * @param goalType type of goal (enum)
     */
    public void addGoal(Pet pet, GoalType goalType) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to add PetGoal to Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        if (goalType == GoalType.ATTACK) {
            pet.getEntityPet().petGoalSelector.addGoal("Attack", new PetGoalAttack(pet.getEntityPet(), (Double) EchoPet.getPluginInstance().options.getConfigOption("attack.lockRange", 0.0D), (Integer) EchoPet.getPluginInstance().options.getConfigOption("attack.ticksBetweenAttacks", 20)));
        } else if (goalType == GoalType.FLOAT) {
            pet.getEntityPet().petGoalSelector.addGoal("Float", new PetGoalFloat(pet.getEntityPet()));
        } else if (goalType == GoalType.FOLLOW_OWNER) {
            pet.getEntityPet().petGoalSelector.addGoal("FollowOwner", new PetGoalFollowOwner(pet.getEntityPet(), pet.getEntityPet().getSizeCategory().getStartWalk(pet.getPetType()), pet.getEntityPet().getSizeCategory().getStopWalk(pet.getPetType()), pet.getEntityPet().getSizeCategory().getTeleport(pet.getPetType())));
        } else if (goalType == GoalType.LOOK_AT_PLAYER) {
            pet.getEntityPet().petGoalSelector.addGoal("LookAtPlayer", new PetGoalLookAtPlayer(pet.getEntityPet(), EntityHuman.class, 8.0F));
        }
    }

    /**
     * Add an implementation of {@link PetGoal} to a {@link Pet}
     *
     * @param pet        the {@link Pet} to add the {@link PetGoal} to
     * @param goal       the {@link PetGoal} to add
     * @param identifier a {@link String} to identify the goal
     */
    public void addGoal(Pet pet, PetGoal goal, String identifier) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to add PetGoal to Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        if (goal == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to ad PetGoal to Pet AI through the EchoPetAPI. Goal cannot be null.");
            return;
        }
        pet.getEntityPet().petGoalSelector.addGoal(identifier, goal);
    }

    /**
     * Remove a goal from a {@link Pet}'s AI
     *
     * @param pet      {@link Pet} to remove the goal from
     * @param goalType type of goal (enum)
     */
    public void removeGoal(Pet pet, GoalType goalType) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to remove PetGoal from Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        pet.getEntityPet().petGoalSelector.removeGoal(goalType.getGoalString());
    }

    /**
     * Remove a goal from a {@link Pet}'s AI
     * <p/>
     * The goal is identified using a string, initiated when the goal is added to the Pet
     *
     * @param pet        {@link Pet} to remove the goal from
     * @param identifier String that identifies a {@link PetGoal}
     */
    public void removeGoal(Pet pet, String identifier) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to remove PetGoal from Pet AI through the EchoPetAPI. Pet cannot be null.");
            return;
        }
        pet.getEntityPet().petGoalSelector.removeGoal(identifier);
    }

    /**
     * Remove a goal from a {@link Pet}'s AI
     *
     * @param pet     {@link Pet} to remove the goal from
     * @param petGoal {@link PetGoal} to remove
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
     * {@link Enum} of predefined {@link PetGoal}s
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
}

