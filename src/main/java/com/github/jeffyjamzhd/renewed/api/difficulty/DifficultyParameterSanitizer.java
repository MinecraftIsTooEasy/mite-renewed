package com.github.jeffyjamzhd.renewed.api.difficulty;

@FunctionalInterface
public interface DifficultyParameterSanitizer<T> {
    /**
     * Returns true to signify that provided difficulty does not have an
     * illegal/invalid configuration
     */
    T sanitize(Difficulty difficulty, T value);
}
