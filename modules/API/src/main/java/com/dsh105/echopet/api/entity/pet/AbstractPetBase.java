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

package com.dsh105.echopet.api.entity.pet;

import com.captainbern.minecraft.conversion.BukkitUnwrapper;
import com.captainbern.minecraft.reflection.MinecraftReflection;
import com.captainbern.reflection.Reflection;
import com.captainbern.reflection.SafeField;
import com.dsh105.commodus.Affirm;
import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.StringUtil;
import com.dsh105.commodus.container.PositionContainer;
import com.dsh105.commodus.container.Vector3dContainer;
import com.dsh105.commodus.particle.Particle;
import com.dsh105.echopet.api.configuration.Lang;
import com.dsh105.echopet.api.configuration.PetSettings;
import com.dsh105.echopet.api.configuration.Settings;
import com.dsh105.echopet.api.entity.*;
import com.dsh105.echopet.api.entity.ai.Mind;
import com.dsh105.echopet.api.entity.ai.PetMind;
import com.dsh105.echopet.api.entity.ai.goal.BehaviourFloat;
import com.dsh105.echopet.api.entity.ai.goal.BehaviourFollowOwner;
import com.dsh105.echopet.api.entity.ai.goal.BehaviourLookAtPlayer;
import com.dsh105.echopet.api.entity.attribute.AttributeManager;
import com.dsh105.echopet.api.entity.attribute.AttributeType;
import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.attribute.EntityAttribute;
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import com.dsh105.echopet.api.entity.entitypet.EntityPetModifier;
import com.dsh105.echopet.api.entity.pet.type.EnderDragonPet;
import com.dsh105.echopet.api.entity.pet.type.ZombiePet;
import com.dsh105.echopet.api.inventory.DataMenu;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.api.plugin.SQLPetManager;
import com.dsh105.echopet.bridge.*;
import com.dsh105.echopet.bridge.entity.LivingEntityBridge;
import com.dsh105.echopet.util.Perm;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractPetBase<T extends LivingEntityBridge, S extends EntityPet> implements Pet<T, S> {

    private static Pattern PREVIOUS_NAME_PATTERN = Pattern.compile(".+\\s([0-9])\\b");

    protected SafeField<Boolean> JUMP_FIELD;

    protected T bridgeEntity;
    protected S entity;
    protected UUID petId;

    protected PlayerBridge owner;
    protected String name;

    protected Mind mind;

    protected Pet rider;
    protected boolean isRider;

    protected boolean ownerInMountingProcess;
    protected boolean owningRiding;
    protected boolean hat;
    protected boolean stationary;

    protected boolean despawned;

    protected double jumpHeight;
    protected double rideSpeed;
    protected boolean shouldVanish;

    protected AbstractPetBase(UUID playerUID) {
        Affirm.notNull(playerUID);
        this.owner = PlayerBridge.of(playerUID);
        Affirm.notNull(this.owner.get());
        do {
            petId = UUID.randomUUID();
        } while (EchoPet.getManager().getPetUniqueIdMap().containsKey(petId));

        this.entity = Spawn.spawnBukkit(this);
        if (this.entity != null) {
            // Begin initiating our EntityPet

            JUMP_FIELD = new Reflection().reflect(MinecraftReflection.getMinecraftClass("EntityLiving")).getSafeFieldByName(getModifier().getJumpField());

            try {
                this.bridgeEntity = (T) EchoPet.getBridgeManager().createEntity(StringUtil.capitalise(getType().name().replace("_", " ")).replace(" ", ""));
            } catch (ClassNotFoundException e) {
                onError(e);
                return;
            }

            entity.modifyBoundingBox(width(), height());
            entity.setFireProof(true);
            getBridgeEntity().setMaxHealth(getTraits().health());
            getBridgeEntity().setHealth(getBridgeEntity().getMaxHealth());
            jumpHeight = PetSettings.JUMP_HEIGHT.getValue(getType().storageName());
            rideSpeed = PetSettings.RIDE_SPEED.getValue(getType().storageName());

            this.mind = new PetMind(this);
            this.mind.addGoal(new BehaviourFloat(), 0);
            this.mind.addGoal(new BehaviourFollowOwner(), 1);
            this.mind.addGoal(new BehaviourLookAtPlayer(), 2);

            getModifier().setAvoidsWater(false);
            getModifier().setAvoidSun(false);
            getModifier().setCanSwim(true);
            getModifier().setBreakDoors(false);
            getModifier().setEnterDoors(false);

            setName(getType().getDefaultName(getOwnerName()));
            moveToOwner();

        }
    }

    @Override
    public T getBridgeEntity() {
        return bridgeEntity;
    }

    @Override
    public S getEntity() {
        return entity;
    }

    @Override
    public <P extends Pet<T, S>> EntityPetModifier<P> getModifier() {
        return (EntityPetModifier<P>) entity.getModifier();
    }

    @Override
    public PlayerBridge getOwner() {
        return owner;
    }

    @Override
    public String getOwnerName() {
        return getOwner().getName();
    }

    @Override
    public UUID getOwnerUID() {
        return owner.getUID();
    }

    @Override
    public String getOwnerUIDAsString() {
        return getOwnerUID().toString();
    }

    @Override
    public PetType getType() {
        return getTraits().type();
    }

    @Override
    public UUID getPetId() {
        return petId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Mind getMind() {
        return mind;
    }

    private Traits getTraits() {
        return Attributes.getTraits(getType());
    }

    @Override
    public Voice getVoice() {
        return Attributes.getVoice(getType());
    }

    @Override
    public String getIdleSound() {
        return Attributes.getIdleSound(getType());
    }

    @Override
    public String getHurtSound() {
        return Attributes.getHurtSound(getType());
    }

    @Override
    public String getDeathSound() {
        return Attributes.getDeathSound(getType());
    }

    @Override
    public void makeStepSound() {
        if (getVoice() != null) {
            getEntity().makeSound(Attributes.getStepSound(getType()), 0.15F, 1.0F);
        }
    }

    @Override
    public SizeCategory getSizeCategory() {
        Size size = this.getClass().getInterfaces()[0].getAnnotation(Size.class);
        if (this instanceof AgeablePet) {
            return ((AgeablePet) this).isBaby() ? SizeCategory.TINY : size.value();
        } else if (this instanceof ZombiePet) {
            return ((ZombiePet) this).isBaby() ? SizeCategory.TINY : size.value();
        }
        return SizeCategory.REGULAR;
    }

    @Override
    public boolean setName(String name) {
        return setName(name, true);
    }

    @Override
    public boolean setName(String name, boolean sendFailMessage) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        if (name.length() > 32) {
            name = name.substring(0, 32);
        }

        if (EchoPet.getManager().getPetNameMapFor(getOwnerUID()).containsKey(name)) {
            Matcher matcher = PREVIOUS_NAME_PATTERN.matcher(name);
            if (matcher.matches()) {
                // Append a number onto the end to prevent duplicate names
                // This is especially problematic for multiple pets with the default name
                name = name.replace(matcher.group(0), " " + (GeneralUtil.toInteger(matcher.group(1)) + 1));
            } else {
                name += " 1";
            }
        }

        boolean allow = true;
        if (Settings.PET_NAME_REGEX_MATCHING.getValue()) {
            List<Map<String, String>> csRegex = Settings.PET_NAME_REGEX.getValue();
            if (!csRegex.isEmpty()) {
                for (Map<String, String> regexMap : csRegex) {
                    for (Map.Entry<String, String> entry : regexMap.entrySet()) {
                        if (Pattern.compile(entry.getKey(), Pattern.CASE_INSENSITIVE).matcher(name).matches()) {
                            allow = !StringUtil.toBoolean(entry.getValue());
                        }
                    }
                }
            }
        }
        if (EchoPet.getBridge(MessageBridge.class).isPermitted(owner.get(), "echopet.pet.name.override") || allow || Settings.PET_NAME.getValue(name)) {
            name = EchoPet.getBridge(MessageBridge.class).translateChatColours(name);
            if (Settings.STRIP_DIACRITICS.getValue()) {
                name = StringUtil.stripDiacritics(name);
            }
            EchoPet.getManager().unmapPetName(getOwnerUID(), this.name);

            this.name = name;
            getBridgeEntity().setName(this.name);
            getBridgeEntity().setNameVisibility(PetSettings.TAG_VISIBLE.getValue(getType().storageName()));

            EchoPet.getManager().mapPetName(this);
            return true;
        }

        if (sendFailMessage) {
            Lang.NAME_NOT_ALLOWED.send(getOwner(), "name", name);
        }
        return false;
    }

    @Override
    public boolean isRider() {
        return isRider;
    }

    @Override
    public Pet getRider() {
        return rider;
    }

    @Override
    public float width() {
        return getTraits().width();
    }

    @Override
    public float height() {
        return getTraits().height();
    }

    @Override
    public boolean getAttribute(Attributes.Attribute attribute) {
        return AttributeManager.getModifier(this).getModifier(attribute).getAttribute(this);
    }

    @Override
    public void setAttribute(Attributes.Attribute attribute, boolean value) {
        AttributeManager.getModifier(this).getModifier(attribute).setAttribute(this, value);
    }

    @Override
    public void invertAttribute(Attributes.Attribute attribute) {
        AttributeManager.getModifier(this).getModifier(attribute).setAttribute(this, !getAttribute(attribute));
    }

    @Override
    public EntityAttribute getAttribute(AttributeType attributeType) {
        return AttributeManager.getModifier(this).getModifier(attributeType).getAttribute(this);
    }

    @Override
    public void setAttribute(EntityAttribute entityAttribute) {
        AttributeManager.getModifier(this).getModifier(AttributeType.getByEnumBridge(entityAttribute.getClass())).setAttribute(this, entityAttribute);
    }

    @Override
    public List<EntityAttribute> getValidAttributes() {
        return AttributeManager.getModifier(this).getValidAttributes();
    }

    @Override
    public List<EntityAttribute> getActiveAttributes() {
        List<EntityAttribute> active = new ArrayList<>();
        for (EntityAttribute entityAttribute : getValidAttributes()) {
            if (AttributeManager.getModifier(this).isActive(this, entityAttribute)) {
                active.add(entityAttribute);
            }
        }
        return Collections.unmodifiableList(active);
    }

    @Override
    public boolean isStationary() {
        return stationary;
    }

    @Override
    public void setStationary(boolean flag) {
        this.stationary = flag;
    }

    @Override
    public void remove(boolean makeDeathSound) {
        if (despawned) {
            // He's dead, Jim!
            return;
        }

        if (entity != null && getBridgeEntity() != null) {
            Particle.DEATH_CLOUD.builder().show(getLocation());
            getBridgeEntity().removeEntity();
            if (makeDeathSound) {
                if (getDeathSound() != null && !getDeathSound().isEmpty()) {
                    entity.makeSound(getDeathSound(), 1.0F, 1.0F);
                }
            }
        }
        if (rider != null) {
            rider.remove(false);
            rider = null;
        }

        this.despawned = true;
    }

    @Override
    public Pet spawnRider(PetType type, boolean sendFailMessage) {
        return setRider(EchoPet.getPetRegistry().spawn(type, getOwnerUID()), sendFailMessage);
    }

    @Override
    public Pet setRider(Pet rider, boolean sendFailMessage) {
        if (rider == null) {
            return null;
        }
        String failMessage;
        if (!PetSettings.ENABLE.getValue(rider.getType().storageName())) {
            failMessage = Lang.PET_TYPE_DISABLED.getValue("type", rider.getType().humanName());
        } else if (!PetSettings.ALLOW_RIDERS.getValue(getType().storageName())) {
            failMessage = Lang.RIDERS_DISABLED.getValue("type", getType().humanName());
        } else {
            if (owningRiding) {
                setOwnerRiding(false);
            }

            if (this.rider != null) {
                this.rider.remove(false);
            }

            this.rider = rider;
            ((AbstractPetBase) rider).isRider = true;
            EchoPet.getBridge(SchedulerBridge.class).runLater(false, 5L, new Runnable() {
                @Override
                public void run() {
                    if (getBridgeEntity() != null) {
                        getBridgeEntity().setPassenger(getRider().getBridgeEntity());
                    }
                }
            });
            return rider;
        }

        if (sendFailMessage) {
            EchoPet.getBridge(MessageBridge.class).send(getOwner(), failMessage);
        }
        rider.remove(false);
        return null;
    }

    @Override
    public void removeRider() {
        rider.remove(true);
        if (EchoPet.getManager() instanceof SQLPetManager) {
            ((SQLPetManager) EchoPet.getManager()).clearRider(this);
        }
        rider = null;

    }

    @Override
    public PositionContainer getLocation() {
        return getBridgeEntity().getLocation();
    }

    @Override
    public boolean moveToOwner() {
        return getOwner().get() != null && move(getOwner().getLocation());
    }

    @Override
    public boolean move(PositionContainer to) {
        if (entity == null || getModifier().isDead()) {
            return false;
        }

        if (rider != null) {
            rider.getBridgeEntity().eject();
            rider.getBridgeEntity().move(to);
        }
        boolean result = getBridgeEntity().move(to);
        if (rider != null) {
            getBridgeEntity().setPassenger(rider.getBridgeEntity());
        }
        return result;
    }

    @Override
    public double getJumpHeight() {
        return jumpHeight;
    }

    @Override
    public void setJumpHeight(double jumpHeight) {
        this.jumpHeight = jumpHeight;
    }

    @Override
    public double getRideSpeed() {
        return rideSpeed;
    }

    @Override
    public void setRideSpeed(double rideSpeed) {
        this.rideSpeed = rideSpeed;
    }

    @Override
    public boolean shouldVanish() {
        return shouldVanish;
    }

    @Override
    public void setShouldVanish(boolean flag) {
        this.shouldVanish = flag;
    }

    @Override
    public boolean isOwnerRiding() {
        return owningRiding;
    }

    @Override
    public boolean isOwnerInMountingProcess() {
        return ownerInMountingProcess;
    }

    @Override
    public void setOwnerRiding(boolean flag) {
        if (owningRiding == flag) {
            return;
        }
        if (hat) {
            setHat(false);
        }

        if (flag) {
            this.ownerInMountingProcess = true;

            if (rider != null) {
                rider.remove(false);
            }
            getBridgeEntity().setPassenger(owner.get());
            if (this instanceof EnderDragonPet) {
                getModifier().setNoClipEnabled(false);
            }

            this.ownerInMountingProcess = false;

            entity.modifyBoundingBox(width() / 2, height() / 2);
        } else {
            if (this instanceof EnderDragonPet) {
                getModifier().setNoClipEnabled(true);
            }
            EchoPet.getManager().loadRider(this);
            entity.modifyBoundingBox(width(), height());
            moveToOwner();
        }

        this.owningRiding = flag;
    }

    @Override
    public boolean isHat() {
        return hat;
    }

    @Override
    public void setHat(boolean flag) {
        if (hat == flag) {
            return;
        }
        if (owningRiding) {
            setOwnerRiding(false);
        }

        if (flag) {
            if (rider != null) {
                rider.remove(false);
            }
            owner.setPassenger(bridgeEntity.getBridgedEntity());
        } else {
            owner.setPassenger(null);
            EchoPet.getManager().loadRider(this);
            entity.modifyBoundingBox(width(), height());
            moveToOwner();
        }

        this.hat = flag;
    }

    @Override
    public void onError(Throwable e) {
        EchoPet.log().severe("Uh oh. Something bad happened");
        e.printStackTrace();
        // TODO: send the player a message
        EchoPet.getManager().removePet(this, false);
    }

    @Override
    public void onLive() {
        // This should NEVER happen. NEVER
        if (getModifier().getPet() == null) {
            remove(false);
            return;
        }

        if (owner.get() == null || !owner.isOnline()) {
            EchoPet.getManager().removePet(this, false);
            return;
        }

        if (owningRiding && getModifier().getPassenger() == null && !ownerInMountingProcess) {
            setOwnerRiding(false);
        }

        for (Status status : new Status[]{Status.INVISIBLE, Status.SPRINTING, Status.SNEAKING}) {
            boolean entityStatus = getStatus(getBridgeEntity().getBridgedEntity(), status);
            if (getStatus(owner.get(), status) != entityStatus) {
                if (status != Status.INVISIBLE || !shouldVanish()) {
                    setStatus(getBridgeEntity().getBridgedEntity(), status, !entityStatus);
                }
            }
        }

        PositionContainer ownerPosition = owner.getLocation();

        if (this.hat) {
            getModifier().setYaw(getType() == PetType.ENDER_DRAGON ? ownerPosition.getXRotation() - 180 : ownerPosition.getXRotation());
        }

        if (owner.isFlying() && PetSettings.CAN_FLY.getValue(getType().storageName())) {
            Vector3dContainer direction = ownerPosition.toVector().subtract(getLocation().toVector());
            getBridgeEntity().setVelocity(new Vector3dContainer(direction.getX() + (ownerPosition.getX() > 0 ? 1 : -1), direction.getY(), direction.getZ() + (ownerPosition.getZ() > 0 ? 1 : -1)).normalize().multiply(0.3D));
        }
    }

    @Override
    public void onRide(float sideMotion, float forwardMotion) {
        if (getModifier().getPassenger() == null || !getModifier().getPassenger().equals(owner.get())) {
            entity.updateMotion(sideMotion, forwardMotion);
            getModifier().setStepHeight(0.5F);
            return;
        }

        getModifier().setStepHeight(1.0F);
        getModifier().applyPitchAndYawChanges(getBridgeEntity().getPassengerLocation().getYRotation() * 0.5F, getBridgeEntity().getPassengerLocation().getXRotation());

        // Retrieve motion of passenger
        sideMotion = getModifier().getPassengerSideMotion() * 0.5F;
        forwardMotion = getModifier().getPassengerForwardMotion();

        if (forwardMotion <= 0F) {
            // Slow down backwards movement
            forwardMotion *= 0.25F;
        }
        // Sidewards motion is slower
        sideMotion *= 0.75F;

        getModifier().setSpeed((float) rideSpeed);
        // Apply all changes to entity motion
        entity.updateMotion(sideMotion, forwardMotion);

        if (JUMP_FIELD != null) {
            boolean canFly = PetSettings.CAN_FLY.getValue(getType().storageName());
            double jumpHeight = canFly ? 0.5D : this.jumpHeight;
            if (canFly || getModifier().isGrounded()) {
                if (JUMP_FIELD.getAccessor().get(getModifier().getPassenger())) {
                    if (owner.isFlying()) {
                        owner.setFlying(false);
                    }
                    getModifier().setMotionY((float) jumpHeight);
                    if (!canFly) {
                        doJumpAnimation();
                    }
                }
            }
        }
    }

    @Override
    public boolean onInteract(PlayerBridge player) {
        if (owner.is(player)) {
            if (EchoPet.getBridge(MessageBridge.class).isPermitted(player, Perm.MENU)) {
                DataMenu.getInventory(this).show(player);
            }
            return true;
        }
        return false;
    }

    @Override
    public void doJumpAnimation() {

    }

    private static boolean getStatus(Object entity, Status status) {
        Object handle = BukkitUnwrapper.getInstance().unwrap(entity);
        return (Boolean) new Reflection().reflect(MinecraftReflection.getMinecraftClass("Entity")).getSafeMethod(status.getGetter()).getAccessor().invoke(handle);
    }

    private static void setStatus(Object entity, Status status, boolean value) {
        Object handle = BukkitUnwrapper.getInstance().unwrap(entity);
        new Reflection().reflect(MinecraftReflection.getMinecraftClass("Entity")).getSafeMethod(status.getSetter(), boolean.class).getAccessor().invoke(handle, value);
    }

    private enum Status {
        INVISIBLE,
        SPRINTING,
        SNEAKING;

        private String key;

        Status() {
            this.key = StringUtil.capitalise(this.name(), true);
        }

        public String getKey() {
            return key;
        }

        public String getSetter() {
            return "set" + key;
        }

        public String getGetter() {
            return "is" + key;
        }
    }
}