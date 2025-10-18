package com.github.jeffyjamzhd.renewed.api.music;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.*;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RenewedMusicEngine
        implements ResourceManagerReloadListener {
    private static final ResourceLocation MUSIC_DEF = new ResourceLocation("miterenewed", "music.json");

    private Minecraft minecraft;
    private SoundManager soundManager;

    public Map<ResourceLocation, MusicMetadata> music;

    public RenewedMusicEngine(Minecraft minecraft, SoundManager manager) {
        this.minecraft = minecraft;
        this.soundManager = manager;
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        this.parseMusicDefinition(manager);
    }

    private void parseMusicDefinition(ResourceManager manager) {
        // Prepare for parsing
        Type dataType = new TypeToken<Map<ResourceLocation, MusicMetadata>>() {}.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(dataType, new MusicKeyDeserializer())
                .create();
        MiTERenewed.LOGGER.info("Loading music from music definition...");

        try (
                BufferedInputStream stream = (BufferedInputStream) manager.getResource(MUSIC_DEF).getInputStream();
                Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)
        ){
            this.music = gson.fromJson(reader, dataType);
            this.music.forEach((location, metadata) -> {
                MiTERenewed.LOGGER.info("{} - {}, by {}",
                        location.toString(),
                        metadata.title(),
                        metadata.artist());
            });

        } catch (Exception e) {
            MiTERenewed.LOGGER.error("No music definition file found in loaded resource pack.");
            e.printStackTrace();
        }
    }
}
