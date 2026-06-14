package com.github.jeffyjamzhd.renewed.api;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;

public interface IWorld {
    Difficulty mr$getDifficulty();
    boolean mr$isDifficultyLocked();
}
