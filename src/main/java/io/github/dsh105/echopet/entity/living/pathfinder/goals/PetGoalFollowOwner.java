package io.github.dsh105.echopet.entity.living.pathfinder.goals;

import io.github.dsh105.echopet.EchoPet;
import io.github.dsh105.echopet.api.event.PetMoveEvent;
import io.github.dsh105.echopet.entity.living.pathfinder.PetGoal;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.GenericAttributes;
import net.minecraft.server.v1_6_R3.Navigation;
import net.minecraft.server.v1_6_R3.PathEntity;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;


public class PetGoalFollowOwner extends PetGoal {

    private EntityLivingPet pet; //d
    private Navigation nav; //g
    private int timer = 0; //h
    private double startDistance;
    private double stopDistance;
    private double teleportDistance;
    private EntityPlayer owner;

    public PetGoalFollowOwner(EntityLivingPet pet, double startDistance, double stopDistance, double teleportDistance) {
        this.pet = pet;
        this.nav = pet.getNavigation();
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.teleportDistance = teleportDistance;
        this.owner = ((CraftPlayer) pet.getOwner()).getHandle();
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
        this.pet.getControllerLook().a(owner, 10.0F, (float) this.pet.bp()); //(bl() - 1.6.1) (bs() - 1.5.2)
        if (--this.timer <= 0) {
            this.timer = 10;
            if (this.pet.getOwner().isFlying()) {
                //Don't move pet when owner flying
                return;
            }

            double speed = 0.55F;

            if (this.pet.e(this.owner) > (this.teleportDistance) && ((CraftPlayer) this.pet.getOwner()).getHandle().onGround) {
                this.pet.getPet().teleport(this.pet.getOwner().getLocation());
                return;
            }

            PetMoveEvent moveEvent = new PetMoveEvent(this.pet.getPet(), this.pet.getLocation(), this.pet.getOwner().getLocation());
            EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(moveEvent);
            if (moveEvent.isCancelled()) {
                return;
            }

            if (owner.onGround && pet.goalTarget == null) {
                //Smooth path finding to entity instead of location
                PathEntity path = pet.world.findPath(pet, owner, (float) pet.getAttributeInstance(GenericAttributes.b).getValue(), true, false, false, true);
                pet.setPathEntity(path);
                nav.a(path, speed);
            }
        }
    }
}