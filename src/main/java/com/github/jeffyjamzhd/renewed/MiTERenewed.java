package com.github.jeffyjamzhd.renewed;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.xiaoyu233.fml.FishModLoader;
import net.xiaoyu233.fml.ModResourceManager;
import net.xiaoyu233.fml.reload.event.MITEEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class MiTERenewed implements ModInitializer {
    public static final String VERSION;
    public static final String NAMESPACE;
    public static final Logger LOGGER;
    public static final String RESOURCE_ID = "miterenewed:";

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

    static {
        Optional<ModContainer> mod = FishModLoader.getModContainer("miterenewed");
        ModMetadata meta = mod.get().getMetadata();

        VERSION = meta.getVersion().getFriendlyString();
        NAMESPACE = meta.getName();
        LOGGER = LogManager.getLogger(NAMESPACE);
    }
}