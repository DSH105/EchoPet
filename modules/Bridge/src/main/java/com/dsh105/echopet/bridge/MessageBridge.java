package com.dsh105.echopet.bridge;

public interface MessageBridge extends GenericBridge {

    String translateChatColours(String message);

    void send(Object conversable, String prefix, String message);
}