package io.github.dsh105.echopet.entity.living.pathfinder.goals;

import io.github.dsh105.echopet.EchoPet;
import io.github.dsh105.echopet.api.event.PetMoveEvent;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.living.pathfinder.PetGoal;
import net.minecraft.server.v1_7_R1.EntityPlayer;
import net.minecraft.server.v1_7_R1.GenericAttributes;
import net.minecraft.server.v1_7_R1.Navigation;
import net.minecraft.server.v1_7_R1.PathEntity;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;


public class PetGoalFollowOwner extends PetGoal {

    private EntityPet pet;
    private Navigation nav;
    private int timer = 0;
    private double startDistance;
    private double stopDistance;
    private double teleportDistance;
    private EntityPlayer owner;

    public PetGoalFollowOwner(EntityPet pet, double startDistance, double stopDistance, double teleportDistance) {
        this.pet = pet;
        this.nav = pet.getNavigation();
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.teleportDistance = teleportDistance;
        this.owner = ((CraftPlayer) pet.getPlayerOwner()).getHandle();
    }

    @Override
    public boolean shouldStart() {
        if (!this.pet.isAlive()) {
            return false;
        } else if (this.owner == null) {
            return false;
        } else if (this.pet.e(this.owner) < this.startDistance) {
            return false;
        } else if (this.pet.getGoalTarget() != null && this.pet.getGoalTarget().isAlive()) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public boolean shouldFinish() {
        PetGoalAttack attackGoal = (PetGoalAttack) this.pet.petGoalSelector.getGoal(PetGoalAttack.class);
        if (attackGoal != null && attackGoal.isActive) {
            return true;
        }
        if (this.owner == null) {
            return true;
        } else if (this.pet.e(this.owner) <= this.stopDistance) {
            return true;
        } else if (this.pet.getGoalTarget() != null && this.pet.getGoalTarget().isAlive()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.timer = 0;

        //Set pathfinding radius
        pet.getAttributeInstance(GenericAttributes.b).setValue(this.teleportDistance);
    }

    @Override
    public void finish() {
        this.nav.h();
    }

    @Override
    public void tick() {
        //https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/PathfinderGoalFollowOwner.java#L57
        this.pet.getControllerLook().a(owner, 10.0F, (float) this.pet.x());
        if (--this.timer <= 0) {
            this.timer = 10;
            if (this.pet.getPlayerOwner().isFlying()) {
                //Don't move pet when owner flying
                return;
            }

            double speed = 0.55F;
            if (this.pet.e(this.owner) > (this.teleportDistance) && ((CraftPlayer) this.pet.getPlayerOwner()).getHandle().onGround) {
                this.pet.getPet().teleport(this.pet.getPlayerOwner().getLocation());
                return;
            }

            PetMoveEvent moveEvent = new PetMoveEvent(this.pet.getPet(), this.pet.getLocation(), this.pet.getPlayerOwner().getLocation());
            EchoPet.getInstance().getServer().getPluginManager().callEvent(moveEvent);
            if (moveEvent.isCancelled()) {
                return;
            }

            if (pet.goalTarget == null) {
                //Smooth path finding to entity instead of location
                PathEntity path = pet.world.findPath(pet, owner, (float) pet.getAttributeInstance(GenericAttributes.b).getValue(), true, false, false, true);
                pet.setPathEntity(path);
                nav.a(path, speed);
            }
        }
    }
}
