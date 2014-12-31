package com.dsh105.echopet.bridge;

import java.util.UUID;

public interface IdentBridge extends GenericBridge {

    UUID getUID(Object player);

    Object getPlayer(UUID uniqueId);

    boolean areIdentical(Object player, Object player1);

    Object getByName(String targetName, boolean exact);

    boolean isPlayer(Object player);
}