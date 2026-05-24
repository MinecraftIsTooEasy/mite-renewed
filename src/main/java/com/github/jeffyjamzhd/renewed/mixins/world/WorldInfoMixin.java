package com.github.jeffyjamzhd.renewed.mixins.world;

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
}
