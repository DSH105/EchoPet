package com.dsh105.echopet.bridge;

import com.dsh105.commodus.container.PlayerContainer;
import com.dsh105.commodus.container.PositionContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class PlayerBridge extends PlayerContainer implements PlatformBridge {

    private static final Map<UUID, PlayerBridge> PLAYER_BRIDGES = new HashMap<>();

    public PlayerBridge(UUID playerUID) {
        super(playerUID);
    }

    public static PlayerBridge of(UUID playerUID) {
        PlayerBridge bridge = PLAYER_BRIDGES.get(playerUID);
        if (bridge == null) {
            bridge = BridgeManager.getBridgeManager().create(PlayerBridge.class);
            PLAYER_BRIDGES.put(playerUID, bridge);
        }
        return bridge;
    }

    public static PlayerBridge of(org.bukkit.entity.Player playerUID) {
        return of(Ident.get().getUID(playerUID));
    }

    public static PlayerBridge of(org.spongepowered.api.entity.player.Player spongePlayer) {
        return of(Ident.get().getUID(spongePlayer));
    }

    public void sendMessage(String message) {
        BridgeManager.getBridgeManager().getGenericBridge(MessageBridge.class).send(this, message);
    }
    
    public boolean isPermitted(String permission) {
        return BridgeManager.getBridgeManager().getGenericBridge(MessageBridge.class).isPermitted(get(), permission);
    }

    public abstract String getName();

    public abstract boolean isFlying();

    public abstract void setFlying(boolean flag);

    public abstract PositionContainer getLocation();

    public abstract double getLocX();

    public abstract double getLocY();

    public abstract double getLocZ();

    public abstract float getXRotation();

    public abstract float getYRotation();

    public abstract boolean isOnGround();

    public abstract void setPassenger(Object entity);

    public abstract boolean isOnline();

    public abstract boolean isDead();
}