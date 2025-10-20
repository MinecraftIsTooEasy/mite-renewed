package com.github.jeffyjamzhd.renewed.mixins.sound;

import com.github.jeffyjamzhd.renewed.api.ISoundManager;
import com.github.jeffyjamzhd.renewed.api.music.RenewedMusicEngine;
import com.github.jeffyjamzhd.renewed.util.MusicHelper;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulscode.sound.SoundSystem;

import java.io.File;
import java.util.Random;

@Mixin(SoundManager.class)
public class SoundManagerMixin implements ISoundManager {
    @Shadow @Final private GameSettings options;
    @Shadow private boolean loaded;
    @Shadow private Random rand;
    @Unique private RenewedMusicEngine musicEngine;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setTicksInit(
            ResourceManager resourceManager,
            GameSettings gameSettings,
            File assets,
            CallbackInfo ci) {
        this.musicEngine = new RenewedMusicEngine(resourceManager, this.options, this.rand);
        ((ReloadableResourceManager) resourceManager).registerReloadListener(this.musicEngine);
    }

    @Redirect(method = "loadSoundFile", at = @At(value = "INVOKE", target = "Lnet/minecraft/SoundManager;addMusic(Ljava/lang/String;)V"))
    private void noMoreMusic(SoundManager instance, String par1Str) {
    }

    @Inject(method = "playRandomMusicIfReady", at = @At("HEAD"), cancellable = true)
    private void playMusicHook(CallbackInfo ci) {
        this.musicEngine.tickEngine(null, null);
        ci.cancel();
    }

    @Inject(method = "onSoundOptionsChanged", at = @At("HEAD"), cancellable = true)
    private void soundOptionsChangedHook(CallbackInfo ci) {
        this.musicEngine.onSoundOptionsChanged();
        ci.cancel();
    }

    @Inject(method = "func_130080_a", at = @At("RETURN"))
    private static void provideSoundSystemHook(
            SoundManager soundManager,
            SoundSystem soundSystem,
            CallbackInfoReturnable<SoundSystem> cir) {
        soundManager.mr$getMusicEngine().provideSoundSystemReference(soundSystem);
    }

    @Override
    public RenewedMusicEngine mr$getMusicEngine() {
        return this.musicEngine;
    }

    @Override
    public void mr$setMusicPitch(float value) {
        this.musicEngine.setPitch(value);
    }

    @Override
    public boolean mr$isLoaded() {
        return this.loaded;
    }

    @Override
    public boolean mr$isMusicPlaying() {
        return this.musicEngine.isPlaying();
    }

    @Override
    public void mr$stopMusic() {
        this.musicEngine.stopMusic();
    }

    @Override
    public void mr$setTicksToPlay(int ticks) {
        this.musicEngine.setDelay(ticks);
    }
}
