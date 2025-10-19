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
    @Shadow @Final private SoundPool soundPoolMusic;
    @Shadow @Final private GameSettings options;
    @Shadow private SoundSystem sndSystem;
    @Shadow private int ticksBeforeMusic;
    @Shadow private boolean loaded;
    @Shadow private Random rand;

    @Unique private SoundPoolEntry mr$lastPlaying;
    @Unique private boolean mr$hasPlayedMagnetic = false;

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

//    @Inject(method = "<init>", at = @At("TAIL"))
//    private void tracklistRegistry(ResourceManager resource, GameSettings gs, File file, CallbackInfo ci) {
//        TracklistRegisterEvent.init();
//    }

    @Redirect(method = "loadSoundFile", at = @At(value = "INVOKE", target = "Lnet/minecraft/SoundManager;addMusic(Ljava/lang/String;)V"))
    private void noMoreMusic(SoundManager instance, String par1Str) {
    }

    @Inject(method = "playRandomMusicIfReady", at = @At("HEAD"), cancellable = true)
    private void playMusicHook(CallbackInfo ci) {
        this.musicEngine.tickEngine();
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

//    @Inject(method = "playRandomMusicIfReady", at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;play(Ljava/lang/String;)V"))
//    private void configureMusic(CallbackInfo ci, @Local(name = "var1")SoundPoolEntry entry) {
//        String name = MusicHelper.getSimpleName(entry.getSoundName());
//        SoundPoolEntry altEntry = null;
//        WorldClient world = Minecraft.getMinecraft().theWorld;
//
//        if (world != null) {
//            if (world.isBloodMoon(true) && !this.mr$hasPlayedMagnetic) {
//                altEntry = this.soundPoolMusic.getRandomSoundFromSoundPool(RESOURCE_ID + "magnetic");
//                this.mr$hasPlayedMagnetic = true;
//            } else {
//                if (name.equals("magnetic")) {
//                    altEntry = mr$rerollMusic("magnetic", entry);
//                    this.mr$hasPlayedMagnetic = false;
//                }
//            }
//        } else {
//            if (name.equals("magnetic")) {
//                altEntry = mr$rerollMusic("magnetic", entry);
//                this.mr$hasPlayedMagnetic = false;
//            }
//        }
//
//        if (altEntry != null) {
//            name = MusicHelper.getSimpleName(altEntry.getSoundName());
//            this.sndSystem.backgroundMusic("BgMusic", altEntry.getSoundUrl(), altEntry.getSoundName(), false);
//            mr$lastPlaying = altEntry;
//        } else {
//            mr$lastPlaying = entry;
//        }
//
//
//        this.ticksBeforeMusic = 500 + rand.nextInt(2500);
//        TracklistRegistry.DISPLAY.queueMusic(name);
//        this.sndSystem.setVolume("BgMusic", this.options.musicVolume);
//
//        // MiTERenewed.LOGGER.info(this.options.musicVolume);
//    }


    @Override
    public RenewedMusicEngine mr$getMusicEngine() {
        return this.musicEngine;
    }

    @Unique
    private SoundPoolEntry mr$rerollMusic(String name, SoundPoolEntry entry) {
        SoundPoolEntry newEntry = entry;
        while (MusicHelper.getSimpleName(newEntry.getSoundName()).equals(name))
            newEntry = this.soundPoolMusic.getRandomSound();
        return newEntry;
    }

    @Override
    public float mr$getMusicPitch() {
        return this.musicEngine.getPitch();
    }

    @Override
    public String mr$getMusicTitle() {
        if (this.loaded && this.mr$lastPlaying != null)
            return this.mr$lastPlaying.getSoundName();
        return ".ogg";
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
        if (this.loaded)
            this.sndSystem.stop("BgMusic");
    }

    @Override
    public void mr$setTicksToPlay(int ticks) {
        this.musicEngine.setDelay(ticks);
    }
}
