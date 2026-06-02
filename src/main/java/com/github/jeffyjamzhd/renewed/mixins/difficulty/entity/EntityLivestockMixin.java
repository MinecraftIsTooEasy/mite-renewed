package com.github.jeffyjamzhd.renewed.mixins.difficulty.entity;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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
        Difficulty difficulty = this.worldObj.mr$getDifficulty();
        if (difficulty == null) {
            return original;
        }

        int sickness = difficulty.getParamValue(RenewedDifficulties.ANIMAL_SICKNESS_BEHAVIOR);
        return sickness == RenewedDifficulties.ANIMAL_SICKNESS_DISABLED || original;
    }
}
