package com.github.jeffyjamzhd.renewed.mixins.difficulty.world;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldClient.class)
@Environment(EnvType.CLIENT)
public abstract class WorldClientMixin extends World {
    @Unique
    private float tickBuffer = 0;

    public WorldClientMixin(ISaveHandler par1ISaveHandler, String par2Str, WorldProvider par3WorldProvider, WorldSettings par4WorldSettings, Profiler par5Profiler, ILogAgent par6ILogAgent, long world_creation_time, long total_world_time) {
        super(par1ISaveHandler, par2Str, par3WorldProvider, par4WorldSettings, par5Profiler, par6ILogAgent, world_creation_time, total_world_time);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;setTotalWorldTime(J)V", ordinal = 0))
    private void setTimeAdjusted(WorldClient instance, long l) {
        long worldTime = instance.getTotalWorldTime();
        boolean isDay = this.isDaytime();
        Difficulty difficulty = Difficulty.getFromWorld(instance).orElseThrow();

        if (isDay) {
            int dayMinutes = difficulty.getParamValue(RenewedDifficulties.DAY_MINUTE_LENGTH);
            float dayFactor = dayMinutes / 10F;

            if (dayFactor < 1F) {
                instance.setTotalWorldTime(worldTime + (int)(1L / dayFactor));
            } else {
                if (this.tickBuffer >= dayFactor) {
                    this.tickBuffer -= dayFactor;
                    instance.setTotalWorldTime(worldTime + 1L);
                    return;
                }
                this.tickBuffer += 1L;
            }
        } else {
            int nightMinutes = difficulty.getParamValue(RenewedDifficulties.NIGHT_MINUTE_LENGTH);
            float nightFactor = nightMinutes / 10F;

            if (nightFactor < 1F) {
                instance.setTotalWorldTime(worldTime + (int)(1L / nightFactor));
            } else {
                if (this.tickBuffer >= nightFactor) {
                    this.tickBuffer -= nightFactor;
                    instance.setTotalWorldTime(worldTime + 1L);
                    return;
                }
                this.tickBuffer += 1L;
            }
        }
    }
}
