package com.github.jeffyjamzhd.renewed.mixins.difficulty.entity;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.*;
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
        Difficulty difficulty = Difficulty.getFromWorld(this.getWorld()).orElseThrow();

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
        Difficulty difficulty = Difficulty.getFromWorld(this.getWorld()).orElseThrow();
        float factor = difficulty.getParamValue(RenewedDifficulties.MINING_FACTOR);
        return original * factor;
    }

    @Inject(method = "calcRawMeleeDamageVs(Lnet/minecraft/Entity;ZZ)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityPlayer;getHeldItemStack()Lnet/minecraft/ItemStack;", ordinal = 1))
    private void modifyAttackDamagePercent(Entity target, boolean critical,
                                           boolean suspended_in_liquid, CallbackInfoReturnable<Float> cir,
                                           @Local(ordinal = 0) LocalFloatRef damage) {
        Difficulty difficulty = Difficulty.getFromWorld(this.getWorld()).orElseThrow();
        float factor = difficulty.getParamValue(RenewedDifficulties.PLAYER_DAMAGE_FACTOR);
        damage.set(damage.get() * factor);
    }

    @ModifyReturnValue(method = "getLevelModifier(Lnet/minecraft/EnumLevelBonus;)F", at = @At("RETURN"))
    private float scaleLevelModifiers(float original,
                                             @Local(argsOnly = true) EnumLevelBonus kind) {
        Difficulty difficulty = Difficulty.getFromWorld(this.getWorld()).orElseThrow();
        int level = this.getExperienceLevel();

        return switch (kind) {
            case CRAFTING -> level * difficulty.getParamValue(RenewedDifficulties.CRAFTING_BONUS_PER_LEVEL);
            case HARVESTING -> level * difficulty.getParamValue(RenewedDifficulties.HARVESTING_BONUS_PER_LEVEL);
            case MELEE_DAMAGE -> level * difficulty.getParamValue(RenewedDifficulties.MELEE_DAMAGE_BONUS_PER_LEVEL);
        };
    }
}
