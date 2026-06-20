package com.github.jeffyjamzhd.renewed.mixins.general.compat.offhand;

import com.bawnorton.mixinsquared.TargetHandler;
import com.github.jeffyjamzhd.renewed.RenewedConfig;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Gui;
import net.minecraft.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiIngame.class, priority = 1500, remap = false)
@Environment(EnvType.CLIENT)
public abstract class GuiIngameOffhandMixin extends Gui {
    @TargetHandler(
            mixin = "com.m.offhand.mixin.MixinGuiIngame",
            name = "offhand$renderOffhandSlot"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V", ordinal = 0))
    private void shiftOffhand(float par1, boolean par2, int par3, int par4, CallbackInfo ci, CallbackInfo ci2,
                              @Local(ordinal = 5) LocalIntRef slotY) {
        slotY.set(slotY.get() - RenewedConfig.HUD_PADDING.getIntegerValue());
    }
}
