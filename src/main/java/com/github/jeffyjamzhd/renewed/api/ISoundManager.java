package com.github.jeffyjamzhd.renewed.api;

public interface ISoundManager {
    /**
     * Grabs music pitch
     * @return Current music pitch
     */
    default float mr$getMusicPitch() {
        return 0F;
    }

    /**
     * Grabs current music playing
     * @return Current music title
     */
    default String mr$getMusicTitle() {
        return "";
    }

    /**
     * Sets the ticks before playing another song
     * @param ticks Ticks
     */
    default void mr$setTicksToPlay(int ticks) {
    }

    /**
     * Sets music pitch
     * @param value Current music pitch
     */
    default void mr$setMusicPitch(float value) {
    }

    /**
     * Returns true if SoundManager is ready
     * @return SoundManager state
     */
    default boolean mr$isLoaded() {
        return false;
    }

    /**
     * Returns if music is playing
     * @return Music play state
     */
    default boolean mr$isMusicPlaying() {
        return false;
    }

    /**
     * Stops music when called
     */
    default void mr$stopMusic() {
    }
}
