package com.dsh105.echopet.bridge;

public class Ident {

    public static IdentBridge get() {
        return BridgeManager.getBridgeManager().getGenericBridge(IdentBridge.class);
    }
}