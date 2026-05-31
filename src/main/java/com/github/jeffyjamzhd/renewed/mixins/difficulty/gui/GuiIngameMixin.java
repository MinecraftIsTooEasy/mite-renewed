package com.github.jeffyjamzhd.renewed.mixins.difficulty.gui;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.GuiIngame;
import net.minecraft.MathHelper;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/PlayerControllerMP;shouldDrawHUD()Z", ordinal = 2))
    private void moveItemDisplayIfHasLotsOfHeartsOrHunger(float par1, boolean par2, int par3, int par4, CallbackInfo ci, @Local(ordinal = 14) LocalIntRef y) {
        float health = this.mc.thePlayer.getMaxHealth();
        int nutrition = this.mc.thePlayer.getNutritionLimit();
        if ((health > 10F || nutrition > 10) && this.mc.playerController.shouldDrawHUD()) {
            y.set(y.get() - 14);
        }
    }

    @ModifyConstant(method = "func_110327_a", constant = @Constant(intValue = 10),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 1),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 2)
            ) )
    private int drawActualFoodAmount(int constant) {
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
    private int wrapHungerHorizontal(int _x, @Local(ordinal = 13) int iteration, @Local(ordinal = 6) int right) {
        return right - (iteration % 10) * 8 - 9;
    }

    @ModifyArg(method = "func_110327_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiIngame;drawTexturedModalRect(IIIIII)V"), index = 1,
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 3),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/Profiler;endSection()V")
            ))
    private int setAirHeightBasedOnFoodHeight(int _y, @Local(ordinal = 7) int bottom) {
        int nutrition = MathHelper.ceiling_float_int((float) (this.mc.thePlayer.getNutritionLimit() / 2) / 10F);
        int processed = Math.max(10 - (nutrition - 2), 3);
        return bottom - (nutrition - 1) * processed - 10;
    }
}
