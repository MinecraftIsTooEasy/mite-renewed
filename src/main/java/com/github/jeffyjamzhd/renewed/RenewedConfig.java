package com.github.jeffyjamzhd.renewed;

import com.github.jeffyjamzhd.renewed.api.music.MusicDelay;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.config.ConfigTab;
import fi.dy.masa.malilib.config.SimpleConfigs;
import fi.dy.masa.malilib.config.options.*;

import java.util.ArrayList;
import java.util.List;

public class RenewedConfig extends SimpleConfigs {
    public static final ConfigBoolean SHOW_MUSIC_DISPLAY;
    public static final ConfigEnum<MusicDelay> MUSIC_DELAY;
    public static final ConfigBoolean POOF_SOUND_ON_DEATH;
    public static final ConfigInteger HUD_PADDING;
    public static final ConfigBoolean LOOT_BEAMS;

    private static final RenewedConfig INSTANCE;
    private static final ArrayList<ConfigBase<?>> ALL;
    private static final List<ConfigTab> TABS;
    private static final ConfigTab TAB_VISUALS;
    private static final ConfigTab TAB_AUDIO;

    public static void init() {
        MiTERenewed.LOGGER.info("Initializing ManyLib config");
        INSTANCE.load();
        ConfigManager.getInstance().registerConfig(INSTANCE);
    }

    public RenewedConfig(List<ConfigBase<?>> values) {
        super("Renewed", null, values);
    }

    @Override
    public List<ConfigTab> getConfigTabs() {
        return TABS;
    }

    private static <T extends ConfigBase<?>> T add(T entry) {
        ALL.add(entry);
        return entry;
    }

    private static String of(String key) {
        return "renewed.%s".formatted(key);
    }

    static {
        ALL = new ArrayList<>();

        SHOW_MUSIC_DISPLAY      = add(new ConfigBoolean(of("showMusicDisplay"), true, of("showMusicDisplay")));
        MUSIC_DELAY             = add(new ConfigEnum<>(of("musicDelay"), MusicDelay.DEFAULT, of("musicDelay")));
        POOF_SOUND_ON_DEATH     = add(new ConfigBoolean(of("poofSoundOnDeath"), true, of("poofSoundOnDeath")));
        HUD_PADDING             = add(new ConfigInteger(of("hudPadding"), 10, 0, 24, of("hudPadding")));
        LOOT_BEAMS              = add(new ConfigBoolean(of("lootBeams"), true, of("lootBeams")));

        TABS = new ArrayList<>();
        TAB_VISUALS = new ConfigTab(of("visuals"), List.of(HUD_PADDING, SHOW_MUSIC_DISPLAY, LOOT_BEAMS));
        TAB_AUDIO = new ConfigTab(of("audio"), List.of(MUSIC_DELAY, POOF_SOUND_ON_DEATH));
        TABS.addAll(List.of(TAB_VISUALS, TAB_AUDIO));

        INSTANCE = new RenewedConfig(ALL);
    }
}
