package com.github.jeffyjamzhd.renewed;

import net.fabricmc.api.ModInitializer;
import net.xiaoyu233.fml.ModResourceManager;
import net.xiaoyu233.fml.reload.event.MITEEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MiTERenewed implements ModInitializer {
    public static final String VERSION = "R198";
    public static final String RESOURCE_ID = "miterenewed:";
    public static final String NAMESPACE = "MiTE Renewed";
    public static final Logger LOGGER = LogManager.getLogger(NAMESPACE);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing MiTE Renewed!");
        ModResourceManager.addResourcePackDomain(RESOURCE_ID.substring(0, RESOURCE_ID.length()-1));
        MITEEvents.MITE_EVENT_BUS.register(new EventListen());
        EventListen.register();
    }

    public static String getVersionString() {
        return NAMESPACE + " " + VERSION;
    }
}