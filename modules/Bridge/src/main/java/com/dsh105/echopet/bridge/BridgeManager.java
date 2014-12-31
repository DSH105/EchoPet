package com.dsh105.echopet.bridge;

import com.dsh105.commodus.ServerBrand;
import com.dsh105.commodus.StringUtil;
import com.dsh105.commodus.reflection.Reflection;
import com.dsh105.echopet.api.plugin.EchoPetCoreBridge;
import com.dsh105.echopet.bridge.entity.LivingEntityBridge;

import java.util.HashMap;
import java.util.Map;

public class BridgeManager {

    private static final Class<? extends GenericBridge>[] GENERIC_BRIDGES = new Class[]{GeneralBridge.class, MessageBridge.class, SchedulerBridge.class};

    private static BridgeManager BRIDGE_MANAGER;

    private final EchoPetCoreBridge pluginCore;
    private final Map<Class<? extends GenericBridge>, GenericBridge> bridges;
    private ServerBrand serverBrand;
    private String serverIdent;

    public BridgeManager(EchoPetCoreBridge pluginCore, ServerBrand serverBrand) {
        if (BRIDGE_MANAGER != null) {
            throw new IllegalStateException("A BridgeManager is already activated.");
        }

        BRIDGE_MANAGER = this;

        this.pluginCore = pluginCore;
        this.bridges = new HashMap<>();
        this.serverBrand = serverBrand;
        this.serverIdent = StringUtil.capitalise(serverBrand.name());
        for (Class<? extends GenericBridge> bridgeType : GENERIC_BRIDGES) {
            String bridgeClassName = bridgeType.getCanonicalName().replace(bridgeType.getSimpleName(), "platform." + serverIdent.toLowerCase() + "." + serverIdent + bridgeType.getSimpleName());
            try {
                Class<? extends GenericBridge> platformBridge = (Class<? extends GenericBridge>) Class.forName(bridgeClassName);
                this.bridges.put(bridgeType, platformBridge.newInstance());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Failed to create platform bridge: " + bridgeClassName + ". Plugin functionality may not be complete!", e);
            }
        }
    }

    public static BridgeManager getBridgeManager() {
        return BRIDGE_MANAGER;
    }

    public EchoPetCoreBridge getPluginCore() {
        return pluginCore;
    }

    public <T extends GenericBridge> T getGenericBridge(Class<T> bridgeType) {
        return (T) bridges.get(bridgeType);
    }

    public <T extends PlatformBridge> T create(Class<T> bridgeType) {
        String bridgeClassName = bridgeType.getCanonicalName().replace(bridgeType.getSimpleName(), "platform." + serverIdent.toLowerCase() + "." + serverIdent + bridgeType.getSimpleName());
        try {
            Class<? extends T> platformBridge = (Class<? extends T>) Class.forName(bridgeClassName);
            return platformBridge.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create platform bridge: " + bridgeClassName + ". Plugin functionality may not be complete!", e);
        }
    }

    public LivingEntityBridge createEntity(String type) throws ClassNotFoundException {
        Class entityBridgeType = (Class<? extends LivingEntityBridge>) Class.forName("com.dsh105.echopet.bridge.platform." + serverIdent.toLowerCase() + "entity.type." + serverIdent + type + "EntityBridge")
        String bridgeClassName = entityBridgeType.getCanonicalName().replace(entityBridgeType.getSimpleName(), "platform." + serverIdent.toLowerCase() + "." + serverIdent + bridgeType.getSimpleName());
        try {
            Class<? extends LivingEntityBridge> platformBridge = (Class<? extends LivingEntityBridge>) Class.forName(bridgeClassName);
            return platformBridge.newInstance(); // TODO: this requires a bukkit OR sponge entity
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create platform bridge: " + bridgeClassName + ". Plugin functionality may not be complete!", e);
        }
    }
}