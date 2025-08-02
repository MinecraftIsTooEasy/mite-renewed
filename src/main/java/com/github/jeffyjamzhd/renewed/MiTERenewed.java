package com.github.jeffyjamzhd.renewed;

import net.fabricmc.api.ModInitializer;
import net.xiaoyu233.fml.ModResourceManager;
import net.xiaoyu233.fml.reload.event.MITEEvents;

public class MiTERenewed implements ModInitializer {
    public static final String RESOURCE_ID = "miterenewed:";
    public static final String NAMESPACE = "MiTE Renewed";

    @Override
    public void onInitialize() {
        ModResourceManager.addResourcePackDomain(RESOURCE_ID.substring(0, RESOURCE_ID.length()-1));
        MITEEvents.MITE_EVENT_BUS.register(new EventListen());
        EventListen.register();
    }
}