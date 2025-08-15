package com.github.jeffyjamzhd.renewed.mixins.sound;

import com.github.jeffyjamzhd.renewed.api.ISoundManager;
import com.github.jeffyjamzhd.renewed.api.event.TracklistRegisterEvent;
import com.github.jeffyjamzhd.renewed.api.registry.TracklistRegistry;
import com.github.jeffyjamzhd.renewed.util.MusicHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulscode.sound.SoundSystem;

import java.io.File;
import java.util.Random;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.RESOURCE_ID;

@Mixin(SoundManager.class)
public class SoundManagerMixin implements ISoundManager {
    @Shadow private SoundSystem sndSystem;
    @Shadow private int ticksBeforeMusic;
    @Shadow private boolean loaded;
    @Shadow private Random rand;
    @Shadow @Final private SoundPool soundPoolMusic;

    @Unique private SoundPoolEntry mr$lastPlaying;
    @Unique private boolean mr$hasPlayedMagnetic = false;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setTicksInit(ResourceManager par1ResourceManager, GameSettings par2GameSettings, File par3File, CallbackInfo ci) {
        this.ticksBeforeMusic = 0;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void tracklistRegistry(ResourceManager resource, GameSettings gs, File file, CallbackInfo ci) {
        TracklistRegisterEvent.init();
    }

    @Inject(method = "playRandomMusicIfReady", at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;play(Ljava/lang/String;)V"))
    private void configureMusic(CallbackInfo ci, @Local(name = "var1")SoundPoolEntry entry) {
        String name = MusicHelper.getSimpleName(entry.getSoundName());
        SoundPoolEntry altEntry = null;
        WorldClient world = Minecraft.getMinecraft().theWorld;

        if (world != null) {
            if (world.isBloodMoon(true) && !this.mr$hasPlayedMagnetic) {
                altEntry = this.soundPoolMusic.getRandomSoundFromSoundPool(RESOURCE_ID + "magnetic");
                this.mr$hasPlayedMagnetic = true;
            } else {
                if (name.equals("magnetic")) {
                    altEntry = rerollMusic("magnetic", entry);
                    this.mr$hasPlayedMagnetic = false;
                }
            }
        } else {
            if (name.equals("magnetic")) {
                altEntry = rerollMusic("magnetic", entry);
                this.mr$hasPlayedMagnetic = false;
            }
        }

        if (altEntry != null) {
            name = MusicHelper.getSimpleName(altEntry.getSoundName());
            this.sndSystem.backgroundMusic("BgMusic", altEntry.getSoundUrl(), altEntry.getSoundName(), false);
            mr$lastPlaying = altEntry;
        } else {
            mr$lastPlaying = entry;
        }


        this.ticksBeforeMusic = 500 + rand.nextInt(2500);
        TracklistRegistry.DISPLAY.queueMusic(name);
    }

    @Unique
    private SoundPoolEntry rerollMusic(String name, SoundPoolEntry entry) {
        SoundPoolEntry newEntry = entry;
        while (MusicHelper.getSimpleName(newEntry.getSoundName()).equals(name))
            newEntry = this.soundPoolMusic.getRandomSound();
        return newEntry;
    }

    @Override
    public float mr$getMusicPitch() {
        if (this.loaded)
            return this.sndSystem.getPitch("BgMusic");
        return 0F;
    }

    @Override
    public String mr$getMusicTitle() {
        if (this.loaded && this.mr$lastPlaying != null)
            return this.mr$lastPlaying.getSoundName();
        return ".ogg";
    }

    @Override
    public void mr$setMusicPitch(float value) {
        if (this.loaded)
            this.sndSystem.setPitch("BgMusic", value);
    }

    @Override
    public boolean mr$isLoaded() {
        return this.loaded;
    }

    @Override
    public boolean mr$isMusicPlaying() {
        if (this.loaded)
            return this.sndSystem.playing("BgMusic");
        return false;
    }

    @Override
    public void mr$stopMusic() {
        if (this.loaded)
            this.sndSystem.stop("BgMusic");
    }

    @Override
    public void mr$setTicksToPlay(int ticks) {
        ticksBeforeMusic = ticks;
    }
}
