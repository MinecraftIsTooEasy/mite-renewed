package com.github.jeffyjamzhd.renewed.api;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;

public interface IPacket1Login {
    void mr$setDifficulty(Difficulty difficulty);
    Difficulty mr$getDifficulty();
}
