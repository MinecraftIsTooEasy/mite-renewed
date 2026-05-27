package com.github.jeffyjamzhd.renewed.mixins.core;

import com.github.jeffyjamzhd.renewed.api.ISaveFormatComparator;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import net.minecraft.SaveFormatComparator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SaveFormatComparator.class)
public class SaveFormatComparatorMixin implements ISaveFormatComparator {
    @Unique
    private Difficulty difficulty;

    @Override
    public void mr$setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public Difficulty mr$getDifficulty() {
        return this.difficulty;
    }
}
