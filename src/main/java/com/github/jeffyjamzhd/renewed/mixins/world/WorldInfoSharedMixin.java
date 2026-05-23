package com.github.jeffyjamzhd.renewed.mixins.world;

import com.github.jeffyjamzhd.renewed.api.IWorldInfoShared;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import net.minecraft.WorldInfoShared;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(WorldInfoShared.class)
public class WorldInfoSharedMixin implements IWorldInfoShared {
    @Unique
    public Difficulty difficulty;

    @Override
    public Difficulty mr$getDifficulty() {
        return this.difficulty;
    }
    //@Inject()

}
