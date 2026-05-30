package com.github.jeffyjamzhd.renewed.mixins.gui;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.GuiIngame;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GuiIngame.class)
@Environment(EnvType.CLIENT)
public class GuiIngameMixin {
    @Shadow
    @Final
    private Minecraft mc;

    @ModifyExpressionValue(method = "func_110327_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/AttributeInstance;getAttributeValue()D"))
    private double getActualMaxHealth(double original) {
        return this.mc.thePlayer.getMaxHealth();
    }

    @ModifyConstant(method = "func_110327_a", constant = @Constant(intValue = 10),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 1),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 2)
            ) )
    private int drawFoodActualAmount(int constant) {
        return this.mc.thePlayer.getNutritionLimit();
    }

    @ModifyArg(method = "func_110327_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiIngame;drawTexturedModalRect(IIIIII)V"), index = 1,
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 1),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 2)
            ))
    private int moveHungerUp(int y, @Local(ordinal = 13) int iteration) {
        return y - (9 * (iteration / 10));
    }

    @ModifyArg(method = "func_110327_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiIngame;drawTexturedModalRect(IIIIII)V"), index = 0,
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 1),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 2)
            ))
    private int wrapHungerHorizontal(int drawX, @Local(ordinal = 13) int iteration, @Local(ordinal = 16) int x, @Local(ordinal = 6) int right) {
        return right - (iteration % 10) * 8 - 9;
    }
}
