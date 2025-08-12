package com.github.jeffyjamzhd.renewed.api.music;

import com.github.jeffyjamzhd.renewed.render.gui.GuiMusic;
import net.minecraft.I18n;
import shims.java.net.minecraft.text.Text;

import java.util.HashMap;
import java.util.function.Consumer;

public class TracklistRegistry {
    public static GuiMusic display;
    public static HashMap<String, Track> TRACKLIST = new HashMap<>();
    public static Consumer<Track> REGISTRY = s -> TRACKLIST.put(s.fileName, s);

    /**
     * Registers provided keys into the tracklist
     * @param tracks Set of entries
     */
    public static void registerTrackList(Track... tracks) {
        for (Track track : tracks) {
            REGISTRY.accept(track);
        }
    }

    /**
     * Registers provided key into the tracklist
     * @param track Entry
     */
    public static void registerTrack(Track track) {
        REGISTRY.accept(track);
    }

    /**
     * Shorthand for creating Track records
     * @param fileName File name
     * @param trackName Track name (literal or key)
     * @return Track record
     */
    public static Track track(String fileName, String artist, String trackName) {
        return new Track(fileName, artist, trackName);
    }

    public static Track getTrackFromFileString(String fileName) {
        if (!fileName.contains(".ogg"))
            fileName += ".ogg";
        return TRACKLIST.getOrDefault(fileName, null);
    }

    public record Track(String fileName, String artist, String trackName) {
        @Override
        public String artist() {
            return I18n.getString(artist);
        }

        @Override
        public String trackName() {
            return I18n.getString(trackName);
        }
    }
}
