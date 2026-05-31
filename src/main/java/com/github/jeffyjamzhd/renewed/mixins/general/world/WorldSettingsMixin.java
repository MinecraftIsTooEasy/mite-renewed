package com.github.jeffyjamzhd.renewed.mixins.general.world;

import com.github.jeffyjamzhd.renewed.api.IWorldSettings;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WorldSettings.class)
@Environment(EnvType.CLIENT)
public class WorldSettingsMixin implements IWorldSettings {
    @Unique
    private Difficulty difficulty;

    @Unique
    public void mr$setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Unique
    public Difficulty mr$getDifficulty() {
        return this.difficulty;
    }
}
