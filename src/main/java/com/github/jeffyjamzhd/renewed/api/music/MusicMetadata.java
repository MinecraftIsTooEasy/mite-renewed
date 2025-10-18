package com.github.jeffyjamzhd.renewed.api.music;

public record MusicMetadata(String title, String artist) {
    @Override
    public String title() {
        return title != null ? title : "Unknown Artist";
    }

    @Override
    public String artist() {
        return artist != null ? artist : "Unknown Track";
    }
}
