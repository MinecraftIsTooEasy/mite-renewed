package com.github.jeffyjamzhd.renewed.mixins.general.core;

import com.github.jeffyjamzhd.renewed.api.ISaveFormatComparator;
import com.github.jeffyjamzhd.renewed.api.IWorldInfo;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.AnvilSaveConverter;
import net.minecraft.SaveFormatComparator;
import net.minecraft.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AnvilSaveConverter.class)
public class AnvilSaveConverterMixin {
    @ModifyArg(method = "getSaveList", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;add(Ljava/lang/Object;)Z"), index = 0)
    private Object appendDifficultyToSaveFormat(Object e, @Local WorldInfo info) {
        SaveFormatComparator save = (SaveFormatComparator) e;
        ((ISaveFormatComparator)save).mr$setDifficulty(((IWorldInfo)info).mr$getDifficulty());
        return save;
    }
}
