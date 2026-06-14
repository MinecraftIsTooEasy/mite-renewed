package com.github.jeffyjamzhd.renewed.mixins.general.world;

import com.github.jeffyjamzhd.renewed.api.IWorldInfo;
import com.github.jeffyjamzhd.renewed.api.IWorldInfoShared;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import net.minecraft.WorldInfo;
import net.minecraft.WorldInfoShared;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldInfo.class)
public class WorldInfoMixin implements IWorldInfo {
    @Shadow
    private WorldInfoShared shared;

    @Override
    public Difficulty mr$getDifficulty() {
        return ((IWorldInfoShared) this.shared).mr$getDifficulty();
    }

    @Override
    public void mr$setDifficulty(Difficulty difficulty) {
        ((IWorldInfoShared)this.shared).mr$setDifficulty(difficulty);
    }

    @Override
    public boolean mr$isDifficultyLocked() {
        return ((IWorldInfoShared)this.shared).mr$isDifficultyLocked();
    }

    @Override
    public void mr$setDifficultyLocked(boolean locked) {
        ((IWorldInfoShared)this.shared).mr$setDifficultyLocked(locked);
    }
}
