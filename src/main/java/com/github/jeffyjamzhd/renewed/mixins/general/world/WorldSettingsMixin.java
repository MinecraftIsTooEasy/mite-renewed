package com.github.jeffyjamzhd.renewed.mixins.general.world;

import com.github.jeffyjamzhd.renewed.api.IWorldSettings;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WorldSettings.class)
public class WorldSettingsMixin implements IWorldSettings {
    @Unique
    private Difficulty difficulty;
    @Unique
    private boolean difficultyLocked;

    @Unique
    public void mr$setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Unique
    public Difficulty mr$getDifficulty() {
        return this.difficulty;
    }

    @Override
    public boolean mr$isDifficultyLocked() {
        return this.difficultyLocked;
    }

    @Override
    public void mr$setDifficultyLocked(boolean locked) {
        this.difficultyLocked = locked;
    }
}
