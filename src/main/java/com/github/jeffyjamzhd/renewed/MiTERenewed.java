package com.github.jeffyjamzhd.renewed;

import com.github.jeffyjamzhd.renewed.handler.RenewedFurnaceHandler;
import com.github.jeffyjamzhd.renewed.registry.RenewedItemProperties;
import com.github.jeffyjamzhd.renewed.registry.RenewedNetwork;
import moddedmite.rustedironcore.api.event.Handlers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.ResourceLocation;
import net.xiaoyu233.fml.FishModLoader;
import net.xiaoyu233.fml.ModResourceManager;
import net.xiaoyu233.fml.config.*;
import net.xiaoyu233.fml.reload.event.MITEEvents;
import net.xiaoyu233.fml.util.FieldReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Optional;

public class MiTERenewed implements ModInitializer {
    public static final String VERSION;
    public static final String NAMESPACE;
    public static final Logger LOGGER;
    public static File RENEWED_CONFIG_FILE;

    public static final String RESOURCE_ID = "miterenewed:";
    public static final String RESOURCE_ID_COMPACT = "MR:";

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing MiTE Renewed!");
        ModResourceManager.addResourcePackDomain(RESOURCE_ID.substring(0, RESOURCE_ID.length()-1));
        RenewedConfig.init();

        MITEEvents.MITE_EVENT_BUS.register(new EventListen());
        EventListen.register();
        RenewedNetwork.init();
        this.registerHandlers();
    }

    private void registerHandlers() {
        Handlers.FurnaceUpdate.register(new RenewedFurnaceHandler());
        Handlers.PropertiesRegistry.register(new RenewedItemProperties());
    }

    public static String getVersionString() {
        return NAMESPACE + " " + VERSION;
    }

    public static ResourceLocation of(String location) {
        return new ResourceLocation(RESOURCE_ID + location);
    }

    public static ResourceLocation ofPacket(String location) {
        return new ResourceLocation(RESOURCE_ID_COMPACT + location);
    }

    static {
        Optional<ModContainer> mod = FishModLoader.getModContainer("miterenewed");
        ModMetadata meta = mod.get().getMetadata();

        VERSION = meta.getVersion().getFriendlyString();
        NAMESPACE = meta.getName();
        LOGGER = LogManager.getLogger(NAMESPACE);
    }
}