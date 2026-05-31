package com.github.jeffyjamzhd.renewed.mixins.difficulty.entity;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.EntityLivingBase;
import net.minecraft.EntityPlayer;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLivingBase {
    @Shadow
    public abstract int getExperienceLevel();

    public EntityPlayerMixin(World par1World) {
        super(par1World);
    }

    @Inject(method = "getHealthLimit()F", at = @At("HEAD"), cancellable = true)
    private void getHealthLimit(CallbackInfoReturnable<Float> cir) {
        Difficulty difficulty = this.getWorld().mr$getDifficulty();
        if (difficulty == null) {
            return;
        }

        int experienceLevel = this.getExperienceLevel();
        int levelsPer = difficulty.getParamValue(RenewedDifficulties.LEVELS_NEEDED_FOR_STAT_UP);
        int minimum = difficulty.getParamValue(RenewedDifficulties.MINIMUM_HEALTH) * 2;
        int maximum = difficulty.getParamValue(RenewedDifficulties.MAXIMUM_HEALTH) * 2;

        float upper = Math.min(minimum + experienceLevel / levelsPer * 2, maximum);
        float value = Math.max(upper, minimum);
        cir.setReturnValue(value);
    }

    @ModifyReturnValue(method = "getDamageVsBlock", at = @At("RETURN"))
    public float miningSpeedFactor(float original) {
        Difficulty difficulty = this.getWorld().mr$getDifficulty();
        if (difficulty == null) {
            return original;
        }

        float factor = difficulty.getParamValue(RenewedDifficulties.MINING_FACTOR);
        return original * factor;
    }
}
