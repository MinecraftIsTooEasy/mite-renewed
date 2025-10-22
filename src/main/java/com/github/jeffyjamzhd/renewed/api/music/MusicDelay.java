package com.github.jeffyjamzhd.renewed.api.music;

import java.util.Arrays;
import java.util.Optional;

public enum MusicDelay {
    RARE("rare", 20 * 60 * 24),
    VANILLA("vanilla", 20 * 60 * 10),
    DEFAULT("default", 20 * 60 * 3),
    CONSTANT("constant", 20);

    private final String name;
    private final int ticksBeforeNext;

    MusicDelay(String name, int delay) {
        this.name = name;
        this.ticksBeforeNext = delay;
    }

    public int getTicksBeforeNext() {
        return this.ticksBeforeNext;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static MusicDelay fromString(String string) {
        Optional<MusicDelay> value = Arrays.stream(MusicDelay.values())
                .filter(musicDelay -> musicDelay.toString().equals(string))
                .findFirst();
        return value.orElse(MusicDelay.DEFAULT);
    }
}
