package com.github.jeffyjamzhd.renewed.mixins.general.world;

import com.github.jeffyjamzhd.renewed.api.IWorldInfoShared;
import com.github.jeffyjamzhd.renewed.api.IWorldSettings;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldInfoShared.class)
public class WorldInfoSharedMixin implements IWorldInfoShared {
    @Unique
    public Difficulty difficulty;

    @Unique
    @Override
    public Difficulty mr$getDifficulty() {
        return this.difficulty;
    }

    @Override
    public void mr$setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Inject(method = "<init>(Lnet/minecraft/WorldSettings;Ljava/lang/String;)V", at = @At("TAIL"))
    private void setDifficultyFromSettings(WorldSettings world_settings, String level_name, CallbackInfo ci) {
        this.difficulty = ((IWorldSettings)world_settings).mr$getDifficulty();
    }

    @Inject(method = "<init>(Lnet/minecraft/NBTTagCompound;)V", at = @At("TAIL"))
    private void createOrGetDifficultyObject(NBTTagCompound tag, CallbackInfo ci) {
        if (tag.hasKey("RenewedDifficulty")) {
            NBTTagCompound difficultyTag = tag.getCompoundTag("RenewedDifficulty");
            this.difficulty = Difficulty.createFromTagCompound(difficultyTag);
        } else {
            // Assign the default if none exists (legacy world)
            this.difficulty = RenewedDifficulties.EXTREME;
        }
    }

    @Inject(method = "updateTagCompound", at = @At("TAIL"))
    private void addDifficultyToTagCompound(NBTTagCompound worldTag, NBTTagCompound playerTag, CallbackInfo ci) {
        worldTag.setCompoundTag("RenewedDifficulty", difficulty.asTagCompound());
    }
}
