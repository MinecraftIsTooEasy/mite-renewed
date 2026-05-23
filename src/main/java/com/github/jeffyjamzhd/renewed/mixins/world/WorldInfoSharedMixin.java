package com.github.jeffyjamzhd.renewed.mixins.world;

import com.github.jeffyjamzhd.renewed.api.IWorldInfoShared;
import com.github.jeffyjamzhd.renewed.api.IWorldSettings;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyParameter;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyProvider;
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

    @Override
    public Difficulty mr$getDifficulty() {
        return this.difficulty;
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
        }
    }

    @Inject(method = "updateTagCompound", at = @At("TAIL"))
    private void addDifficultyToTagCompound(NBTTagCompound worldTag, NBTTagCompound playerTag, CallbackInfo ci) {
        NBTTagCompound difficultyTag = new NBTTagCompound();
        NBTTagList parameterTagList = new NBTTagList();

        worldTag.setCompoundTag("RenewedDifficulty", difficultyTag);
        difficultyTag.setString("Preset", difficulty.id.toString());
        difficultyTag.setTag("Parameters", parameterTagList);

        for (DifficultyParameter<?> parameter : DifficultyProvider.identifierToParam.values()) {
            NBTTagCompound parameterTag = new NBTTagCompound();
            parameter.writeNBT(parameterTag, difficulty.getParamValue(parameter.id));
            parameterTagList.appendTag(parameterTag);
        }
    }
}
