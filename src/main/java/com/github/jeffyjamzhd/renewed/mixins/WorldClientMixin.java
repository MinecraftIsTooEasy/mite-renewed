package com.github.jeffyjamzhd.renewed.mixins;

import com.github.jeffyjamzhd.renewed.util.MusicHelper;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldClient.class)
public abstract class WorldClientMixin extends World {
    @Shadow @Final private Minecraft mc;

    public WorldClientMixin(ISaveHandler par1ISaveHandler, String par2Str, WorldProvider par3WorldProvider, WorldSettings par4WorldSettings, Profiler par5Profiler, ILogAgent par6ILogAgent, long world_creation_time, long total_world_time) {
        super(par1ISaveHandler, par2Str, par3WorldProvider, par4WorldSettings, par5Profiler, par6ILogAgent, world_creation_time, total_world_time);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/Profiler;endSection()V"))
    void updateMusicPitch(CallbackInfo ci) {
        // Begin section
        this.theProfiler.endStartSection("musicPitchUpdate");

        // Get information
        this.mc.sndManager.mr$setMusicPitch(
                MusicHelper.simulateIntendedPitch(this.getAsWorldClient(), this.getAdjustedTimeOfDay()));
    }
}
