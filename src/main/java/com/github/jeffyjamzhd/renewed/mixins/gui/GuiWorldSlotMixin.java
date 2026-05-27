package com.github.jeffyjamzhd.renewed.mixins.gui;

import com.github.jeffyjamzhd.renewed.api.ISaveFormatComparator;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiWorldSlot.class)
@Environment(EnvType.CLIENT)
public class GuiWorldSlotMixin {
    @Inject(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/SaveFormatComparator;areSkillsEnabled()Z", ordinal = 0))
    private void appendToWorldName(
            int par1, int par2, int par3, int par4, Tessellator par5Tessellator, CallbackInfo ci,
            @Local(ordinal = 2) LocalRef<String> worldInfo, @Local SaveFormatComparator info) {
        Difficulty difficulty = ((ISaveFormatComparator) info).mr$getDifficulty();
        if (difficulty != null) {
            worldInfo.set(worldInfo.get() + ", " + difficulty.getLocalizedName());
        }
    }

}
