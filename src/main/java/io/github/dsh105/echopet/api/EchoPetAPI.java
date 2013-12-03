package io.github.dsh105.echopet.api;

import io.github.dsh105.echopet.EchoPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.living.data.PetData;
import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.entity.living.data.PetType;
import io.github.dsh105.echopet.entity.living.pathfinder.PetGoal;
import io.github.dsh105.echopet.entity.living.pathfinder.goals.PetGoalAttack;
import io.github.dsh105.echopet.entity.living.pathfinder.goals.PetGoalFloat;
import io.github.dsh105.echopet.entity.living.pathfinder.goals.PetGoalFollowOwner;
import io.github.dsh105.echopet.entity.living.pathfinder.goals.PetGoalLookAtPlayer;
import io.github.dsh105.echopet.logger.ConsoleLogger;
import io.github.dsh105.echopet.logger.Logger;
import io.github.dsh105.echopet.menu.main.MenuOption;
import io.github.dsh105.echopet.menu.main.PetMenu;
import io.github.dsh105.echopet.menu.selector.PetSelector;
import io.github.dsh105.echopet.mysql.SQLPetHandler;
import io.github.dsh105.echopet.util.Lang;
import io.github.dsh105.echopet.util.MenuUtil;
import io.github.dsh105.echopet.util.StringUtil;
import net.minecraft.server.v1_7_R1.EntityHuman;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class EchoPetAPI {

    /**
     * Gives a {@link io.github.dsh105.echopet.entity.living.LivingPet} to the specified {@link Player}
     * <p/>
     * Pets will be spawned immediately next to the target player, linked until it is removed.
     *
     * @param player      the {@link Player} that will be provided with a LivingPet
     * @param petType     the {@link PetType} (type of LivingPet) that will be given to the player
     * @param sendMessage defines if the plugin sends a message to the target {@link Player}
     * @return the LivingPet created
     */
    public LivingPet givePet(Player player, PetType petType, boolean sendMessage) {
        if (player != null && petType != null) {
            LivingPet pet = EchoPet.getPluginInstance().PH.createPet(player, petType, sendMessage);
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
     * Removes a {@link io.github.dsh105.echopet.entity.living.LivingPet} if the {@link Player} has one active
     *
     * @param player      the {@link Player} to remove their LivingPet from
     * @param sendMessage defines if the plugin sends a message to the target {@link Player}
     */
    public void removePet(Player player, boolean sendMessage, boolean save) {
        EchoPet.getPluginInstance().PH.removePets(player, true);
        if (save) {
            if (hasPet(player)) {
                PetHandler.getInstance().saveFileData("autosave", PetHandler.getInstance().getPet(player));
                SQLPetHandler.getInstance().saveToDatabase(PetHandler.getInstance().getPet(player), false);
            }
        }
    }

    /**
     * Checks if a {@link Player} has a {@link io.github.dsh105.echopet.entity.living.LivingPet}
     *
     * @param player the {@link Player} used to check for LivingPet
     * @return true if {@link Player} has a LivingPet, false if not
     */
    public boolean hasPet(Player player) {
        return EchoPet.getPluginInstance().PH.getPet(player) != null;
    }

    /**
     * Gets a {@link Player}'s {@link io.github.dsh105.echopet.entity.living.LivingPet}
     *
     * @param player the {@link Player} to get the LivingPet of
     * @return the {@link io.github.dsh105.echopet.entity.living.LivingPet} instance linked to the {@link Player}
     */
    public LivingPet getPet(Player player) {
        return EchoPet.getPluginInstance().PH.getPet(player);
    }

    /**
     * Gets all active {@link io.github.dsh105.echopet.entity.living.LivingPet}
     *
     * @return an array of all active {@link io.github.dsh105.echopet.entity.living.LivingPet}s
     */

    public LivingPet[] getAllPets() {
        ArrayList<LivingPet> pets = EchoPet.getPluginInstance().PH.getPets();
        return pets.toArray(new LivingPet[pets.size()]);
    }

    /**
     * Teleports a {@link io.github.dsh105.echopet.entity.living.LivingPet} to a {@link Location}
     *
     * @param pet      the {@link io.github.dsh105.echopet.entity.living.LivingPet} to be teleported
     * @param location the {@link Location} to teleport the {@link io.github.dsh105.echopet.entity.living.LivingPet} to
     * @return success of teleportation
     */
    public boolean teleportPet(LivingPet pet, Location location) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to teleport LivingPet to Location through the EchoPetAPI. LivingPet cannot be null.");
            return false;
        }
        if (pet.isPetHat() || pet.isOwnerRiding()) {
            return false;
        }
        return pet.getCraftPet().teleport(location);
    }

    /**
     * Save a LivingPet to file or an SQL Database
     *
     * @param pet {@link io.github.dsh105.echopet.entity.living.LivingPet} to be saved
     * @param saveType whether to save to file or SQL database
     * @return success of save
     */
    public boolean savePet(LivingPet pet, SaveType saveType) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to save LivingPet file through the EchoPetAPI. LivingPet cannot be null.");
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
     * Adds {@link PetData} to a {@link io.github.dsh105.echopet.entity.living.LivingPet}
     *
     * @param pet     the {@link io.github.dsh105.echopet.entity.living.LivingPet} to add the data to
     * @param petData {@link PetData} to add to the {@link io.github.dsh105.echopet.entity.living.LivingPet}
     */
    public void addData(LivingPet pet, PetData petData) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to add PetData [" + petData.toString() + "] to LivingPet through the EchoPetAPI. LivingPet cannot be null.");
            return;
        }
        EchoPet.getPluginInstance().PH.setData(pet, new PetData[]{petData}, true);
    }

    /**
     * Removes {@link PetData} from a {@link io.github.dsh105.echopet.entity.living.LivingPet}
     *
     * @param pet     the {@link io.github.dsh105.echopet.entity.living.LivingPet} to remove the data from
     * @param petData {@link PetData} to remove to the {@link io.github.dsh105.echopet.entity.living.LivingPet}
     */
    public void removeData(LivingPet pet, PetData petData) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to remove PetData [" + petData.toString() + "] from LivingPet through the EchoPetAPI. LivingPet cannot be null.");
            return;
        }
        EchoPet.getPluginInstance().PH.setData(pet, new PetData[]{petData}, false);
    }

    /**
     * Checks if a {@link io.github.dsh105.echopet.entity.living.LivingPet} has specific {@link PetData}
     *
     * @param pet     the {@link io.github.dsh105.echopet.entity.living.LivingPet} to search
     * @param petData the {@link PetData} searched for in the {@link io.github.dsh105.echopet.entity.living.LivingPet} instance
     * @return true if the {@link io.github.dsh105.echopet.entity.living.LivingPet} has the specified {@link PetData}
     */
    public boolean hasData(LivingPet pet, PetData petData) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to check PetData [" + petData.toString() + "] of LivingPet through the EchoPetAPI. LivingPet cannot be null.");
            return false;
        }
        return pet.getActiveData().contains(petData);
    }

    /**
     * Opens the LivingPet Selector GUI Menu
     *
     * @param player {@link Player} to view the Menu
     * @param sendMessage defines if the plugin sends a message to the target {@link Player}
     */
    public void openPetSelector(Player player, boolean sendMessage) {
        PetSelector petSelector = new PetSelector(45, player);
        petSelector.open(sendMessage);
    }

    /**
     * Opens the LivingPet Selector GUI Menu
     *
     * @param player {@link Player} to view the menu
     */
    public void openPetSelector(Player player) {
        this.openPetSelector(player, false);
    }

    /**
     * Opens the LivingPet Data GUI Menu
     *
     * @param player {@link Player} to view the Menu
     * @param sendMessage defines if the plugin sends a message to the target {@link Player}
     */
    public void openPetDataMenu(Player player, boolean sendMessage) {
        LivingPet pet = PetHandler.getInstance().getPet(player);
        if (pet == null) {
            return;
        }
        ArrayList<MenuOption> options = MenuUtil.createOptionList(pet.getPetType());
        PetMenu menu = new PetMenu(pet, options, pet.getPetType() == PetType.HORSE ? 18 : 9);
        menu.open(sendMessage);
    }

    /**
     * Opens the LivingPet Data GUI Menu
     *
     * @param player {@link Player} to view the menu
     */
    public void openPetDataMenu(Player player) {
        this.openPetDataMenu(player, false);
    }

    /**
     * Set a target for the {@link io.github.dsh105.echopet.entity.living.LivingPet} to attack
     *
     * @param pet    the attacker
     * @param target the {@link LivingEntity} for the {@link io.github.dsh105.echopet.entity.living.LivingPet} to attack
     */
    public void setAttackTarget(LivingPet pet, LivingEntity target) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to set attack target for LivingPet through the EchoPetAPI. LivingPet cannot be null.");
            return;
        }
        if (target == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to set attack target for LivingPet through the EchoPetAPI. Target cannot be null.");
            return;
        }
        if (pet.getEntityPet().petGoalSelector.getGoal(PetGoalAttack.class) != null) {
            pet.getCraftPet().setTarget(target);
        }
    }

    /**
     * Get the {@link LivingEntity} that a {@link io.github.dsh105.echopet.entity.living.LivingPet} is targetting
     *
     * @param pet the attacker
     * @return {@link LivingEntity} being attacked, null if none
     */
    public LivingEntity getAttackTarget(LivingPet pet) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to get attack target for LivingPet through the EchoPetAPI. LivingPet cannot be null.");
        }
        return pet.getCraftPet().getTarget();
    }

    /**
     * Add a predefined goal to a {@link io.github.dsh105.echopet.entity.living.LivingPet} from the API
     *
     * @param pet      the {@link io.github.dsh105.echopet.entity.living.LivingPet} to add the goal to
     * @param goalType type of goal (enum)
     */
    public void addGoal(LivingPet pet, GoalType goalType) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to add PetGoal to LivingPet AI through the EchoPetAPI. LivingPet cannot be null.");
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
     * Add an implementation of {@link PetGoal} to a {@link io.github.dsh105.echopet.entity.living.LivingPet}
     *
     * @param pet        the {@link io.github.dsh105.echopet.entity.living.LivingPet} to add the {@link PetGoal} to
     * @param goal       the {@link PetGoal} to add
     * @param identifier a {@link String} to identify the goal
     */
    public void addGoal(LivingPet pet, PetGoal goal, String identifier) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to add PetGoal to LivingPet AI through the EchoPetAPI. LivingPet cannot be null.");
            return;
        }
        if (goal == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to ad PetGoal to LivingPet AI through the EchoPetAPI. Goal cannot be null.");
            return;
        }
        pet.getEntityPet().petGoalSelector.addGoal(identifier, goal);
    }

    /**
     * Remove a goal from a {@link io.github.dsh105.echopet.entity.living.LivingPet}'s AI
     *
     * @param pet      {@link io.github.dsh105.echopet.entity.living.LivingPet} to remove the goal from
     * @param goalType type of goal (enum)
     */
    public void removeGoal(LivingPet pet, GoalType goalType) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to remove PetGoal from LivingPet AI through the EchoPetAPI. LivingPet cannot be null.");
            return;
        }
        pet.getEntityPet().petGoalSelector.removeGoal(goalType.getGoalString());
    }

    /**
     * Remove a goal from a {@link io.github.dsh105.echopet.entity.living.LivingPet}'s AI
     * <p/>
     * The goal is identified using a string, initiated when the goal is added to the LivingPet
     *
     * @param pet        {@link io.github.dsh105.echopet.entity.living.LivingPet} to remove the goal from
     * @param identifier String that identifies a {@link PetGoal}
     */
    public void removeGoal(LivingPet pet, String identifier) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to remove PetGoal from LivingPet AI through the EchoPetAPI. LivingPet cannot be null.");
            return;
        }
        pet.getEntityPet().petGoalSelector.removeGoal(identifier);
    }

    /**
     * Remove a goal from a {@link io.github.dsh105.echopet.entity.living.LivingPet}'s AI
     *
     * @param pet     {@link io.github.dsh105.echopet.entity.living.LivingPet} to remove the goal from
     * @param petGoal {@link PetGoal} to remove
     */
    public void removeGoal(LivingPet pet, PetGoal petGoal) {
        if (pet == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to remove PetGoal from LivingPet AI through the EchoPetAPI. LivingPet cannot be null.");
            return;
        }
        if (petGoal == null) {
            ConsoleLogger.log(Logger.LogLevel.SEVERE, "Failed to remove PetGoal from LivingPet AI through the EchoPetAPI. Goal cannot be null.");
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

    /**
     * Types used for saving {@link io.github.dsh105.echopet.entity.living.LivingPet}s
     */
    public enum SaveType {
        SQL, FILE;
    }
}

