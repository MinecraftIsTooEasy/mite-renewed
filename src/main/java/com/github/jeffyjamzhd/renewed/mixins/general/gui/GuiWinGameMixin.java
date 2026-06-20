package com.github.jeffyjamzhd.renewed.mixins.general.gui;

import com.github.jeffyjamzhd.renewed.api.music.RenewedMusicEngine;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.GuiScreen;
import net.minecraft.GuiWinGame;
import net.minecraft.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GuiWinGame.class)
public abstract class GuiWinGameMixin extends GuiScreen {
    @Inject(method = "initGui", at = @At("HEAD"))
    private void stopPlayingMusic(CallbackInfo ci) {
        RenewedMusicEngine engine = this.mc.sndManager.mr$getMusicEngine();
        engine.stopMusic();
        this.mc.sndManager.mr$setTicksToPlay(10);
    }

    @Inject(method = "updateScreen", at = @At("HEAD"))
    private void updateMusic(CallbackInfo ci) {
        this.mc.sndManager.playRandomMusicIfReady();
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiWinGame;func_73986_b(IIF)V", ordinal = 0))
    private void unmuteSoundManager(int par1, int par2, float par3, CallbackInfo ci) {
        SoundManager.muted = false;
    }
}
