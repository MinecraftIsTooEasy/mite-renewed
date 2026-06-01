package com.github.jeffyjamzhd.renewed.mixins.difficulty.world;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(World.class)
public abstract class WorldMixin {
    @Unique
    private float tickBuffer = 0;

    @Shadow
    public abstract boolean isDaytime();

    @Redirect(method = "advanceTotalWorldTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;setTotalWorldTime(J)V"))
    private void setTimeAdjusted(World instance, long par1, @Local(argsOnly = true) long ticks) {
        long worldTime = instance.getTotalWorldTime();
        boolean isDay = this.isDaytime();
        Difficulty difficulty = Difficulty.getFromWorld(instance).orElseThrow();

        if (isDay) {
            int dayMinutes = difficulty.getParamValue(RenewedDifficulties.DAY_MINUTE_LENGTH);
            float dayFactor = dayMinutes / 10F;

            if (dayFactor < 1F) {
                instance.setTotalWorldTime(worldTime + (int)(ticks / dayFactor));
            } else {
                if (this.tickBuffer >= dayFactor) {
                    this.tickBuffer -= dayFactor;
                    instance.setTotalWorldTime(worldTime + ticks);
                    return;
                }
                this.tickBuffer += ticks;
                //instance.setTotalWorldTime(worldTime + ticks);
            }
        } else {
            int nightMinutes = difficulty.getParamValue(RenewedDifficulties.NIGHT_MINUTE_LENGTH);
            float nightFactor = nightMinutes / 10F;

            if (nightFactor < 1F) {
                instance.setTotalWorldTime(worldTime + (int)(ticks / nightFactor));
            } else {
                if (this.tickBuffer >= nightFactor) {
                    this.tickBuffer -= nightFactor;
                    instance.setTotalWorldTime(worldTime + ticks);
                    return;
                }
                this.tickBuffer += ticks;
                //instance.setTotalWorldTime(worldTime + ticks);
            }
        }
    }
}
