package com.dsh105.echopet.bridge.platform.sponge;

import com.dsh105.commodus.Affirm;
import com.dsh105.commodus.sponge.SpongeUtil;
import com.dsh105.echopet.bridge.GeneralBridge;
import com.dsh105.echopet.bridge.container.EventContainer;
import org.spongepowered.api.util.event.Event;

public class SpongeGeneralBridge implements GeneralBridge {

    @Override
    public void postEvent(EventContainer event) {
        SpongeUtil.getGame().getEventManager().post(event.asSponge());
    }
}