package com.github.jeffyjamzhd.renewed;

import com.github.jeffyjamzhd.renewed.handler.RenewedFurnaceHandler;
import moddedmite.rustedironcore.api.event.Handlers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
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

    public static FieldReference<String> TICKS_UNTIL_NEXT_SONG = new FieldReference<>("default");
    public static FieldReference<Boolean> MUSIC_DISPLAY = new FieldReference<>(true);

    public static ConfigRoot CONFIG;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing MiTE Renewed!");
        ModResourceManager.addResourcePackDomain(RESOURCE_ID.substring(0, RESOURCE_ID.length()-1));

        MITEEvents.MITE_EVENT_BUS.register(new EventListen());
        EventListen.register();
        this.registerHandlers();
    }

    @Override
    public Optional<ConfigRegistry> createConfig() {
        return Optional.of(new ConfigRegistry(CONFIG, RENEWED_CONFIG_FILE));
    }

    private void registerHandlers() {
        Handlers.FurnaceUpdate.register(new RenewedFurnaceHandler());
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
        RENEWED_CONFIG_FILE = new File(NAMESPACE + ".json");

        CONFIG = ConfigRoot.create(1)
                .addEntry(ConfigCategory.of("Audio")
                        .addEntry(ConfigEntry.of("Music Delay", TICKS_UNTIL_NEXT_SONG)
                                .withComment("The delay between music tracks. [0 : rare, 1 : vanilla, 2 : default, 3 : constant]"))
                        .addEntry(ConfigEntry.of("Music Display", MUSIC_DISPLAY)
                                .withComment("When false, disables the music display from showing up.")));
    }
}