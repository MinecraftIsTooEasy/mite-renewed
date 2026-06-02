package com.github.jeffyjamzhd.renewed.mixins.difficulty.world;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(World.class)
public abstract class WorldMixin {
    @Unique
    private float tickBuffer = 0;

    @Shadow
    public abstract boolean isDaytime();

    @Shadow
    public abstract int getDayOfWorld();

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
            }
        }
    }

    @ModifyReturnValue(method = "canPrecipitate", at = @At("RETURN"))
    private boolean blockPrecipitation(boolean original) {
        Difficulty difficulty = Difficulty.getFromWorld((World) (Object) this).orElse(RenewedDifficulties.EXTREME);
        int gracePeriod = difficulty.getParamValue(RenewedDifficulties.WEATHER_GRACE_PERIOD);
        boolean noSkip = this.getDayOfWorld() > gracePeriod;
        return original && noSkip;
    }

    @ModifyReturnValue(method = "getWeatherEventsForToday", at = @At(value = "RETURN", ordinal = 0))
    private List returnEmptyList(List original) {
        // Normally this would return a null value, which causes a crash
        // in the weather event methods if you're in the overworld.
        // So instead, return an empty list!

        return List.of();
    }
}
