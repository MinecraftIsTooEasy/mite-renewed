package com.github.jeffyjamzhd.renewed.api.music;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.api.event.MusicConditionRegisterEvent;
import com.github.jeffyjamzhd.renewed.render.gui.GuiMusic;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;
import net.minecraft.client.main.Main;
import paulscode.sound.SoundSystem;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Environment(EnvType.CLIENT)
public class RenewedMusicEngine
        implements ResourceManagerReloadListener {
    private static final int RARE_COOLDOWN = 20 * 60 * 24;
    private static final int VANILLA_COOLDOWN = 20 * 60 * 10;
    private static final int DEFAULT_COOLDOWN = 20 * 60 * 3;

    /**
     * Location where music definitions are located
     */
    private static final ResourceLocation MUSIC_DEF = new ResourceLocation("miterenewed", "music.json");
    /**
     * Channel in which all music is played on
     */
    private static final String CHANNEL_NAME = "RenewedMusic";

    /**
     * Random
     */
    private final Random random;
    /**
     * Renewed's music sound pool, separate from the vanilla one
     */
    private final SoundPool soundPoolRenewedMusic;
    /**
     * Sound engine reference
     */
    private SoundSystem soundSystem;
    /**
     * Minecraft settings
     */
    private GameSettings settings;
    /**
     * If the sound system is currently loaded or not
     */
    private boolean isLoaded = false;
    /**
     * Currently playing track
     */
    private MusicMetadata track;

    /**
     * Ticks left before the next track plays
     */
    private int ticksBeforeNextTrack = 20;

    /**
     * Registered music
     */
    public HashMap<ResourceLocation, MusicMetadata> music;
    /**
     * Music gui reference
     */
    public GuiMusic gui;

    public RenewedMusicEngine(ResourceManager manager, GameSettings gs, Random random)
    {
        this.settings = gs;
        this.soundPoolRenewedMusic = new SoundPool(manager, "music", false);
        this.random = random;
        this.music = new HashMap<>();

        // Send out event
        MusicConditionRegisterEvent.init();
    }

    /**
     * Updates the engine, starts a track when possible
     */
    public void tickEngine() {
        if (Main.is_MITE_DS || !isLoaded || isMuted() || music.isEmpty()) {
            return;
        }

        if (this.soundSystem.playing(CHANNEL_NAME) && this.soundSystem.playing("streaming")) {
            return;
        }

        if (this.ticksBeforeNextTrack > 0) {
            this.ticksBeforeNextTrack--;
            return;
        }

        String trackToPlay = determineMusicTrack();
        SoundPoolEntry entry = this.soundPoolRenewedMusic.getRandomSoundFromSoundPool(trackToPlay);
        if (entry != null) {
            this.ticksBeforeNextTrack = this.random.nextInt(2500) + DEFAULT_COOLDOWN;
            this.soundSystem.backgroundMusic(CHANNEL_NAME, entry.getSoundUrl(), entry.getSoundName(), false);
            this.soundSystem.setVolume(CHANNEL_NAME, this.getVolume());
            this.soundSystem.play(CHANNEL_NAME);

            // Queue display
            this.gui.queueMusic(track);
        } else {
            // Reset track, as its invalid (didn't load)
            this.track = null;
            MiTERenewed.LOGGER.error("Could not load track. Likely a malformed asset path. {}", trackToPlay);
        }
    }

    /**
     * Returns track metadata to play
     */
    private String determineMusicTrack() {
        // Do a random pick for now
        List<ResourceLocation> locations = this.music.keySet().stream().toList();
        int random = this.random.nextInt(music.values().size());
        ResourceLocation location = locations.get(random);

        // Set track, return asset path
        this.track = this.music.get(location);
        return this.parseResourceLocationForLoading(location);
    }

    public void stopMusic() {
        if (Main.is_MITE_DS || !isLoaded || isMuted()) {
            return;
        }

        this.soundSystem.stop(CHANNEL_NAME);
    }

    public float getVolume() {
        return this.settings.musicVolume;
    }

    public void setDelay(int delayTicks) {
        this.ticksBeforeNextTrack = delayTicks;
    }

    public void setPitch(float pitch) {
        if (!this.isLoaded) {
            MiTERenewed.LOGGER.warn("Tried to change pitch of music, but engine is not loaded!");
            return;
        }

        this.soundSystem.setPitch(CHANNEL_NAME, pitch);
    }

    public float getPitch() {
        if (!this.isLoaded) {
            MiTERenewed.LOGGER.warn("Tried to get pitch of music, but engine is not loaded!");
            return 1F;
        }

        return this.soundSystem.getPitch(CHANNEL_NAME);
    }

    public boolean isPlaying() {
        return this.isLoaded && this.soundSystem.playing(CHANNEL_NAME);
    }

    public boolean isMuted() {
        return this.getVolume() <= 0F;
    }

    //-------- SoundManager hooks --------//

    /**
     * Called when sound options change
     */
    public void onSoundOptionsChanged() {
        if (this.isLoaded) {
            // Handle volume
            float volume = this.getVolume();
            if (volume <= 0F) {
                this.soundSystem.stop(CHANNEL_NAME);
            }
            this.soundSystem.setVolume(CHANNEL_NAME, volume);

            // Music frequency... etc
        }
    }

    /**
     * Called upon resource reload
     */
    public void onCleanup() {
        this.isLoaded = false;
    }

    /**
     * Called by {@link SoundManagerINNER1} during it's reload process
     */
    public void provideSoundSystemReference(SoundSystem system) {
        this.soundSystem = system;
        this.isLoaded = true;
    }

    /**
     * Called by {@link Minecraft} during it's initialization process
     */
    public void provideGuiReference(GuiMusic gui) {
        this.gui = gui;
    }

    //-------- Resource Management --------//

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        this.stopMusic();
        this.onCleanup();
        this.parseMusicDefinition(manager);
    }

    /**
     * Parses music definition file
     * @see MusicKeyDeserializer
     */
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
            if (music == null) {
                throw new Exception("No music definition file found in loaded resource pack.");
            }

            this.music.forEach((location, metadata) -> {
                // Add to pool and log entry
                this.soundPoolRenewedMusic.addSound(location.toString());
                MiTERenewed.LOGGER.info("{} - {}, by {}",
                        location.toString(),
                        metadata.getTitle(),
                        metadata.getArtist());
            });

        } catch (Exception e) {
            MiTERenewed.LOGGER.error(e.getMessage());
            this.music = new HashMap<>();
        }
    }

    /**
     * Prepares resource location for loading from the {@link SoundPool}
     */
    private String parseResourceLocationForLoading(ResourceLocation location) {
        String trackFile = location.toString();
        return trackFile.substring(0, trackFile.length()-4);
    }
}
