package com.dsh105.echopet.bridge;

import com.dsh105.echopet.bridge.container.EventContainer;

public interface GeneralBridge extends GenericBridge {

    void postEvent(EventContainer event);
}