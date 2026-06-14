package com.github.jeffyjamzhd.renewed.api.compat;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;

public interface IGuiWorldOption {
    default void mr$enableEditOptions() {}
    default void mr$attemptAssigningCustomDifficulty(Difficulty difficulty) {}
}
