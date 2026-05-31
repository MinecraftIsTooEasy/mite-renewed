package com.github.jeffyjamzhd.renewed.mixins.difficulty.entity;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import net.minecraft.EntityLivingBase;
import net.minecraft.EntityMob;
import net.minecraft.EntitySkeleton;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntitySkeleton.class)
public abstract class EntitySkeletonMixin extends EntityMob {
    public EntitySkeletonMixin(World par1World) {
        super(par1World);
    }

    @Inject(method = "attackEntityWithRangedAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityArrow;setDamage(D)V", ordinal = 0))
    private void scaleMobDamageForArrow(EntityLivingBase par1EntityLivingBase, float par2, CallbackInfo ci, @Local LocalDoubleRef damage) {
        Difficulty difficulty = Difficulty.getFromWorld(this.getWorld()).orElseThrow();
        double factor = (double) difficulty.getParamValue(RenewedDifficulties.MOB_DAMAGE_FACTOR);
        damage.set(damage.get() * factor);
    }
}
