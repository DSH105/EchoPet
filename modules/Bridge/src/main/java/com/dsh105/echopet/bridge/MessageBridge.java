package com.dsh105.echopet.bridge;

public interface MessageBridge extends GenericBridge {

    String translateChatColours(String message);

    void send(Object conversable, String message);

    void send(Object conversable, String prefix, String message);

    void send(PlayerBridge player, String message);

    void send(PlayerBridge player, String prefix, String message);

    boolean isPermitted(Object permissible, String permission);
}