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
    private static final int FADE_OUT_TICKS = 100;
    private static final int STATE_COOLDOWN = 0;
    private static final int STATE_ACTIVE = 1;
    private static final int STATE_TRANSITION = 2;

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
     * Currently playing track location
     */
    private ResourceLocation trackLocation;
    /**
     * Ticks left before the next track plays
     */
    private int ticksBeforeNextTrack = 20;
    /**
     * Ticks left before transition completes
     */
    private int ticksBeforeTransition = FADE_OUT_TICKS;
    /**
     * Currently playing track's condition value
     */
    private int trackConditionValue;
    /**
     * If the transition is going out right now
     */
    private boolean transitionOut;
    /**
     * If the transition was manually called
     */
    private boolean transitionOutForced;
    /**
     * Current engine state
     */
    private int state = STATE_COOLDOWN;

    /**
     * Registered music
     */
    public HashMap<ResourceLocation, MusicMetadata> music;
    /**
     * Sorted music
     */
    private TreeMap<Integer, HashSet<ResourceLocation>> sortedMusic;
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
        this.sortedMusic = new TreeMap<>(Integer::compareTo);

        // Send out event
        MusicConditionRegisterEvent.init();
    }

    /**
     * Updates the engine, starts a track when possible
     */
    public void tickEngine(World world, EntityPlayer player) {
        // Dont do anything under these conditions
        if (Main.is_MITE_DS || !isLoaded || isMuted() || music.isEmpty()) {
            return;
        }

        // Tick states
        switch (this.state) {
            case STATE_COOLDOWN -> stateCooldown(world, player);
            case STATE_ACTIVE -> stateRunning(world, player);
            case STATE_TRANSITION -> stateTransition(world, player);
        }
    }

    private void stateRunning(World world, EntityPlayer player) {
        // Only check once every second
        if (world != null && world.getTimeOfDay() % 20 == 0) {
            checkForTransition(world, player);
        }

        // Don't bother if music is still playing
        if (this.soundSystem.playing(CHANNEL_NAME) || this.soundSystem.playing("streaming")) {
            return;
        }

        // Time for cooldown state
        this.setState(STATE_COOLDOWN);
    }

    private void stateCooldown(World world, EntityPlayer player) {
        if (this.ticksBeforeNextTrack > 0) {
            this.ticksBeforeNextTrack--;
            return;
        }

        String trackToPlay = determineMusicTrack(world, player);
        SoundPoolEntry entry = this.soundPoolRenewedMusic.getRandomSoundFromSoundPool(trackToPlay);
        if (entry != null) {
            // Play music
            this.setDelay();
            this.soundSystem.backgroundMusic(CHANNEL_NAME, entry.getSoundUrl(), entry.getSoundName(), false);
            this.soundSystem.setVolume(CHANNEL_NAME, this.getVolume());
            this.soundSystem.play(CHANNEL_NAME);
            this.setState(STATE_ACTIVE);
            this.simulateIntendedPitch(world != null ? world.getAsWorldClient() : null, false);

            MiTERenewed.LOGGER.info("Tried to play {}", entry.getSoundName());

            // Queue display
            this.gui.queueMusic(track);
        } else {
            // Reset track, as its invalid (didn't load)
            this.track = null;
            if (!trackToPlay.isEmpty()) {
                MiTERenewed.LOGGER.error("Could not load track. Likely a malformed asset path. {}", trackToPlay);
            }
            this.setDelay();
        }
    }

    private void stateTransition(World world, EntityPlayer player) {
        // Check if condition does suddenly match every half second
        if (world != null && world.getTimeOfDay() % 10 == 0) {
            checkForTransition(world, player);
        }

        // Handle transition
        if (this.transitionOut || this.transitionOutForced) {
            this.ticksBeforeTransition--;
            if (this.ticksBeforeTransition <= 0) {
                stopMusic();
                this.setState(STATE_COOLDOWN);
            }
        } else {
            this.ticksBeforeTransition++;
            if (this.ticksBeforeTransition > FADE_OUT_TICKS) {
                this.setState(STATE_ACTIVE);
            }
        }

        // Set volume
        float busVolume;
        if (this.transitionOut || this.transitionOutForced) {
            busVolume = Math.max(0F, getVolume() - (1F - (ticksBeforeTransition / (float) FADE_OUT_TICKS)));
        } else {
            busVolume = Math.min(getVolume(), getVolume() - (1F - (ticksBeforeTransition / (float) FADE_OUT_TICKS)));
        }

        this.soundSystem.setVolume(CHANNEL_NAME, busVolume);
    }

    public void fadeOutCurrentTrack() {
        if (this.isLoaded && this.isPlaying()) {
            this.setState(STATE_TRANSITION);
            this.transitionOutForced = true;
        }
    }

    private void checkForTransition(World world, EntityPlayer player) {
        if (!this.trackStillMatchesConditions(world, player)) {
            this.setState(STATE_TRANSITION);
            this.transitionOut = true;
            return;
        }
        this.transitionOut = false;
    }

    private void setState(int incoming) {
        switch (incoming) {
            case STATE_TRANSITION -> {
                if (state != STATE_TRANSITION) {
                    this.ticksBeforeTransition = FADE_OUT_TICKS;
                    this.state = incoming;
                }
            }
            case STATE_COOLDOWN -> {
                this.state = incoming;
                this.transitionOutForced = false;
            }
            default -> this.state = incoming;
        }
    }

    /**
     * Returns track metadata to play
     */
    private String determineMusicTrack(World world, EntityPlayer player) {
        // Iterate through registered tracks and evaluate
        sortMusic(world, player);

        // Do nothing if sorted music is empty
        if (sortedMusic.isEmpty()) {
            return "";
        }

        // Do random pick from best match
        List<ResourceLocation> locations;
        locations = this.sortedMusic
                .lastEntry()
                .getValue()
                .stream()
                .toList();

        // Roll for hopefully unique track
        int rolls = 0;
        ResourceLocation location = trackLocation;
        while (rolls < 3 && location == trackLocation) {
            int random = this.random.nextInt(locations.size());
            location = locations.get(random);
            rolls++;
        }

        // Set track, return asset path
        this.track = this.music.get(location);
        this.trackLocation = location;
        this.trackConditionValue = this.sortedMusic.lastKey();
        return this.parseResourceLocationForLoading(location);
    }

    private boolean trackStillMatchesConditions(World world, EntityPlayer player) {
        // Ignore if track cannot be "cut-off"
        if (track == null || !track.canBeCutoff()) {
            return true;
        }

        // Check self
        Optional<Integer> checkContainer = this.track.getConditions()
                .stream()
                .map(condition -> condition.check(world, player) ? condition.getPriority() : -condition.getPriority())
                .reduce(Integer::sum);

        // Sort music
        sortMusic(world, player);

        if (checkContainer.isPresent()) {
            // If our value is lower than the current highest,
            // we must transition
            int check = checkContainer.get();
            if (check < this.sortedMusic.lastKey()) {
                return false;
            }

            // If current track is misaligned with its current placement,
            // we should transition
            if (check < trackConditionValue) {
                return false;
            }

            // Update value
            this.trackConditionValue = check;
        }

        // Otherwise, we're fine.
        return true;
    }

    /**
     * Sorts registered music tracks based on how well they match current conditions
     */
    private void sortMusic(World world, EntityPlayer player) {
        // Clear and begin iterating
        this.sortedMusic.clear();

        for (Map.Entry<ResourceLocation, MusicMetadata> entry : this.music.entrySet()) {
            ResourceLocation key = entry.getKey();
            MusicMetadata data = entry.getValue();

            Optional<Integer> checkContainer = data.getConditions()
                    .stream()
                    .map(condition -> condition.check(world, player) ? condition.getPriority() : -condition.getPriority())
                    .reduce(Integer::sum);

            if (checkContainer.isPresent()) {
                int value = checkContainer.get();
                if (value < 0) {
                    continue;
                }
                this.sortedMusic.putIfAbsent(value, new HashSet<>());
                this.sortedMusic.get(value).add(key);
            } else {
                MiTERenewed.LOGGER.error("Sorting failed for {}, something went wrong when checking conditions",
                        key.getResourcePath());
            }
        }
    }

    /**
     * Simulates the intended pitch of music based on provided time.
     * @param world Client's world
     * @return The intended music pitch
     */
    public void simulateIntendedPitch(WorldClient world, boolean interpolated) {
        // Do not simulate if no track is playing
        if (track == null) {
            return;
        }

        // Do not pitch if there's a fixed pitch set
        Optional<Float> fixedPitch = track.getFixedPitch();
        if (fixedPitch.isPresent()) {
            this.setPitch(fixedPitch.get());
            return;
        }

        // Do not simulate further if world is null
        if (world == null) {
            this.setPitch(1F);
            return;
        }

        float offset = 0F;
        float time = world.getAdjustedTimeOfDay();
        float target = targetOffset(world);

        // Calculate time factor
        if (time > WorldClient.getTimeOfSunset() - 2000 || time < WorldClient.getTimeOfSunrise()) {
            // Nighttime
            int speed = world.isBloodMoon(false) ? 2000 : 4000;
            time -= WorldClient.getTimeOfSunset() - 2000;

            if (time <= 0 || time > speed)
                offset += target;
            else
                offset += Math.min(target * (time / (float) speed), target);
        } else {
            int speed = 3000;
            time -= WorldClient.getTimeOfSunrise();
            offset += Math.max(target - (time / (float) speed), 0F);
        }

        // Calculate weather factor and return
        offset += (float) (0.05 * world.getRainStrength(0.001F));
        offset += (float) (0.05 * world.getWeightedThunderStrength(0.001F));
        float intended = 1F - offset;
        if (interpolated) {
            float current = this.getPitch();
            this.setPitch(current + (intended - current) * 0.01F);
        } else {
            this.setPitch(intended);
        }
    }

    public static float targetOffset(WorldClient world) {
        if (world.isBloodMoon(false)) {
            return .4F;
        } else {
            return .1F;
        }
    }

    public void stopMusic() {
        if (Main.is_MITE_DS || !isLoaded) {
            return;
        }

        this.soundSystem.stop(CHANNEL_NAME);
    }

    public float getVolume() {
        return this.settings.musicVolume;
    }

    public void setDelay() {
        String ticksEnumString = MiTERenewed.TICKS_UNTIL_NEXT_SONG.get();
        MusicDelay ticksEnum = MusicDelay.fromString(ticksEnumString);
        int ticks = ticksEnum.getTicksBeforeNext();
        this.setDelay((int) (ticks * 0.75F) + random.nextInt((int) (ticks * 0.25)));
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
     * Called by {@link SoundManager} inner thread during it's reload process
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
                MiTERenewed.LOGGER.info("Registered {} | \"{}\" by {}",
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
