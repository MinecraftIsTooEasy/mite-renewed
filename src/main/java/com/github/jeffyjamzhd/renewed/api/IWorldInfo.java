package com.github.jeffyjamzhd.renewed.api;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;

public interface IWorldInfo {
    /**
     * @return World difficulty object
     */
    Difficulty mr$getDifficulty();
    void mr$setDifficulty(Difficulty difficulty);
}
