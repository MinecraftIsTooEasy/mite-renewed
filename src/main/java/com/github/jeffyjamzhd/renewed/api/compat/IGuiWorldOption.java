package com.github.jeffyjamzhd.renewed.api.compat;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;

public interface IGuiWorldOption {
    default void mr$updateButtonUsability(boolean mode) {}
    default void mr$updateButtonText() {}
    default void mr$assignOrSendDifficulty(Difficulty difficulty) {}
}
