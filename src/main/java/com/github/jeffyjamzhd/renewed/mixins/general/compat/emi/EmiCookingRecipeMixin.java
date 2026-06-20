package com.github.jeffyjamzhd.renewed.mixins.general.compat.emi;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.emi.emi.recipe.EmiCookingRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(value = EmiCookingRecipe.class, remap = false)
public class EmiCookingRecipeMixin {
    @ModifyExpressionValue(method = "addWidgets", at = @At(value = "FIELD", target = "Ldev/emi/emi/recipe/EmiCookingRecipe;xp:I", opcode = Opcodes.GETFIELD))
    private int scaleExperience(int original) {
        Minecraft minecraft = Minecraft.getMinecraft();
        Difficulty clientDifficulty = Difficulty.getFromWorld(minecraft.theWorld).orElseThrow();
        float scalar = clientDifficulty.getParamValue(RenewedDifficulties.SMELTING_EXPERIENCE_FACTOR);

        return Math.round(original * scalar);
    }
}
