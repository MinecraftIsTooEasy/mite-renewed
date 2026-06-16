package com.github.jeffyjamzhd.renewed.mixins.difficulty.entity;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.EntityAnimal;
import net.minecraft.EntityLivestock;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityLivestock.class)
public abstract class EntityLivestockMixin extends EntityAnimal {
    public EntityLivestockMixin(World par1World) {
        super(par1World);
    }

    @ModifyExpressionValue(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityLivestock;updateWellness()Z"))
    private boolean forceWellnessIfDisabled(boolean original) {
        Difficulty difficulty = Difficulty.getFromWorld(this.getWorld()).orElseThrow();
        int sickness = difficulty.getParamValue(RenewedDifficulties.ANIMAL_SICKNESS_BEHAVIOR);

        return sickness == RenewedDifficulties.ANIMAL_SICKNESS_DISABLED || original;
    }

    @ModifyReturnValue(method = "getExperienceValue", at = @At("RETURN"))
    private int giveAnimalXPifNeeded(int original) {
        Difficulty difficulty = Difficulty.getFromWorld(this.getWorld()).orElseThrow();
        return difficulty.getParamValue(RenewedDifficulties.ANIMALS_DROP_XP) ? 3 : 0;
    }
}
