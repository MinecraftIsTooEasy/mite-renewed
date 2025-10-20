package com.github.jeffyjamzhd.renewed.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ItemStack;
import net.minecraft.Minecraft;
import net.minecraft.PlayerControllerMP;
import net.minecraft.SoundManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin {
    @Shadow @Final private Minecraft mc;

    @ModifyReturnValue(method = "isItemStackEligibleForAUM", at = @At("RETURN"))
    public boolean aumExtraCondition(
            boolean original, @Local(argsOnly = true) ItemStack stack) {
        return original || (stack != null && stack.getItem().mr$isAutoUse(stack));
    }

    @Redirect(method = "updateController", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/SoundManager;playRandomMusicIfReady()V"))
    private void removeUpdateForVanillaMusicEngine(SoundManager instance) {
    }

    @Inject(method = "updateController", at = @At("HEAD"))
    private void updateMusicEngine(CallbackInfo ci) {
        this.mc.sndManager
                .mr$getMusicEngine()
                .tickEngine(this.mc.theWorld, this.mc.thePlayer);
    }
}
