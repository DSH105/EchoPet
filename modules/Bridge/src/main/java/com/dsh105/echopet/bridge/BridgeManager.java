package com.dsh105.echopet.bridge;

import com.dsh105.commodus.reflection.Reflection;

import java.util.HashMap;
import java.util.Map;

public class BridgeManager {

    private static final Class<? extends GenericBridge>[] GENERIC_BRIDGES = new Class[]{MessageBridge.class};

    private final Map<Class<? extends GenericBridge>, GenericBridge> bridges;

    public BridgeManager(ServerBridge serverBridge) {
        this.bridges = new HashMap<>();
        for (Class<? extends GenericBridge> bridgeType : GENERIC_BRIDGES) {
            String bridgeClassName = bridgeType.getCanonicalName().replace(bridgeType.getSimpleName(), "platform." + serverBridge.getClassPrefix().toLowerCase() + "." + serverBridge.getClassPrefix() + bridgeType.getSimpleName());
            Class<? extends GenericBridge> platformBridge = (Class<? extends GenericBridge>) Reflection.getClass(bridgeClassName);
            this.bridges.put(bridgeType, Reflection.newInstance(Reflection.getConstructor(platformBridge)));
        }
    }

    public <T extends GenericBridge> T getGenericBridge(Class<T> bridgeType) {
        return (T) bridges.get(bridgeType);
    }

}